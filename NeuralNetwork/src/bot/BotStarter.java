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

import poker.Card;
import poker.HandHoldem;
import poker.PokerMove;

import com.stevebrecher.HandEval;

/**
 * This class is the brains of your bot. Make your calculations here and return the best move with GetMove
 */
public class BotStarter implements Bot {
	private pythonProcess pypro;
	private int previousStack;

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
		if (this.pypro == null) {
			this.pypro = new pythonProcess();
			this.pypro.startProcess();
			this.pypro.initProcess();
			this.previousStack = 0;
		}
		HandHoldem hand = state.getHand(); //hand.getCards(), state.getTable()
		String handCategory = getHandCategory(hand, state.getTable()).toString();
		System.err.printf("my hand is %s, opponent action is %s, pot: %d\n", handCategory, state.getOpponentAction(), state.getPot());

		EvalStarter handEvalClass = new EvalStarter();
		RiskEvaluation riskEvalClass = new RiskEvaluation();
		
		Card[] mycards = hand.getCards();
		Card[] boardCards = state.getTable();

		int currentStage = 0;
		double cardScore = handEvalClass.normalizedHandScore(mycards);
		double riskEval = riskEvalClass.riskEval(mycards, boardCards);
		double potEval = (double) state.getAmountToCall() / (state.getPot() + state.getAmountToCall());

		if (boardCards.length == 0) {
			currentStage = 1;
			if (this.previousStack != 0) {
				pypro.writeToProcesss(5);
				pypro.writeToProcesss(this.previousStack - state.getmyStack());
				this.previousStack = state.getmyStack();
			}
		}

		switch (boardCards.length) {
			case 0:
            	currentStage = 1;
            	break;
            case 3:
            	currentStage = 2;
				break;
            case 4:
				currentStage = 3;
				break;
			case 5:
				currentStage = 4;
				break;
        }

		pypro.writeToProcesss(currentStage);
		pypro.writeToProcesss(cardScore);
		pypro.writeToProcesss(riskEval);
		//pypro.writeToProcesss(potEval);

		double decision = this.pypro.readFromProcess();
		System.out.println(String.valueOf(decision));

		// Return the appropriate move according to our amazing strategy
		PokerMove retVal = null;
		if( decision < 0 ) {
			retVal = new PokerMove(state.getMyName(), "fold", 0);
		} else if (decision >= 0 && decision < 1) {
			retVal = (state.getAmountToCall() == 0) ? new PokerMove(state.getMyName(), "check", 0) : new PokerMove(state.getMyName(), "call", state.getAmountToCall());
		}
		else {
			retVal = new PokerMove(state.getMyName(), "raise", 4*state.getBigBlind());
		}

		return retVal;
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
