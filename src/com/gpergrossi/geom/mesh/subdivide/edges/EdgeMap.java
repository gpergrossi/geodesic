package com.gpergrossi.geom.mesh.subdivide.edges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The edge map is used to lookup and store unique edges when working on a mesh.
 */
public class EdgeMap {
	Map<Integer, Map<Integer, ForwardEdge>> sharedEdges;
	
	public EdgeMap() {
		this.sharedEdges = new HashMap<>();
	}
	
	public boolean containsEdge(int lesserVertex, int greaterVertex) {
		if (!sharedEdges.containsKey(lesserVertex)) return false;
		if (!sharedEdges.get(lesserVertex).containsKey(greaterVertex)) return false;
		return true;
	}

	public void putEdge(int lesserVertex, int greaterVertex, ForwardEdge forwardEdge) {
		if (!sharedEdges.containsKey(lesserVertex)) {
			sharedEdges.put(lesserVertex, new HashMap<>());		
		}
		if (!sharedEdges.get(lesserVertex).containsKey(greaterVertex)) {
			sharedEdges.get(lesserVertex).put(greaterVertex, forwardEdge);
		}
	}

	public ForwardEdge getEdge(int lesserVertex, int greaterVertex) {
		return sharedEdges.get(lesserVertex).get(greaterVertex);
	}

	public List<ForwardEdge> getForwardEdges() {
		List<ForwardEdge> forwardEdges = new ArrayList<>();
		for (Map<Integer, ForwardEdge> map : this.sharedEdges.values()) {
			for (ForwardEdge edge : map.values()) {
				forwardEdges.add(edge);
			}
		}
		return forwardEdges;
	}
}
