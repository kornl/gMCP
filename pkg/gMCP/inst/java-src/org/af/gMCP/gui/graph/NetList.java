package org.af.gMCP.gui.graph;

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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.VariableDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	
	static DecimalFormat format = new DecimalFormat("#.####");

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

	public void setEdge(Edge e) {
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
		control.getDataTable().getModel().setValueAt(e.getEdgeWeight(), getNodes().indexOf(e.from), getNodes().indexOf(e.to));
		graphHasChanged();
	}

	public void setEdge(Node von, Node nach) {
		setEdge(von, nach, 1d);		
	}
	
	public void setEdge(Node von, Node nach, Double w) {	
		setEdge(von, nach, new EdgeWeight(w));
	}

	public void setEdge(Node von, Node nach, EdgeWeight w) {
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
		control.getDataTable().getModel().setValueAt(w, getNodes().indexOf(von), getNodes().indexOf(nach));
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
				analysis = RControl.getR().eval("graphAnalysis("+graphName+", file=tempfile())").asRChar().getData()[0];
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
	
	public Vector<Node> getNodes() {
		return nodes;
	}
	
	public String getLaTeX() {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		String latex = "";
		double scale=0.5;
		latex += "\\begin{tikzpicture}[scale="+scale+"]";
		for (int i = 0; i < getNodes().size(); i++) {
			Node node = getNodes().get(i);
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
			latex += "\\draw [->,line width=1pt] ("+node1.getName().replace("_", "-")+") to["+to+"] node[pos="+pos+",above,fill=blue!20] {$"+weight+"$} ("+node2.getName().replace("_", "-")+");\n";

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

	public GraphMCP loadGraph() {
		control.stopTesting();
		reset();
		this.updateGUI = false;
		GraphMCP graph = new GraphMCP(initialGraph, this);
		control.getPView().restorePValues();
		this.updateGUI = true;
		graphHasChanged();
		revalidate();
		repaint();
		return graph;
	}

	public void loadGraph(String string) {
		boolean matrix = RControl.getR().eval("is.matrix("+string+")").asRLogical().getData()[0];
		RControl.getR().eval(initialGraph + " <- placeNodes("+ (matrix?"matrix2graph(":"(")+ string + "))");
		GraphMCP graph = loadGraph();	
		if (graph.getDescription()!=null) {
			control.getDView().setDescription(graph.getDescription());
		} else {
			control.getDView().setDescription("");
		}		
		if (graph.pvalues!=null && graph.pvalues.length>1) {
			control.getPView().setPValues(graph.pvalues);
		}
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		if (drag==-1 && edrag == -1) return;
		if (drag!=-1) {
			nodes.get(drag).setX( (int) ((e.getX()+offset[0]) / (double) getZoom()));
			nodes.get(drag).setY( (int) ((e.getY()+offset[1]) / (double) getZoom()));
		} else {
			edges.get(edrag).setK1( (int) ((e.getX()+offset[0]) / (double) getZoom()));
			edges.get(edrag).setK2( (int) ((e.getY()+offset[1]) / (double) getZoom()));
		}
		calculateSize();
		repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	protected int[] offset;
	
	public void mousePressed(MouseEvent e) {
		//logger.debug("MousePressed at ("+e.getX()+","+ e.getY()+").");
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
				setEdge(firstVertex, secondVertex);
				newEdge = false;
				firstVertexSelected = false;
				statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			}
			repaint();
			return;
		}

		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).inYou(e.getX(), e.getY())) {
				drag = i;
				offset = nodes.get(i).offset(e.getX(), e.getY());
			}
		}
		for (int i = edges.size()-1; i >=0 ; i--) {
			if (edges.get(i).inYou(e.getX(), e.getY())) {
				drag = -1;
				edrag = i;
				offset = edges.get(i).offset(e.getX(), e.getY());
			}
		}
		if (e.getClickCount() == 2 && !testingStarted) {			
			for (int i = edges.size()-1; i >=0 ; i--) {
				if (edges.get(i).inYou(e.getX(), e.getY())) {
					new UpdateEdge(edges.get(i), this, control);
					repaint();
					return;
				}
			}
			for (int i = nodes.size()-1; i >=0 ; i--) {
				if (nodes.get(i).inYou(e.getX(), e.getY())) {
					new UpdateNode(nodes.get(i), this);
					repaint();
					return;
				}
			}
		}		
		repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		drag = -1;
		edrag = -1;
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
		
		if (expRejections != null && powAtlst1 != null && rejectAll != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font("Arial", Font.BOLD, (int) (12 * getZoom())));
			
			String s = "Expected number of rejections: " + format.format(expRejections);			
			
			g2d.drawString(s ,
					(float) (( 10 ) * getZoom()),
					(float) (( 20 ) * getZoom())); 
			
			s = "Prob. to reject at least one hyp.: " + format.format(powAtlst1);		
			
			g2d.drawString(s ,
					(float) (( 10 ) * getZoom()),
					(float) (( 20+30 ) * getZoom())); 
			
			s = "Prob. to reject all hypotheses: " + format.format(rejectAll);		
			
			g2d.drawString(s ,
					(float) (( 10 ) * getZoom()),
					(float) (( 20+30*2 ) * getZoom())); 
			
			if (userDefined!=null) {
				s = "User defined Power: " + format.format(userDefined);		

				g2d.drawString(s ,
						(float) (( 10 ) * getZoom()),
						(float) (( 20+30*3 ) * getZoom()));
			}
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
		logger.info("Removing "+edge);
		for (Edge e : edges) {
			if (e.from == edge.to && e.to == edge.from) {
				e.curve = false;				
			}
		}
		edges.remove(edge);
		control.getDataTable().getModel().setValueAt(new EdgeWeight(0), getNodes().indexOf(edge.from), getNodes().indexOf(edge.to));
		graphHasChanged();
	}

	public void removeNode(Node node) {
		logger.info("Removing "+node);		
		for (int i=edges.size()-1; i>=0; i--) {
			Edge e = edges.get(i);
			if (e.from==node || e.to==node) {
				edges.remove(e);
			}
		}
		control.getDataTable().getModel().delRowCol(getNodes().indexOf(node));
		nodes.remove(node);
		control.getPView().removePPanel(node);
		if (nodes.size()==0) {
			control.enableButtons(false);
		}
		repaint();
		graphHasChanged();
	}

	public void reset() {
		logger.info("Reset.");
		edges.removeAllElements();
		for (int i=getNodes().size()-1; i>=0; i--) {
			removeNode(getNodes().get(i));
		}
		statusBar.setText(GraphView.STATUSBAR_DEFAULT);
		firstVertexSelected = false;
		newVertex = false;
		newEdge = false;
		zoom = 1.00;
		control.getDView().setDescription("Enter a description for the graph.");
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
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}

		Hashtable<String,Double> ht = new Hashtable<String,Double>();
		if (!variables.isEmpty() && !(variables.size()==1 && variables.contains("ε"))) {
			VariableDialog vd = new VariableDialog(this.control.parent, variables);
			ht = vd.getHT();
		} else if (variables.size()==1 && variables.contains("ε")){
			ht.put("ε", Configuration.getInstance().getGeneralConfig().getEpsilon());
		}		
		graphName = RControl.getR().eval("make.names(\""+graphName+"\")").asRChar().getData()[0];
		saveGraph(graphName, verbose, null);
		RControl.getR().eval(graphName+"<- gMCP:::replaceVariables("+graphName+", variables="+getRVariableList(ht)+")");
		loadGraph(graphName);
		return saveGraph(graphName, verbose, ht);
	}
	
	
	public String saveGraph(String graphName, boolean verbose) {
		return saveGraph(graphName, verbose, new Hashtable<String,Double>());
	}
	
	public String getRVariableList(Hashtable<String,Double> ht) {
		// For use in replaceVariables <-function(graph, variables=list())
		String list = "list(";
		Enumeration<String> keys = ht.keys();
		for (; keys.hasMoreElements();) {
			String key = keys.nextElement();
			list += "\""+EdgeWeight.UTF2LaTeX(key.charAt(0))+"\"="+ht.get(key)+",";
		}
		list += "\""+"epsilon"+"\"="+Configuration.getInstance().getGeneralConfig().getEpsilon()+",";
		return list.substring(0, list.length()>5?list.length()-1:list.length())+")";			
	}
	
	public String saveGraph(String graphName, boolean verbose, Hashtable<String,Double> ht) {		
		graphName = RControl.getR().eval("make.names(\""+graphName+"\")").asRChar().getData()[0];		
		String alpha = "";
		String nodeStr = "";
		String x = "";
		String y = "";
		for (Node n : nodes) {
			//alpha += "\""+n.getWS() +"\",";
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
		RControl.getR().evalVoid(".gsrmtVar$m <- matrix(0, nrow="+nodes.size()+", ncol="+nodes.size()+")");
		RControl.getR().evalVoid("rownames(.gsrmtVar$m) <- colnames(.gsrmtVar$m) <- .gsrmtVar$hnodes");
		for (Edge e : edges) {
			RControl.getR().evalVoid(".gsrmtVar$m[\""+e.from.getName()+"\",\""+e.to.getName()+"\"] <- \""+ e.getWS().replaceAll("\\\\", "\\\\\\\\") +"\"");
		}
		RControl.getR().evalVoid(graphName+" <- new(\"graphMCP\", m=.gsrmtVar$m, weights=.gsrmtVar$alpha)");
		for (int i=nodes.size()-1; i>=0; i--) {
			Node n = nodes.get(i);
			if (n.isRejected()) {
				RControl.getR().evalVoid("nodeAttr("+graphName+", \""+n.getName()+"\", \"rejected\") <- TRUE");
			}
		}
		RControl.getR().evalVoid(graphName+"@nodeAttr$X <- c("+x+")");
		RControl.getR().evalVoid(graphName+"@nodeAttr$Y <- c("+y+")");
		for (Edge e : edges) {				
			RControl.getR().evalVoid("edgeAttr("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"labelX\") <- "+(e.k1-Node.getRadius()));
			RControl.getR().evalVoid("edgeAttr("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"labelY\") <- "+(e.k2-Node.getRadius()));
			//logger.debug("Weight is: "+e.getW(ht));
			if (((Double)e.getW(ht)).isNaN()) {
				RControl.getR().evalVoid("edgeAttr("+graphName+", \""+e.from.getName()+"\", \""+e.to.getName()+"\", \"variableWeight\") <- \""+e.getWS().replaceAll("\\\\", "\\\\\\\\")+"\"");
			}
			if (e.getW(ht)==0) {
				RControl.getR().evalVoid(graphName +"@m[\""+e.from.getName()+"\", \""+e.to.getName()+"\"] <- 0");
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

	public String getGraphName() {
		saveGraph(".tmpGraph", false);
		return ".tmpGraph";
	}

	Double expRejections = null;
	Double powAtlst1 = null;
	Double rejectAll = null;
	Double userDefined = null;
	
	public void setPower(double[] localPower, Double expRejections,
			Double powAtlst1, Double rejectAll, Double userDefined) {
		for (int i=0; i<localPower.length; i++) {
			this.nodes.get(i).setLocalPower(localPower[i]);			
		}
		this.expRejections = expRejections;
		this.powAtlst1 = powAtlst1;
		this.rejectAll = rejectAll;
		this.userDefined = userDefined;
		this.repaint();
	}
	
}
