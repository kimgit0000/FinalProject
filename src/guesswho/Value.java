package guesswho;

import java.awt.Color;
import java.util.HashMap;

public class Value {
	//the values for the different attributes a character can has
	private static final Value[] hairColours = { new Value(Color.black, "black"),
			new Value(new Color(251, 231, 161), "blonde"), new Value(new Color(100, 50, 0), "brown") };
	private static final Value[] eyeColours = { new Value(Color.black, "black"),
			new Value(new Color(140, 70, 30), "brown"), new Value(Color.cyan, "blue") };
	private static final Value[] skinColours = { new Value(new Color(0, 102, 204), "blue"),
			new Value(new Color(128, 255, 0), "green"), new Value(new Color(255, 0, 255), "pink") };
	private static final Value[] hairStyles = { new Value(0, "bald"), new Value(1, "straight"), new Value(2, "curly"),
			new Value(3, "afro"), new Value(4, "wavy") };
	private static final Value[] trueFalse = { new Value(true, "true"), new Value(false, "false") };
	private static final Value[][] vals = { hairColours, eyeColours, skinColours, hairStyles, trueFalse, trueFalse, trueFalse, trueFalse};

	//displays the possible values
	@SuppressWarnings("serial")
	public static HashMap<Attribute, Value[]> possibleValues = new HashMap<Attribute, Value[]>() {
		{
			for (int i = 0; i < vals.length; i++) {
				put(Attribute.possibleAttributes[i], vals[i]);
			}
		}
	};

	
	private Object value;
	private String displayValue;

	//gets the value the player chose
	public Value(Object value, String displayValue) {
		this.value = value;
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public Object getValue() {
		return value;
	}
}