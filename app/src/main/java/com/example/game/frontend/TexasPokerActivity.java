package com.example.game.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.R;

import com.example.game.backend.PermanentStatistics;
import com.example.game.backend.TexasPoker.*;

/**
 * the main activity of TexasPoker game
 */
public class TexasPokerActivity extends AppCompatActivity {
    //set a image box of 52 cards
    public ImageBox cardBox = new ImageBox();

    public Deskcards desk;
    public Handcards hand;
    public Changingcards changing;
    private Card chosen;

    private int chosenID;

    protected int score;
    private int round = 1;
    private boolean chooseHandCard = false;

    private ImageView change_1;
    private ImageView change_2;
    private ImageView change_3;
    private ImageView hand_1;
    private ImageView hand_2;
    private ImageView hand_3;
    private ImageView hand_4;
    private ImageView hand_5;
    private ImageView back_button;
    private TextView pass_button;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texas_poker);

        //form a new card desk
        desk = new Deskcards();

        // choose 5 cards from desk
        hand = new Handcards();
        for (int i = 0; i < 5; i++) {
            hand.addCard(i, desk.ChooseHandCard());
        }
        // sort card so that we can compute hand card score easily
        hand.sortHandCards();

        //choose 3 cards from the cards not in hand
        changing = new Changingcards();
        for (int i = 0; i < 3; i++) {
            changing.addCard(i, desk.ChooseChangingCard());
        }

        //back_button to go back to helper
        back_button = findViewById(R.id.back_button);
        //if player don't want to change card this turn, press pass_button
        pass_button = findViewById(R.id.pass);

        //get the score of current hand card
        score = hand.getScore();
        draw();

        //set listener to all the button
        setListener();
    }

    /**
     * this method can draw the cards when the handcards or changingcards have some change
     * get the index of cards and find the image in cardbox
     */
    private void draw(){
        change_1 = findViewById(R.id.change1);
        change_1.setImageResource(cardBox.box[changing.getChangingCards()[0].getIndex()]);

        change_2 = findViewById(R.id.change2);
        change_2.setImageResource(cardBox.box[changing.getChangingCards()[1].getIndex()]);

        change_3 = findViewById(R.id.change3);
        change_3.setImageResource(cardBox.box[changing.getChangingCards()[2].getIndex()]);

        hand_1 = findViewById(R.id.hand1);
        hand_1.setImageResource(cardBox.box[hand.getHandCards()[0].getIndex()]);

        hand_2 = findViewById(R.id.hand2);
        hand_2.setImageResource(cardBox.box[hand.getHandCards()[1].getIndex()]);

        hand_3 = findViewById(R.id.hand3);
        hand_3.setImageResource(cardBox.box[hand.getHandCards()[2].getIndex()]);

        hand_4 = findViewById(R.id.hand4);
        hand_4.setImageResource(cardBox.box[hand.getHandCards()[3].getIndex()]);

        hand_5 = findViewById(R.id.hand5);
        hand_5.setImageResource(cardBox.box[hand.getHandCards()[4].getIndex()]);

        //print the current score
        scoreView = findViewById(R.id.score);
        scoreView.setText("score: " + score);
    }

    /**
     * set listenner to all the button
     */
    private void setListener() {
        OnClick onClick = new OnClick();
        change_1.setOnClickListener(onClick);
        change_2.setOnClickListener(onClick);
        change_3.setOnClickListener(onClick);
        hand_1.setOnClickListener(onClick);
        hand_2.setOnClickListener(onClick);
        hand_3.setOnClickListener(onClick);
        hand_4.setOnClickListener(onClick);
        hand_5.setOnClickListener(onClick);
        back_button.setOnClickListener(onClick);
        pass_button.setOnClickListener(onClick);
    }

    /**
     * define the reaction to listener
     */
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                // these 3 button is button of click card in changingcards
                case R.id.change1:
                    chosenChangingCard(change_1, 0);
                    break;
                case R.id.change2:
                    chosenChangingCard(change_2,1);
                    break;
                case R.id.change3:
                    chosenChangingCard(change_3,2);
                    break;

                // these 5 button is button of click card in handcards
                case R.id.hand1:
                    chosenHandCard(hand_1,0);
                    break;
                case R.id.hand2:
                    chosenHandCard(hand_2,1);
                    break;
                case R.id.hand3:
                    chosenHandCard(hand_3,2);
                    break;
                case R.id.hand4:
                    chosenHandCard(hand_4,3);
                    break;
                case R.id.hand5:
                    chosenHandCard(hand_5,4);
                    break;

                // not change card this turn
                case R.id.pass:
                    passOneRound(pass_button);
                    break;

                //back to game rules
                case R.id.back_button:
                    onBackPressed();
                    finish();
            }

        }
    }

    /**
     * this method is the reaction after player click one of the changing card button
     * @param button which button player chosen
     * @param index the index of changingCards array(0,1,2)
     */
    public void chosenChangingCard(ImageView button, int index){

        // if click the changing card button on the right time
        if(!chooseHandCard) {
            // if game is over
            if(round > 3){
                tipGameOver(button);
            }else{// send information of the card play chosen
                chosen = changing.getChangingCards()[index];
                chosenID = index;

                //choose changing card successfully, change the game status
                chooseHandCard = true;
            }
        }else {//when it's time to choose hand card
            // if game is over
            if (round > 3) {
                tipGameOver(button);
            } else {
                // tell play it's time to choose hand card
                tipChooseHandCard(button);
            }
        }
    }
    /**
     * this method is the reaction after player click one of the hand card button
     * @param button which button player chosen
     * @param index the index of handCards array(0,1,2,3,4)
     */
    public void chosenHandCard(ImageView button, int index){

        // if click the hand card button on the right time
        if(chooseHandCard) {
            if(round > 3){
                tipGameOver(button);
            }else {

                //exchange hand card player chosen and the chosen card in changing cards
                chosen.moveToHand();
                hand.getHandCards()[index].moveToChanging();
                changing.getChangingCards()[chosenID] = hand.getHandCards()[index];
                hand.getHandCards()[index] = chosen;

                // sort hand card after changing
                hand.sortHandCards();

                // put the changing cards back to desk
                for (int i = 0; i < 3; i++) {
                    changing.getChangingCards()[i].moveToDesk();
                }
                // choose 3 changing cards again
                for (int i = 0; i < 3; i++) {
                    changing.addCard(i, desk.ChooseChangingCard());
                }

                // compute the score and draw the screen again
                score = hand.getScore();
                draw();

                // change status of the game
                round++;
                chooseHandCard = false;

                // update permanent statistics when game is over
                if(round == 4){
                    //updatePermanentStatistics();
                }
            }
        }else{//same as method chosenChangingCard
            if(round > 3){
                tipGameOver(button);
            }else {
                tipChooseChangingCard(button);
            }
        }
    }

    /**
     * skip 1 round without doing anything
     * @param button pass_button
     */
    public void passOneRound(TextView button){
        if(round > 3){
            tipGameOver(button);
        }else {
            for (int i = 0; i < 3; i++) {
                changing.getChangingCards()[i].moveToDesk();
            }
            for (int i = 0; i < 3; i++) {
                changing.addCard(i, desk.ChooseChangingCard());
            }
            draw();
            round++;
            chooseHandCard = false;
        }
    }

    /**
     * tip to players when they press the changing card when they supposed to choose a card in hand cards
     * @param view which button they choose
     */
    public void tipChooseHandCard(View view) {
        Context context = getApplicationContext();
        CharSequence text = "It's time to choose a hand card to abandon.";

        //how long the tips shown
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * tip to players when they press the hand card when they supposed to choose a card in changing cards
     * @param view which button they choose
     */
    public void tipChooseChangingCard(View view) {
        Context context = getApplicationContext();
        CharSequence text = "It's time to pick a card from the three cards on the top.";

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * tip to players when they press any button when game is over
     * @param view which button they choose
     */
    public void tipGameOver(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Game is over, click the arrow on the top to quit.";

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

//    /**
//     * update the score to permanent statistics
//     */
//    private void updatePermanentStatistics() {
//        if (score > PermanentStatistics.TexasPokerTopScore) {
//            PermanentStatistics.TexasPokerTopScore = score;
//        }
//    }
}
