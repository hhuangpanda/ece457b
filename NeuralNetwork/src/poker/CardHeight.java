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

public enum CardHeight {
	DEUCE(0),
	THREE(1),
	FOUR(2),
	FIVE(3),
	SIX(4),
	SEVEN(5),
	EIGHT(6),
	NINE(7),
	TEN(8),
	JACK(9),
	QUEEN(10),
	KING(11),
	ACE(12);

	private final int value;
	CardHeight(final int newValue) {
		value = newValue;
	}
	public int getValue() { return value; }
}
