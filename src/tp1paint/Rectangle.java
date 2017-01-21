package tp1paint;

import java.awt.Color;
import java.awt.Point;

public class Rectangle extends Forme {
	private int longueur;
	private int hauteur;
	private int xInitial;
	private int yInitial;

	public Rectangle (Point pointDebut, Point pointFin, Color saCouleur, Color saCouleur2, boolean formeRempli)
	{
		super(pointDebut, pointFin, saCouleur, saCouleur2, formeRempli);//Superclasse est Forme
		
		this.longueur =Math.abs(pointFin.x-pointDebut.x); //Sa longueur dans les axes des x.
		this.hauteur =Math.abs(pointFin.y-pointDebut.y); //Sa hauteur dans les axes des y.
		
		
		//Cadrant en bas, à droite
		if (pointDebut.x>=pointFin.x && pointDebut.y>=pointFin.y)
		{
			this.xInitial = pointFin.x;
			this.yInitial = pointFin.y;
		}
		//Cadrant en bas, à gauche
		else if (pointDebut.x<pointFin.x && pointDebut.y>=pointFin.y)
		{
			this.xInitial = pointFin.x- this.longueur;
			this.yInitial = pointFin.y;
		}
		//Cadrant en haut, à droite
		else if (pointDebut.x>=pointFin.x && pointDebut.y<pointFin.y)
		{
			this.xInitial = pointFin.x;
			this.yInitial = pointFin.y- this.hauteur;
		}
		//Cadrant en haut, à gauche
		else
		{
			this.xInitial = pointFin.x- this.longueur;
			this.yInitial = pointFin.y - this.hauteur;
		}
	}

	public int getLongueur() {
		return longueur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public int getxInitial() {
		return xInitial;
	}

	public int getyInitial() {
		return yInitial;
	}

}