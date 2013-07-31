package ru.willir.dnaviewer.ui;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity implements SettingsMainFragment.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsMainFragment()).commit();
    }

    @Override
    public void onSettingsChanged() {
    }
}
