package distributed.systems.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import distributed.systems.core.exception.IDNotAssignedException;
import distributed.systems.das.BattleField;

public class BattleFieldThread extends Thread {

	HashMap<String, UnitThread> unitThreads = new HashMap<String, UnitThread>();
	private BattleField battleField;
	static final int MAXQUEUE = 1;
	private String threadName;
	BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public BattleFieldThread(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			while (true) {
				// iterate over every unit to check for new messages
				Iterator<Entry<String, UnitThread>> it = unitThreads.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, UnitThread> pair = (Map.Entry<String, UnitThread>) it
							.next();
					System.out.println(pair.getKey() + " = " + pair.getValue());
					UnitThread unitThreads = pair.getValue();
					battleField.onMessageReceived(unitThreads.getMessage());
					it.remove(); // avoids a ConcurrentModificationException
				}
				sleep(100);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public synchronized void sendMessage(Message message, String origin)
			throws IDNotAssignedException, InterruptedException {
		putMessage(message);
	}

	public synchronized void putUnitThread(String name, UnitThread unitThread) {
		unitThreads.put(name, unitThread);
	}

	private synchronized void putMessage(Message message)
			throws InterruptedException {
		while (messages.size() == MAXQUEUE)
			wait();
		messages.offer(message);
		notify();
	}

	// Called by units
	public synchronized Message getMessage() throws InterruptedException {
		notify();
		while (messages.size() == 0)
			wait();
		Message message = (Message) messages.poll();
		return message;
	}

	public synchronized void addMessageReceivedHandler(BattleField battleField) {
		this.battleField = battleField;
		Thread t = new Thread(this);
		t.start();
	}

}
