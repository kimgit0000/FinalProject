package guesswho;

/*
 *
 * Descrption: A recreation of the classic board game Guess Who? digitally where each player picks a character
 * and asks questions to guess who the other person picked first
 */

import java.util.Random;

public class GuessWho {
	//calls the other classes in the package
	public static Board board;
	public static Bot bot;
	public static Human human;
	public static Game game;

	public static void main(String[] args) throws InterruptedException {
		newGame();
	}

	// begins a new game of guess who
	public static void newGame() {
		board = new Board();
		bot = new Bot();
		human = new Human(board);
		game = new Game(bot, human, 24);

		Random r = new Random();
		board.initialize();
		// Bot chooses randomly
		game.setChoice2(r.nextInt(24));

		// Wait to start until player chooses their character
	}

	//moves onto the next turn and updates the board ui every turn
	public static void doubleTurn() {
		for (int i = 0; i < 2 && game.checkWin() == 0; i++) {
			game.nextTurn();
			board.updateUI();
		}
	}
	//checks when a player wins and displays a win message
	public static void finishGame() {
		board.updateUI();
		System.out.println("Player " + ((game.checkWin() + 1) / 2 + 1) + " won!");
	}
}