public class Position {
	private int x, y, z;
	
	public int getX() { return x; }
	public void incrementX () { x++; }
	public void decrementX () { x--; }
	
	public int getY() { return y; }
	public void incrementY () { y++; }
	public void decrementY () { y--; }
	
	public int getZ() { return z; }
	public void incrementZ () { z++; }
	public void decrementZ () { z--; }
	
	public Position (int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean isValid (int size) {
		return (x >= 0 && x < size && y >= 0 && y < size && z >= 0 && z < size);
	}
	
	public String toString () {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public boolean equals (Position pos) {
		return (x == pos.x && y == pos.y && z == pos.z);
	}
	
	public int[] toArray () {
		return new int[] {x, y, z};
	}
}
