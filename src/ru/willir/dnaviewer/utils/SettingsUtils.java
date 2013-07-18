package ru.willir.dnaviewer.utils;

import ru.willir.dnaviewer.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsUtils {

    private static SettingsUtils mInstance = null;

    public static SettingsUtils getInstance(Context ctx) {
        if(mInstance == null)
            mInstance = new SettingsUtils(ctx);
        return mInstance;
    }

    private Context mContext;

    private SettingsUtils(Context ctx) {
        mContext = ctx;
    }

    public String getXScaleAsString() {
        String defVal = mContext.getString(R.string.pref_default_x_scale);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Constants.PREF_KEY_X_SCALE, defVal);
    }

    public float getXScale() {
        return Float.parseFloat(getXScaleAsString());
    }
}
