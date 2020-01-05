package dakhli.elyes.tennisgame;

public class Player {
	
	private String name;
	private int gameScore;
	private boolean advantaged;
	private TennisGame tennisGame;
	
	public Player(String name) {
		super();
		this.name = name;
	}
	/**
	 * To call when the player scores
	 */
	public void winBall(){
		if(tennisGame!=null){
			tennisGame.winBall(this);
		}
	}
	public void incrementGameScore(){
		this.gameScore++;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGameScore() {
		return gameScore;
	}
	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	public TennisGame getTennisGame() {
		return tennisGame;
	}

	public void setTennisGame(TennisGame tennisGame) {
		this.tennisGame = tennisGame;
	}
	public boolean isAdvantaged() {
		return advantaged;
	}
	public void setAdvantaged(boolean advantaged) {
		this.advantaged = advantaged;
	}
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		Player p = (Player) obj;
		return p.name.equalsIgnoreCase(this.name);
	}
	
	
}
