package com.example.game.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;

import com.example.game.backend.PermanentStatistics;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchLevelChooser(View view) {
        Intent myIntent = new Intent(MainActivity.this, ChooseLevelActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void launchAccountCreator(View view) {
        Intent myIntent = new Intent(MainActivity.this, CreateAccountActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void login(View view) {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void changeLanguage(View view) {
        Intent myIntent = new Intent(MainActivity.this, ChangeLanguageActivity.class);
        MainActivity.this.startActivity(myIntent);
    }



    public void viewAbout(View view) {
        Context context = getApplicationContext();
        CharSequence text = "About game:\n" +
                "\n" +
                "CSC207 Final Project\n" +
                "by Andrew Gritsevskiy, Zehao Zhou, Eddie Tian, Feiyang Fan, Kainan Ni, and Junyuan Li.\n" +
                "\n" +
                "Music: joshuaempyre at freesound.org.";

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
    }
}
