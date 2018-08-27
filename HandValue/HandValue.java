/** This program is for evaluating a hand taking the command line arguments as
 *  cards
 *  
 *  This program receive 5 cards on the command line and will print out only 
 *  the number of points the hand comprising the first four of those cards 
 *  would score if the fifth card were the start card.
 *  
 *  Cards should be entered on the command line as two-character strings, the
 *  first being an upper-case A for Ace, K for King, Q for Queen, J for Jack,
 *  T for Ten, or digit between 2 and 9 for ranks 2-9. The second character
 *  should be a C for Clubs, a D for Diamonds, an H for Hearts, or an S for 
 *  Spades.
 */

import java.util.Arrays;

public class HandValue {

	/** A constant integer for the number of hand cards. */
	public static final int handCard = 4;
	
	/** @param arrays of ranks of five cards including the start card
	 *  @param arrays of characters of their suits.
	 *  @return if "One for his nob" is scored or not. 
	 */
	public static boolean oneForHisNod(CribbageRank[] card, char[] suit) {
		int i;
		for(i=0; i< handCard; i++) {
			if(card[i].abbrev() == 'J' && suit[i] == suit[suit.length-1]) {
				return true;
			}
		}
		return false;
	}
	
	/** @param combinations of ranks of the five cards.
	 *  @return the points got because of pairs. 
	 */
	public static int pairs(CribbageRank[][] cardCombo) {
		int pairs = 0;
		for(CribbageRank[] subCombo : cardCombo) {
			if(subCombo.length == 2) {
				if(subCombo[0] == subCombo[1]) {
					pairs++;
				}
			}
		}
		return pairs*2;
	}
	
	/** @param combinations of ranks of the five cards.
	 *  @return the points got because of fifteens. 
	 */
	public static int fifteens(CribbageRank[][] cardCombo) {
		int fifteens = 0;
		int sum;
		for(CribbageRank[] subCombo : cardCombo) {
			sum = 0;
			for(CribbageRank card : subCombo) {
				sum += card.faceValue();
			}
			if(sum == 15) {
				fifteens++;
			}
		}
		return fifteens*2;
	}
	
	/** @param arrays of characters of the suit of the cards
	 *  @return the points got because of flushes. 
	 */
	public static int flushes(char[] suit) {
		/*
		 * sort the suits of hand cards, then compare the first with 
		 * the last element to determine if they are of the same suit. 
		 * if not, 0 point is scored.
		 * 
		 * if so, compare the suit of the hand cards with the one of 
		 * the start card.
		 * if they are the same, 5 points are scored. 
		 * if not, 4 points are scored.
		 */
		Arrays.sort(suit, 0, handCard);
		if(suit[0] == suit[handCard-1]) {
			if(suit[0] == suit[suit.length-1]) {
				return 5;
			}
			return 4;
		}
		return 0;
	}
	
	/** @param combinations of the five cards.
	 *  @return the points got because of runs. 
	 */
	public static int runs(CribbageRank[][] cardCombo) {
		int times = 0;
		int runs = 0;
		boolean isRun;
		for(CribbageRank[] subCombo : cardCombo) {
			if(subCombo.length >= 3) {
				Arrays.sort(subCombo);
				isRun = true;
				for(int i = 1; i<subCombo.length; i++) {
					if(subCombo[i] != subCombo[i-1].nextHigher()) {
						isRun = false;
					}
				}
				if(isRun) {
					if(runs < subCombo.length) {
						times = 0;
						runs = subCombo.length;
					}
					if(runs == subCombo.length) {
						times++;
					}
				}
			}
		}
		return runs*times;
	}
	
	/** The main method is to take the command line arguments as cards and 
	 *  to use the other methods of the HandValue class to calculate the 
	 *  total score of a hand and print it.	 
	 */
	
	public static void main(String[] args) {
		int points = 0;
		CribbageRank[] cardValue = new CribbageRank[handCard+1];
		char[] suit = new char[handCard+1];
		
		// initialize the arrays of ranks and suits of the cards
		for(int i = 0; i< cardValue.length; i++) {
			cardValue[i] = CribbageRank.valueOf(args[i].charAt(0));
			suit[i] = args[i].charAt(1);
		}
		
		// get the combinations of ranks of the cards
		CribbageRank[][] cardCombo = Combinations.combinations(cardValue);
		
		// add up all the points to find the value of a hand
		/* be careful. "one for his nod" has to be first tested
		 * since in the method HandValue.flushes(char[] suit)
		 *  the array is going to be sorted 
		 */
		if(HandValue.oneForHisNod(cardValue, suit)) {
			points++;
		}	
		points += HandValue.pairs(cardCombo) + HandValue.fifteens(cardCombo) 
		          + HandValue.flushes(suit) + HandValue.runs(cardCombo);
		
		System.out.println(points);
	}

}
