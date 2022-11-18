package com.theta.android.sudokuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class HelpFunc {
    private HelpFunc() {
    }

    public static String cleanString(String s) {
        return s.replaceAll("[\u200B-\u200D\uFEFF]", "").trim().replace(" ","");
    }

    public static List<String> readFile(Context context, int file) {
        InputStream is = context.getResources().openRawResource(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        List<String> lines = new ArrayList<>();

        String line = "";
        try {
            while ( (line = reader.readLine()) != null ) {
                lines.add(line);

            }
        }
        catch (IOException e) {
            Log.e("readFile", "could not init pairs");
            e.printStackTrace();
        }

        return lines;
    }

    public static List<String> split(String s, char splitAt) {
        List<String> splitList= new ArrayList<>();
        if (s.equals("")) {return splitList;}

        int prevSplit = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == splitAt) {
                splitList.add(s.substring(prevSplit, i));
                prevSplit = i+1;
            }
        }
        splitList.add(s.substring(prevSplit, s.length()));
        return splitList;
    }

}

