package com.gpergrossi.geom.mesh.subdivide;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.Edge;
import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.mesh.Mesh;
import com.gpergrossi.geom.mesh.MeshBuilder;
import com.gpergrossi.geom.mesh.subdivide.edges.EdgeMap;
import com.gpergrossi.geom.mesh.subdivide.edges.ForwardEdge;
import com.gpergrossi.geom.mesh.subdivide.edges.SubdividedEdge;
import com.gpergrossi.geom.mesh.subdivide.vertices.RemappedVertex;
import com.gpergrossi.geom.mesh.subdivide.vertices.VertexMap;

public class MeshSubdivider {

	private Mesh inputMesh;
	
	private boolean analyzed = false;
	private VertexMap vertexMap;
	private EdgeMap edgeMap;
	private List<SubdividedTriangle> subTriangles;
	
	public boolean skipFaces;
	
	public MeshSubdivider(Mesh mesh) {
		this.inputMesh = mesh;
		this.vertexMap = new VertexMap(mesh);
		this.edgeMap = new EdgeMap();
		this.subTriangles = new ArrayList<>();
	}
	
	public Mesh buildSubdividedMesh(int division) {
		if (division < 1) throw new IllegalArgumentException();
		
		// Build data structures
		this.analyzeInputMesh();
		
		// Clean previous working data
		this.vertexMap.getRemappedVertices().parallelStream().forEach((item) -> item.clean());
		this.edgeMap.getForwardEdges().parallelStream().forEach((item) -> item.clean());
		this.subTriangles.parallelStream().forEach((item) -> item.clean());
		
		// Subdivide
		this.subdivide(division);
		
		// Build mesh
		return buildMesh();
	}

	private void analyzeInputMesh() {
		if (analyzed) return;

		System.out.println("Analyzing mesh...");
		long time = System.currentTimeMillis();
		
		for (Triangle tri : this.inputMesh.getTriangles()) {
			SubdividedTriangle subTri = new SubdividedTriangle();
						
			for (Edge edge : tri.getEdges()) {
				boolean backward = (edge.vertexA > edge.vertexB);
				RemappedVertex lesserVertex = vertexMap.getVertex(!backward ? edge.vertexA : edge.vertexB);
				RemappedVertex greaterVertex = vertexMap.getVertex(!backward ? edge.vertexB : edge.vertexA);
				
				ForwardEdge forwardEdge;
				if (edgeMap.containsEdge(lesserVertex.originalIndex, greaterVertex.originalIndex)) {
					forwardEdge = edgeMap.getEdge(lesserVertex.originalIndex, greaterVertex.originalIndex);
				} else {
					forwardEdge = new ForwardEdge(lesserVertex, greaterVertex);
					edgeMap.putEdge(lesserVertex.originalIndex, greaterVertex.originalIndex, forwardEdge);
				}
				
				SubdividedEdge subEdge = forwardEdge;
				if (backward) subEdge = subEdge.reverse();
				
				subTri.addEdge(subEdge);
			}
			subTri.verify();
			subTriangles.add(subTri);
		}
		
		int vertexCount = vertexMap.getRemappedVertices().size();
		assert (vertexCount == inputMesh.getVertices().size());
		
		int triangleCount = subTriangles.size();
		assert (triangleCount == inputMesh.getTriangles().size());

		int maxEdges = triangleCount * 3;
		int minEdges = maxEdges / 2;
		int edgeCount = edgeMap.getForwardEdges().size();
		assert(edgeCount > minEdges);
		assert(edgeCount < maxEdges);

		System.out.println("  " + vertexCount + " vertices");
		System.out.println("  " + triangleCount + " triangles");
		System.out.println("  " + edgeCount + " edges");
		
		long elapsed = System.currentTimeMillis() - time;
		System.out.println("Completed in " + elapsed + " ms");
		System.out.println();
		
		this.analyzed = true;
	}

	private void subdivide(int division) {
		System.out.println("Subdividing by " + division + "...");
		long time = System.currentTimeMillis();
		
		this.subTriangles.parallelStream().forEach((item) -> item.subdivide(division));
		
		long elapsed = System.currentTimeMillis() - time;
		System.out.println("Completed in " + elapsed + " ms");
		System.out.println();
	}
	
	private Mesh buildMesh() {
		System.out.println("Building mesh ...");
		long time = System.currentTimeMillis();
		
		MeshBuilder meshBuilder = new MeshBuilder();
		this.subTriangles.forEach(tri -> tri.buildMesh(meshBuilder, this.skipFaces));
		Mesh result = meshBuilder.build();

		System.out.println("  " + result.getVertices().size() + " vertices");
		System.out.println("  " + result.getTriangles().size() + " triangles");
		
		long elapsed = System.currentTimeMillis() - time;
		System.out.println("Completed in " + elapsed + " ms");
		System.out.println();
		
		return result;
	}
	
}
