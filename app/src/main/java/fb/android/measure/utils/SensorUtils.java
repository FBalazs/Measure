package fb.android.measure.utils;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorUtils {
    public static SensorManager getSensorManager(Context context) {
        return (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    public static void registerDefaultSensorListener(Context context, SensorEventListener listener, int sensorType, int samplingPeriodUs, int maxReportLatencyUs) {
        SensorManager sm = getSensorManager(context);
        sm.registerListener(listener, sm.getDefaultSensor(sensorType), samplingPeriodUs, maxReportLatencyUs);
    }

    public static void registerDefaultSensorListener(Context context, SensorEventListener listener, int sensorType, int samplingPeriodUs) {
        registerDefaultSensorListener(context, listener, sensorType, samplingPeriodUs, 0);
    }

    public static void unregisterSensorListener(Context context, SensorEventListener listener) {
        getSensorManager(context).unregisterListener(listener);
    }
}
