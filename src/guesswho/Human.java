package guesswho;

public class Human extends Player {
	//calls the board class to create the player's board
	Board board;

	public Human(Board board) {
		this.board = board;
	}
	
	//get the player's guess and check
	@Override
	public Pair<Attribute, Value> getGuess(GameCharacter[] characters) {
		// Get the guess from the UI
		Attribute attrib = Attribute.possibleAttributes[board.attributeSelection.getSelectedIndex()];
		Value val = Value.possibleValues.get(attrib)[board.valueSelection.getSelectedIndex()];
		return new Pair<Attribute, Value>(attrib, val);
	}

}