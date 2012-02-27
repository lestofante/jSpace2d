package base.game.network;

import java.io.IOException;
import java.util.ArrayList;

import base.worker.Worker;

public interface SelectorHandler {

	public boolean start() throws IOException;

	public ArrayList<Worker> update() throws IOException;

}
