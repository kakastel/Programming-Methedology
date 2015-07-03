/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 80;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	/**Delay after each move of ball*/
	private static final int DELAY = 15;

	/* Method: init() */
	/** Sets up the Breakout program. */
	public void run() {
		instructions();
		setUp();
		play();
	}
	
	private void instructions() {
		GLabel instruct = new GLabel ("To win: Hit all the bricks with the ball " +
				"using the paddle.");
		add(instruct, (getWidth() - instruct.getWidth()) / 2, getHeight() / 2);
		waitForClick();
		remove(instruct);
	}
	
	private void setUp() {
		drawBricks();
		drawPaddle();
	}
	
	
	//Adding the Bricks
	private GRect brick;
	private void drawBricks() {
		for (int i=0; i < 10; i++) {
			for(int j=0; j<10; j++) {
				int x = j * (BRICK_WIDTH + BRICK_SEP);
				int y = BRICK_Y_OFFSET + i * (BRICK_HEIGHT + BRICK_SEP);
				brick = new GRect(x + 2, y, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				switch(i) {
					case 0: brick.setFillColor(Color.RED);
							brick.setColor(Color.RED);
							break;
					case 1: brick.setFillColor(Color.RED);
							brick.setColor(Color.RED);
							break;	
					case 2: brick.setFillColor(Color.ORANGE);
							brick.setColor(Color.ORANGE);
							break;
					case 3: brick.setFillColor(Color.ORANGE);
							brick.setColor(Color.ORANGE);
							break;
					case 4: brick.setFillColor(Color.YELLOW);
							brick.setColor(Color.YELLOW);
							break;
					case 5: brick.setFillColor(Color.YELLOW);
							brick.setColor(Color.YELLOW);
							break;
					case 6: brick.setFillColor(Color.GREEN);
							brick.setColor(Color.GREEN);
							break;
					case 7: brick.setFillColor(Color.GREEN);
							brick.setColor(Color.GREEN);
							break;
					case 8: brick.setFillColor(Color.CYAN);
							brick.setColor(Color.CYAN);
							break;
					case 9: brick.setFillColor(Color.CYAN);
							brick.setColor(Color.CYAN);
							break;
				}
				add(brick);
			}
		}
	}
	
	//Adding the Paddle
	private GRect paddle;
	private int paddley;
	private int paddlex;
	private void drawPaddle() {
		int paddlex = getWidth() / 2 - PADDLE_WIDTH / 2;
		paddley = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(paddlex, paddley, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}

	//Adding the Ball
	private GOval ball;
	private void drawBall() {
		int ballx = getWidth() / 2 - BALL_RADIUS;
		int bally =  getHeight() / 2 - BALL_RADIUS;
		ball = new GOval(ballx, bally, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}
	
 
	
	public void mouseMoved(MouseEvent e) {
		double lastX = e.getX();
		if (lastX < getWidth() - PADDLE_WIDTH / 2 && lastX > PADDLE_WIDTH / 2) {
			paddle.setLocation(lastX - PADDLE_WIDTH / 2, paddley);
		}
	}
	
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx, vy;
	private int brickNumber, lifeCounter = 3, points;
	private GLabel livesLeft;
	
	private void play() {
		brickNumber = 100;
		points = 0;
		livesLeft = new GLabel("Lives Left: " + lifeCounter);
		livesLeft.setLocation(30, paddley + 50);
		add(livesLeft);
		
		for(int turns=0; turns < NTURNS; turns++)  {
			lifeCounter--;
			drawBall();
			times = 0;
			waitForClick();
			
			int ballEnd = paddley + PADDLE_HEIGHT;
			
			while(ball.getY() < ballEnd && brickNumber != 0) {
				moveBall();
				checkForCollision();
				pause(DELAY);
			}
			remove(ball);
			updateLifeCounter();
		}
		if(brickNumber == 0) {
			winnerMessage();
		} else {
			gameOver();
		}
	}
	
	private void winnerMessage() {
		GLabel win = new GLabel("Winner", getWidth() / 2, getHeight() / 2);
		add(win);
	}
	
	private void gameOver() {
		GLabel lose = new GLabel("Game Over...", getWidth() / 2, getHeight() / 2);
		add(lose);
	}
	
	
	private int times;
	private GLabel point;
	
	private void moveBall() {
			times ++;
			if (times == 1) {
				vy = 3;
				vx = rgen.nextDouble(1.0, 3.0);
				if (rgen.nextBoolean(0.5)) vx = -vx;
			}
			ball.move(vx, vy);
		}
	
	private void checkForCollision() {
			getCollidingObject();
			GObject collider = getCollidingObject();
			if (collider == paddle) {
				vy = -vy;
				vx = rgen.nextDouble(-4.0, 4.0);
			} else if (collider != null) {
					remove(collider);
					vy = -vy;
					brickNumber--;
					points++;
				}
			if(ball.getX() > getWidth() - BALL_RADIUS || ball.getX() < BALL_RADIUS) {
				vx = -vx;
			}
			if(ball.getY() < BALL_RADIUS) {
				vy = -vy;
				double diff = ball.getY();
				ball.move(0, -2 * diff);
			}
		}
	
	private GObject getCollidingObject() {
		double x = ball.getX();
		double y = ball.getY();
		if(getElementAt(x, y) != null) {
			return getElementAt(x,y);
		} else if(getElementAt(x + BALL_RADIUS * 2, y) != null) { 
			return getElementAt(x + BALL_RADIUS * 2, y);
		} else if(getElementAt(x + BALL_RADIUS * 2, y + BALL_RADIUS * 2) != null) {
			return getElementAt(x + BALL_RADIUS * 2, y + BALL_RADIUS * 2);
		} else if(getElementAt(x, y + BALL_RADIUS * 2) != null) {
			return getElementAt(x, y + BALL_RADIUS * 2);
		} else {
			return null;
		}
	}
	
	private void updateLifeCounter() {
		livesLeft.setLabel("Lives Left: " + lifeCounter);
	}
	
}
