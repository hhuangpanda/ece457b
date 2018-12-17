package bot;

import com.google.common.collect.ObjectArrays;
import com.stevebrecher.HandEval;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import poker.Card;
import poker.CardHeight;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by huangster on 2016-03-25.
 */
public class EvalStarter{
    static final Logger logger = LogManager.getLogger(EvalStarter.class);
    // Engine Order is Space, Heart, Club, Diamond (2-Ace) right to left in the long integer
    private static final int SUITED_BONUS = 8;
    private static final int HIGH_BONUS = 8; // receive bonus when above this card height (ten)
    private static final int PAIR_BONUS = 24;
    private static final int CONNECTED_BONUS = 4;
    private static final int HALFCONNECTED_BONUS = 2; // gutshot not as good as open-ended straight

    // bonuses for full hands
    private static final int TWO_PAIR_BONUS = 48;
    private static final int THREE_OF_A_KIND_BONUS = 96;
    private static final int STRAIGHT_BONUS = 144;
    private static final int FLUSH_BONUS = 192;
    private static final int FULL_HOUSE_BONUS = 240;
    private static final int FOUR_OF_A_KIND_BONUS = 288;
    private static final int STRAIGHT_FLUSH_BONUS = 336;

    // http://www.pokerology.com/lessons/drawing-odds/
    private static final int FOUR_OF_FIVE_FLUSH_BONUS = (int)(FLUSH_BONUS * 0.35); // 35% to hit your flush on turn or river
    private static final int FOUR_OF_SIX_FLUSH_BONUS = (int)(FLUSH_BONUS * 0.196); // 19.6% to hit your flush on river
    // having three of the same suit actually is about the same as a suited bonus (8.015)
    private static final int THREE_OF_FIVE_FLUSH_BONUS = (int)(FLUSH_BONUS * 0.213 * 0.196); // hit 10 outs on turn and 9 on river

    private static final int FOUR_OF_FIVE_STRAIGHT_BONUS = (int)(STRAIGHT_BONUS * 0.315); // 31.5% to hit 8 outs
    private static final int FOUR_OF_FIVE_GUTSHOT_BONUS = (int)(STRAIGHT_BONUS * 0.165); // 4 outs in a gutshot
    private static final int FOUR_OF_SIX_STRAIGHT_BONUS = (int)(STRAIGHT_BONUS * 0.174); // 17.4% to hit straight on river
    private static final int FOUR_OF_SIX_GUTSHOT_BONUS = (int)(STRAIGHT_BONUS * 0.087); // 8.7% to hit gutshot on river
    // three of five is about the same as the connected bonus (4.26)
    private static final int THREE_OF_FIVE_STRAIGHT_BONUS = (int)(STRAIGHT_BONUS * 0.17 * 0.174); // must hit 8 outs on both turn and river (oversimplified)

    // modifier for accounting relative strength of cards with 6 or 7 card hand and max card bonus
    private static final int RELATIVE_STRENGTH_BUFFER_7 = 14;
    private static final int RELATIVE_STRENGTH_MULTIPLIER_7 = 4;
    private static final int RELATIVE_STRENGTH_BUFFER_6 = 14;
    private static final int RELATIVE_STRENGTH_MULTIPLIER_6 = 3;
    private static final int MAX_CARD_DIVISOR = 4;

    public double normalizedHandScore(String cardsString){
        logger.info(String.format("Evaluating normalized %s", cardsString));
        Card[] cards = parseCards(cardsString);
        return normalizedHandScore(cards);
    }

    public double normalizedHandScore(String cardsString, String tableString)
    {
        Card[] cards = parseCards(cardsString);
        Card[] table = parseCards(tableString);
        Card[] allCards = ObjectArrays.concat(cards, table, Card.class);
        return normalizedHandScore(allCards);
    }

    public double normalizedHandScore(Card[] cards, Card[] table) {
        Card[] allCards = ObjectArrays.concat(cards, table, Card.class);
        return normalizedHandScore(allCards);
    }

