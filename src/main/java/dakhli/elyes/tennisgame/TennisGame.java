package dakhli.elyes.tennisgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TennisGame {

	public static final int POINT_0 = 0;
	public static final int POINT_15 = 1;
	public static final int POINT_30 = 2;
	public static final int POINT_40 = 3;
	
	private static final String MATCH_DESC_PLAYER1_LIBELLE = "Player 1";
	private static final String MATCH_DESC_PLAYER2_LIBELLE = "Player 2";
	private static final String MATCH_DESC_SCORE_LIBELLE = "Score";
	private static final String MATCH_DESC_CURR_GAME_STATUS = "Current game status";
	private static final String MATCH_DESC_MATCH_STATUS = "Match Status";
	private static final String MATCH_DESC_LIBELLE_SEPARATOR = " : ";
	private static final String MATCH_DESC_LINE_BREAK = "\n";
	private static final String MATCH_DESC_ADVANTAGE_LIBELLE = "Advantage";
	private static final String MATCH_DESC_DEUCE_LIBELLE = "Deuce";
	
	private Player player1;
	private Player player2;
	private List<GameSet> sets;
	private GameSet currentGameSet;
	private MatchStatusEnum matchStatus;

	/**
	 * Parameterized constructor
	 * @param player1 first player
	 * @param player2 second player
	 */
	public TennisGame(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.player1.setTennisGame(this);
		this.player2.setTennisGame(this);
		this.sets = new ArrayList<GameSet>();
		this.sets.add(new GameSet());
		currentGameSet = this.sets.get(0);
		matchStatus = MatchStatusEnum.IN_PROGRESS;
	}

	

	/**
	 * Tests if the current set is a tie-break
	 * @return
	 */
	private boolean isCurrentSetTieBreak() {
		return this.currentGameSet.isTieBreak();
	}
	/**
	 * Called when a player scores
	 * @param player player who scored
	 */
	public void winBall(Player player){
		if(player != null && this.matchStatus.equals(MatchStatusEnum.IN_PROGRESS)){
			//if it's a tie-break
			if(isCurrentSetTieBreak()){
				incrementTieBreak(player);
			}else{
				incrementScore(player);
			}
		}
	}
	/**
	 * Increment the score when its a tie-break
	 * @param player player who wins scores
	 */
	private void incrementTieBreak(Player player) {
		if(!this.currentGameSet.isTieBreakOver()){
			if(player1.equals(player)){
				this.currentGameSet.incrementTieBreakScore1();
				if(this.currentGameSet.isTieBreakOver()){
					this.currentGameSet.incrementScore1();
					this.initNewGameSet();
				}
			}else{
				this.currentGameSet.incrementTieBreakScore2();
				if(this.currentGameSet.isTieBreakOver()){
					this.currentGameSet.incrementScore2();
					this.initNewGameSet();
				}
			}
		}
	}

	/**
	 * Increments the score in the current game
	 * @param player
	 */
	private void incrementScore(Player player) {
		if (player.getGameScore() != POINT_40 ||
				(player.getGameScore()==POINT_40 && getTheOtherPlayer(player).getGameScore()!=POINT_40)) {
			player.incrementGameScore();
			if(player.getGameScore() > POINT_40){
				winGame(player);
			}
		} else if (player.getGameScore() == POINT_40 && getTheOtherPlayer(player).getGameScore()== POINT_40) {
			// if the player is advantaged, he wins the game
			if (player.isAdvantaged()) {
				winGame(player);
			} else {
				// if the other player doesnt have advantage, set advantage to
				// this player otherwise the other player loses advantage
				Player otherPlayer = getTheOtherPlayer(player);
				if (otherPlayer.isAdvantaged()) {
					// the other player loses advantage
					otherPlayer.setAdvantaged(false);
				} else {
					player.setAdvantaged(true);
				}
			}
		}
	}

	/**
	 * In case the player wins the game, both scores must reset to 0 and inits a new set when the current set is over
	 * @param player
	 */
	private void winGame(Player player) {
		if(player.equals(this.player1)){
			if(currentGameSet.getScore1()<=6 && this.currentGameSet.getScore2()<=6){
				//adding 1 to set score
				this.currentGameSet.setScore1(currentGameSet.getScore1()+1);
				boolean isMatchFinished = isMatchFinished();
				if((this.currentGameSet.getScore1()==6 && this.currentGameSet.getScore2()<5 
						|| this.currentGameSet.getScore1()==7 && this.currentGameSet.getScore2()==5)
						&& !isMatchFinished){
					initNewGameSet();
				}
				if(isMatchFinished){
					updateMatchStatus();
				}
			}
			resetGameScore();
		}else if(player.equals(player2)){
			if(currentGameSet.getScore2()<=6 && this.currentGameSet.getScore1()<=6){
				this.currentGameSet.setScore2(currentGameSet.getScore2()+1);
				boolean isMatchFinished = isMatchFinished();
				if((this.currentGameSet.getScore2()==6 && this.currentGameSet.getScore1()<5 
						|| this.currentGameSet.getScore2()==7 && this.currentGameSet.getScore1()==5)
						&& !isMatchFinished){
					initNewGameSet();
				}
				if(isMatchFinished){
					updateMatchStatus();
				}
			}
			resetGameScore();
		}
	}

	/**
	 * Updates the match status {@link MatchStatusEnum}
	 */
	private void updateMatchStatus() {
		//update match status
		Map<Player, Integer> score = this.getGlobalScore();
		if(score.get(player1)==3 || score.get(player2)==3){
			this.matchStatus = score.get(player1)==3 ? MatchStatusEnum.FINISHED_PLAYER1_WINS : MatchStatusEnum.FINISHED_PLAYER2_WINS;
		}else{
			this.matchStatus = MatchStatusEnum.IN_PROGRESS;
		}
		
	}

	/**
	 * Tests if the match is over
	 * @return
	 */
	private boolean isMatchFinished() {
		Map<Player, Integer> globalScore = getGlobalScore();
		for(Map.Entry<Player, Integer> ent : globalScore.entrySet()){
			if(ent.getValue()==3){
				return true;
			}
		}
		return false;
	}

	/**
	 * Counts the number of won sets for each player
	 * @return map containing each player aith his score
	 */
	public Map<Player, Integer> getGlobalScore() {
		Map<Player, Integer> scoreMap = new HashMap<Player, Integer>();
		
		Integer score1 = new Integer(0);
		Integer score2 = new Integer(0);
		for(GameSet gs : this.sets){
			if(!gs.isTieBreak() && gs.getScore1()>=6 && Math.abs(gs.getScore1() - gs.getScore2()) >=2
					|| (gs.isTieBreak() && gs.isTieBreakOver() && gs.getTieBreakWinnerScore()==gs.getScore1())){
				score1++;
			}else if(!gs.isTieBreak() && gs.getScore2()>=6 && Math.abs(gs.getScore1() - gs.getScore2()) >=2
					|| (gs.isTieBreak() && gs.isTieBreakOver() && gs.getTieBreakWinnerScore()==gs.getScore2())){
				score2++;
			}
		}
		scoreMap.put(player1, score1);
		scoreMap.put(player2, score2);
		return scoreMap;
	}
	/**
	 * Starts a new set
	 */
	private void initNewGameSet() {
		this.currentGameSet = new GameSet();
		this.sets.add(this.currentGameSet);
		resetGameScore();
	}
	/**
	 * Resets the current game scores
	 */
	private void resetGameScore() {
		this.player1.setGameScore(POINT_0);
		this.player1.setAdvantaged(false);
		this.player2.setGameScore(POINT_0);
		this.player2.setAdvantaged(false);
	}
	/**
	 * Returns the opponent player to the player given in parameter
	 * @param player current player
	 * @return
	 */
	private Player getTheOtherPlayer(Player player) {
		return player.equals(this.player1) ? this.player2 : this.player1;
	}
	
	/**
	 * get the full description of the game
	 * @return
	 */
	public String getMatchDescription(){
		StringBuilder sb = new StringBuilder();
		sb.append(MATCH_DESC_PLAYER1_LIBELLE).append(MATCH_DESC_LIBELLE_SEPARATOR)
		.append((player1 != null && player1.getName()!=null && !player1.getName().isEmpty()) ? player1.getName() : "").append(MATCH_DESC_LINE_BREAK)
		.append(MATCH_DESC_PLAYER2_LIBELLE).append(MATCH_DESC_LIBELLE_SEPARATOR)
		.append((player2 != null && player2.getName()!=null && !player2.getName().isEmpty()) ? player2.getName() : "").append(MATCH_DESC_LINE_BREAK)
		.append(MATCH_DESC_SCORE_LIBELLE).append(MATCH_DESC_LIBELLE_SEPARATOR).append(this.getScoreDescription()).append(MATCH_DESC_LINE_BREAK);
		//display current game status only if the is in progress
		if(matchStatus.equals(MatchStatusEnum.IN_PROGRESS)){
			sb.append(MATCH_DESC_CURR_GAME_STATUS).append(MATCH_DESC_LIBELLE_SEPARATOR).append(getCurrentGameStatus()).append(MATCH_DESC_LINE_BREAK);
		}
		sb.append(MATCH_DESC_MATCH_STATUS).append(MATCH_DESC_LIBELLE_SEPARATOR).append(this.getMatchStatus().toString())
			.append(MATCH_DESC_LINE_BREAK).append(MATCH_DESC_LINE_BREAK);
		return sb.toString();
	}

	/**
	 * Returns current game status
	 * @return
	 */
	private String getCurrentGameStatus() {
		String status = null;
		if(player1.isAdvantaged() || player2.isAdvantaged()){
			status = MATCH_DESC_ADVANTAGE_LIBELLE;
		}else if(player1.getGameScore()==POINT_40 && player1.getGameScore()==POINT_40){
			status = MATCH_DESC_DEUCE_LIBELLE;
		}else{
			int sc1 = getPointesFromScore(player1.getGameScore());
			int sc2 = getPointesFromScore(player2.getGameScore());
			status = sc1 + "-" + sc2;
		}
		return status;
	}


	/**
	 * Converts int score to tennis point 15,30,40
	 * @param score in score
	 * @return
	 */
	private int getPointesFromScore(int score) {
		return score >= POINT_40 ? 40 : (score * 15);
	}

	/**
	 * get the full score of the match
	 * @return
	 */
	public String getScoreDescription() {
		StringBuilder sb = new StringBuilder();
		for(GameSet gs : sets){
			sb.append(gs.toString()).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * Getter matchStatus
	 * @return
	 */
	public MatchStatusEnum getMatchStatus() {
		return matchStatus;
	}

	/**
	 * Getter player1
	 * @return player1
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Getter player2
	 * @return player2
	 */
	public Player getPlayer2() {
		return player2;
	}
	/**
	 * Getter currentGameSet
	 * @return
	 */
	public GameSet getCurrentGameSet() {
		return currentGameSet;
	}
	
}
