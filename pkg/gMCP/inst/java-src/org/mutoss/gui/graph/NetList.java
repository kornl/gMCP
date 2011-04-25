package org.mutoss.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;
import org.mutoss.gui.RControl;
import org.mutoss.gui.dialogs.VariableDialog;

public class NetList extends JPanel implements MouseMotionListener, MouseListener {

	private static final Log logger = LogFactory.getLog(NetList.class);
	GraphView control;
	
	int drag = -1;
	int edrag = -1;
	
	protected Vector<Edge> edges = new Vector<Edge>();
	protected Vector<Node> nodes = new Vector<Node>();
	
	Node firstVertex;	
	boolean firstVertexSelected = false;
	
	public String initialGraph = ".InitialGraph" + (new Date()).getTime();

	boolean newEdge = false;
	boolean newVertex = false;	

	JLabel statusBar;

	public boolean testingStarted = false;

	double zoom = 1.00;

	public NetList(JLabel statusBar, GraphView graphview) {
		this.statusBar = statusBar;
		this.control = graphview;
		addMouseMotionListener(this);
		addMouseListener(this);
		Font f = statusBar.getFont();
		statusBar.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
	}

	public void acceptNode(Node node) {
		control.getPView().savePValues();
		saveGraph(".tmpGraph", false);
		RControl.getR().eval(".tmpGraph <- substituteEps(.tmpGraph, eps="+Configuration.getInstance().getGeneralConfig().getEpsilon()+")");
		RControl.getR().eval(".tmpGraph <- rejectNode(.tmpGraph, \""+node.getName()+"\")");
		reset();
		new GraphMCP(".tmpGraph", this);
		control.getPView().restorePValues();
	}

	public void addDefaultNode(int x, int y) {
		int i = nodes.size() + 1;
		String name = "H" + i;		
		while (whichNode(name) != -1) {
			i = i + 1;
			name = "H" + i;
		}
		addNode(new Node(name, x, y, 0d, this));		
	}

	public void addEdge(Edge e) {
		Edge old = null;
		for (Edge e2 : edges) {
			if (e2.from == e.from && e2.to == e.to) {
				old = e2;
			}
			if (e2.from == e.to && e2.to == e.from) {
				e.curve = true;
				e2.curve = true;
			}
		}
		if (old != null) edges.remove(old);
		edges.add(e);
		control.getDataTable().getModel().setValueAt(e.getEdgeWeight(), getKnoten().indexOf(e.from), getKnoten().indexOf(e.to));
		graphHasChanged();
	}

	public void addEdge(Node von, Node nach) {
		addEdge(von, nach, 1d);		
	}
	
	public void addEdge(Node von, Node nach, Double w) {	
		addEdge(von, nach, new EdgeWeight(w));
	}

	public void addEdge(Node von, Node nach, EdgeWeight w) {
		Integer x = null;
		Integer y = null;
		boolean curve = false;
		for (Edge e : edges) {
			if (e.from == nach && e.to == von) {
				e.curve = true;
				curve = true;
			}
		}		
		for (int i = edges.size()-1; i >= 0; i--) {
			if (edges.get(i).from == von && edges.get(i).to == nach) {
				x = edges.get(i).getK1();
				y = edges.get(i).getK2();
				edges.remove(i);				
			}
		}
		if (!w.toString().equals("0")) {
			if (x!=null) {
				edges.add(new Edge(von, nach, w, this, x, y));
			} else {
				edges.add(new Edge(von, nach, w, this, curve));
			}
			edges.lastElement().curve = curve;
		}
		control.getDataTable().getModel().setValueAt(w, getKnoten().indexOf(von), getKnoten().indexOf(nach));
		graphHasChanged();
	}

	public void addNode(Node node) {
		control.enableButtons(true);		
		nodes.add(node);
		control.getPView().addPPanel(node);
		control.getDataTable().getModel().addRowCol(node.getName());
		calculateSize();
		graphHasChanged();
	}
	
	public boolean updateGUI = true;

