package com.statiticsapp.CustomViews;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.statiticsapp.Interfaces.GenericDialogListener;
import com.statiticsapp.R;
import com.statiticsapp.Utils.Constants;

public class GenericDialog extends DialogFragment  {

    GenericDialogListener callback;
    String message;
    String title;
    String buttonOk;
    String buttonCancel;


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.message = (String) args.getSerializable(Constants.MESSAGE);
        if(args.containsKey(Constants.TITLE)) this.title = (String) args.getSerializable(Constants.TITLE);
        if(args.containsKey(Constants.BUTTON_OK)) this.buttonOk = (String) args.getSerializable(Constants.BUTTON_OK);
        if(args.containsKey(Constants.BUTTON_CANCEL)) this.buttonCancel = (String) args.getSerializable(Constants.BUTTON_CANCEL);

    }

    public void setCallback(GenericDialogListener callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_generic, container, false);

        TextView messageTV = root.findViewById(R.id.generic_dialog_message);
        messageTV.setText(message);

        Button ok = root.findViewById(R.id.generic_dialog_button_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText sampleSizeTxt = root.findViewById(R.id.dialog_sample_size);
                EditText medianTxt = root.findViewById(R.id.dialog_median);
                EditText stdDeviationTxt = root.findViewById(R.id.dialog_std_deviation);

                int sampleSize;
                double median;
                double stdDeviation;

                try {
                    sampleSize = Integer.parseInt(sampleSizeTxt.getText().toString());
                    median = Double.parseDouble(medianTxt.getText().toString());
                    stdDeviation = Double.parseDouble(stdDeviationTxt.getText().toString());
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "Se deben completar los 3 campos obligatoriamente", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(callback != null) callback.onOkPressed(sampleSize, median, stdDeviation);
                dismiss();
            }
        });

        if(buttonOk != null) ok.setText(buttonOk);

        if(buttonCancel != null) {
            Button cancel = root.findViewById(R.id.generic_dialog_button_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            cancel.setText(buttonCancel);
        }

        if(title != null) {
            TextView titleTV = root.findViewById(R.id.generic_dialog_title);
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(title);
        }

        return root;
    }
}
