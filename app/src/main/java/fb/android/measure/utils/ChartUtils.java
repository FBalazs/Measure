package fb.android.measure.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class ChartUtils {
    public static void setDefaults(LineChart chart) {
        chart.setHighlightEnabled(true);
        chart.setDrawGridBackground(false);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        chart.setBorderColor(Color.BLACK);
        chart.setDescriptionColor(Color.BLACK);
        chart.getLegend().setTextColor(Color.BLACK);
        chart.getLegend().setTextSize(14.0f);

        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setGridColor(Color.BLACK);
    }

    public static int addDataSet(LineData data, LineDataSet dataSet) {
        data.addDataSet(dataSet);
        return data.getIndexOfDataSet(dataSet);
    }

    public static int addDataSet(LineData data, String label, int color) {
        LineDataSet dataSet = createBasicDataSet(label, color);
        data.addDataSet(dataSet);
        return data.getIndexOfDataSet(dataSet);
    }

    public static LineDataSet createBasicDataSet(String label, int color) {
        LineDataSet ret = new LineDataSet(null, label);

        ret.setColor(color);
        ret.setCircleColor(color);

        ret.setDrawCircles(false);
        ret.setDrawCircleHole(false);
        ret.setLineWidth(1f);
        ret.setCircleSize(3f);

        ret.setValueTextSize(9f);
        ret.setDrawValues(false);

//        ret.setDrawFilled(true);
//        ret.setFillAlpha(65);
//        ret.setFillColor(color);
        return ret;
    }

    public static void removeFirstIndex(LineData data) {
        data.removeXValue(0);
        for(LineDataSet dataSet : data.getDataSets())
            dataSet.removeEntry(0);
    }
}
