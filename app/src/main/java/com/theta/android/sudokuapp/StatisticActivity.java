package com.theta.android.sudokuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        display();
    }

    private void display() {
        List<Integer> stats = readStats(this);

        TextView winView = findViewById(R.id.wins);
        TextView scoreView = findViewById(R.id.highscore);
        TextView timeView = findViewById(R.id.toptime);
        TextView lMovesView = findViewById(R.id.leastmoves);
        TextView tMovesView = findViewById(R.id.totalmoves);

        winView.setText(winView.getText() + Integer.toString(stats.get(0)));
        scoreView.setText(scoreView.getText() + Integer.toString(stats.get(1)));

        int time = stats.get(2);
        if (time == 0) {
            timeView.setText(timeView.getText() + "None");
        }
        else {
            timeView.setText(timeView.getText() + Integer.toString(time));
        }

        int lMoves = stats.get(3);
        if (lMoves == 0) {
            lMovesView.setText(lMovesView.getText() + "None");
        }
        else {
            lMovesView.setText(lMovesView.getText() + Integer.toString(lMoves));
        }

        tMovesView.setText(tMovesView.getText() + Integer.toString(stats.get(4)));
    }

    public static void addStats(Context context, int wins, int score, int time, int moves) {
        List<Integer> stats = readStats(context);
        int prevWins = stats.get(0);
        int prevHScore = stats.get(1);
        int prevTTime = stats.get(2);
        int prevLMoves = stats.get(3);
        int prevTMoves = stats.get(4);

        stats.set(0, prevWins + wins);
        if (prevHScore < score) {
            stats.set(1, score);
        }
        if (prevTTime == 0 || prevTTime > time) {
            stats.set(2, time);
        }
        if (prevLMoves == 0 || prevLMoves > moves) {
            stats.set(3, moves);
        }
        stats.set(4, prevTMoves + moves);

        writeStats(context, stats);
    }

    private static void writeStats(Context context, List<Integer> stats) {
        SharedPreferences prefs = context.getSharedPreferences("Stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("wins", stats.get(0));
        editor.putInt("highscore", stats.get(1));
        editor.putInt("toptime", stats.get(2));
        editor.putInt("leastmoves", stats.get(3));
        editor.putInt("totalmoves", stats.get(4));
        editor.commit();
    }

    private static List<Integer> readStats(Context context) {
        List<Integer> stats = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("Stats", Context.MODE_PRIVATE);
        stats.add(prefs.getInt("wins", 0));
        stats.add(prefs.getInt("highscore", 0));
        stats.add(prefs.getInt("toptime", 0));
        stats.add(prefs.getInt("leastmoves", 0));
        stats.add(prefs.getInt("totalmoves", 0));
        return stats;
    }

}
