package com.theta.android.sudokuapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeBut;
    private Switch pairSwitch;
    private Switch translateSwitch;

    private List<Integer> diffIds = new ArrayList<Integer>(Arrays.asList(R.id.easyBut, R.id.mediumBut, R.id.hardBut));
    private RadioGroup diffGroup;

    private List<Integer> sizeIds = new ArrayList<Integer>(Arrays.asList(R.id.size4, R.id.size6, R.id.size9, R.id.size12));
    private RadioGroup sizeGroup;

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        diffGroup = (RadioGroup) findViewById(R.id.difficulty);
        darkModeBut = (Switch) findViewById(R.id.colormode);
        pairSwitch = (Switch) findViewById(R.id.practicePairs);
        sizeGroup = (RadioGroup) findViewById(R.id.sudokuSize);
        translateSwitch = (Switch) findViewById(R.id.translate);

        diffGroup.check(diffIds.get(readDifficulty(this)));
        darkModeBut.setChecked(readColorMode(this));
        pairSwitch.setChecked(readPracticeMode(this));
        sizeGroup.check(sizeIds.get(readSize(this)));
        translateSwitch.setChecked(readTranslateMode(this));

        //word_bank button
        Button btn_word = (Button) findViewById(R.id.button_word);
        //set the button to do stuff on click
        btn_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWordButton();

            }
            public void openWordButton() {
                Intent intent_word = new Intent(SettingsActivity.this, WordBankActivity.class);
                startActivity(intent_word);
            }
        });




    }

    private void saveSettings() {
        SharedPreferences prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int difficulty = diffIds.indexOf(diffGroup.getCheckedRadioButtonId());

        int boardSize = sizeIds.indexOf(sizeGroup.getCheckedRadioButtonId());

        Boolean darkMode = darkModeBut.isChecked();
        Boolean practiceMode = pairSwitch.isChecked();
        Boolean translateMode = translateSwitch.isChecked();

        editor.putInt("difficulty", difficulty);
        editor.putBoolean("darkmode", darkMode);
        editor.putBoolean("practicemode", practiceMode);
        editor.putInt("boardsize", boardSize);
        editor.putBoolean("translatemode", translateMode);
        editor.commit();

        cSudoku.deleteSave(this);
    }

    public static int readDifficulty(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getInt("difficulty", 0);
    }

    public static Boolean readColorMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getBoolean("darkmode", false);
    }

    public static int readSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getInt("boardsize", 2);
    }

    public static Boolean readPracticeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getBoolean("practicemode", false);
    }

    public static Boolean readTranslateMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return prefs.getBoolean("translatemode", false);
    }

}