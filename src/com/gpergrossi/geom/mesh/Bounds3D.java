package com.gpergrossi.geom.mesh;

import com.gpergrossi.geom.Point3D;

public class Bounds3D {

	private boolean empty;
	private double minX, maxX;
	private double minY, maxY;
	private double minZ, maxZ;
	
	public Bounds3D() { 
		this.empty = true;
	}

	private void initialize(Point3D pt) {			
		this.minX = pt.x;
		this.maxX = pt.x;
		this.minY = pt.y;
		this.maxY = pt.y;
		this.minZ = pt.z;
		this.maxZ = pt.z;
		this.empty = false;
	}
	
	public void include(Point3D pt) {
		if (this.empty) this.initialize(pt);
		this.minX = Math.min(minX, pt.x);
		this.maxX = Math.max(maxX, pt.x);
		this.minY = Math.min(minY, pt.y);
		this.maxY = Math.max(maxY, pt.y);
		this.minZ = Math.min(minZ, pt.z);
		this.maxZ = Math.max(maxZ, pt.z);
	}

	public Bounds3D copy() {
		Bounds3D copy = new Bounds3D();
		copy.empty = this.empty;
		copy.minX = this.minX;
		copy.maxX = this.maxX;
		copy.minY = this.minY;
		copy.maxY = this.maxY;
		copy.minZ = this.minZ;
		copy.maxZ = this.maxZ;
		return copy;
	}

	public boolean contains(Point3D pt) {
		if (this.empty) return false;
		if (pt.x < minX) return false;
		if (pt.x < maxX) return false;
		if (pt.y < minY) return false;
		if (pt.y < maxY) return false;
		if (pt.z < minZ) return false;
		if (pt.z < maxZ) return false;
		return true;
	}
	
}
