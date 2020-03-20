package com.gpergrossi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.Vertex;

public class MeshBuilder {
	
	private Bounds3D bounds;
	private List<Vertex> vertices;
	private List<Triangle> triangles;
	
	public MeshBuilder() {
		this.bounds = new Bounds3D();
		this.vertices = new ArrayList<Vertex>();
		this.triangles = new ArrayList<Triangle>();
	}

	public synchronized int AddVertex(Vertex v) {
		int index = this.vertices.size();
		this.vertices.add(v);
		bounds.include(v.position);
		return index;
	}
	
	public synchronized void AddTriangle(Triangle tri) {
		this.triangles.add(tri);
	}
	
	public synchronized void AddMesh(Mesh mesh) {
		int vertexOffset = this.vertices.size();
		
		for (Vertex vert : mesh.getVertices()) {
			this.AddVertex(vert);
		}
		
		for (Triangle tri : mesh.getTriangles()) {
			Triangle newTri = new Triangle(tri.vertexA + vertexOffset, tri.vertexB + vertexOffset, tri.vertexC + vertexOffset);
			this.AddTriangle(newTri);
		}
	}
	
	public synchronized void verify() {
		for (Vertex vert : vertices) {
			checkVertexInBounds(vert);
		}
		
		for (Triangle tri : triangles) {
			checkVertexIndexInRange(tri.vertexA);
			checkVertexIndexInRange(tri.vertexB);
			checkVertexIndexInRange(tri.vertexC);
		}
	}
	
	private void checkVertexInBounds(Vertex vert) {
		if (!bounds.contains(vert.position)) throw new IllegalStateException();
	}

	private void checkVertexIndexInRange(int index) {
		if (index < 0) throw new IndexOutOfBoundsException();
		if (index >= vertices.size()) throw new IndexOutOfBoundsException();
	}

	public Mesh build() {
		return new Mesh(bounds, vertices, triangles);
	}
	
}
