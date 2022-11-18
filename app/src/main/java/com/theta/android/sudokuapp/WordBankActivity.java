package com.theta.android.sudokuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class WordBankActivity extends AppCompatActivity {
    private cWordBank fileSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_bank);

        this.fileSystem = new cWordBank(this, findViewById(R.id.wordBankRoot));

        Button fileBut = findViewById(R.id.fileBut);
        fileBut.setOnClickListener(view -> createPrompt(fileSystem, true));

        Button FolderBut = findViewById(R.id.folderBut);
        FolderBut.setOnClickListener(view -> createPrompt(fileSystem, false));

        Button returnBut = findViewById(R.id.returnBut);
        returnBut.setOnClickListener(view -> fileSystem.changeDir(null));

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.fileSystem.saveFiles();
    }

    private void createPrompt(cWordBank fileSystem, Boolean isFile) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View prompt = inflater.inflate(R.layout.prompt, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(prompt);

        final TextView title = prompt.findViewById(R.id.title);
        title.setText("Enter " + (isFile? "file": "folder") + " name");
        final EditText edit = prompt.findViewById(R.id.editText);

        alert.setCancelable(false);
        alert.setPositiveButton("Save", (dialogInterface, i) -> fileSystem.addFile(edit.getText().toString(), isFile, false, true));
        alert.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        alert.create();
        alert.show();
    }

}