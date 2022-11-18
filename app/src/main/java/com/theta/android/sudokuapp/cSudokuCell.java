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

public class cSudokuCell {
    private final Button but;
    private final Context context;
    final private SudokuCell cell;
    final private cSudoku parent;
    private Boolean translate;
    private Boolean enabled = true;


    public cSudokuCell(Context context, cSudoku parent, LinearLayout butParent, Boolean translate) {
        this.translate = translate;
        this.parent = parent;
        this.context = context;
        this.cell = new SudokuCell();

        LayoutInflater inflater = LayoutInflater.from(context);
        but = (Button) inflater.inflate(R.layout.cell, butParent, false);
        butParent.addView(but);

        createListener();
    }

    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }

    public View getView() {
        return but;
    }

    public String getText() {
        return cell.getText();
    }

    public void setText(String text) {
        setText(text, false);
    }

    private void setText(String text, Boolean callChange) {
        if (translate) {
            text = parent.translate(text);
        }
        cell.setText(text);
        but.setText(cell.getFirst2());

        if (callChange) {
            parent.onCellChange(this);
        }
    }

    private void createListener() {
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enabled){
                    createPrompt();
                }
            }
        });
    }

    private void createPrompt() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View prompt = inflater.inflate(R.layout.prompt, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(prompt);

        final TextView title = prompt.findViewById(R.id.title);
        title.setText("Current word: " + this.getText());
        final EditText edit = prompt.findViewById(R.id.editText);

        alert.setCancelable(false);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setText(edit.getText().toString(), true);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.create();
        alert.show();
    }

}
