// Level.java

package portals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;

import java.io.*;
import java.net.URI;
import java.net.URL;


public class Level {
	private int width;
	private int height;
	private boolean[][] levelData;
	
	Level(int width, int height, boolean[][] levelData) {
		this.width = width;
		this.height = height;
		this.levelData = new boolean[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.levelData[x][y] = levelData[x][y];
			}
		}
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (levelData[x][y]) {
					g.fillRect(x*Constants.BLOCK_SIZE, y*Constants.BLOCK_SIZE, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
				}
			}
		}
	}
	
	
	public static Level loadLevel(String filepath) {
		int width = 0;
		int height = 0;
		boolean[][] levelData = null;
		
		try {
			InputStream inStream = Level.class.getResourceAsStream(filepath);
			//FileInputStream inStream = new FileInputStream("portals\\" + filepath);
			DataInputStream dStream = new DataInputStream(inStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(dStream));
			
			String curLine;
			ArrayList<String> lines = new ArrayList<String>();
			
			while ((curLine = br.readLine()) != null) {
				lines.add(curLine);
				int length;
				if ((length = curLine.length()) > width) width = length;
				height++;
			}
			
			levelData = new boolean[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					levelData[x][y] = false;
				}
			}
			
			for (int i = 0; i < height; i++) {
				curLine = lines.get(i);
				
				for (int c = 0; c < curLine.length(); c++) {
					levelData[c][i] = (curLine.charAt(c) == '1');
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading input file: " + e.getMessage());
		}
		
		System.out.println("Loaded new level of size " + width + "*" + height + " successfully!");
		
		return new Level(width, height, levelData);
	}
	
	public ArrayList<CollisionObstacle> getCollisions(CollisionObstacle c) {
		ArrayList<CollisionObstacle> collisions = new ArrayList<CollisionObstacle>();
		
		int left = c.getX() / Constants.BLOCK_SIZE;
		int right = (c.getX() + c.getWidth()) / Constants.BLOCK_SIZE;
		int top = c.getY() / Constants.BLOCK_SIZE;
		int bottom = (c.getY() + c.getHeight()) / Constants.BLOCK_SIZE;
		
		for (int x = left; x <= right; x++) {
			for (int y = top; y <= bottom; y++) {
				if ((x >= 0 && x < width && y >= 0 && y < height)
					&& levelData[x][y]) {
					collisions.add(getBlockCollisionRect(x, y));
				}
			}
		}
		
		return collisions;
	}
	
	public RayObstacleIntersection getRayObstacleIntersection(Ray r) {
		RayObstacleIntersection bestHit = null;
		RayObstacleIntersection current;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (levelData[x][y]) {
					current = Collisions.getRayObstacleIntersection(r, getBlockCollisionRect(x, y));
					if (current != null) {
						if (current.t > 0 && (bestHit == null || current.t <= bestHit.t)) bestHit = current;
					}
				}
			}
		}
		
		return bestHit;
	}
	
	public CollisionRect getBlockCollisionRect(int x, int y) {
		return new CollisionRect(x*Constants.BLOCK_SIZE, y*Constants.BLOCK_SIZE, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
	}
	
	public void handleCollisions(CollisionActor c) {
		ArrayList<CollisionObstacle> collisions = getCollisions(c);
		CollisionObstacle current;
		
		for (int i = 0; i < collisions.size(); i++) {
			current = collisions.get(i);
			
			if (Collisions.isColliding(c, current)) {
				Collisions.doCollisionResolution(c, current);
			}
		}
	}
}