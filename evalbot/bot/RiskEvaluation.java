package bot;

import com.google.common.collect.ObjectArrays;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import poker.Card;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huangster on 2016-03-27.
 */
public class RiskEvaluation {
    /**
     * Sourced from https://en.wikipedia.org/wiki/Poker_probability_%28Texas_hold_%27em%29
     *
     * There are 1326 starting hands in poker, but only 169 distinct hands (13*13)
     * Pocket pair: unique combinations = 13, total = 4C2 * 13 = 6*13 = 78
     * Suited cards: unique combinations = 78 total = 4C1 * 78 = 4*78 = 312
     * Unsuited unpaired: unique combinations = 78, total = 4C1 * 3C1 * 78 = 12*78 = 936
     */
    /**
     * Out of 1326 hands
     * When there are 3 of the same suit
     * 1. compare 2 more cards of that suit = 10 of that suit left: 10*(11)/2 = 55
     * 2. compare 1 more card of that suit and one not: 1C1
     * 3. compare 2 cards not of that suit: (3C1 * 2C1) * 78 = 468 (780 total hands)
     * 4. pair with 2 of not that suit: 3C2*13 = 39
     * 5. pair with 1 of that suit and one not: 1C1 * 3C1 * 13 = 39
     */

    static final Logger logger = LogManager.getLogger(RiskEvaluation.class);

    class IntObj{
        int value;
        IntObj(){
            this(0);
        }
        IntObj(int v){
            value = v;
        }
    }

    public static final int MAX_SCORE_2_CARDS = 52;
    public static final int MAX_SCORE_5_OR_MORE_CARDS = 377;

    private static final int PAIR_COMBINATION_MULTIPLIER = 6;
    private static final int SUITED_COMBINATION_MULTIPLIER = 4;
    private static final int UNSUITED_COMBINATION_MULTIPLIER = 12;

    public double riskEval(String myCards, String tableCards){
        return riskEval(myCards, tableCards, true);
    }
    public double riskEval(String myCards, String tableCards, boolean takeSqrt){
        logger.info(String.format("Evaluating risk of %s with table of %s", myCards, tableCards));
        if(takeSqrt){
            return Math.sqrt(riskEval(parseCards(myCards), parseCards(tableCards))); // square rooting for more even distribution
        }
        return riskEval(parseCards(myCards), parseCards(tableCards));
    }

    /**
     *
     * @param myCards cards dealt to me (2 cards)
     * @param tableCards (cards on the table 0, 3, 4, 5)
     * @return risk between 0 and 1
     */
    public double riskEval(Card[] myCards, Card[] tableCards){
        if(tableCards == null || tableCards.length == 0){
            return riskEval2Cards(myCards);
        }
        else if(tableCards.length == 3 || tableCards.length == 4 || tableCards.length == 5){
            long startTime = System.currentTimeMillis();
            double returnValue = riskEval5PlusCards(myCards, tableCards);
            long endTime = System.currentTimeMillis();
            logger.info(String.format("It took %d ms to risk evaluate 5 cards", (endTime-startTime)));
            return returnValue;
        }
        return riskEval2Cards(myCards);
    }

    private double riskEval2Cards(Card[] myCards){
        logger.info("Risk Evaluating 2 cards");
        EvalStarter es = new EvalStarter();
        int myPoints = es.handScore(myCards);
        int lessThanMyPoints = 0;
        int moreThanMyPoints = 0;
        IntObj lessThanTally = new IntObj();
        IntObj moreThanTally = new IntObj();
        Card[] opponentCards = new Card[2];
        for(int i = 0; i < 13; ++i){
            // let card i be a spade (2 - Ace)
            Card card1 = new Card(i);
            opponentCards[0] = card1;
            for(int j = i+1; j < 13; ++j){
                // let card j be either a spade or a heart (3 - Ace)
                Card card2_suited = new Card(j); // spade
                Card card2_unsuited = new Card(j+13); // heart
                opponentCards[1] = card2_suited;
                comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, SUITED_COMBINATION_MULTIPLIER);
                opponentCards[1] = card2_unsuited;
                comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, UNSUITED_COMBINATION_MULTIPLIER);
            }
            Card card2 = new Card(i+13); // pair of spade and heart
            opponentCards[1] = card2;
            comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, PAIR_COMBINATION_MULTIPLIER);
        }
        logger.info(String.format("Hands worth more than %s: %d", Arrays.toString(myCards), moreThanTally.value));
        logger.info(String.format("Hands worth less than or equal to %s: %d", Arrays.toString(myCards), lessThanTally.value));
        return (double)(moreThanTally.value) / (moreThanTally.value + lessThanTally.value);
    }
    private double riskEval5PlusCards(Card[] myCards, Card[] tableCards){
        logger.info("Risk Evaluating 5 cards");
        Card[] cards = ObjectArrays.concat(myCards, tableCards, Card.class);
        List<Card> baseCards = Arrays.asList(cards);
        EvalStarter es = new EvalStarter();
        int myPoints = es.handScore(cards);
//        CardSuit highestOccurringSuit = CardSuit.DIAMONDS;
//        int highestSuitOccurrence = 0;
//        int[] suitCounter = new int[4]; // spade(0), heart(1), club(2), diamond(3)
//        for(Card card : tableCards){
//            suitCounter[card.getSuit().getValue()]++;
//            if(suitCounter[card.getSuit().getValue()] > highestSuitOccurrence){
//                highestSuitOccurrence = suitCounter[card.getSuit().getValue()];
//                highestOccurringSuit = card.getSuit();
//            }
//        }
        IntObj lessThanTally = new IntObj();
        IntObj moreThanTally = new IntObj();
        Card[] opponentCards = new Card[tableCards.length + 2];
        System.arraycopy(tableCards, 0, opponentCards, 2, tableCards.length);
        for(int i = 0; i < 51; ++i){
            Card card1 = new Card(i);
            if(baseCards.contains(card1)){
                continue;
            }
            opponentCards[0] = card1;
            Card card2;
            for(int j = i+1; j < 52; ++j){
                card2 = new Card(j);
                if(baseCards.contains(card2)){
                    continue;
                }
                opponentCards[1] = card2;
                int currentMoreThan = moreThanTally.value;
                comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, 1);
                if(moreThanTally.value > currentMoreThan){
                    logger.info(String.format("%s (%d) beats my hand of %s (%d)", Arrays.toString(opponentCards), es.handScore(opponentCards), baseCards, myPoints));
                }
            }
        }
        // basically same as when there are 2 cards
