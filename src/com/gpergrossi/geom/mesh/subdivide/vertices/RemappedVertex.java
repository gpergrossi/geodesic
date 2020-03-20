package com.gpergrossi.geom.mesh.subdivide.vertices;

import com.gpergrossi.geom.Vertex;
import com.gpergrossi.geom.mesh.Mesh;
import com.gpergrossi.geom.mesh.subdivide.Cleanable;

/**
 * A RemappedVertex is a MappableVertex that comes from an existing mesh.
 * 
 * As with all MappableVertex objects, it records its own index mapping. 
 * We can use this information to avoid adding the same vertex to a mesh more than once.
 */
public class RemappedVertex implements MappableVertex, Cleanable {
	
	public final Mesh mesh;
	public final int originalIndex;
	private int assignedIndex = -1;
	
	public RemappedVertex(Mesh mesh, int originalIndex) {
		this.mesh = mesh;
		if (originalIndex < 0 || originalIndex >= mesh.getVertices().size()) throw new IndexOutOfBoundsException();
		this.originalIndex = originalIndex;
	}

	@Override
	public Vertex getVertex() {
		return mesh.getVertices().get(originalIndex);
	}

	@Override
	public void clean() {
		this.assignedIndex = -1;
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
