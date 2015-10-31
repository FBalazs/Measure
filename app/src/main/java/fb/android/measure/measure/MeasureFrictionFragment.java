package fb.android.measure.measure;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;

import java.util.Iterator;
import java.util.LinkedList;

import fb.android.measure.R;
import fb.android.measure.SensorFragment;
import fb.android.measure.settings.Settings;
import fb.android.measure.utils.ChartUtils;

public class MeasureFrictionFragment extends SensorFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_measure_friction;
    }

    private LineChart chart;
    private CheckBox cbA, cbAvgA;
    private TextView tvAccel, tvFriction, tvSamplesPerSec, tvState;
    private ToggleButton btnStart;

    private float gravity;
    private final int BUFFLENGTH = 5;
    private final int DATALENGTH = 32768;

    private long startTime, currentTime, lastUiUpdateTime;
    private float a, avgA;

    private int currentBuffLength;
    private LinkedList<Float> buffA = new LinkedList<>();

    private long samples;
    private int currentDataLength;
    private LinkedList<Float> dataAX = new LinkedList<>(), dataAY = new LinkedList<>(), dataAZ = new LinkedList<>(),
            dataA = new LinkedList<>(), dataAvgA = new LinkedList<>();
    private LinkedList<String> xVals = new LinkedList<>();
    private LineData chartData = new LineData();
    private int dataSetAX, dataSetAY, dataSetAZ, dataSetA, dataSetAvgA;

    private final int STATE_STOPPED = 0,
                    STATE_STARTED = 1,
                    STATE_STILL = 2,
                    STATE_PUSH = 3,
                    STATE_SLIDESTART = 4,
                    STATE_SLIDE = 5;
    private final String[] STATESTRING = new String[]{"STOPPED", "STARTED", "STILL", "PUSH", "SLIDESTART", "SLIDE"};
    private int state;

    private double stillAccelSumm;
    private float stillAccelAvg;

    private double slideAccelSumm;
    private long slideAccelSamples;

    private float friction;

    private void initChart() {
        ChartUtils.setDefaults(chart);
        chart.setDescription("");
//        chart.setNoDataTextDescription(getResources().getString(R.string.no_data));
    }

    private void refreshChart() {
        chartData = new LineData(xVals);
        dataSetAX = ChartUtils.addDataSet(chartData, "ax", Color.RED);
        dataSetAY = ChartUtils.addDataSet(chartData, "ay", Color.GREEN);
        dataSetAZ = ChartUtils.addDataSet(chartData, "az", Color.BLUE);
        dataSetA = ChartUtils.addDataSet(chartData, "a", Color.CYAN);
        dataSetAvgA = ChartUtils.addDataSet(chartData, "avgA", Color.BLUE);

        Iterator<Float> itAX = dataAX.iterator();
        Iterator<Float> itAY = dataAY.iterator();
        Iterator<Float> itAZ = dataAZ.iterator();
        Iterator<Float> itA = dataA.iterator();
        Iterator<Float> itAvgA = dataAvgA.iterator();
        for(int i = 0; itA.hasNext(); ++i) {
            chartData.addEntry(new Entry(itAX.next(), i), dataSetAX);
            chartData.addEntry(new Entry(itAY.next(), i), dataSetAY);
            chartData.addEntry(new Entry(itAZ.next(), i), dataSetAZ);
            chartData.addEntry(new Entry(itA.next(), i), dataSetA);
            chartData.addEntry(new Entry(itAvgA.next(), i), dataSetAvgA);
        }

        chartData.getDataSetByIndex(dataSetA).setVisible(cbA.isChecked());
        chartData.getDataSetByIndex(dataSetAvgA).setVisible(cbAvgA.isChecked());

        chart.setData(chartData);
        chart.invalidate();
    }

    private void refreshTexts() {
        tvAccel.setText(getString(R.string.accel_equal, avgA));
        tvFriction.setText(getString(R.string.friction_equal, friction));
        tvSamplesPerSec.setText(getString(R.string.samplespersec_equal, samples * 1000f / (currentTime - startTime)));
        tvState.setText(getString(R.string.state_equal, STATESTRING[state]));
    }

    private void onCbChanged() {
        chartData.getDataSetByIndex(dataSetA).setVisible(cbA.isChecked());
        chartData.getDataSetByIndex(dataSetAvgA).setVisible(cbAvgA.isChecked());

        chart.setData(chartData);
        chart.invalidate();
    }

    private void onBtnStartChange(boolean checked) {
        if(checked) {
            if(!isListening())
                startListen(Sensor.TYPE_ACCELEROMETER, Settings.getAccelSamplingPeriodUs(getContext()));

            startTime = System.currentTimeMillis();

            currentBuffLength = 0;
            buffA.clear();

            samples = 0;
            currentDataLength = 0;
            dataAX.clear();
            dataAY.clear();
            dataAZ.clear();
            dataA.clear();
            dataAvgA.clear();

            xVals.clear();

            state = STATE_STARTED;
            stillAccelSumm = 0;

            slideAccelSumm = 0;
            slideAccelSamples = 0;

            friction = 0;
        } else {
            if(isListening())
                stopListen();
            state = STATE_STOPPED;
            Log.d(getClass().getName(), "currentDataLength="+currentDataLength);
            refreshTexts();
            refreshChart();
        }
    }

    @Override
    public void initFragment(Activity activity, View view) {
        chart = (LineChart)view.findViewById(R.id.fragment_measure_friction_chart);

        cbA = (CheckBox)view.findViewById(R.id.fragment_measure_friction_cb_chart_a_enable);
        cbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });
        cbAvgA = (CheckBox)view.findViewById(R.id.fragment_measure_friction_cb_chart_avga_enable);
        cbAvgA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });

        tvAccel = (TextView)view.findViewById(R.id.fragment_measure_friction_tv_accel);
        tvFriction = (TextView)view.findViewById(R.id.fragment_measure_friction_tv_friction);
        tvSamplesPerSec = (TextView)view.findViewById(R.id.fragment_measure_friction_tv_samplespersec);
        tvState = (TextView)view.findViewById(R.id.fragment_measure_friction_tv_state);

        btnStart = (ToggleButton)view.findViewById(R.id.fragment_measure_friction_btn_start_stop);
        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onBtnStartChange(isChecked);
            }
        });

        gravity = Settings.getCalibratedGravity(activity);

        initChart();

        onBtnStartChange(btnStart.isChecked());
        refreshTexts();
    }

    @Override
    public void onSensorData(float[] v) {
        currentTime = System.currentTimeMillis();

        float ax = v[0], ay = v[1], az = v[2];

        a = (float)Math.sqrt(ax*ax + ay*ay);

        while(BUFFLENGTH <= currentBuffLength) {
            buffA.removeFirst();
            --currentBuffLength;
        }
        buffA.addLast(a);
        ++currentBuffLength;

        double sum = 0;
        for(Float f : buffA)
            sum += f;
        avgA = (float)(sum/currentBuffLength);

        while(DATALENGTH <= currentDataLength) {
            dataAX.removeFirst();
            dataAY.removeFirst();
            dataAZ.removeFirst();
            dataA.removeFirst();
            dataAvgA.removeFirst();
            xVals.removeFirst();
            --currentDataLength;
        }
        dataAX.addLast(ax);
        dataAY.addLast(ay);
        dataAZ.addLast(az);
        dataA.addLast(a);
        dataAvgA.addLast(avgA);
        xVals.addLast("" + (currentTime - startTime) / 1000d);
        ++samples;
        ++currentDataLength;

        switch (state) {
            case STATE_STARTED:
                stillAccelSumm += avgA;
                if(currentTime-startTime > 500) { // TODO wait time
                    stillAccelAvg = (float)(stillAccelSumm/samples);
                    state = STATE_STILL;
                }
                break;
            case STATE_STILL:
                if(2.0 < avgA) // TODO push accel threshold
                    state = STATE_PUSH;
                break;
            case STATE_PUSH:
                if(avgA < stillAccelAvg*1.5)
                    state = STATE_SLIDESTART;
                break;
            case STATE_SLIDESTART:
                if(stillAccelAvg*1.5 < avgA)
                    state = STATE_SLIDE;
                break;
            case STATE_SLIDE:
                slideAccelSumm += avgA;
                ++slideAccelSamples;
                if(avgA < stillAccelAvg*1.5) {
                    friction = (float)(slideAccelSumm/slideAccelSamples)/gravity;
                    btnStart.setChecked(false);
                }
                break;
        }

        if(1000/50 < currentTime-lastUiUpdateTime) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshTexts();
                }
            });
            lastUiUpdateTime = currentTime;
        }
    }

    @Override
    public void onPause() {
        if(isListening()) {
            btnStart.setChecked(false);
        }
        super.onPause();
    }
}