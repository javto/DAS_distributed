package distributed.systems.core;

import distributed.systems.das.MessageRequest;
import distributed.systems.das.units.Unit;
import distributed.systems.das.units.Unit.UnitType;

public class Message {

	private int id;
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
			id = (Integer) putThis;
			break;
		case "origin":
			origin = (String) putThis;
			break;
		case "unit":
			unit = (Unit) putThis;
			break;
		case "type":
			type = (UnitType) putThis;
			break;
		case "x":
			x = (Integer) putThis;
			break;
		case "y":
			y = (Integer) putThis;
			break;
		case "damage":
			damage = (Integer) putThis;
			break;
		case "healed":
			healed = (Integer) putThis;
			break;
		case "request":
			request = (MessageRequest) putThis;
			break;
		default:
			break;
		}
	}
}
