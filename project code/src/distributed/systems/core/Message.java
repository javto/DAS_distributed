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
	private int to;

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
		case "to":
			return to;
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
		case "to":
			to = (Integer) putThis;
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

	public void printMessageInfo() {
		String origin = (String) get("origin");
		MessageRequest requestType = (MessageRequest) get("request");
		Integer id = (Integer) get("id");
		Integer x = (Integer) get("x");
		Integer y = (Integer) get("y");
		Unit unitInfo = (Unit) get("unit");
		UnitType unitType = (UnitType) get("type");
		Integer damage = (Integer) get("damage");
		Integer healed = (Integer) get("healed");
		Integer to = (Integer) get("to");
		StringBuilder info = new StringBuilder();
		info.append("Message contents[");
		if (origin != null) {
			info.append(" origin: " + origin + ".");
		}
		if (to != null) {
			info.append(" to: " + to + ".");
		}
		if (requestType != null) {
			info.append(" requestType: " + requestType.name() + ".");
		}
		if (unitInfo != null) {
			info.append(" unitID: " + unitInfo.getUnitID() + ".");
		}
		if (id != null) {
			info.append(" id: " + id.intValue() + ".");
		}
		if (x != null) {
			info.append(" x: " + x.intValue() + ".");
		}
		if (y != null) {
			info.append(" y: " + y.intValue() + ".");
		}
		if (unitType != null) {
			info.append(" unitType: " + unitType.name() + ".");
		}
		if (damage != null) {
			info.append(" damage: " + damage.intValue() + ".");
		}
		if (healed != null) {
			info.append(" healed: " + healed.intValue() + ".");
		}
		info.append("]");
		System.out.println(info.toString());
	}
}
