package fb.android.measure.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;

import fb.android.measure.R;

public class Settings {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static int getSamplingPeriodUs(Context context) {
        return Integer.parseInt(getPreferences(context).getString(context.getString(R.string.pref_measurement_traction_accel_samplingus_key),
                context.getString(R.string.pref_measurement_traction_accel_samplingus_defvalue)));
    }

    public static float getCalibratedGravity(Context context) {
        return getPreferences(context).getFloat(context.getString(R.string.pref_calibration_gravity_key), SensorManager.GRAVITY_EARTH);
    }

    public static void setCalibratedGravity(Context context, float value) {
        getPreferences(context).edit().putFloat(context.getString(R.string.pref_calibration_gravity_key), value).commit();
    }
}
