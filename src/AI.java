import java.util.ArrayList;
 
import javax.swing.*;

public class AI extends Player{
	private boolean foundDestination;
	private int destX, destY;
	private char[][] map;
	private ArrayList<Integer> queueX, queueY;
	public AI(String p, int x, int y, String wD) {
		super(p, x, y, wD);
		foundDestination = false;
		numBomb = 1;
		queueX = new ArrayList<Integer>();
		queueY = new ArrayList<Integer>();
	}
	
	public void processObjective(ArrayList<Player> player,ImageIcon[] pCurrent,int i){
		//make map of bombMap from panel
		map = Panel.bombMap;
		//check if player is in a dangerous position and not already finding a safe spot
		if (inDanger()&&!foundDestination){
			//sets path to go to safe spot
			safeSpotExists(getCoordX(), getCoordY(), map, queueX, queueY);
		}
		//if player is not in a dangerous position, find random destination
		else if (!foundDestination){
			findRandDirection();
		}
		//walks to destination
		else{
			moveToDestination(player,pCurrent,i);
		}
	}
	
	public void findRandDirection(){
		int numDirections = 0;
		//checks number of possible directions to go
		if (map[getCoordY()][getCoordX()+1]=='0'){
			numDirections++;
		}
		if (map[getCoordY()][getCoordX()-1]=='0'){
			numDirections++;
		}
		if (map[getCoordY()-1][getCoordX()]=='0'){
			numDirections++;
		}
		if (map[getCoordY()+1][getCoordX()]=='0'){
			numDirections++;
		}
		//generates random number to indicate direction to walk in
		int randDirection = (int)(Math.random()*numDirections);
		int tempCounter = 0;
		//sets destination based on random number
		if (map[getCoordY()][getCoordX()+1]=='0'){
			if (randDirection==tempCounter){
				destX = getCoordX()+1;
				destY = getCoordY();
				foundDestination = true;
				return;
			}
			tempCounter++;
		}
		if (map[getCoordY()][getCoordX()-1]=='0'){
			if (randDirection==tempCounter){
				destX = getCoordX()-1;
				destY = getCoordY();
				foundDestination = true;
				return;
			}
			tempCounter++;
		}
		if (map[getCoordY()-1][getCoordX()]=='0'){
			if (randDirection==tempCounter){
				destX = getCoordX();
				destY = getCoordY()-1;
				foundDestination = true;
				return;
			}
			tempCounter++;
		}
		if (map[getCoordY()+1][getCoordX()]=='0'){
			if (randDirection==tempCounter){
				destX = getCoordX();
				destY = getCoordY()+1;
				foundDestination = true;
				return;
			}
			tempCounter++;
		}
	}
	
	public void moveToDestination(ArrayList<Player> player, ImageIcon[] pCurrent, int i){
		boolean[][] moving = new boolean[4][4];
		//moves player to destination if it is not already there
		if (destX>getCoordX()||(destX==getCoordX()&&destY==getCoordY()&&getGridPosX()<17)){
			moving[i][R]=true;
			pMove(moving, player, pCurrent, i);
		}
		else if (destX<getCoordX()||(destX==getCoordX()&&destY==getCoordY()&&getGridPosX()>23)){
			moving[i][L]=true;
			pMove(moving, player, pCurrent, i);
		}
		else if (destY>getCoordY()|(destX==getCoordX()&&destY==getCoordY()&&getGridPosY()<17)){
			moving[i][D]=true;
			pMove(moving, player, pCurrent, i);
		}
		else if (destY<getCoordY()||(destX==getCoordX()&&destY==getCoordY()&&getGridPosY()>23)){
			moving[i][U]=true;
			pMove(moving, player, pCurrent, i);
		}
		else{
			//if player has reached destination, prepare to find a new destination next iteration
			foundDestination = false;
		}
	}
	
