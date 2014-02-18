package ca.wacos.opengltest;

public class Vector implements Cloneable {

	// These are coordinates relative to the origin (0, 0, 0). Changes to the origin variable do not affect these values
	private double x = 0;
	private double y = 0;
	private double z = 0;

	private Location origin = new Location(0, 0, 0);

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(double x, double y, double z, Location origin) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.origin = origin;
	}

	public Vector copy(Vector vec) {
		x = vec.getX();
		y = vec.getY();
		z = vec.getZ();
		origin = vec.origin.clone();
		return this;
	}
	public Vector clone() {
		return new Vector(x, y, z, origin.clone());
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public Vector add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	public Vector add(Location loc) {
		x += loc.getX();
		y += loc.getX();
		z += loc.getX();
		return this;
	}
	public Vector subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	public Vector subtract(Location loc) {
		x -= loc.getX();
		y -= loc.getX();
		z -= loc.getX();
		return this;
	}
	public Vector addX(double x) {
		this.x += x;
		return this;
	}
	public Vector addY(double y) {
		this.y += y;
		return this;
	}
	public Vector addZ(double z) {
		this.z += z;
		return this;
	}
	public Vector subtractX(double x) {
		this.x -= x;
		return this;
	}
	public Vector subtractY(double y) {
		this.y -= y;
		return this;
	}
	public Vector subtractZ(double z) {
		this.z -= z;
		return this;
	}
	public Vector multiply(double m) {
		x *= m;
		y *= m;
		z *= m;
		return this;
	}
	public Location getOrigin() {
		return origin;
	}
	public Vector setOrigin(Location origin) {
		this.origin = origin;
		return this;
	}
	public double getMagnitude() {
		return Math.sqrt(getMagnitudeSquared());
	}
	public double getMagnitudeSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
	}
	public Vector setMagnitude(double m) {
		if (m < 0) return null;
		return multiply(m / getMagnitude());
	}

	// Compares origins
	public double distance(Vector vec) {
		return Math.sqrt(distanceSquared(vec));
	}
	public double distanceSquared(Vector vec) {
		return Math.pow(vec.getX() - x, 2) + Math.pow(vec.getY() - y, 2) + Math.pow(vec.getZ() - z, 2);
	}

	// Returns the x, y and z coordinates relative to the origin as a Location
	public Location relativeToOrigin() {
		return new Location(x + origin.getX(), y + origin.getY(), z + origin.getZ());
	}

	// Converts this vector to a Location with corresponding yaw and pitch
	public Location toLocation() {

		// Create positive yaw and calculate terminal angle
		double yaw = Math.atan(Math.abs(x/z));
		if (x < 0 && z > 0)
			yaw = Math.PI - yaw;
		else if (x < 0 && z < 0)
			yaw = Math.PI + yaw;
		else if (x > 0 && z < 0)
			yaw = 2 * Math.PI - yaw;

		double h = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

		// Positive pitch for looking up, negative for looking down.
		double pitch = Math.atan(Math.abs(y/h));
		if (y < 0)
			pitch = -pitch;

		return new Location(origin.getX(), origin.getY(), origin.getZ(), yaw, pitch);
	}
}
