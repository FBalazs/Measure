package fb.android.measure.calib;

import android.app.Activity;
import android.hardware.Sensor;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import fb.android.measure.R;
import fb.android.measure.SensorFragment;
import fb.android.measure.settings.Settings;

public class CalibrateGravityFragment extends SensorFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_calibrate_gravity;
    }

    private TextView tv;
    private ToggleButton btnCalibrate;

    private long samples = -1;
    private double sum;

    private void onBtnCalibrateChange(boolean checked) {
        if(checked) {
            samples = 0;
            sum = 0;
            startListen(Sensor.TYPE_ACCELEROMETER, Settings.getSamplingPeriodUs(getContext()));
            tv.setText(getString(R.string.calibrating));
        } else {
            if(samples != -1)
                stopListen();
            if(0 < samples)
                Settings.setCalibratedGravity(getContext(), (float)(sum/samples));
            tv.setText(getString(R.string.gravity_equal, Settings.getCalibratedGravity(getContext())));
        }
    }

    @Override
    public void initFragment(Activity activity, View view) {
        tv = (TextView)view.findViewById(R.id.fragment_calibrate_gravity_tv);

        btnCalibrate = (ToggleButton)view.findViewById(R.id.fragment_calibrate_gravity_btn);
        btnCalibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onBtnCalibrateChange(isChecked);
            }
        });

        onBtnCalibrateChange(btnCalibrate.isChecked());
    }

    @Override
    public void onSensorData(float[] v) {
        ++samples;
        sum += Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }
}
