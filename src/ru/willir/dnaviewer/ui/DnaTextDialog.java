package ru.willir.dnaviewer.ui;

import ru.willir.dnaviewer.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DnaTextDialog extends DialogFragment {

    private String mDnaText;

    public DnaTextDialog(String dnaText) {
        mDnaText = dnaText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dna_text_dialog, null);
        EditText dnaEditText = (EditText) view.findViewById(R.id.dna_text_dialog_edit);
        dnaEditText.setText(mDnaText);

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }
}
