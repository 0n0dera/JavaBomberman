import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import java.util.*;

public class MapPanel extends JPanel implements ActionListener,MouseListener,Constants{
	boolean quit = false;
	int x,y;
	ImageIcon[] icons;
	ImageIcon guy = new ImageIcon("p1.gif");
	char[][] grid;
	int color;
	boolean save,load;
	JFileChooser fc;
	File def,savedFile,openedFile;
	int whichIcon;
	public MapPanel(){
		super();
		setLayout(new BorderLayout());
		icons = new ImageIcon[]{new ImageIcon("tile.gif"),new ImageIcon("wall.gif"),new ImageIcon("box.gif")};
		grid = new char[SIZE][SIZE];
		whichIcon = 0;
		save = false;
		load = true;
		def = new File("map1.txt");
		savedFile = null;
		openedFile = null;
		fc = new JFileChooser();
		addMouseListener(this);
		resetGrid();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				g.drawImage(icons[Integer.parseInt(""+grid[i][j])].getImage(),j*40,i*40,null);
			}
				
		}

		g.setColor(Color.BLACK);
		for (int i=1;i<SIZE;i++)
			g.drawLine(40*i, 0, 40*i, WIN_H-16);
		for (int i=1;i<SIZE;i++)
			g.drawLine(0,40*i,WIN_W,40*i);
		g.drawImage(guy.getImage(),0,0,null);

	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		save = false;
		x = e.getX()/40;
		y = e.getY()/40;
		if (!(x==1&&y==1)&&!(x==1&&y==SIZE-2)&&!(x==SIZE-2&&y==1)&&!(x==SIZE-2&&y==SIZE-2)){
			if (e.isMetaDown())
				grid[y][x] = '0';
			else{
				grid[y][x] = (""+whichIcon%3+1).charAt(0);
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String a = b.getText();
		b.setFocusPainted(false);
		if (a.equals("Floor")||a.equals("Wall")||a.equals("Box")){
			whichIcon++;
			if (whichIcon%3+1 == 1)
				b.setText("Floor");
			else if (whichIcon%3+1 == 2)
				b.setText("Wall");
			else if (whichIcon%3+1 == 3)
				b.setText("Box");
		}
		else if (a.equals("Reset")){
			resetGrid();
		}
		else if (a.equals("Save"))
			save();
		else if (a.equals("Load"))
			open();
		else if (a.equals("Exit")){
			if (save)
				quit=true;
			else{
				int n = JOptionPane.showConfirmDialog(null,"Do you want to save?","Save before you exit~",JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					save();
				}
				quit=true;
			}			
		}					
		repaint();
	}
	public void open(){
		char[] temp;
		int rVal =  fc.showOpenDialog(this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			savedFile= fc.getSelectedFile();
			BufferedReader r = null;
			try{
				r = new BufferedReader(new FileReader(savedFile));
				for (int i=0;i<SIZE;i++){
					temp = r.readLine().toCharArray();
					for (int j=0;j<SIZE;j++){
						grid[i][j] = temp[j];
					}
				}
			}catch(Exception ae){
				System.out.println(ae);
			}
			finally{
				try{
					r.close();
				}catch(Exception a1){JOptionPane.showMessageDialog(this,"That file does not exist.");}
			}
		}
	}
	public void save(){
		fc.setSelectedFile(def);
		int rVal =  fc.showSaveDialog(this);
		if (rVal == JFileChooser.APPROVE_OPTION){ 
			savedFile= fc.getSelectedFile();
			BufferedWriter w = null;
			try{
				w = new BufferedWriter(new FileWriter(savedFile));
				for (int i=0;i<SIZE;i++){
					for (int j=0;j<SIZE;j++){
						w.write(grid[i][j]);
					}
					if (i!=SIZE-1)
						w.write("\r\n");
					
				}
			}catch(IOException ae){}
			finally{
				try {
					w.close();
				} catch (IOException e1) {}
			}
			save = true;
		}
	}
	public boolean getQuit(){
		return quit;
	}
	public void resetGrid(){
		for (int i=0;i<SIZE;i++){
			for (int j=0;j<SIZE;j++){
				if (i==0||j==0||j==SIZE-1||i==SIZE-1)
					grid[i][j] = '1';
				else
					grid[i][j] = '0';
			}
		}
	}
	public void reset(){
		quit = false;
		resetGrid();
	}
}
