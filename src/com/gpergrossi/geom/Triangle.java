package com.gpergrossi.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A triangles is a list of three vertex indices.
 */
public class Triangle {

	public final int vertexA;
	public final int vertexB;
	public final int vertexC;
	
	public Triangle(int a, int b, int c) {
		this.vertexA = a;
		this.vertexB = b;
		this.vertexC = c;
	}
	
	public Iterable<Edge> getEdges() {
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge(vertexA, vertexB));
		edges.add(new Edge(vertexB, vertexC));
		edges.add(new Edge(vertexC, vertexA));
		return Collections.unmodifiableList(edges);
	}
	
}