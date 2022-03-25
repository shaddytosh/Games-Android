package com.example.game.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;
import com.example.game.backend.AlienDefense.UpgradesManager;

public class UpgradesActivity extends AppCompatActivity {

    private int score;
    private long enemiesHit;
    private long timePlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrades);

//        makeMoney();
        score = getIntent().getIntExtra("SCORE", 0);
        enemiesHit = getIntent().getLongExtra("ENEMIES_HIT", 0);
        timePlayed = getIntent().getLongExtra("TIME_PLAYED", 0);

        TextView moneyView = findViewById(R.id.money);
        moneyView.setText("Money: $" + UpgradesManager.getMoney(getApplicationContext()));
    }

    private void makeMoney() {
        UpgradesManager.deposit(getApplicationContext(), 12636726);
    }

    /**
     * Notifies the user that they don't have enough money to make an upgrades purchase.
     */
    private void notEnoughMoney() {
        Context context = getApplicationContext();
        CharSequence text = "Not enough money!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Updates the displayed money the user has.
     */
    private void updateDisplayedMoney() {
        TextView moneyView = findViewById(R.id.money);
        moneyView.setText("Money: $" + UpgradesManager.getMoney(getApplicationContext()));
    }

    /**
     * Purchases the upgrade related to the button that was pressed.
     *
     * @param view - the button pressed. Should contain a tag containing the upgrade id.
     */
    public void buyUpgrade(View view) {
        int which = Integer.parseInt(view.getTag().toString());

        boolean success = UpgradesManager.enableBonus(getApplicationContext(), which);

        if (success) {
            Button p1_button = (Button) view;
            p1_button.setText(getString(R.string.bought));
            p1_button.setEnabled(false);
        } else {
            notEnoughMoney();
        }

        updateDisplayedMoney();
    }

    /**
     * Launches an intent to play again.
     *
     * @param view - the pressed button view.
     */
    public void again(View view) {
        Intent myIntent = new Intent(getApplicationContext(), SpaceInvaders2Activity.class);
        UpgradesActivity.this.startActivity(myIntent);
    }

    /**
     * Launches an intent to go back to the main menu.
     *
     * @param view - the pressed button view.
     */
    public void mainMenu(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ChooseLevelActivity.class);
        myIntent.putExtra("SPACE_SCORE", score);
        myIntent.putExtra("SPACE_ENEMIES_HIT", enemiesHit);
        myIntent.putExtra("SPACE_TIME_PLAYED", timePlayed);
        myIntent.putExtra("SWITCH_GAME", true);
        myIntent.putExtra("USER_NAME", getIntent().getStringExtra("USER_NAME"));
        UpgradesActivity.this.startActivity(myIntent);
    }
}