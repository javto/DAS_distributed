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
	static final int MAXQUEUE = 100;
	private String threadName;
	BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public BattleFieldThread(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
		UnitThread unitThread;
		Message message;
		try {
			while (true) {
				System.out.println("check unit's messages in BFthread");
				// iterate over every unit to check for new messages
				Iterator<Entry<String, UnitThread>> it = unitThreads.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, UnitThread> pair = (Map.Entry<String, UnitThread>) it
							.next();
					System.out.println(pair.getKey() + " = " + pair.getValue());
					unitThread = pair.getValue();
					message = unitThread.getMessage();
					if (message != null) {
						battleField.onMessageReceived(message);
					}
//					it.remove(); // avoids a ConcurrentModificationException
				}
				sleep(300);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public synchronized void sendMessage(Message message, String origin)
			throws InterruptedException {
		message.put("origin", origin);
		putMessage(message);
	}
	
	public synchronized void putUnitThread(String name, UnitThread unitThread) {
		unitThreads.put(name, unitThread);
	}

	private synchronized void putMessage(Message message)
			throws InterruptedException {
		messages.offer(message);
		notify();
	}

	// Called by units
	public synchronized Message getMessage() throws InterruptedException {
		System.out.println("Unit: getting BF message");
		notify();
		Message message = (Message) messages.poll();
		return message;
	}

	public synchronized void addMessageReceivedHandler(BattleField battleField) {
		this.battleField = battleField;
		Thread t = new Thread(this);
		t.start();
	}

}