    public double normalizedHandScore(Card[] cards){
        return normalizedHandScore(cards, true);
    }

    public double normalizedHandScore(Card[] cards, boolean takeSqrt){
        if(cards.length == 2){
            return handScore2Cards(cards)/52.0; // best score for 2 cards
        }
        if(takeSqrt){
            return Math.sqrt(handScore(cards)/377.0); // best score period for 7 cards, square rooted because bonuses are assigned by doubling per stronger card
        }
        return (handScore(cards)/377.0); // out of best score for 7 cards
    }

    /**
     * Give this a card string e.g. [2d,3h] and it will spit out a score (must be 2, 5, 6, or 7 cards)
     * @param cardsString
     * @return
     */
    public int handScore(String cardsString)
    {
//        logger.info(String.format("Evaluating %s", cardsString));
        Card[] cards = parseCards(cardsString);
        return handScore(cards);
    }

    public int handScore(String cardsString, String tableString)
    {
        Card[] cards = parseCards(cardsString);
        Card[] table = parseCards(tableString);
        Card[] allCards = ObjectArrays.concat(cards, table, Card.class);
        return handScore(allCards);
    }

    public int handScore(Card[] cards){
        logger.info(String.format("Evaluating %s", Arrays.toString(cards)));
        if(cards.length == 2){
            return handScore2Cards(cards);
        }
        else if(cards.length == 5){
            return handScore5Cards(cards);
        }
        else if(cards.length == 6){
            return handScore6Cards(cards);
        }
        else if(cards.length == 7){
            return handScore7Cards(cards);
        }
        return handScore2Cards(cards);
    }

    public static int handScore2Cards(Card[] cards){
        logger.info("Calculating points for 2 cards");
        int total = 0;
        int val1 = cards[0].getHeight().getValue();
        int val2 = cards[1].getHeight().getValue();
        int highCard = Math.max(val1, val2);
        int lowCard = Math.min(val1, val2);
        // pair
        if(val1 == val2){
            total += PAIR_BONUS;
            logger.log(Level.INFO, "pair bonus");
        }
        else{
            // potential flush
            if(cards[0].getSuit() == cards[1].getSuit()){
                total += SUITED_BONUS;
                logger.log(Level.INFO, "same suit");
            }
            // Aces can't have open-ended straights
            if(highCard == CardHeight.ACE.getValue()){
                // using Ace as low straight (A2 to A5)
                if(highCard - lowCard >= 9){
                    total += HALFCONNECTED_BONUS;
                    logger.log(Level.INFO, "half connected, Ace low roll");
                }
                // using Ace has high straight (AK to AT)
                else if(highCard - lowCard < 5){
                    total += HALFCONNECTED_BONUS;
                    logger.log(Level.INFO, "half connected, Ace high roll");
                }
            }
            // potential open-ended straight eg. KQ or KJ
            else if(Math.abs(val1 - val2) < 3){
                total += CONNECTED_BONUS;
                logger.log(Level.INFO, "connected, potential open ended straight");
            }
            else if(Math.abs(val1 - val2) < 5){
                // potential gutshot straight eg. KT or K9
                total += HALFCONNECTED_BONUS;
                logger.log(Level.INFO, "connected, potential gutshot straight");
            }
        }
        // high card is important for heads up and as kicker, applies to face card or better
        int highCardBonus = highCard - HIGH_BONUS;
        if(highCardBonus > 0){
            total += highCardBonus;
            logger.log(Level.INFO, String.format("High card bonus of %d", highCardBonus));
        }
        return total + val1 + val2;
    }

