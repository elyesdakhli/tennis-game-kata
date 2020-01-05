package dakhli.elyes.tennisgame;

import static dakhli.elyes.tennisgame.TennisGame.POINT_0;
import static dakhli.elyes.tennisgame.TennisGame.POINT_15;
import static dakhli.elyes.tennisgame.TennisGame.POINT_30;
import static dakhli.elyes.tennisgame.TennisGame.POINT_40;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TennisGameTest {
	private TennisGame tennisGame;
	private Player player1;
	private Player player2;
	
	private static final String PLAYER1_NAME = "Djokovic";
	private static final String PLAYER2_NAME = "Nadal";
	
	@Before
	public void beforeTennisGame(){
		player1 = new Player(PLAYER1_NAME);
		player2 = new Player(PLAYER2_NAME);
		tennisGame = new TennisGame(player1, player2);
	}
	
	@Test
	public void gameMustStartWithEmptyGameScoresTest() {
		assertEquals("Match status should be in progress", MatchStatusEnum.IN_PROGRESS, tennisGame.getMatchStatus());
		assertEquals("Player 1 initial score must be equal to 0", 0, player1.getGameScore());
		assertEquals("Player 2 initial score must be equal to 0", 0, player2.getGameScore());
	}
	
	@Test
	public void gameScoreMustBeIncrementedWhenPlayerWinsBallAndResetTo0WhenHeWinsTheGameTest() {
		player1.winBall();
		assertEquals("Game Score should be equal to 15", POINT_15, player1.getGameScore());
		player1.winBall();
		assertEquals("Game Score should be equal to 30", POINT_30, player1.getGameScore());
		player1.winBall();
		assertEquals("Game Score should be equal to 40", POINT_40, player1.getGameScore());
		
		winsBall(player2, 2);
		assertEquals("Game Score should be equal to 30", POINT_30, player2.getGameScore());
		
		player1.winBall(); //player wins the game (old score is 40-30)
		assertEquals("Set score for player 1 must be equal to 1", 1, tennisGame.getCurrentGameSet().getScore1());
		assertEquals("Set score for player 2 must be equal to 0", 0, tennisGame.getCurrentGameSet().getScore2());
		
		//checking game scores for both player after one player has won the game
		assertEquals("Player 1 game score must be equal to 0", POINT_0, player1.getGameScore());
		assertEquals("Player 2 game score must be equal to 0", POINT_0, player2.getGameScore());
	}
	
	@Test
	public void playerWinsAGameByDeuceRule() {
		for(int i=0; i<3; i++) {
			player1.winBall();
			player2.winBall();
		}
		assertEquals("Player 1 game score must be equal to 40", POINT_40, player1.getGameScore());
		assertEquals("Player 2 game score must be equal to 40", POINT_40, player2.getGameScore());
		
		//player 2 take the advantage
		player2.winBall();
		assertTrue("Player 2 must be advantaged", player2.isAdvantaged());
		
		//player 1 wins a ball and player 2 loses advantage => DEUCE
		player1.winBall();
		assertFalse("Player must not be advantaged", player1.isAdvantaged());
		assertFalse("Player must not be advantaged", player2.isAdvantaged());
		
		player1.winBall();
		assertTrue("Player 1 must be advantaged", player1.isAdvantaged());
		
		player1.winBall();
		assertEquals("Player 1 should win the game", 1, tennisGame.getCurrentGameSet().getScore1());
		assertEquals("Player 2 should win the game", 0, tennisGame.getCurrentGameSet().getScore2());
	}
	
	@Test
	public void gameMustStartWithEmptySetsScoresTest() {
		Map<Player, Integer> playersScore = tennisGame.getGlobalScore();		
		assertEquals("Player 1 initial score must be equal to 0", Integer.valueOf(0), playersScore.get(player1));
		assertEquals("Player 2 initial score must be equal to 0", Integer.valueOf(0), playersScore.get(player2));
	}
	
	@Test
	public void whenThePlayerWinsTheGameSetScoreIsIncrementedTest() {
		winsBall(player1, 4);
		assertEquals("The score must be 1-0", 1, tennisGame.getCurrentGameSet().getScore1());
		assertEquals("The score must be 1-0", 0, tennisGame.getCurrentGameSet().getScore2());
		
		winsBall(player2, 4*6);
		
		assertEquals("Won sets number for player 1 must be 0", Integer.valueOf(0), tennisGame.getGlobalScore().get(player1));
		assertEquals("Won sets number for player 2 must be 1", Integer.valueOf(1), tennisGame.getGlobalScore().get(player2));
		
		//match description output
		System.out.println(tennisGame.getMatchDescription());
	}
	
	@Test
	public void player1MustWinTheSetByTieBreakTest() {
		winsBall(player1, 4*5); // score 5-0
		winsBall(player2, 4*5); // score 5-5
		
		winsBall(player1, 4); // score 6-5
		assertEquals("Player 1 cannot win a set without tie-break", Integer.valueOf(0), tennisGame.getGlobalScore().get(player1));
		winsBall(player2, 4); // score 6-6
		
		System.out.println(tennisGame.getMatchDescription());
		winsBall(player1, 7);
		
		assertEquals("Won sets number for player 1 must be 1", Integer.valueOf(1), tennisGame.getGlobalScore().get(player1));
		assertEquals("Won sets number for player 2 must be 0", Integer.valueOf(0), tennisGame.getGlobalScore().get(player2));
		System.out.println(tennisGame.getMatchDescription());
	}
	
	@Test
	public void player1ShouldWinTheMatchAfterWinning3Sets() {
		winsBall(player1, 4*6*3);
		assertEquals("Player 1 should win the match", MatchStatusEnum.FINISHED_PLAYER1_WINS, tennisGame.getMatchStatus());
	}
	
	@Test
	public void player2ShouldWinTheMatchAfterWinning3Sets() {
		winsBall(player2, 4*6*3);
		assertEquals("Player 2 should win the match", MatchStatusEnum.FINISHED_PLAYER2_WINS, tennisGame.getMatchStatus());
	}
	
	private void winsBall(Player player, int nbBalls) {
		for(int i=0; i<nbBalls;i++){
			player.winBall();
		}
	}
	

}
