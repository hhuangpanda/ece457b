package tests;


import bot.EvalStarter;
import bot.RiskEvaluation;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by huangster on 2016-03-27.
 */
public class RiskEvaluationTest {
    @Test
    public void testRiskEval2CardsTwoThreeOffsuit() {
        String hand1 = "[2s,3c]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("2 4 offsuit have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsTwoFiveOffsuit() {
        String hand1 = "[2s,5c]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("2 5 offsuit have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsTwoSevenOffsuit() {
        String hand1 = "[2s,7c]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("2 7 offsuit have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsNineTenSuited() {
        String hand1 = "[9d,Td]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("9 10 suited have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsDeuces() {
        String hand1 = "[2s,2c]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of 2s have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsSevens() {
        String hand1 = "[7s,7c]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of 7s have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsQueenSixSuited() {
        String hand1 = "[Qh,6h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Q 6 suited have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsAceKing() {
        String hand1 = "[As,Kh]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("A K offsuit have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2CardsKings() {
        String hand1 = "[Ks,Kc]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of Kings have a risk of %f", rEval.riskEval(hand1, "")));
        assertTrue(true);
    }
    @Test
    public void testRiskEval2Cards(){
        testRiskEval2CardsTwoThreeOffsuit();
        testRiskEval2CardsTwoFiveOffsuit();
        testRiskEval2CardsTwoSevenOffsuit();
        testRiskEval2CardsNineTenSuited();
        testRiskEval2CardsDeuces();
        testRiskEval2CardsSevens();
        testRiskEval2CardsQueenSixSuited();
        testRiskEval2CardsAceKing();
        testRiskEval2CardsKings();
    }

    @Test
    public void testRiskEval5CardsKingsOverPair() {
        String hand = "[Ks,Kc]";
        String table = "[Td,4d,9s]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of Kings have a risk of %f", rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsKings2Pair() {
        String hand = "[Ks,Kc]";
        String table = "[Ad,9d,9s]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of Kings have a risk of %f", rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsAlmostStraight(){
        String hand = "[Th,8d]";
        String table = "[2h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsAlmostFlush(){
        String hand = "[3h,7h]";
        String table = "[2h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsAlmostStraightFlush(){
        String hand = "[3h,5h]";
        String table = "[4h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5Cards3OfAKind(){
        String hand = "[9c,9d]";
        String table = "[4h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5Cards3OfAKind2(){
        String hand = "[4c,4d]";
        String table = "[4h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5Cards3OfAKind3(){
        String hand = "[6s,6d]";
        String table = "[4h,6c,9h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsStraight(){
        String hand = "[5s,7d]";
        String table = "[4h,6c,8h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsFlush(){
        String hand = "[2h,7h]";
        String table = "[4h,Kh,8h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval5CardsFlush2(){
        String hand = "[2h,Ah]";
        String table = "[4h,Kh,8h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6CardsKingsOverPair() {
        String hand = "[Ks,Kc]";
        String table = "[Td,4d,9s,2h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Pair of Kings have a risk of %f", rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6CardsTopPair() {
        String hand = "[Ts,Kc]";
        String table = "[Td,4d,9s,2h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f", rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6CardsBottomPair() {
        String hand = "[2s,Kc]";
        String table = "[Td,4d,9s,2h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f", rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6Cards3OfAKind(){
        String hand = "[6s,6d]";
        String table = "[4h,6c,9h,Jc]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6CardsWeakFlush(){
        String hand = "[2h,Td]";
        String table = "[4h,Kh,8h,7h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval6CardsStrongFlush(){
        String hand = "[Ah,Td]";
        String table = "[4h,Kh,8h,7h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }

    @Test
    public void testRiskEval7CardsWeakFlush(){
        String hand = "[2d,Td]";
        String table = "[4h,Kh,8h,7h,2h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
    @Test
    public void testRiskEval7CardsStrongFlush(){
        String hand = "[5d,Ah]";
        String table = "[4h,Kh,8h,7h,2h]";
        RiskEvaluation rEval = new RiskEvaluation();
        System.out.println(String.format("Risk eval: %f",rEval.riskEval(hand, table)));
        EvalStarter es = new EvalStarter();
        System.out.println(String.format("Hand Eval: %f", es.normalizedHandScore(hand, table)));
        assertTrue(true);
    }
}