	public void graphHasChanged() {
		control.resultUpToDate = false;
		if (!updateGUI) return;
		String analysis = null;
		Set<String> variables = getAllVariables();
		variables.remove("ε");
		if (variables.size()==0) {
			try {
				String graphName = ".tmpGraph" + (new Date()).getTime();
				saveGraph(graphName, false);
				analysis = RControl.getR().eval("graphAnalysis("+graphName+")").asRChar().getData()[0];
			} catch (Exception e) {
				// We simply set the analysis to null - that's fine.
			}
		} else {
			analysis = "Graphs with variables are not yet supported for analysis.";
		}
		control.getDView().setAnalysis(analysis);
	}

	/**
	 * Calculates the size of the panel to view all nodes and resizes the panel.
	 */

	public int[] calculateSize() {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getX() > maxX)
				maxX = nodes.get(i).getX();
			if (nodes.get(i).getY() > maxY)
				maxY = nodes.get(i).getY();
		}
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getK1() > maxX)
				maxX = edges.get(i).getK1();
			if (edges.get(i).getK2() > maxY)
				maxY = edges.get(i).getK2();
		}		
		setPreferredSize(new Dimension(
				(int) ((maxX + 2 * Node.getRadius() + 30) * getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 30) * getZoom())));
		if (updateGUI) {
			revalidate();
			repaint();
		}		
		return new int[] {maxX, maxY};
	}
	
	public Edge findEdge(Node von, Node nach) {
		for (Edge e : edges) {
			if (von == e.from && nach == e.to) {
				return e;
			}
		}
		return null;
	}
	
	public Vector<Edge> getEdges() {
		return edges;
	}

	public BufferedImage getImage() {
		long maxX = 0;
		long maxY = 0;
		for (Node node : nodes) {
			if (node.getX() > maxX)
				maxX = node.getX();
			if (node.getY() > maxY)
				maxY = node.getY();
		}
		for (Edge edge : edges) {
			if (edge.getK1() > maxX)
				maxX = edge.getK1();
			if (edge.getK2() > maxY)
				maxY = edge.getK2();
		}		
		
		BufferedImage img = new BufferedImage((int) ((maxX + 2 * Node.getRadius() + 10) * getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 10) * getZoom()), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		g.setStroke(new BasicStroke(Configuration.getInstance().getGeneralConfig().getLineWidth()));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (Node node : nodes) {
			node.paintYou(g);
		}
		for (Edge edge : edges) {
			edge.paintEdge(g);			
		}
		for (Edge edge : edges) {
			edge.paintEdgeLabel(g);			
		}
		return img;

	}
	
	public Vector<Node> getKnoten() {
		return nodes;
	}
	
	public String getLaTeX() {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		String latex = "";
		double scale=0.5;
		latex += "\\begin{tikzpicture}[scale="+scale+"]";
		for (int i = 0; i < getKnoten().size(); i++) {
			Node node = getKnoten().get(i);
			latex += "\\node ("+node.getName().replace("_", "-")+") at ("+node.getX()+"bp,"+(-node.getY())+"bp)\n";
			String nodeColor = "green!80";
			if (node.isRejected()) {nodeColor = "red!80";}
			latex += "[draw,circle split,fill="+nodeColor+"] {$"+node.getName()+"$ \\nodepart{lower} $"+format.format(node.getWeight())+"$};\n";			
		}
		for (int i = 0; i < getEdges().size(); i++) {
			Node node1 = getEdges().get(i).from;
			Node node2 = getEdges().get(i).to;			
			String to = "bend left="+getEdges().get(i).getBendLeft();
			String weight = getEdges().get(i).getWLaTeX();			
			String pos = format.format(getEdges().get(i).getPos()).replace(",", ".");
			latex += "\\draw [->,line width=1pt] ("+node1.getName().replace("_", "-")+") to["+to+"] node[pos="+pos+",above,fill=blue!20] {"+weight+"} ("+node2.getName().replace("_", "-")+");\n";

		}
		latex += "\\end{tikzpicture}\n\n";
		return latex;
	}
	
	public double getZoom() {
		return zoom;
	}

	public boolean isTesting() {		
		return testingStarted;
	}

	public void loadGraph() {
		this.updateGUI = false;
		GraphMCP graph = new GraphMCP(initialGraph, this);
		if (graph.getDescription()!=null) {
			control.getDView().setDescription(graph.getDescription());
		} else {
			control.getDView().setDescription("");
		}
		control.getPView().restorePValues();
		this.updateGUI = true;
		graphHasChanged();
		revalidate();
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		if (drag==-1 && edrag == -1) return;
		if (drag!=-1) {
			nodes.get(drag).setX( (int) ((e.getX() - Node.getRadius() * getZoom()) / (double) getZoom()));
			nodes.get(drag).setY( (int) ((e.getY() - Node.getRadius() * getZoom()) / (double) getZoom()));
		} else {
			edges.get(edrag).setK1( (int) ((e.getX()) / (double) getZoom()));
			edges.get(edrag).setK2( (int) ((e.getY()) / (double) getZoom()));
		}
		calculateSize();
		repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		logger.debug("MousePressed at ("+e.getX()+","+ e.getY()+").");
		if (newVertex) {
			addDefaultNode((int)(e.getX() / getZoom()) - Node.r, 
						(int) (e.getY() / getZoom()) - Node.r);
			newVertex = false;
			statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			repaint();
			return;
		}
		if (newEdge) {
			if (!firstVertexSelected) {
				firstVertex = vertexSelected(e.getX(), e.getY());
				if (firstVertex == null)
					return;
				firstVertexSelected = true;
				statusBar.setText("Select a second node to which the edge should lead.");
			} else {
				Node secondVertex = vertexSelected(e.getX(), e.getY());
				if (secondVertex == null || secondVertex == firstVertex) {
					return;
				}
				addEdge(firstVertex, secondVertex);
				newEdge = false;
				firstVertexSelected = false;
				statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			}
			repaint();
			return;
		}
		if (drag == -1) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).inYou(e.getX(), e.getY())) {
					drag = i;
					//statusBar.setText("Nr:" + knoten.get(i).nr + " Beschreibung:" + knoten.get(i).name);
				}
			}
			if (drag != -1) {
				nodes.get(drag).drag = true;
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
		if (e.getClickCount() == 2 && !testingStarted) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).inYou(e.getX(), e.getY())) {
					new UpdateNode(nodes.get(i), this);
				}
			}
			for (int i = 0; i < edges.size(); i++) {
				if (edges.get(i).inYou(e.getX(), e.getY())) {
					new UpdateEdge(edges.get(i), this, control);
				}
			}		
		}		
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		if (drag != -1) {
			nodes.get(drag).setX( (int) ((e.getX() - Node.getRadius() * getZoom()) / (double) getZoom()));
			nodes.get(drag).setY( (int) ((e.getY() - Node.getRadius() * getZoom()) / (double) getZoom()));
			calculateSize();
			nodes.get(drag).drag = false;
			drag = -1;
			repaint();
		}
		if (edrag != -1) {
			edges.get(edrag).setK1( (int) ((e.getX()) / (double) getZoom()));
			edges.get(edrag).setK2( (int) ((e.getY()) / (double) getZoom()));
			calculateSize();
			edrag = -1;
			repaint();
		}
	}
	
	/**
	 * We use paintComponent() instead of paint(), since the later one
	 * is not called by a revalidate of the scrollbars.
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
		BasicStroke stroke = new BasicStroke(Configuration.getInstance().getGeneralConfig().getLineWidth());
		((Graphics2D)g).setStroke(stroke);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (Node node : nodes) {
			node.paintYou(g);
		}
		for (Edge edge : edges) {
			edge.paintEdge(g);			
		}
		for (Edge edge : edges) {
			edge.paintEdgeLabel(g);			
		}
	}
	
	/**
	 * Repaints the NetzListe and sets the preferredSize etc.
	 */

	public void refresh() {
		calculateSize();
		revalidate();
		repaint();
	}
	
	public void removeEdge(Edge edge) {
		for (Edge e : edges) {
			if (e.from == edge.to && e.to == edge.from) {
				e.curve = false;				
			}
		}
		edges.remove(edge);
		control.getDataTable().getModel().setValueAt(new EdgeWeight(0), getKnoten().indexOf(edge.from), getKnoten().indexOf(edge.to));
		graphHasChanged();
	}

	public void removeNode(Node node) {
		for (int i=edges.size()-1; i>=0; i--) {
			Edge e = edges.get(i);
			if (e.from==node || e.to==node) {
				edges.remove(e);
			}
		}
		control.getDataTable().getModel().delRowCol(getKnoten().indexOf(node));
		nodes.remove(node);
		control.getPView().removePPanel(node);
		if (nodes.size()==0) {
			control.enableButtons(false);
		}
		repaint();
		graphHasChanged();
	}

	public void reset() {
		edges.removeAllElements();
		for (int i=getKnoten().size()-1; i>=0; i--) {
			removeNode(getKnoten().get(i));
		}
		statusBar.setText(GraphView.STATUSBAR_DEFAULT);
		firstVertexSelected = false;
		newVertex = false;
		newEdge = false;
		zoom = 1.00;
		graphHasChanged();
	}

	public void saveGraph() {
		saveGraph(initialGraph, false);
		control.getPView().savePValues();
	}
	
	public Set<String> getAllVariables() {
		Set<String> variables = new HashSet<String>();		
		for (Edge e : edges) {		
			variables.addAll(e.getVariable());
		}
		return variables;
	}
	
	public String saveGraphWithoutVariables(String graphName, boolean verbose) {
		Set<String> variables = getAllVariables();
		/*if (!Configuration.getInstance().getGeneralConfig().useEpsApprox()) */
		{
			variables.remove("ε");
		}

		Hashtable<String,Double> ht = new Hashtable<String,Double>();
		if (!variables.isEmpty() && !(variables.size()==1 && variables.contains("ε"))) {
			VariableDialog vd = new VariableDialog(this.control.parent, variables);
			ht = vd.getHT();
		} else if (variables.size()==1 && variables.contains("ε")){
			ht.put("ε", Configuration.getInstance().getGeneralConfig().getEpsilon());
		}
		return saveGraph(graphName, verbose, ht);
	}
	
	
	public String saveGraph(String graphName, boolean verbose) {
		return saveGraph(graphName, verbose, new Hashtable<String,Double>());
	}
	
	public String saveGraph(String graphName, boolean verbose, Hashtable<String,Double> ht) {
		
		graphName = RControl.getR().eval("make.names(\""+graphName+"\")").asRChar().getData()[0];
		
		String alpha = "";
		String nodeStr = "";
		String x = "";
		String y = "";
		for (Node n : nodes) {
			alpha += n.getWeight() +",";
			nodeStr += "\""+n.getName() +"\","; 
			x += n.getX() + ",";
			y += n.getY() + ",";
		}
		alpha = alpha.substring(0, alpha.length()-1);
		nodeStr = nodeStr.substring(0, nodeStr.length()-1);
		x = x.substring(0, x.length()-1);
		y = y.substring(0, y.length()-1);
		
		RControl.getR().evalVoid(".gsrmtVar <- list()");
		RControl.getR().evalVoid(".gsrmtVar$alpha <- c("+alpha+")");
		RControl.getR().evalVoid(".gsrmtVar$hnodes <- c("+nodeStr+")");
		RControl.getR().evalVoid(".gsrmtVar$edges <- vector(\"list\", length="+nodes.size()+")");
		RControl.getR().evalVoid("names(.gsrmtVar$edges)<-.gsrmtVar$hnodes");
		for (int i=nodes.size()-1; i>=0; i--) {
			Node n = nodes.get(i);
			String edgeL = "";
			String weights = "";
			for (Edge e : edges) {				
				if (e.from == n) {
					edgeL += "\""+e.to.getName()+"\",";
					weights += e.getW(ht)[0] +",";
				}
			}
			if (edgeL.length()!=0) {
				edgeL = edgeL.substring(0, edgeL.length()-1);
				weights = weights.substring(0, weights.length()-1);			
				RControl.getR().evalVoid(".gsrmtVar$edges[["+(i+1)+"]] <- list(edges=c("+edgeL+"), weights=c("+weights+"))");
			} else {
				RControl.getR().evalVoid(".gsrmtVar$edges[["+(i+1)+"]] <- list(edges=character(0), weights=numeric(0))");
			}
		}		

		RControl.getR().evalVoid(graphName+" <- new(\"graphMCP\", nodes=.gsrmtVar$hnodes, edgeL=.gsrmtVar$edges, weights=.gsrmtVar$alpha)");
		//TODO remove this stupid workaround.
		RControl.getR().evalVoid(graphName+" <- gMCP:::stupidWorkAround("+graphName+")");
		for (int i=nodes.size()-1; i>=0; i--) {
			Node n = nodes.get(i);
			if (n.isRejected()) {
				RControl.getR().evalVoid("nodeData("+graphName+", \""+n.getName()+"\", \"rejected\") <- TRUE");
			}
		}
		RControl.getR().evalVoid(".gsrmtVar$nodeX <- c("+x+")");
		RControl.getR().evalVoid(".gsrmtVar$nodeY <- c("+y+")");
		RControl.getR().evalVoid("names(.gsrmtVar$nodeX) <- .gsrmtVar$hnodes");
		RControl.getR().evalVoid("names(.gsrmtVar$nodeY) <- .gsrmtVar$hnodes");
		RControl.getR().evalVoid("nodeRenderInfo("+graphName+") <- list(nodeX=.gsrmtVar$nodeX, nodeY=.gsrmtVar$nodeY)");
		for (Edge e : edges) {				
			RControl.getR().evalVoid("edgeData("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"labelX\") <- "+(e.k1-Node.getRadius()));
			RControl.getR().evalVoid("edgeData("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"labelY\") <- "+(e.k2-Node.getRadius()));
			String eps = e.getEpsilonString(null);
			if (eps!=null) {
				RControl.getR().evalVoid("edgeData("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"epsilon\") <- list("+eps+")");
			}
			System.out.println("Weight is: "+e.getW(ht)[0]);
			if (((Double)e.getW(ht)[0]).isNaN()) {
				RControl.getR().evalVoid("edgeData("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"variableWeight\") <- \""+e.getWS().replaceAll("\\\\", "\\\\\\\\")+"\"");
			}
		}	
		RControl.getR().evalVoid("attr("+graphName+", \"description\") <- \""+ control.getDView().getDescription()+"\"");
		if (verbose) { JOptionPane.showMessageDialog(null, "The graph as been exported to R under ther variable name:\n\n"+graphName, "Saved as \""+graphName+"\"", JOptionPane.INFORMATION_MESSAGE); }
		return graphName;
	}
	
	public void setEdges(Vector<Edge> edges) {
		this.edges = edges;
		graphHasChanged();
	}

	public void setKnoten(Vector<Node> knoten) {
		this.nodes = knoten;
		graphHasChanged();
	}
	
	public void setZoom(double p) {
		zoom = p;
	}

	public void startTesting() {
		testingStarted = true;	
		statusBar.setText("Reject nodes or reset to the initial graph for modifications.");
	}

	public void stopTesting() {
		testingStarted = false;
		statusBar.setText(GraphView.STATUSBAR_DEFAULT);
	}

	public Node vertexSelected(int x, int y) {
		for (Node n : nodes) {
			if (n.inYou(x, y)) {
				return n;
			}
		}
		return null;
	}
	public int whichNode(String name) {
		for (int i=0; i<nodes.size(); i++) {
			if (nodes.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public void loadGraph(String string) {
		control.stopTesting();
		reset();
		boolean matrix = RControl.getR().eval("is.matrix("+string+")").asRLogical().getData()[0];
		RControl.getR().eval(initialGraph + " <- placeNodes("+ (matrix?"matrix2graph(":"(")+ string + "))");
		loadGraph();
	}
	
}
