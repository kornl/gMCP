package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.RControl;

import af.statguitoolkit.config.Configuration;

/**
 * Diese Klasse (extends JPanel) stellt einen Graphen graphisch dar.
 */

public class NetzListe extends JPanel implements MouseMotionListener, MouseListener {

	private static final Log logger = LogFactory.getLog(NetzListe.class);
	RunnableAlgorithm algo;
	int drag = -1;
	int edrag = -1;
	Node firstVertex;
	AbstractGraphControl control;
	
	boolean firstVertexSelected = false;
	protected Vector<Edge> edges = new Vector<Edge>();
	protected Vector<Node> knoten = new Vector<Node>();

	public Vector<Edge> getEdges() {
		return edges;
	}

	public void setEdges(Vector<Edge> edges) {
		this.edges = edges;
	}

	public Vector<Node> getKnoten() {
		return knoten;
	}

	public void setKnoten(Vector<Node> knoten) {
		this.knoten = knoten;
	}

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

	public NetzListe(JLabel statusBar, VS vs,  AbstractGraphControl abstractGraphControl) {
		this.statusBar = statusBar;
		this.vs = vs;
		this.control = abstractGraphControl;
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
		Integer x = null;
		Integer y = null;
		boolean curve = false;
		for (Edge e : edges) {
			if (e.von == nach && e.nach == von) {
				e.curve = true;
				curve = true;
			}
		}		
		for (int i = edges.size()-1; i >= 0; i--) {
			if (edges.get(i).von == von && edges.get(i).nach == nach) {
				x = edges.get(i).getK1();
				y = edges.get(i).getK2();
				edges.remove(i);				
			}
		}
		if (w!=0) {
			if (x!=null) {
				edges.add(new Edge(von, nach, w, vs, x, y));
			} else {
				edges.add(new Edge(von, nach, w, vs, curve));
			}						
		}
		edges.lastElement().curve = curve;
	}
	
