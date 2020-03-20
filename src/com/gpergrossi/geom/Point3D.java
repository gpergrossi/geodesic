package com.gpergrossi.geom;

public class Point3D {
	
	public static final Point3D ZERO = new Point3D(0, 0, 0);
	
	public final double x, y, z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distanceSquaredTo(Point3D other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		double dz = this.z - other.z;
		return dx*dx + dy*dy + dz*dz;
	}

	public double distanceTo(Point3D other) {
		return Math.sqrt(this.distanceSquaredTo(other));
	}

	public Point3D normalize() {
		double length = this.norm();
		return new Point3D(this.x / length, this.y / length, this.z / length);
	}

	private double norm() {
		return Math.sqrt(this.normSquared());
	}

	private double normSquared() {
		return x*x + y*y + z*z;
	}
	
}
