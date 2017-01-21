package tp1paint;
/*Steven Tan
 * Extra: 
 *  -Ouverture d'un fichier image dans la surface de dessin
 *  -Curseur différent pour chaque outils
 *  -Bouttons undo/redo
 *  -Toggle pour remplir les formes ou non
 *  -Boutton "Reset" qui vide le canevas de dessin
 *  -Le pot de peinture qui fonctionne comme dans paint!! :)
 * 
 */
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class CadrePaint extends JFrame {
	private JPanel CanevasDessin;
	private JPanel panelOutils;
	private JPanel panelFormes;
	private JPanel panelCouleur;
	private JPanel grilleCouleur;
	private JToggleButton toggleCrayon;
	private JToggleButton toggleEfface;
	private JToggleButton togglePipette;
	private JToggleButton togglePot;
	private JToggleButton toggleRectangle;
	private JToggleButton toggleTriangle;
	private JToggleButton toggleCercle;
	private JToggleButton toggleC1;
	private JToggleButton toggleC2;
	private JLabel[] listeIcone;
	private SurfaceDessin sd;
	private EcouteurMotion ecM;
	private EcouteurListener ecL;
	private EcouteurBtn ecB;
	private ButtonGroup groupeChoix;
	private JButton boutonUndo;
	private JButton boutonRedo;
	private JLabel labelCouleur1;
	private JLabel labelCouleur2;
	private JLabel labelPosition;
	private JToggleButton toggleRemplirForme;
	private Vector <BufferedImage>lesImages = new Vector<BufferedImage>();//vecteur qui contiendra toutes les images après chaqu trait.
	private BufferedImage image; //Une photo du canevas.
	private Graphics2D g2; 	//On va l'utiliser pour faire des dessins
	private Point pInitial, pCourant,pAncienCourant, pFin; // Coordonnées des points 
	private Color couleurPrimaire;
	private Color couleurSecondaire;
	private String mode;
	private JSlider slider; //Contrôleur pour la taille du trait.
	private BasicStroke strokeCrayon; //Stroke pour la taille et le type de trait utilisé.
	private BasicStroke strokeEfface;
	private JFileChooser fileChooser; //Ouvrir/sauvegarder une image.
	private Cursor leCurseur; //Pour curseur
	private Toolkit toolkit; //Ouvrir une image pour le curseur.
	
	//Variable globale.
	int compteur = 0;
	boolean remplirForme;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadrePaint frame = new CadrePaint();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null); //POUR LE CENTRER DANS L'ECRAN

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CadrePaint() {
		
		setTitle("PaintPro2015");
		setBounds(100, 100, 1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		//Initialiser les variables
		ecM = new EcouteurMotion();
		ecL = new EcouteurListener();
		ecB = new EcouteurBtn();
		remplirForme = false;
		couleurPrimaire = Color.BLACK;
		couleurSecondaire = Color.WHITE;
    	strokeCrayon = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    	strokeEfface = new BasicStroke(8, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER); //Il va être square pour différencier.
        
        
		//Chercher les icones du dossier puis les mettre dans un vecteur.
		listeIcone = new JLabel[12];
		//Faire un vecteur
		File dossier = new File("icones"); //creer un dossier a partir d'un dossier. Il contiendra toutes les icones.
		File[] liste = dossier.listFiles();
		for (int i = 0; i<liste.length;i++)
		{
			listeIcone[i] = new JLabel();
			listeIcone[i].setIcon(new ImageIcon(liste[i].getPath()));
		}

		sd = new SurfaceDessin();
		sd.setBounds(1,1,962, 574);
		sd.addMouseListener(ecL);
		sd.addMouseMotionListener(ecM);
		CanevasDessin = new JPanel();
		CanevasDessin.setLayout(null);
		CanevasDessin.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		CanevasDessin.setBounds(10, 146, 964, 576);
		CanevasDessin.add(sd);
		
		//Le vecteur contiendra toutes les images.
		lesImages = new Vector<BufferedImage>();
		
		 //Curseur initial
	  	toolkit = Toolkit.getDefaultToolkit();
	  	leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/crayon.gif") , new Point(5, 25), "gif");
	  	CanevasDessin.setCursor (leCurseur);

		getContentPane().add(CanevasDessin);




		
		//panelOutils---------------------------------------------------------------------------------
		panelOutils = new JPanel();
		panelOutils.setBounds(256, 11, 200, 50);
		getContentPane().add(panelOutils);
		panelOutils.setLayout(new GridLayout(0, 4, 3, 0));
		
		toggleCrayon = new JToggleButton("");
		toggleCrayon.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toggleCrayon.setLayout(new FlowLayout());
		toggleCrayon.add(listeIcone[1]);
		panelOutils.add(toggleCrayon);
		
		toggleEfface = new JToggleButton("");
		toggleEfface.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toggleEfface.setLayout(new FlowLayout());
		toggleEfface.add(listeIcone[2]);
		panelOutils.add(toggleEfface);
		
		togglePipette = new JToggleButton("");
		togglePipette.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		togglePipette.setLayout(new FlowLayout());
		togglePipette.add(listeIcone[3]);
		panelOutils.add(togglePipette);
		
		togglePot = new JToggleButton("");
		togglePot.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		togglePot.setLayout(new FlowLayout());
		togglePot.add(listeIcone[5]);

		panelOutils.add(togglePot);
		groupeChoix = new ButtonGroup();
		groupeChoix.add(toggleCrayon);
		groupeChoix.add(toggleEfface);
		groupeChoix.add(togglePipette);
		groupeChoix.add(togglePot);
		
		//panelFormes-------------------------------------------------------------------------
		panelFormes = new JPanel();
		panelFormes.setBounds(306, 73, 150, 50);
		getContentPane().add(panelFormes);
		panelFormes.setLayout(new GridLayout(0, 3, 3, 0));
		
		toggleRectangle = new JToggleButton("");
		toggleRectangle.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toggleRectangle.setLayout(new FlowLayout());
		toggleRectangle.add(listeIcone[4]);
		panelFormes.add(toggleRectangle);
		
		toggleCercle = new JToggleButton("");
		toggleCercle.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toggleCercle.setLayout(new FlowLayout());
		toggleCercle.add(listeIcone[0]);
		panelFormes.add(toggleCercle);
		
		toggleTriangle = new JToggleButton("");
		toggleTriangle.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toggleTriangle.setLayout(new FlowLayout());
		toggleTriangle.add(listeIcone[7]);
		panelFormes.add(toggleTriangle);		
		
		groupeChoix.add(toggleRectangle);
		groupeChoix.add(toggleCercle);
		groupeChoix.add(toggleTriangle);
		
		//panelCouleur ------------------------------------------------------------------
		panelCouleur = new JPanel();
		panelCouleur.setBounds(466, 11, 182, 85);
		getContentPane().add(panelCouleur);
		panelCouleur.setLayout(new GridLayout(0, 2, 8, 0));
		
		
		toggleC1 = new JToggleButton("Couleur 1");
		toggleC1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		toggleC1.setBackground(new Color(248, 248, 255));
		toggleC1.setVerticalAlignment(SwingConstants.BOTTOM);
		toggleC1.setLayout(new FlowLayout());
		labelCouleur1 = new JLabel("");
		labelCouleur1.setBorder(new LineBorder(new Color(0, 0, 0)));
		labelCouleur1.setOpaque(true);
		labelCouleur1.setBackground(couleurPrimaire);
		labelCouleur1.setPreferredSize(new Dimension(75,50));
		toggleC1.add(labelCouleur1);
		toggleC1.addActionListener(ecB);
		panelCouleur.add(toggleC1);
		
		toggleC2 = new JToggleButton("Couleur 2");
		toggleC2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		toggleC2.setBackground(new Color(248, 248, 255));
		toggleC2.setVerticalAlignment(SwingConstants.BOTTOM);
		toggleC2.setLayout(new FlowLayout());
		toggleC2.addActionListener(ecB);

		labelCouleur2 = new JLabel("");
		labelCouleur2.setBorder(new LineBorder(new Color(0, 0, 0)));
		labelCouleur2.setOpaque(true);
		labelCouleur2.setBackground(couleurSecondaire);
		labelCouleur2.setPreferredSize(new Dimension(75,50));
		toggleC2.add(labelCouleur2);
		panelCouleur.add(toggleC2);

		//Groupe pour le toggle
		ButtonGroup groupeCouleurs = new ButtonGroup();
		groupeCouleurs.add(toggleC1);
		groupeCouleurs.add(toggleC2);
		
		//grilleCouleur----------------------------------------------------------------------
		grilleCouleur = new JPanel();
		grilleCouleur.setBounds(658, 11, 316, 112);
		getContentPane().add(grilleCouleur);
		grilleCouleur.setLayout(new GridLayout(2, 5, 2, 2));
		
		JButton boutonBleu = new JButton("");
		boutonBleu.setBackground(Color.BLUE);
		grilleCouleur.add(boutonBleu);
		
		JButton boutonVert = new JButton("");
		boutonVert.setBackground(Color.GREEN);
		grilleCouleur.add(boutonVert);
		
		JButton boutonOrange = new JButton("");
		boutonOrange.setBackground(Color.ORANGE);
		grilleCouleur.add(boutonOrange);
		
		JButton boutonJaune = new JButton("");
		boutonJaune.setBackground(Color.YELLOW);
		grilleCouleur.add(boutonJaune);
		
		JButton boutonBlanc = new JButton("");
		boutonBlanc.setBackground(Color.WHITE);
		grilleCouleur.add(boutonBlanc);
		
		JButton boutonRouge = new JButton("");
		boutonRouge.setBackground(Color.RED);
		grilleCouleur.add(boutonRouge);
		
		JButton boutonMagenta = new JButton("");
		boutonMagenta.setBackground(Color.MAGENTA);
		grilleCouleur.add(boutonMagenta);
		
		JButton boutonRose = new JButton("");
		boutonRose.setBackground(Color.PINK);
		grilleCouleur.add(boutonRose);
		
		JButton boutonGris = new JButton("");
		boutonGris.setBackground(Color.GRAY);
		grilleCouleur.add(boutonGris);
		
		JButton boutonNoir = new JButton("");
		boutonNoir.setBackground(Color.BLACK);
		grilleCouleur.add(boutonNoir);
		
		//Tout le code répétitif a était mis dans une boucle for pour alléger le code.
		for (int i = 0; i< grilleCouleur.getComponentCount();i++)
		{	
			JButton leBouton = (JButton)grilleCouleur.getComponent(i);
			leBouton.setOpaque(true);
			leBouton.setForeground(Color.BLACK);
			leBouton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			leBouton.setBackground(leBouton.getBackground());
			leBouton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					//Selon le toggle actif, ajouter la couleur dans le bon label.
					if (toggleC1.isSelected())
					{
						couleurPrimaire = leBouton.getBackground();
						labelCouleur1.setBackground(couleurPrimaire);
						g2.setColor(couleurPrimaire);
					}
					else 
					{
						couleurSecondaire = leBouton.getBackground();
						labelCouleur2.setBackground(couleurSecondaire);
						g2.setColor(couleurPrimaire);
					}
				} });;
				
		}
