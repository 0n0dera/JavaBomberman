import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;
public class Bomb extends Rectangle implements ActionListener{
	private ImageIcon[] bomb;
	private Timer t;
	private int mode, whichP;
	public Bomb(int x, int y,int player){
		this.x = x;
		this.y = y;
		whichP = player;
		this.width = 28;
		this.height = 30;
		bomb = new ImageIcon[]{new ImageIcon("bomb1.gif"),new ImageIcon("bomb2.gif"),new ImageIcon("bomb3.gif"),new ImageIcon("bomb4.gif")};
		mode = 0;
		t = new Timer(1000,this);
		t.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == t&&mode < 3&&!Panel.pause)
			mode++;
	}
	
	public ImageIcon bombFuse(){
		return bomb[mode%4];
	}
	public Timer getT(){
		return t;
	}
	public int getP(){
		return whichP;
	}
	public int getM(){
		return mode;
	}
}

