package com.example.game.frontend;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game.R;
import com.example.game.backend.Platformer.PlatformerManager;


public class PokerRuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poker_rule);

    }

    public void startGame(View view) {
        Intent myIntent = new Intent(PokerRuleActivity.this, TexasPokerActivity.class);
        PokerRuleActivity.this.startActivity(myIntent);
    }

    public void back(View view) {
        onBackPressed();
        finish();
    }
}
