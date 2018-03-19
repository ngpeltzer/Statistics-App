package com.statisticsapp.utils;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Utils {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager != null && activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isNotPositive(double value) {
        return value <= 0;
    }

    public static boolean isInputEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    public static boolean isProbabilityInvalid(double probability) {
        return probability < 0 || probability > 1;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideAll(View... views) {
        for(View v : views) v.setVisibility(View.GONE);
    }
}
