package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JPanel;

import org.mutoss.config.Configuration;
import org.mutoss.gui.RControl;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class Node {
	
	public Vector<NodeListener> listener = new Vector<NodeListener>(); 
	int x;
	int y;
	private String name;
	boolean fix = false;
	boolean drag = false;
	VS vs;
	private double weight;
	private String stringW = "";
	private Color color = Color.WHITE;
	boolean rejected = false;

	public static int r = 25;

	public static void setRadius(int radius) {
		r = radius;
	}

	static int count = 1;
	
	TeXIcon iconName, iconWeight;

	public Node(String name, int x, int y, double alpha, VS vs) {
		count++;
		this.vs = vs;
		setName(name);
		setX(x);
		setY(y);		
		setWeight(alpha, null);		
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		x = ((x+ (int)(0.5*grid)) / grid)*grid;
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		int grid = Configuration.getInstance().getGeneralConfig().getGridSize();
		y = ((y+ (int)(0.5*grid)) / grid)*grid;
		this.y = y;
	}

	public void paintYou(Graphics g) {
		if (rejected && !Configuration.getInstance().getGeneralConfig().showRejected()) return;
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D rc;
		g2d.setColor(getColor());
		// if (this.fix) {	g2d.setColor(new Color(50, 255, 50)); }		
		Ellipse2D e = new Ellipse2D.Double();
		e.setFrame(x * vs.getZoom(), 
				y * vs.getZoom(), 
				r * 2 * vs.getZoom(), 
				r * 2 * vs.getZoom());
		g2d.fill(e);
		g2d.setColor(new Color(0, 0, 0));
		e.setFrame(x * vs.getZoom(), 
				y * vs.getZoom(), 
				r * 2 * vs.getZoom(), 
				r * 2 * vs.getZoom());
		g2d.draw(e);

		if (!Configuration.getInstance().getGeneralConfig().useJLaTeXMath()) {
			g2d.setFont(new Font("Arial", Font.PLAIN, (int) (12 * vs.getZoom())));
			FontRenderContext frc = g2d.getFontRenderContext();

			rc = g2d.getFont().getStringBounds(name, frc);
			g2d.drawString(name, 
					(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2), 
					(float) ((y + r - 0.25*r) * vs.getZoom())); // +rc.getHeight()/2));

			rc = g2d.getFont().getStringBounds(getWS(), frc);
			g2d.drawString(getWS(),
					(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2),
					(float) ((y + 1.5 * r) * vs.getZoom())); 
		} else {		
			iconName.paintIcon(Edge.panel, g2d,
					(int) ((x + r) * vs.getZoom() - iconName.getIconWidth() / 2), 
					(int) ((y + r - 0.6*r) * vs.getZoom()));	

			iconWeight.paintIcon(Edge.panel, g2d,
					(int) ((x + r) * vs.getZoom() - iconWeight.getIconWidth() / 2), 
					(int) ((y + 1.1 * r) * vs.getZoom()));
		}
	}

	DecimalFormat format = new DecimalFormat("#.###");
	
	private String getWS() {		
		return stringW;
	}

	public static int getRadius() {
		return r;
	}

	public boolean inYou(int x, int y) {
		return ((x / vs.getZoom() - this.x - r)
				* (x / vs.getZoom() - this.x - r)
				+ (y / vs.getZoom() - this.y - r)
				* (y / vs.getZoom() - this.y - r) <= (r * r));
	}

	public void mouseRelease(MouseEvent e) {
	}

	public void setWeight(double w, NodeListener me) {
		this.weight = w;	
		if (!Configuration.getInstance().getGeneralConfig().showFractions()) {
			stringW = format.format(w);
		} else {
			stringW = RControl.getFraction(w, 3);
			if (stringW.length()>7) {
				stringW = format.format(w);
			}
		}
		
		iconWeight = Edge.getTeXIcon(stringW, (int) (14 * vs.getZoom()));
		
		for (NodeListener l : listener) {
			if (me!=l) {
				l.updated(this);
			}
		}
		vs.nl.repaint();
	}

	public double getWeight() {
		return weight;
	}

	public void setColor(Color color) {
		this.color = color;
		vs.repaint();
	}

	public Color getColor() {
		if (rejected) return Color.MAGENTA;
		return color;
	}

	public void addNodeListener(NodeListener l) {
		listener.add(l);		
	}

	public String getName() {		
		return name;
	}

	public void setName(String name) {
		this.name = name;	
		TeXFormula formula = new TeXFormula("\\mathbf{"+name+"}"); 
		iconName = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (14 * vs.getZoom()));
	}
	
	public boolean isRejected() {		
		return rejected;
	}

	public void reject() {
		color = Color.MAGENTA;
		rejected = true;
	}

}
