package com.example.game.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;
import com.example.game.backend.PermanentStatistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

//reference: https://www.mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
public class CreateAccountActivity extends AppCompatActivity {

    private static final String GAME_FILE = "gameData.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    @SuppressWarnings("unchecked")
    public void createAccount(View view) {
        boolean createAccountSuccess = false;
        ArrayList<Object> gameData = new ArrayList<>();
        EditText userName = findViewById(R.id.editText);
        EditText password = findViewById(R.id.editText2);
        String actualUserName = userName.getText().toString();
        String actualPassword = password.getText().toString();
        if (actualUserName.equals("") || actualPassword.equals("")) {
            displayWarning("Invalid Inputs");
            return;
        }
        gameData.add(actualPassword);
        gameData.add(new PermanentStatistics());
        File rootDirectory = this.getFilesDir();
        System.out.println(rootDirectory);
        if (fileExists(this, GAME_FILE)) {
            System.out.println("file read: " + GAME_FILE);
            //read gameData.txt, get hashMap inside, update hashMap and overwrite in file
            try {
                FileInputStream fi = openFileInput(GAME_FILE);
                ObjectInputStream oi = new ObjectInputStream(fi);
                HashMap<String, ArrayList<Object>> hashMap = (HashMap<String, ArrayList<Object>>) oi.readObject();
                //if userName already exists then do not overwrite, do not start activity
                if (!hashMap.containsKey(actualUserName)) {
                    hashMap.put(actualUserName, gameData);
                    oi.close();
                    fi.close();
                    FileOutputStream fo = openFileOutput(GAME_FILE, Context.MODE_PRIVATE);
                    ObjectOutputStream oo = new ObjectOutputStream(fo);
                    oo.writeObject(hashMap);
                    oo.close();
                    fo.close();
                    createAccountSuccess = true;
                } else {
                    oi.close();
                    fi.close();
                    displayWarning("Used User Name");
                    return;
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + GAME_FILE);
            } catch (IOException e) {
                System.out.println("Error initializing stream");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //create a new gameData file
            try {
                System.out.println("file created: " + GAME_FILE);
                HashMap<String, ArrayList<Object>> hashMap = new HashMap<>();
                FileOutputStream f = openFileOutput(GAME_FILE, Context.MODE_PRIVATE);
                ObjectOutputStream o = new ObjectOutputStream(f);
                hashMap.put(actualUserName, gameData);
                o.writeObject(hashMap);
                o.close();
                f.close();
                createAccountSuccess = true;
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + GAME_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (createAccountSuccess) {
            Intent myIntent = new Intent(CreateAccountActivity.this, ChooseLevelActivity.class);
            myIntent.putExtra("USER_NAME", actualUserName);
            CreateAccountActivity.this.startActivity(myIntent);
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return (file != null && file.exists());
    }

    private void displayWarning(String warning) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, warning, duration);
        toast.show();
    }

}
