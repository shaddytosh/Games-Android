package com.example.game.backend.TexasPoker;

public class Changingcards {
    private Card [] changingCards = new Card[3];

    public void addCard(int i, Card newCard){
        this.changingCards[i] = newCard;
    }

    public Card[] getChangingCards() {
        return changingCards;
    }
}