//        if(highestSuitOccurrence == 1){
//            for(int i = 0; i < 13; ++i){
//                // let card i be a spade (2 - Ace)
//                Card card1 = new Card(i);
//                opponentCards[0] = card1;
//                for(int j = i+1; j < 13; ++j){
//                    // let card j be either a spade or a heart (3 - Ace)
//                    Card card2_suited = new Card(j); // spade
//                    Card card2_unsuited = new Card(j+13); // heart
//                    opponentCards[1] = card2_suited;
//                    comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, SUITED_COMBINATION_MULTIPLIER);
//                    opponentCards[1] = card2_unsuited;
//                    comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, UNSUITED_COMBINATION_MULTIPLIER);
//                }
//                Card card2 = new Card(i+13); // pair of spade and heart
//                opponentCards[1] = card2;
//                comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, PAIR_COMBINATION_MULTIPLIER);
//            }
//        }
//        // with 3 of the same suit on the board, there are better scores if the opponent has that suit in hand
//        else if(highestSuitOccurrence == 3){
//            // pairs?
//            // no card of that suit on the board
//            int notSameSuit = (highestOccurringSuit.getValue() - 1)>=0?(highestOccurringSuit.getValue() - 1):(highestOccurringSuit.getValue() - 1 + 4);
//            int notSameSuit2 = (notSameSuit-1)>=0?(notSameSuit-1):(notSameSuit-1+4);
//            for(int i = 0; i < 13; ++i){
//                Card card1 = new Card(i+13*notSameSuit);
//                opponentCards[0] = card1;
//                for(int j = i+1; j < 13; ++j){
//                    Card card2_unsuited = new Card(j+13*notSameSuit2);
//                    opponentCards[1] = card2_unsuited;
//                    comparePointsAndTally(myPoints, es.handScore(opponentCards), lessThanTally, moreThanTally, UNSUITED_COMBINATION_MULTIPLIER/2);
//                }
//            }
//            // 1 card of that suit on the board
//            // 2 cards of that suit on the board
//
//        }
        logger.info(String.format("Hands worth more than %s: %d", Arrays.toString(myCards), moreThanTally.value));
        logger.info(String.format("Hands worth less than or equal to %s: %d", Arrays.toString(myCards), lessThanTally.value));
        return (double)(moreThanTally.value) / (moreThanTally.value + lessThanTally.value);
    }

    // updates the tallies to check how many opponent hands are worth more/less than mine
    // be careful with the IntObj items, may need refactoring to be more OO
    private void comparePointsAndTally(int myPoints, Card[] opponentCards, IntObj lessTally, IntObj moreTally, int multiplier){
        EvalStarter es = new EvalStarter();
        comparePointsAndTally(myPoints, es.handScore(opponentCards), lessTally, moreTally, multiplier);
    }
    private void comparePointsAndTally(int myPoints, int opponentPoints, IntObj lessTally, IntObj moreTally, int multiplier){
        if(opponentPoints > myPoints){
            moreTally.value += multiplier;
        }
        else{
            lessTally.value += multiplier;
        }
    }
    /**
     * Parse the input string from the engine to actual Card objects
     * @param value : input
     * @return Card[] : array of Card objects
     */
    public static Card[] parseCards(String value) {
        if( value.endsWith("]") ) { value = value.substring(0, value.length()-1); }
        if( value.startsWith("[") ) { value = value.substring(1); }
        if( value.length() == 0 ) { return new Card[0]; }
        String[] parts = value.split(",");
        Card[] cards = new Card[parts.length];
        for( int i = 0; i < parts.length; ++i ) {
            cards[i] = Card.getCard(parts[i]);
        }
        return cards;
    }
}
