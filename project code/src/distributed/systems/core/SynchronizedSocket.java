package distributed.systems.core;

import java.net.ServerSocket;

import distributed.systems.das.units.Unit;

public class SynchronizedSocket extends Socket {

	private ServerSocket serverSocket;
	private IMessageReceivedHandler handler;
	
	public SynchronizedSocket(Socket localSocket) {

	}

	
	/**
	 * add message handler for a unit
	 * @param unit
	 */
	public void addMessageReceivedHandler(Unit unit) {
		this.handler = handler;
		Thread t = new Thread(this);
		t.start();
	}
}
