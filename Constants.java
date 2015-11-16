// Constants.java

package portals;


public class Constants {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	public static final int BLOCK_SIZE = 50;
	public static final double GRAVITY = 0.098;
	public static final double JUMP_VEL = 6.0;
	public static final double MOVE_ACCEL = 1.0;  // 0.2 previously
	public static final double MAX_MOVE_SPEED = 3.0;
	public static final double MOVE_DECAY = 0.8;
	public static final double MOVE_DECAY_AIRBORNE = 0.9;
	public static final double TERMINAL_VELOCITY = 10.0;
	public static final int PLAYER_WIDTH = 40;
	public static final int PLAYER_HEIGHT = 75;
	public static final int PORTAL_HEIGHT = 125;
	public static final int PORTAL_DRAW_WIDTH = 7;
	public static final int PORTAL_COLLISION_WIDTH_TOLERANCE = 2;
	public static final int PORTAL_ORIENTATION_UP = 0;
	public static final int PORTAL_ORIENTATION_DOWN = 1;
	public static final int PORTAL_ORIENTATION_LEFT = 2;
	public static final int PORTAL_ORIENTATION_RIGHT = 3;
	public static final int SIDE_TOP = 0;
	public static final int SIDE_BOTTOM = 1;
	public static final int SIDE_LEFT = 2;
	public static final int SIDE_RIGHT = 3;
	public static final String LEVEL_FILE = "test_level.txt";
}