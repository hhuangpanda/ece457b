/**
 * www.TheAIGames.com 
 * Heads Up Omaha pokerbot
 *
 * Last update: May 07, 2014
 *
 * @author Jim van Eeden, Starapple
 * @version 1.0
 * @License MIT License (http://opensource.org/Licenses/MIT)
 */


package bot;

import com.stevebrecher.HandEval;
import poker.Card;
import poker.HandHoldem;
import poker.PokerMove;

/**
 * This class is the brains of your bot. Make your calculations here and return the best move with GetMove
 */
public class BotStarter implements Bot {

	private EvalStarter handEval;
	private RiskEvaluation riskEval;
	private FuzzyLogic fuzzyLogic;

	public BotStarter()
	{
		handEval = new EvalStarter();
		riskEval = new RiskEvaluation();
		fuzzyLogic = new FuzzyLogic();
	}

	/**
	 * Implement this method to return the best move you can. Currently it will return a raise the ordinal value
	 * of one of our cards is higher than 9, a call when one of the cards has a higher ordinal value than 5 and
	 * a check otherwise.
	 * @param state : The current state of your bot, with all the (parsed) information given by the engine
	 * @param timeOut : The time you have to return a move
	 * @return PokerMove : The move you will be doing
	 */
	@Override
	public PokerMove getMove(BotState state, Long timeOut) {
		HandHoldem hand = state.getHand();
		String handCategory = getHandCategory(hand, state.getTable()).toString();

		Card[] cards = state.getHand().getCards();
		Card[] table = state.getTable();

		double pot = state.getPot();

		double h = handEval.normalizedHandScore(cards,table);
		double r = riskEval.riskEval(cards,table);
		double b = state.getAmountToCall() / (pot + state.getAmountToCall());

		double betPercent = fuzzyLogic.evaulateRules(h,r,b);

		if (betPercent < b) {
			return new PokerMove(state.getMyName(), "fold", 0);
		}
		else if (Math.abs(betPercent - b) < 0.2) {
			return new PokerMove(state.getMyName(), "call", state.getAmountToCall());
		}
		else {
			return new PokerMove(state.getMyName(), "raise", (int)Math.round(pot * betPercent));
		}
	}
	
	/**
	 * Calculates the bot's hand strength, with 0, 3, 4 or 5 cards on the table.
	 * This uses the com.stevebrecher package to get hand strength.
	 * @param hand : cards in hand
	 * @param table : cards on table
	 * @return HandCategory with what the bot has got, given the table and hand
	 */
	public HandEval.HandCategory getHandCategory(HandHoldem hand, Card[] table) {
		if( table == null || table.length == 0 ) { // there are no cards on the table
			return hand.getCard(0).getHeight() == hand.getCard(1).getHeight() // return a pair if our hand cards are the same
					? HandEval.HandCategory.PAIR
					: HandEval.HandCategory.NO_PAIR;
		}
		long handCode = hand.getCard(0).getNumber() + hand.getCard(1).getNumber();
		
		for( Card card : table ) { handCode += card.getNumber(); }

		if( table.length == 3 ) { // three cards on the table
			return rankToCategory(HandEval.hand5Eval(handCode));
		}
		if( table.length == 4 ) { // four cards on the table
			return rankToCategory(HandEval.hand6Eval(handCode));
		}
		return rankToCategory(HandEval.hand7Eval(handCode)); // five cards on the table
	}
	
	/**
	 * small method to convert the int 'rank' to a readable enum called HandCategory
	 */
	public HandEval.HandCategory rankToCategory(int rank) {
		return HandEval.HandCategory.values()[rank >> HandEval.VALUE_SHIFT];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BotParser parser = new BotParser(new BotStarter());
		parser.run();
	}

}
