package tp1paint;

import java.awt.Color;
import java.awt.Point;

public class Triangle extends Forme {
	private Point sommet;
	private Point gauche;
	private Point droite;
	private int[] tabx;
	private int[] taby;
	public Triangle (Point pointDebut, Point pointFin, Color saCouleur, Color saCouleur2,boolean formeRempli)
	{
		super(pointDebut,pointFin,saCouleur, saCouleur2, formeRempli); //Superclasse est Forme
		this.sommet = new Point();
		this.gauche = new Point();
		this.droite = new Point();
		//Cadrant en bas, à droite
		if (pointFin.x>=pointDebut.x && pointFin.y>=pointDebut.y)
		{
			//Dans Paint, le point droite suit la souris, la hauteur du sommet est au "y" initial
			// et le "x" du point gauche se retrouve au "x" initial.
			this.sommet.setLocation(pointDebut.x+Math.abs((pointFin.x- pointDebut.x))/2, pointDebut.y);
			this.gauche.setLocation(pointDebut.x, pointFin.y);
			this.droite.setLocation(pointFin.x, pointFin.y);
		}
		//Cadrant en bas, à gauche
		else if (pointFin.x<pointDebut.x && pointFin.y>=pointDebut.y)
		{
			//Dans Paint, le point gauche suit la souris, la hauteur du sommet est au "y" initial
			// et le "x" du point droite se retrouve au "x" initial.
			this.sommet.setLocation(pointDebut.x-Math.abs((pointFin.x- pointDebut.x))/2, pointDebut.y);
			this.droite.setLocation(pointDebut.x, pointFin.y);
			this.gauche.setLocation(pointFin.x, pointFin.y);
		}
		//Cadrant en haut, à droite
		else if (pointFin.x>=pointDebut.x && pointFin.y<pointDebut.y)
		{
			//Dans paint, le point gauche est le point initial, la hauteur du sommet est au "y"
			// de la souris et le "x" du point droite est au "x" de la souris.
			this.sommet.setLocation(pointDebut.x+Math.abs((pointFin.x- pointDebut.x))/2, pointFin.y);
			this.gauche.setLocation(pointDebut.x, pointDebut.y);
			this.droite.setLocation(pointFin.x, pointDebut.y);
		}
		//Cadrant en haut, à gauche
		else
		{
			//Dans paint, le point droite est le point initial, la hauteur du sommet est au "y"
			// de la souris et le "x" du point gauche est au "x" de la souris.
			this.sommet.setLocation(pointDebut.x-Math.abs((pointFin.x- pointDebut.x))/2, pointFin.y);
			this.droite.setLocation(pointDebut.x, pointDebut.y);
			this.gauche.setLocation(pointFin.x, pointDebut.y);
		}
		//Tab de points
		this.tabx = new int[]{this.gauche.x,this.sommet.x,this.droite.x};
		this.taby = new int[]{this.gauche.y,this.sommet.y,this.droite.y};

	}
	public int[] getTabx() {
		return tabx;
	}
	public int[] getTaby() {
		return taby;
	}
}