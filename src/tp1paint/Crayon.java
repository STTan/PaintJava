package tp1paint;

import java.awt.Color;
import java.awt.Point;

public class Crayon extends TraceLibre {
	private Point pointDebut;
	private Point pointFin;
	public Crayon (Point pointDebut, Point pointFin, Color saCouleur)
	{
		super(pointDebut,pointFin,saCouleur); //Superclasse est TraceLibre
		this.pointDebut = pointDebut;
		this.pointFin = pointFin;
	}


public Point getPointDebut() {
	return pointDebut;
}

public Point getpointFin() {
	return pointFin;
}
}

