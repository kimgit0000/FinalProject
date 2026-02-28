package guesswho;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

public class CharacterButton extends JButton {
	// Constants for scaling
	static final double PI = 3.1415;
	static final int BRUSH_SIZE = 2;
	static int FACE_WIDTH = 84;
	static int FACE_HEIGHT = 96;
	static int WIDTH_OFFSET = 8;
	static int HEIGHT_OFFSET = 12;
	static int NAME_HEIGHT = 12;
	static int EYES_X = 14;
	static int EYES_Y = 10;
	static int IRIS_S = 8;
	static int PUPIL_S = 4;
	static int GLASSES_X = 20;
	static int GLASSES_Y = 18;

	//the base of the character's faces
	private Graphics2D g2d;
	//boolean for if they're eliminated or not
	private boolean eliminated = false;

	//sets the colour of the background
	public static void setBgColor(Color bgColor) {
		CharacterButton.bgColor = bgColor;
	}

	private static Color bgColor;

	// Randomly generated variables that aren't considered traits
	// Just to add some variety
	private int eyeBrowOrientation = (int) (Math.random() * 3);
	private int mouthExpression = (int) (Math.random() * 4);
	private int beardStyle = (int) (Math.random() * 3);
	private int mustacheStyle = (int) (Math.random() * 2);

	// The character's traits
	private Color hairColour;
	private Color eyeColour;
	private Color skinColour;
	private Integer hairStyle;
	private Boolean hasGlasses;
	private Boolean hasBeard;
	private Boolean hasMustache;
	private Boolean isMale;

	//calls the values for the character's traits and scales the sizes to fit
	CharacterButton(GameCharacter character) {
		this.hairColour = (Color) character.characteristics.get(Attribute.possibleAttributes[0]).getValue();
		this.eyeColour = (Color) character.characteristics.get(Attribute.possibleAttributes[1]).getValue();
		this.skinColour = (Color) character.characteristics.get(Attribute.possibleAttributes[2]).getValue();
		this.hairStyle = (Integer) character.characteristics.get(Attribute.possibleAttributes[3]).getValue();
		this.hasGlasses = (Boolean) character.characteristics.get(Attribute.possibleAttributes[4]).getValue();
		this.hasBeard = (Boolean) character.characteristics.get(Attribute.possibleAttributes[5]).getValue();
		this.hasMustache = (Boolean) character.characteristics.get(Attribute.possibleAttributes[6]).getValue();
		this.isMale = (Boolean) character.characteristics.get(Attribute.possibleAttributes[7]).getValue();
		setPreferredSize(new Dimension(FACE_WIDTH + WIDTH_OFFSET * 2, FACE_HEIGHT + HEIGHT_OFFSET * 2 + NAME_HEIGHT));
	}

	// copies all the values from another CharacterButton and repaints
	public void copyCharacterButton(CharacterButton button) {
		this.hairColour = button.hairColour;
		this.eyeColour = button.eyeColour;
		this.skinColour = button.skinColour;
		this.hairStyle = button.hairStyle;
		this.hasGlasses = button.hasGlasses;
		this.hasBeard = button.hasBeard;
		this.hasMustache = button.hasMustache;
		this.isMale = button.isMale;
		this.eyeBrowOrientation = button.eyeBrowOrientation;
		this.mouthExpression = button.mouthExpression;
		this.mustacheStyle = button.mustacheStyle;
		this.beardStyle = button.beardStyle;
		this.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		this.setBackground(bgColor);
		g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(BRUSH_SIZE));

		drawHairBack();
		drawFace();
		drawEyes();
		drawHairFront();
		if (hasGlasses) {
			drawGlasses();
		}
		// if a character has no hair or facial hair, it is impossible to know their hair colour
		// so I added eyebrows for this case (I didn't want to remove bald hair)
		drawEyeBrows();
		
		//draws the beard and/or mustache if the character has one
		if (hasBeard) {
			drawBeard();
		}
		if (hasMustache) {
			drawMustache();
		}
		//draws the mouth
		drawMouth();

