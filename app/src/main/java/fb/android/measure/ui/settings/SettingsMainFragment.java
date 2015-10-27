package fb.android.measure.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import fb.android.measure.R;

public class SettingsMainFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_measurement);
    }
}