//Le slider pour l'épaisseur d'un trait ------------------------------------------------
		slider = new JSlider(1,64,8);
		slider.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Taille du trait", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		slider.setBounds(10, 83, 200, 39);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
		      public void stateChanged(ChangeEvent event) {
	            strokeCrayon = new BasicStroke(slider.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);//Trait pour crayon
	            strokeEfface = new BasicStroke(slider.getValue(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER); //Trait pour efface.

		      }
		    });
		getContentPane().add(slider);

		toggleCrayon.addActionListener(ecB);
		toggleEfface.addActionListener(ecB);
		togglePipette.addActionListener(ecB);
		togglePot.addActionListener(ecB);
		toggleRectangle.addActionListener(ecB);
		toggleTriangle.addActionListener(ecB);
		toggleCercle.addActionListener(ecB);
		
		
		JPanel panelOption = new JPanel();
		panelOption.setBounds(10, 11, 200, 50);
		getContentPane().add(panelOption);
		panelOption.setLayout(new GridLayout(1, 4, 3, 0));
		
		JButton boutonSauvegarde = new JButton("");
		panelOption.add(boutonSauvegarde);
		boutonSauvegarde.setLayout(new FlowLayout());

//boutonSauvegarde----------------------------------------------------------------------------------

		boutonSauvegarde.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser = new JFileChooser();
				File fichier; // notre fichier qui contiendra l’image
				fileChooser.setDialogTitle("Sauvegarder votre oeuvre d'art."); // lui donner un titre
				//indiquer que c’est pour sauvegarder
				int selection = fileChooser.showSaveDialog (CadrePaint.this);
				//a-t-il appuyé sur enregistrer ? 
				if ( selection == JFileChooser.APPROVE_OPTION )
				{
				fichier = fileChooser.getSelectedFile(); //contient le nom qu’on lui a donné dans le JFileChooser
				try {
					//Récuperer l'image pour sauvegarder.
					ImageIO.write ( recupererImage(sd), "png", new File( fichier.getAbsolutePath()+ ".png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		boutonSauvegarde.add(listeIcone[6]);
		
		JButton boutonOpen = new JButton("");
		boutonOpen.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		boutonOpen.setLayout(new FlowLayout());
		boutonOpen.add(listeIcone[9]);

		
//boutonUndo----------------------------------------------------------------------------------	
		boutonOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser = new JFileChooser();
				File fichier; // notre fichier qui contiendra l’image
				fileChooser.setDialogTitle("Ouvrir un dessin."); // lui donner un titre
				//indiquer que c’est pour sauvegarder
				int selection = fileChooser.showOpenDialog(CadrePaint.this);//Pour ouvrir.
				//Faut confirmer....
				if ( selection == JFileChooser.APPROVE_OPTION )
				{
				fichier = fileChooser.getSelectedFile(); //contient le nom qu’on lui a donné dans le JFileChooser
				try {
					
					BufferedImage dessinOuvert = ImageIO.read(fichier); //Lire le fichier choisi
					g2.drawImage(dessinOuvert, 0, 0, null); //dessiner le fichier choisi.
					repaint();
					compteur++;
		            lesImages.setSize(compteur); //Pour undo/redo fonctionne
		            lesImages.add(recupererImage(sd)); //ajouter dans le vecteur.

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}

		    	
			}
		});
		panelOption.add(boutonOpen);
		
		boutonUndo = new JButton("");
		boutonUndo.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		boutonUndo.setLayout(new FlowLayout());
		boutonUndo.add(listeIcone[11]);	
		
		
//boutonUndo----------------------------------------------------------------------------------
		boutonUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		    	if (compteur>1)
		    	{
		    		//Si on est à la dernière image, faut reculer de 2 dans le compteur.
		    		//Ex. si on est à jour, le compteur est = au size du vecteur, mais la dernière image à -2.
		    		if(compteur == lesImages.size())
		    			compteur = compteur-2;
		    		else 
		    			compteur--;
				    g2.setColor(Color.white);
				    g2.fillRect(0, 0, getSize().width, getSize().height);
				    repaint();
	            g2.drawImage(lesImages.elementAt(compteur), 0, 0, null); //Dessiner l'image précédente.
	            repaint();

		    	}
			}
		});

		boutonRedo = new JButton("");
		boutonRedo.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		boutonRedo.setLayout(new FlowLayout());
		boutonRedo.add(listeIcone[10]);	

