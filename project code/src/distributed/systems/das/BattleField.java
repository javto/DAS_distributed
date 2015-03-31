package distributed.systems.das;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import distributed.systems.core.BattleFieldThread;
import distributed.systems.core.IMessageReceivedHandler;
import distributed.systems.core.Message;
import distributed.systems.das.units.Dragon;
import distributed.systems.das.units.Player;
import distributed.systems.das.units.Unit;
import distributed.systems.das.units.Unit.UnitType;

/**
 * The actual battlefield where the fighting takes place. It consists of an
 * array of a certain width and height.
 * 
 * It is a singleton, which can be requested by the getBattleField() method. A
 * unit can be put onto the battlefield by using the putUnit() method.
 * 
 * @author Pieter Anemaet, Boaz Pat-El
 */
public class BattleField implements IMessageReceivedHandler, Serializable {
	/* the serial id necessary for the deep copy */
	private static final long serialVersionUID = -5410319266379346636L;

	/* The array of units */
	private Unit[][] map;

	/* The static singleton */
	private static BattleField battlefield;

	/* Primary socket of the battlefield */
	private BattleFieldThread serverSocket;

	/*
	 * The last id that was assigned to an unit. This variable is used to
	 * enforce that each unit has its own unique id.
	 */
	private int lastUnitID = 0;

	public final static String serverID = "server";
	public final static int MAP_WIDTH = 4;
	public final static int MAP_HEIGHT = 4;
	private ArrayList<Unit> units;

	/**
	 * Initialize the battlefield to the specified size
	 * 
	 * @param width
	 *            of the battlefield
	 * @param height
	 *            of the battlefield
	 */
	private BattleField(int width, int height) {
		synchronized (this) {
			map = new Unit[width][height];
			serverSocket = new BattleFieldThread("BattleFieldAwesome");
			serverSocket.addMessageReceivedHandler(this);
			units = new ArrayList<Unit>();
		}
	}

	/**
	 * Singleton method which returns the sole instance of the battlefield.
	 * 
	 * @return the battlefield.
	 */
	public static BattleField getBattleField() {
		if (battlefield == null)
			battlefield = new BattleField(MAP_WIDTH, MAP_HEIGHT);
		return battlefield;
	}

	/**
	 * Puts a new unit at the specified position. First, it checks whether the
	 * position is empty, if not, it does nothing. In addition, the unit is also
	 * put in the list of known units.
	 * 
	 * @param unit
	 *            is the actual unit being spawned on the specified position.
	 * @param x
	 *            is the x position.
	 * @param y
	 *            is the y position.
	 * @return true when the unit has been put on the specified position.
	 */
	private boolean spawnUnit(Unit unit, int x, int y) {
		synchronized (this) {
			if (map[x][y] != null) {
				return false;
			}

			map[x][y] = unit;
			unit.setPosition(x, y);
		}
		units.add(unit);

		return true;
	}

	/**
	 * Put a unit at the specified position. First, it checks whether the
	 * position is empty, if not, it does nothing.
	 * 
	 * @param unit
	 *            is the actual unit being put on the specified position.
	 * @param x
	 *            is the x position.
	 * @param y
	 *            is the y position.
	 * @return true when the unit has been put on the specified position.
	 */
	private synchronized boolean putUnit(Unit unit, int x, int y) {
		if (map[x][y] != null)
			return false;

		map[x][y] = unit;
		unit.setPosition(x, y);

		return true;
	}

	/**
	 * Get a unit from a position.
	 * 
	 * @param x
	 *            position.
	 * @param y
	 *            position.
	 * @return the unit at the specified position, or return null if there is no
	 *         unit at that specific position.
	 */
	public Unit getUnit(int x, int y) {
		assert x >= 0 && x < map.length;
		assert y >= 0 && x < map[0].length;

		return map[x][y];
	}

	/**
	 * Move the specified unit a certain number of steps.
	 * 
	 * @param unit
	 *            is the unit being moved.
	 * @param deltax
	 *            is the delta in the x position.
	 * @param deltay
	 *            is the delta in the y position.
	 * 
	 * @return true on success.
	 */
	private synchronized boolean moveUnit(Unit unit, int newX, int newY) {
		int originalX = unit.getX();
		int originalY = unit.getY();

		if (unit.getHitPoints() <= 0)
			return false;

		if (newX >= 0 && newX < BattleField.MAP_WIDTH)
			if (newY >= 0 && newY < BattleField.MAP_HEIGHT)
				if (map[newX][newY] == null) {
					if (putUnit(unit, newX, newY)) {
						map[originalX][originalY] = null;
						return true;
					}
				}

		return false;
	}

