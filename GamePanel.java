// GamePanel.java

package portals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private Level level;
	private Player player;
	
	public GamePanel() {
		addKeyListener(new TAdapter());
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		setBackground(Color.WHITE);
		setDoubleBuffered(true);
		
		level = Level.loadLevel(Constants.LEVEL_FILE);
		player = new Player(100, 100);
		//Portal portal1 = new Portal(50, 300, Constants.PORTAL_ORIENTATION_RIGHT, 0);
		Portal portal1 = new Portal(750, 350, Constants.PORTAL_ORIENTATION_LEFT, 0);
		//Portal portal1 = new Portal(200, 50, Constants.PORTAL_ORIENTATION_DOWN, 0);
		//Portal portal2 = new Portal(400, 550, Constants.PORTAL_ORIENTATION_UP, 1);
		Portal portal2 = new Portal(200, 550, Constants.PORTAL_ORIENTATION_UP, 1);
		
		Timer timer = new Timer(5, this);
		Timer slowdownTimer = new Timer(20, this);  // Use for testing
		timer.start();
	}
	
	public void update() {
		player.update();
		player.checkIfAirborne(level);
		
		// If the player is in a portal, handle physics differently
		Portal intersectedPortal = null;
		
		if ((intersectedPortal = Portal.getIntersectedPortal(player)) != null) {
			Collisions.doCollisionResolutionPortal(player, intersectedPortal);
		}
		// Otherwise, let the level do it
		else level.handleCollisions(player);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.LIGHT_GRAY);
		
		for (int x = 0; x < Constants.SCREEN_WIDTH; x += 20) {
			g2d.drawLine(x, 0, x, Constants.SCREEN_HEIGHT);
		}
		
		g2d.setColor(Color.GRAY);
		
		for (int x = 0; x < Constants.SCREEN_WIDTH; x += 100) {
			g2d.drawLine(x, 0, x, Constants.SCREEN_HEIGHT);
		}
		
		g2d.setColor(Color.LIGHT_GRAY);
		
		for (int y = 0; y < Constants.SCREEN_HEIGHT; y += 20) {
			g2d.drawLine(0, y, Constants.SCREEN_WIDTH, y);
		}
		
		g2d.setColor(Color.GRAY);
		
		for (int y = 0; y < Constants.SCREEN_HEIGHT; y += 100) {
			g2d.drawLine(0, y, Constants.SCREEN_WIDTH, y);
		}
		
		level.draw(g);  // Level platforms
		// Dotted line to the mouse
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {5.0f, 5.0f}, 0.0f));
		//g2d.drawLine(player.getX() + player.getWidth() / 2, player.getY() + player.getWidth() / 2, Mouse.xPos, Mouse.yPos);
		// ...Being replaced with a random test line to the portal gun's hit point for the moment :P
		Ray r = new Ray(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2,
			Mouse.xPos - (player.getX() + player.getWidth() / 2), Mouse.yPos - (player.getY() + player.getHeight() / 2));
		RayObstacleIntersection hit = level.getRayObstacleIntersection(r);
		if (hit != null) {
			g2d.drawLine(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, hit.xPos, hit.yPos);
		}
		// Player and portals
		player.draw(g);
		Portal.drawPortals(g);
	}
	
	public void spawnPortal(int num) {
		Ray r = new Ray(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2,
			Mouse.xPos - (player.getX() + player.getWidth() / 2), Mouse.yPos - (player.getY() + player.getHeight() / 2));
		
		RayObstacleIntersection hit = level.getRayObstacleIntersection(r);
		
		if (hit != null) {
			int direction;
			
			switch(hit.side) {
				case Constants.SIDE_TOP:
					direction = Constants.PORTAL_ORIENTATION_UP;
					break;
				case Constants.SIDE_BOTTOM:
					direction = Constants.PORTAL_ORIENTATION_DOWN;
					break;
				case Constants.SIDE_LEFT:
					direction = Constants.PORTAL_ORIENTATION_LEFT;
					break;
				case Constants.SIDE_RIGHT:
					direction = Constants.PORTAL_ORIENTATION_RIGHT;
					break;
				default:
					direction = Constants.PORTAL_ORIENTATION_UP;
					break;
			}
			
			Portal newPortal = new Portal(hit.xPos, hit.yPos, direction, num);
			level.handleCollisions(newPortal);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}
	
	public void mousePressed(MouseEvent e) {
		// ...
	}
	
	public void mouseReleased(MouseEvent e) {
		// ...
	}
	
	public void mouseEntered(MouseEvent e) {
		// ...
	}
	
	public void mouseExited(MouseEvent e) {
		// ...
	}
	
	public void mouseClicked(MouseEvent e) {
		switch(e.getButton()) {
			case MouseEvent.BUTTON1:  // Left-click: portal 0
				spawnPortal(0);
				break;
			case MouseEvent.BUTTON3:  // Right-click: portal 1
				spawnPortal(1);
				break;
			default:
				break;
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		Mouse.xPos = e.getX();
		Mouse.yPos = e.getY();
	}
	
	public void mouseDragged(MouseEvent e) {
		// ...
	}
	
	
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			player.handleKeyPress(e);
			Keyboard.handleKeyPress(e);
		}
		
		public void keyReleased(KeyEvent e) {
			player.handleKeyRelease(e);
			Keyboard.handleKeyRelease(e);
		}
	}
}