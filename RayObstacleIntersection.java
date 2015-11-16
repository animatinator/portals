// RayObstacleIntersection.java

package portals;


public class RayObstacleIntersection {
	public double t;
	public int xPos;
	public int yPos;
	public int side;
	
	RayObstacleIntersection(double t, int xPos, int yPos, int side) {
		this.t = t;
		this.xPos = xPos;
		this.yPos = yPos;
		this.side = side;
	}
}