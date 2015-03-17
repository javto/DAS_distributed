package distributed.systems.core;

import java.util.HashMap;

import distributed.systems.core.exception.AlreadyAssignedIDException;
import distributed.systems.core.exception.IDNotAssignedException;
import distributed.systems.das.BattleField;
import distributed.systems.das.units.Unit;



public class Socket implements Runnable{

	Thread battleFieldThread;
	//key is ID	
	HashMap<Integer, Thread> unitTreads;
	
	public void run() {
		
	}
	public void sendMessage(Message damageMessage, String string) throws IDNotAssignedException{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * register with serverID
	 */
	public void register(String string) throws AlreadyAssignedIDException{

	}

	public void unRegister() {

	}

	/**
	 * add message handler for the battlefield
	 * @param battleField
	 */
	public void addMessageReceivedHandler(BattleField battleField) {
		Thread battleFieldThread = new Thread();
	}

	/**
	 * add message handler for a unit
	 * @param unit
	 */
	public void addMessageReceivedHandler(Unit unit) {

	}
	
	
}
