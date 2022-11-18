package com.theta.android.sudokuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "SUDOKU";
    //button stuff
    private cSudoku game;
    private Button btn_help;
    private Button btn_settings;
    private Button btn_stat;
    private Button btn_hint;

    @Override
    protected void onStop() {
        super.onStop();
        game.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();

        // creates sudoku board
        Log.d(TAG, "ran1");
        game = new cSudoku(this, findViewById(R.id.gameBoard));

        //Button stuff

        //help button
        btn_help = (Button) findViewById(R.id.help);
        //set the button to do stuff on click
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelpButton();
            }
            public void openHelpButton() {
                Intent intent_help = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent_help);
            }
        });

        //settings button
        btn_settings = (Button) findViewById(R.id.settings);
        //set the button to do stuff on click
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsButton();

            }
            public void openSettingsButton() {
                Intent intent_settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent_settings);
            }
        });


        //Statistics button
        btn_stat = (Button) findViewById(R.id.statistics);
        //set the button to do stuff on click
        btn_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatButton();
            }
            public void openStatButton() {
                Intent intent_stat = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent_stat);
            }
        });

        //Hint button
        btn_hint = (Button) findViewById(R.id.hint);
        //set the button to do stuff on click
        btn_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHintButton();
            }
            public void openHintButton() {
                //do stuff for hint
            }
        });





    }

    private void setLayout() {
        if (SettingsActivity.readColorMode(this)) {
            LinearLayout header = findViewById(R.id.mainHeader);
            LinearLayout footer = findViewById(R.id.mainFooter);
            LinearLayout root = findViewById(R.id.root);

            int color = getResources().getColor(R.color.dark);
            header.setBackgroundColor(color);
            footer.setBackgroundColor(color);
            root.setBackgroundColor(color);
        }
    }

}