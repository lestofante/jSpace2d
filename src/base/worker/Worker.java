package base.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.GameHandler;

public abstract class Worker {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public abstract int execute(GameHandler g);
}
