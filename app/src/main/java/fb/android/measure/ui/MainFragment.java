package fb.android.measure.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import fb.android.measure.R;

public class MainFragment extends BaseFragment implements SensorEventListener {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_main;
    }

    private TextView tvAccX, tvAccY, tvAccZ, tvAngle;

    @Override
    public void initFragment(Activity activity, View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        SensorManager sm = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), );
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensors)
            Log.d(getClass().getName(), "name="+sensor.getName()+" type="+sensor.getStringType()+" vendor="+sensor.getVendor());
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
