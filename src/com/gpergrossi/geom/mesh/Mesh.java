package com.gpergrossi.geom.mesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.Vertex;

public class Mesh {
	
	private final Bounds3D bounds;
	private List<Vertex> vertices;
	private List<Triangle> triangles;
	
	public Mesh(Bounds3D bounds, List<Vertex> vertices, List<Triangle> triangles) {
		this.bounds = bounds.copy();
		this.vertices = Collections.unmodifiableList(new ArrayList<Vertex>(vertices));
		this.triangles = Collections.unmodifiableList(new ArrayList<Triangle>(triangles));
	}
	
	public List<Vertex> getVertices() {
		return this.vertices;
	}
	
	public List<Triangle> getTriangles() {
		return this.triangles;
	}
	
	public Mesh transform(Function<Point3D, Point3D> transformer) {
		System.out.println("Transforming " + vertices.size() + " vertices...");
		long time = System.currentTimeMillis();
		
		Function<Vertex, Vertex> vertexTransformer = (vert) -> {
			Point3D transformedPos = transformer.apply(vert.position);
			return new Vertex(transformedPos);
		};
		
		// Parallel transforming of all vertices
		List<Vertex> vertices = this.vertices.parallelStream().map(vertexTransformer).collect(Collectors.toList());
		
		Mesh result = new Mesh(this.bounds, vertices, this.triangles);
		long elapsed = System.currentTimeMillis() - time;
		System.out.println("Completed in " + elapsed + " ms");
		System.out.println();
		return result;
	}
	
}
