package com.gpergrossi.geom.mesh.subdivide.vertices;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.mesh.Mesh;

/**
 * A VertexMap stores a mapping from vertices in the original mesh to
 * RemappedVertex objects that may or may not have yet been used in the
 * subdivided mesh.
 * 
 * The VertexMap also provides a getRemappedVertices() function that can
 * be used to retrieve a list of all RemappedVertex objects.
 */
public class VertexMap {
	private final Mesh mesh;
	RemappedVertex[] remappedVerts;
	
	public VertexMap(Mesh mesh) {
		this.mesh = mesh;
		this.remappedVerts = new RemappedVertex[mesh.getVertices().size()];
	}
	
	public RemappedVertex getVertex(int originalIndex) {
		if (originalIndex < 0 || originalIndex >= remappedVerts.length) throw new IndexOutOfBoundsException();
		
		if (remappedVerts[originalIndex] == null) {
			remappedVerts[originalIndex] = new RemappedVertex(mesh, originalIndex);
		}
		return remappedVerts[originalIndex];
	}

	public List<RemappedVertex> getRemappedVertices() {
		List<RemappedVertex> verts = new ArrayList<RemappedVertex>();
		for (int i = 0; i < remappedVerts.length; i++) {
			if (remappedVerts[i] != null) verts.add(remappedVerts[i]);
		}
		return verts;
	}
}
