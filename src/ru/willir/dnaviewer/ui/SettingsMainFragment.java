package ru.willir.dnaviewer.ui;

import ru.willir.dnaviewer.R;
import ru.willir.dnaviewer.utils.ArrayExtn;
import ru.willir.dnaviewer.utils.Constants;
import ru.willir.dnaviewer.utils.SettingsUtils;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsMainFragment extends PreferenceFragment {

    private ListPreference mXScalePref = null;
    private SettingsUtils mSettingsUtils = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mSettingsUtils = SettingsUtils.getInstance(getActivity());
        mXScalePref = (ListPreference) findPreference(Constants.PREF_KEY_X_SCALE);

        setDefaults();
    }

    private void setDefaults() {
        String xScaleDef = mSettingsUtils.getXScaleAsString();
        int xScaleDefIdx = ArrayExtn.searchArr(xScaleDef, mXScalePref.getEntryValues());
        xScaleDefIdx = (xScaleDefIdx != -1) ? xScaleDefIdx : 0;
        mXScalePref.setValueIndex(xScaleDefIdx);
    }
}
