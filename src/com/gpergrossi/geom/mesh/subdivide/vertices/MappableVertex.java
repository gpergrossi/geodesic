package com.gpergrossi.geom.mesh.subdivide.vertices;

import com.gpergrossi.geom.Vertex;
import com.gpergrossi.geom.mesh.MeshBuilder;

/**
 * A mappable vertex represents a vertex that may or may not have yet been used in a mesh.
 * Once the mappable vertex is used in a mesh, its index is assigned and we know not to
 * add the same vertex to the mesh a second time.
 */
public interface MappableVertex {
	public Vertex getVertex();
	
	public boolean isAssigned();
	public void setIndex(int index);
	public int getIndex();
	
	public default int getOrCreateIndex(MeshBuilder mb) {
		int index;
		if (!this.isAssigned()) {
			index = mb.AddVertex(this.getVertex());
			this.setIndex(index);
		} else {
			index = this.getIndex();
		}
		return index;
	}
}
