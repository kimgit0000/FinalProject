package guesswho;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings("rawtypes")
public class Game implements Predicate {
	public static GameCharacter[] allPossibleCharacters = {};
	// The characters in the game - limited random amount of elements
	public GameCharacter[] characters;
	// Which characters player 1 has eliminated by guessing
	private boolean[] player1Mask;
	// Which characters player 2 has eliminated by guessing
	private boolean[] player2Mask;
	private Player player1;
	private Player player2;
	// Indices showing the players' choices
	private int choice1 = -1;
	private int choice2 = -1;
	// Whether both choices have been selected or not
	private int choicesSelected = 0;
	// false for player 1's turn, true for player 2's turn
	private boolean turn = false;
	private int numCharacters;

	/**
	 * Create a Game class to manage the game functionality
	 * 
	 * @param player1
	 * @param player2
	 * @param numCharacters number of game characters
	 */
	public Game(Player player1, Player player2, int numCharacters) {
		this.player1 = player1;
		this.player2 = player2;
		this.numCharacters = numCharacters;

		// Generate all possible characters
		if (allPossibleCharacters.length == 0) {
			generateAllPossibleCharacters();
		}
		// Shuffle array
		Random r = new Random();
		for (int i = 0; i < allPossibleCharacters.length; i++) {
			Bot.swap(allPossibleCharacters, i, r.nextInt(allPossibleCharacters.length - i) + i);
		}

		// Select characters for the game
		characters = new GameCharacter[numCharacters];
		for (int i = 0; i < numCharacters; i++)
			characters[i] = allPossibleCharacters[i];

		// Set the masks to false
		player2Mask = new boolean[numCharacters];
		player1Mask = new boolean[numCharacters];
	}

	private void generateAllPossibleCharacters() {
		allPossibleCharacters = findAllCombinations(Attribute.possibleAttributes);
	}

	// Recursively find every possible game character for the given attributes
	@SuppressWarnings("unchecked")
	public GameCharacter[] findAllCombinations(Attribute[] attributes) {
		// Base case
		if (attributes.length == 0)
			return new GameCharacter[] { new GameCharacter() };
		ArrayList<GameCharacter> returnArrList = new ArrayList<GameCharacter>();
		Attribute[] reducedAttributes = new Attribute[attributes.length - 1];
		for (int i = 1; i < attributes.length; i++)
			reducedAttributes[i - 1] = attributes[i];
		for (Value value : Value.possibleValues.get(attributes[0])) {
			// One subcase for each value for the first attribute in the array
			GameCharacter[] subcase = findAllCombinations(reducedAttributes);
			for (GameCharacter character : subcase) {
				character.characteristics.put(attributes[0], value);
				returnArrList.add(character);
			}
		}
		// Remove certain characters (for example a woman that is bald and is sporting a
		// mustache and a beard)
		returnArrList.removeIf(this);

		// Convert to array and return
		GameCharacter[] returnArr = new GameCharacter[returnArrList.size()];
		returnArrList.toArray(returnArr);
		return returnArr;
	}

	/**
	 * Handle one turn on guess who
	 * 
	 * @return Whether the player's guess is correct or not (true or false,
	 *         respectively)
	 */
	public boolean nextTurn() {
		if (choicesSelected != 3)
			throw new RuntimeException("Select both players' choices before making guesses!");
		// The guess of the player
		Pair<Attribute, Value> guess;
		// Whether the answer to the guess is yes or no
		boolean yesno;
		// The correct value for the guessed attribute
		Value correctVal;
		// The remaining non-eliminated characters
		ArrayList<GameCharacter> existingCharacters = new ArrayList<GameCharacter>();
		//does the checking, depending if its for the person or bot, and removes that character
		if (turn) {
			for (int i = 0; i < numCharacters; i++)
				if (!player2Mask[i])
					existingCharacters.add(characters[i]);
			GameCharacter[] existingCharactersArr = new GameCharacter[existingCharacters.size()];
			existingCharacters.toArray(existingCharactersArr);
			guess = player1.getGuess(existingCharactersArr);
			correctVal = characters[choice1].characteristics.get(guess.firstValue);
			yesno = correctVal == guess.secondValue;
			for (int i = 0; i < numCharacters; i++)
				if ((characters[i].characteristics.get(guess.firstValue) == guess.secondValue) != yesno)
					player2Mask[i] = true;
		} else {
			for (int i = 0; i < numCharacters; i++)
				if (!player1Mask[i])
					existingCharacters.add(characters[i]);
			GameCharacter[] existingCharactersArr = new GameCharacter[existingCharacters.size()];
			existingCharacters.toArray(existingCharactersArr);
			guess = player2.getGuess(existingCharactersArr);
			correctVal = characters[choice2].characteristics.get(guess.firstValue);
			yesno = correctVal == guess.secondValue;
			for (int i = 0; i < numCharacters; i++)
				if ((characters[i].characteristics.get(guess.firstValue) == guess.secondValue) != yesno)
					player1Mask[i] = true;
		}
		turn = !turn;
		return yesno;
	}

	/**
	 * Check if one of the players has won
	 * 
	 * @return Which player has won, or 0 for an ongoing game
	 */
	// -1 if player 1 won
	// 1 if player 2 won
	// 0 if the game is still going
	public int checkWin() {
		int countPlayer1 = 0;
		for (boolean maskVal : player1Mask)
			if (!maskVal)
				countPlayer1++;
		if (countPlayer1 == 1)
			return -1;

		int countPlayer2 = 0;
		for (boolean maskVal : player2Mask)
			if (!maskVal)
				countPlayer2++;
		if (countPlayer2 == 1)
			return 1;
		return 0;
	}

	@Override
	public boolean test(Object t) {
		GameCharacter character = (GameCharacter) t;
		return character.characteristics
				.get(Attribute.possibleAttributes[7]) == Value.possibleValues.get(Attribute.possibleAttributes[7])[1]
				&& (character.characteristics.get(
						Attribute.possibleAttributes[3]) == Value.possibleValues.get(Attribute.possibleAttributes[3])[0]
						|| character.characteristics.get(Attribute.possibleAttributes[5]) == Value.possibleValues
								.get(Attribute.possibleAttributes[5])[0]
						|| character.characteristics.get(Attribute.possibleAttributes[6]) == Value.possibleValues
								.get(Attribute.possibleAttributes[6])[0]);
	}

	public boolean[] getPlayer1Mask() {
		return player1Mask;
	}

	public boolean[] getPlayer2Mask() {
		return player2Mask;
	}

	public int getChoice1() {
		return choice1;
	}

	public void setChoice1(int choice1) {
		this.choice1 = choice1;
		choicesSelected |= 1;
	}

	public int getChoice2() {
		return choice2;
	}

	public void setChoice2(int choice2) {
		this.choice2 = choice2;
		choicesSelected |= 2;
	}
}