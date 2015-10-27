package fb.android.measure.traction;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;

import fb.android.measure.R;
import fb.android.measure.SensorFragment;
import fb.android.measure.settings.Settings;

public class MeasureTractionFragment extends SensorFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_measure_traction;
    }

    private LineChart chart;
    private TextView tvAccel, tvAngle, tvTraction;
    private ToggleButton btnStart;

    private float gravity;
    private final int buffLength = 5;
    private LinkedList<Float> llA, llAngle;
    private float avgA, avgAngle;
    private float maxA;
    private final int maxListLength = 32768;
    private ArrayList<Float> listA, listAvgA;

    private void onBtnStartChange(boolean checked) {
        if(checked) {
            if(!isListening())
                startListen(Sensor.TYPE_ACCELEROMETER, Settings.getSamplingPeriodUs(getContext()));
            llA.clear();
            llAngle.clear();
            maxA = 0;
            listA.clear();
            listAvgA.clear();
        } else {
            if(isListening())
                stopListen();
            fillChart();
        }
    }

    private void fillChart() {
        int size = listA.size();

        String[] xVals = new String[size];

        ArrayList<Entry> entriesA = new ArrayList<>(size);
        ArrayList<Entry> entriesAvgA = new ArrayList<>(size);
        for(int i = 0; i < size; ++i) {
            entriesA.add(new Entry(listA.get(i), i));
            entriesAvgA.add(new Entry(listAvgA.get(i), i));
            xVals[i] = ""+i;
        }

        LineDataSet setA = new LineDataSet(entriesA, "a");
        LineDataSet setAvgA = new LineDataSet(entriesAvgA, "avga");

        setA.setColor(Color.CYAN);
        setA.setCircleColor(Color.CYAN);
        setA.setLineWidth(1f);
        setAvgA.setColor(Color.MAGENTA);
        setAvgA.setCircleColor(Color.MAGENTA);
        setAvgA.setLineWidth(1f);

        setA.setDrawCircles(true);
        setA.setCircleSize(3f);
        setA.setDrawCircleHole(false);
        setAvgA.setDrawCircles(true);
        setAvgA.setCircleSize(3f);
        setAvgA.setDrawCircleHole(false);

        setA.setValueTextSize(9f);
        setA.setDrawValues(false);
        setAvgA.setValueTextSize(9f);
        setAvgA.setDrawValues(false);

        setA.setDrawFilled(true);
        setA.setFillAlpha(65);
        setA.setFillColor(getResources().getColor(R.color.accent_dark));
        setAvgA.setDrawFilled(true);
        setAvgA.setFillAlpha(65);
        setAvgA.setFillColor(getResources().getColor(R.color.accent_dark));

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setA);
        dataSets.add(setAvgA);

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.BLACK);

        chart.setData(data);
        chart.invalidate();
    }

    private void initChart() {
//        chart.setDescription("");
//        chart.setNoDataTextDescription(getResources().getString(R.string.no_data));

        chart.setHighlightEnabled(true);
        chart.setDrawGridBackground(false);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        int colorTextWhite = Color.BLACK;
        chart.setBorderColor(colorTextWhite);
        chart.setDescriptionColor(colorTextWhite);
        chart.getLegend().setTextColor(colorTextWhite);
        chart.getLegend().setTextSize(14.0f);

        chart.getAxisRight().setEnabled(false);

        chart.animateX(2000, Easing.EasingOption.EaseInBack);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(colorTextWhite);
        xAxis.setGridColor(colorTextWhite);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(colorTextWhite);
        leftAxis.setGridColor(colorTextWhite);


//        LimitLine ll = new LimitLine(20f, getResources().getString(R.string.daily_goal));
//        ll.setLineColor(getResources().getColor(R.color.primary_light));
//        ll.setLineWidth(2f);
//        ll.setTextColor(colorTextWhite);
//        ll.enableDashedLine(10, 5, 0);// set the line to be drawn like this "- - - - - -"
//        ll.setTextSize(14f);
//
//        leftAxis.addLimitLine(ll);
    }

    @Override
    public void initFragment(Activity activity, View view) {
        chart = (LineChart)view.findViewById(R.id.fragment_measure_traction_chart);
        initChart();

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
        llA = new LinkedList<>();
        llAngle = new LinkedList<>();
        listA = new ArrayList<>();
        listAvgA = new ArrayList<>();

        onBtnStartChange(btnStart.isChecked());
        refreshTexts();
    }

    private void refreshTexts() {
        tvAccel.setText(getString(R.string.accel_equal, avgA)+" maxA = "+maxA);
        tvAngle.setText(getString(R.string.angle_equal, avgAngle*180/Math.PI));
        tvTraction.setText(getString(R.string.traction_equal, Math.tan(avgAngle)));
    }

    private long lastUiUpdateTime;

    @Override
    public void onSensorData(float[] v) {
        float ax = v[0], ay = v[1], az = v[2];

        float a = (float)Math.sqrt(ax*ax + ay*ay);
        float angle = (float)Math.acos(Math.min(1f, Math.abs(az)/gravity));
        listA.add((float)Math.sin(angle)*gravity);
        if(listA.size() == maxListLength+1)
            listA.remove(0);

//        a -= Math.sin(angle)*gravity;

        llA.addLast(a);
        if(llA.size() == buffLength+1)
            llA.removeFirst();

        llAngle.addLast(angle);
        if(llAngle.size() == buffLength+1)
            llAngle.removeFirst();

        double sum = 0;
        for(Float f : llA)
            sum += f;
        avgA = (float)(sum/llA.size());

        sum = 0;
        for(Float f : llAngle)
            sum += f;
        avgAngle = (float)(sum/llAngle.size());

        avgA -= Math.sin(avgAngle)*gravity;
        if(llA.size() == buffLength && Math.abs(maxA) < Math.abs(avgA))
            maxA = avgA;
        listAvgA.add(avgA);
        if(listAvgA.size() == maxListLength+1)
            listAvgA.remove(0);

        if(1000/50 < System.currentTimeMillis()-lastUiUpdateTime) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshTexts();
                }
            });
            lastUiUpdateTime = System.currentTimeMillis();
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
