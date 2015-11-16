// CollisionRect.java

package portals;


public class CollisionRect implements CollisionObstacle {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public CollisionRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {return x;}
	public void setX(int newX) {x = newX;}
	public int getY() {return y;}
	public void setY(int newY) {y = newY;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
}