	public void addEdge(Edge e) {
		for (Edge e2 : edges) {
			if (e2.von == e.nach && e2.nach == e.von) {
				e.curve = true;
				e2.curve = true;
			}
		}
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

	public int[] calculateSize() {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < knoten.size(); i++) {
			if (knoten.get(i).getX() > maxX)
				maxX = knoten.get(i).getX();
			if (knoten.get(i).getY() > maxY)
				maxY = knoten.get(i).getY();
		}
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getK1() > maxX)
				maxX = edges.get(i).getK1();
			if (edges.get(i).getK2() > maxY)
				maxY = edges.get(i).getK2();
		}		
		setPreferredSize(new Dimension(
				(int) ((maxX + 2 * Node.getRadius() + 30) * vs.getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 30) * vs.getZoom())));
		revalidate();
		repaint();
		return new int[] {maxX, maxY};
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
		if (drag==-1 && edrag == -1) return;
		if (drag!=-1) {
			knoten.get(drag).setX( (int) ((e.getX() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom()));
			knoten.get(drag).setY( (int) ((e.getY() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom()));
		} else {
			edges.get(edrag).setK1( (int) ((e.getX() * vs.getZoom()) / (double) vs.getZoom()));
			edges.get(edrag).setK2( (int) ((e.getY() * vs.getZoom()) / (double) vs.getZoom()));
		}
		calculateSize();
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

	public void mouseMoved(MouseEvent e) {}
	
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
		}
		if (drag == -1 && edrag == -1) {
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).inYou(e.getX(), e.getY())) {
					edrag = i;
					//statusBar.setText("Nr:" + knoten.get(i).nr + " Beschreibung:" + knoten.get(i).name);
				}
			}		
		}
		if (e.getClickCount() == 2) {
			for (int i = 0; i < knoten.size(); i++) {
				if (knoten.get(i).inYou(e.getX(), e.getY())) {
					new UpdateNode(knoten.get(i), this);
				}
			}
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).inYou(e.getX(), e.getY())) {
					new UpdateEdge(edges.get(i), this);
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
		knoten.add(new Node(knoten.size() + 1, "HA_" + (knoten.size() + 1), x, y, vs));		
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
			knoten.get(drag).setX( (int) ((e.getX() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom()));
			knoten.get(drag).setY( (int) ((e.getY() - Node.getRadius() * vs.getZoom()) / (double) vs.getZoom()));
			calculateSize();
			knoten.get(drag).drag = false;
			drag = -1;
			repaint();
		}
		if (edrag != -1) {
			edges.get(edrag).setK1( (int) ((e.getX() * vs.getZoom()) / (double) vs.getZoom()));
			edges.get(edrag).setK2( (int) ((e.getY() * vs.getZoom()) / (double) vs.getZoom()));
			calculateSize();
			edrag = -1;
			repaint();
		}
	}

	/**
	 * Die Paint-Methode. paint() geht nicht, da sie nicht bei revalidate der
	 * Scrollbars aufgerufen wird.
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		g.setColor(Color.LIGHT_GRAY);
		if (grid>1) {
			for(int x=(-Node.r/grid)*grid; x < getWidth(); x += grid) {				
				g.drawLine(x+Node.r, 0, x+Node.r, getHeight());	
			}
			for(int y=(-Node.r/grid)*grid; y < getHeight(); y += grid) {
				g.drawLine(0, y+Node.r, getWidth(), y+Node.r);
			}
		}
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
		revalidate();
		repaint();
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
				knoten.get(i).setX( 3 + (d * (i % w)));
				knoten.get(i).setY( 3 + (d * (i / w)));
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

	

	public void acceptNode(Node node) {
		int epsilon = 0; 
		boolean eps = true;
		boolean hasChildren = updateEdges(node);
		for (int i=edges.size()-1; i>=0; i--) {
			Edge e = edges.get(i);
			if (e.nach == node) {				
				removeEdge(e);
			}
			if (e.von == node && !e.w.toString().equals("NaN")) {
				e.nach.alpha = e.nach.alpha + node.alpha * e.w;				
				removeEdge(e);
				eps = false;
			} 
			if (e.von == node && e.w.toString().equals("NaN")) {
				epsilon++;
			}
		}
		if (epsilon>0) {
			for (int i=edges.size()-1; i>=0; i--) {
				Edge e = edges.get(i);
				if (e.von == node && e.w.toString().equals("NaN")) {
					if (eps) {
						e.nach.alpha = e.nach.alpha + node.alpha / epsilon;					
					}
					removeEdge(e);
				} 
			}
		}
		if (hasChildren) {
			node.alpha = 0; 
		}
		node.setColor(Color.MAGENTA);
		revalidate();
		repaint();
	}

	private boolean updateEdges(Node node) {
		Vector<Node> epsChildren = new Vector<Node>();
		Vector<Node> realChildren = new Vector<Node>();
		Vector<Node> allChildren = new Vector<Node>();
		for (Edge e : edges) {
			if (node == e.von) {
				if (e.w.toString().equals("NaN")) {
					epsChildren.add(e.nach);
				} else {
					realChildren.add(e.nach);
				}		
				allChildren.add(e.nach);
			}
		}		
		Vector<Edge> all = new Vector<Edge>();
		all.addAll(edges);
		for (Edge e : all) {			
			if (e.nach == node) {				
				if (e.w.toString().equals("NaN")) { 
					for (Node nach : allChildren) {
						if (findEdge(e.von,nach)==null) {
							if (e.von!=nach) { addEdge(e.von, nach); }
						}
					}
				} else { 
					for (Node nach : epsChildren) {
						if (findEdge(e.von,nach)==null) {
							if (e.von!=nach) { addEdge(e.von, nach); }
						}
					}
					for (Node nach : realChildren) {						
						Double glk = (findEdge(e.von,nach)==null)?0:findEdge(e.von,nach).w;
						Double glj = e.w;
						Double gjk = findEdge(node,nach).w;
						Double gjl = (findEdge(node,e.von)==null)?0:findEdge(node,e.von).w;
						if (e.von!=nach) { addEdge(e.von, nach, (glk+glj*gjk)/(1-glj*gjl)); }						
					}					
				}				
			}
		}		
		return allChildren.size()!=0;
	}

	public void reset() {
		edges.removeAllElements();
    	knoten.removeAllElements();
		statusBar.setText(GraphView.STATUSBAR_DEFAULT);
		firstVertexSelected = false;
		vs.newVertex = false;
		vs.newEdge = false;
		vs.zoom = 1.00;
	}
	

	public void saveGraph(String graphName, boolean verbose) {
		graphName = RControl.getR().eval("make.names(\""+graphName+"\")").asRChar().getData()[0];
		
		String alpha = "";
		String nodes = "";
		String x = "";
		String y = "";
		for (Node n : knoten) {
			alpha += n.getAlpha() +",";
			nodes += "\""+n.getName() +"\","; 
			x += n.getX() + ",";
			y += n.getY() + ",";
		}
		alpha = alpha.substring(0, alpha.length()-1);
		nodes = nodes.substring(0, nodes.length()-1);
		x = x.substring(0, x.length()-1);
		y = y.substring(0, y.length()-1);
		
		RControl.getR().evalVoid(".gsrmtVar <- list()");
		RControl.getR().evalVoid(".gsrmtVar$alpha <- c("+alpha+")");
		RControl.getR().evalVoid(".gsrmtVar$hnodes <- c("+nodes+")");
		RControl.getR().evalVoid(".gsrmtVar$edges <- vector(\"list\", length="+knoten.size()+")");
		for (int i=0; i<knoten.size(); i++) {
			Node n = knoten.get(i);
			String edgeL = "";
			String weights = "";
			for (Edge e : edges) {				
				if (e.von == n) {
					edgeL += "\""+e.nach.getName()+"\",";
					weights += e.w +",";
				}
			}
			if (edgeL.length()!=0) {
				edgeL = edgeL.substring(0, edgeL.length()-1);
				weights = weights.substring(0, weights.length()-1);			
				RControl.getR().evalVoid(".gsrmtVar$edges[["+(i+1)+"]] <- list(edges=c("+edgeL+"), weights=c("+weights+"))");
			}
		}		
		RControl.getR().evalVoid("names(.gsrmtVar$edges)<-.gsrmtVar$hnodes");
		RControl.getR().evalVoid(graphName+" <- new(\"graphSRMTP\", nodes=.gsrmtVar$hnodes, edgeL=.gsrmtVar$edges, alpha=.gsrmtVar$alpha)");		
		RControl.getR().evalVoid(".gsrmtVar$nodeX <- c("+x+")");
		RControl.getR().evalVoid(".gsrmtVar$nodeY <- c("+y+")");
		RControl.getR().evalVoid("names(.gsrmtVar$nodeX) <- .gsrmtVar$hnodes");
		RControl.getR().evalVoid("names(.gsrmtVar$nodeY) <- .gsrmtVar$hnodes");
		RControl.getR().evalVoid("nodeRenderInfo("+graphName+") <- list(nodeX=.gsrmtVar$nodeX, nodeY=.gsrmtVar$nodeY)");	
		if (verbose) { JOptionPane.showMessageDialog(null, "The graph as been exported to R under ther variable name: "+graphName, "Saved as \""+graphName+"\"", JOptionPane.INFORMATION_MESSAGE); }
	}
	
	public Edge findEdge(Node von, Node nach) {
		for (Edge e : edges) {
			if (von == e.von && nach == e.nach) {
				return e;
			}
		}
		return null;
	}
	
	
	public void removeEdge(Edge edge) {
		for (Edge e : edges) {
			if (e.von == edge.nach && e.nach == edge.von) {
				e.curve = false;				
			}
		}
		edges.remove(edge);		
	}
	
	public BufferedImage getImage() {
		long maxX = 0;
		long maxY = 0;
		for (int i = 0; i < knoten.size(); i++) {
			if (knoten.get(i).getX() > maxX)
				maxX = knoten.get(i).getX();
			if (knoten.get(i).getY() > maxY)
				maxY = knoten.get(i).getY();
		}		
		BufferedImage img = new BufferedImage((int) ((maxX + 2 * Node.getRadius() + 10) * vs.getZoom())
				, (int) ((maxY + 2 * Node.getRadius() + 10) * vs.getZoom()), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		for (Node node : knoten) {
			node.paintYou(g);
		}
		for (Edge edge : edges) {
			edge.paintYou(g);			
		}
		return img;

	}
	
	public void removeNode(Node node) {
		for (int i=edges.size()-1; i>=0; i--) {
			Edge e = edges.get(i);
			if (e.von==node || e.nach==node) {
				edges.remove(e);
			}
		}
		knoten.remove(node);
		repaint();
	}
	
}
