package com.gpergrossi.geom.mesh.subdivide.vertices;

import com.gpergrossi.geom.Vertex;

/**
 * A SubdivideVertex is a MappableVertex that did not exist in the
 * original mesh. It is created by the subdivision process and does
 * not correspond to an original vertex.
 * 
 * As with all MappableVertex objects, it records its own index mapping. 
 * We can use this information to avoid adding the same vertex to a mesh more than once.
 */
public class SubdivideVertex implements MappableVertex {

	Vertex vertex;
	private int assignedIndex = -1;
	
	public SubdivideVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	@Override
	public Vertex getVertex() {
		return vertex;
	}

	@Override
	public boolean isAssigned() {
		return this.assignedIndex != -1;
	}

	@Override
	public void setIndex(int index) {
		this.assignedIndex = index;
	}

	@Override
	public int getIndex() {
		if (assignedIndex == -1) throw new IllegalStateException("No index assigned!");
		return assignedIndex;
	}

}
