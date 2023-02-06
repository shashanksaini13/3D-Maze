import java.util.Random;

public class KruskalsAlgorithm {
	private int size;
	private int[][][] maze;
	private Room[][][] mazerooms;
	public KruskalsAlgorithm(int size) {
		this.size=size;
		reset();
	}

	public Room[][][] generateMaze () {
		Random rand=new Random();
		do {
			int x=rand.nextInt(size);
			int y=rand.nextInt(size);
			int z=rand.nextInt(size);
			int next=rand.nextInt(6);
			if (next==0&&x!=0&&maze[x][y][z]!=maze[x-1][y][z]) {
				update(maze[x][y][z],maze[x-1][y][z]);
				mazerooms[x][y][z].setDoor(Room.west, true);
				mazerooms[x-1][y][z].setDoor(Room.east, true);
			} else if (next==1&&y!=size-1&&maze[x][y][z]!=maze[x][y+1][z]) {
				update(maze[x][y][z],maze[x][y+1][z]);
				mazerooms[x][y][z].setDoor(Room.north, true);
				mazerooms[x][y+1][z].setDoor(Room.south, true);
			} else if (next==2&&z!=0&&maze[x][y][z]!=maze[x][y][z-1]) {
				update(maze[x][y][z],maze[x][y][z-1]);
				mazerooms[x][y][z].setDoor(Room.down, true);
				mazerooms[x][y][z-1].setDoor(Room.up, true);
			} else if (next==3&&x!=size-1&&maze[x][y][z]!=maze[x+1][y][z]) {
				update(maze[x][y][z],maze[x+1][y][z]);
				mazerooms[x][y][z].setDoor(Room.east, true);
				mazerooms[x+1][y][z].setDoor(Room.west, true);
			} else if (next==4&&y!=0&&maze[x][y][z]!=maze[x][y-1][z]) {
				update(maze[x][y][z],maze[x][y-1][z]);
				mazerooms[x][y][z].setDoor(Room.south, true);
				mazerooms[x][y-1][z].setDoor(Room.north, true);
			} else if (next==5&&z!=size-1&&maze[x][y][z]!=maze[x][y][z+1]) {
				update(maze[x][y][z],maze[x][y][z+1]);
				mazerooms[x][y][z].setDoor(Room.up, true);
				mazerooms[x][y][z+1].setDoor(Room.down, true);
			}
		} while (!done());
		if (Maze.shortestPath(mazerooms)<25) {
			reset();
			return generateMaze();
		}
		return mazerooms;
	}

	private void reset() {
		maze=new int[size][size][size];
		mazerooms=new Room[size][size][size];
		int count=0;
		for (int k=0;k<size;k++) {
			for (int j=0;j<size;j++) {
				for (int i=0;i<size;i++) {
					maze[i][j][k]=count;
					count++;
					mazerooms[i][j][k]=new Room();
				}
			}
		}
	}

	private void update(int keep, int change) {
		for (int i=0;i<size;i++) {
			for (int j=0;j<size;j++) {
				for (int k=0;k<size;k++) {
					if (maze[i][j][k]==change) {
						maze[i][j][k]=keep;
					}
				}
			}
		}
	}

	private boolean done() {
		for (int i=0;i<size;i++) {
			for (int j=0;j<size;j++) {
				for (int k=0;k<size;k++) {
					if (maze[i][j][k]!=maze[0][0][0]) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
