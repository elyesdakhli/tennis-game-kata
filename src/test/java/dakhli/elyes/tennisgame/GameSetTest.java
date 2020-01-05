package dakhli.elyes.tennisgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameSetTest {
	private GameSet gameSet;

	@Before
	public void init() {
		gameSet = new GameSet();
	}
	@Test
	public void shouldIniGameSettWithEmptyScoresTest() {
		assertEquals("New GameSet player 1 score must be equal to 0 ", 0, gameSet.getScore1());
		assertEquals("New GameSet player 2 score must be equal to 0 ", 0, gameSet.getScore2());
	}
	
	@Test
	public void shouldBeATieBreakTest() {
		gameSet.setScore1(6);
		gameSet.setScore2(6);
		assertTrue("Must be a tie break", gameSet.isTieBreak());
	}
	
	@Test
	public void tieBreakShouldBeOver() {
		gameSet.setTieBreakScore1(5);
		gameSet.setTieBreakScore2(7);
		assertTrue("Tie-break should be over", gameSet.isTieBreakOver());
	}
}
