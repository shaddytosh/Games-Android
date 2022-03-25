package com.example.game.backend.TexasPoker;

/**
 * the class of hand card, include methods of sort hand cards and compute the score of hand cards
 */
public class Handcards {
    private Card [] handCards = new Card[5];

    //these are the variable about card type
    private boolean flush;
    private boolean straight;
    private boolean fourOfAKind;
    private boolean threeOfAKind;
    private int pair;
    private int score = 0;

    // helper variable of sort the card type of pairs
    private int pairLocation1 = -1;
    private int pairLocation2 = -1;

    //base score of different card type
    final int base = 14*13*12*11*10;
    final int[] scoreBase = {13*12*11*10, 12*11*10, 11*10, 10, 1};

    /**
     * add a newCard to the hand card at the location of i
     * @param i location to add
     * @param newCard card to add
     */
    public void addCard(int i, Card newCard){
        this.handCards[i] = newCard;
    }

    public Card [] getHandCards(){
        return this.handCards;
    }

    /**
     * a big method to sort hand card and determine some type status of hand card
     */
    public void sortHandCards(){
        flush = true;
        straight = true;
        fourOfAKind = false;
        threeOfAKind = false;
        pair = 0;

        //sort the cards from small number to large
        for(int i = 0; i < 5; i++){
            for (int j = i + 1; j < 5; j++){
                if(handCards[i].getNumber() > handCards[j].getNumber()){
                    changeLocation(i,j);
                }
            }
        }

        //determine if it is flush, that is, all cards with same suit
        //also determine if it is straight, looks like(2,3,4,5,6)or(10,J,Q,K,A)
        for(int i = 0; i < 5; i++){
            if(!handCards[i].getSuit().equals(handCards[0].getSuit())){
                flush = false;
            }
            if(handCards[i].getNumber() != handCards[0].getNumber() + i){
                straight = false;
            }
        }

        //when it is not flush or straight
        if((!flush)&&(!straight)){

            if(handCards[0].getNumber() == handCards[3].getNumber()){//the first type of four of a kind
                fourOfAKind = true;

                //change the location, 4 same number cards at end
                changeLocation(0,4);

            }else if(handCards[1].getNumber() == handCards[4].getNumber()){// the second type of four of a kind
                fourOfAKind = true;

            }else if(handCards[0].getNumber() == handCards[2].getNumber()){// the first type of three of a kind
                threeOfAKind = true;

                //change the location ,so that 3 same number cards at end
                changeLocation(0,3);
                changeLocation(1,4);

                // check if it is a FULL HOUSE, looks like (2,2,3,3,3)
                if(handCards[0].getNumber() == handCards[1].getNumber()){
                    pair++;
                }

            }else if(handCards[1].getNumber() == handCards[3].getNumber()){// the second type of three of a kind
                threeOfAKind = true;
                changeLocation(1,4);

            }else if(handCards[2].getNumber() == handCards[4].getNumber()){// the third type of three of a kind
                threeOfAKind =true;
                if(handCards[0].getNumber() == handCards[1].getNumber()){
                    pair++;
                }

            }else {

                //remaining card types can be 1 pair or 2 pairs
                // check how many types does it has
                for(int i = 0; i < 4; i++){
                    if(handCards[i].getNumber() == handCards[i+1].getNumber()){
                        pair++;
                        if (pair == 1){
                            pairLocation1 = i;
                        }else {
                            pairLocation2 = i;
                        }
                    }
                }
                // 1 pair
                if(pair == 1){

                    //move pair to the end and sort other 3 cards from small to large
                    if (pairLocation1 == 2) {
                        changeLocation(pairLocation1, 4);
                    }else if (pairLocation1 == 1){
                        changeLocation(1,3);
                        changeLocation(2,4);
                    }else if(pairLocation1 == 0){
                        changeLocation(0,2);
                        changeLocation(1,3);
                        changeLocation(2,4);
                    }
                }else if (pair == 2){

                    //if it is 2 pairs, move the pair with larger number to location 3 and 4
                    // smaller pair to location 1 and 2, that single card to location 1
                    if (pairLocation1 == 0){
                        if(pairLocation2 == 2){
                            if (handCards[pairLocation1].getNumber() > handCards[pairLocation2].getNumber()){
                                changeLocation(1,3);
                                changeLocation(0,4);
                            }else {
                                changeLocation(2,4);
                                changeLocation(0,2);
                            }
                        }else {
                            if (handCards[pairLocation1].getNumber() > handCards[pairLocation2].getNumber()){
                                changeLocation(1,3);
                                changeLocation(0,4);
                                changeLocation(0,2);
                            }else {
                                changeLocation(0, 2);
                            }
                        }
                    }else {
                        if (handCards[pairLocation1].getNumber() > handCards[pairLocation2].getNumber()){
                            changeLocation(1,3);
                            changeLocation(2,4);
                        }
                    }
                }
            }
        }

        //computeScore after all the sorting things are done
        computeScore();
    }

    /**
     * compute score with different type
     */

    public void computeScore(){
        if(flush){
            if(straight){// straight flush is biggest type, so it has largest base
                score = 8*base + handCards[4].getNumber()*scoreBase[0];

            }else{//type: flush
                int [] array = {handCards[4].getNumber(),handCards[3].getNumber(),handCards[2].getNumber(),handCards[1].getNumber(),handCards[0].getNumber()};
                score = 5*base + computeScoreByArray(array);
            }
        }else {
            if (straight){//type: straight
                score = 4*base + handCards[4].getNumber()*scoreBase[0];

            }else {
                if (fourOfAKind){//type: four of a kind
                    int[] array = {handCards[1].getNumber(),handCards[0].getNumber()};
                    score = 7*base + computeScoreByArray(array);

                }else{
                    if(threeOfAKind){
                        if(pair == 1){//type: full house
                            int[] array = {handCards[2].getNumber(),handCards[0].getNumber()};
                            score = 6*base + computeScoreByArray(array);

                        }else if(pair == 0){//type: three of a kind
                            int[] array = {handCards[2].getNumber(),handCards[1].getNumber(),handCards[0].getNumber()};
                            score = 3*base + computeScoreByArray(array);

                        }
                    }else {
                        if(pair == 2){//type: two pairs
                            int[] array = {handCards[3].getNumber(),handCards[1].getNumber(),handCards[0].getNumber()};
                            score = 2*base + computeScoreByArray(array);

                        }else if(pair == 1){//type: one pair
                            int[] array = {handCards[3].getNumber(),handCards[2].getNumber(),handCards[1].getNumber(),handCards[0].getNumber()};
                            score = base + computeScoreByArray(array);

                        }else {//type: high card
                            int[] array = {handCards[4].getNumber(),handCards[3].getNumber(),handCards[2].getNumber(),handCards[1].getNumber(),handCards[0].getNumber()};
                            score = computeScoreByArray(array);
                        }
                    }
                }
            }
        }
    }

    /**
     * use useful number array of hand cards to compute score after determine the card type base
     * @param array useful number array, the hand card (2,3,3,4,4) will have a array of {4,3,2}
     * @return the score after determine the card type base
     */
    public int computeScoreByArray(int[] array){
        int output = 0;
        for(int i = 0; i < array.length; i++){
            output += array[i] * scoreBase[i];
        }
        return output;
    }

    /**
     * change the location of two cards
     * @param cardA the index of first card
     * @param cardB the index of second card
     */
    public void changeLocation(int cardA, int cardB){
        Card tempCard;
        tempCard = handCards[cardA];
        handCards[cardA] = handCards[cardB];
        handCards[cardB] = tempCard;
    }

    public int getScore() {
        return score;
    }
}
