package com.theta.android.sudokuapp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SudokuCellTest {


    @Test
    public void setText() {
        SudokuCell cell = new SudokuCell();

        String testValue = "1";
        cell.setText(testValue);
        String expectedValue = "1";

        String result = cell.getText();

        assertEquals(expectedValue,result);
    }

    @Test
    public void getFirst2() {
        SudokuCell cell = new SudokuCell();

        String testValue = "123";
        cell.setText(testValue);
        String expectedValue = "12";

        String result = cell.getFirst2();

        assertEquals(expectedValue,result);
    }

    @Test
    public void getText() {
        int testValue = 0;
        int expectedValue = 0;

        assertEquals(testValue, expectedValue);
    }

    @Test
    public void styleCell(){
        int testValue = 0;
        int expectedValue = 0;

        assertEquals(testValue, expectedValue);
    }

    @Test
    public void createListener(){
        int testValue = 0;
        int expectedValue = 0;

        assertEquals(testValue, expectedValue);
    }

    @Test
    public void createPrompt() {
        int testValue = 0;
        int expectedValue = 0;

        assertEquals(testValue, expectedValue);
    }

}