	public boolean inDanger(){
		//returns true if the player sprite is in any danger
		if (map[getCoordY()][getCoordX()]!='0'||(getGridPosX()>23&&(map[getCoordY()][getCoordX()+1]=='s'||map[getCoordY()][getCoordX()+1]=='b'))||(getGridPosX()<17&&(map[getCoordY()][getCoordX()-1]=='s'||map[getCoordY()][getCoordX()-1]=='b'))||(getGridPosY()>23&&(map[getCoordY()+1][getCoordX()+1]=='s'||map[getCoordY()+1][getCoordX()+1]=='b'))||(getGridPosY()<17&&(map[getCoordY()-1][getCoordX()]=='s'||map[getCoordY()-1][getCoordX()]=='b')))
			return true;
		return false;
	}
	
	public void safeSpotExists(int currX, int currY, char[][] tempMap, ArrayList<Integer> qX, ArrayList<Integer> qY){
		//base case for if the safe spot was found
		if (tempMap[currY][currX]=='0'){
			foundDestination = true;
			destX = qX.get(0);
			destY = qY.get(0);
			return;
		}
		//checks 4 surrounding grids to check if player can walk there
		if (tempMap[currY][currX+1]=='s'||tempMap[currY][currX+1]=='0'){
			//arraylists made to store current path created for player
			ArrayList<Integer> tempX = new ArrayList<Integer>();
			ArrayList<Integer> tempY = new ArrayList<Integer>();
			for (int i=0;i<qX.size();i++){
				tempX.add(qX.get(i));
				tempY.add(qY.get(i));
			}
			tempX.add(currX+1);
			tempY.add(currY);
			//checkedmap made so player doesnt retrace steps to safe spot
			char[][] checkedMapA = new char[SIZE][SIZE];
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++)
					checkedMapA[i][j]=map[i][j];
			}
			checkedMapA[currY][currX]='c';
			//recursive call
			safeSpotExists(currX+1, currY, checkedMapA, tempX, tempY);
		}
		if (tempMap[currY][currX-1]=='s'||tempMap[currY][currX-1]=='0'){
			ArrayList<Integer> tempX = new ArrayList<Integer>();
			ArrayList<Integer> tempY = new ArrayList<Integer>();
			for (int i=0;i<qX.size();i++){
				tempX.add(qX.get(i));
				tempY.add(qY.get(i));
			}
			tempX.add(currX-1);
			tempY.add(currY);
			char[][] checkedMapB = new char[SIZE][SIZE];
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++)
					checkedMapB[i][j]=map[i][j];
			}
			checkedMapB[currY][currX]='c';
			safeSpotExists(currX-1, currY, checkedMapB, tempX, tempY);
		}
		if (tempMap[currY-1][currX]=='s'||tempMap[currY-1][currX]=='0'){
			ArrayList<Integer> tempX = new ArrayList<Integer>();
			ArrayList<Integer> tempY = new ArrayList<Integer>();
			for (int i=0;i<qX.size();i++){
				tempX.add(qX.get(i));
				tempY.add(qY.get(i));
			}
			tempX.add(currX);
			tempY.add(currY-1);
			char[][] checkedMapC = new char[SIZE][SIZE];
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++)
					checkedMapC[i][j]=map[i][j];
			}
			checkedMapC[currY][currX]='c';
			safeSpotExists(currX, currY-1, checkedMapC, tempX, tempY);
		}
		if (tempMap[currY+1][currX]=='s'||tempMap[currY+1][currX]=='0'){
			ArrayList<Integer> tempX = new ArrayList<Integer>();
			ArrayList<Integer> tempY = new ArrayList<Integer>();
			for (int i=0;i<qX.size();i++){
				tempX.add(qX.get(i));
				tempY.add(qY.get(i));
			}
			tempX.add(currX);
			tempY.add(currY+1);
			char[][] checkedMapD = new char[SIZE][SIZE];
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++)
					checkedMapD[i][j]=map[i][j];
			}
			checkedMapD[currY][currX]='c';
			safeSpotExists(currX, currY+1, checkedMapD, tempX, tempY);
		}
	}
	
	//place bombs sometimes
	public boolean placeBomb(){
		int temp = (int)(Math.random()*400);
		if (temp==1){
			return true;
		}
		return false;
	}
}
