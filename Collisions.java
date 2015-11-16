// Collisions.java

package portals;

import java.lang.Math;


public class Collisions {
	public static boolean isColliding(CollisionObstacle a1, CollisionObstacle a2) {
		int left1 = a1.getX(); int left2 = a2.getX();
		int right1 = left1 + a1.getWidth(); int right2 = left2 + a2.getWidth();
		int top1 = a1.getY(); int top2 = a2.getY();
		int bottom1 = top1 + a1.getHeight(); int bottom2 = top2 + a2.getHeight();
		
		boolean xIntersect = (left1 > left2 && left1 < right2) || (right1 > left2 && right1 < right2);
		boolean yIntersect = (top1 > top2 && top1 < bottom2) || (bottom1 > top2 && bottom1 < bottom2);
		
		return (xIntersect && yIntersect);
	}
	
	public static boolean isInPortal(CollisionObstacle a, Portal p) {
		// Portal collisions are handled differently as it is an infinitely thin obstacle and the
		// player's centre must be contained within it on the axis parallel to its length
		int left = a.getX(); int right = left + a.getWidth();
		int top = a.getY(); int bottom = top + a.getHeight();
		int centreX = (left + right) / 2;
		int centreY = (top + bottom) / 2;
		boolean xIntersect;
		boolean yIntersect;
		
		// Vertically standing portals
		if (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT || p.getOrientation() == Constants.PORTAL_ORIENTATION_RIGHT) {
			xIntersect = (left < p.getXPos() && right > p.getXPos());
			// Only count y-intersections where the actor's centre is in the portal
			int pY = p.getYPos();
			int halfHeight = p.getPortalHeight() / 2;
			yIntersect = (centreY > (pY - halfHeight) && centreY < (pY + halfHeight));
		}
		// Horizontally lying portals
		else {
			yIntersect = (top <= p.getYPos() && bottom >= p.getYPos());
			// Only count x-intersections where the actor's centre is in the portal
			int pX = p.getXPos();
			int halfWidth = p.getPortalHeight() / 2;
			xIntersect = (centreX > (pX - halfWidth) && centreX < (pX + halfWidth));
		}
		
		return (xIntersect && yIntersect);
	}
	
	public static RayObstacleIntersection getRayObstacleIntersection(Ray r, CollisionObstacle o) {
		int oLeft = o.getX(); int oRight = oLeft + o.getWidth();
		int oTop = o.getY(); int oBottom = oTop + o.getHeight();
		
		double xt1 = r.getTimeFromX(oLeft);
		double xt2 = r.getTimeFromX(oRight);
		double yt1 = r.getTimeFromY(oTop);
		double yt2 = r.getTimeFromY(oBottom);
		
		boolean goingRight = (xt2 >= xt1);
		boolean goingDown = (yt2 >= yt1);
		double firstX = Math.min(xt1, xt2);
		double secondX = Math.max(xt1, xt2);
		double firstY = Math.min(yt1, yt2);
		double secondY = Math.max(yt1, yt2);
		
		// If an intersection has occurred...
		if (firstX < secondY && secondX > firstY) {
			double t;
			int side;
			
			if (firstX > firstY) {
				t = firstX;
				
				if (goingRight) side = Constants.SIDE_LEFT;
				else side = Constants.SIDE_RIGHT;
			}
			else {
				t = firstY;
				
				if (goingDown) side = Constants.SIDE_TOP;
				else side = Constants.SIDE_BOTTOM;
			}
			
			int x = r.getXAtTime(t);
			int y = r.getYAtTime(t);
			
			return new RayObstacleIntersection(t, x, y, side);
		}
		
		else return null;
	}
	
