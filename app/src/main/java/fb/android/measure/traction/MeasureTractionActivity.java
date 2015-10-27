package fb.android.measure.traction;

import android.os.Bundle;
import android.view.WindowManager;

import fb.android.measure.BaseActivity;
import fb.android.measure.R;

public class MeasureTractionActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_measure_traction;
    }

    @Override
    protected int getMenuResource() {
        return R.menu.menu_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
