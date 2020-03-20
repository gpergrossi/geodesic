package com.gpergrossi.geom.mesh.subdivide;

/**
 * Cleanable objects have a clean() method.
 * 
 * Depending on context, clean can be expected to return the object to
 * a state in which it will be re-usable for a new task.
 */
public interface Cleanable {
	public void clean();
}
