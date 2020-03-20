package com.gpergrossi.geom.mesh.subdivide;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.Vertex;
import com.gpergrossi.geom.mesh.MeshBuilder;
import com.gpergrossi.geom.mesh.subdivide.edges.SubdividedEdge;
import com.gpergrossi.geom.mesh.subdivide.vertices.MappableVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.RemappedVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.SubdivideVertex;
import com.gpergrossi.util.SubdivideUtil;

public class SubdividedTriangle implements Cleanable {
	public List<SubdividedEdge> edges;
	private RemappedVertex vertexA;
	private RemappedVertex vertexB;
	private RemappedVertex vertexC;

	private Object subdivisionLock = new Object();
	private volatile int subdivision = 1;
	private volatile List<List<MappableVertex>> subdividedVertices;
	
	public SubdividedTriangle() {
		this.edges = new ArrayList<>();
	}

	public void addEdge(SubdividedEdge subEdge) {
		this.edges.add(subEdge);
		
		switch(this.edges.size()) {
		case 1:
			this.vertexA = this.edges.get(0).getStartVertex();
			this.vertexB = this.edges.get(0).getEndVertex();
			break;
			
		case 2:
			if (this.vertexB != this.edges.get(1).getStartVertex()) throw new IllegalStateException("Second edge must have shared vertex with first edge!");
			this.vertexC = this.edges.get(1).getEndVertex();
			break;
		
		case 3:
			if (this.vertexC != this.edges.get(2).getStartVertex()) throw new IllegalStateException("Third edge must have shared vertex with second edge!");
			if (this.vertexA != this.edges.get(2).getEndVertex()) throw new IllegalStateException("Third edge must have shared vertex with first edge!");
			break;
			
		default:
			throw new IllegalStateException("Too many edges!");
		}
	}
	
	public void verify() {
		if (this.edges.size() < 3) throw new IllegalStateException("Too few edges!");
		if (this.edges.size() > 3) throw new IllegalStateException("Too many edges!");
		if (this.vertexA != this.edges.get(2).getEndVertex()) throw new IllegalStateException("VertexA did not match third edge!");
		if (this.vertexA != this.edges.get(0).getStartVertex()) throw new IllegalStateException("VertexA did not match first edge!");
		if (this.vertexB != this.edges.get(0).getEndVertex()) throw new IllegalStateException("VertexB did not match first edge!");
		if (this.vertexB != this.edges.get(1).getStartVertex()) throw new IllegalStateException("VertexB did not match second edge!");
		if (this.vertexC != this.edges.get(1).getEndVertex()) throw new IllegalStateException("VertexC did not match second edge!");
		if (this.vertexC != this.edges.get(2).getStartVertex()) throw new IllegalStateException("VertexC did not match third edge!");
	}

	public void subdivide(int division) {
		synchronized (subdivisionLock) {
			if (this.subdivision == division && subdividedVertices != null) return; // Already done
			if (division < 1) throw new IllegalArgumentException();
			
			this.edges.parallelStream().forEach((item) -> item.setSubdivide(division));
			
			SubdividedEdge edgeA = this.edges.get(0);
			SubdividedEdge edgeB = this.edges.get(1);
			SubdividedEdge edgeC = this.edges.get(2);


			// We're going to form rows following the subdivided vertices of A and C.reverse()
			edgeC = edgeC.reverse(); // This operation is not modifying edgeC. We are only reversing our VIEW of it.
			
			// We will be creating rows of vertices to form a triangular lattice.
			// The letter vertices are shared vertices from the original mesh.
			// The edge vertices are already defined in the various edges.
			// The following operation will create an array of all vertices in reading order.
			// The vertices represented by stars ('*') will be created new.
			//
			//                    a[0]  A  c.reverse()[0]
			//                         / \
			//                  a[1]  . - .  c.reverse()[1]
			//                       /     \
			//                a[2]  . - * - .  c.reverse()[2]
			//                     /         \
			//              a[3]  . - * - * - .  c.reverse()[3]
			//                   /             \
			//            a[4]  B - . - . - . - C  c.reverse()[4]
			//
			//               b[0] b[1] b[2] b[3] b[4]
			//
			// NOTE: '-', '/', and '\' do not represent vertices. They are only there to give the diagram visual structure.
			
			this.subdivision = division;
			this.subdividedVertices = new ArrayList<>();
			for (int i = 0; i <= division; i++) {
				List<MappableVertex> row = new ArrayList<>();
				if (i == 0) {
					// First row is just a[0]
					row.add(edgeA.getSubdivideVertex(0));
				} else if (i == division) {
					// Last row is just edgeB
					for (int j = 0; j <= i; j++) {
						row.add(edgeB.getSubdivideVertex(j));
					}
				} else {
					// Other rows may need to produce new points
					MappableVertex start = edgeA.getSubdivideVertex(i);
					MappableVertex end = edgeC.getSubdivideVertex(i);
					
					row.add(start);
					List<Vertex> verts = SubdivideUtil.interpolateVertices(start.getVertex(), end.getVertex(), i);
					for (int j = 1; j < i; j++) {
						row.add(new SubdivideVertex(verts.get(j)));
					}
					row.add(end);
				}
				this.subdividedVertices.add(row);
			}
		}
	}
	
