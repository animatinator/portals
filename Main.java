// Main.java

package portals;

import javax.swing.JFrame;


public class Main extends JFrame {
	public Main() {
		add(new GamePanel());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Portals! Woo!");
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}