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


package poker;

public enum CardSuit {
	SPADES(0),
	HEARTS(1),
	CLUBS(2),
	DIAMONDS(3);

	private final int value;
	CardSuit(final int newValue) {
		value = newValue;
	}
	public int getValue() { return value; }
}
