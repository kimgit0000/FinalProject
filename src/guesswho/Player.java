package guesswho;

// Class for all player to inherit from
public abstract class Player {
	public abstract Pair<Attribute, Value> getGuess(GameCharacter[] characters);
}