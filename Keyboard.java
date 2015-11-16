// Keyboard.java

package portals;

import java.awt.event.KeyEvent;


public class Keyboard {
	public static boolean UP_ARROW = false;
	public static boolean DOWN_ARROW = false;
	public static boolean LEFT_ARROW = false;
	public static boolean RIGHT_ARROW = false;
	
	public static void handleKeyPress(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				UP_ARROW = true;
				break;
			case KeyEvent.VK_DOWN:
				DOWN_ARROW = true;
				break;
			case KeyEvent.VK_LEFT:
				LEFT_ARROW = true;
				break;
			case KeyEvent.VK_RIGHT:
				RIGHT_ARROW = true;
				break;
		}
	}
	
	public static void handleKeyRelease(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				UP_ARROW = false;
				break;
			case KeyEvent.VK_DOWN:
				DOWN_ARROW = false;
				break;
			case KeyEvent.VK_LEFT:
				LEFT_ARROW = false;
				break;
			case KeyEvent.VK_RIGHT:
				RIGHT_ARROW = false;
				break;
		}
	}
}