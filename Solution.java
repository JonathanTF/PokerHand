import java.util.*;

public class Solution {

	static List<String> ranks = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k", "a");
	static List<String> suits = Arrays.asList("h", "c", "d", "s");
	
	//a hand is an array of card objects - contain rank, value of the rank, and suit
	private static class Card{

		private char rank;
		private int value;
		private char suit;
		
		public Card(char rank, char suit) {
			this.setRank(rank);
			this.setSuit(suit);	
			this.value = ranks.indexOf(String.valueOf(rank)) + 2;
		}

		public void setRank(char rank) {
			this.rank = rank;
		}

		public void setSuit(char suit) {
			this.suit = suit;
		}
		
		public boolean equals(Card c) {
			if( this.rank == c.rank &&
				this.value == c.value &&
				this.suit == c.suit 
				) {
					return true;
			}
			
			return false;
		}
		
	}
	
	static public class cardCompare implements Comparator<Card>{

		@Override
		public int compare(Card c1, Card c2) {
			return c1.value - c2.value;
		}
		
	}

	//input validity check
	static boolean validInput(String input){
		if( input.length() < 2 ) {
			return false;
		}

		if( !ranks.contains(input.substring(0, 1)) ) {
			return false;
		}
		
		if( !suits.contains(input.substring(1, 2)) ) {
			return false;
		}
		
		return true;
	}
	
	public static boolean checkStraight(ArrayList<Card> hand) {
		Iterator<Card> itr = hand.iterator();
		int val = itr.next().value;

		while(itr.hasNext()) {
			if( itr.next().value != ++val ) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkFlush(ArrayList<Card> hand) {
		Iterator<Card> itr = hand.iterator();
		char suit = itr.next().suit;

		while(itr.hasNext()) {
			if( itr.next().suit != suit ) {
				return false;
			}
		}
		
		return true;
	}
	
	//returns a 'rank count' int[] that counts the number of occurrences of each rank
	public static int[] countRanks(ArrayList<Card> hand) {
		int rankCnt[] = {0,0,0,0,0,0,0,0,0,0,0,0,0};

		for(Card c : hand) {
			++rankCnt[ranks.indexOf(String.valueOf(c.rank))];
		}
		
		return rankCnt;	
	}
	
	//returns an int[] where [0] is the highest number of the same rank and [1] is an index into the 'ranks' ArrayList giving the rank string
	public static int[] maxOfAKind(int[] rankCnt) {
		int max = 0;
		int maxIdx = 0;
		
		for(int i=0; i<rankCnt.length; i++) {
			if( rankCnt[i] > max ) {
				max = rankCnt[i];
				maxIdx = i;
			}
		}
		
		return new int[]{max,maxIdx};
	}
	
	//returns 0, 1, or 2 for number of pairs in a rank count
	public static int numPairs(int[] rankCnt) {
		int numP = 0;
		
		for(int i : rankCnt) {
			if( i == 2 ) numP++;
		}
		
		return numP;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true) {	
			ArrayList<Card> hand = new ArrayList<Card>();
			int h_idx = 0;
			boolean valid = true;
			boolean duplicates = false;
			
			//get input and check if valid
			while( (h_idx < 5) && sc.hasNext() ) {
				String input = sc.next();
				input = input.toLowerCase();
				
				if( input.equals("exit") ) {
					System.out.println("goodbye");
					sc.close();
					System.exit(0);
				}
				h_idx++;
				
				if( !validInput(input) ) {
					valid = false;
					continue;
				}
				
				hand.add(new Card(input.charAt(0), input.charAt(1)));
			}
			if( !valid ) {
				System.err.println("Invalid input");
				continue;
			}		
			
			//check for duplicates
			ArrayList<Card> check = new ArrayList<Card>();
			for(Card c_hand : hand) {
				for(Card c_check : check) {
					if( c_hand.equals(c_check) ) {
						duplicates = true;
					}
				}
				check.add(c_hand);
			}
			if( duplicates ) {
				System.err.println("Duplicate cards");
				continue;
			}
			
			//sort the cards by rank 
			hand.sort(new cardCompare());
			
			//check for each condition in order from best to worst
			boolean isStraight = false;
			boolean isFlush = false;
			if( checkStraight(hand) ) {
				isStraight = true;
			}
			if( checkFlush(hand) ) {
				isFlush = true;
			}
			if( isStraight && isFlush ) {
				System.out.println("straight flush");
				continue;
			}
			
			int rankCnt[] = countRanks(hand);
			int[] maxKind = maxOfAKind(rankCnt);
			if( maxKind[0] == 4 ) {
				System.out.println("four of a kind");
				continue;
			}
			
			int pairs = numPairs(rankCnt);
			if( (maxKind[0] == 3) && (pairs == 1) ) {
				System.out.println("full house");
				continue;	
			}
			
			if( isFlush ) {
				System.out.println("flush");
				continue;
			}
			
			if( isStraight ) {
				System.out.println("straight");
				continue;
			}
			
			if( (maxKind[0] == 3) ) {
				System.out.println("three of a kind");
				continue;
			}
			
			if (pairs == 2 ) {
				System.out.println("two pairs");
				continue;
			}
			
			if( pairs == 1 ) {
				System.out.println("one pair");
				continue;	
			}
			
			System.out.println("high card: " + hand.get(4).rank + hand.get(4).suit);	
		}
	}
}
