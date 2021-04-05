/*
 * File: Breakout.java
 * -------------------
 * Developers: Yaroshepta Bohdan, Zasukha Dasha
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;

import java.awt.*;
import java.awt.event.*;


public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;

	/** Dimensions of the paddle */
	private static final int PLAYER_WIDTH = 80;
	private static final int PLAYER_HEIGHT = 5;

	/** Number of bricks per row */
	private static final int NumberOfBricksPerRow = 10;

	/** Number of rows of bricks */
	private static final int NumberRowOfBricks = 10;

	/** Separation between bricks */
	private static final int BRICK_SEPARATION = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NumberOfBricksPerRow - 1) * BRICK_SEPARATION)
			/ NumberOfBricksPerRow;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 13;

	/** Diameter of the ball in pixels */
	private static final int BALL_DIAM = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 100;

	/** Number of turns (lives) */
	private static int XP = 3;
	
	
	/** whole amount of bricks */
	private int numberOfBricks = NumberOfBricksPerRow * NumberRowOfBricks;

	/** value of balls` speed */
	private double ballSpeed = 1.5;
	
	/** ball speed by X */
	private double ballSpeedX = 1;
	
	/** ball speed by Y */
	private double ballSpeedY = 1;
	
	/** value of speed increase of the ball */
	private double speedIncrease = 1.0;
	
	/** value of player`s score */
	private int score = 0;
	
	/** line to separate upper labels and game field */
	private GRect line;
	
	/** label to show score */
	private GLabel SCOREshow;

	
	/** Runs the Breakout program. */
	public void run() {

		this.setSize(WIDTH, HEIGHT);
		this.setBackground(Color.black);

		/* cycle of a game */
		while (true) {
			addScoreLabel();
			addUpperLabels();
			setup();
			while (!gameOver()) {
				ballMove();
				checkCollision();
				// player.setLocation(ball.getX() + BALL_DIAM - PLAYER_WIDTH/2, player.getY());
				// player1.setLocation(ball.getX() + BALL_DIAM - PLAYER_WIDTH/2,
				// player1.getY());
				this.resize(WIDTH,HEIGHT);
				pause(5);
			}
			showEndGameLabel();
			if (XP == 0) {
				XP = 3;
				score = 0;
			}
		}		
	}
	
	/**
	 * adds a label with score after every collision
	 */
	private void addScoreLabel() {
		SCOREshow = new GLabel("SC: " + score);
		SCOREshow.setFont("Arial-25");
		SCOREshow.setColor(Color.WHITE);
		double xSC = SCOREshow.getBounds().getWidth();
		SCOREshow.getBounds().getHeight();
		add(SCOREshow, 9.3*WIDTH / 11 - xSC/2 + 35, BRICK_Y_OFFSET / 4);
		
	}

	/**
	 * adds a label with info for player after every turn 
	 * according to win of lose of the player
	 */
	private void showEndGameLabel() {
		if (numberOfBricks == 0) {
			removeAll();
			XP--;
			addScoreLabel();
			addUpperLabels();
			winnerLabel();
			waitForClick();
			removeAll();
			numberOfBricks = NumberOfBricksPerRow * NumberRowOfBricks;
		}
		if (ball.getY() > getHeight() - BALL_DIAM) {
			this.removeAll();
			XP--;
			addScoreLabel();
			addUpperLabels();
			loserLabel();
			waitForClick();
			removeAll();
			numberOfBricks = NumberOfBricksPerRow * NumberRowOfBricks;
		}
	}
	
	/**
	 * adds loser label
	 */
	private void loserLabel() {
		
		GLabel gameOver2 = new GLabel("Oops!");
		gameOver2.setFont("Arial-35");
		gameOver2.setColor(Color.WHITE);
		double x1 = gameOver2.getBounds().getWidth();
		double y1 = gameOver2.getBounds().getHeight();
		gameOver2.setLocation(WIDTH / 2 - x1 / 2, HEIGHT / 2 - y1);
		add(gameOver2);

		GLabel gameOver3 = new GLabel("You`re a loser!");
		gameOver3.setFont("Arial-35");
		gameOver3.setColor(Color.WHITE);
		double x = gameOver3.getBounds().getWidth();
		double y = gameOver3.getBounds().getHeight();
		gameOver3.setLocation(WIDTH / 2 - x / 2, HEIGHT / 2);
		add(gameOver3);
		
		/* after all turns adds restart label, in other case - continue label */
		if (XP == 0) {
			GLabel restart = new GLabel("Click to restart");
			restart.setFont("Arial-20");
			restart.setColor(Color.MAGENTA);
			double xR = restart.getBounds().getWidth();
			restart.setLocation(WIDTH / 2 - xR / 2, HEIGHT / 2 + y);
			add(restart);
			
		} else {
			GLabel continueL = new GLabel("Click to continue the game");
			continueL.setFont("Arial-20");
			continueL.setColor(Color.MAGENTA);
			double xC = continueL.getBounds().getWidth();
			continueL.setLocation(WIDTH / 2 - xC / 2, HEIGHT / 2 + y);
			add(continueL);
		}
	}

	/**
	 * adds a simple info bar on the top of the screen, 
	 * shows score, XPs left and name of the game
	 */
	private void addUpperLabels() {
		GLabel XPshow = new GLabel("XP: " + XP);
		XPshow.setFont("Arial-25");
		XPshow.setColor(Color.WHITE);
		double xXP = XPshow.getBounds().getWidth();
		add(XPshow, WIDTH / 11 - xXP / 2, BRICK_Y_OFFSET / 4);

		GLabel gameName = new GLabel("BREAKOUT");
		gameName.setFont("Arial-Bold-25");
		gameName.setColor(Color.MAGENTA);
		double xGN = gameName.getBounds().getWidth();
		add(gameName, WIDTH/2 - xGN/2, BRICK_Y_OFFSET/4);
		
		line = new GRect(0, BRICK_Y_OFFSET/3, WIDTH, 3);
		line.setColor(Color.BLACK);
		add(line);
	}

	/**
	 * adds winner label
	 */
	private void winnerLabel() {
		GLabel gameOver1 = new GLabel("Hooray!");
		gameOver1.setFont("Arial-35");
		gameOver1.setColor(Color.WHITE);
		double x1 = gameOver1.getBounds().getWidth();
		double y1 = gameOver1.getBounds().getHeight();
		gameOver1.setLocation(WIDTH / 2 - x1 / 2, HEIGHT / 2 - y1);
		add(gameOver1);

		GLabel gameOver = new GLabel("You`re a winner!");
		gameOver.setFont("Arial-35");
		gameOver.setColor(Color.WHITE);
		double x = gameOver.getBounds().getWidth();
		double y = gameOver.getBounds().getHeight();
		gameOver.setLocation(WIDTH / 2 - x / 2, HEIGHT / 2);
		add(gameOver);
		
		GLabel restart = new GLabel("Click to restart");
		restart.setFont("Arial-20");
		restart.setColor(Color.MAGENTA);
		double xR = restart.getBounds().getWidth();
		restart.setLocation(WIDTH / 2 - xR / 2, HEIGHT / 2 + y);
		add(restart);
		
	}

	/**
	 * checks for collision and removes an object if it isn`t a player and upper info bar 
	 * also adds points to score after brick is removed
	 */
	private void checkCollision() {
		GPoint l, r, u, d;
		GObject l1, r1, u1, d1;
		l = new GPoint(ball.getX() - 0.5, ball.getY() + BALL_DIAM / 2);
		r = new GPoint(ball.getX() + BALL_DIAM + 0.5, ball.getY() + BALL_DIAM / 2);
		u = new GPoint(ball.getX() + BALL_DIAM / 2, ball.getY() - 0.5);
		d = new GPoint(ball.getX() + BALL_DIAM / 2, ball.getY() + BALL_DIAM + 0.5);
		
		if ((getElementAt(l) != null) || (int) l.getX() <= 0) {
			l1 = getElementAt(l);
			if (l1 != player & l1 != player1 & l1 != null & l1 != line) {
				remove(getElementAt(l));
				numberOfBricks--;
				addPoints();
				remove(SCOREshow);
				addScoreLabel();
			}
			ballSpeedX = -ballSpeedX * speedIncrease;
		} else if ((getElementAt(r) != null) || (int) r.getX() >= WIDTH) {
			r1 = getElementAt(r);
			if (r1 != player & r1 != player1 & r1 != null & r1 != line) {
				remove(r1);
				numberOfBricks--;
				addPoints();
				remove(SCOREshow);
				addScoreLabel();
			}
			ballSpeedX = -ballSpeedX * speedIncrease;
		} else if ((getElementAt(u) != null) || (int) u.getY() <= 0) {
			u1 = getElementAt(u);
			if (u1 != player & u1 != player1 & u1 != null & u1 != line) {
				remove(u1);
				numberOfBricks--;
				addPoints();
				remove(SCOREshow);
				addScoreLabel();
			}
			if (u1 != player)
				ballSpeedY = -ballSpeedY * speedIncrease;
		} else if (getElementAt(d) != null) {
			d1 = getElementAt(d);
			if (d1 != player & d1 != player1 & d1 != null & d1 != line) {
				remove(d1);
				numberOfBricks--;
				addPoints();
				remove(SCOREshow);
				addScoreLabel();
			}
			ballSpeedY = -ballSpeedY * speedIncrease;
		}
		if (ballSpeedY >= 6 || ballSpeedX >= 6) {
			speedIncrease = 1;
		}
		
	}

	/**
	 * add points after brick is removed
	 */
	private void addPoints() {
		score += 10;
		
	}

	/** moving of the player */
	public void mouseMoved(MouseEvent a) {
		int x = a.getX();
		player.setLocation(x - PLAYER_WIDTH / 2, player.getY());
		player1.setLocation(player.getX(), player1.getY());
	}

	/**
	 * setting up and preparing for the game
	 * adding welcome label, player, ball, bricks and mouse listeners
	 */
	private void setup() {

		GLabel welcome = new GLabel("Click to start the game");
		welcome.setFont("Arial-35");
		welcome.setColor(Color.WHITE);
		double x = welcome.getBounds().getWidth();
		welcome.setLocation(WIDTH / 2 - x / 2, HEIGHT / 2);

		addBricks();
		if (XP == 3) {
			add(welcome);
			waitForClick();
			remove(welcome);
		}

		player = new GRect(WIDTH / 2 - PLAYER_WIDTH / 2, HEIGHT - HEIGHT / 10, PLAYER_WIDTH, PLAYER_HEIGHT);
		player1 = triangle();

		player1.setVisible(false);
		ball = new GOval(WIDTH / 2 - BALL_DIAM / 2, (HEIGHT * 8) / 10 - BALL_DIAM + 2, BALL_DIAM, BALL_DIAM);
		ball.setFilled(true);
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.white);
		player.setFilled(true);
		player.setColor(Color.WHITE);
		player.setFillColor(Color.white);

		add(ball);
		add(player);
		add(player1, WIDTH / 2 - PLAYER_WIDTH / 2, HEIGHT - HEIGHT / 10);

		ballSpeedX = random(0, 1.5);
		ballSpeedY = -Math.abs(Math.sqrt(Math.pow(ballSpeed, 2) - Math.pow(ballSpeedX, 2)));
		if (random(0, 10) % 2 == 0)
			ballSpeedX = -ballSpeedX;
		addMouseListeners();

	}

	/**
	 * adds bricks in the cycle
	 */
	private void addBricks() {
		for (int column = 0; column < NumberRowOfBricks; column++) {
			for (int row = 0; row < NumberOfBricksPerRow; row++) {
				GRect brick = new GRect(row * (BRICK_WIDTH + BRICK_SEPARATION) + BRICK_SEPARATION / 2,
						column * (BRICK_HEIGHT + BRICK_SEPARATION) + BRICK_Y_OFFSET, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setColor(Color.WHITE);
				brick.setFilled(true);
				brick.setFillColor(setNeededColor(column));
				add(brick);
			}
		}
	}

	/**
	 * changes the color of the bricks according to the number of the row
	 * 
	 * @param row
	 * @return right color
	 */
	private Color setNeededColor(int row) {
		int numberRow = row * 5 / NumberRowOfBricks;

		if (numberRow <= 0) {
			return Color.RED;
		} else if (numberRow == 1) {
			return Color.ORANGE;
		} else if (numberRow == 2) {
			return Color.YELLOW;
		} else if (numberRow == 3) {
			return Color.GREEN;
		} else {
			return Color.CYAN;
		}
	}

	/**
	 * boolean to check if the game is over
	 * 
	 * @return true if game is over, false in other case
	 */
	private boolean gameOver() {
		if (numberOfBricks == 0){ 
			return true;
		}
		else if (ball.getY() > getHeight() - BALL_DIAM) {
			return true;
		}
		return false;
	}

	/**
	 * moving of the ball
	 */
	private void ballMove() {
		ball.move(ballSpeedX, ballSpeedY);
	}

	
	/**
	 * adding a triangle to the player to check for collision
	 * 
	 * @return a triangle
	 */
	private GPolygon triangle() {
		GPolygon player2 = new GPolygon();
		player2.addVertex(0, 0);
		player2.addEdge(PLAYER_WIDTH, 0);
		player2.addEdge(-PLAYER_WIDTH / 2, 50);
		player2.addEdge(-PLAYER_WIDTH / 2, -50);
		// player2.setFilled(true);
		// player2.setColor(Color.WHITE);
		return player2;
	}

	/**
	 * returning random number to start the game with the random speed of the ball
	 * 
	 * @param min number (from this number)
	 * @param max number (till this number)
	 * @return a random number in the range of [min;max]
	 */
	private double random(double min, double max) {
		double n = 0;
		n = ((Math.random() * (max - min)) + min); // the random from 1 to 100
		print(n);
		return (n);
	}

	private GPolygon player1;
	private GOval ball;
	private GRect player;
}
