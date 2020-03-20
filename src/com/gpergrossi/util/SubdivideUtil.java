package com.gpergrossi.util;

import java.util.ArrayList;
import java.util.List;

import com.gpergrossi.geom.Point3D;
import com.gpergrossi.geom.Vertex;

public class SubdivideUtil {
	
	public static List<Point3D> interpolatePoints(Point3D start, Point3D end, int division) {
		if (division < 1) throw new IllegalArgumentException();
		
		List<Point3D> result = new ArrayList<>();
		
		double dx = (end.x - start.x) / division;
		double dy = (end.y - start.y) / division;
		double dz = (end.z - start.z) / division;
		
		for (int i = 0; i <= division; i++) {
			result.add(new Point3D(start.x + dx * i, start.y + dy * i, start.z + dz * i));
		}
		
		return result;
	}
	
	public static List<Vertex> interpolateVertices(Vertex start, Vertex end, int division) {
		if (division < 1) throw new IllegalArgumentException();
		
		List<Vertex> result = new ArrayList<>();
		
		result.add(start);
		List<Point3D> points = interpolatePoints(start.position, end.position, division);
		for (int i = 1; i < division; i++) {
			result.add(new Vertex(points.get(i)));
		}
		result.add(end);
		
		return result;
	}

}