//boutonRedo----------------------------------------------------------------------------------	
			boutonRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (compteur<lesImages.size()) //On peut pas redo par-dessus le size du vecteur.
		    	{
		    		if(compteur+1 < lesImages.size())
		    			compteur++;
	            g2.drawImage(lesImages.elementAt(compteur), 0, 0, null);
	            repaint();

		    	}
		    	
			}
		});

		panelOption.add(boutonUndo);
		panelOption.add(boutonRedo);

        labelPosition = new JLabel("Position: ");
        labelPosition.setBounds(10, 733, 173, 17);
        getContentPane().add(labelPosition);
        
        toggleRemplirForme = new JToggleButton("");
        toggleRemplirForme.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        toggleRemplirForme.setBounds(256, 73, 47, 50);
        toggleRemplirForme.setLayout(new FlowLayout());
        toggleRemplirForme.add(listeIcone[8]);

        JButton boutonRecommencer = new JButton("Vider");
        boutonRecommencer.setBounds(885, 733, 89, 23);
//boutonRecommencer-------------------------------------------------------------------
        boutonRecommencer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 g2.setColor(Color.white);//Commencer avec une canevas "vide" blanc.
				 g2.fillRect(0, 0, getSize().width, getSize().height);
			     compteur=1; 
			     lesImages.add(recupererImage(sd)); //Ajouter le canevas "vide" blanc en tant que première photo dans le vecteur.	    
		    	lesImages.setSize(compteur); //Vecteur recommence à 1 image vide.
		    	repaint();
		   
			}
		});
        getContentPane().add(boutonRecommencer);
        
