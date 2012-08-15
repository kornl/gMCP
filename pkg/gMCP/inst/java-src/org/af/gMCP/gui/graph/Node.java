package org.af.gMCP.gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Vector;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.RControl;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class Node {
	
	static DecimalFormat format = new DecimalFormat("#.####");
	static DecimalFormat formatSmall = new DecimalFormat("#.###E0");
	public static int r = 25;	
	private Color color = Color.WHITE;
	TeXIcon iconName, iconWeight;
	int lastFontSize = 14;
	public Vector<NodeListener> listener = new Vector<NodeListener>();

	Double localPower = null;
	
	private String name;
	NetList nl;
	
	private boolean rejectable = false;

	boolean rejected = false;

	private String stringW = "";	
	private double weight;

	int x;
	
	int y;
	
	public Node(String name, int x, int y, double alpha, NetList vs) {
		this.nl = vs;
		setName(name);
		setX(x);
		setY(y);		
		setWeight(alpha, null);		
	}

	public void addNodeListener(NodeListener l) {
		listener.add(l);		
	}

	public Color getColor() {
		if (rejected) return Color.MAGENTA;
		return color;
	}

	public String getName() { return name; }
	
	public String getRName() { return name.replaceAll("\\\\", "\\\\\\\\"); }

	public double getWeight() { return weight; }
	
	private String getWS() { return stringW; }

	public int getX() { return x; }

	public int getY() { return y; }

	public boolean inYou(int x, int y) {
		return ((x / nl.getZoom() - this.x - r)
				* (x / nl.getZoom() - this.x - r)
				+ (y / nl.getZoom() - this.y - r)
				* (y / nl.getZoom() - this.y - r) <= (r * r));
	}
	

	public boolean containsYou(int[] start, int[] end) {
		return Math.min(start[0], end[0])/ nl.getZoom()<=x+r && Math.max(start[0], end[0])/ nl.getZoom()>=x-r
				&& Math.min(start[1], end[1])/ nl.getZoom()<=y+r && Math.max(start[1], end[1])/ nl.getZoom()>=y-r; 
	}

	public static int getRadius() { return r; }
	
	public static void setRadius(int radius) { r = radius; }

	public boolean isRejectable() {
		return rejectable && !rejected;
	}

	public boolean isRejected() { 	return rejected; }
	
	public int[] offset(int x2, int y2) {
		return new int[] {(int) (x* nl.getZoom())-x2, (int) (y* nl.getZoom())-y2};
	}

	public void paintYou(Graphics g) {
		if (rejected && !Configuration.getInstance().getGeneralConfig().showRejected()) return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(new Font("Arial", Font.PLAIN, (int) (12 * nl.getZoom())));		
		FontRenderContext frc = g2d.getFontRenderContext();		
		Rectangle2D rc;
		g2d.setColor(getColor());
		// if (this.fix) {	g2d.setColor(new Color(50, 255, 50)); }		
		Ellipse2D e = new Ellipse2D.Double();
		e.setFrame(x * nl.getZoom(), 
				y * nl.getZoom(), 
				r * 2 * nl.getZoom(), 
				r * 2 * nl.getZoom());
		g2d.fill(e);
		g2d.setColor(new Color(0, 0, 0));
		e.setFrame(x * nl.getZoom(), 
				y * nl.getZoom(), 
				r * 2 * nl.getZoom(), 
				r * 2 * nl.getZoom());
		g2d.draw(e);

		if (localPower != null) {			
			String s = format.format(localPower);
			rc = g2d.getFont().getStringBounds(s, frc);
			g2d.drawString(s ,
					(float) ((x + r) * nl.getZoom() - rc.getWidth() / 2),
					(float) ((y + 2.5 * r) * nl.getZoom())); 
		}
		
		if (!Configuration.getInstance().getGeneralConfig().useJLaTeXMath()) {			

			rc = g2d.getFont().getStringBounds(name, frc);
			g2d.drawString(name, 
					(float) ((x + r) * nl.getZoom() - rc.getWidth() / 2), 
					(float) ((y + r - 0.25*r) * nl.getZoom())); // +rc.getHeight()/2));

			rc = g2d.getFont().getStringBounds(getWS(), frc);
			g2d.drawString(getWS(),
					(float) ((x + r) * nl.getZoom() - rc.getWidth() / 2),
					(float) ((y + 1.5 * r) * nl.getZoom())); 
		} else {		
			if (lastFontSize != (int) (14 * nl.getZoom())) {
				lastFontSize = (int) (14 * nl.getZoom());
				iconWeight = Edge.getTeXIcon(this.nl.control.getGraphGUI(), stringW, lastFontSize);
				TeXFormula formula = new TeXFormula("\\mathbf{"+name+"}");
				iconName = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, lastFontSize);
			}
			iconName.paintIcon(Edge.panel, g2d,
					(int) ((x + r) * nl.getZoom() - iconName.getIconWidth() / 2), 
					(int) ((y + r - 0.6*r) * nl.getZoom()));	

			iconWeight.paintIcon(Edge.panel, g2d,
					(int) ((x + r) * nl.getZoom() - iconWeight.getIconWidth() / 2), 
					(int) ((y + 1.1 * r) * nl.getZoom()));
		}
		
	}

	/**
	 * This method will save the graph,
	 * call rejectNode in R and show the result.
	 * All nodeListeners (i.e. PPanel) are notified.
	 */
	public void reject() {		
		nl.acceptNode(this);
		for (NodeListener l : listener) {
			l.reject();
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
		nl.repaint();
	}

	public void setLocalPower(double d) {
		localPower = d;	
	}
	
	public void setName(String name) {
		this.name = name;	
		TeXFormula formula = new TeXFormula("\\mathbf{"+name+"}"); 
		iconName = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (14 * nl.getZoom()));
		nl.graphHasChanged();
	}

	public void setRejectable(boolean rejectable) {
		if (rejectable) {
			setColor(new Color(50, 255, 50));
		} else {
			setColor(Color.WHITE);
		}
		this.rejectable = rejectable;
	}

	public void setWeight(double w, NodeListener me) {
		this.weight = w;	
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		if (!Configuration.getInstance().getGeneralConfig().showFractions()) {
			stringW = format.format(w);
		} else {
			if (weight!=0 && weight < Math.pow(0.1, 3)) {
				stringW = formatSmall.format(weight);
			} else {
				stringW = RControl.getFraction(w, 5);
				if (stringW.length()>7) {
					stringW = "\\sim "+format.format(w);
				}
			}
		}
		
		iconWeight = Edge.getTeXIcon(this.nl.control.getGraphGUI(), stringW, (int) (14 * nl.getZoom()));
		
		for (NodeListener l : listener) {
			if (me!=l) {
				l.updated(this);
			}
		}
		nl.graphHasChanged();
		nl.repaint();
	}
	
	public void setX(int x) {
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		x = ((x+ (int)(0.5*grid)) / grid)*grid;
		this.x = x;
	}

	public void setY(int y) {
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		y = ((y+ (int)(0.5*grid)) / grid)*grid;
		this.y = y;
	}

	public String toString() {		
		return name+" (w: "+getWS()+")";
	}

}
