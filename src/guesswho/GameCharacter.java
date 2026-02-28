package guesswho;

import java.util.HashMap;

public class GameCharacter {
	// All stats of the character
	public HashMap<Attribute, Value> characteristics;
	
/*
	// for graphics testing only
	GameCharacter(Object[] vals) {
		System.out.println(vals.length);
		characteristics = new HashMap<>();

		for (int i = 0; i < vals.length; i++) {
			Value val = new Value(vals[i], "");
			characteristics.put(Attribute.possibleAttributes[i], val);
		}

		for (Attribute a : characteristics.keySet()) {
			System.out.println(a.getType());
			System.out.println(characteristics.get(a).getValue());
		}
	}
*/
	
	public GameCharacter() {
		characteristics = new HashMap<Attribute, Value>();
	}
}