//toggleRemplirForme----------------------------------------------------------------------------------
        toggleRemplirForme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Changer l'état si on veut ou non remplir les formes.
				if(toggleRemplirForme.isSelected())
					remplirForme = true;
				else
					remplirForme = false;
			}
		});

        getContentPane().add(toggleRemplirForme);

       
        //Faire en sorte que le canevas soit blanc dès le début.
        image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB );//Créer une photo
    	g2 = (Graphics2D) image.getGraphics(); //Pour dessiner sur le canevas
	    g2.setColor(Color.white);//Commencer avec une canevas "vide" blanc.
	    g2.fillRect(0, 0, getSize().width, getSize().height);
        compteur++; 
        lesImages.add(recupererImage(sd)); //Ajouter le canevas "vide" blanc en tant que première photo dans le vecteur.	    
		//Initialiser la couleur 1 et le crayon dès qu'on ouvre le programme.
        mode = "TraceLibre"; 
		toggleC1.setSelected(true);
        toggleCrayon.setSelected(true);

	    repaint();
	}

	//1# créer la surface de dessin
		private class SurfaceDessin extends JPanel
		{
			protected void paintComponent (Graphics g)
			{
			//important, quand on redéfinit une méthode, on cherche la méthode de la superclasse en question.
			super.paintComponent(g);	
            g.drawImage(image, 0, 0, null); //dessuber l'image.
			}

		}
