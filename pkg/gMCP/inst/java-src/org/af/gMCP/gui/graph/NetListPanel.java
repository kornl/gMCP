package org.af.gMCP.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.af.commons.images.GraphDrawHelper;
import org.af.gMCP.config.Configuration;

public class NetListPanel extends JPanel implements MouseMotionListener, MouseListener {

	int[] dragN = new int[0];
	int[] dragE = new int[0];
	
	static DecimalFormat format = new DecimalFormat("#.####");
	
	boolean unAnchor = false;
	Node firstVertex;	
	boolean firstVertexSelected = false;

	boolean newEdge = false;
	boolean newVertex = false;	

	
	public static Color[] layerColors = new Color[] {
		Color.BLACK,
		Color.BLUE,
		Color.RED, //TODO: Find better Colors then the following:
		Color.YELLOW,
		Color.GREEN
	};
	
	NetList nl;
	
	public NetListPanel(NetList nl) {
		this.nl = nl;
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	/**
	 * Returns an image of the graph.
	 * @param zoom Zoom used for the image. Bigger values result in higher resolutions.
	 * If "null" the current zoom is used.
	 * @return Returns an image of the graph with a border of 5 pixel in most cases.
	 */
	public BufferedImage getImage(Double zoom) {
		if (zoom == null) zoom = getZoom();
		double oldZoom = getZoom();
		setZoom(zoom);
		
		long maxX = 0;
		long maxY = 0;
		for (Node node : getNodes()) {
			if (node.getX() > maxX)
				maxX = node.getX();
			if (node.getY() > maxY)
				maxY = node.getY();
		}
		for (Edge edge : getEdges()) {
			if (edge.getK1() > maxX)
				maxX = edge.getK1();
			if (edge.getK2() > maxY)
				maxY = edge.getK2();
		}		
		
		BufferedImage img = new BufferedImage((int) ((maxX + 2 * Node.getRadius() + 400) * getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 400) * getZoom()), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		
		if (!Configuration.getInstance().getGeneralConfig().exportTransparent()) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, img.getWidth(), img.getHeight());			
		}
		
		g.setStroke(new BasicStroke(Configuration.getInstance().getGeneralConfig().getLineWidth()));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (Node node : getNodes()) {
			node.paintYou(g);
		}
		for (Edge edge : getEdges()) {
			edge.paintEdge(g);			
		}
		for (Edge edge : getEdges()) {
			edge.paintEdgeLabel(g);			
		}
		
		img = cutImage(img, 5);
		setZoom(oldZoom);
		
