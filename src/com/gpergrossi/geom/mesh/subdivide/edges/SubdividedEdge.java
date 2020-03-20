package com.gpergrossi.geom.mesh.subdivide.edges;

import com.gpergrossi.geom.mesh.subdivide.Cleanable;
import com.gpergrossi.geom.mesh.subdivide.vertices.MappableVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.RemappedVertex;

public interface SubdividedEdge extends Cleanable {
	public RemappedVertex getStartVertex();
	public RemappedVertex getEndVertex();
	public SubdividedEdge reverse();
	
	public void setSubdivide(int division);
	public int getCurrentSubdivision();
	public MappableVertex getSubdivideVertex(int i);
}
