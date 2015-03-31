package distributed.systems.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import distributed.systems.das.units.Unit;

public class UnitThread extends Thread {

	private BattleFieldThread battleFieldThread;
	static final int MAXQUEUE = 100;
	private String threadName;
	private Unit unit;
	BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public UnitThread(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			while (true) {
				System.out.println("check battlefield messages in Uthread");
				if(battleFieldThread != null) {
					Message message = battleFieldThread.getMessage();
					if(message!= null) {
						unit.onMessageReceived(message);
					}
				}
				sleep(100);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	private synchronized void putMessage(Message message)
			throws InterruptedException {

		while (messages.size() == MAXQUEUE)
			wait();
		messages.offer(message);
		notify();
	}

	public synchronized void putBattleFieldThread(
			BattleFieldThread battleFieldThread) {
		this.battleFieldThread = battleFieldThread;
	}

	// Called by Battlefield
	public synchronized Message getMessage() throws InterruptedException {
		System.out.println("BattleField: getting unit message");
		notify();
//		while (messages.size() == 0)
//			wait();
		Message message = (Message) messages.poll();
		return message;
	}

	/**
	 * add message Handler
	 * 
	 * @param unit
	 */
	public synchronized void addMessageReceivedHandler(Unit unit) {
		this.unit = unit;
		Thread t = new Thread(this);
		t.start();
	}

	public synchronized void sendMessage(Message message, String origin)
			throws InterruptedException {
		message.put("origin", origin);
		putMessage(message);
	}
}
