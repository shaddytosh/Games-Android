package com.example.game.backend.TexasPoker;

/**
 * the class of a desk of 52 cards
 */
public class Deskcards {
    private Card [] deskCards = new Card[52];

    // from index 0 to 51, form every cards into desk
    public Deskcards(){
        for (int i = 0; i < 52; i++){
            if (i < 13){//the first 13 cards are heart
                Card card = new Card();
                card.setSuit("heart");
                card.setNumber(i + 2);
                card.setIndex(i);
                deskCards[i] = card;
            }else if(i < 26){//second 13 cards are spade
                Card card = new Card();
                card.setSuit("spade");
                card.setNumber(i - 13 + 2);
                card.setIndex(i);
                deskCards[i] = card;
            }else if(i < 39){//third 13 cards are diamond
                Card card = new Card();
                card.setSuit("diamond");
                card.setNumber(i - (13 * 2) + 2);
                card.setIndex(i);
                deskCards[i] = card;
            }else {//remain cards are club
                Card card = new Card();
                card.setSuit("club");
                card.setNumber(i -(13 *3)+ 2);
                card.setIndex(i);
                deskCards[i] = card;
            }
        }
    }

    //public Card [] getDeskCards(){
    //return deskCards;
    //}

    /**
     * choose a hand card from desk randomly
     * @return the card we choose
     */
    public Card ChooseHandCard(){
        Card chosen = null;
        while (chosen == null){
            int randomIndex = (int)(Math.random()*52);
            if (randomIndex < 52){
                if (deskCards[randomIndex].getState().equals("inDesk")){
                    deskCards[randomIndex].moveToHand();
                    chosen = deskCards[randomIndex];
                }
            }
        }
        return chosen;
    }

    /**
     * choose a changing card from desk randomly
     * @return the card we choose
     */
    public Card ChooseChangingCard(){
        Card chosen = null;
        while (chosen == null){
            int randomIndex = (int)(Math.random()*52);
            if (randomIndex < 52){
                if (deskCards[randomIndex].getState().equals("inDesk")){
                    deskCards[randomIndex].moveToChanging();
                    chosen = deskCards[randomIndex];
                }
            }
        }
        return chosen;
    }
}
