package dakhli.elyes.tennisgame;

public class GameSet {

	private int score1;
	private int score2;
	private int tieBreakScore1;
	private int tieBreakScore2;
	private boolean tieBreak;
	
	public GameSet() {
		super();
	}
	/**
	 * Returns the winner of the tie break
	 * @return
	 */
	public int getTieBreakWinnerScore(){
		if(this.isTieBreakOver()){
			return score1 > score2 ? score1 : score2;
		}
		return -1;
	}
	/**
	 * Tests if the tie break is over
	 * @return
	 */
	public boolean isTieBreakOver() {
		return (tieBreakScore1 >= 7 || tieBreakScore2 >= 7) && Math.abs(tieBreakScore1 - tieBreakScore2) >= 2;
	}
	public void incrementTieBreakScore1(){
		this.tieBreakScore1++;
	}
	public void incrementTieBreakScore2(){
		this.tieBreakScore2++;
	}
	
	public void incrementScore1(){
		this.score1++;
	}
	public void incrementScore2(){
		this.score2++;
	}
	public int getScore1() {
		return score1;
	}
	public void setScore1(int score1) {
		this.score1 = score1;
	}
	public int getScore2() {
		return score2;
	}
	public void setScore2(int score2) {
		this.score2 = score2;
	}
	
	public int getTieBreakScore1() {
		return tieBreakScore1;
	}
	public void setTieBreakScore1(int tieBreakScore1) {
		this.tieBreakScore1 = tieBreakScore1;
	}
	public int getTieBreakScore2() {
		return tieBreakScore2;
	}
	public void setTieBreakScore2(int tieBreakScore2) {
		this.tieBreakScore2 = tieBreakScore2;
	}
	public boolean isTieBreak() {
		tieBreak = tieBreak  || (score1 == 6 &&  score1 == score2);
		return tieBreak;
	}
	public void setTieBreak(boolean tieBreak) {
		this.tieBreak = tieBreak;
	}
	@Override
	public String toString() {
		StringBuilder status = new StringBuilder("(").append(score1).append(" - ").append(score2).append(")");
		if(isTieBreak()){
			status.append("[").append(tieBreakScore1).append("-").append(tieBreakScore2).append("]");
		}
		return status.toString();
	}
	
		
}
