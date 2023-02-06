public class Player {
	private Position position;
	private int orientation;
	private boolean[][][] roomVisits;
	private int moves;

	public Player (Position pos, int orient, int xSize, int ySize, int zSize) {
		position = pos;
		orientation = orient;
		moves = 0;
		roomVisits = new boolean[xSize][ySize][zSize];
		roomVisits[position.getX()][position.getY()][position.getZ()] = true;
		// 5,5,5 should be true
	}

	public Position getPosition() {
		//System.out.println("Accessing player position");
		return position;
	}

	public int getOrientation() {
		return orientation;
	}
	
	public int getMoves() {
		return moves;
	}

	public void turnLeft() {
		if (orientation > 0) {
			orientation = orientation - 1;
		} else if (orientation == 0) {
			orientation = 3;
		}
	}

	public void turnRight() {
		if (orientation < 3) {
			orientation = orientation + 1;
		} else if (orientation == 3) {
			orientation = 0;
		}
	}

	public void moveForward() {
		if (orientation == 0) {
			position.incrementY();
		} else if (orientation == 1) {
			position.incrementX();
		} else if (orientation == 2) {
			position.decrementY();
		} else if (orientation == 3) {
			position.decrementX();
		}
		roomVisits[position.getX()][position.getY()][position.getZ()] = true;
		moves++;
	}

	public void moveUp() {
		position.incrementZ();
		roomVisits[position.getX()][position.getY()][position.getZ()] = true;
		moves++;
	}

	public void moveDown() {
		position.decrementZ();
		roomVisits[position.getX()][position.getY()][position.getZ()] = true;
		moves++;
	}

	public boolean roomVisited(int x, int y, int z) {
		if (roomVisits[x][y][z]) {
			return true;
		}
		return false;
	}
	
////	//TODO: REMOVE FOR FINAL RELEASE
//	public void setPosition (Position pos) {
//		this.position = pos;
//	}
}