package fb.android.measure;

public class MainActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuResource() {
        return R.menu.menu_main;
    }

    @Override
    protected boolean canGoBack() {
        return false;
    }
}
