package ru.willir.dnaviewer.ui;

import ru.willir.dnaviewer.R;
import ru.willir.dnaviewer.utils.ArrayExtn;
import ru.willir.dnaviewer.utils.Constants;
import ru.willir.dnaviewer.utils.SettingsUtils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsMainFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of settings changes.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onSettingsChanged();
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onSettingsChanged() {
        }
    };

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    private ListPreference mXScalePref = null;
    private ListPreference mFontSizePref = null;
    private SettingsUtils mSettingsUtils = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mSettingsUtils = SettingsUtils.getInstance(getActivity());
        mXScalePref = (ListPreference) findPreference(Constants.PREF_KEY_X_SCALE);
        mFontSizePref = (ListPreference) findPreference(Constants.PREF_KEY_FONT_SIZE);

        setDefaults();
    }

    private void setDefaults() {
        setValToListPreference(mXScalePref, mSettingsUtils.getXScaleAsString());
        setValToListPreference(mFontSizePref, mSettingsUtils.getFontSizeAsString());
    }

    private void setValToListPreference(ListPreference pref, String val) {
        int idx = ArrayExtn.searchArr(val, pref.getEntryValues());
        idx = (idx != -1) ? idx : 0;
        pref.setValueIndex(idx);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.unregisterOnSharedPreferenceChangeListener(this);

        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mCallbacks.onSettingsChanged();
    }
}
