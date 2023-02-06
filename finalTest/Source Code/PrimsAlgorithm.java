import java.util.*;

public class PrimsAlgorithm {
	// http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm
	private boolean[][][] visited;
	private ArrayList<Position> frontier;
	private int size;

	public PrimsAlgorithm (int size) {
		visited = new boolean[size][size][size];
		frontier = new ArrayList<Position>();
		this.size = size;
	}

	public Room[][][] generateMaze () {
		Room[][][] maze = new Room[size][size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				for (int k = 0; k < size; k++)
					maze[i][j][k] = new Room();
		visited[(size-1)/2][(size-1)/2][(size-1)/2] = true;
		Position[] initialFrontier = {new Position((size-1)/2-1, (size-1)/2, (size-1)/2),
				new Position((size-1)/2+1, (size-1)/2, (size-1)/2),
				new Position((size-1)/2, (size-1)/2-1, (size-1)/2),
				new Position((size-1)/2, (size-1)/2+1, (size-1)/2),
				new Position((size-1)/2, (size-1)/2, (size-1)/2-1),
				new Position((size-1)/2, (size-1)/2, (size-1)/2+1)
		};
		for (Position pos : initialFrontier)
			if (pos.isValid(size))
				frontier.add(pos);
		//			frontier.add(new Position(size-1, size-1, size-2));
		//			frontier.add(new Position(size-1, size-2, size-1));
		//			frontier.add(new Position(size-2, size-1, size-1));
		while (frontier.size() > 0) {
			//System.out.println("Frontier size: " + frontier.size());
			Position newRoom = frontier.get((int) (frontier.size() * Math.random()));
			connectRoom(maze, newRoom);
			visited[newRoom.getX()][newRoom.getY()][newRoom.getZ()] = true;
			frontier.remove(newRoom);
			addFrontier(newRoom);
		}
		//System.out.println(maze.length);
		if (Maze.shortestPath(maze)<16) {
			visited = new boolean[size][size][size];
			frontier = new ArrayList<Position>();
			return generateMaze();
		}
		return maze;
	}

	private void addFrontier (Position newRoom) {
		Position[] possibleFrontier = {new Position(newRoom.getX()  + 1, newRoom.getY(), newRoom.getZ()),
				new Position(newRoom.getX() - 1, newRoom.getY(), newRoom.getZ()),
				new Position(newRoom.getX(), newRoom.getY() + 1, newRoom.getZ()),
				new Position(newRoom.getX(), newRoom.getY() - 1, newRoom.getZ()),
				new Position(newRoom.getX(), newRoom.getY(), newRoom.getZ() + 1),
				new Position(newRoom.getX(), newRoom.getY(), newRoom.getZ() - 1)};
		for (Position pos : possibleFrontier) {
			//System.out.println("frontier.contains(newRoom): " + frontier.contains(newRoom));
			if (pos.isValid(visited.length) && !visited[pos.getX()][pos.getY()][pos.getZ()]) {
				boolean inFrontier = false;
				for (Position frontierPos : frontier)
					if (frontierPos.equals(pos)) {
						inFrontier = true;
						break;
					}
				if (!inFrontier)
					frontier.add(pos);
			}
		}
	}

	private void connectRoom (Room[][][] maze, Position newRoom) {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++)
			order.add(i);
		Collections.shuffle(order);
		for (Integer direction : order) {
			switch (direction) {
			case 0: //connect E
				Position connectingRoom0 = new Position(newRoom.getX() + 1, newRoom.getY(), newRoom.getZ());
				if (connectingRoom0.isValid(size) && 
						visited[connectingRoom0.getX()][connectingRoom0.getY()][connectingRoom0.getZ()]) {
					maze[connectingRoom0.getX()][connectingRoom0.getY()][connectingRoom0.getZ()].setDoor(Room.west, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.east, true);
					//System.out.println("Connecting" + newRoom + "to " + connectingRoom0 + ".");
					return;
				}
				continue;
			case 1: // connects W
				Position connectingRoom1 = new Position(newRoom.getX() - 1, newRoom.getY(), newRoom.getZ());
				if (connectingRoom1.isValid(size) && 
						visited[connectingRoom1.getX()][connectingRoom1.getY()][connectingRoom1.getZ()]) {
					maze[connectingRoom1.getX()][connectingRoom1.getY()][connectingRoom1.getZ()].setDoor(Room.east, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.west, true);
					//System.out.println("Connecting" + newRoom + "to " + connectingRoom1 + ".");
					return;
				}
				continue;
			case 2: // connects N
				Position connectingRoom2 = new Position(newRoom.getX(), newRoom.getY() + 1, newRoom.getZ());
				if (connectingRoom2.isValid(size) && 
						visited[connectingRoom2.getX()][connectingRoom2.getY()][connectingRoom2.getZ()]) {
					maze[connectingRoom2.getX()][connectingRoom2.getY()][connectingRoom2.getZ()].setDoor(Room.south, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.north, true);
					//System.out.println("Connecting" + newRoom + "to " + connectingRoom2 + ".");
					return;
				}
				continue;
			case 3: // connects S
				Position connectingRoom3 = new Position(newRoom.getX(), newRoom.getY() - 1, newRoom.getZ());
				if (connectingRoom3.isValid(size) && 
						visited[connectingRoom3.getX()][connectingRoom3.getY()][connectingRoom3.getZ()]) {
					maze[connectingRoom3.getX()][connectingRoom3.getY()][connectingRoom3.getZ()].setDoor(Room.north, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.south, true);
					//System.out.println("Connecting" + newRoom + "to " + connectingRoom3 + ".");
					return;
				}
				continue;
			case 4: // connects U
				Position connectingRoom4 = new Position(newRoom.getX(), newRoom.getY(), newRoom.getZ() + 1);
				if (connectingRoom4.isValid(size) && 
						visited[connectingRoom4.getX()][connectingRoom4.getY()][connectingRoom4.getZ()]) {
					maze[connectingRoom4.getX()][connectingRoom4.getY()][connectingRoom4.getZ()].setDoor(Room.down, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.up, true);
					//System.out.println("Connecting" + newRoom + "to " + connectingRoom4 + ".");
					return;
				}
				continue;
			case 5: // connects D
				Position connectingRoom5 = new Position(newRoom.getX(), newRoom.getY(), newRoom.getZ() - 1);
				if (connectingRoom5.isValid(size) && 
						visited[connectingRoom5.getX()][connectingRoom5.getY()][connectingRoom5.getZ()]) {
					maze[connectingRoom5.getX()][connectingRoom5.getY()][connectingRoom5.getZ()].setDoor(Room.up, true);
					maze[newRoom.getX()][newRoom.getY()][newRoom.getZ()].setDoor(Room.down, true);
					//System.out.println("Connecting" + newRoom + " to " + connectingRoom5 + ".");
					return;
				}
				continue;
			}
		}
	}
}