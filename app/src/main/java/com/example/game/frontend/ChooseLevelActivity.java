package com.example.game.frontend;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;
import com.example.game.backend.PermanentStatistics;
import com.example.game.backend.Platformer.PlatformerManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChooseLevelActivity extends AppCompatActivity {

    private static final String GAME_FILE = "gameData.txt";
    private static PermanentStatistics permanentStatistics;
    private String userName;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        Intent intent = getIntent();
        if (!intent.getBooleanExtra("LOG_IN", false) && !intent.getBooleanExtra("SWITCH_GAME", false)) {
            permanentStatistics = new PermanentStatistics();
        } else if (intent.getBooleanExtra("LOG_IN", false)) {
            permanentStatistics = (PermanentStatistics) ((ArrayList<Object>) intent.getSerializableExtra("GAME_DATA")).get(1);
        }
        updateStatistics(intent);
        userName = intent.getStringExtra("USER_NAME");
        String welcomeString = "Welcome!";
        TextView mTextView = findViewById(R.id.textView3);
        if (userName == null) {
            mTextView.setText(welcomeString);
        } else {
            Resources res = getResources();
            String greetingText = String.format(res.getString(R.string.welcome_string), userName);
            mTextView.setText(greetingText);
        }
    }

    public void launchSpaceShooter(View view) {
        Intent myIntent = new Intent(ChooseLevelActivity.this, SpaceInvaders2Activity.class);
        myIntent.putExtra("USER_NAME", userName); //Optional parameters
        ChooseLevelActivity.this.startActivity(myIntent);
    }

    public void launchPlatformer(View view) {
        Intent myIntent = new Intent(ChooseLevelActivity.this, PlatformerActivity.class);
        boolean gameInProgress = getIntent().getBooleanExtra("GAME_IN_PROGRESS_SAVE", false);
        if (gameInProgress) {
            PlatformerManager platformerManager = (PlatformerManager) getIntent().getSerializableExtra("GAME_MANAGER_SAVE");
            long inGameTime = (long) getIntent().getSerializableExtra("IN_GAME_TIME_SAVE");
            myIntent.putExtra("GAME_IN_PROGRESS_LAUNCH", true);
            myIntent.putExtra("GAME_MANAGER_LAUNCH", platformerManager);
            myIntent.putExtra("IN_GAME_TIME_LAUNCH", inGameTime);
        }
        myIntent.putExtra("USER_NAME", userName);
        ChooseLevelActivity.this.startActivity(myIntent);
    }

    public void launchZombiesVsPlants(View view) {
        Intent myIntent = new Intent(ChooseLevelActivity.this, ZombiesVsPlantsActivity.class);
        myIntent.putExtra("USER_NAME", userName);
        ChooseLevelActivity.this.startActivity(myIntent);
    }

    public void launchTexasPoker(View view) {
        Intent myIntent = new Intent(ChooseLevelActivity.this, PokerRuleActivity.class);
        ChooseLevelActivity.this.startActivity(myIntent);
    }

    public void viewStatistics(View view) {
        Context context = getApplicationContext();
        CharSequence text = "STATISTICS: \n" +
                "\n" +
                "---Space Invaders---\n" +
                "Enemies killed: " + permanentStatistics.alienDefenseTotalEnemiesKilled + "\n" +
                "---Platformer---\n" +
                "Longest time: " + permanentStatistics.platformerLongestTime + "\n" +
                "High score: " + permanentStatistics.platformerTopScore + "\n" +
                "---Zombies Vs. Plants---\n" +
                "Plants killed: " + permanentStatistics.zvpTotalPlantsKilled + "\n" +
                "---Overall Statistics---\n" +
                "Total coins: " + permanentStatistics.totalCoins + "\n" +
                "Total playing time: " + convertMillisecondsToHMS(permanentStatistics.totalPlayingTime) + "\n" +
                "";

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
    private String convertMillisecondsToHMS(long milliseconds) {
        return String.format(Locale.CANADA, "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @SuppressWarnings("unchecked")
    public void logOut(View view) {
        Intent myIntent = new Intent(ChooseLevelActivity.this, MainActivity.class);

        try {
            FileInputStream fi = openFileInput(GAME_FILE);
            ObjectInputStream oi = new ObjectInputStream(fi);
            HashMap<String, ArrayList<Object>> hashMap = (HashMap<String, ArrayList<Object>>) oi.readObject();
            ArrayList<Object> list = hashMap.get(userName);
            list.set(1, permanentStatistics);
            hashMap.put(userName, list);
            oi.close();
            fi.close();
            FileOutputStream fo = openFileOutput(GAME_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            oo.writeObject(hashMap);
            oo.close();
            fo.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + GAME_FILE);
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ChooseLevelActivity.this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
    }

    public void updateStatistics(Intent intent) {
        permanentStatistics.updateSpace(intent.getIntExtra("SPACE_SCORE", 0),
                intent.getLongExtra("SPACE_ENEMIES_HIT", 0),
                intent.getLongExtra("SPACE_TIME_PLAYED", 0));
        permanentStatistics.updatePlatformer(
                intent.getIntExtra("PLATFORMER_IN_GAME_TIME", 0),
                intent.getIntExtra("PLATFORMER_SCORE", 0),
                intent.getLongExtra("PLATFORMER_TOTAL_TIME", 0));
        permanentStatistics.updateZVP(
                intent.getIntExtra("ZVP_PLANTS_KILLED", 0),
                intent.getIntExtra("ZVP_COINS", 0),
                intent.getLongExtra("ZVP_TIME", 0));
    }
}