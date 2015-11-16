// Portal.java

package portals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Portal implements CollisionActor {
	private static Portal p1 = null;
	private static Portal p2 = null;
	
	private int xPos;
	private int yPos;
	private int drawWidth;
	private int height;
	private int orientation;
	private Portal other;
	
	public Portal(int xPos, int yPos, int orientation, int num) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.drawWidth = Constants.PORTAL_DRAW_WIDTH;
		this.height = Constants.PORTAL_HEIGHT;
		this.orientation = orientation;
		
		if (num == 0) {
			p1 = this;
			this.other = p2;
			if (p2 != null) p2.other = this;
		}
		else {
			p2 = this;
			this.other = p1;
			if (p1 != null) p1.other = this;
		}
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLUE);
		
		switch(orientation) {
			case Constants.PORTAL_ORIENTATION_UP:
				g2d.fillRect(xPos - (height / 2), yPos - drawWidth, height, drawWidth);
				break;
			case Constants.PORTAL_ORIENTATION_DOWN:
				g2d.fillRect(xPos - (height / 2), yPos, height, drawWidth);
				break;
			case Constants.PORTAL_ORIENTATION_LEFT:
				g2d.fillRect(xPos - drawWidth, yPos - (height / 2), drawWidth, height);
				break;
			case Constants.PORTAL_ORIENTATION_RIGHT:
				g2d.fillRect(xPos, yPos - (height / 2), drawWidth, height);
				break;
		}
	}
	
	public static void drawPortals(Graphics g) {
		if (p1 != null) p1.draw(g);
		if (p2 != null) p2.draw(g);
	}
	
	public static Portal getIntersectedPortal(CollisionObstacle a) {
		if (p1 != null && Collisions.isInPortal(a, p1)) return p1;
		else if (p2 != null && Collisions.isInPortal(a, p2)) return p2;
		else return null;
	}
	
	public int getX() {
		if (orientation == Constants.PORTAL_ORIENTATION_LEFT || orientation == Constants.PORTAL_ORIENTATION_RIGHT) {
			return xPos - ((orientation == Constants.PORTAL_ORIENTATION_LEFT) ? drawWidth : 0);
		}
		else return xPos - (height / 2);
	}
	public void setX(int newX) {
		if (orientation == Constants.PORTAL_ORIENTATION_LEFT || orientation == Constants.PORTAL_ORIENTATION_RIGHT) {
			xPos = newX + ((orientation == Constants.PORTAL_ORIENTATION_LEFT) ? drawWidth : 0);
		}
		else xPos = newX + (height / 2);
	}
	public int getY() {
		if (orientation == Constants.PORTAL_ORIENTATION_UP || orientation == Constants.PORTAL_ORIENTATION_DOWN) {
			return yPos - ((orientation == Constants.PORTAL_ORIENTATION_UP) ? drawWidth : 0);
		}
		else return yPos - (height / 2);
	}
	public void setY(int newY) {
		if (orientation == Constants.PORTAL_ORIENTATION_UP || orientation == Constants.PORTAL_ORIENTATION_DOWN) {
			yPos = newY + ((orientation == Constants.PORTAL_ORIENTATION_UP) ? drawWidth : 0);
		}
		else yPos = newY + (height / 2);
	}
	
	public int getXPos() {return xPos;}
	public void setXPos(int newX) {xPos = newX;}
	public int getYPos() {return yPos;}
	public void setYPos(int newY) {yPos = newY;}
	
	public int getWidth() {return (orientation == Constants.PORTAL_ORIENTATION_LEFT || orientation == Constants.PORTAL_ORIENTATION_RIGHT) ? drawWidth : height;}
	public int getHeight() {return (orientation == Constants.PORTAL_ORIENTATION_LEFT || orientation == Constants.PORTAL_ORIENTATION_RIGHT) ? height : drawWidth;}
	
	public int getDrawWidth() {return drawWidth;}
	public int getPortalHeight() {return height;}
	
	public double getVelX() {return 0.0;}
	public void setVelX(double newVelX) {}  // Moving portals? Begone, you madman
	public void setVelY(double newVelY) {}
	public double getVelY() {return 0.0;}
	
	public int getOrientation() {return orientation;}
	
	public Portal getOther() {return other;}
}