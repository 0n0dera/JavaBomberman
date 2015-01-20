import java.awt.Rectangle;

import javax.swing.ImageIcon;


public class Powerup extends Rectangle implements Constants{
	private ImageIcon pic;	
	private String effect;
	public Powerup(int x, int y,int a){
		this.x = x;
		this.y = y;
		this.height = 32;
		this.width = 32;
		pic = PUPS[a];
		effect = EFFECTS[a];
	}
	public ImageIcon getP(){
		return pic;
	}
	public String effect(){
		return effect;
	}
}
