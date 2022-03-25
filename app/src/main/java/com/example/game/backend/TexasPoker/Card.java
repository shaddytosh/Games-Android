package com.example.game.backend.TexasPoker;

/**
 * the class for every single cards
 */
public class Card {
    /**
     * suits of the cards
     */
    private String suit;

    /**
     * number of the cards, J is 11, Q is 12, K is 13, A is 14
     */
    private int number;

    /**
     * state of the card
     */
    private String state;

    /**
     * Index of the card, we need to sort the cards, use the suits and numbers to determine the index of the cards
     * heart 2 is index 0, heart A is index 12, spade 2 is index 13, diamond 2 is index 26, club 2 is index 39
     * club A has largest index of 51
     */
    private int Index;

    protected Card(){
        state = "inDesk";
    }

    protected void setSuit(String suit) {
        this.suit = suit;
    }

    protected void setNumber(int number) {
        this.number = number;
    }

    public void moveToChanging(){
        this.state = "inChanging";
    }

    public void moveToHand(){
        this.state = "inHand";
    }

    public void moveToDesk(){
        this.state = "inDesk";
    }

    public int computerIndex(String suit, int number){
        int result = 0;
        if (suit.equals("spade")){
            result += 13;
        }else if (suit.equals("diamond")){
            result += 13 * 2;
        }else if (suit.equals("club")){
            result += 13 * 3;
        }
        result += number - 2;
        return result;
    }

    protected void setIndex(int index) {
        this.Index = index;
    }

    public String getSuit() {
        return this.suit;
    }

    public int getNumber() {
        return this.number;
    }

    protected String getState() {
        return this.state;
    }

    public int getIndex() {
        return this.Index;
    }
}
