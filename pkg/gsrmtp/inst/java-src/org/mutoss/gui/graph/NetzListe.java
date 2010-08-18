package org.mutoss.gui.graph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Diese Klasse (extends JPanel) stellt einen Graphen graphisch dar.
 */

public class NetzListe extends JPanel implements MouseMotionListener, MouseListener {

	private static final Log logger = LogFactory.getLog(NetzListe.class);
	RunnableAlgorithm algo;
	int drag = -1;
	Node firstVertex;

	boolean firstVertexSelected = false;
	public Vector<Edge> edges = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();

	boolean started = false;

	JLabel statusBar;

	protected VS vs;

	/**
	 * Konstruktor der die NetzListe erzeugt
	 * 
	 * @param document
	 *            Ein org.jdom.Document, das den Plan enthält.
	 * @param statusBar
	 *            Die Statusbar des zugehörigen FrameViewer
	 * @param vs
	 *            VS Viewer Setting Objekt
	 */

	public NetzListe(JLabel statusBar, VS vs) {
		this.statusBar = statusBar;
		this.vs = vs;
		vs.setNL(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		Font f = statusBar.getFont();
		statusBar.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

	}

	/**
	 * Fügt Kante hinzu mit Gewicht 1
	 * 
	 * @param von
	 *            Nummer des Knotens, aus dem die Kante austritt.
	 * @param nach
	 *            Nummer des Knotens, in den die Kante eintritt.
	 */

	public void addEdge(Node von, Node nach) {
		addEdge(von,nach,1d);
	}

	/**
	 * Fügt Kante hinzu mit Gewicht w
	 * 
	 * @param von
	 *            Nummer des Knotens, aus dem die Kante austritt.
	 * @param nach
	 *            Nummer des Knotens, in den die Kante eintritt.
	 * @param w
	 *            Gewicht der Kante
	 */
	
	public void addEdge(Node von, Node nach, Double w) {
		for (int i = edges.size()-1; i >= 0; i--) {
			if (edges.get(i).von == von && edges.get(i).nach == nach) {
				edges.remove(i);
			}
		}
		if (w!=0) {
			edges.add(new Edge(von, nach, w, vs));			
		} else {
			von.degree--; 
			nach.degree--;	
		}
		revalidate();
		repaint();
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}

	/**
	 * Fügt Knoten hinzu und ruft calculateSize auf.
	 * 
	 * @param id
	 *            id des Knotens
	 * @param name
	 *            Name / Beschreibung des Knotens
	 */

	public void addNode(int id, String name,
			int x, int y, boolean fixed) {
		knoten.add(new Node(id, name, x, y, vs));
		knoten.lastElement().fix = fixed;		
		calculateSize();
	}

	
	public void addNode(Node node) {
		knoten.add(node);
		knoten.lastElement().fix = false;		
		calculateSize();
	}
	/**
	 * Berechnet die benötigte Größe um alle Knoten anzuzeigen und setzt sie.
	 */

	public void calculateSize() {
		long maxX = 0;
		long maxY = 0;
		for (int i = 0; i < knoten.size(); i++) {
			if (knoten.get(i).x > maxX)
				maxX = knoten.get(i).x;
			if (knoten.get(i).y > maxY)
				maxY = knoten.get(i).y;
		}
		setPreferredSize(new Dimension(
				(int) ((maxX + 2 * Node.getRadius() + 10) * vs.getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 10) * vs.getZoom())));
		revalidate();
		repaint();
	}

	public void changePhysics() {
		if (!started) {
			System.out.println("Starte Algorithmen");
			algo = new RunnableAlgorithm(knoten, edges, vs,	this);
			algo.start();
			started = true;
			algo.force = false;
		}
		algo.force = !algo.force;
	}

	/**
	 * Liefert die Adjacenz-Matrix zurück
	 */
	public int[][] getAMatrix() {
		int[][] e = new int[knoten.size()][];
		for (int i = 0; i < knoten.size(); i++) {
			e[i] = new int[knoten.size()];
			for (int j = 0; j < knoten.size(); j++) {
				e[i][j] = 0;
			}
		}
		for (int i = 0; i < edges.size(); i++) {
			e[knoten.indexOf(edges.get(i).von)][knoten.indexOf(edges.get(i).nach)] = 1;
			e[knoten.indexOf(edges.get(i).nach)][knoten.indexOf(edges.get(i).von)] = 1;
		}
		return e;
	}

	/**
	 * Liefert die interne Nummer des Knoten mit der ID id
	 * 
	 * @param id
	 *            ID des gesuchten Knotens
	 */

