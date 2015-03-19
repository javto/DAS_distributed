package distributed.systems.core;

import distributed.systems.das.units.Unit;

public class Message {

	private String id;
	private Unit unit;
	private int x;
	private int y;
	
	public Message() {
	}
	
	public Object get(String toGet) {
		switch (toGet) {
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
	
	public void put(String toPut, Object putThis ) {
		switch (toPut) {
		case "id":
			id = (String) putThis;
		case "unit":
			unit = (Unit) putThis;
		case "x":
			x = (Integer) putThis;
		case "y":
			y = (Integer) putThis;
		default:
			break;
		}
	}
}
