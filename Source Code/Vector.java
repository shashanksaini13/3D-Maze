public class Vector {
	private double x, y, z;
	public double getX () { return x; }
	public double getY () { return y; }
	public double getZ () { return z; }
	
	public Vector () { x = 0; y = 0; z = 0; }
	public Vector (double x, double y) { this.x = x; this.y = y; z = 0; }
	public Vector (double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
	
	public Vector plus (Vector pos) {
		return new Vector(x+pos.getX(), y+pos.getY(), z+pos.getZ());
	}
	public Vector scale (double factor) {
		x *= factor;
		y *= factor;
		z *= factor;
		return this;
	}
	public Vector minus (Vector pos) {
		return (this.clone().plus(pos.clone().scale(-1)));
	}
	public Vector unit () {
		return this.clone().scale(1/this.magnitude());
	}
	
	public Vector clone () {
		return new Vector(x, y, z);
	}
	public double magnitude () {
		return Math.sqrt(x*x + y*y + z*z);
	}
	public String toString () {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	public String toSmallString() {
		return "(" + (int) x + ", " + (int) y + ", " + (int) z + ")";
	}
	
	public double dot (Vector pos) {
		return x*pos.getX() + y*pos.getY() + z*pos.getZ();
	}
	public Vector cross (Vector pos) {
		return new Vector(y*pos.getZ()-z*pos.getY(), -1*(x*pos.getZ()-z*pos.getX()), x*pos.getY()-y*pos.getX());
	}
	public Vector projOnto (Vector pos) {
		return pos.clone().scale(this.dot(pos)/(pos.magnitude()*pos.magnitude()));
	}
}
