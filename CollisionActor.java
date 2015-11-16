// CollisionActor.java

package portals;


public interface CollisionActor extends CollisionObstacle {
	public int getX();
	public void setX(int newX);
	public int getY();
	public void setY(int newY);
	public int getWidth();
	public int getHeight();
	public double getVelX();
	public void setVelX(double newVelX);
	public double getVelY();
	public void setVelY(double newVelY);
}