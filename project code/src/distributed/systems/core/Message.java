package distributed.systems.core;

import distributed.systems.das.MessageRequest;
import distributed.systems.das.units.Unit;
import distributed.systems.das.units.Unit.UnitType;

public class Message {

	private String id;
	private String origin;
	private Unit unit;
	private UnitType type;
	private int x;
	private int y;
	private int damage;
	private int healed;
	private MessageRequest request;

	public Message() {
	}

	public Object get(String toGet) {
		switch (toGet) {
		case "type":
			return type;
		case "origin":
			return origin;
		case "damage":
			return damage;
		case "healed":
			return healed;
		case "request":
			return request;
		case "id":
			return id;
		case "unit":
			return unit;
		case "x":
			return x;
		case "y":
			return y;
		default:
			break;
		}
		return null;
	}

	public void put(String toPut, Object putThis) {
		switch (toPut) {
		case "id":
			id = (String) putThis;
		case "origin":
			origin = (String) putThis;
		case "unit":
			unit = (Unit) putThis;
		case "type":
			type = (UnitType) putThis;
		case "x":
			x = (Integer) putThis;
		case "y":
			y = (Integer) putThis;
		case "damage":
			damage = (Integer) putThis;
		case "healed":
			healed = (Integer) putThis;
		case "request":
			request = (MessageRequest) putThis;
		default:
			break;
		}
	}
}
