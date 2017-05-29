package com.statiticsapp.CustomViews;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.statiticsapp.Interfaces.GenericDialogListener;
import com.statiticsapp.R;

/**
 * Created by ngpeltzer on 6/16/16.
 */
public class GenericDialog extends DialogFragment  {

    Activity activity;
    String message;
    GenericDialogListener callback;
    String title;
    String buttonOk;
    String buttonCancel;


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.message = (String) args.getSerializable("message");
        if(args.containsKey("title")) this.title = (String) args.getSerializable("title");
        if(args.containsKey("buttonOk")) this.buttonOk = (String) args.getSerializable("buttonOk");
        if(args.containsKey("buttonCancel")) this.buttonCancel = (String) args.getSerializable("buttonCancel");

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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_generic, container, false);

        TextView messageTV = (TextView) root.findViewById(R.id.generic_dialog_message);
        messageTV.setText(message);

        Button ok = (Button) root.findViewById(R.id.generic_dialog_button_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText sampleSizeTxt = (EditText) root.findViewById(R.id.dialog_sample_size);
                EditText medianTxt = (EditText) root.findViewById(R.id.dialog_median);
                EditText stdDeviationTxt = (EditText) root.findViewById(R.id.dialog_std_deviation);

                int sampleSize = Integer.parseInt(sampleSizeTxt.getText().toString());
                double median = Double.parseDouble(medianTxt.getText().toString());
                double stdDeviation = Double.parseDouble(stdDeviationTxt.getText().toString());

                if(callback != null) callback.onOkPressed(sampleSize, median, stdDeviation);
                dismiss();
            }
        });

        if(buttonOk != null) ok.setText(buttonOk);

        if(buttonCancel != null)
        {
            Button cancel = (Button) root.findViewById(R.id.generic_dialog_button_cancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            cancel.setText(buttonCancel);
        }

        if(title != null)
        {
            TextView titleTV = (TextView) root.findViewById(R.id.generic_dialog_title);
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(title);
        }

        return root;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