    private int handScore5Cards(Card[] cards){
        int total = 0;
        long myCards = 0;
        boolean isFlush = false;
        boolean isStraight = false;
        for(Card card : cards){
            myCards += card.getNumber();
        }
        long myCardResult = HandEval.hand5Eval(myCards);
        BitSet cardSet = longToBooleanHand(myCardResult);
        logger.info(String.format("Hand string is %s, of length %d", Long.toBinaryString(myCardResult), Long.toBinaryString(myCardResult).length()));
        total = getCardPointsFromHand(cardSet);
        if(total == STRAIGHT_FLUSH_BONUS){
            isStraight = true;
            isFlush = true;
        }
        else if(total == FLUSH_BONUS){
            isFlush = true;
        }
        else if(total == STRAIGHT_BONUS){
            isStraight = true;
        }
        int maxCard = 0;
        int[] suitCounter = new int[4]; // spade(0), heart(1), club(2), diamond(3)
        boolean[] heightCounter = new boolean[13]; // Two(0)-Ace(12)
        logger.log(Level.INFO, "Allocating points per card for 5 cards");
        // give points for each card, also set up future calculations
        int totalValue = 0;
        for(Card card : cards){
            int value = card.getHeight().getValue();
            heightCounter[value] = true;
            suitCounter[card.getSuit().getValue()] = suitCounter[card.getSuit().getValue()] + 1;
            if(value > maxCard){
                maxCard = value;
            }
            totalValue += value;
        }
        // each card has less value as the overall hand is more important
        total += (totalValue/2);

        // possible future flush
        boolean flushBonus = false;
        if(!isFlush){
            int cardsSameSuit = 0;
            for(int i = 0; i < suitCounter.length; ++i){
                if(suitCounter[i] > cardsSameSuit){
                    cardsSameSuit = suitCounter[i];
                }
            }
            if(cardsSameSuit == 4){
                total += FOUR_OF_FIVE_FLUSH_BONUS;
                flushBonus = true;
                logger.log(Level.INFO, "same suit 4/5 cards");
            }
            else if(cardsSameSuit == 3){
                total += THREE_OF_FIVE_FLUSH_BONUS;
                flushBonus = true;
                logger.log(Level.INFO, "same suit 3/5 cards");
            }

        }
        // possible future straight
        boolean straightBonus = false;
        if(!isStraight){
            int consecutiveCards = 0;
            int maxConsecutiveCards = 0;
            for(int i = 0; i < heightCounter.length; ++i){
                if(heightCounter[i]){
                    if(++consecutiveCards > maxConsecutiveCards){
                        maxConsecutiveCards = consecutiveCards;
                    }
                }
                else{
                    consecutiveCards = 0;
                }
            }
            if(maxConsecutiveCards == 4){
                logger.log(Level.INFO, "straight 4/5 cards open-ended or gutshot");
                straightBonus = true;
                // check ace as high roll
                if(heightCounter[heightCounter.length-1] && heightCounter[heightCounter.length-2] &&
                        heightCounter[heightCounter.length-3] && heightCounter[heightCounter.length-4]){
                    total += FOUR_OF_FIVE_GUTSHOT_BONUS;
                }
                else{
                    total += FOUR_OF_FIVE_STRAIGHT_BONUS;
                }
            }
            else if(maxConsecutiveCards == 3){
                logger.log(Level.INFO, "straight 3/5 cards or 4/5 gutshot");
                straightBonus = true;
                // check ace as low roll
                if(heightCounter[heightCounter.length-1] && heightCounter[0] && heightCounter[1] && heightCounter[2]){
                    total += FOUR_OF_FIVE_GUTSHOT_BONUS;
                }
                else{
                    int maxCardsInProximity = cardsInProximityForGutshot(heightCounter);
                    // look for 1 0 1 1 1 or 1 1 1 0 1 combinations
                    if(maxCardsInProximity == 4){
                        total += FOUR_OF_FIVE_GUTSHOT_BONUS;
                    }
                    else{
                        total += THREE_OF_FIVE_STRAIGHT_BONUS;
                    }
                }
            }
            // look for the 1 1 0 1 1 combination
            else if(maxConsecutiveCards == 2){
                int maxCardsInProximity = cardsInProximityForGutshot(heightCounter);
                if(maxCardsInProximity == 4){
                    total += FOUR_OF_FIVE_GUTSHOT_BONUS;
                    logger.log(Level.INFO, "straight 4/5 gutshot");
                    straightBonus = true;
                }
            }
            // compensate for counting cards that are outs for flush and straight and flush beating straight
            if(flushBonus && straightBonus || isFlush && straightBonus){
                total -= FOUR_OF_FIVE_GUTSHOT_BONUS/1.5;
            }
        }

        // high card bonus
        total += (maxCard/MAX_CARD_DIVISOR);
        logger.log(Level.INFO, String.format("High card bonus worth %d", (maxCard/MAX_CARD_DIVISOR)));

        logger.info(String.format("Final hand score is %d", total));
        return total;
    }

