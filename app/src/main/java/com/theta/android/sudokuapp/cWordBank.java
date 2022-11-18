package com.theta.android.sudokuapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// one space (" ") means moving down a directory
// two spaces and a dot ("  .") means accessing a attribute of that file

public class cWordBank {
    private final static String rootDir = "";
    private final static String defaultDir = "Default_Pairs";
    private final static String practiceDir = "Practice";
    private final static String mainPairDir = "  .mainPairDir";

    private LinearLayout layout;
    private Context context;
    private String dir = "";
    private List<cWordFile> fileList;

    public cWordBank(Context context, LinearLayout layout) {
        this.context = context;
        this.layout = layout;
        getFiles();

    }

    private static String getFileName(SharedPreferences prefs, String filedir) {
        return prefs.getString(filedir+"  .name", null);
    }
    private Boolean getFileisDel(SharedPreferences prefs, String dir, String name) {
        return prefs.getBoolean((dir+" "+name).trim()+"  .isDel", true);
    }
    private Boolean getFileisFile(SharedPreferences prefs, String dir, String name) {
        return prefs.getBoolean((dir+" "+name).trim()+"  .isFile", false);
    }

    private static void setFilePerms(SharedPreferences.Editor editor, String dir, String name, boolean isFile, boolean isDel) {
        String fileDir = (dir+" "+name).trim();
        editor.putString(fileDir+"  .name", name);
        editor.putBoolean(fileDir+"  .isFile", isFile);
        editor.putBoolean(fileDir+"  .isDel", isDel);
        editor.commit();
    }

    private static void setFilesInDir(SharedPreferences.Editor editor, String dir, String files) {
        editor.putString(dir+"  .files", files);
        editor.commit();
    }

    private static List<String> getFilesInDir(SharedPreferences prefs, String dir) {
        return HelpFunc.split(prefs.getString(dir+"  .files", ""), ' ');
    }

    public void saveFiles() {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String files = "";

        for (cWordFile f: fileList) {
            files += f.getText() + " ";
        }
        files = files.trim();

        setFilesInDir(editor, dir, files);

        for (cWordFile file: fileList) {
            String s = file.getText();
            Boolean isFile = file.isFile();
            Boolean isDel = file.isDel();
            setFilePerms(editor, dir, s, isFile, isDel);
        }
        editor.commit();

    }

    private void getFiles() {
        fileList = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);

        List<String> fileNames = getFilesInDir(prefs, dir);
        String MainPairDir = prefs.getString(mainPairDir, rootDir);
        for (String s: fileNames) {
            Boolean isChecked = MainPairDir.equals((dir+" "+s).trim());
            Boolean isFile = getFileisFile(prefs, dir, s);
            Boolean isDel = getFileisDel(prefs, dir, s);
            addFile(s, isFile, isChecked, isDel);
        }

    }

    private static void createDefaults(SharedPreferences prefs) { //needs improving
        String[] defaultList = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"};
        SharedPreferences.Editor editor = prefs.edit();


        setFilePerms(editor, rootDir, practiceDir, false, false);
        setFilePerms(editor, rootDir, defaultDir, true, false);

        setFilesInDir(editor, rootDir, practiceDir+" "+defaultDir);

        editor.putInt(defaultDir+"  .numpairs", defaultList.length);
        for (int i = 0; i < defaultList.length; i++) {
            editor.putString(defaultDir+"  ."+i+".first", Integer.toString(i+1));
            editor.putString(defaultDir+"  ."+i+".second", defaultList[i]);
        }

        editor.commit();
    }

    public void changeDir(cWordFile file) {
        saveFiles();
        if (file == null) {
            if (dir.equals(rootDir)) {return;}

            int i = dir.lastIndexOf(" ");
            if (i == -1) {i = 0;}
            dir = dir.substring(0, i);

        }
        else {
            dir = (dir+" "+file.getText()).trim();

        }
        while (fileList.size() > 0) {
            removeFile(fileList.remove(0));
        }
        getFiles();
    }

    public void removeFile(cWordFile f) {
        layout.removeView(f.getView());
        fileList.remove(f);
    }

    public void openFile(cWordFile file) {
        Intent intent = new Intent(context, WordPairActivity.class);
        intent.putExtra("fileDir", (dir+" "+file.getText()).trim());
        intent.putExtra("isDel", file.isDel());
        context.startActivity(intent);
    }

    public void addFile(String name, Boolean isFile, Boolean isChecked, Boolean isDel) {
        name = HelpFunc.cleanString(name);
        for (cWordFile f: fileList) {
            String text = f.getText();
            if (text.equals(name)) {
                return;
            }
        }
        fileList.add(new cWordFile(context, name, layout, this, isFile, isDel));
        if (isChecked) {
            fileList.get(fileList.size()-1).setCheckBox(true);
        }
    }

    public void setMainPairs(cWordFile file) {
        setMainPairs((dir+" "+file.getText()).trim());

        for (cWordFile f: fileList) {
            f.setCheckBox(false);
        }
        file.setCheckBox(true);
    }

    private void setMainPairs(String fileDir) {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(rootDir+"  .mainPairDir", fileDir);
        editor.commit();

    }

    public static List<String> getMainPairs(Context context) {
        return getMainPairs(context, false);
    }

    private static List<String> getMainPairs(Context context, Boolean ignorePractice) {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        List<String> pairLines = new ArrayList<>();

        String mainDir = prefs.getString(mainPairDir, rootDir);
        if (mainDir == rootDir) {
            createDefaults(prefs);
            mainDir = prefs.getString(mainPairDir, rootDir);
        }
        else if(SettingsActivity.readPracticeMode(context) == true && !ignorePractice) {
            List<String> fileNames = getFilesInDir(prefs, practiceDir);
            if (fileNames.size() != 0) {
                mainDir = (practiceDir+" "+fileNames.get(new Random().nextInt(fileNames.size()))).trim();
            }
        }


        int numPairs = prefs.getInt(mainDir+"  .numpairs", 0);
        for (int i = 0; i < numPairs; i++) {
            String first= prefs.getString(mainDir+"  ."+i+".first", "");
            String second= prefs.getString(mainDir+"  ."+i+".second", "");
            pairLines.add(first+","+second);
        }
        return pairLines;
    }

    public static List<String> getDefaultPairs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("  .mainPairDir", defaultDir);
        editor.commit();

        return getMainPairs(context, true);
    }

    public static void addPractice(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        copyFile(context, prefs.getString(mainPairDir, rootDir), rootDir+practiceDir);
    }
    public static void copyFile(Context context, String fileDir, String toDir) {
        SharedPreferences prefs = context.getSharedPreferences("WordBank", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String fileName = getFileName(prefs, fileDir);
        editor.putString(toDir+"  .files", (prefs.getString(toDir+"  .files", " ")+" "+fileName).trim());
        setFilePerms(editor, toDir, fileName, true, true);

        toDir = (toDir+" "+fileName).trim();
        int numPairs = prefs.getInt(fileDir+"  .numpairs", 0);
        editor.putInt(toDir+"  .numpairs", numPairs);
        for (int i = 0; i < numPairs; i++) {
            String first= prefs.getString(fileDir+"  ."+i+".first", "");
            String second= prefs.getString(fileDir+"  ."+i+".second", "");

            editor.putString(toDir+"  ."+i+".first",first);
            editor.putString(toDir+"  ."+i+".second",second);
        }

        editor.commit();
    }



}
