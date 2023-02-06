import java.util.*;
// NOTE: starting at (n-1, n-1, n-1), ending at (0, 0, 0)
// NOTE: x WE, y SN, z BT
public class Maze {
	private Room[][][] mazeArray;
	private int size;
	private int difficulty;
	public int getSize () {
		return size;
	}
	public int getDifficulty () {
		return difficulty;
	}
	
	public Maze (int difficulty) {
		this.difficulty = difficulty;
		
		if (difficulty == 0) { // Hunt-and-Kill Algorithm
			mazeArray = (new HuntKill(4)).generateMaze();
			size=4;
		} else 
		if (difficulty == 1) { // Prim's Algorithm
			mazeArray = (new PrimsAlgorithm(5)).generateMaze();
			size = 5;
		} else if (difficulty == 2) { // Kruskal's Algorithm
			mazeArray = (new KruskalsAlgorithm(6)).generateMaze();
			size = 6;
		} else {
			mazeArray = new Room[5][5][5];
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++)
					for (int k = 0; k < 5; k++) {
						boolean[] doors = {true, true, true, true, true, true};
						boolean[] outside = new boolean[6];
						mazeArray[i][j][k] = new Room(doors, outside);
					}
			size = 5;
		}
		exit();
		addPaintings();
	}
	
	public Maze (int difficulty, int size) {
		this.difficulty = difficulty;
		this.size=size;
		if (difficulty == 0) { // Hunt-and-Kill Algorithm
			mazeArray = (new HuntKill(size)).generateMaze();
		} else 
		if (difficulty == 1) { // Prim's Algorithm
			mazeArray = (new PrimsAlgorithm(size)).generateMaze();
		} else if (difficulty == 2) { // Kruskal's Algorithm
			mazeArray = (new KruskalsAlgorithm(size)).generateMaze();
		} else {
			mazeArray = new Room[5][5][5];
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++)
					for (int k = 0; k < 5; k++) {
						boolean[] doors = {true, true, true, true, true, true};
						boolean[] outside = new boolean[6];
						mazeArray[i][j][k] = new Room(doors, outside);
					}
			size = 5;
		}
		exit();
		addPaintings();
	}

	private void addPaintings () {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				for (int k = 0; k < size; k++)
					if (!(i==0 && j==0 && k==0)) {
						double random = Math.random();
						if (random < 1) {
							int randomDoor = (int) (4*Math.random());
							if (!mazeArray[i][j][k].getDoor(randomDoor)) {
								mazeArray[i][j][k].setPainting((new Painting()).specifyPainting(randomDoor, new Vector(100*i, 100*j, 100*k), true));
							}
						}
					}
	}
	
	public Maze(Room[][][] rooms) {
		mazeArray = rooms;
	}
	
	private void exit() {
		Random rand=new Random();
		int exit=rand.nextInt(2);
		if (exit==0) {
			mazeArray[0][0][0].setDoor(Room.west, true);
			mazeArray[0][0][0].setLeadsOutside(Room.west);
			mazeArray[0][0][0].setPainting(new Painting().specifyPainting(Room.west, new Vector(0, 0, 0), false));
		} else {
			mazeArray[0][0][0].setDoor(Room.south, true);
			mazeArray[0][0][0].setLeadsOutside(Room.south);
			mazeArray[0][0][0].setPainting(new Painting().specifyPainting(Room.south, new Vector(0, 0, 0), false));
		}
	}
	
	public int shortestPath() {
		LinkedList<Position> q = new LinkedList<Position>();
		boolean[][][] visited=new boolean[mazeArray.length][mazeArray.length][mazeArray.length];
		int[][][] distance=new int[mazeArray.length][mazeArray.length][mazeArray.length];
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance.length; j++)
				for (int k = 0; k < distance.length; k++)
					distance.clone()[i][j][k] = Integer.MAX_VALUE;
		distance[mazeArray.length-1][mazeArray.length-1][mazeArray.length-1]=0;
		visited[mazeArray.length-1][mazeArray.length-1][mazeArray.length-1]=true;
		Position next = new Position(mazeArray.length-1, mazeArray.length-1, mazeArray.length-1);
		q.add(next);
		boolean found=false;
		while (!found && q.size() > 0) {
//			for (MazePos pos : q)
//				System.out.print(pos);
//			System.out.println();
//			System.out.println("q size: " + q.size());
			Position prev = q.poll();
			int x = (int) prev.getX(), y = (int) prev.getY(), z = (int) prev.getZ();
			Room room = mazeArray[(int) prev.getX()][(int) prev.getY()][(int) prev.getZ()];
			boolean[] doors=new boolean[6];
			for (int i=0;i<6;i++) {
				doors[i]=room.getDoor(i);
			}
			//System.out.println(prev + ": " + distance[x][y][z]);
			if (!(x == 0 && y == 0 && z == 0)) {
				if (doors[0]) {
					if (!visited[x][y+1][z]) {
						visited[x][y+1][z]=true;
						distance[x][y+1][z] = Math.min(distance[x][y][z]+1, distance[x][y+1][z]);
						next = new Position(x, y+1, z);
						q.add(next);
					}
					//System.out.print("N");
				}
				if (doors[1]) {
					if (!visited[x+1][y][z]) {
						visited[x+1][y][z]=true;
						distance[x+1][y][z] = Math.min(distance[x][y][z]+1, distance[x+1][y][z]);
						next = new Position(x+1, y, z);
						q.add(next);
					}
					//System.out.print("E");
				}
				if (doors[2]) {
					if (!visited[x][y-1][z]) {
						visited[x][y-1][z]=true;
						distance[x][y-1][z] = Math.min(distance[x][y][z]+1, distance[x][y-1][z]);
						next = new Position(x, y-1, z);
						q.add(next);
						if (x==0&&y-1==0&&z==0) {
							found=true;
						}
					}
					//System.out.print("S");
				}
				if (doors[3]) {
					if (!visited[x-1][y][z]) {
						visited[x-1][y][z]=true;
						distance[x-1][y][z] = Math.min(distance[x][y][z]+1, distance[x-1][y][z]);
						next = new Position(x-1, y, z);
						q.add(next);
						if (x-1==0&&y==0&&z==0) {
							found=true;
						}
					}
					//System.out.print("W");
				}
				if (doors[4]) {
					if (!visited[x][y][z+1]) {
						visited[x][y][z+1]=true;
						distance[x][y][z+1] = Math.min(distance[x][y][z]+1, distance[x][y][z+1]);
						next = new Position(x, y, z+1);
						q.add(next);
					}
					//System.out.print("U");
				}
				if (doors[5]) {
					if (!visited[x][y][z-1]) {
						visited[x][y][z-1]=true;
						distance[x][y][z-1] = Math.min(distance[x][y][z]+1, distance[x][y][z-1]);
						next = new Position(x, y, z-1);
						q.add(next);
						if (x==0&&y==0&&z-1==0) {
							found=true;
						}
					}
					//System.out.print("D");
				}
				//System.out.println();
			}
		}
		return distance[0][0][0];
	}
	
	public static int shortestPath(Room[][][] mazeArray) {
		LinkedList<Position> q = new LinkedList<Position>();
		boolean[][][] visited=new boolean[mazeArray.length][mazeArray.length][mazeArray.length];
		int[][][] distance=new int[mazeArray.length][mazeArray.length][mazeArray.length];
		for (int i = 0; i < distance.length; i++)
			for (int j = 0; j < distance.length; j++)
				for (int k = 0; k < distance.length; k++)
					distance.clone()[i][j][k] = Integer.MAX_VALUE;
		distance[mazeArray.length-1][mazeArray.length-1][mazeArray.length-1]=0;
		visited[mazeArray.length-1][mazeArray.length-1][mazeArray.length-1]=true;
		Position next = new Position(mazeArray.length-1, mazeArray.length-1, mazeArray.length-1);
		q.add(next);
		boolean found=false;
		while (!found && q.size() > 0) {
//			for (MazePos pos : q)
//				System.out.print(pos);
//			System.out.println();
//			System.out.println("q size: " + q.size());
			Position prev = q.poll();
			int x = (int) prev.getX(), y = (int) prev.getY(), z = (int) prev.getZ();
			Room room = mazeArray[(int) prev.getX()][(int) prev.getY()][(int) prev.getZ()];
			boolean[] doors=new boolean[6];
			for (int i=0;i<6;i++) {
				doors[i]=room.getDoor(i);
			}
			//System.out.println(prev + ": " + distance[x][y][z]);
			if (!(x == 0 && y == 0 && z == 0)) {
				if (doors[0]) {
					if (!visited[x][y+1][z]) {
						visited[x][y+1][z]=true;
						distance[x][y+1][z] = Math.min(distance[x][y][z]+1, distance[x][y+1][z]);
						next = new Position(x, y+1, z);
						q.add(next);
					}
					//System.out.print("N");
				}
				if (doors[1]) {
					if (!visited[x+1][y][z]) {
						visited[x+1][y][z]=true;
						distance[x+1][y][z] = Math.min(distance[x][y][z]+1, distance[x+1][y][z]);
						next = new Position(x+1, y, z);
						q.add(next);
					}
					//System.out.print("E");
				}
				if (doors[2]) {
					if (!visited[x][y-1][z]) {
						visited[x][y-1][z]=true;
						distance[x][y-1][z] = Math.min(distance[x][y][z]+1, distance[x][y-1][z]);
						next = new Position(x, y-1, z);
						q.add(next);
						if (x==0&&y-1==0&&z==0) {
							found=true;
						}
					}
					//System.out.print("S");
				}
				if (doors[3]) {
					if (!visited[x-1][y][z]) {
						visited[x-1][y][z]=true;
						distance[x-1][y][z] = Math.min(distance[x][y][z]+1, distance[x-1][y][z]);
						next = new Position(x-1, y, z);
						q.add(next);
						if (x-1==0&&y==0&&z==0) {
							found=true;
						}
					}
					//System.out.print("W");
				}
				if (doors[4]) {
					if (!visited[x][y][z+1]) {
						visited[x][y][z+1]=true;
						distance[x][y][z+1] = Math.min(distance[x][y][z]+1, distance[x][y][z+1]);
						next = new Position(x, y, z+1);
						q.add(next);
					}
					//System.out.print("U");
				}
				if (doors[5]) {
					if (!visited[x][y][z-1]) {
						visited[x][y][z-1]=true;
						distance[x][y][z-1] = Math.min(distance[x][y][z]+1, distance[x][y][z-1]);
						next = new Position(x, y, z-1);
						q.add(next);
						if (x==0&&y==0&&z-1==0) {
							found=true;
						}
					}
					//System.out.print("D");
				}
				//System.out.println();
			}
		}
		return distance[0][0][0];
	}

	public Room getRoom (Position playerPos) {
		return getRoom(playerPos.getX(), playerPos.getY(), playerPos.getZ());
	}
	
	public Room getRoom (int x, int y, int z) {
		return mazeArray[x][y][z];
	}
	
	public void printMaze() {
		System.out.println();
		for (int k = mazeArray.length-1; k >= 0; k--) {
			System.out.println("Floor: " + k);
			
			for (int i = 0; i < mazeArray.length; i++)
				System.out.print(" _______");
			System.out.println();
			
			for (int j = mazeArray.length-1; j >= 0; j--) {
				for (int q = 0; q <= mazeArray.length; q++)
					System.out.print("|       ");
				System.out.println();
				
				for (int i = 0; i<mazeArray.length; i++) {
					boolean[] doors=new boolean[6];
					for (int q = 0; q < 6; q++)
						doors[q]=mazeArray[i][j][k].getDoor(q);
					
					if (doors[Room.west])
						System.out.print(" ");
					else
						System.out.print("|");
					System.out.print("   ");
					
					if (doors[Room.up] && doors[Room.down])
						System.out.print("b");
					else if (doors[Room.up])
						System.out.print("^");
					else if (doors[Room.down])
						System.out.print("v");
					else
						System.out.print(" ");
					
					System.out.print("   ");
					if (i == mazeArray.length-1&&doors[Room.east])
						System.out.print(" ");
					else if (i == mazeArray.length-1)
						System.out.print("|");

				}
				System.out.println();
				
				for (int i = 0; i < mazeArray.length; i++)
					if (mazeArray[i][j][k].getDoor(Room.south))
						System.out.print("|__   __");						
					else
						System.out.print("|_______");
				System.out.println("|");
			}
			
			System.out.println();
		}
	}
	
	
//	public static void main (String[] args) {
//		Maze maze = new Maze(0);
//		maze.printMaze();
//		System.out.println("Shortest Path: " + maze.shortestPath());
//	}
}