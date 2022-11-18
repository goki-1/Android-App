package com.theta.android.sudokuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.core.util.Pair;

import android.media.audiofx.Equalizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class cSudoku {
    private final String TAG = "sudoku";

    private final Sudoku sudoku;
    private final LinearLayout board;
    private final int boardLength = 10;
    private List<List<cSudokuCell>> cells;
    private Context context;
    private Boolean isWinScreen = false;

    public String translate(String text) {
        if (text == null) return null;
        Pair<String, String> pair = sudoku.findWordPair(text);
        return pair == null? text: pair.first;
    }


    public void onStop() {
        if (!isWinScreen) {
            saveGame();
        }

    }

    public cSudoku(Context context, ViewGroup board) {
        this.context = context;
        this.sudoku = new Sudoku();
        this.board = (LinearLayout) board;

        startGame(false);
    }

    private void startGame(Boolean isReplay) {
        isWinScreen = false;

        int sizeId = SettingsActivity.readSize(context);
        int difficulty = SettingsActivity.readDifficulty(context);
        sudoku.setSize(sizeId);
        sudoku.setDifficulty(difficulty);

        List<String> pairLines = cWordBank.getMainPairs(context);
        if (pairLines.size() < sudoku.getSize()) {
            pairLines = cWordBank.getDefaultPairs(context);
        }
        sudoku.initPairs(pairLines);
        loadGame();
        if (!isReplay) {
            initBoard();
        }
        generateBoard();
        sudoku.startGame();
    }

    private void generateBoard() {
        final int size = sudoku.getSize();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cSudokuCell cell = cells.get(y).get(x);
                String word = sudoku.getWordAt(y, x);
                cell.setEnabled(word.length() == 0);
                cell.setText(word);
            }
        }
    }

    private void initBoard() {
        final int size = sudoku.getSize();
        cells = new ArrayList<List<cSudokuCell>>(size);
        Boolean translate = SettingsActivity.readTranslateMode(context);

        for (int y = 0; y < size; y++) {
            LinearLayout row = createRow(size);
            for (int x = 0; x < size; x++) {
                cSudokuCell cell = new cSudokuCell(context, this, row, translate);
                if ((x+1)%sudoku.getGridW() == 0 && x != size-1) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cell.getView().getLayoutParams();
                    params.rightMargin = boardLength;
                    cell.getView().setLayoutParams(params);
                }
                if ((y+1)%sudoku.getGridH() == 0 && y != size-1) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) row.getLayoutParams();
                    params.bottomMargin = boardLength;
                    row.setLayoutParams(params);
                }

                cells.get(y).add(cell);
            }
        }
    }

    private LinearLayout createRow(final int size) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout row = (LinearLayout) inflater.inflate(R.layout.boardrow, board, false);
        board.addView(row);

        cells.add(new ArrayList<>(size));
        return row;
    }

    public void onCellChange(cSudokuCell cell) {
        if (sudoku.onCellChange(findIndex(cell), cell.getText())) {
            isWinScreen = true;
            deleteSave(context);
            int score = sudoku.getScore();
            int winTime = sudoku.getTime();
            int moves = sudoku.getMoves();
            createWinPopup(score, winTime, moves);
            StatisticActivity.addStats(context, 1, score, winTime, moves);
        }
    }

    private void createWinPopup(int score, int time, int moves) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View winScreen = inflater.inflate(R.layout.win_screen, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(winScreen);

        final RadioGroup pairPrompt = (RadioGroup) winScreen.findViewById(R.id.pairPrompt);

        final TextView scoreText = (TextView) winScreen.findViewById(R.id.gameScore);
        final TextView timeText = (TextView) winScreen.findViewById(R.id.gameTime);
        final TextView movesText = (TextView) winScreen.findViewById(R.id.gameMoves);
        scoreText.setText(scoreText.getText().toString() + Integer.toString(score));
        timeText.setText(timeText.getText().toString() + Integer.toString(time));
        movesText.setText(movesText.getText().toString() + Integer.toString(moves));

        alert.setCancelable(false);
        alert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (pairPrompt.getCheckedRadioButtonId() == R.id.yesBut) {
                    cWordBank.addPractice(context);
                }

                startGame(true);
            }
        });
        alert.create();
        alert.show();
    }

    private Pair<Integer, Integer> findIndex(cSudokuCell cell) {
        for (int y = 0; y < cells.size(); y++) {
            int x = cells.get(y).indexOf(cell);
            if (x != -1) {
                return new Pair<>(y, x);
            }
        }
        return null;
    }

    public void saveGame() {
        SharedPreferences prefs = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = sudoku.getSize();
        String boardLayout = sudoku.getSaveString();

        editor.putString("boardLayout", boardLayout);
        editor.commit();
    }

    public static void deleteSave(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("boardLayout", "");
        editor.commit();
    }

    private void loadGame() {
        SharedPreferences prefs = context.getSharedPreferences("save", Context.MODE_PRIVATE);

        String boardlayout = prefs.getString("boardLayout", "");

        if (boardlayout.equals("")) {
            sudoku.generateBoard();
        }
        else {
            sudoku.loadSave(boardlayout);
        }
    }


}