//Ecouteur pour dessiner -------------------------------------------------------------------------
		private class EcouteurMotion implements MouseMotionListener
		{

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				//Trouver les coordonnées.
				labelPosition.setText("Position: "+ e.getX() + " : " + e.getY());
		        pCourant = new Point(e.getX(), e.getY());
				
		        if (mode =="TraceLibre")//Tracer une ligne antre chaque point.
		        {//Créer un objet crayon.
					Crayon cr1 = new Crayon(pCourant, pAncienCourant, couleurPrimaire);
					g2.drawLine(cr1.getPointDebut().x, cr1.getPointDebut().y, cr1.getpointFin().x, cr1.getpointFin().y);
		        }
				else if (mode =="Efface")//Tracer une ligne antre chaque point.
				{//Créer un objet efface.
					Efface ef1 = new Efface(pCourant, pAncienCourant, couleurPrimaire);
					g2.drawLine(ef1.getPointDebut().x, ef1.getPointDebut().y, ef1.getpointFin().x, ef1.getpointFin().y);
				}
				else if (mode =="Rectangle")
					creerRectangle();

				else if (mode =="Cercle")
					creerCercle();
				
				else if (mode =="Triangle")
					creerTriangle();
			
			    repaint();
	            //Garder en mémoire les points.
	            pAncienCourant = pCourant;
	            
	            
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				//Afficher les coordonnées de la souris en bas.
				labelPosition.setText("Position: "+ e.getX() + " : " + e.getY());
				
			}
		}

