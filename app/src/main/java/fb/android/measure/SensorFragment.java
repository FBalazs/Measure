package fb.android.measure;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import fb.android.measure.utils.SensorUtils;

public abstract class SensorFragment extends BaseFragment implements SensorEventListener {
    private boolean listening = false;

    public void startListen(int sensorType, int samplingPeriodUs, int maxReportLatenyUs) {
        listening = true;
        SensorUtils.registerDefaultSensorListener(getContext(), this, sensorType, samplingPeriodUs, maxReportLatenyUs);
    }

    public void startListen(int sensorType, int samplingPeriodUs) {
        startListen(sensorType, samplingPeriodUs, 0);
    }

    public void stopListen() {
        SensorUtils.unregisterSensorListener(getContext(), this);
        listening = false;
    }

    public boolean isListening() {
        return listening;
    }

    public abstract void onSensorData(float[] v);

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            onSensorData(event.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
