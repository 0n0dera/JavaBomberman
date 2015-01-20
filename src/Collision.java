import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Collision implements Constants{
	static Rectangle boomRec;
	public static boolean cWallsCrates(Rectangle r,ArrayList<Rectangle> wC){
		for (Rectangle w:wC){
			if (r.intersects(w))
				return true;
		}
		return false;
	}
	
	public static boolean cBombs(Rectangle r,int p,ArrayList<Player> player,ArrayList<Bomb> bombs){
		for (Bomb b:bombs){
			if (player.get(p).intersects(b)){
				return false;
			}
			else{
				if (r.intersects(b)&&!player.get(p).getWO()){
					return true;
				}
			}
		}
		return false;
	}
	public static void cPUps(int p, ArrayList<Player> player,ArrayList<Powerup> pUps){
		for (int i=0;i<pUps.size();i++){
			if(pUps.get(i).contains(new Point(player.get(p).getmidX(),player.get(p).getmidY()))){
				String a =pUps.get(i).effect();
				Panel.playSound(1);
				if (a.equals("life"))
					player.get(p).lifeUp();
				else if (a.equals("plus"))
					player.get(p).upBomb();
				else if (a.equals("fast")){
					if (player.get(p).getSpd()<MAX_SPEED)
						player.get(p).speedUp();
				}
				else if (a.equals("walk"))
					player.get(p).setWO();
				else
					player.get(p).upPow();
				pUps.remove(pUps.get(i));
			}    			
		}
	}
	public static void cBoom(int p, ArrayList<Player> player, ArrayList<Boom> booms){
		for (int i=0;i<booms.size();i++){
			boomRec = new Rectangle(player.get(p).x+5,player.get(p).y+2,14,23);
			if (!player.get(p).getInvul()&&boomRec.intersects(booms.get(i))){
				player.get(p).dying();
				player.get(p).invul();
			}
		}
	}
}