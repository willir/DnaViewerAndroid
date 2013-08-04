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
    private SharedPreferences mPrefs;

    private SettingsUtils(Context ctx) {
        mContext = ctx.getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getXScaleAsString() {
        String defVal = mContext.getString(R.string.pref_default_x_scale);
        return mPrefs.getString(Constants.PREF_KEY_X_SCALE, defVal);
    }

    public float getXScale() {
        return Float.parseFloat(getXScaleAsString());
    }

    public String getFontSizeAsString() {
        String defVal = mContext.getString(R.string.pref_default_font_size);
        return mPrefs.getString(Constants.PREF_KEY_FONT_SIZE, defVal);
    }

    public int getFontSize() {
        return Integer.parseInt(getFontSizeAsString());
    }

    public boolean getShowDoubleSignal() {
        boolean defVal = Boolean.parseBoolean(mContext.getString(R.string.pref_default_show_double_signal));
        return mPrefs.getBoolean(Constants.PREF_KEY_SHOW_DOUBLE_SIGNAL, defVal);
    }
}
