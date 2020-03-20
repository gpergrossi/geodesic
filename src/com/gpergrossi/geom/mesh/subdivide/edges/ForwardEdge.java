package com.gpergrossi.geom.mesh.subdivide.edges;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.Vertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.MappableVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.RemappedVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.SubdivideVertex;
import com.gpergrossi.util.SubdivideUtil;

/**
 * In order to avoid duplicating vertices that are shared by multiple triangles along
 * an edge, each edge must be uniquely stored and subdivided only once.
 * 
 * If an edge is used by more than one triangle, we need to make sure each time that
 * edge is used it refers to the same edge object. Additionally, we want to treat an 
 * Edge A->B the same as another Edge B->A.  We call edges for which the condition
 * (index of vertexA < index of vertexB) holds "forward" edges. 
 * 
 * Finally, we use an EdgeMap to store the forward edges. We will only create an edge
 * if it is not already stored in the EdgeMap. When a BackwardEdge is encountered, we
 * find or create it's ForwardEdge equivalent and then produce a proxy BackwardEdge to
 * maintain the edge's direction while storing only the Forward copy.
 */
public class ForwardEdge implements SubdividedEdge {
	public RemappedVertex startVertex, endVertex;
	
	private Object subdivisionLock = new Object();
	private volatile int subdivision = 1;
	private volatile List<MappableVertex> subdividedVertices;
	
	public ForwardEdge(RemappedVertex startVertex, RemappedVertex endVertex) {
		if (startVertex.originalIndex > endVertex.originalIndex) throw new IllegalArgumentException();
		this.startVertex = startVertex;
		this.endVertex = endVertex;
	}

	@Override
	public RemappedVertex getStartVertex() {
		return startVertex;
	}

	@Override
	public RemappedVertex getEndVertex() {
		return endVertex;
	}

	@Override
	public SubdividedEdge reverse() {
		return new BackwardEdge(this);
	}

	@Override
	public void setSubdivide(int division) {
		synchronized (subdivisionLock) {
			if (this.subdivision == division && subdividedVertices != null) return; // Already done
			if (division < 1) throw new IllegalArgumentException();
			
			this.subdivision = division;
			this.subdividedVertices = new ArrayList<>();
			
			Vertex start = this.startVertex.getVertex();
			Vertex end = this.endVertex.getVertex();
			List<Vertex> verts = SubdivideUtil.interpolateVertices(start, end, division);

			this.subdividedVertices.add(startVertex);
			for (int i = 1; i < division; i++) {
				this.subdividedVertices.add(new SubdivideVertex(verts.get(i)));
			}
			this.subdividedVertices.add(endVertex);
		}
	}

	@Override
	public int getCurrentSubdivision() {
		synchronized (subdivisionLock) {
			return this.subdivision;
		}
	}

	@Override
	public MappableVertex getSubdivideVertex(int i) {
		synchronized (subdivisionLock) {
			if (this.subdividedVertices == null) throw new IllegalStateException("Not subdivided yet!");
			return this.subdividedVertices.get(i);
		}
	}

	@Override
	public void clean() {
		synchronized (subdivisionLock) {
			if (this.subdividedVertices != null) {
				this.subdividedVertices.clear();
				this.subdividedVertices = null;
			}
			this.subdivision = 1;
		}
	}
}
