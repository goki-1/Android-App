package com.theta.android.sudokuapp;

import android.util.Log;
import androidx.core.util.Pair;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Sudoku {
    private int size; //side length size
    private int gridH;// this should be smaller than (or equal) to gridW
    private int gridW;// this should be greater than (or equal) to gridW
    private int difficulty; //0 = easy, 1 = medium, 2=hard
    private List<Pair<String, String>> pairs;
    private List<List<String>> cells;
    private long startTime;
    private int moves;

    private final Boolean testing = true; // change this to true if you only want 1 empty cell upon creating a game

    public int getGridW() {
        return gridW;
    }

    public int getGridH() {
        return gridH;
    }

    public int getMoves() {
        return moves;
    }

    public int getTime() {
        return (int) ((Calendar.getInstance().getTimeInMillis() - startTime) / 1000);
    }
    public int getScore() {
        int winTime = getTime();
        return 10000 - (int) Math.sqrt(winTime*moves);
    }

    public String getWordAt(int y, int x) {
        return cells.get(y).get(x);
    }


    public void setSize(int sizeId) {
        if (sizeId == 0 || sizeId == 4) {
            size = 4;
            gridH = 2;
            gridW = 2;
        }
        else if (sizeId == 1 || sizeId == 6) {
            size = 6;
            gridH = 2;
            gridW = 3;
        }
        else if (sizeId == 2 || sizeId == 9) {
            size = 9;
            gridH = 3;
            gridW = 3;
        }
        else{
            size = 12;
            gridH = 3;
            gridW = 4;
        }
    }


    public void setDifficulty(final int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() { return difficulty; }

    public int getSize() {
        return size;
    }

    public void startGame() {
        this.startTime = Calendar.getInstance().getTimeInMillis();
        this.moves = 0;
    }

    private Boolean isBoardFull(List<List<Integer>> boardLayout) {
        for (List<Integer> i: boardLayout) {
            if (i.contains(-1)) {
                return false;
            }
        }
        return true;
    }

    public void loadSave(String layout) {
        cells = new ArrayList<>();
        String[] cellStrings = layout.split(",");
        for (int y = 0; y < size; y++) {
            cells.add(new ArrayList<>());
            for (int x = 0; x < size; x++) {
                cells.get(y).add(cellStrings[y*size+x].trim());
            }
        }
    }

    public String getSaveString() {
        String s = "";
        for (List<String> row: cells) {
            for(String cell: row) {
                s = s + cell + " ,";
            }
        }
        s = s.substring(0, s.length()-1);
        return s;
    }

    private Boolean makeBoard(List<List<Integer>> boardLayout, List<Integer> vals ) {
        int y=0,x=0;
        for (int i = 0; i < size*size; i++) {
            y = i/size;
            x = i%size;
            if (boardLayout.get(y).get(x) == -1) {
                Collections.shuffle(vals);
                int yGrid = (y/gridH)*gridH;
                int xGrid = (x/gridW)*gridW;

                for (int val: vals) {
                    Boolean valid = true;
                    for (int j = 0; j < size; j++) { // check grid & col
                        if ((boardLayout.get(j).get(x) == val) || ((boardLayout.get(yGrid+(j%gridH)).get(xGrid+(j/gridH))) == val)) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid && !boardLayout.get(y).contains(val)) { //check row
                        boardLayout.get(y).set(x, val);
                        if (isBoardFull(boardLayout)) {
                            return true;
                        }
                        else if (makeBoard(boardLayout, vals)) {
                            return true;
                        }
                    }
                }
                break;
            }
        }

        boardLayout.get(y).set(x, -1);
        return false;
    }

    public List<List<Integer>> createBoard() {
        List<List<Integer>> boardLayout = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            boardLayout.add(new ArrayList<>());
            for (int x = 0; x < size; x++) {
                boardLayout.get(y).add(-1);
            }
        }

        List<Integer> vals = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            vals.add(i);
        }

        makeBoard(boardLayout, vals);

        if (testing) {
            boardLayout.get(0).set(0, -1);
        }
        else {
            for (int i = 0; i < size*(difficulty+3); i++) {
                int x = new Random().nextInt(size);
                int y = new Random().nextInt(size);

                boardLayout.get(y).set(x, -1);
            }
        }
        return boardLayout;
    }


    /**
     * Checks board cells to see if player has completed the game
     *
     * @param
     * @return True if the player has won the game
     */
    public Boolean checkWin() {
        for (int i = 0; i < size; i++) {
            Boolean lineV = checkLineV(new Pair<>(0, i));
            Boolean lineH = checkLineH(new Pair<>(i, 0) );
            Boolean grid = checkGrid(new Pair<>((i/gridH)*gridH, (i%gridH)*gridH));

            if (!(lineV && lineH && grid)){
                return false;
            }
        }
        return true;
    }


    private boolean checkLineH(Pair<Integer,Integer> index) {
        List<Pair<String,String>> seen = new ArrayList<>();

        for (String word: cells.get(index.first)) {
            Pair<String,String> pair = findWordPair(word);
            if (pair == null || seen.contains(pair)) {
                return false;
            }
            seen.add(pair);
        }
        return true;
    }

    private Boolean checkLineV(Pair<Integer,Integer> index) {
        List<Pair<String,String>> seen = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String word = cells.get(i).get(index.second);
            Pair<String,String> pair = findWordPair(word);
            if (pair == null || seen.contains(pair)) {
                return false;
            }
            seen.add(pair);
        }
        return true;
    }

    private Boolean checkGrid(Pair<Integer,Integer> index) {
        index = new Pair<>((index.first/gridH)*gridH,(index.second/gridW)*gridW); //index of first cell in grid
        List<Pair<String, String>> seen = new ArrayList<>();

        for (int y = index.first; y < index.first + gridH; y++) {
            for (int x = index.second; x < index.second + gridW; x++) {
                String word = cells.get(y).get(x);
                Pair<String, String> pair = findWordPair(word);
                if (pair == null || seen.contains(pair)) {
                    return false;
                }
                seen.add(pair);
            }
        }
        return true;
    }

    public Pair<String, String> findWordPair(String text) {
        for (Pair<String, String> pair: pairs) {
            if (text.equals(pair.first) || text.equals(pair.second) ) {
                return pair;
            }
        }
        return null;
    }

    public void generateBoard() {
        List<List<Integer>> boardLayout = createBoard();
        cells = new ArrayList<>();

        for (int y = 0; y < size; y++) {
            cells.add(new ArrayList<>());
            for (int x = 0; x < size; x++) {
                int pairIndex = boardLayout.get(y).get(x);

                String firstWord;
                if (pairIndex != -1) {
                    firstWord = pairs.get(pairIndex).first;
                }
                else {
                    firstWord = "";
                }

                cells.get(y).add(firstWord);
            }
        }
    }

    public void initPairs(List<String> lines) {
        pairs = new ArrayList<>();
        for (String line: lines) {
            String[] items = line.split(",");
            if (items.length == 2) {
                items[0] = HelpFunc.cleanString(items[0]);
                items[1] = HelpFunc.cleanString(items[1]);
                pairs.add(new Pair<>(items[0], items[1]));
            }
        }

    }

    public Boolean onCellChange(Pair<Integer, Integer> index, String value) {
        moves++;
        cells.get(index.first).set(index.second, value);

        return checkWin();
    }


}