	public int getSubdivision() {
		synchronized (subdivisionLock) {
			return this.subdivision;
		}
	}
	
	/**
	 * The layout of the vertex arrays is N rows (from 0 to division) where
	 * each row is one longer than the last until the last row has (division + 1) vertices.
	 * 
	 * Example: <pre>
	 * // If division is 2, there will be 6 vertices layed out as follows:
	 * [i = 0]: { [j = 0]: vertex0 }
	 * [i = 1]: { [j = 0]: vertex1, [j = 1]: vertex2 }
	 * [i = 2]: { [j = 0]: vertex3, [j = 1]: vertex4, [j = 2]: vertex5 }
	 * </pre>
	 * 
	 * Arguments are valid for i from 0 to subdivision (inclusive) and for j from 0 to i (inclusive).
	 * 
	 * @param i - row
	 * @param j - index
	 * @return
	 */
	public MappableVertex getVertex(int i, int j) {
		synchronized (subdivisionLock) {
			if (this.subdividedVertices == null) throw new IllegalStateException("Not subdivided yet!");
			return this.subdividedVertices.get(i).get(j);
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

	public void buildMesh(MeshBuilder meshBuilder, boolean skipFaces) {
		synchronized (subdivisionLock) {
			for (int i = 0; i < subdivision; i++) {
				List<MappableVertex> row0 = this.subdividedVertices.get(i);
				List<MappableVertex> row1 = this.subdividedVertices.get(i+1);
				
				if (skipFaces) {
					// only add vertices
					row0.forEach(mv -> mv.getOrCreateIndex(meshBuilder));
					row1.forEach(mv -> mv.getOrCreateIndex(meshBuilder));
				} else {
					// stitch faces
					stitch(meshBuilder, row0, row1);
				}
			}
		}
	}
	
	/**
	 * Takes two rows of MappableVertices and builds a triangle strip between them.
	 * @param meshBuilder
	 * @param row0 - top row
	 * @param row1 - bottom row
	 */
	private static void stitch(MeshBuilder meshBuilder, List<MappableVertex> row0, List<MappableVertex> row1) {
		if (row0.size() < 1) throw new IllegalArgumentException("row0 must have a size of at least 1");
		if (row1.size() < 1) throw new IllegalArgumentException("row1 must have a size of at least 1");
		if (row0.size() + row1.size() < 3) throw new IllegalArgumentException("row0 and row1 must have a total size of at least 3");
		if (row0.size() < row1.size() - 1) throw new IllegalArgumentException("row0 must have a size of at least row1.size() - 1");
		if (row0.size() > row1.size() + 1) throw new IllegalArgumentException("row0 must have a size of at most row1.size() + 1");
		
		boolean currentTriangleFacingUp = true;
		int index0 = 0;
		int index1 = 0;
		if (row0.size() > row1.size()) {
			currentTriangleFacingUp = false;
		}
		
		@SuppressWarnings("unused")
		int trianglesProduced = 0;
		
		while (true) {
			MappableVertex a, b, c;
			
			if (currentTriangleFacingUp) {
				if (index0 >= row0.size() || index1 + 1 >= row1.size()) break;
				a = row0.get(index0);
				b = row1.get(index1);
				c = row1.get(index1 + 1);
				index1++;
			} else {
				if (index0 +1 >= row0.size() || index1 >= row1.size()) break;
				a = row0.get(index0);
				b = row1.get(index1);
				c = row0.get(index0 + 1);
				index0++;
			}
			currentTriangleFacingUp = !currentTriangleFacingUp;
			
			// Add vertices only if necessary
			int indexA = a.getOrCreateIndex(meshBuilder);
			int indexB = b.getOrCreateIndex(meshBuilder);
			int indexC = c.getOrCreateIndex(meshBuilder);
			
			// Add triangle
			meshBuilder.AddTriangle(new Triangle(indexA, indexB, indexC));
			trianglesProduced++;
		}
		
		//System.out.println("Stitching rows of " + row0.size() + " and " + row1.size() + " elements produced " + trianglesProduced + " triangles");
	}
}