//ÉcouteurListener -------------------------------------------------------------------------
		private class EcouteurListener implements MouseListener
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				pInitial = new Point(e.getX(),e.getY());
				pAncienCourant = new Point(e.getX(),e.getY()); //Garder en mémoire le point initial.
		        g2.setStroke(strokeCrayon);
		        
		        //Dépendamment du clique choisi par l'utilisateur, changer les couleurs utilisées.
		        if (e.getButton()==e.BUTTON3) //boutton droite
		        {
		        	couleurPrimaire = labelCouleur2.getBackground();
		        	couleurSecondaire = labelCouleur1.getBackground();
		        }
		        else // (e.getButton()==e.BUTTON1) et le reste...
		        {
		        	couleurPrimaire = labelCouleur1.getBackground();
		        	couleurSecondaire = labelCouleur2.getBackground();
		        }
		        
				//Inverser les couleurs encore une autre fois si c'est l'autre toggle qui est choisi.
				if (toggleC2.isSelected())
				{
					Color reserve = couleurSecondaire;
		        	couleurSecondaire = couleurPrimaire;
		        	couleurPrimaire = reserve;
				}
				
		        //En mode pipette, le clique détermine dans quelle label on va conserver la couleur.
				if (mode =="Pipette")
				{
			        if (e.getButton()==e.BUTTON1)
			        {
						couleurPrimaire = new Color (image.getRGB(pInitial.x, pInitial.y));
						labelCouleur1.setBackground(couleurPrimaire);
			        }
			        else //(e.getButton()==e.BUTTON3)
			        {
						couleurSecondaire = new Color (image.getRGB(pInitial.x, pInitial.y));
						labelCouleur2.setBackground(couleurSecondaire);
			        }
				}
				else if (mode =="Pot") 
				{
					potPeinture(image.getRGB(pInitial.x, pInitial.y), pInitial);
				}
				else if (mode =="TraceLibre")
				{
					g2.setColor(couleurPrimaire);
					//Petite touche pour qu'un clique rapide puisse faire un point!
					g2.fillOval(pInitial.x-slider.getValue()/2, pInitial.y-slider.getValue()/2, slider.getValue(), slider.getValue());
					repaint();
				}
				else if (mode =="Efface")
				{
			        g2.setStroke(strokeEfface); //pas le meme stroke que le crayon.
					g2.setColor(labelCouleur2.getBackground());//Toujours la couleur du toggleCouleur2.
					//Petite touche pour qu'un clique rapide puisse faire un point!
					g2.fillOval(pInitial.x-slider.getValue()/2, pInitial.y-slider.getValue()/2, slider.getValue(), slider.getValue());
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
	              
	              compteur++; //Il y a une image de plus dans le vecteur!
	              lesImages.setSize(compteur); //Le size de vecteur d'image est toujours égal à la dernière image.
	              lesImages.add(recupererImage(sd));//Ajouter la dernière image dans le vecteur.
	              pFin = new Point(e.getX(), e.getY());

	              if (mode =="Pipette") //Si c'est on est sur le mode pipette, retourner en mode crayon à la fin.
	              {
				        toggleCrayon.setSelected(true);
				        mode = "TraceLibre";
						leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/crayon.gif") , new Point(5, 25), "img");
						CanevasDessin.setCursor(leCurseur);
	              }
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		}
//Écouteur bouton ----------------------------------------------------------------
		private class EcouteurBtn implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Ecouteur nécessaire si je veux que le curseur change si on appuie sur le boutton.
				if(toggleCrayon.isSelected())
				{		
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/crayon.gif") , new Point(5, 25), "gif");
					mode ="TraceLibre";
				}
				else if (toggleEfface.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/efface.gif") , new Point(5, 25), "gif");
					mode ="Efface";
				}
				else if (togglePipette.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/pipette.gif") , new Point(5, 25), "gif");
					mode ="Pipette";
				}
				else if (togglePot.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/remplissage.gif") , new Point(5, 25), "gif");
					mode ="Pot";
				}
				else if (toggleRectangle.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/rectangle.gif") , new Point(5, 25), "gif");
					mode ="Rectangle";
				}
				else if (toggleTriangle.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/triangle.gif") , new Point(5, 25), "gif");
					mode ="Triangle";
				}
				else if (toggleCercle.isSelected())
				{
					leCurseur = toolkit.createCustomCursor(toolkit.getImage("icones/cercle.gif") , new Point(5, 25), "gif");
					mode ="Cercle";
				}
				CanevasDessin.setCursor (leCurseur);

			}
		}
	  
		
	    public BufferedImage recupererImage(JPanel surface)
	    {
	    	Dimension size = surface.getSize();
	    	BufferedImage imageCopie = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
	    	Graphics2D g2 = imageCopie.createGraphics();
	    	surface.paint(g2);
	    	return imageCopie;
	    }
	    
	    //Pour dessiner un rectangle, les coordonnées du point initial est toujours l'opposé
	    //du point de fin.
		public void creerRectangle()
		{
			
			//Redessinez l'ancienne image à chaque 'mousedrag' pour ne pas accumuler les dessins.
			if (compteur ==1)//Pour éviter un bug, car l'index sera out of bound.
					g2.drawImage(lesImages.elementAt(compteur-1), 0, 0, null);
				else
					g2.drawImage(lesImages.elementAt(compteur), 0, 0, null);
			//Créer un rectangle à chaque fois.
			MonRectangle r1 = new MonRectangle(pInitial, pCourant, couleurPrimaire, couleurSecondaire, remplirForme);
				
				//Si on clique sur le boutton remplir forme, dessiner la forme remplie aussi.
				if (remplirForme)
				{
				//Remplir un rectangle.
				g2.setColor(couleurSecondaire);
				g2.fillRect(r1.getxInitial(), r1.getyInitial(), r1.getLongueur(), r1.getHauteur());
				}
				//Dessiner un rectangle normal.
				g2.setColor(couleurPrimaire);
				g2.drawRect(r1.getxInitial(), r1.getyInitial(), r1.getLongueur(), r1.getHauteur());

			
		}
		//Pour dessiner un cercle, exactement le même algorithme que pour le rectangle.
		public void creerCercle()
		{
			//Créer un objet cercle!
			Cercle c1 = new Cercle(pInitial, pCourant, couleurPrimaire, couleurSecondaire, remplirForme);

			//Redessinez l'ancienne image à chaque 'mousedrag' pour ne pas accumuler les dessins.
			if (compteur ==1)//Pour éviter un bug, car l'index sera out of bound.
				g2.drawImage(lesImages.elementAt(compteur-1), 0, 0, null);
			else
				g2.drawImage(lesImages.elementAt(compteur), 0, 0, null);

			//Si on a cliqué le boutton "remplir forme", dessiner une autre forme aussi.
			if (remplirForme)
			{//Dessiner un cercle rempli.
			g2.setColor(couleurSecondaire); //Couleur secondaire
			g2.fillOval(c1.getxInitial(), c1.getyInitial(), c1.getLongueur(), c1.getHauteur());
			}
			//Dessiner un cercle normal.
			g2.setColor(couleurPrimaire);
			g2.drawOval(c1.getxInitial(), c1.getyInitial(), c1.getLongueur(), c1.getHauteur());

		}
		//Pour dessiner le triangle, on a besoin de connaître les 3 sommets.
		public void creerTriangle()
		{
			//Créer un objet triangle!
			Triangle t1 = new Triangle(pInitial, pCourant, couleurPrimaire, couleurSecondaire, remplirForme);
			
			//Redessinez l'ancienne image à chaque 'mousedrag' pour ne pas accumuler les dessins.
			if (compteur ==1)//Pour éviter un bug, car l'index sera out of bound.
				g2.drawImage(lesImages.elementAt(compteur-1), 0, 0, null);
			else
				g2.drawImage(lesImages.elementAt(compteur), 0, 0, null);
			
			
			//Si l'utilisateur a cliqué sur "remplir forme"
			if (remplirForme)
			{//Dessiner un triangle rempli
			g2.setColor(couleurSecondaire);
			g2.fillPolygon(t1.getTabx(), t1.getTaby(), 3);
			}
			//Dessiner un triangle normal.
			g2.setColor(couleurPrimaire);
			g2.drawPolygon(t1.getTabx(), t1.getTaby(), 3);
		}
		
		//Méthode comme dans paint. Changer la couleur du point et des points adjacents de la même couleur
		//avec la couleur selectionnée.
		public void potPeinture(int leRGB,Point pointInitial)
		  {
			  //Si la couleur du point initial est déjà de la couleur primaire, l'outil pot ne fait rien.
			  if (image.getRGB(pointInitial.x, pointInitial.y)==couleurPrimaire.getRGB())
				  return;
			  else //Sinon, remplir les pixels adjacents au point de la même couleur.
			  {
				//Pour alléger le code, garder en mémoire 
				int hauteurCanevas = CanevasDessin.getHeight();
				int longueurCanevas = CanevasDessin.getWidth();
			  
				//Créer une table qui contient toutes les couleurs de chaque point du canevas.
				int[][] tabPoints = new int[hauteurCanevas][longueurCanevas];
				for (int i = 0; i<hauteurCanevas;i++)
					for (int j = 0; j<longueurCanevas; j++)
						tabPoints[i][j] = image.getRGB(j,i);
					
				//Créer une stack qui contiendra les points à modifier.
			    Stack<Point> stackPointsAModifier = new Stack<Point>();
			    //Ajouter le point initial. À partir de ce point, déterminer les autres points de la même
			    //couleur qui sont adjacents et les ajouter dans le stack.
			    stackPointsAModifier.push(pointInitial);
			    
			    //Tant qu'il y a toujours des points à modifier, continuer.
			    while (stackPointsAModifier.size() > 0)
			    {
			    	//Travailler avec le point par dessus le stack
			    	Point pointPop = stackPointsAModifier.pop();
			    	//Prendre en mémoire ses coordonnées sur le canevas.
			    	int x = pointPop.x;
			    	int y = pointPop.y;
			    	
			    	//Si ce n'est pas un point valide... ne pas travailler avec ce point. 
			    	if (y < 0 || y > hauteurCanevas - 1 || x < 0 || x > longueurCanevas - 1)
			    		continue;
			    	
			    	//Si la couleur du point choisi fait partie de la chaîne, donc travailler avec.
			    	if (tabPoints[y][x] == leRGB)
			    	{
			    		//changer la couleur du point courant dans la tab.
			    		tabPoints[y][x] = couleurPrimaire.getRGB();
						//Ajouter dans le stack les points adjacent qu'on peut potentiellement travailler avec.
			    		stackPointsAModifier.push(new Point(x + 1, y)); //Ajouter le point adjacent droite.
			    		stackPointsAModifier.push(new Point(x - 1, y)); //Ajouter le point adjacent gauche.
			    		stackPointsAModifier.push(new Point(x, y + 1)); //Ajouter le point adjacent bas.
			    		stackPointsAModifier.push(new Point(x, y - 1)); //Ajouter le point adjacent haut.
			    	}
			    }
			    
			    //Changer la couleur des points à partir de la tab si elles sont modifiées.
				for (int i = 0; i<hauteurCanevas;i++)
					for (int j = 0; j<longueurCanevas; j++)
						image.setRGB(j,i,tabPoints[i][j]);
					
				repaint();
			}
		  }
}
