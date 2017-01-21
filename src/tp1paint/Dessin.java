package tp1paint;

import java.awt.Color;
import java.awt.Point;

public abstract class Dessin {
	private Point pointDebut;
	private Point pointFin;
	private Color saCouleur;
	public Dessin(Point pointDebut, Point pointFin, Color saCouleur)
	{
		this.pointDebut = pointDebut;
		this.pointFin = pointFin;
		this.saCouleur = saCouleur;
	}
}
