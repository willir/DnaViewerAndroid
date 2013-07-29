package ru.willir.dnaviewer.ui;

import ru.willir.dnaviewer.R;
import ru.willir.dnaviewer.utils.Constants;
import ru.willir.dnaviewer.utils.DLog;
import ru.willir.dnaviewer.utils.DnaAbiData;
import ru.willir.dnaviewer.utils.DnaViewNative;
import ru.willir.dnaviewer.utils.FileUtils;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

public class MainActivity extends Activity {

    private static final int RES_CODE_FILE_SELECT = 1;
    private static final int RES_CODE_SETTINGS = 2;

    private GraphView mGraphView = null;
    private DnaAbiData mDnaAbiData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGraphView = (GraphView) findViewById(R.id.graph_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*.ab1");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    RES_CODE_FILE_SELECT);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case RES_CODE_FILE_SELECT:
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file 
                Uri uri = data.getData();
                Log.d(DLog.TAG, "File Uri: " + uri.toString());
                // Get the path
                String path = FileUtils.getPath(this, uri);
                Log.d(DLog.TAG, "File Path: " + path);

                mDnaAbiData = DnaViewNative.parseAbiFile(path);
                Log.d(DLog.TAG, "dnaAbiData: " + mDnaAbiData);
                mGraphView.setMinimumWidth(mDnaAbiData.lastNonTrashPoint + 100);
                mGraphView.setDnaData(mDnaAbiData);
            }
            break;
        case RES_CODE_SETTINGS:
            mGraphView.onSettingsChanged();
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_load_file) {
            showFileChooser();
        } else if(id == R.id.menu_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, RES_CODE_SETTINGS);
        } else if(id == R.id.menu_dna_text) {
            if(mDnaAbiData == null)
                return true;
            DnaTextDialog dialog = new DnaTextDialog(mDnaAbiData.nseq);
            dialog.show(getFragmentManager(), Constants.TAG_DIALOG_DNA_TEXT);
        } else {
            //Nothing
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mGraphView.onSettingsChanged();
    }
}
