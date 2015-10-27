package fb.android.measure;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import fb.android.measure.calib.CalibrateGravityActivity;
import fb.android.measure.traction.MeasureTractionActivity;

public class MainFragment extends BaseFragment {
    @Override
    public int getLayoutResource() {
        return R.layout.fragment_main;
    }

    private Button btnMeasureTraction,
            btnCalibrateGravity;

    @Override
    public void initFragment(Activity activity, View view) {
        btnMeasureTraction = (Button)view.findViewById(R.id.fragment_main_btn_measure_traction);
        btnMeasureTraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MeasureTractionActivity.class));
            }
        });

        btnCalibrateGravity = (Button)view.findViewById(R.id.fragment_main_btn_calibrate_gravity);
        btnCalibrateGravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CalibrateGravityActivity.class));
            }
        });
    }
}
