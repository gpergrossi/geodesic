package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.mesh.Mesh;
import com.gpergrossi.geom.mesh.MeshBuilder;
import com.gpergrossi.geom.mesh.MeshIO;
import com.gpergrossi.geom.shapes.SphereGenerator;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		double detail = 0.1;
		
		MeshBuilder mb = new MeshBuilder();
		
		Mesh mesh0 = SphereGenerator.generateGeoSphere(new Point3D(0.0, -0.0638745677, 0.0), 0.73, detail, false);
		Mesh mesh1 = SphereGenerator.generateGeoSphere(new Point3D(0.7804534796, 0.5065043158, 0.0), 0.38, detail, false);
		Mesh mesh2 = SphereGenerator.generateGeoSphere(new Point3D(-0.7804534796, 0.5065043158, 0.0), 0.38, detail, false);
		
		mb.AddMesh(mesh0);
		mb.AddMesh(mesh1);
		mb.AddMesh(mesh2);
		
		MeshIO.WriteOBJ(new FileOutputStream(new File("molecule.obj")), mb.build());
	}
	
}
