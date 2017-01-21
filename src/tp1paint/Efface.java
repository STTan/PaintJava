package tp1paint;

import java.awt.Color;
import java.awt.Point;

public class Efface extends TraceLibre {
	private Point pointDebut;
	private Point pointFin;
	public Efface (Point pointDebut, Point pointFin, Color saCouleur)
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
