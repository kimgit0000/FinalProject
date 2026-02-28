package guesswho;

import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {
	// Difficulty of bot - lower is harder
	private int difficulty = 3;
	@SuppressWarnings("unchecked")
	// Possible guesses the bot can guess
	private Pair<Attribute, Value>[] possibleGuesses = new Pair[0];

	/**
	 * Create a bot with a custom difficulty
	 * 
	 * @param difficulty Difficulty of bot - lower is harder
	 */
	@SuppressWarnings("unchecked")
	public Bot(int difficulty) {
		this.difficulty = difficulty;

		// Generate all possible guesses
		ArrayList<Pair<Attribute, Value>> possibleGuesses = new ArrayList<Pair<Attribute, Value>>();
		for (Attribute possibleAttribute : Attribute.possibleAttributes) {
			for (Value possibleValue : Value.possibleValues.get(possibleAttribute)) {
				possibleGuesses.add(new Pair<Attribute, Value>(possibleAttribute, possibleValue));
			}
		}
		// Convert to array
		this.possibleGuesses = new Pair[possibleGuesses.size()];
		possibleGuesses.toArray(this.possibleGuesses);
	}

	/**
	 * Create a bot with the default difficulty
	 */
	public Bot() {
		this(3);
	}

	/**
	 * Swap two elements in an array
	 * 
	 * @param array
	 * @param i     The index of the first element
	 * @param j     The index of the second element
	 */
	public static void swap(Object[] array, int i, int j) {
		Object temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * Swap two ints in an array
	 * 
	 * @param array
	 * @param i     The index of the first element
	 * @param j     The index of the second element
	 */
	public static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * Get the bot's guess given an array of remaining characters
	 * 
	 * @param characters The remaining characters
	 * @return A guess, formatted as an attribute value pair
	 */
	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public Pair<Attribute, Value> getGuess(GameCharacter[] characters) {
		Random r = new Random();

		// Evaluate possible guesses
		int[] moveEvaluations = new int[possibleGuesses.length];
		for (int i = 0; i < possibleGuesses.length; i++) {
			// Count the number of characters that will be eliminated by the guess
			int eliminated = 0;
			for (GameCharacter character : characters) {
				if (character.characteristics.get(possibleGuesses[i].firstValue) != possibleGuesses[i].secondValue) {
					eliminated++;
				}
			}
			moveEvaluations[i] = Math.abs(2 * eliminated - characters.length);
		}

		// Sort guesses using improved bubble sort
		boolean swapped = true;
		for (int i = 0; swapped && i < moveEvaluations.length - 1; i++) {
			swapped = false;
			for (int j = 0; j < moveEvaluations.length - i - 1; j++) {
				if (moveEvaluations[j] > moveEvaluations[j + 1]) {
					// Swap the moveEvaluations and possibleGuesses elements at the same time to
					// keep the relative order
					swap(moveEvaluations, j, j + 1);
					swap(possibleGuesses, j, j + 1);
					swapped = true;
				}
			}
		}

		// Select a move
		int n = Math.min(difficulty, possibleGuesses.length - 1);
		for (int i = 0; i < moveEvaluations.length; i++) {
			if (moveEvaluations[i] == 0) {
				n = Math.min(n, i);
				break;
			}
		}
		// Randomly choose an index for a guess to select
		int index = r.nextInt(n + 1);
		Pair<Attribute, Value> move = possibleGuesses[index];

		// Remove move from array by creating a new arraylist with every element except
		// for the one to be removed
		ArrayList<Pair<Attribute, Value>> newArrayList = new ArrayList<Pair<Attribute, Value>>();
		for (Pair<Attribute, Value> guess : possibleGuesses) {
			if (guess == move)
				continue;
			newArrayList.add(guess);
		}
		possibleGuesses = new Pair[newArrayList.size()];
		newArrayList.toArray(possibleGuesses);

		return move;
	}
	
	//gets and sets the difficulty of the bot
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}