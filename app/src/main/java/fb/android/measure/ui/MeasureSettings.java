package fb.android.measure.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fb.android.measure.R;

public class MeasureSettings {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getSamplingPeriodUs(Context context) {
        return getPreferences(context).getInt(context.getString(R.string.pref_measurement_samplingus_key), 10); // TODO defvalue
    }
}
