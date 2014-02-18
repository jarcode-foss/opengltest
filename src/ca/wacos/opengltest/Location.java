package ca.wacos.opengltest;

public class Location implements Cloneable {

	public static final double MAX_YAW = Math.PI / 2d;
	public static final double MIN_YAW = -Math.PI / 2d;

	private double x = 0;
	private double y = 0;
	private double z = 0;
	private double yaw = 0; // in radians
	private double pitch = 0; // in radians

	public Location(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(double x, double y, double z, double yaw, double pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location copy(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
		return this;
	}
	public Location clone() {
		return new Location(x, y, z, yaw, pitch);
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
	public double getYaw() {
		return yaw;
	}
	public double getPitch() {
		return pitch;
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
	public void setYaw(double yaw) {
		this.yaw = yaw % (Math.PI * 2);
	}
	public void setPitch(double pitch) {
		if (pitch > MAX_YAW)
			this.pitch = MAX_YAW;
		else if (pitch < MIN_YAW)
			this.pitch = MIN_YAW;
		else
			this.pitch = pitch;
	}
	public Location addX(double x) {
		this.x += x;
		return this;
	}
	public Location addY(double y) {
		this.y += y;
		return this;
	}
	public Location addZ(double z) {
		this.z += z;
		return this;
	}
	public Location subtractX(double x) {
		this.x -= x;
		return this;
	}
	public Location subtractY(double y) {
		this.y -= y;
		return this;
	}
	public Location subtractZ(double z) {
		this.z -= z;
		return this;
	}
	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	public Location add(Location loc) {
		x += loc.getX();
		y += loc.getX();
		z += loc.getX();
		return this;
	}
	public Location subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	public Location subtract(Location loc) {
		x -= loc.getX();
		y -= loc.getX();
		z -= loc.getX();
		return this;
	}
	public double distance(Location loc) {
		return Math.sqrt(distanceSquared(loc));
	}
	public double distanceSquared(Location loc) {
		return Math.pow(loc.getX() - x, 2) + Math.pow(loc.getY() - y, 2) + Math.pow(loc.getZ() - z, 2);
	}
	public Vector toVector() {
		return toVector(1);
	}
	public Vector toVector(double magnitude) {
		double x = magnitude * Math.sin(yaw) * Math.cos(pitch);
		double z = magnitude * Math.cos(yaw) * Math.cos(pitch);
		double y = magnitude * Math.sin(yaw);
		return new Vector(x, y, z, clone());
	}
}
