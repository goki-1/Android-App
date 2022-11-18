package com.theta.android.sudokuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SudokuCell {
    private String text = "";

    public void setText(String text) {
        this.text = HelpFunc.cleanString(text);
    }

    public String getText() {
        return text;
    }

    public String getFirst2() {
        return text.length() > 2 ? text.substring(0, 2) : text;
    }

}