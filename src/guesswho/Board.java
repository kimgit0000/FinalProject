package guesswho;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Board extends JFrame implements ActionListener {
	private final int BOARD_HEIGHT = 600;
	private final int BOARD_WIDTH = 14 * BOARD_HEIGHT / 9;
	private ImageIcon guessWho;

	/*
	 * Theme 0: Dark (default theme) Theme 1: Light Theme 2: Classic (Guess who
	 * board game colours) Theme 3: Modern Arrays contain: Text colour, background
	 * colour, and face background colour in this order
	 */
	private static Color[][] themes = { { Color.white, new Color(50, 50, 50), Color.darkGray },
			{ Color.black, Color.white, Color.lightGray }, { Color.red, Color.blue, Color.yellow },
			{ Color.black, new Color(111, 148, 239), new Color(45, 200, 185) } };

	// GUI
	/*
	 * gameBoard: Contains the faces
	 * guesserPane: Where the character can select the attribute and value of their guess
	 * gameStatus: General status of the game; changes depending on how the game progresses
	 * botBoard (gameStatus): shows the bot's progress
	 * gameDialog: Contains everything else, like theme selection and difficulty
	 */
	private JPanel gameBoard, guesserPane, gameStatus, botBoard, gameDialog;
	private JLabel playerQuery, playerQuery2, logo, turnsDisplay, botDifficulties, text, text2;
	public JComboBox valueSelection, attributeSelection;
	private JComboBox themeSelection;
	private JButton submitGuess;

	private CharacterButton[][] faces;
	private JLabel[][] botFaces;
	private CharacterButton playerChoice;

	// Board functionality
	private GameCharacter[] gameCharacters;
	private int turnCount, botDiff;
	private String difficulty;

	public void initialize() {
		gameCharacters = GuessWho.game.characters;
		turnCount = 0;

		guessWho = new ImageIcon("src/guesswho/Guess_who_game_logo.png");
		// Resize the image
		Image resizedImage = guessWho.getImage().getScaledInstance(BOARD_WIDTH / 10, BOARD_HEIGHT / 11,
				Image.SCALE_SMOOTH);

		// gets the player to select a difficulty
		difficultySelection();

		// initializes faces (gameBoard)
		faces = new CharacterButton[4][6];
		for (int i = 0; i < faces.length; i++) {
			for (int j = 0; j < faces[0].length; j++) {
				faces[i][j] = new CharacterButton(gameCharacters[i * 6 + j]);
				faces[i][j].addActionListener(this);
			}
		}

		// initializes gameBoard (CENTER)
		gameBoard = new JPanel();
		gameBoard.setPreferredSize(new Dimension(19 * BOARD_WIDTH / 32, 8 * BOARD_HEIGHT / 9));
		gameBoard.setLayout(new GridLayout(4, 6, 5, 1));
		for (int i = 0; i < faces.length; i++) {
			for (int j = 0; j < faces[0].length; j++) {
				gameBoard.add(faces[i][j]);
			}
		}

		// initializes guesserPane (SOUTH)
		guesserPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		guesserPane.setLayout(new FlowLayout());
		guesserPane.setPreferredSize(new Dimension(BOARD_WIDTH, 50));

		// initializes comboBoxes and button where player will select their guess
		playerQuery = new JLabel("Attribute: ");
		String[] attributeNames = new String[Attribute.possibleAttributes.length];
		for (int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = Attribute.possibleAttributes[i].getType();
		}
		attributeSelection = new JComboBox(attributeNames);
		attributeSelection.addActionListener(this);
		playerQuery2 = new JLabel("Value: ");
		valueSelection = new JComboBox();
		refreshComboBoxValues();
		submitGuess = new JButton("Submit Question");
		submitGuess.addActionListener(this);
		submitGuess.setEnabled(false);

		guesserPane.add(playerQuery);
		guesserPane.add(attributeSelection);
		guesserPane.add(playerQuery2);
		guesserPane.add(valueSelection);
		guesserPane.add(submitGuess);

		// initializes gameInfo (EAST)
		gameStatus = new JPanel();
		gameStatus.setLayout(new GridLayout(5, 1, 5, 1));
		gameStatus.setPreferredSize(new Dimension(5 * BOARD_WIDTH / 32, BOARD_HEIGHT));

		// content
		text = new JLabel("Your Choice:");
		text.setPreferredSize(new Dimension(250, 50));
		// creates characterButton with an arbitrary character, doesn't matter since its
		// invisible and will be changed later
		playerChoice = new CharacterButton(gameCharacters[0]);
		playerChoice.setPreferredSize(new Dimension(250, 250));
		playerChoice.setVisible(false);

		// botBoard: a panel with the progress of the bot
		text2 = new JLabel("Bot's progress");
		text2.setPreferredSize(new Dimension(400, 50));
		botBoard = new JPanel();
		botBoard.setPreferredSize(new Dimension(6 * BOARD_WIDTH / 32, 3 * BOARD_HEIGHT / 9));
		botBoard.setLayout(new GridLayout(4, 6, 3, 3));
		botFaces = new JLabel[faces.length][faces[0].length];
		for (int i = 0; i < botFaces.length; i++) {
			for (int j = 0; j < botFaces[0].length; j++) {
				botFaces[i][j] = new JLabel();
				botFaces[i][j].setBackground(Color.white);
				botFaces[i][j].setOpaque(true);
				// botFaces[i][j].setIcon(new ImageIcon(resizedImage));
				botBoard.add(botFaces[i][j]);
			}
		}
		turnsDisplay = new JLabel("You are on turn: " + turnCount);

		// add everything in gameStatus
		gameStatus.add(turnsDisplay);
		gameStatus.add(text);
		gameStatus.add(playerChoice);
		gameStatus.add(text2);
		gameStatus.add(botBoard);

		// initializes (WEST)
		gameDialog = new JPanel(new FlowLayout(FlowLayout.CENTER));
		gameDialog.setPreferredSize(new Dimension(BOARD_WIDTH / 6, BOARD_HEIGHT));
		// contents
		logo = new JLabel();
		logo.setIcon(new ImageIcon(resizedImage));
		themeSelection = new JComboBox(new String[] { "Dark", "Light", "Classic", "Modern" });
		themeSelection.setToolTipText("Select a theme");
		themeSelection.addActionListener(this);
		botDifficulties = new JLabel("<html>The difficulty level<br>of the bot is:<html>" + difficulty);
		// add
		gameDialog.add(themeSelection);
		gameDialog.add(logo);
		gameDialog.add(botDifficulties);

		// initializes frame
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// add everything
		add(gameBoard, BorderLayout.CENTER);
		add(guesserPane, BorderLayout.SOUTH);
		add(gameStatus, BorderLayout.EAST);
		add(gameDialog, BorderLayout.WEST);
		setVisible(true);

		recolor(themes[0]);
		// Ask for the player to select their character
		JOptionPane.showMessageDialog(this, "Click on a character on the game board to select your character",
				"Character Selection", JOptionPane.PLAIN_MESSAGE);
	}

	private void difficultySelection() {
		String extraMessage = "";
		do {
			difficulty = JOptionPane.showInputDialog(this,
					"Enter the difficulty level for your opponent(1 - 10):" + extraMessage, "Opponent level",
					JOptionPane.PLAIN_MESSAGE);
			try {
				botDiff = 11 - Integer.parseInt(difficulty);
				if (difficulty == null || difficulty.isEmpty()) {
					difficulty = "10"; // Default if no input is provided
				}
				// Because the settings for difficulty is reversed, 1 is hardest and 10 is
				// easiest, so map the range [1, 10] to [10, 1]
				GuessWho.bot.setDifficulty(botDiff);
			} catch (NumberFormatException e) {
				extraMessage = "\n Invalid input, enter a number";
				continue;
			}
			extraMessage = "\n Invalid input, enter a number between 1 and 10 inclusive";

		} while (botDiff > 10 || botDiff <= 0);
	}

	//recolors the board depending on the theme the player chooses
	private void recolor(Color[] theme) {
		playerQuery.setForeground(theme[0]);
		playerQuery2.setForeground(theme[0]);
		turnsDisplay.setForeground(theme[0]);
		text.setForeground(theme[0]);
		text2.setForeground(theme[0]);
		botDifficulties.setForeground(theme[0]);
		gameBoard.setBackground(theme[1]);
		guesserPane.setBackground(theme[1]);
		gameStatus.setBackground(theme[1]);
		gameDialog.setBackground(theme[1]);
		CharacterButton.setBgColor(theme[2]);
		for (int i = 0; i < faces.length; i++) {
			for (int j = 0; j < faces[0].length; j++) {
				faces[i][j].repaint();
			}
		}
	}

	// Changes the selection of values on comboBox based on selected attribute
	private void refreshComboBoxValues() {
		valueSelection.removeAllItems();
		Value[] values = Value.possibleValues.get(Attribute.possibleAttributes[attributeSelection.getSelectedIndex()]);
		for (int i = 0; i < values.length; i++) {
			valueSelection.addItem(values[i].getDisplayValue());
		}
	}

	// X out characters
	public void removeChars(boolean turn) {
		if (turn) {
			// x out the human player's board
			for (int i = 0; i < faces.length; i++) {
				for (int j = 0; j < faces[0].length; j++) {
					if (GuessWho.game.getPlayer1Mask()[i * 6 + j]) {
						faces[i][j].eliminateCharacter();
					}
				}
			}
		} else {
			//greys out the bot's characters
			for (int i = 0; i < botFaces.length; i++) {
				for (int j = 0; j < botFaces[0].length; j++) {
					if (GuessWho.game.getPlayer2Mask()[i * 6 + j]) {
						botFaces[i][j].setBackground(Color.darkGray);
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitGuess) {
			GuessWho.doubleTurn();
		} else if (e.getSource() == attributeSelection) {
			refreshComboBoxValues();
		} else if (e.getSource() == themeSelection) {
			recolor(themes[themeSelection.getSelectedIndex()]);
		} else {
			// a face is clicked on (gets player's choice)
			int index = -1;
			for (int i = 0; i < faces.length; i++) {
				for (int j = 0; j < faces[0].length; j++) {
					if (e.getSource() == faces[i][j]) {
						index = i * 6 + j;
					}
				}
			}
			for (int i = 0; i < faces.length; i++) {
				for (int j = 0; j < faces[0].length; j++) {
					faces[i][j].setEnabled(false);
				}
			}
			GuessWho.game.setChoice1(index);
			playerChoice.copyCharacterButton(faces[index / 6][index % 6]);
			playerChoice.setVisible(true);
			playerChoice.setEnabled(false);
			submitGuess.setEnabled(true);
		}
	}

	public void updateUI() {
		refreshComboBoxValues();
		removeChars(turnCount++ % 2 == 0);
		turnsDisplay.setText("You are on turn: " + (turnCount / 2));
		if (GuessWho.game.checkWin() == -1) {
			JOptionPane.showMessageDialog(this, "YOU WIN!!!", "Win Message", JOptionPane.PLAIN_MESSAGE);
			submitGuess.setEnabled(false);
		} else if (GuessWho.game.checkWin() == 1) {
			JOptionPane.showMessageDialog(this, "YOU LOSE!!!", "Lose Message", JOptionPane.PLAIN_MESSAGE);
			submitGuess.setEnabled(false);
		}

		// Gives the player an option to play again
		if(GuessWho.game.checkWin() != 0) {
			int playAgain = JOptionPane.showConfirmDialog(this, "Play again?", "New game", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (playAgain) {
				case 2: // cancel
				case 1: // no
					System.exit(0);
					break;
				case 0: // yes
					GuessWho.board.setVisible(false);
					GuessWho.board.dispose();
					GuessWho.newGame();
					break;
			}
		}
	}
}