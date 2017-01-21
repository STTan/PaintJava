package tp1paint;

import java.awt.Color;
import java.awt.Point;

public abstract class Forme extends Dessin {
	private boolean formeRempli; //Si c'est une forme remplie
	private Color saCouleur2; //Garder si besoin, la couleur 2 si on veut une forme remplie.
	
	public Forme (Point pointDebut, Point pointFin, Color saCouleur,Color saCouleur2, boolean formeRempli)
	{
		super(pointDebut,pointFin,saCouleur); //Superclasse est Dessin
		this.formeRempli = formeRempli;
	}

}