    private int handScore6Cards(Card[] cards){
        int total = 0;
        long myCards = 0;
        boolean isFlush = false;
        boolean isStraight = false;
        for(Card card : cards){
            myCards += card.getNumber();
        }
        long myCardResult = HandEval.hand6Eval(myCards);
        logger.info(String.format("Hand string is %s, of length %d", Long.toBinaryString(myCardResult), Long.toBinaryString(myCardResult).length()));
        BitSet cardSet = longToBooleanHand(myCardResult);
        total = getCardPointsFromHand(cardSet);
        if(total == STRAIGHT_FLUSH_BONUS){
            isStraight = true;
            isFlush = true;
        }
        else if(total == FLUSH_BONUS){
            isFlush = true;
        }
        else if(total == STRAIGHT_BONUS){
            isStraight = true;
        }
        int relativeStrength = 0;
        if(total == FOUR_OF_A_KIND_BONUS || total == FLUSH_BONUS){
            relativeStrength = cardSet.previousSetBit(cardSet.length()-4);
            logger.info(String.format("Next relevant bit value is %d", relativeStrength));
            total += RELATIVE_STRENGTH_MULTIPLIER_6 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_6);
        }
        else{
            relativeStrength = cardSet.previousSetBit(cardSet.length()-3);
            logger.info(String.format("Next relevant bit value is %d", relativeStrength));
            total += RELATIVE_STRENGTH_MULTIPLIER_6 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_6);
        }
        logger.info(String.format("Adding %d relative strength points", RELATIVE_STRENGTH_MULTIPLIER_6 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_6)));
        int maxCard = 0;
        int[] suitCounter = new int[4]; // spade(0), heart(1), club(2), diamond(3)
        boolean[] heightCounter = new boolean[13]; // Two(0)-Ace(12)
        logger.log(Level.INFO, "Allocating points per card for 6 cards");
        // give points for each card, also set up future calculations
        int totalValue = 0;
        for(Card card : cards){
            int value = card.getHeight().getValue();
            heightCounter[value] = true;
            suitCounter[card.getSuit().getValue()] = suitCounter[card.getSuit().getValue()] + 1;
            if(value > maxCard){
                maxCard = value;
            }
            totalValue += value;
        }
        // each card has less value as the overall hand is more important
        total += (totalValue/3);

        // possible future flush
        boolean flushBonus = false;
        if(!isFlush){
            int cardsSameSuit = 0;
            for(int i = 0; i < suitCounter.length; ++i){
                if(suitCounter[i] > cardsSameSuit){
                    cardsSameSuit = suitCounter[i];
                }
            }
            if(cardsSameSuit == 4){
                total += FOUR_OF_SIX_FLUSH_BONUS;
                flushBonus = true;
                logger.log(Level.INFO, "same suit 4/6 cards");
            }
        }
        // possible future straight
        boolean straightBonus = false;
        if(!isStraight){
            int consecutiveCards = 0;
            int maxConsecutiveCards = 0;
            for(int i = 0; i < heightCounter.length; ++i){
                if(heightCounter[i]){
                    if(++consecutiveCards > maxConsecutiveCards){
                        maxConsecutiveCards = consecutiveCards;
                    }
                }
                else{
                    consecutiveCards = 0;
                }
            }
            if(maxConsecutiveCards == 4){
                logger.log(Level.INFO, "straight 4/6 cards open-ended or gutshot");
                straightBonus = true;
                // check ace as high roll
                if(heightCounter[heightCounter.length-1] && heightCounter[heightCounter.length-2] &&
                        heightCounter[heightCounter.length-3] && heightCounter[heightCounter.length-4]){
                    total += FOUR_OF_SIX_GUTSHOT_BONUS;
                }
                else{
                    total += FOUR_OF_SIX_STRAIGHT_BONUS;
                }
            }
            else if(maxConsecutiveCards == 3){
                // check ace as low roll
                if(heightCounter[heightCounter.length-1] && heightCounter[0] && heightCounter[1] && heightCounter[2]){
                    total += FOUR_OF_SIX_GUTSHOT_BONUS;
                    logger.log(Level.INFO, "straight 4/6 gutshot (Ace lowroll)");
                    straightBonus = true;
                }
                else{
                    int maxCardsInProximity = cardsInProximityForGutshot(heightCounter);
                    // look for 1 0 1 1 1 or 1 1 1 0 1 combinations
                    if(maxCardsInProximity == 4){
                        total += FOUR_OF_SIX_GUTSHOT_BONUS;
                        logger.log(Level.INFO, "straight 4/6 gutshot [11101 or 10111]");
                        straightBonus = true;
                    }
                }
            }
            // look for the 1 1 0 1 1 combination
            else if(maxConsecutiveCards == 2){
                int maxCardsInProximity = cardsInProximityForGutshot(heightCounter);
                if(maxCardsInProximity == 4){
                    total += FOUR_OF_SIX_GUTSHOT_BONUS;
                    logger.log(Level.INFO, "straight 4/5 gutshot [11011]");
                    straightBonus = true;
                }
            }
            // compensate for counting cards that are outs for flush and straight and flush beating straight
            if(flushBonus && straightBonus){
                total -= FOUR_OF_SIX_GUTSHOT_BONUS/1.5;
            }
            // harsher penalty as flush >> straight
            if(isFlush && straightBonus){
                total -= FOUR_OF_SIX_GUTSHOT_BONUS;
            }
        }

        // high card bonus
        total += (maxCard/MAX_CARD_DIVISOR);
        logger.log(Level.INFO, String.format("High card bonus worth %d", (maxCard/MAX_CARD_DIVISOR)));

        logger.info(String.format("Final hand score is %d", total));
        return total;
    }

    private int handScore7Cards(Card[] cards){
        int total = 0;
        long myCards = 0;

        for(Card card : cards){
            myCards += card.getNumber();
        }
        long myCardResult = HandEval.hand7Eval(myCards);
        logger.info(String.format("Hand string is %s, of length %d", Long.toBinaryString(myCardResult), Long.toBinaryString(myCardResult).length()));
        BitSet cardSet = longToBooleanHand(myCardResult);
        total = getCardPointsFromHand(cardSet);
        int relativeStrength = 0;
        if(total == FOUR_OF_A_KIND_BONUS || total == FLUSH_BONUS){
            relativeStrength = cardSet.previousSetBit(cardSet.length()-4);
            logger.info(String.format("Next relevant bit value is %d", relativeStrength));
            total += RELATIVE_STRENGTH_MULTIPLIER_7 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_7);
        }
        else{
            relativeStrength = cardSet.previousSetBit(cardSet.length()-3);
            logger.info(String.format("Next relevant bit value is %d", relativeStrength));
            total += RELATIVE_STRENGTH_MULTIPLIER_7 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_7);
        }
        logger.info(String.format("Adding %d relative strength points", RELATIVE_STRENGTH_MULTIPLIER_7 *(relativeStrength- RELATIVE_STRENGTH_BUFFER_7)));
        int maxCard = 0;
        int[] suitCounter = new int[4]; // spade(0), heart(1), club(2), diamond(3)
        boolean[] heightCounter = new boolean[13]; // Two(0)-Ace(12)
        logger.log(Level.INFO, "Allocating points per card for 7 cards");
        // give points for each card, also set up future calculations
        int totalValue = 0;
        for(Card card : cards){
            int value = card.getHeight().getValue();
            heightCounter[value] = true;
            suitCounter[card.getSuit().getValue()] = suitCounter[card.getSuit().getValue()] + 1;
            if(value > maxCard){
                maxCard = value;
            }
            totalValue += value;
        }
        // each card has less value as the overall hand is more important
        total += (totalValue/4);

        // high card bonus
        total += (maxCard/MAX_CARD_DIVISOR);
        logger.log(Level.INFO, String.format("High card bonus worth %d", (maxCard/MAX_CARD_DIVISOR)));

        logger.info(String.format("Final hand score is %d", total));
        return total;
    }
    private int getCardPointsFromHand(BitSet cardSet){
        int cardSetBitLength = cardSet.length();
        int total = 0;
        logger.info(String.format("Set is %s, length is %d, second set bit is %d", cardSet, cardSetBitLength, cardSet.previousSetBit(cardSetBitLength-2)));

        // straight flush
        if(cardSetBitLength == 28){
            total = STRAIGHT_FLUSH_BONUS;
            logger.log(Level.INFO, "Straight flush");
        }
        // 4 of a kind, full house, flush or straight
        else if(cardSetBitLength == 27){
            // 4 of a kind of full house
            if(cardSet.previousSetBit(cardSetBitLength-2) == 25){
                // 4 of a kind
                if(cardSet.previousSetBit(cardSetBitLength-3) == 24){
                    total = FOUR_OF_A_KIND_BONUS;
                    logger.log(Level.INFO, "4 of a kind");
                }
                // full house
                else{
                    total = FULL_HOUSE_BONUS;
                    logger.log(Level.INFO, "full house");
                }
            }
            // flush
            else if(cardSet.previousSetBit(cardSetBitLength-2) == 24){
                total = FLUSH_BONUS;
                logger.log(Level.INFO, "Flush");
            }
            // straight
            else{
                total = STRAIGHT_BONUS;
                logger.log(Level.INFO, "Straight");
            }
        }
        // 3 of a kind (set) or two pair
        else if(cardSetBitLength == 26){
            // 3 of a kind
            if(cardSet.previousSetBit(cardSetBitLength-2) == 24){
                total = THREE_OF_A_KIND_BONUS;
                logger.log(Level.INFO, "3 of a kind");
            }
            // 2 pair
            else{
                total = TWO_PAIR_BONUS;
                logger.log(Level.INFO, "2 pair");
            }
        }
        // pair
        else if(cardSetBitLength == 25){
            total = PAIR_BONUS;
            logger.log(Level.INFO, "1 pair");
        }
        return total;
    }

    private int cardsInProximityForGutshot(boolean[] heightCounter){
        int maxCardsInProximity = 0;
        int cardsInProximity = 0;
        for(int i = 0; i < heightCounter.length-4; ++i){
            cardsInProximity = 0;
            if(!heightCounter[i]){
                continue;
            }
            for(int j = i; j < i+5; ++j){
                if(heightCounter[j]){
                    if(++cardsInProximity > maxCardsInProximity){
                        maxCardsInProximity = cardsInProximity;
                    }
                }
            }
        }
        return maxCardsInProximity;
    }

    public BitSet longToBooleanHand(long cardValue){
        long[] longs = {cardValue};
        return BitSet.valueOf(longs);
    }
    /**
     * Parse the input string from the engine to actual Card objects
     * @param String value : input
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
/*
    public static void log(Object o){
        if(Debugger.isEnabled()) {
            System.out.println(o.toString());
        }
    }
*/
//    public static void main(String[] args) {
//        System.out.println("hello world");
//        String hand1 = "[Ad,Kh]";
//        System.out.println(handScore(hand1));
//        try{
//            System.in.read();
//
//        }catch(Exception ex){}
//    }

}
