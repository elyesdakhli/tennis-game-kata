package dakhli.elyes.tennisgame;

/**
 * Match status
 * @author Elyes
 *
 */
public enum MatchStatusEnum {
	/**
	 * The match is in progress
	 */
	IN_PROGRESS("In progress"),
	/**
	 * Player 1 wins the match
	 */
	FINISHED_PLAYER1_WINS("Player 1 wins"),
	/**
	 * PLayer 2 wins the match
	 */
	FINISHED_PLAYER2_WINS("Player 2 wins");
	
	/**
	 * Constructor
	 * @param desc
	 */
	MatchStatusEnum(String desc){
		this.desc = desc;
	}
	/**
	 * Status description
	 */
	private String desc;
	
	public String toString(){
		return desc;
	}
}
