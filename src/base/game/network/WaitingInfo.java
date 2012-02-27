package base.game.network;

public class WaitingInfo {

	long timeoutMillis = 10000;//10 seconds
	long startTimeout;
	
	public WaitingInfo(long currentTimeMillis) {
		startTimeout = currentTimeMillis;
	}

	public boolean isTimeout() {
		return System.currentTimeMillis()-startTimeout > timeoutMillis;
	}

}