	public static void doCollisionResolutionPortal(CollisionActor a, Portal p) {
		int aLeft = a.getX(); int aRight = aLeft + a.getWidth();
		int aTop = a.getY(); int aBottom = aTop + a.getHeight();
		
		// Prevent entry if the other portal doesn't exist
		if (p.getOther() == null) {
			if (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT && aRight > p.getXPos()) {
				int xRes = (p.getXPos() - a.getWidth()) - aLeft;
				if (Math.signum(a.getVelX()) != Math.signum(xRes)) a.setVelX(0.0);
				a.setX(aLeft + xRes);
			}
			else if (p.getOrientation() == Constants.PORTAL_ORIENTATION_RIGHT && aLeft < p.getXPos()) {
				int xRes = p.getXPos() - aLeft;
				if (Math.signum(a.getVelX()) != Math.signum(xRes)) a.setVelX(0.0);
				a.setX(aLeft + xRes);
			}
			else if (p.getOrientation() == Constants.PORTAL_ORIENTATION_UP && aBottom > p.getYPos()) {
				int yRes = (p.getYPos() - a.getHeight()) - aTop;
				if (Math.signum(a.getVelY()) != Math.signum(yRes)) a.setVelY(0.0);
				a.setY(aTop + yRes);
			}
			else if (p.getOrientation() == Constants.PORTAL_ORIENTATION_DOWN && aTop < p.getYPos()) {
				int yRes = p.getYPos() - aTop;
				if (Math.signum(a.getVelY()) != Math.signum(yRes)) a.setVelY(0.0);
				a.setY(aTop + yRes);
			}
		}
		else {  // Portal does exist. Portal physics engage!
			// Prevent the player moving out of the sides of the portal (enforcing walls)
			// Vertically standing portals
			if (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT || p.getOrientation() == Constants.PORTAL_ORIENTATION_RIGHT) {
				int pTop = p.getYPos() - (p.getPortalHeight() / 2);
				int pBottom = p.getYPos() + (p.getPortalHeight() / 2);
				
				// Off the top
				if (aTop < pTop && aBottom > pTop) {
					int yRes = pTop - aTop;
					if (Math.signum(a.getVelY()) != Math.signum(yRes)) a.setVelY(0.0);
					a.setY(aTop + yRes);
				}
				// Off the bottom
				else if (aTop < pBottom && aBottom > pBottom) {
					int yRes = pBottom - aBottom;
					if (Math.signum(a.getVelY()) != Math.signum(yRes)) a.setVelY(0.0);
					a.setY(aTop + yRes);
				}
			}
			// Horizontally lying portals
			else {
				int pLeft = p.getXPos() - (p.getPortalHeight() / 2);
				int pRight = p.getXPos() + (p.getPortalHeight() / 2);
				
				// Off the left
				if (aLeft < pLeft && aRight > pLeft) {
					int xRes = pLeft - aLeft;
					if (Math.signum(a.getVelX()) != Math.signum(xRes)) a.setVelX(0.0);
					a.setX(aLeft + xRes);
				}
				// Off the right
				else if (aLeft < pRight && aRight > pRight) {
					int xRes = pRight - aRight;
					if (Math.signum(a.getVelX()) != Math.signum(xRes)) a.setVelX(0.0);
					a.setX(aLeft + xRes);
				}
			}
			
			// If they've passed through sufficiently (centre position is through), put them at the other portal
			int aCentreX = (aLeft + (a.getWidth() / 2));
			int aCentreY = (aTop + (a.getHeight() / 2));
			
			if (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT && aCentreX > p.getXPos()
				|| p.getOrientation() == Constants.PORTAL_ORIENTATION_RIGHT && aCentreX < p.getXPos()
				|| p.getOrientation() == Constants.PORTAL_ORIENTATION_UP && aCentreY > p.getYPos()
				|| p.getOrientation() == Constants.PORTAL_ORIENTATION_DOWN && aCentreY < p.getYPos()) {
				int offset;  // Essentially, distance from portal centre to actor centre (parallel to portal length)
				double xVel;  // These are based on 'y' being in direction of portal height
				double yVel;  // and 'left' being left for vertical and down for horizontal portals
				// Rotations of this:
				// 
				//         U
				//         |
				//         |
				// L . . . | . . . R
				//         |
				//         |
				//         D
				
				// Vertically standing portals
				if (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT || p.getOrientation() == Constants.PORTAL_ORIENTATION_RIGHT) {
					offset = aCentreY - p.getYPos();
					yVel = a.getVelY();
					// xVel should be positive, hence following faffery
					xVel = (p.getOrientation() == Constants.PORTAL_ORIENTATION_LEFT) ? a.getVelX() : -a.getVelX();
				}
				// Horizontally lying portals
				else {
					/*if (p.getOrientation() == Constants.PORTAL_ORIENTATION_UP) offset = p.getX() - aCentreX;
					else offset = aCentreX - p.getX();*/
					offset = aCentreX - p.getXPos();
					// Again, xVel is positive (but should be the velocity along the portal's normal)
					xVel = (p.getOrientation() == Constants.PORTAL_ORIENTATION_UP) ? a.getVelY() : -a.getVelY();
					yVel = (p.getOrientation() == Constants.PORTAL_ORIENTATION_UP) ? -a.getVelX() : a.getVelX();
				}
				
				
				// Now get the other portal and place the actor there
				Portal other = p.getOther();
				int otherX = other.getXPos();
				int otherY = other.getYPos();
				
				switch(other.getOrientation()) {
					case Constants.PORTAL_ORIENTATION_UP:
						a.setX(otherX + offset);
						a.setY((otherY - 2) - (a.getHeight() / 2));
						a.setVelX(-yVel);
						a.setVelY(-xVel);
						break;
						
					case Constants.PORTAL_ORIENTATION_DOWN:
						a.setX(otherX + offset);
						a.setY((otherY + 2) - (a.getHeight() / 2));
						a.setVelX(yVel);
						a.setVelY(xVel);
						break;
						
					case Constants.PORTAL_ORIENTATION_LEFT:
						a.setX((otherX - 2) - (a.getWidth() / 2));
						a.setY(otherY + offset);
						a.setVelX(-xVel);
						a.setVelY(yVel);
						break;
						
					case Constants.PORTAL_ORIENTATION_RIGHT:
						a.setX((otherX + 2) - (a.getWidth() / 2));
						a.setY(otherY + offset);
						a.setVelX(xVel);
						a.setVelY(yVel);
						break;
				}
			}
		}
	}
	
