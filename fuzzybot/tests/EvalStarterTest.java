package tests;

import bot.EvalStarter;
import bot.RiskEvaluation;
import com.google.common.collect.ObjectArrays;
import com.stevebrecher.HandEval;
import org.junit.Test;
import poker.Card;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Created by huangster on 2016-03-25.
 */
public class EvalStarterTest {
    @Test
    public void testSomething() {
        String hand1 = "[Ad,Kh]";
        String hand2 = "[2s,2c]";
        EvalStarter es = new EvalStarter();
        Card[] cards = es.parseCards(hand1);
        // useful for seeing how cards are ordered
//        for(Card card : cards){
//            System.out.println(Long.toBinaryString(card.getNumber())); // prints up to the leftmost 1
//            System.out.println(Long.toBinaryString(card.getNumber()).length()); // number of digits in previous line
//            BitSet set = es.longToBooleanHand(card.getNumber());
//            System.out.println(set.length()%16); // score of the card (counting from 1-13 for 2-Ace)
//        }
        assertTrue(true);
    }
    @Test
    public void testHandScorePairOfTwos() {
        String hand2 = "[2s,2c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Pair of 2's have a score of %d", es.handScore(hand2)));
        assertTrue(true);
    }
    @Test
    public void testHandScoreAceKing() {
        String hand1 = "[Ad,Kh]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Ace King offsuit have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScoreJackTenSuited() {
        String hand1 = "[Jd,Td]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Jack Ten suited have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScoreJackEightSuited() {
        String hand1 = "[Jd,8d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Jack 8 suited have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScorePocketAces() {
        String hand1 = "[Ad,Ah]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Pocket Aces have a score of %d", es.handScore(hand1)));
        assertTrue(es.handScore(hand1) == RiskEvaluation.MAX_SCORE_2_CARDS);
    }
    @Test
    public void testHandScoreTwoFourOffsuit() {
        String hand1 = "[2s,4c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("2 4 offsuit have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScoreAceFiveOffsuit() {
        String hand1 = "[Ac,5d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Ace 5 offsuit have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScoreAceFiveSuited() {
        String hand1 = "[Ac,5c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Ace 5 suited have a score of %d", es.handScore(hand1)));
        assertTrue(true);
    }

    @Test
    public void testHandScore2Cards() {
        testHandScoreTwoFourOffsuit();
        testHandScoreAceFiveOffsuit();
        testHandScoreAceFiveSuited();
        testHandScoreJackEightSuited();
        testHandScoreJackTenSuited();
        testHandScoreAceKing();
        testHandScorePairOfTwos();
        testHandScorePocketAces();
    }
    @Test
    public void testHandScore5CardsAceHigh() {
        String hand1 = "[Ac,5c,Tc,9s,2d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsAceHigh2() {
        String hand1 = "[Ac,5c,Tc,9s,4d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsAlmostStraight() {
        String hand1 = "[7c,5c,Ts,9d,6d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsAlmostFlush() {
        String hand1 = "[2c,5c,Tc,9c,6d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsAlmostStraightFlush() {
        String hand1 = "[7c,5c,Tc,9c,6d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost straight or flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsStraight() {
        String hand1 = "[Kc,Jh,Tc,9s,Qd]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsPair() {
        String hand1 = "[Kc,Kh,Ac,9s,Qd]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards2Pair() {
        String hand1 = "[2c,2h,3c,3s,5d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 2 pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards2Pair2() {
        String hand1 = "[Ac,Ah,Kc,Kd,Qs]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 2 pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards3OfAKind() {
        String hand1 = "[4c,4h,4s,2s,7d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 3 set has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsFlush() {
        String hand1 = "[Kc,Ac,Tc,2c,7c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsFullHouse() {
        String hand1 = "[Ks,Kc,Kd,Qh,Qc]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s full house has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsFullHouse2() {
        String hand1 = "[2s,2c,2d,3h,3c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s full house has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards4OfAKind2() {
        String hand1 = "[Ad,Ac,As,Ah,7c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s quads has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards4OfAKind() {
        String hand1 = "[2c,2h,2d,2s,5d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s quads has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsStraightFlush() {
        String hand1 = "[As,2s,3s,4s,5s]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5CardsStraightFlush2() {
        String hand1 = "[Kh,Jh,Th,Ah,Qh]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore5Cards() {
        testHandScore5CardsAceHigh();
        testHandScore5CardsAceHigh2();
        testHandScore5CardsAlmostStraight();
        testHandScore5CardsAlmostFlush();
        testHandScore5CardsAlmostStraightFlush();
        testHandScore5CardsPair();
        testHandScore5Cards2Pair();
        testHandScore5Cards2Pair2();
        testHandScore5Cards3OfAKind();
        testHandScore5CardsStraight();
        testHandScore5CardsFlush();
        testHandScore5CardsFullHouse();
        testHandScore5CardsFullHouse2();
        testHandScore5Cards4OfAKind();
        testHandScore5Cards4OfAKind2();
        testHandScore5CardsStraightFlush();
        testHandScore5CardsStraightFlush2();

//        System.out.println(String.format("Nothing is %d", (HandEval.NO_PAIR)));
//        System.out.println(String.format("Nothing is %d", HandEval.PAIR));
//        System.out.println(String.format("Nothing is %d", HandEval.TWO_PAIR));
//        System.out.println(String.format("Nothing is %d", HandEval.THREE_OF_A_KIND));
//        System.out.println(String.format("Nothing is %d", HandEval.STRAIGHT));
//        System.out.println(String.format("Nothing is %d", HandEval.FLUSH));
//        System.out.println(String.format("Nothing is %d", HandEval.FULL_HOUSE));
//        System.out.println(String.format("Nothing is %d", HandEval.FOUR_OF_A_KIND));
//        System.out.println(String.format("Nothing is %d", HandEval.STRAIGHT_FLUSH));

//        System.out.println(String.format("Nothing is %s, of length %d", Long.toBinaryString(HandEval.NO_PAIR), Long.toBinaryString(HandEval.NO_PAIR).length()));
//        System.out.println(String.format("PAIR is %s, of length %d", Long.toBinaryString(HandEval.PAIR), Long.toBinaryString(HandEval.PAIR).length()));
//        System.out.println(String.format("TWO_PAIR is %s, of length %d", Long.toBinaryString(HandEval.TWO_PAIR), Long.toBinaryString(HandEval.TWO_PAIR).length()));
//        System.out.println(String.format("THREE_OF_A_KIND is %s, of length %d", Long.toBinaryString(HandEval.THREE_OF_A_KIND), Long.toBinaryString(HandEval.THREE_OF_A_KIND).length()));
//        System.out.println(String.format("STRAIGHT is %s, of length %d", Long.toBinaryString(HandEval.STRAIGHT), Long.toBinaryString(HandEval.STRAIGHT).length()));
//        System.out.println(String.format("FLUSH is %s, of length %d", Long.toBinaryString(HandEval.FLUSH), Long.toBinaryString(HandEval.FLUSH).length()));
//        System.out.println(String.format("FULL_HOUSE is %s, of length %d", Long.toBinaryString(HandEval.FULL_HOUSE), Long.toBinaryString(HandEval.FULL_HOUSE).length()));
//        System.out.println(String.format("FOUR_OF_A_KIND is %s, of length %d", Long.toBinaryString(HandEval.FOUR_OF_A_KIND), Long.toBinaryString(HandEval.FOUR_OF_A_KIND).length()));
//        System.out.println(String.format("STRAIGHT_FLUSH is %s, of length %d", Long.toBinaryString(HandEval.STRAIGHT_FLUSH), Long.toBinaryString(HandEval.STRAIGHT_FLUSH).length()));

        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsAceHigh() {
        String hand1 = "[Ac,5c,Tc,9s,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsAlmostStraight() {
        String hand1 = "[7c,5c,Ts,9d,6d,4h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsAlmostFlush() {
        String hand1 = "[2c,5c,Tc,9c,6d,Qh]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsAlmostStraightFlush() {
        String hand1 = "[Ah,5c,6h,9h,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost straight or flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsPair() {
        String hand1 = "[4c,5c,Tc,9s,4d,7d]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsPair2() {
        String hand1 = "[Kc,5c,Ac,9s,Kd,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6Cards2Pair() {
        String hand1 = "[5d,5c,4c,2s,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 2 pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6Cards3OfAKind() {
        String hand1 = "[7c,5c,7s,9s,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 3 set has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsStraight() {
        String hand1 = "[8c,5c,Tc,6s,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsStraight2() {
        String hand1 = "[Ac,5h,2c,3s,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsFlush() {
        String hand1 = "[Ac,5c,Tc,9c,4d,7c]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsFullHouse() {
        String hand1 = "[9d,3c,3s,9s,3d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s full house has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6Cards4OfAKind() {
        String hand1 = "[Ac,Ad,As,Ah,4d,7h]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s quads has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6CardsStraightFlush() {
        String hand1 = "[Kh,Jh,Th,Ah,Qh,Ad]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore6Cards() {
        testHandScore6CardsAceHigh();
        testHandScore6CardsAlmostStraight();
        testHandScore6CardsAlmostFlush();
        testHandScore6CardsAlmostStraightFlush();
        testHandScore6CardsPair();
        testHandScore6CardsPair2();
        testHandScore6Cards2Pair();
        testHandScore6Cards3OfAKind();
        testHandScore6CardsStraight();
        testHandScore6CardsStraight2();
        testHandScore6CardsFlush();
        testHandScore6CardsFullHouse();
        testHandScore6Cards4OfAKind();
        testHandScore6CardsStraightFlush();
    }

    @Test
    public void testHandScore7CardsAceHigh() {
        String hand1 = "[Ac,5d,Tc,9s,4d,7h,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsAlmostStraightFlush() {
        String hand1 = "[Ah,5d,6h,9h,4d,7h,Td]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s almost straight or flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsPair() {
        String hand1 = "[Kc,Kd,Tc,9s,Ad,7h,Qs]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7Cards2Pair() {
        String hand1 = "[5c,5d,2c,9s,4d,2h,8s]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 2 pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7Cards2Pair2() {
        String hand1 = "[5c,5d,3c,9s,4d,Kh,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 2 pair has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7Cards3OfAKind() {
        String hand1 = "[7c,5d,Tc,9s,4d,7h,7s]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s 3 set has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsStraight() {
        String hand1 = "[Ac,5d,2c,3s,4d,7h,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsStraight2() {
        String hand1 = "[6c,5d,Tc,8s,4d,7h,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsFlush() {
        String hand1 = "[Ac,5c,Tc,9s,4d,7c,Kc]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsFullHouse() {
        String hand1 = "[5c,5d,5s,Kh,4d,7h,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s full house has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7Cards4OfAKind() {
        String hand1 = "[9c,9d,9h,9s,4d,7h,Ks]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s quads has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(true);
    }
    @Test
    public void testHandScore7CardsStraightFlush() {
        String hand1 = "[Ac,Qc,Tc,Jc,Ad,Ah,Kc]";
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("%s straight flush has a score of %d", hand1, es.handScore(hand1)));
        assertTrue(es.handScore(hand1) == RiskEvaluation.MAX_SCORE_5_OR_MORE_CARDS);
    }
    @Test
    public void testHandScore7Cards() {
        testHandScore7CardsAceHigh();
        testHandScore7CardsAlmostStraightFlush();
        testHandScore7CardsPair();
        testHandScore7Cards2Pair();
        testHandScore7Cards2Pair2();
        testHandScore7Cards3OfAKind();
        testHandScore7CardsStraight();
        testHandScore7CardsStraight2();
        testHandScore7CardsFlush();
        testHandScore7CardsFullHouse();
        testHandScore7Cards4OfAKind();
        testHandScore7CardsStraightFlush();
    }
    @Test
    public void mockData(){
        String hand = "[4d,4h]";
        String table = "[As,5d,4s]";
        Card[] hand1 = EvalStarter.parseCards(hand);
        Card[] table1 = EvalStarter.parseCards(table);
        Card[] myHand = ObjectArrays.concat(hand1, table1, Card.class);
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand eval: %f",es.normalizedHandScore(myHand)));
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        assertTrue(true);
    }
//    @Test
//    public void mockData(){
//        String hand = "[Th,8d]";
//        String table = "[2h,6c,9h]";
//        Card[] hand1 = EvalStarter.parseCards(hand);
//        Card[] table1 = EvalStarter.parseCards(table);
//        Card[] myHand = ObjectArrays.concat(hand1, table1, Card.class);
//        EvalStarter es = new EvalStarter();
//        System.out.println(String.format("Hand eval: %f",es.normalizedHandScore(myHand)));
//        RiskEvaluation rEval = new RiskEvaluation();
//        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
//        assertTrue(true);
//    }
}
