package com.example.game.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String GAME_FILE = "gameData.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //reads gameData.txt, reads Info entered, determine if log in success and display warning if not
    //if log in success reads gameData and give it to level chooser.
    @SuppressWarnings("unchecked")
    public void login(View view) {
        boolean logInSuccess = false;
        ArrayList<Object> gameData = new ArrayList<>();
        EditText userName = findViewById(R.id.editText3);
        EditText password = findViewById(R.id.editText4);
        String actualUserName = userName.getText().toString();
        String actualPassword = password.getText().toString();
        if (actualUserName.equals("") || actualPassword.equals("")) {
            displayWarning("Invalid Inputs");
            return;
        }
        try {
            FileInputStream fi = openFileInput(GAME_FILE);
            ObjectInputStream oi = new ObjectInputStream(fi);
            HashMap<String, ArrayList<Object>> hashMap = (HashMap<String, ArrayList<Object>>) oi.readObject();
            //if User exists
            if (hashMap.containsKey(actualUserName)) {
                gameData = hashMap.get(actualUserName);
                if (gameData != null) {
                    //if password is incorrect/correct
                    if (!gameData.get(0).equals(actualPassword)) {
                        displayWarning("Invalid password");
                    } else {
                        logInSuccess = true;
                    }
                }

            //if User does not exist
            } else {
                displayWarning("User does not exist");
            }
            oi.close();
            fi.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + GAME_FILE);
            displayWarning("Please create an account first");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (logInSuccess) {
            Intent myIntent = new Intent(LoginActivity.this, ChooseLevelActivity.class);
            myIntent.putExtra("USER_NAME", actualUserName);
            myIntent.putExtra("GAME_DATA", gameData);
            myIntent.putExtra("LOG_IN", true);
            LoginActivity.this.startActivity(myIntent);
        }
    }
    private void displayWarning(String warning) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, warning, duration);
        toast.show();
    }
}
