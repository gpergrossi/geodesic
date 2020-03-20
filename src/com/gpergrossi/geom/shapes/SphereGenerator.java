package com.gpergrossi.geom.shapes;

import com.gpergrossi.geom.Edge;
import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.mesh.Mesh;
import com.gpergrossi.geom.mesh.subdivide.MeshSubdivider;

public class SphereGenerator {

    public static Mesh generateGeoSphere(Point3D center, double radius, double maxDistanceBetweenVertices) {
    	return generateGeoSphere(center, radius, maxDistanceBetweenVertices, false);
    }
    
    public static Mesh generateGeoSphere(Point3D center, double radius, double maxDistanceBetweenVertices, boolean skipFaces) {
    	
		Mesh icosahedron = Icosahedron.getInstance().getMesh();
		MeshSubdivider ms = new MeshSubdivider(icosahedron);
		ms.skipFaces = skipFaces;
		
		double edgeLength = radius * 1.051462;
		double dilationFactor = 1.25; // How much the longest edges grow by when blown up to a sphere.
		double segments = edgeLength * dilationFactor / maxDistanceBetweenVertices;
		Mesh mesh = ms.buildSubdividedMesh((int) Math.ceil(segments));
		
		mesh = mesh.transform((pt) -> {
			pt = pt.normalize();
			pt = new Point3D(center.x + pt.x * radius, center.y + pt.y * radius, center.z + pt.z * radius);
			return pt;
		});

		//computeStats(mesh, maxDistanceBetweenVertices);
    	
		return mesh;
    }

	@SuppressWarnings("unused")
	private static void computeStats(Mesh mesh, double maxDist) {
		System.out.println("Statistics: ");
		double maxEdgeLength = 0;
		double minEdgeLength = Double.MAX_VALUE;
		for (Triangle tri : mesh.getTriangles()) {
			for (Edge edge : tri.getEdges()) {
				Point3D posA = mesh.getVertices().get(edge.vertexA).position;
				Point3D posB = mesh.getVertices().get(edge.vertexB).position;
				double dist = posA.distanceTo(posB);
				maxEdgeLength = Math.max(maxEdgeLength, dist);
				minEdgeLength = Math.min(minEdgeLength, dist);
			}
		}

		System.out.println("  desired maxDist = " + maxDist);
		System.out.println("  min edge length = " + minEdgeLength);
		System.out.println("  max edge length = " + maxEdgeLength);
		System.out.println("  ratio = " + (maxEdgeLength / minEdgeLength));
	}
    
}
