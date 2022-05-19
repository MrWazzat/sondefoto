package com.example.sondefoto.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

public class NumberTextWatcher implements TextWatcher {

    private EditText et;

    private String oldText;

    private boolean wasTextJustErased = false;
    private DecimalFormat df;


    public NumberTextWatcher(EditText et) {
        this.df = new DecimalFormat("##.###", new DecimalFormatSymbols(Locale.ENGLISH));
        this.oldText = "";
        this.et = et;
    }


    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        String newText = et.getText().toString();
        String newTextClean = newText.replaceAll("[^\\d.]|\\.", "");
        String oldTextClean = oldText.replaceAll("[^\\d.]|\\.", "");

        Log.i("TW: New Text", newText);
        Log.i("TW: New Text Clean", newTextClean);
        Log.i(": Old Text", oldTextClean);


        String newFormattedText = this.formatNumber(newTextClean);
        Log.i("TW", newFormattedText);

        et.setText(newFormattedText);
        et.setSelection(newFormattedText.length());

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        wasTextJustErased = (count == 1 && after == 0);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private String formatNumber(String cleanString) {
        if(cleanString.length() > 5){
            return formatNumber(cleanString.substring(0, 5));
        }

        if (cleanString.length() >= 3 && cleanString.length() <= 5) {
            String firstPart = cleanString.substring(0, 2);
            String secondpart = cleanString.substring(2);
            return firstPart + "." + secondpart;
        }

        return cleanString;
    }

}
