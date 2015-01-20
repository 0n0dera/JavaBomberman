import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;


public class Boom extends Rectangle implements ActionListener {
	private ImageIcon boom;
	private Timer t;
	private int counter;
	public Boom(int x, int y){
		this.x = x;
		this.y = y;
		this.width = 28;
		this.height = 30;
		counter=0;
		boom = new ImageIcon("boom.gif");
		t= new Timer(1200,this);
		t.start();
	}
	public ImageIcon getBoom(){
		return boom;
	}
	public Timer getT(){
		return t;
	}
	public void actionPerformed(ActionEvent arg0) {
		if (Panel.pause){
			t.stop();
		}
		else t.start();
		if (arg0.getSource() == t)
			counter++;
	}
	public int getC(){
		return counter;
	}
}
