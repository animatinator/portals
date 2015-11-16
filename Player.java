// Player.java

package portals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import java.lang.Math;


public class Player implements CollisionActor {
	private double xPos;
	private double yPos;
	private int width;
	private int height;
	private double xVel;
	private double yVel;
	private boolean airborne = false;
	
	public Player(int startX, int startY) {
		xPos = startX;
		yPos = startY;
		width = Constants.PLAYER_WIDTH;
		height = Constants.PLAYER_HEIGHT;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)xPos, (int)yPos, width, height);
	}
	
	public void update() {
		xPos += xVel;
		yPos += yVel;
		
		yVel += Math.min(Constants.GRAVITY, Constants.TERMINAL_VELOCITY - yVel);
		
		// If the player is moving left, accelerate
		if (Keyboard.LEFT_ARROW && xVel > -Constants.MAX_MOVE_SPEED) {
			xVel -= Constants.MOVE_ACCEL;
		}
		else if (!airborne) {  // Otherwise, slow down if not airborne
			if (xVel < 0) {
				xVel *= Constants.MOVE_DECAY;
			}
		}
		else {  // Slow down a liiittle bit if airborne (getting silly, perhaps...)
			if (xVel > 0) xVel *= Constants.MOVE_DECAY_AIRBORNE;
		}
		// If the player is moving right
		if (Keyboard.RIGHT_ARROW && xVel < Constants.MAX_MOVE_SPEED) {
			xVel += Constants.MOVE_ACCEL;
		}
		else if (!airborne) {  // Again, slow down if not airborne
			if (xVel > 0) {
				xVel *= Constants.MOVE_DECAY;
			}
		}
		else {  // Slow down a liiittle bit if airborne
			if (xVel > 0) xVel *= Constants.MOVE_DECAY_AIRBORNE;
		}
	}
	
	public void checkIfAirborne(Level level) {
		// Testing for collisions with a slightly lengthened version of the player's collision
		// rect, pulled in by two pixels at each side to avoid detecting collisions with side walls
		CollisionRect testBox = new CollisionRect((int)xPos + 2, (int)yPos + 2, width - 4, height);
		
		if (level.getCollisions(testBox).size() == 0) {
			airborne = true;
		}
		else airborne = false;
	}
	
	public void handleKeyPress(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && !airborne) {
			yVel = -Constants.JUMP_VEL;
		}
	}
	
	public void handleKeyRelease(KeyEvent e) {
		// ...
	}
	
	public int getX() {return (int)xPos;}
	public void setX(int newX) {xPos = (double)newX;}
	public int getY() {return (int)yPos;}
	public void setY(int newY) {yPos = (double)newY;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public double getVelX() {return xVel;}
	public void setVelX(double newVelX) {xVel = newVelX;}
	public double getVelY() {return yVel;}
	public void setVelY(double newVelY) {yVel = newVelY;}
}