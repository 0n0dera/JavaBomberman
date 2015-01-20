import java.io.*;
public class MapLoader implements Constants{
	public char[][] getMap(int mode) throws IOException{
		BufferedReader r = new BufferedReader(new FileReader("map"+mode+".txt"));
		char[][] map = new char[SIZE][SIZE];
		char temp[];
		for (int i=0;i<SIZE;i++){
			temp = r.readLine().toCharArray();
			for (int j=0;j<SIZE;j++){
				map[i][j] = temp[j];
			}
		}
		return map;
	}
}
