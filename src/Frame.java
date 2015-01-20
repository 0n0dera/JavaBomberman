import javax.swing.*; 

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import java.awt.* ;
import java.awt.event.*;

public class Frame extends JFrame implements Constants{
	// declare variables
	static Container c;
	static Panel display;
	static JPanel menuPanel;
	JButton mute, pause,quit;
	JPanel mapButtonPanel;
	static Clip main,arena;
	static MapPanel mapPanel;
	static AudioInputStream audioIn;
	static boolean instr,firstTime;
	JButton maingame, arenaB, instructions, mapmaker,bType,exit,save,reset,load;
	ImageIcon image,image2;
	static JLabel menuImage, instruction;

	public Frame(){
		super("Bomberman");
		// initialize panels,buttons,labels, adding actionlisteners, setting layouts, and adding to the frame's container
		mapPanel = new MapPanel();
		mapButtonPanel = new JPanel();
		display = new Panel();
		firstTime = true;
		menuPanel = new JPanel();
		audioIn = null;
		menuPanel.setLayout(new GridLayout(5,5,3,3));
		menuPanel.setBackground(Color.BLACK);
		c = getContentPane();
		display.setLayout(new BorderLayout());
		c.setLayout(new BorderLayout());
		instr = false;
		image = new ImageIcon("menu.png");
		image2 = new ImageIcon("instructions.gif");
		menuImage = new JLabel(image);
		instruction = new JLabel(image2);
		c.add(menuImage,BorderLayout.CENTER);
		maingame = new JButton("Play");
		arenaB = new JButton("Arena");
		instructions = new JButton("Instructions");
		mapmaker = new JButton("Map Maker");
		pause = new JButton("Pause");
		mute = new JButton("Mute");
		quit = new JButton("Quit");
		bType = new JButton("Floor");
		load = new JButton("Load");
		exit = new JButton("Exit");
		save = new JButton("Save");
		reset = new JButton("Reset");
		bType.addActionListener(mapPanel);
		load.addActionListener(mapPanel);
		exit.addActionListener(mapPanel);
		save.addActionListener(mapPanel);
		reset.addActionListener(mapPanel);
		mute.addActionListener(display);
		quit.addActionListener(display);
		pause.addActionListener(display);
		bType.setBackground(new Color(255,182,193));
		bType.setFocusPainted(false);
		mapButtonPanel.setLayout(new GridLayout(1,4));
		mapButtonPanel.add(bType);
		mapButtonPanel.add(reset);
		mapButtonPanel.add(save);
		mapButtonPanel.add(load);
		mapButtonPanel.add(exit);
		setMenuPanel();
		c.add(menuPanel,BorderLayout.SOUTH);
		c.setBackground(Color.BLACK);
		addKeyListener(display);
		menuPanel.addKeyListener(display);
		addMouseListener(display);
		// if the main game "Play" button is clicked...
		maingame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent firstEventName) {
				// remove the menu GUI
				c.remove(menuPanel);
				c.remove(menuImage);
				// refer to setMainGame method
				setMainGame("Bomberman", 1);
				c.validate();
				// set some booleans so that the game starts to run
				Panel.gameover = false;
				firstTime = false;
				Panel.quit = false;
				// start music
				musicMain();
			}
		}
				);
		// if the arena button is clicked
		arenaB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent firstEventName) {
				c.remove(menuPanel);
				c.remove(menuImage);
				setMainGame("Bomberman", 2);
				c.validate();
				firstTime = false;
				Panel.gameover = false;
				Panel.quit = false;
				musicArena();
			}
		}
				);
		// if the instructions button is clicked
		instructions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent firstEventName) {
				if (!instr){
					instr = !instr;
					c.remove(menuImage);
					// add the instructions
					c.add(instruction);
				}
				else{
					instr = !instr;
					c.remove(instruction);
					c.add(menuImage);
				}
				c.validate();
				c.repaint();
			}
		}
				);
		// if the mapmaker is clicked
		mapmaker.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent firstEventName) {
				c.remove(menuPanel);
				c.remove(menuImage);
				c.remove(instruction);
				setMapMaker();
				c.validate();
			}
		}
				);
	}

	// set up all the menu panel GUI
	public void setMenuPanel(){
		menuPanel.add(new JLabel(""));
		menuPanel.add(maingame);
		menuPanel.add(new JLabel(""));
		menuPanel.add(new JLabel(""));
		menuPanel.add(arenaB);
		menuPanel.add(new JLabel(""));
		menuPanel.add(new JLabel(""));
		menuPanel.add(instructions);
		menuPanel.add(new JLabel(""));
		menuPanel.add(new JLabel(""));
		menuPanel.add(mapmaker);
		menuPanel.add(new JLabel(""));
	}
	// prepare the main game by resetting maps, arrays of objects, and starting the game's timer
	public void setMainGame(String title, int mode){
		setTitle(title);
		addKeyListener(display);
		display.resetMap(mode);
		menuImage.addKeyListener(display);
		c.setLayout(new BorderLayout());
		c.add(display, BorderLayout.CENTER);
		addKeyListener(display);
		c.validate();
		repaint();
		setSize(WIN_W, WIN_H); 
		display.requestFocus();
		display.time.start();
	}
	// preparing the mapmaker
	public void setMapMaker(){
		setTitle("Map Maker");
		c.add(mapPanel);
		c.add(mapButtonPanel,BorderLayout.SOUTH);
		repaint();
		setSize(WIN_W-70, WIN_H+26);
		mapPanel.requestFocus();
	}

	public static void main (String[] args){
		Frame a = new Frame();
		a.setSize(800, 700);     
		a.setVisible(true); 
		a.setResizable(false);
		a.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		boolean reset = false;
		while (true){
			if (!mapPanel.getQuit()&&!display.getQuit()){
				reset = false;
			}
			// reset menu GUI if mappanel quit
			else if (mapPanel.getQuit()){
				if (!reset){
					c.removeAll();
					c.add(menuImage,BorderLayout.CENTER);
					c.add(menuPanel,BorderLayout.SOUTH);
					c.validate();
					c.repaint();
					mapPanel.reset();
					a.setSize(800, 700); 
					instr = false;
					reset = true;
				}
			}
			// if esc is pressed while in game mode, go back to menu screen, and reset maps
			else if (display.getQuit()){
				if (!reset){
					c.removeAll();
					c.add(menuImage,BorderLayout.CENTER);
					c.add(menuPanel,BorderLayout.SOUTH);
					c.validate();
					c.repaint();
					display.resetMap(1);
					display.resetMap(2);
					display.resetQuit();
					a.setSize(800, 700); 
					instr = false;
					reset = true;
					stop();
				}
			}
		}
	}
	// playing BG music for main game
	public static void musicMain(){
		try{
			// open an audio input stream
			File f = new File("sounds/main.wav");
			audioIn = AudioSystem.getAudioInputStream(f);
			// get a sound clip
			main = AudioSystem.getClip();
			// open audio clip
			main.open(audioIn);
			main.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
			System.out.println("No file!");
		}
	}
	// music for the arena (similar comments as above)
	public static void musicArena(){
		try{
			File f = new File("sounds/arena.wav");
			audioIn = AudioSystem.getAudioInputStream(f);
			arena = AudioSystem.getClip();
			arena.open(audioIn);
			arena.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
			System.out.println("No file!");
		}
	}
	// stop music
	public static void stop(){
		if (main!=null)
			main.stop();
		if (arena!=null)
			arena.stop();
	}
}
