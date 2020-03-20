package com.gpergrossi.geom.shapes;

import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.Vertex;
import com.gpergrossi.geom.mesh.Mesh;
import com.gpergrossi.geom.mesh.MeshBuilder;
import com.gpergrossi.util.LazySingleton;

public class Icosahedron {
	
	public static final double Phi = (1.0 + Math.sqrt(5)) / 2.0; // Golden ratio
	
	private volatile static LazySingleton<Icosahedron> singleton = new LazySingleton<>(() -> new Icosahedron());
	
	public static Icosahedron getInstance() {
		return singleton.getInstance();
	}
	
	
	private Mesh mesh;
	
	/**
	 * Constructs an Icosahedron mesh centered at (0, 0, 0) with a radius of 1.
	 * Only vertex positions are generated. There are 12 vertices (shared maximally) and 20 triangles.
	 */
	private Icosahedron() {
		MeshBuilder builder = new MeshBuilder();
		
		// See diagram "documentation/Icosahedron-golden-rectangles.svg"
		
		// Blue rectangle
		builder.AddVertex(new Vertex(new Point3D(0, Phi, 1).normalize()));  // Normalize coordinates to get radius of 1
		builder.AddVertex(new Vertex(new Point3D(0, Phi, -1).normalize()));
		builder.AddVertex(new Vertex(new Point3D(0, -Phi, -1).normalize()));
		builder.AddVertex(new Vertex(new Point3D(0, -Phi, 1).normalize()));
		
		// Light Green rectangle
		builder.AddVertex(new Vertex(new Point3D(1, 0, Phi).normalize()));
		builder.AddVertex(new Vertex(new Point3D(1, 0, -Phi).normalize()));
		builder.AddVertex(new Vertex(new Point3D(-1, 0, -Phi).normalize()));
		builder.AddVertex(new Vertex(new Point3D(-1, 0, Phi).normalize()));
		
		// Dark Green rectangle
		builder.AddVertex(new Vertex(new Point3D(Phi, 1, 0).normalize()));
		builder.AddVertex(new Vertex(new Point3D(Phi, -1, 0).normalize()));
		builder.AddVertex(new Vertex(new Point3D(-Phi, -1, 0).normalize()));
		builder.AddVertex(new Vertex(new Point3D(-Phi, 1, 0).normalize()));
		
		// 5 triangles around (0, Phi, 1)
		builder.AddTriangle(new Triangle(0, 1, 11));
		builder.AddTriangle(new Triangle(0, 11, 7));
		builder.AddTriangle(new Triangle(0, 7, 4));
		builder.AddTriangle(new Triangle(0, 4, 8));
		builder.AddTriangle(new Triangle(0, 8, 1));

		// 5 triangles around (0, -Phi, -1)
		builder.AddTriangle(new Triangle(2, 3, 10));
		builder.AddTriangle(new Triangle(2, 10, 6));
		builder.AddTriangle(new Triangle(2, 6, 5));
		builder.AddTriangle(new Triangle(2, 5, 9));
		builder.AddTriangle(new Triangle(2, 9, 3));
		
		// 10 triangles around the middle
		builder.AddTriangle(new Triangle(1, 5, 6));
		builder.AddTriangle(new Triangle(1, 6, 11));
		builder.AddTriangle(new Triangle(11, 6, 10));
		builder.AddTriangle(new Triangle(11, 10, 7));
		builder.AddTriangle(new Triangle(7, 10, 3));
		builder.AddTriangle(new Triangle(7, 3, 4));
		builder.AddTriangle(new Triangle(4, 3, 9));
		builder.AddTriangle(new Triangle(4, 9, 8));
		builder.AddTriangle(new Triangle(8, 9, 5));
		builder.AddTriangle(new Triangle(8, 5, 1));
		
		this.mesh = builder.build();
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}
	
}
