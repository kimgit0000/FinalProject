package guesswho;

public class Attribute {
	//sets the possible attributes for the characters
	public static Attribute[] possibleAttributes = { new Attribute("hair colour"), new Attribute("eye colour"),
			new Attribute("skin colour"), new Attribute("hair style"), new Attribute("has glasses"),
			new Attribute("has beard"), new Attribute("has mustache"), new Attribute("is male") };
	private String type;
	
	public Attribute(String type) {
		this.type = type;
	}
	//gets the attribute chosen
	public String getType() {
		return type;
	}
}