	public int getNodeNr(int id) throws Exception {
		for (int j = 0; j < knoten.size(); j++) {
			if (knoten.get(j).nr == id)
				return j;
		}
		throw new Exception();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		if (drag==-1) return;
		knoten.get(drag).x = (int) ((e.getX() - Node.getRadius() * vs.getZoom()) / (double) vs
				.getZoom());
		knoten.get(drag).y = (int) ((e.getY() - Node.getRadius() * vs.getZoom()) / (double) vs
				.getZoom());
		calculateSize();
		// vs.frameViewer.refresh();
		repaint();
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	/**
	 * Methode die vom MouseMotionListener MouseMotionNetz aufgerufen wird, wenn
	 * die Mouse bewegt wird.
	 * 
	 * @param e
	 *            Eingetretenes MouseEvent
	 */

	public void mouseMoved(MouseEvent e) {
			/* for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).inYou(e.getX(), e.getY())) {
					statusBar.setText("Nr:" + knoten.get(i).nr + " Beschreibung:"
							+ knoten.get(i).name);
				}
			}*/
	}
	
	public void mousePressed(MouseEvent e) {
		logger.debug("MousePressed at ("+e.getX()+","+ e.getY()+").");
		if (vs.newVertex) {
			addDefaultNode((int)(e.getX() / vs.getZoom())	- Node.r, 
						(int) (e.getY() / vs.getZoom()) - Node.r);
			vs.newVertex = false;
			statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			repaint();
			return;
		}
		if (vs.newEdge) {
			if (!firstVertexSelected) {
				firstVertex = vertexSelected(e.getX(), e.getY());
				if (firstVertex == null)
					return;
				firstVertexSelected = true;
				statusBar.setText("Select a second node to which the edge should lead.");
			} else {
				Node secondVertex = vertexSelected(e.getX(), e.getY());
				if (secondVertex == null)
					return;
				addEdge(firstVertex, secondVertex);
				vs.newEdge = false;
				firstVertexSelected = false;
				statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			}
			repaint();
			return;
		}
		if (drag == -1) {
			for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).inYou(e.getX(), e.getY())) {
					drag = i;
					//statusBar.setText("Nr:" + knoten.get(i).nr + " Beschreibung:" + knoten.get(i).name);
				}
			}
			if (drag != -1) {
				if (false) {
					knoten.get(drag).fix = true;
				}
				knoten.get(drag).drag = true;
			}
		} else {
			
		}
		if (e.getClickCount() == 2) {
			for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).inYou(e.getX(), e.getY())) {
					new UpdateNode(knoten.get(i));
				}
			}
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).inYou(e.getX(), e.getY())) {
					new UpdateEdge(edges.get(i));
				}
			}
		/*	for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).inYou(e.getX(), e.getY())) {
					knoten.get(i).fix = !knoten.get(i).fix;
				}
			}*/
		}		
		// vs.frameViewer.refresh();
		repaint();
	}

	public void addDefaultNode(int x, int y) {
		knoten.add(new Node(knoten.size() + 1, "" + (knoten.size() + 1), x, y, vs));		
	}

	/**
	 * Methode die vom MouseListener MouseNetz aufgerufen wird, wenn eine
	 * Mousetaste losgelassen wird.
	 * 
	 * @param e
	 *            Eingetretenes MouseEvent
	 */

	public void mouseReleased(MouseEvent e) {
		if (drag != -1) {
			knoten.get(drag).x = (int) ((e.getX() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom());
			knoten.get(drag).y = (int) ((e.getY() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom());
			calculateSize();
			knoten.get(drag).drag = false;
			drag = -1;
			// vs.frameViewer.refresh();
			repaint();
		}
	}

	/**
	 * Die Paint-Methode. paint() geht nicht, da sie nicht bei revalidate der
	 * Scrollbars aufgerufen wird.
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Node node : knoten) {
			node.paintYou(g);
		}
		for (Edge edge : edges) {
			edge.paintYou(g);			
		}
	}

	/**
	 * Mal die NetzListe neu und setzt preferredSize etc.
	 */

	public void refresh() {
		calculateSize();
	}

	/**
	 * Setzt die Settings aus dem Viewer Settings Objekt und führt
	 * gegebenenfalls Algorithmen zur Anordnung der Knoten aus.
	 */

	public void updateSettings() {
		int d = (2 * Node.getRadius() + 10);
		switch (vs.getGraphDrawAlgo()) {
		case 0:
			for (int i = 0; i < knoten.size(); i++) {
				int w = (int) (Math.sqrt(knoten.size())) + 1;
				knoten.get(i).x = 3 + (d * (i % w));
				knoten.get(i).y = 3 + (d * (i / w));
			}
			break;
		case 1:
			RunnableAlgorithm.hierarchicallySort(knoten, edges, vs);
			break;
		}
		calculateSize();
	}

	public Node vertexSelected(int x, int y) {
		for (Node n : knoten) {
			if (n.inYou(x, y)) {
				return n;
			}
		}
		return null;
	}

}
