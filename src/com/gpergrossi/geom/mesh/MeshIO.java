package com.gpergrossi.geom.mesh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.Triangle;
import com.gpergrossi.geom.Vertex;

public class MeshIO {

	public static void WriteOBJ(OutputStream os, Mesh mesh) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

		bw.write("# Mesh contains:\r\n");
		bw.write("#   " + mesh.getVertices().size() + " vertices\r\n");
		bw.write("#   " + mesh.getTriangles().size() + " triangles\r\n");
		bw.write("\r\n");
		
		bw.write("# Vertices\r\n");
		for (Vertex vert : mesh.getVertices()) {
			Point3D pos = vert.position;
			bw.write("v " + pos.x + " " + pos.y + " " + pos.z + "\r\n");
		}
		bw.write("\r\n");
		
		bw.write("# Triangles\r\n");
		for (Triangle tri : mesh.getTriangles()) {
			bw.write("f " + (tri.vertexA + 1) + " " + (tri.vertexB + 1) + " " + (tri.vertexC + 1) + "\r\n");
		}
		bw.write("\r\n");
		
		bw.flush();
		
	}
	
}
