package distributed.systems.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import distributed.systems.das.units.Unit;

public class UnitThread extends Thread {

	static final int MAXQUEUE = 1;
	private String threadName;
	BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public UnitThread(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			while (true) {
				putMessage(new Message());
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

	// Called by Consumer
	public synchronized Message getMessage() throws InterruptedException {
		notify();
		while (messages.size() == 0)
			wait();
		Message message = (Message) messages.poll();
		return message;
	}
	
	/**
	 * add message Handler
	 * 
	 * @param unit
	 */
	public void addMessageReceivedHandler(Unit unit) {
		try {
			unit.onMessageReceived(getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(Message message, String origin) throws InterruptedException {
		putMessage(message);
	}
}
