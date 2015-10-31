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

public class MeasureTractionFragment extends SensorFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_measure_traction;
    }

    private LineChart chart;
    private CheckBox cbA, cbAvgA, cbAngle, cbAvgAngle;
    private TextView tvAccel, tvAngle, tvTraction;
    private ToggleButton btnStart;

    private float gravity;
    private int BUFFLENGTH;
    private final int DATALENGTH = 32768;
    private float slideAccel, thresholdAccel;

    private long startTime, currentTime, lastUiUpdateTime;
    private float a, angle, avgA, avgAngle;

    private int currentBuffLength;
    private LinkedList<Float> buffA = new LinkedList<>(), buffAngle = new LinkedList<>();

    private int currentDataLength;
    private LinkedList<Float> dataAX = new LinkedList<>(), dataAY = new LinkedList<>(), dataAZ = new LinkedList<>(), dataA = new LinkedList<>(), dataAvgA = new LinkedList<>(),
            dataAngle = new LinkedList<>(), dataAvgAngle = new LinkedList<>();
    private LinkedList<String> xVals = new LinkedList<>();
    private LineData chartData = new LineData();
    private int dataSetAX, dataSetAY, dataSetAZ, dataSetA, dataSetAvgA, dataSetAngle, dataSetAvgAngle;

    private void onBtnStartChange(boolean checked) {
        if(checked) {
            if(!isListening())
                startListen(Sensor.TYPE_ACCELEROMETER, Settings.getAccelSamplingPeriodUs(getContext()));

            startTime = System.currentTimeMillis();

            currentBuffLength = 0;
            buffA.clear();
            buffAngle.clear();

            currentDataLength = 0;
            dataAX.clear();
            dataAY.clear();
            dataAZ.clear();
            dataA.clear();
            dataAvgA.clear();
            dataAngle.clear();
            dataAvgAngle.clear();

            xVals.clear();
        } else {
            if(isListening())
                stopListen();
            Log.d(getClass().getName(), "currentDataLength="+currentDataLength);
            refreshChart();
        }
    }

    private void refreshChart() {
        chartData = new LineData(xVals);
        dataSetAX = ChartUtils.addDataSet(chartData, "ax", Color.RED);
        dataSetAY = ChartUtils.addDataSet(chartData, "ay", Color.GREEN);
        dataSetAZ = ChartUtils.addDataSet(chartData, "az", Color.BLUE);
        dataSetA = ChartUtils.addDataSet(chartData, "a", Color.CYAN);
        dataSetAvgA = ChartUtils.addDataSet(chartData, "avgA", Color.BLUE);
        dataSetAngle = ChartUtils.addDataSet(chartData, "angle", Color.GREEN);
        dataSetAvgAngle = ChartUtils.addDataSet(chartData, "avgAngle", 0xFF254117);

        Iterator<Float> itAX = dataAX.iterator();
        Iterator<Float> itAY = dataAY.iterator();
        Iterator<Float> itAZ = dataAZ.iterator();
        Iterator<Float> itA = dataA.iterator();
        Iterator<Float> itAvgA = dataAvgA.iterator();
        Iterator<Float> itAngle = dataAngle.iterator();
        Iterator<Float> itAvgAngle = dataAvgAngle.iterator();
        for(int i = 0; itA.hasNext(); ++i) {
            chartData.addEntry(new Entry(itAX.next(), i), dataSetAX);
            chartData.addEntry(new Entry(itAY.next(), i), dataSetAY);
            chartData.addEntry(new Entry(itAZ.next(), i), dataSetAZ);
            chartData.addEntry(new Entry(itA.next(), i), dataSetA);
            chartData.addEntry(new Entry(itAvgA.next(), i), dataSetAvgA);
            chartData.addEntry(new Entry(itAngle.next()*180/(float)Math.PI, i), dataSetAngle);
            chartData.addEntry(new Entry(itAvgAngle.next()*180/(float)Math.PI, i), dataSetAvgAngle);
        }

        chartData.getDataSetByIndex(dataSetA).setVisible(cbA.isChecked());
        chartData.getDataSetByIndex(dataSetAvgA).setVisible(cbAvgA.isChecked());
        chartData.getDataSetByIndex(dataSetAngle).setVisible(cbAngle.isChecked());
        chartData.getDataSetByIndex(dataSetAvgAngle).setVisible(cbAvgAngle.isChecked());

        chart.setData(chartData);
        chart.invalidate();
    }

    private void onCbChanged() {
        chartData.getDataSetByIndex(dataSetA).setVisible(cbA.isChecked());
        chartData.getDataSetByIndex(dataSetAvgA).setVisible(cbAvgA.isChecked());
        chartData.getDataSetByIndex(dataSetAngle).setVisible(cbAngle.isChecked());
        chartData.getDataSetByIndex(dataSetAvgAngle).setVisible(cbAvgAngle.isChecked());

        chart.setData(chartData);
        chart.invalidate();
    }

    private void initChart() {
        ChartUtils.setDefaults(chart);
        chart.setDescription("");
//        chart.setNoDataTextDescription(getResources().getString(R.string.no_data));
    }

    @Override
    public void initFragment(Activity activity, View view) {
        chart = (LineChart)view.findViewById(R.id.fragment_measure_traction_chart);

        cbA = (CheckBox)view.findViewById(R.id.fragment_measure_traction_cb_chart_a_enable);
        cbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });
        cbAvgA = (CheckBox)view.findViewById(R.id.fragment_measure_traction_cb_chart_avga_enable);
        cbAvgA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });
        cbAngle = (CheckBox)view.findViewById(R.id.fragment_measure_traction_cb_chart_angle_enable);
        cbAngle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });
        cbAvgAngle = (CheckBox)view.findViewById(R.id.fragment_measure_traction_cb_chart_avgangle_enable);
        cbAvgAngle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCbChanged();
            }
        });

        tvAccel = (TextView)view.findViewById(R.id.fragment_measure_traction_tv_accel);
        tvAngle = (TextView)view.findViewById(R.id.fragment_measure_traction_tv_angle);
        tvTraction = (TextView)view.findViewById(R.id.fragment_measure_traction_tv_traction);

        btnStart = (ToggleButton)view.findViewById(R.id.fragment_measure_traction_btn_start_stop);
        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onBtnStartChange(isChecked);
            }
        });

        gravity = Settings.getCalibratedGravity(activity);
        BUFFLENGTH = Settings.getTractionBuffLength(activity);
        slideAccel = Settings.getTractionSlideAccel(activity);
        thresholdAccel = Settings.getTractionThresholdAccel(activity);

        initChart();

        onBtnStartChange(btnStart.isChecked());
        refreshTexts();
    }

    private void refreshTexts() {
        tvAccel.setText(getString(R.string.accel_equal, avgA));
        tvAngle.setText(getString(R.string.angle_equal, avgAngle*180/Math.PI));
        tvTraction.setText(getString(R.string.traction_equal, Math.tan(avgAngle)));

    }

    @Override
    public void onSensorData(float[] v) {
        currentTime = System.currentTimeMillis();

        float ax = v[0], ay = v[1], az = v[2];

        a = (float)Math.sqrt(ax*ax + ay*ay);
        angle = (float)Math.acos(Math.min(1f, Math.abs(az) / gravity));
//        a -= Math.sin(angle)*gravity;

        while(BUFFLENGTH <= currentBuffLength) {
            buffA.removeFirst();
            buffAngle.removeFirst();
            --currentBuffLength;
        }
        buffA.addLast(a);
        buffAngle.addLast(angle);
        ++currentBuffLength;

        a -= Math.sin(angle)*gravity;

        double sum = 0;
        for(Float f : buffA)
            sum += f;
        avgA = (float)(sum/currentBuffLength);
        sum = 0;
        for(Float f : buffAngle)
            sum += f;
        avgAngle = (float)(sum/currentBuffLength);
        avgA -= Math.sin(avgAngle)*gravity;

        while(DATALENGTH <= currentDataLength) {
            dataAX.removeFirst();
            dataAY.removeFirst();
            dataAZ.removeFirst();
            dataA.removeFirst();
            dataAngle.removeFirst();
            dataAvgA.removeFirst();
            dataAvgAngle.removeFirst();
            xVals.removeFirst();
            --currentDataLength;
        }
        dataAX.addLast(ax);
        dataAY.addLast(ay);
        dataAZ.addLast(az);
        dataA.addLast(a);
        dataAngle.addLast(angle);
        dataAvgA.addLast(avgA);
        dataAvgAngle.addLast(avgAngle);
        xVals.addLast("" + (currentTime - startTime) / 1000d);
        ++currentDataLength;

        if(Math.max(slideAccel*Math.tan(avgAngle), thresholdAccel) < avgA) {
            btnStart.setChecked(false);
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