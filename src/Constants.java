import java.io.File;

import javax.swing.ImageIcon;

public interface Constants {
	public final int SIZE = 17;
	public final int WIN_W = 755;
	public final int WIN_H = 709;
	public final int R = 0;
	public final int L = 1;
	public final int U = 2;
	public final int D = 3;
	public final int MAX_SPEED = 5, LIFES = 3;
	public final ImageIcon[] ICONS = new ImageIcon[]{
		new ImageIcon("tile.gif"),
		new ImageIcon("wall.gif"),
		new ImageIcon("box.gif")};
	public final File[] SOUNDS = new File[]{
		new File("sounds/bomb.wav"),
		new File("sounds/pup.wav"),
		new File("sounds/die.wav")};
	public final ImageIcon[] GIRLS = new ImageIcon[]{
		new ImageIcon("girls/girl1.gif"),
		new ImageIcon("girls/girl2.gif"),
		new ImageIcon("girls/girl3.gif"),
		new ImageIcon("girls/girl4.gif")};
	public final ImageIcon[] WIN = new ImageIcon[]{
		new ImageIcon("win/tie.gif"),
		new ImageIcon("win/win1.gif"),
		new ImageIcon("win/win2.gif"),
		new ImageIcon("win/win3.gif"),
		new ImageIcon("win/win4.gif")};
	public final ImageIcon[] LIVES = new ImageIcon[]{
		new ImageIcon("num/0.gif"),
		new ImageIcon("num/1.gif"),
		new ImageIcon("num/2.gif"),
		new ImageIcon("num/3.gif"),
		new ImageIcon("p1/down1.gif"),
		new ImageIcon("p2/down1.gif"),
		new ImageIcon("p3/down1.gif"),
		new ImageIcon("p4/down1.gif")};
	public final ImageIcon[] PUPS = new ImageIcon[]{
		new ImageIcon("pup/life.gif"),
		new ImageIcon("pup/plus.gif"),
		new ImageIcon("pup/fast.gif"),	
		new ImageIcon("pup/walk.gif"),
		new ImageIcon("pup/power.gif")};
	public final ImageIcon SIDE = new ImageIcon("side.gif"), LIFE = new ImageIcon("life.gif");
	public final String[] EFFECTS = new String[]{"life","plus","fast","walk","power"};
}
