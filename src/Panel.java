import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

class Panel extends JPanel implements ActionListener, KeyListener, MouseListener,Constants{
	static char[][] map;
	static char[][] bombMap;
	static boolean quit,pause,gameover;
	javax.swing.Timer time;
	private int winner;
	static boolean pQuit;
	static ArrayList<Player> player;
	static ArrayList<Rectangle> wallsCrates;
	static ArrayList<Bomb> bombs;
	private ArrayList<Boom> booms;
	private Graphics2D g2;
	private ArrayList<Powerup> pUps;
	private ImageIcon[] pCurrent;
	private boolean[][] moving;
	public Panel(){
		super();
		addKeyListener(this);
		quit = true;
		pause = false;
		gameover = false;
		player = new ArrayList<Player>();
		moving = new boolean[4][4];
		bombs = new ArrayList<Bomb>();
		booms = new ArrayList<Boom>();
		pUps = new ArrayList<Powerup>();
		wallsCrates = new ArrayList<Rectangle>();
		resetMap(1);
		time = new javax.swing.Timer(10,this);
		time.start();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g2 = (Graphics2D)g;
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				g.drawImage(ICONS[Integer.parseInt(""+map[i][j])].getImage(), j*40,i*40, null);
			}
		}
		drawSide(g);
		for (Powerup p:pUps)
			g.drawImage(p.getP().getImage(),p.x,p.y,null);
		for (Bomb bomb:bombs)
			g.drawImage(bomb.bombFuse().getImage(),bomb.x,bomb.y,null);
		for (Boom boom:booms)
			g.drawImage(boom.getBoom().getImage(),boom.x,boom.y,null);
		for (int i=0;i<player.size();i++){
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			if (player.get(i).getInvul())
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			if (!player.get(i).isDead())
				g.drawImage(pCurrent[i].getImage(),(int)player.get(i).x,(int)player.get(i).y,null);
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (gameover){
			for (int i=0;i<player.size();i++){
				if (!player.get(i).isDead())
					winner =i+1;
			}
			g.drawImage(WIN[winner].getImage(),5,150,null);
			g.drawImage(GIRLS[getGirl()].getImage(),550,180,null);
			time.stop();
		}
	}

	public void actionPerformed(ActionEvent e){
		if (pause)
			time.stop();
		win();

		if (e.getSource()==time&&!quit&&!Frame.firstTime){
			drawBoom();
			checkBoom();
			for (int i=0;i<player.size();i++){
				if (i<2)
					player.get(i).pMove(moving,player,pCurrent,i);
				else if (i<=3)
					player.get(i).processObjective(player,pCurrent,i);
				Collision.cPUps(i,player,pUps);
				Collision.cBoom(i,player,booms);
				if (!player.get(i).isDead()&&player.get(i).getLives()==0){
					player.get(i).died();
					playSound(2);
				}
			}
			//when more than 2 players, players 3 and 4 will place bombs based on AI class
			if (player.size()>2){
				for (int i=2;i<4;i++){
					if (player.get(i).placeBomb()&&!player.get(i).isDead()&&player.get(i).totalBomb()>0&&bombMap[player.get(i).getCoordY()][player.get(i).getCoordX()]!='b'){
						Bomb b = new Bomb((int)player.get(i).getCoordX()*40+6,(int)player.get(i).getCoordY()*40+5,i);
						bombs.add(b);
						setBooms(player.get(i).getCoordX(),player.get(i).getCoordY(),i);
						bombMap[player.get(i).getCoordY()][player.get(i).getCoordX()]='b';
						player.get(i).putBomb();
					}
				}
			}
			repaint();
		}
	}

	public void keyTyped(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
		for (int i=0;i<2;i++)
			pStop(e,i);
	}
	public void keyPressed(KeyEvent e){
		for (int i=0;i<2;i++){
			pMove(e,i);
			pBomb(e);
		}
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
			quit = true;
			time.stop();
		}
		if (e.getKeyCode()==KeyEvent.VK_P){
			pause = !pause;
			if (!pause)
				time.start();
		}
		if (e.getKeyCode() == KeyEvent.VK_M){
			if (player.size()==2&&!Frame.arena.isActive())
				Frame.musicArena();
			else if (player.size()==4&&!Frame.main.isActive())
				Frame.musicMain();
			else
				Frame.stop();
		}
		if (gameover){
			if (e.getKeyCode()==KeyEvent.VK_SPACE){
				quit = true;
				time.stop();
			}
		}

	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public boolean onlyOne(int player,int direction){
		for (int i=0;i<4;i++){
			if (moving[player][i]==true&&i!=direction)
				return false;
		}
		return true;
	}

	public void pMove(KeyEvent e, int p){
		if (p==0){
			if (e.getKeyCode()==KeyEvent.VK_RIGHT&&onlyOne(p,R)){
				moving[p][R] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_LEFT&&onlyOne(p,L)){
				moving[p][L] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_UP&&onlyOne(p,U)){
				moving[p][U] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_DOWN&&onlyOne(p,D)){
				moving[p][D] = true;
			}
		}
		else if (p==1){
			if (e.getKeyCode()==KeyEvent.VK_D&&onlyOne(p,R)){
				moving[p][R] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_A&&onlyOne(p,L)){
				moving[p][L] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_W&&onlyOne(p,U)){
				moving[p][U] = true;
			}
			if (e.getKeyCode()==KeyEvent.VK_S&&onlyOne(p,D)){
				moving[p][D] = true;
			}
		}
	}
	public void pStop(KeyEvent e,int p){
		if (p==0){
			if (e.getKeyCode()==KeyEvent.VK_RIGHT){
				moving[0][R] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_LEFT){
				moving[0][L] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_UP){
				moving[0][U] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_DOWN){
				moving[0][D] = false;
			}
		}
		if (p==1){
			if (e.getKeyCode()==KeyEvent.VK_D){
				moving[1][R] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_A){
				moving[1][L] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_W){
				moving[1][U] = false;
			}
			if (e.getKeyCode()==KeyEvent.VK_S){
				moving[1][D] = false;
			}
		}
	}
	public void pBomb(KeyEvent e){
		for (int i=0;i<player.size();i++){
			if (!player.get(i).isDead()&&((e.getKeyCode()==KeyEvent.VK_CONTROL&&i==0)||(e.getKeyCode()==KeyEvent.VK_E&&i==1))&& player.get(i).totalBomb()>0&&bombMap[player.get(i).getCoordY()][player.get(i).getCoordX()]!='b'){
				Bomb b = new Bomb((int)player.get(i).getCoordX()*40+6,(int)player.get(i).getCoordY()*40+5,i);
				bombs.add(b);
				bombMap[player.get(i).getCoordY()][player.get(i).getCoordX()]='b';
				setBooms(player.get(i).getCoordX(),player.get(i).getCoordY(),i);
				player.get(i).putBomb();
			}
		}
	}
	public void setBooms(int x, int y,int p){
		boolean left=true,right=true,up=true,down=true;
		for (int i=1;i<=player.get(p).getPow();i++){			
			if (left&&map[y][x-i]!='0')
				left = false;
			if (right&&map[y][x+i]!='0')
				right = false;
			if (up&&map[y-i][x]!='0')
				up = false;
			if (down&&map[y+i][x]!='0')
				down = false;
			if (left)
				bombMap[y][x-i]='s';
			if (right)
				bombMap[y][x+i]='s';
			if (up)
				bombMap[y-i][x]='s';
			if (down)
				bombMap[y+i][x]='s';
		}
	}
	private void drawBoom(){
		for (int i=0;i<bombs.size();i++){
			Bomb b = bombs.get(i);
			if (b.getM()==3){
				playSound(0);
				for (int j=1;j<=player.get(b.getP()).getPow();j++){
					//left
					if (map[b.y/40][b.x/40-j]!='1'){
						booms.add(new Boom(b.x-40*j,b.y));
						bombMap[b.y/40][(b.x-40*j)/40] = 'n';
						if (map[b.y/40][b.x/40-j]=='2'){
							map[b.y/40][b.x/40-j]='0';
							wallsCrates.remove(findCrate(b.x-40*j,b.y));
							if ((int)(Math.random()*4) == 0)
								pUps.add(new Powerup(b.x-40*j,b.y,getPUp()));
							break;
						}
					}
					else break;
				}
				//right
				for (int j=1;j<=player.get(b.getP()).getPow();j++){
					if (map[b.y/40][b.x/40+j]!='1'){
						booms.add(new Boom(b.x+40*j,b.y));
						bombMap[b.y/40][(b.x+40*j)/40] = 'n';
						if (map[b.y/40][b.x/40+j]=='2'){
							map[b.y/40][b.x/40+j]='0';
							wallsCrates.remove(findCrate(b.x+40*j,b.y));
							if ((int)(Math.random()*4) == 0)
								pUps.add(new Powerup(b.x+40*j,b.y,getPUp()));
							break;
						}
					}
					else break;
				}
				for (int j=1;j<=player.get(b.getP()).getPow();j++){
					//up
					if (map[b.y/40-j][b.x/40]!='1'){
						booms.add(new Boom(b.x,b.y-40*j));
						bombMap[(b.y-40*j)/40][b.x/40] = 'n';
						if (map[b.y/40-j][b.x/40]=='2'){
							map[b.y/40-j][b.x/40]='0';
							wallsCrates.remove(findCrate(b.x,b.y-40*j));
							if ((int)(Math.random()*4)== 0)
								pUps.add(new Powerup(b.x,b.y-40*j,getPUp()));
							break;
						}
					}
					else break;	
				}
				for (int j=1;j<=player.get(b.getP()).getPow();j++){
					//down
					if (map[b.y/40+j][b.x/40]!='1'){
						booms.add(new Boom(b.x,b.y+40*j));
						bombMap[(b.y+40*j)/40][b.x/40] = 'n';
						if (map[b.y/40+j][b.x/40]=='2'){
							map[b.y/40+j][b.x/40]='0';
							wallsCrates.remove(findCrate(b.x,b.y+40*j));
							if ((int)(Math.random()*4) == 0)
								pUps.add(new Powerup(b.x,b.y+40*j,getPUp()));
							break;
						}
					}
					else break;	
				}
				booms.add(new Boom(b.x,b.y));
				bombMap[bombs.get(i).y/40][bombs.get(i).x/40]='0';	
				bombs.remove(i);
				player.get(b.getP()).upBomb();
			}
		}
	}

	private int findCrate(int x, int y){
		for (int i=0;i<wallsCrates.size();i++){
			if (wallsCrates.get(i).x == x/40*40 && wallsCrates.get(i).y==y/40*40)
				return i;
		}
		return 0;
	}
	private void checkBoom(){
		for (int i=0;i<booms.size();i++)
			if (booms.get(i).getC()==1){
				bombMap[booms.get(i).y/40][booms.get(i).x/40] = '0';
				booms.remove(i);
			}
	}
	private int getPUp(){		
		int a= (int)(Math.random()*4.2+0.8);
		return a;
	}
	public void resetMap(int mode){
		player.clear();
		switch(mode){
		case 1:
			player.add(new Player("p1",45,45,"r"));
			player.add(new Player("p2",607,45,"l"));
			player.add(new AI("p3",45,605,"r"));
			player.add(new AI("p4",610,605,"l"));
			for (int i=0;i<player.size();i++){
				player.get(i).setNormalMode();
			}
			break;
		case 2:
			player.add(new Player("p1",45,45,"r"));
			player.add(new Player("p2",610,605,"l"));
			for (int i=0;i<player.size();i++){
				player.get(i).setArenaMode();
			}
			break;
		}
		try {
			map = new MapLoader().getMap(mode);
			bombMap = new MapLoader().getMap(mode);
		} catch (IOException e) {
			System.out.println("No map!");
		}
		pCurrent = new ImageIcon[4];
		for (int i=0;i<player.size();i++)
			pCurrent[i] = player.get(i).move("stop");
		wallsCrates.clear();
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				if (map[i][j]=='1')
					wallsCrates.add(new Wall(j*40,i*40));
				else if (map[i][j] == '2')
					wallsCrates.add(new Crate(j*40,i*40));
			}
		}
		pUps.clear();
		bombs.clear();
		booms.clear();
	}

	public boolean getQuit(){
		return quit;
	}

	public void resetQuit() {
		quit = false;
	}
	public void drawSide(Graphics g){
		g.drawImage(SIDE.getImage(),680,0,null);
		g.drawImage(LIFE.getImage(),690,20,null);
		for (int i=0;i<player.size();i++){
			g.drawImage(LIVES[4+i].getImage(),700,50+i*150,null);
			g.drawImage(LIVES[player.get(i).getLives()].getImage(),693,95+i*150,null);
		}
	}
	public void win(){
		int numDead=0;
		for (Player p:player){
			if (p.isDead())
				numDead++;
		}
		if ((player.size()==2 && numDead==1)||(player.size()==4&&numDead==3)||numDead == player.size()){
			gameover = true;
		}
	}
	public int getGirl(){
		return (int)(Math.random()*4);
	}
	public static void playSound(int a){
		try{
			// Open an audio input stream.
			File f = SOUNDS[a];
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		}
		catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
			System.out.println("No file!");
		}
	}
}