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
				//iterate over every unit to check for new messages
				Iterator<Entry<String, UnitThread>> it = unitThreads.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, UnitThread> pair = (Map.Entry<String, UnitThread>) it
							.next();
					System.out.println(pair.getKey() + " = " + pair.getValue());
					it.remove(); // avoids a ConcurrentModificationException
				}
				Message message = unitThreads.get("test").getMessage();
				System.out.println("got message");
				sleep(2000);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void sendMessage(Message message, String origin)
			throws IDNotAssignedException, InterruptedException {
		putMessage(message);
	}
	
	private synchronized void putMessage(Message message)
			throws InterruptedException {
		while (messages.size() == MAXQUEUE)
			wait();
		messages.offer(message);
		notify();
	}

	// Called by Consumer
	public synchronized Message getMessage() throws InterruptedException {
		notify();
		while (messages.size() == 0)
			wait();
		Message message = (Message) messages.poll();
		return message;
	}

	public void addMessageReceivedHandler(BattleField battleField) {
		try {
			battleField.onMessageReceived(getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