	/**
	 * Remove a unit from a specific position and makes the unit disconnect from
	 * the server.
	 * 
	 * @param x
	 *            position.
	 * @param y
	 *            position.
	 */
	private synchronized void removeUnit(int x, int y) {
		Unit unitToRemove = this.getUnit(x, y);
		if (unitToRemove == null)
			return; // There was no unit here to remove
		map[x][y] = null;
		unitToRemove.disconnect();
		units.remove(unitToRemove);
	}

	/**
	 * Returns a new unique unit ID.
	 * 
	 * @return int: a new unique unit ID.
	 */
	public synchronized int getNewUnitID() {
		return ++lastUnitID;
	}

	public void onMessageReceived(Message message) {
		System.out.println("BattleField: received message:");
		message.printMessageInfo();
		Message reply = null;
		String origin = (String) message.get("origin");
		MessageRequest request = (MessageRequest) message.get("request");
		Unit unit;
		switch (request) {
		case spawnUnit:
			this.spawnUnit((Unit) message.get("unit"), (Integer) message.get("x"),
					(Integer) message.get("y"));
			break;
		case putUnit:
			this.putUnit((Unit) message.get("unit"), (Integer) message.get("x"),
					(Integer) message.get("y"));
			break;
		case getUnit: {
			reply = new Message();
			int x = (Integer) message.get("x");
			int y = (Integer) message.get("y");
			/*
			 * Copy the id of the message so that the unit knows what message
			 * the battlefield responded to.
			 */
			reply.put("id", message.get("id"));
			// Get the unit at the specific location
			reply.put("unit", getUnit(x, y));
			break;
		}
		case getType: {
			reply = new Message();
			int x = (Integer) message.get("x");
			int y = (Integer) message.get("y");
			/*
			 * Copy the id of the message so that the unit knows what message
			 * the battlefield responded to.
			 */
			reply.put("id", message.get("id"));
			if (getUnit(x, y) instanceof Player)
				reply.put("type", UnitType.player);
			else if (getUnit(x, y) instanceof Dragon)
				reply.put("type", UnitType.dragon);
			else
				reply.put("type", UnitType.undefined);
			break;
		}
		case dealDamage: {
			int x = (Integer) message.get("x");
			int y = (Integer) message.get("y");
			unit = this.getUnit(x, y);
			if (unit != null)
				unit.adjustHitPoints(-(Integer) message.get("damage"));
			/*
			 * Copy the id of the message so that the unit knows what message
			 * the battlefield responded to.
			 */
			break;
		}
		case healDamage: {
			int x = (Integer) message.get("x");
			int y = (Integer) message.get("y");
			unit = this.getUnit(x, y);
			if (unit != null)
				unit.adjustHitPoints((Integer) message.get("healed"));
			/*
			 * Copy the id of the message so that the unit knows what message
			 * the battlefield responded to.
			 */
			break;
		}
		case moveUnit:
			reply = new Message();
			this.moveUnit((Unit) message.get("unit"), (Integer) message.get("x"),
					(Integer) message.get("y"));
			/*
			 * Copy the id of the message so that the unit knows what message
			 * the battlefield responded to.
			 */
			reply.put("id", message.get("id"));
			break;
		case removeUnit:
			this.removeUnit((Integer) message.get("x"), (Integer) message.get("y"));
			return;
		}

		try {
			if (reply != null) {
				System.out.println("BattleField: sending reply");
				reply.printMessageInfo();
				serverSocket.sendMessage(reply, origin);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	

	/**
	 * Close down the battlefield. Unregisters the serverSocket so the program
	 * can actually end.
	 */
	public synchronized void shutdown() {
		// Remove all units from the battlefield and make them disconnect from
		// the server
		for (Unit unit : units) {
			unit.disconnect();
			unit.stopRunnerThread();
		}

	}

	/**
	 * returns a deep copy of the battlefield
	 * 
	 * @param object
	 * @return
	 */
	public static BattleField deepClone(BattleField toCopy) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(toCopy);
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (BattleField) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BattleFieldThread getBattleFieldThread() {
		if (serverSocket == null) {
			serverSocket = new BattleFieldThread(serverID);
			serverSocket.addMessageReceivedHandler(this);
		}
		return serverSocket;
	}
}
