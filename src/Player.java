import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class Player extends Rectangle implements ActionListener,Constants {
	private ImageIcon[] up,down,left,right;
	private int phase,lives,spd,power;
	protected int numBomb;
	private int midX;
	private int midY;
	private boolean walkOver,invul,dead;
	private String whichDir;
	private Timer t, invulT;
	public Player(String p,int x, int y,String wD){
		this.x = x;
		this.y = y;
		this.width = 24;
		this.height = 30;
		midX=x+13;
		midY=y+15;
		whichDir = wD;
		walkOver = false;
		dead = false;
		up = new ImageIcon[]{new ImageIcon(p+"/up1.gif"),new ImageIcon(p+"/up2.gif"),new ImageIcon(p+"/up3.gif")};
		left = new ImageIcon[]{new ImageIcon(p+"/left1.gif"),new ImageIcon(p+"/left2.gif"),new ImageIcon(p+"/left3.gif")};
		right = new ImageIcon[]{new ImageIcon(p+"/right1.gif"),new ImageIcon(p+"/right2.gif"),new ImageIcon(p+"/right3.gif")};
		down = new ImageIcon[]{new ImageIcon(p+"/down1.gif"),new ImageIcon(p+"/down2.gif"),new ImageIcon(p+"/down3.gif")};
		invul = false;
		phase = 0;
		invulT = new Timer(1500,this);
		t = new Timer(150,this);
		t.start();
	}
	public void actionPerformed(ActionEvent e) {
		phase++;
		if (e.getSource() == invulT){
			invul = false;
			invulT.stop();
		}
	}
	public ImageIcon move(String dir){
		if (dir.equals("up")){
			up();
			whichDir = "u";
			return up[phase%3];
		}
		else if (dir.equals("down")){
			down();
			whichDir = "d";
			return down[phase%3];
		}
		else if (dir.equals("right")){
			right();
			whichDir = "r";
			return right[phase%3];
		}
		else if (dir.equals("left")){
			left();
			whichDir = "l";
			return left[phase%3];
		}
		else{
			if (whichDir.equals("r"))
				return right[0];
			else if (whichDir.equals("l"))
				return left[2];
			else if (whichDir.equals("u"))
				return up[1];
			return down[1];
		}
	}
	private void right(){
		x+=spd;
		midX+=spd;
	}
	private void up(){
		y-=spd;
		midY-=spd;
	}
	private void left(){
		x-=spd;
		midX-=spd;
	}
	private void down(){
		y+=spd;
		midY+=spd;
	}
	public int getPow(){
		return power;
	}
	public int totalBomb(){
		return numBomb;
	}
	public void putBomb(){
		numBomb--;
	}
	public int getCoordX(){
		return midX/40;
	}
	public int getCoordY(){
		return midY/40;
	}
	public int getGridPosX(){
		return midX%40;
	}
	public int getGridPosY(){
		return midY%40;
	}
	public boolean getWO(){
		return walkOver;
	}
	public void setWO(){
		walkOver = true;
	}
	public int getmidX(){
		return midX;
	}
	public int getmidY(){
		return midY;
	}
	public void lifeUp(){
		if (lives<LIFES)
			lives++;
	}
	public void speedUp(){
		spd++;
	}
	public int getSpd(){
		return spd;
	}
	public void upBomb(){
		numBomb++;
	}
	public void upPow(){
		power++;
	}
	public void setNormalMode(){
		spd = 1;
		power = 2;
		numBomb = 1;
		lives = 3;
		walkOver = false;
	}
	public void setArenaMode(){
		spd = 4;
		power = 10;
		numBomb = 4;
		lives = 3;
		walkOver = true;
	}
	public void dying(){
		if (!dead)
			lives--;
	}
	public int getLives(){
		return lives;
	}
	public boolean getInvul(){
		return invul;
	}
	public void invul(){
		invul = true;
		invulT.start();
	}
	public void died(){
		dead = true;
	}
	public boolean isDead(){
		return dead;
	}
	public void pMove(boolean[][] moving,ArrayList<Player> player,ImageIcon[] pCurrent,int i){
		if (player.get(i).dead) return;
		Rectangle temp;
		char[][] map = Panel.map;
		if (moving[i][R]){
			temp = new Rectangle((int)player.get(i).getX()+3,(int)player.get(i).getY(),25,30);
			if (!Collision.cWallsCrates(temp,Panel.wallsCrates)&&!Collision.cBombs(temp,i,player,Panel.bombs)){
				pCurrent[i] = player.get(i).move("right");
			}
			else{
				if(map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosY()>20)
					pCurrent[i] = player.get(i).move("up");
				else if (map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosY()<20)
					pCurrent[i] = player.get(i).move("down");
				else if (map[player.get(i).getCoordY()-1][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosY()<13)
					pCurrent[i] = player.get(i).move("up");
				else if (map[player.get(i).getCoordY()+1][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosY()>27)
					pCurrent[i] = player.get(i).move("down");
			}
		}
		if (moving[i][L]){ 
			temp = new Rectangle((int)player.get(i).getX()-3,(int)player.get(i).getY(),25,30);
			if (!Collision.cWallsCrates(temp,Panel.wallsCrates)&&!Collision.cBombs(temp,i,player,Panel.bombs)){
				pCurrent[i] = player.get(i).move("left");
			}
			else{
				if(map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosY()>20)
					pCurrent[i] = player.get(i).move("up");
				else if (map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosY()<20)
					pCurrent[i] = player.get(i).move("down");
				else if (map[player.get(i).getCoordY()-1][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosY()<13)
					pCurrent[i] = player.get(i).move("up");
				else if (map[player.get(i).getCoordY()+1][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosY()>27)
					pCurrent[i] = player.get(i).move("down");
			}
		}
		if (moving[i][U]){
			temp = new Rectangle((int)player.get(i).getX(),(int)player.get(i).getY()-3,25,30);
			if (!Collision.cWallsCrates(temp,Panel.wallsCrates)&&!Collision.cBombs(temp,i,player,Panel.bombs)){
				pCurrent[i] = player.get(i).move("up");
			}
			else{
				if(map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()]=='0'&&player.get(i).getGridPosX()>20)
					pCurrent[i] = player.get(i).move("left");
				else if (map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()]=='0'&&player.get(i).getGridPosX()<20)
					pCurrent[i] = player.get(i).move("right");
				else if (map[player.get(i).getCoordY()-1][player.get(i).getCoordX()-1]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosX()<13)
					pCurrent[i] = player.get(i).move("left");
				else if (map[player.get(i).getCoordY()-1][player.get(i).getCoordX()+1]=='0'&&map[player.get(i).getCoordY()-1][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosX()>27)
					pCurrent[i] = player.get(i).move("right");
			}
		}
		if (moving[i][D]){
			temp = new Rectangle((int)player.get(i).getX(),(int)player.get(i).getY()+3,25,30);
			if (!Collision.cWallsCrates(temp,Panel.wallsCrates)&&!Collision.cBombs(temp,i,player,Panel.bombs)){
				pCurrent[i] = player.get(i).move("down");
			}
			else{
				if(Panel.map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()]=='0'&&player.get(i).getGridPosX()>20)
					pCurrent[i] = player.get(i).move("left");
				else if (map[player.get(i).getCoordY()][player.get(i).getCoordX()]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()]=='0'&&player.get(i).getGridPosX()<20)
					pCurrent[i] = player.get(i).move("right");
				else if (map[player.get(i).getCoordY()+1][player.get(i).getCoordX()-1]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()-1]=='0'&&player.get(i).getGridPosX()<13)
					pCurrent[i] = player.get(i).move("left");
				else if (map[player.get(i).getCoordY()+1][player.get(i).getCoordX()+1]=='0'&&map[player.get(i).getCoordY()+1][player.get(i).getCoordX()+1]=='0'&&player.get(i).getGridPosX()>27)
					pCurrent[i] = player.get(i).move("right");
			}
		}
	}  	
	public void processObjective(ArrayList<Player> player,ImageIcon[] pCurrent,int i){

	}
	public boolean placeBomb(){
		return false;
	}
}