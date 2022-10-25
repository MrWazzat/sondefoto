package com.example.sondefoto.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {

    private static final Integer MAX_LENGTH = 6;
    private static final Integer SEPARATOR_POSITION = 2;
    private static final String SEPARATOR = ".";

    private final EditText et;

    public NumberTextWatcher(EditText et) {
        this.et = et;
    }


    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        String newText = et.getText().toString();
        String newTextClean = newText.replaceAll("[^\\d.]|\\.", "");

        Log.i("TW: New Text", newText);
        Log.i("TW: New Text Clean", newTextClean);
        
        String newFormattedText = this.formatNumber(newTextClean);
        Log.i("TW", newFormattedText);

        et.setText(newFormattedText);
        et.setSelection(newFormattedText.length());

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private String formatNumber(String cleanString) {
        if(cleanString.length() > MAX_LENGTH){
            return formatNumber(cleanString.substring(0, MAX_LENGTH));
        }

        if (cleanString.length() >= 3 && cleanString.length() <= MAX_LENGTH) {
            String firstPart = cleanString.substring(0, SEPARATOR_POSITION);
            String secondpart = cleanString.substring(SEPARATOR_POSITION);
            return firstPart + SEPARATOR + secondpart;
        }

        return cleanString;
    }

}
