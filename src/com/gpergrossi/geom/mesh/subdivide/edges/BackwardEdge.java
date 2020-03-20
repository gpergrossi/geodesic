package com.gpergrossi.geom.mesh.subdivide.edges;

import com.gpergrossi.geom.mesh.subdivide.vertices.MappableVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.RemappedVertex;

/**
 * A BackwarEdge is a SubdividedEdge that is a mirror of a ForwardEdge.
 * 
 * In order to avoid duplicating vertices that are shared by multiple triangles along
 * an edge, each edge must be uniquely stored and subdivided only once.
 * 
 * However, the direction of the edge matters in the subdividing process, so
 * this BackwardEdge class works as proxy to the unique ForwardEdge while maintaining
 * the direction of the edge from the perspective of the triangle.
 *
 */
public class BackwardEdge implements SubdividedEdge {
	public final ForwardEdge ref;

	public BackwardEdge(ForwardEdge edge) {
		this.ref = edge;
	}
	
	@Override
	public RemappedVertex getStartVertex() {
		return ref.getEndVertex(); // return backwards edge information
	}

	@Override
	public RemappedVertex getEndVertex() {
		return ref.getStartVertex(); // return backwards edge information
	}

	@Override
	public SubdividedEdge reverse() {
		return ref;
	}
	
	@Override
	public void setSubdivide(int division) {
		ref.setSubdivide(division);
	}

	@Override
	public int getCurrentSubdivision() {
		return ref.getCurrentSubdivision();
	}

	@Override
	public MappableVertex getSubdivideVertex(int i) {
		return ref.getSubdivideVertex(this.getCurrentSubdivision() - i);
	}

	@Override
	public void clean() {
		ref.clean();
	}
}