	public static void doCollisionResolution(CollisionActor a1, CollisionObstacle a2) {		
		int left1 = a1.getX(); int left2 = a2.getX();
		int right1 = left1 + a1.getWidth(); int right2 = left2 + a2.getWidth();
		int top1 = a1.getY(); int top2 = a2.getY();
		int bottom1 = top1 + a1.getHeight(); int bottom2 = top2 + a2.getHeight();
		
		// Calculate x-resolution
		int xRes = 0;
		boolean overlapX = true;
		
		if (left1 < left2 && right1 > left2) {  // Actor is overlapping left edge of obstacle
			xRes = left2 - right1;
		}
		else if (left1 < right2 && right1 > right2) {  // Actor is overlapping right edge of obstacle
			xRes = right2 - left1;
		}
		else overlapX = false;
		
		// Calculate y-resolution
		int yRes = 0;
		boolean overlapY = true;
		
		if (top1 < top2 && bottom1 > top2) {
			yRes = top2 - bottom1;
		}
		else if (top1 < bottom2 && bottom1 > bottom2) {
			yRes = bottom2 - top1;
		}
		else overlapY = false;
		
		// If only one axis needs resolving, resolve it
		if (!overlapX) {
			a1.setY(top1 + yRes);
			// Stop the actor in its tracks if it's hurtling towards the obstacle
			if (Math.signum(a1.getVelY()) != Math.signum(yRes)) a1.setVelY(0.0);
		}
		else if (!overlapY) {
			a1.setX(left1 + xRes);
			// Stop the actor in its tracks if it's hurtling towards the obstacle
			if (Math.signum(a1.getVelX()) != Math.signum(xRes)) a1.setVelX(0.0);
		}
		else {  // Otherwise, resolve on the axis which involves least movement
			if (Math.abs(xRes) < Math.abs(yRes)) {
				a1.setX(left1 + xRes);
				// Stop the actor in its tracks if it's hurtling towards the obstacle
				if (Math.signum(a1.getVelX()) != Math.signum(xRes)) a1.setVelX(0.0);
			}
			else {
				a1.setY(top1 + yRes);
				// Stop the actor in its tracks if it's hurtling towards the obstacle
				if (Math.signum(a1.getVelY()) != Math.signum(yRes)) a1.setVelY(0.0);
			}
		}
	}
}