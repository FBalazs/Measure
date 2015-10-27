package fb.android.measure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fb.android.measure.settings.SettingsActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    protected boolean running;

    protected abstract int getLayoutResource();

    protected abstract int getMenuResource();

    protected boolean canGoBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    protected void onPause() {
        running = false;
        super.onPause();
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuResource = getMenuResource();
        if (menuResource == -1)
            return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(menuResource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}