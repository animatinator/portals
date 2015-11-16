// Ray.java

package portals;


public class Ray {
	private int xStart;
	private int yStart;
	private double dx;
	private double dy;
	
	public Ray(int xStart, int yStart, double dx, double dy) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getXAtTime(double t) {
		//System.out.println("X at t = "+(dx*t) + " (where t = "+t+" and dx = "+dx+")");
		return (int)(xStart + (dx * t));
	}
	
	public int getYAtTime(double t) {
		return (int)(yStart + (dy * t));
	}
	
	public double getTimeFromX(int x) {
		return ((double)(x - xStart)) / dx;
	}
	
	public double getTimeFromY(int y) {
		return ((double)(y - yStart)) / dy;
	}
	
	public int getXStart() {return xStart;}
	public void setXStart(int newXStart) {xStart = newXStart;}
	public int getYStart() {return yStart;}
	public void setYStart(int newYStart) {yStart = newYStart;}
	
	public double getdx() {return dx;}
	public void setdx(double newdx) {dx = newdx;}
	public double getdy() {return dy;}
	public void setdy(double newdy) {dy = newdy;}
}