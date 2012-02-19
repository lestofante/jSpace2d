package base.game.actions;

import base.game.GameHandler;

public abstract class GameAction {

	protected int playerID;

	public GameAction(int playerID){
		this.playerID = playerID;
	}

	public abstract void update(GameHandler manager);
	
}
