package com.example.sondefoto.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat formatter;
    private EditText et;

    private boolean wasTextJustErased = false;

    public NumberTextWatcher(EditText et)
    {
        formatter = new DecimalFormat("##.###");
        formatter.setDecimalSeparatorAlwaysShown(true);
        this.et = et;
    }

    @Override
    public void afterTextChanged(Editable s)
    {


        et.removeTextChangedListener(this);

        int currLen = et.getText().length();
        String currText = et.getText().toString();


        Log.i("TW", ""+currText.charAt(currLen-1));
        if(wasTextJustErased && currLen == 3 && currText.charAt(currLen-1) == '.'){
            et.setText(currText.substring(0, currLen - 1));
        }

        if(wasTextJustErased)
            return;

        if(currLen == 2){
            Log.i("TW", "Adding a dot");
            currText = currText + ".";
            et.setText(currText);
            et.setSelection(et.getText().length());
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        wasTextJustErased = (count == 1 && after == 0);
        Log.i("TW : s", s.toString());
        Log.i("TW : start", "" + start);
        Log.i("TW : count",""+count);
        Log.i("TW : after", ""+after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

}
