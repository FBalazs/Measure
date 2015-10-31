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

    public static String getStringPreference(Context context, int key, int defvalue) {
        return getPreferences(context).getString(context.getString(key), context.getString(defvalue));
    }

    public static int getStringIntPreference(Context context, int key, int defvalue) {
        return Integer.parseInt(getStringPreference(context, key, defvalue));
    }

    public static float getStringFloatPreference(Context context, int key, int defvalue) {
        return Float.parseFloat(getStringPreference(context, key, defvalue));
    }

    public static float getCalibratedGravity(Context context) {
        return getPreferences(context).getFloat(context.getString(R.string.pref_calibration_gravity_key), SensorManager.GRAVITY_EARTH);
    }

    public static void setCalibratedGravity(Context context, float value) {
        getPreferences(context).edit().putFloat(context.getString(R.string.pref_calibration_gravity_key), value).commit();
    }

    public static int getAccelSamplingPeriodUs(Context context) {
        return getStringIntPreference(context,
                R.string.pref_measurement_accel_samplingus_key,
                R.string.pref_measurement_accel_samplingus_defvalue);
    }

    public static int getTractionBuffLength(Context context) {
        return getStringIntPreference(context,
                R.string.pref_measurement_traction_bufflength_key,
                R.string.pref_measurement_traction_bufflength_defvalue);
    }

    public static float getTractionSlideAccel(Context context) {
        return getStringFloatPreference(context,
                R.string.pref_measurement_traction_slide_accel_key,
                R.string.pref_measurement_traction_slide_accel_defvalue);
    }

    public static float getTractionThresholdAccel(Context context) {
        return getStringFloatPreference(context,
                R.string.pref_measurement_traction_threshold_accel_key,
                R.string.pref_measurement_traction_threshold_accel_defvalue);
    }
}