		// if the character has eliminated, draws an x over their face
		if (eliminated) {
			g2d.setPaint(Color.red);
			g2d.setStroke(new BasicStroke(BRUSH_SIZE * 8));
			g2d.drawLine(0, 0, FACE_WIDTH + 2 * WIDTH_OFFSET, FACE_HEIGHT + 2 * HEIGHT_OFFSET);
			g2d.drawLine(FACE_WIDTH + 2 * WIDTH_OFFSET, 0, 0, FACE_HEIGHT + 2 * HEIGHT_OFFSET);
		}
	}

	// draws an x over the character
	public void eliminateCharacter() {
		eliminated = true;
		repaint();
	}

	// draws the character's eyebrows
	private void drawEyeBrows() {
		g2d.setPaint(hairColour);
		switch (eyeBrowOrientation) {
		case 0: // neutral
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 8, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50);
			g2d.drawLine(WIDTH_OFFSET + 4 * FACE_WIDTH / 5, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 5 * FACE_WIDTH / 8, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50);
			break;
		case 1: // sad
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 8, HEIGHT_OFFSET + 14 * FACE_HEIGHT / 50);
			g2d.drawLine(WIDTH_OFFSET + 4 * FACE_WIDTH / 5, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 5 * FACE_WIDTH / 8, HEIGHT_OFFSET + 14 * FACE_HEIGHT / 50);
			break;
		case 2: // angry
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 14 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 8, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50);
			g2d.drawLine(WIDTH_OFFSET + 4 * FACE_WIDTH / 5, HEIGHT_OFFSET + 14 * FACE_HEIGHT / 50,
					WIDTH_OFFSET + 5 * FACE_WIDTH / 8, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 50);
		}
	}

	// draws the character's face
	private void drawFace() {
		// draws the character's face
		g2d.setPaint(skinColour);
		g2d.fillOval(WIDTH_OFFSET, HEIGHT_OFFSET, FACE_WIDTH, FACE_HEIGHT);
	}

	// draws the character's eyes
	private void drawEyes() {
		g2d.setPaint(Color.white);
		g2d.fillOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 8 - EYES_X, HEIGHT_OFFSET + FACE_HEIGHT / 3, EYES_X, EYES_Y);
		g2d.fillOval(WIDTH_OFFSET + 5 * FACE_WIDTH / 8, HEIGHT_OFFSET + FACE_HEIGHT / 3, EYES_X, EYES_Y);

		g2d.setPaint(eyeColour);
		g2d.fillOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 8 - (EYES_X + IRIS_S) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - IRIS_S) / 2, IRIS_S, IRIS_S);
		g2d.fillOval(WIDTH_OFFSET + 5 * FACE_WIDTH / 8 + (EYES_X - IRIS_S) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - IRIS_S) / 2, IRIS_S, IRIS_S);

		g2d.setPaint(Color.black);
		g2d.fillOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 8 - (EYES_X + PUPIL_S) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - PUPIL_S) / 2, PUPIL_S, PUPIL_S);
		g2d.fillOval(WIDTH_OFFSET + 5 * FACE_WIDTH / 8 + (EYES_X - PUPIL_S) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - PUPIL_S) / 2, PUPIL_S, PUPIL_S);
	}

	// draws the character's glasses
	private void drawGlasses() {
		g2d.setPaint(Color.black);
		g2d.drawOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 8 - (EYES_X + GLASSES_X) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - GLASSES_Y) / 2, GLASSES_X, GLASSES_Y);
		g2d.drawOval(WIDTH_OFFSET + 5 * FACE_WIDTH / 8 + (EYES_X - GLASSES_X) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 + (EYES_Y - GLASSES_Y) / 2, GLASSES_X, GLASSES_Y);
		g2d.drawLine(WIDTH_OFFSET + 3 * FACE_WIDTH / 8 - (EYES_X - GLASSES_X) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 - (EYES_Y - GLASSES_Y) / 2,
				WIDTH_OFFSET + 5 * FACE_WIDTH / 8 + (EYES_X - GLASSES_X) / 2,
				HEIGHT_OFFSET + FACE_HEIGHT / 3 - (EYES_Y - GLASSES_Y) / 2);
	}

	// draw the portion of hair in the back of the face
	private void drawHairBack() {
		g2d.setPaint(hairColour);
		int curlSize = FACE_HEIGHT / 4;
		if (isMale) {
			// male hairStyles
			switch (hairStyle) {
			case 1: // straight hair
				g2d.fillArc(WIDTH_OFFSET, HEIGHT_OFFSET - FACE_HEIGHT / 9, 3 * FACE_WIDTH / 4, FACE_HEIGHT, 0, 180);
				g2d.fillArc(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET - FACE_HEIGHT / 9, 3 * FACE_WIDTH / 4,
						FACE_HEIGHT, 0, 180);
				break;
			case 2: // curly
				break;
			case 3: // afro
				g2d.fillOval(WIDTH_OFFSET - FACE_WIDTH / 18, HEIGHT_OFFSET - FACE_HEIGHT / 5, 10 * FACE_WIDTH / 9,
						3 * FACE_HEIGHT / 4);
				break;
			case 4: // wavy hair

				break;
			}
		} else {
			switch (hairStyle) {
			case 1: // straight hair
				g2d.fillRect(WIDTH_OFFSET - FACE_WIDTH / 18, HEIGHT_OFFSET + FACE_HEIGHT / 4, 10 * FACE_WIDTH / 9,
						5 * FACE_HEIGHT / 6);
				g2d.setPaint(bgColor);
				g2d.fillArc(WIDTH_OFFSET - FACE_WIDTH / 4, HEIGHT_OFFSET + 9 * FACE_HEIGHT / 10, 3 * FACE_WIDTH / 2,
						2 * FACE_HEIGHT / 3, 30, 120);
				break;
			case 2: // curly
				int rows = 5;
				int columns = 6;
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < columns; j++) {
						g2d.fillOval(WIDTH_OFFSET + j * FACE_WIDTH / columns - curlSize / columns,
								HEIGHT_OFFSET + i * FACE_HEIGHT / rows, curlSize, curlSize);
					}
				}
				break;
			case 3: // afro
				g2d.fillOval(WIDTH_OFFSET - FACE_WIDTH / 18, HEIGHT_OFFSET - FACE_HEIGHT / 5, 10 * FACE_WIDTH / 9,
						3 * FACE_HEIGHT / 4);
				break;
			case 4: // wavy hair
				g2d.setStroke(new BasicStroke(BRUSH_SIZE * 2));
				int arches = 10;
				int waves = 15;
				int wavesWidth = 17 * FACE_WIDTH / 16;
				int wavesHeight = 9 * FACE_HEIGHT / 10;

				for (int i = 0; i < waves; i++) {
					for (int j = 0; j < arches; j++) {
						if (j % 2 == 0) {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, 180);
						} else {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, -180);
						}
					}
				}
				g2d.setStroke(new BasicStroke(BRUSH_SIZE));
				break;
			}
		}
	}

	// draw the portion of hair in front of the face
	private void drawHairFront() {
		g2d.setPaint(hairColour);
		int curlSize = FACE_HEIGHT / 4;
		if (isMale) {
			// male hairStyles
			switch (hairStyle) {
			case 1: // straight hair
				break;
			case 2: // curly
				g2d.fillOval(WIDTH_OFFSET - curlSize / 8, HEIGHT_OFFSET, curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 15, curlSize,
						curlSize);
				g2d.fillOval(WIDTH_OFFSET + 2 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 10,
						curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 15,
						curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + 4 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET, curlSize, curlSize);
				break;
			case 3: // afro (
				break;
			case 4: // wavy hair
				g2d.setStroke(new BasicStroke(BRUSH_SIZE * 2));
				int arches = 10;
				double initialAngle = PI / 18;
				int initialWidth = (int) (FACE_WIDTH * Math.cos(initialAngle));
				int waveHeight = FACE_HEIGHT / 10;
				int increment = 20;

				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < arches; j++) {
						if ((j % 2 == 0 && i % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
							g2d.drawArc(WIDTH_OFFSET + j * initialWidth / arches + (FACE_WIDTH - initialWidth) / 2,
									HEIGHT_OFFSET + increment, initialWidth / arches, waveHeight, 0, 180);
						} else {
							g2d.drawArc(WIDTH_OFFSET + j * initialWidth / arches + (FACE_WIDTH - initialWidth) / 2,
									HEIGHT_OFFSET + increment, initialWidth / arches, waveHeight, 0, -180);
						}
					}
					increment -= 3;
					initialAngle += PI / 36;
					initialWidth = (int) (FACE_WIDTH * Math.cos(initialAngle));
				}
				g2d.setStroke(new BasicStroke(BRUSH_SIZE));
				break;
			}
		} else {
			// female hairstyles
			switch (hairStyle) {
			case 1: // straight hair
				g2d.fillArc(WIDTH_OFFSET - FACE_WIDTH / 18, HEIGHT_OFFSET - FACE_HEIGHT / 12, 10 * FACE_WIDTH / 9,
						2 * FACE_HEIGHT / 3, 0, 180);
				break;
			case 2: // curly
				g2d.fillOval(WIDTH_OFFSET - curlSize / 8, HEIGHT_OFFSET, curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 15, curlSize,
						curlSize);
				g2d.fillOval(WIDTH_OFFSET + 2 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 10,
						curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + 3 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET - FACE_HEIGHT / 15,
						curlSize, curlSize);
				g2d.fillOval(WIDTH_OFFSET + 4 * FACE_WIDTH / 5 - curlSize / 8, HEIGHT_OFFSET, curlSize, curlSize);
				break;
			case 3: // afro (
				break;
			case 4: // wavy hair
				g2d.setStroke(new BasicStroke(BRUSH_SIZE * 2));
				int arches = 10;
				double initialAngle = 0;
				int initialWidth = (int) (FACE_WIDTH * Math.cos(initialAngle));
				int increment = 10;

				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < arches; j++) {
						if ((j % 2 == 0 && i % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
							g2d.drawArc(WIDTH_OFFSET + j * initialWidth / arches + (FACE_WIDTH - initialWidth) / 2,
									HEIGHT_OFFSET + increment, initialWidth / arches, FACE_HEIGHT / 10, 20, 180);
						} else {
							g2d.drawArc(WIDTH_OFFSET + j * initialWidth / arches + (FACE_WIDTH - initialWidth) / 2,
									HEIGHT_OFFSET + increment, initialWidth / arches, FACE_HEIGHT / 10, 20, -180);
						}
					}
					increment -= 4;
					initialAngle += PI / 16;
					initialWidth = (int) (FACE_WIDTH * Math.cos(initialAngle));
				}

				int waves = 15;
				int wavesWidth = 17 * FACE_WIDTH / 16;
				int wavesHeight = 9 * FACE_HEIGHT / 10;
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < arches; j++) {
						if (j % 2 == 0) {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, 180);
						} else {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, -180);
						}
					}
				}
				for (int i = waves - 2; i < waves; i++) {
					for (int j = 0; j < arches; j++) {
						if (j % 2 == 0) {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, 180);
						} else {
							g2d.drawArc(WIDTH_OFFSET + i * wavesWidth / waves - FACE_WIDTH / 32,
									HEIGHT_OFFSET + j * wavesHeight / arches + FACE_HEIGHT / 9, wavesWidth / waves,
									wavesHeight / arches, 90, -180);
						}
					}
				}

				g2d.setStroke(new BasicStroke(BRUSH_SIZE));
				break;

			}
		}
	}

	// draws the character's mouth
	private void drawMouth() {
		g2d.setPaint(Color.black);
		switch (mouthExpression) {
		case 0:
			// smile
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 29 * FACE_HEIGHT / 40,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 4, HEIGHT_OFFSET + 29 * FACE_HEIGHT / 40);
			g2d.drawArc(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 5, FACE_WIDTH / 2,
					FACE_HEIGHT / 4, 0, -180);
			g2d.setPaint(Color.white);
			g2d.fillArc(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 5, FACE_WIDTH / 2,
					FACE_HEIGHT / 4, 0, -180);
			break;
		case 1:
			g2d.drawArc(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 2 * FACE_HEIGHT / 3, FACE_WIDTH / 2,
					FACE_HEIGHT / 6, -170, 160); // grin
			break;
		case 2:
			g2d.drawArc(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 4, FACE_WIDTH / 2,
					FACE_HEIGHT / 6, 10, 160); // frown
			break;
		case 3:
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 3, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 4,
					WIDTH_OFFSET + 2 * FACE_WIDTH / 3, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 4); // neutral
			break;
		}
	}

	// draws the character's beard
	private void drawBeard() {
		g2d.setPaint(hairColour);
		switch (beardStyle) {
		case 0:
			int[] xPoints = { WIDTH_OFFSET + 2 * FACE_WIDTH / 5, WIDTH_OFFSET + FACE_WIDTH / 2,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 5 };
			int[] yPoints = { HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12, HEIGHT_OFFSET + 13 * FACE_HEIGHT / 12,
					HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12 };
			g2d.fillPolygon(xPoints, yPoints, 3);
			break;
		case 1:
			g2d.setStroke(new BasicStroke(BRUSH_SIZE * 2));
			int[] xPoints2 = { WIDTH_OFFSET + 2 * FACE_WIDTH / 5, WIDTH_OFFSET + 5 * FACE_WIDTH / 11,
					WIDTH_OFFSET + 6 * FACE_WIDTH / 11, WIDTH_OFFSET + 3 * FACE_WIDTH / 5 };
			int[] yPoints2 = { HEIGHT_OFFSET + FACE_HEIGHT, HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12,
					HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12, HEIGHT_OFFSET + FACE_HEIGHT };
			g2d.fillPolygon(xPoints2, yPoints2, 4);
			g2d.drawArc(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 3 * FACE_HEIGHT / 4 - BRUSH_SIZE,
					3 * FACE_WIDTH / 5, FACE_HEIGHT / 4, 200, 140);
			g2d.setStroke(new BasicStroke(BRUSH_SIZE));
			break;
		case 2:
			int[] xPoints3 = { WIDTH_OFFSET + 2 * FACE_WIDTH / 5, WIDTH_OFFSET + 5 * FACE_WIDTH / 11,
					WIDTH_OFFSET + 6 * FACE_WIDTH / 11, WIDTH_OFFSET + 3 * FACE_WIDTH / 5 };
			int[] yPoints3 = { HEIGHT_OFFSET + FACE_HEIGHT, HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12,
					HEIGHT_OFFSET + 11 * FACE_HEIGHT / 12, HEIGHT_OFFSET + FACE_HEIGHT };
			g2d.fillPolygon(xPoints3, yPoints3, 4);
			break;
		}
	}

	// draws the character's mustache
	private void drawMustache() {
		g2d.setPaint(hairColour);
		g2d.setStroke(new BasicStroke(BRUSH_SIZE * 2));
		switch (mustacheStyle) {
		case 0:
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 4, HEIGHT_OFFSET + 2 * FACE_HEIGHT / 3,
					WIDTH_OFFSET + 7 * FACE_WIDTH / 15, HEIGHT_OFFSET + 21 * FACE_HEIGHT / 33);
			g2d.drawLine(WIDTH_OFFSET + 8 * FACE_WIDTH / 15, HEIGHT_OFFSET + 21 * FACE_HEIGHT / 33,
					WIDTH_OFFSET + 3 * FACE_WIDTH / 4, HEIGHT_OFFSET + 2 * FACE_HEIGHT / 3);
			break;
		case 1:
			g2d.drawArc(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 2 * FACE_WIDTH / 3, 3 * FACE_WIDTH / 5,
					FACE_HEIGHT / 6, 0, 180);
			g2d.drawLine(WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 19 * FACE_WIDTH / 24,
					WIDTH_OFFSET + FACE_WIDTH / 5, HEIGHT_OFFSET + 21 * FACE_HEIGHT / 24);
			g2d.drawLine(WIDTH_OFFSET + 4 * FACE_WIDTH / 5, HEIGHT_OFFSET + 19 * FACE_WIDTH / 24,
					WIDTH_OFFSET + 4 * FACE_WIDTH / 5, HEIGHT_OFFSET + 21 * FACE_HEIGHT / 24);
			break;
		}
		g2d.setStroke(new BasicStroke(BRUSH_SIZE));
	}
}