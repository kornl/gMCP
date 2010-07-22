package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;


/**
 * 
 * \brief Klasse für Knoten in der NetzListe
 * 
 * @version 29 Dec 2001
 * @author G9-Gui
 * @see NetzListe
 */

public class Node {
	
	public Vector<NodeListener> listener = new Vector<NodeListener>(); 
	public long nr;
	public long x;
	public long y;
	public String name;
	boolean fix = false;
	boolean drag = false;
	VS vs;
	int degree;
	public double w;
	private Color color = Color.WHITE;

	public static int r = 25;

	/**
	 * Ändert den Radius der Knoten
	 * 
	 * @param radius
	 *            Neuer Radius der Knoten.
	 */

	public static void setRadius(int radius) {
		r = radius;
	}

	public Node(int nr, String name, int x, int y, VS vs) {
		this.nr = nr;
		this.name = name;
		this.x = x;
		this.y = y;
		this.vs = vs;
		degree = 0;
	}

	/**
	 * Gibt den Knoten auf g aus.
	 * 
	 * @param g
	 *            Graphics-Objekt, auf das gemalt wird.
	 */
	public void paintYou(Graphics g) {
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
		if (vs.moreInfo) {
			Line2D l = new Line2D.Double();
			l.setLine(x * vs.getZoom(), 
					(y + r) * vs.getZoom(), 
					(x + r * 2)	* vs.getZoom(), 
					(y + r) * vs.getZoom());
			g2d.draw(l);
			l.setLine((x + r) * vs.getZoom(), 
					(y + r) * vs.getZoom(), 
					(x + r)	* vs.getZoom(), 
					(y + r * 2) * vs.getZoom());
			g2d.draw(l);

			g2d.setFont(new Font("Arial", Font.PLAIN, (int) (8 * vs.getZoom())));
			FontRenderContext frc = g2d.getFontRenderContext();
			if (vs.shownr) {
				rc = g2d.getFont().getStringBounds("" + nr, frc);
				g2d.drawString("" + nr, 
						(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2), 
						(float) ((y + r) * vs.getZoom() - rc.getHeight() / 2));
			} else {
				rc = g2d.getFont().getStringBounds(name, frc);
				g2d.drawString("" + name, 
						(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2), 
						(float) ((y + r) * vs.getZoom() - rc.getHeight() / 2));
			}
			rc = g2d.getFont().getStringBounds("" + degree, frc);
			g2d.drawString("" + degree,
					(float) ((x + r * 0.6) * vs.getZoom() - rc.getWidth() / 2),
					(float) ((y + 1.5 * r) * vs.getZoom()));
			// rc = g2d.getFont().getStringBounds("" + (start+dauer), frc);
			// g2d.drawString(""+(start+dauer), (float)
			// ((x+1.4*r)*vs.getZoom()-rc.getWidth()/2), (float)
			// ((y+1.5*r)*vs.getZoom()));
		} else {
			g2d.setFont(new Font("Arial", Font.PLAIN, (int) (12 * vs
							.getZoom())));
			FontRenderContext frc = g2d.getFontRenderContext();
			if (vs.shownr) {
				rc = g2d.getFont().getStringBounds("" + nr, frc);
				g2d.drawString("" + nr, 
						(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2), 
						(float) ((y + r - 0.25*r) * vs.getZoom())); // +rc.getHeight()/2));
			} else {
				rc = g2d.getFont().getStringBounds(name, frc);
				g2d.drawString(name, 
						(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2), 
						(float) ((y + r - 0.25*r) * vs.getZoom())); // +rc.getHeight()/2));
			}
			rc = g2d.getFont().getStringBounds(getWS(), frc);
			g2d.drawString(getWS(),
					(float) ((x + r) * vs.getZoom() - rc.getWidth() / 2),
					(float) ((y + 1.5 * r) * vs.getZoom()));
		}
	}

	private String getWS() {
		String w = ""+getW();		
		return w.substring(0, Math.min(5,w.length()));
	}

	/**
	 * Gibt den Radius der Knoten zurück
	 * 
	 * @return Radius der Knoten.
	 */
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

	public void setW(double w, NodeListener me) {
		this.w = w;	
		for (NodeListener l : listener) {
			if (me!=l) {
				l.updated(this);
			}
		}
		vs.nl.repaint();
	}

	public double getW() {
		return w;
	}

	public void setColor(Color color) {
		this.color = color;
		vs.repaint();
	}

	public Color getColor() {
		return color;
	}

	public void addNodeListener(NodeListener l) {
		listener.add(l);		
	}

}