		return img;
	}
	
	private List<Node> getNodes() {		
		return nl.nodes;
	}

	private List<Edge> getEdges() {		
		return nl.edges;
	}
	
	private BufferedImage cutImage(BufferedImage img, int offset) {
		int minX = img.getWidth();
		int minY = img.getHeight();
		int maxX = 0;
		int maxY = 0;		
		//System.out.println(img.getRGB(1, 1));
		for (int x=0; x<img.getWidth(); x++) {
			for(int y=0; y<img.getHeight(); y++) {				
				if (img.getRGB(x, y)!=0 && img.getRGB(x, y)!=-1) {
					if (x<minX) minX = x;
					if (y<minY) minY = y;
					if (x>maxX) maxX = x;
					if (y>maxY) maxY = y;
				}
			}
		}
		//System.out.println(Math.max(0, minX-offset)+","	+Math.max(0, minY-offset)+","+Math.min(maxX-minX+2*offset, img.getWidth())+"," +Math.min(maxY-minY+2*offset, img.getHeight()));
		return img.getSubimage(Math.max(0, minX-offset), 
				Math.max(0, minY-offset), 
				Math.min(maxX-minX+2*offset, img.getWidth()), 
				Math.min(maxY-minY+2*offset, img.getHeight()));
	}

	/**
	 * Calculates the size of the panel to view all nodes and resizes the panel.
	 */
	public int[] calculateSize() {
		int maxX = 0;
		int maxY = 0;
		for (int i = 0; i < getNodes().size(); i++) {
			if (getNodes().get(i).getX() > maxX)
				maxX = getNodes().get(i).getX();
			if (getNodes().get(i).getY() > maxY)
				maxY = getNodes().get(i).getY();
		}
		for (int i = 0; i < getEdges().size(); i++) {
			if (getEdges().get(i).getK1() > maxX)
				maxX = getEdges().get(i).getK1();
			if (getEdges().get(i).getK2() > maxY)
				maxY = getEdges().get(i).getK2();
		}		
		setPreferredSize(new Dimension(
				(int) ((maxX + 2 * Node.getRadius() + 30) * getZoom()),
				(int) ((maxY + 2 * Node.getRadius() + 30) * getZoom())));
		if (nl.updateGUI) {
			revalidate();
			repaint();
		}		
		return new int[] {maxX, maxY};
	}
	
	
	public double getZoom() {
		return nl.getZoom();
	}
	
	public void setZoom(double p) {
		nl.setZoom(p);;
	}
	
	private void showPopUp(MouseEvent e, Node node, Edge edge){
        NetListPopUpMenu menu = new NetListPopUpMenu(nl, node, edge);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		if (firstVertexSelected) {
			arrowHeadPoint = e.getPoint();
			repaint();
			return;
		}
		if (dragN.length==0 && dragE.length==0) { /* Dragging without objects creates a rectangular. */
			endPoint = new int[] {e.getX(), e.getY()};
			repaint();
			return;
		}

		for (int i : dragN) {
			if (!unAnchor && Configuration.getInstance().getGeneralConfig().getUnAnchor()) { 
				for (Edge edge : getEdges()) {
					if (edge.from == getNodes().get(i) || edge.to == getNodes().get(i)) {
						edge.fixed = false;
					}
				}
				unAnchor = true;
			}
			getNodes().get(i).setX( (int) ((e.getX()+offsetN[i][0]) / (double) getZoom()));
			getNodes().get(i).setY( (int) ((e.getY()+offsetN[i][1]) / (double) getZoom()));
			placeUnfixedNodes(getNodes().get(i));
		}

		for (int i : dragE) {		
			getEdges().get(i).setK1( (int) ((e.getX()+offsetE[i][0]) / (double) getZoom()));
			getEdges().get(i).setK2( (int) ((e.getY()+offsetE[i][1]) / (double) getZoom()));
		}

		calculateSize();
		repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	Point arrowHeadPoint = null;
	
	public void mouseMoved(MouseEvent e) {
		if (firstVertexSelected) {
			arrowHeadPoint = e.getPoint();
			repaint();
		}
	}

	protected int[][] offsetE;
	protected int[][] offsetN;
	protected int[] startingPoint = null;
	protected int[] endPoint = null;
	
	public void mousePressed(MouseEvent e) {		
		// Trigger PopUp
		if (e.isPopupTrigger()) {
			popUp(e);	
		}
		// Right-click stops placement of new nodes
		if (e.getButton()==MouseEvent.BUTTON2) {
			newVertex = false;
			nl.control.buttonNewNode.setSelected(false);
			return;
		}
		// Check whether to add new node
		if (newVertex && vertexSelected(e.getX(), e.getY())==null) {
			nl.addDefaultNode((int)(e.getX() / getZoom()) - Node.r, 
						(int) (e.getY() / getZoom()) - Node.r);
			nl.statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			repaint();
			return;
		}
		// Check whether to add new edge
		if (newEdge) {
			if (!firstVertexSelected) {
				firstVertex = vertexSelected(e.getX(), e.getY());
				if (firstVertex == null)
					return;
				firstVertexSelected = true;
				nl.statusBar.setText("Select a second node to which the edge should lead.");
			} else {
				Node secondVertex = vertexSelected(e.getX(), e.getY());
				if (secondVertex == null || secondVertex == firstVertex) {
					return;
				}	
				nl.setEdge(firstVertex, secondVertex, this);
				newEdge = false;
				arrowHeadPoint = null;
				firstVertexSelected = false;
				nl.statusBar.setText(GraphView.STATUSBAR_DEFAULT);
			}
			repaint();
			return;
		}
		
		
		// Drag'n'drop
		if (dragN.length!=0 || dragE.length!=0) {
			offsetN = new int[getNodes().size()][2];
			offsetE = new int[getEdges().size()][2];
			for (int i : dragN) {
				offsetN[i] = getNodes().get(i).offset(e.getX(), e.getY());
			}
			for (int i : dragE) {
				offsetE[i] = getEdges().get(i).offset(e.getX(), e.getY());
			}
		} else {
			for (int i = 0; i < getNodes().size(); i++) {
				if (getNodes().get(i).inYou(e.getX(), e.getY())) {
					dragN = new int[] {i};
					offsetN = new int[getNodes().size()][2];
					offsetN[i] = getNodes().get(i).offset(e.getX(), e.getY());
				}
			}
			for (int i = getEdges().size()-1; i >=0 ; i--) {
				if (getEdges().get(i).inYou(e.getX(), e.getY())) {
					dragN = new int[0];
					dragE = new int[] {i};
					offsetE = new int[getEdges().size()][2];
					offsetE[i] = getEdges().get(i).offset(e.getX(), e.getY());
				}
			}
		}
		
		// Double click opens dialog for changing nodes or edges. 
		if (e.getClickCount() == 2 && !nl.testingStarted) {			
			for (int i = getEdges().size()-1; i >=0 ; i--) {
				if (getEdges().get(i).inYou(e.getX(), e.getY())) {
					new UpdateEdge(getEdges().get(i), nl, nl.control);
					mouseReleased(null);
					repaint();
					return;
				}
			}
			for (int i = getNodes().size()-1; i >=0 ; i--) {
				if (getNodes().get(i).inYou(e.getX(), e.getY())) {
					new UpdateNode(getNodes().get(i), nl.control);
					mouseReleased(null);
					repaint();
					return;
				}
			}
		}
		
		startingPoint = new int[] {e.getX(), e.getY()};
		repaint();
	}
	
	public Node vertexSelected(int x, int y) {
		for (Node n : getNodes()) {
			if (n.inYou(x, y)) {
				return n;
			}
		}
		return null;
	}
	
	private void placeUnfixedNodes(Node node) {
		for (Edge e : getEdges()) {
			if ((e.from == node || e.to == node) && !e.isFixed()) {
				e.move();
			}
		}		
	}

	/**
	 * Unfortunately a double click resulting in opening a new dialog does not trigger a mouseReleased-event in the end.
	 * Therefore the method can be called with e=null whenever a dialog is opened that way.
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		dragN = new int[0];
		dragE = new int[0];
		
		if (e != null)	{
			if (endPoint!=null && startingPoint!=null && Configuration.getInstance().getGeneralConfig().experimentalFeatures()) {
				Vector<Node> nodes = new Vector<Node>();
				Vector<Edge> edges = new Vector<Edge>(); 
				for (Edge edge : getEdges()) {
					if (edge.containsYou(startingPoint, endPoint)) {
						edges.add(edge);
					}
				}
				for (Node node : getNodes()) {
					if (node.containsYou(startingPoint, endPoint)) {
						nodes.add(node);
					}
				}
				NetListSelectionPopUpMenu menu = new NetListSelectionPopUpMenu(nl, getNodes(), getEdges());
				repaint();
				menu.show(e.getComponent(), e.getX()-20, e.getY()-20);
			} else if (e.isPopupTrigger()) {
				popUp(e);	
			}
		}
		
		for(int i : dragE) {			
			getEdges().get(i).setFixed(true);
		}
		
		unAnchor = false;
		endPoint = null;
		if (e !=null && newEdge && firstVertexSelected) {				
			Node secondVertex = vertexSelected(e.getX(), e.getY());
			if (secondVertex == null || secondVertex == firstVertex) {
				return;
			}
			nl.setEdge(firstVertex, secondVertex, this);
			newEdge = false;
			arrowHeadPoint = null;
			firstVertexSelected = false;
			nl.statusBar.setText(GraphView.STATUSBAR_DEFAULT);

			repaint();
			return;
		}
	}
	
	public void popUp(MouseEvent e) {
		for (int i = 0; i < getNodes().size(); i++) {
			if (getNodes().get(i).inYou(e.getX(), e.getY())) {
				//TODO
				showPopUp(e, getNodes().get(i), null);
			}
		}
		for (int i = getEdges().size()-1; i >=0 ; i--) {
			if (getEdges().get(i).inYou(e.getX(), e.getY())) {
				showPopUp(e, null, getEdges().get(i));
			}
		}
	}
	
	/**
	 * We use paintComponent() instead of paint(), since the later one
	 * is not called by a revalidate of the scrollbars.
	 */
	public void paintComponent(Graphics g) {
		// Apart from speed issues we shouldn't draw the graph while it is modified.
		if (!nl.updateGUI) return;
		/* TODO Actually we also have to check whether paintComponent is in progress,
		 * otherwise for example "for (Node node : nodes) { node.paintYou(g) }"
		 * will throw a ConcurrentModificationException.
		 */
		super.paintComponent(g);		
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		g.setColor(Color.LIGHT_GRAY);
		if (grid>1) {
			for(int x=(int)(Node.r*getZoom()); x < getWidth(); x += grid*getZoom()) {				
				g.drawLine(x, 0, x, getHeight());	
			}
			for(int x=(int)(Node.r*getZoom()); x > 0; x -= grid*getZoom()) {				
				g.drawLine(x, 0, x, getHeight());	
			}
			for(int y=(int)(Node.r*getZoom()); y < getHeight(); y += grid*getZoom()) {
				g.drawLine(0, y, getWidth(), y);
			}
			for(int y=(int)(Node.r*getZoom()); y > 0; y -= grid*getZoom()) {
				g.drawLine(0, y, getWidth(), y);
			}
		}
		BasicStroke stroke = new BasicStroke(Configuration.getInstance().getGeneralConfig().getLineWidth());
		((Graphics2D)g).setStroke(stroke);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (Node node : getNodes()) {
			node.paintYou(g);
		}
		for (Edge edge : getEdges()) {
			edge.paintEdge(g);			
		}
		for (Edge edge : getEdges()) {
			edge.paintEdgeLabel(g);			
		}
		
		/* Draw selection box if something is selected */
		if (endPoint!=null && startingPoint!=null && Configuration.getInstance().getGeneralConfig().experimentalFeatures()) {
			int x = Math.min(startingPoint[0], endPoint[0]);
			int y = Math.min(startingPoint[1], endPoint[1]);
			int width = Math.abs(startingPoint[0] - endPoint[0]);
			int height = Math.abs(startingPoint[1] - endPoint[1]);
			((Graphics2D)g).setStroke(new BasicStroke(1));
			((Graphics2D)g).setPaint(new Color(0, 0, 255, 30));			
			((Graphics2D)g).fillRect(x, y, width, height);
			((Graphics2D)g).setPaint(new Color(0, 0, 255));
			((Graphics2D)g).drawRect(x, y, width, height);			
		}
		
		if (firstVertexSelected && firstVertex != null && arrowHeadPoint != null) { //TODO Insert *getZoom()
			double a1 = firstVertex.getX()+ Node.getRadius();
			double a2 = firstVertex.getY()+ Node.getRadius();
			double c1 = arrowHeadPoint.getX()/getZoom();
			double c2 = arrowHeadPoint.getY()/getZoom();
			if (!(firstVertex.inYou((int)c1, (int)c2))) {
				double dx = a1 - c1;
				double dy = a2 - c2;
				double d = Math.sqrt(dx * dx + dy * dy);
				a1 = a1 - ((Node.getRadius()) * dx / d);
				a2 = a2 - ((Node.getRadius()) * dy / d);					
				g.setColor(Color.DARK_GRAY);
				GraphDrawHelper.malVollenPfeil(g, (int)(a1*getZoom()), (int)(a2*getZoom()), (int)(c1*getZoom()), (int)(c2*getZoom()), (int) (8 * getZoom()), 35);
				g.setColor(Color.BLACK);
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

	public void reset() {
		firstVertexSelected = false;
		newVertex = false;
		newEdge = false;
	}
	
}
