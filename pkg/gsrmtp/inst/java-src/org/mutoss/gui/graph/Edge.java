package org.mutoss.gui.graph;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import org.af.commons.images.GraphDrawHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Edge {

	public Node von;
	public Node nach;
	Double w;
	VS vs;
	public boolean curve = false;
	private static final Log logger = LogFactory.getLog(Edge.class);

	public Edge(Node von, Node nach, Double w, VS vs) {
		this.von = von;
		this.nach = nach;
		this.w = w;
		this.vs = vs;
	}
	
	Graphics2D g2d;
	
	public void paintYou(Graphics g) {
		long x1, x2, y1, y2;
		x1 = von.x + Node.getRadius();
		x2 = nach.x + Node.getRadius();
		y1 = von.y + Node.getRadius();
		y2 = nach.y + Node.getRadius();
		if (von != nach) {
			long dx = x1 - x2;
			long dy = y1 - y2;
			double d = Math.sqrt(dx * dx + dy * dy);
			x1 = x1 - (int) (Node.getRadius() * dx / d);
			x2 = x2 + (int) (Node.getRadius() * dx / d);
			y1 = y1 - (int) (Node.getRadius() * dy / d);
			y2 = y2 + (int) (Node.getRadius() * dy / d);		
			
			if (vs.directed) {
				GraphDrawHelper.malVollenPfeil(g, 
						(int) (x1 * vs.getZoom()),
						(int) (y1 * vs.getZoom()), 
						(int) (x2 * vs.getZoom()), 
						(int) (y2 * vs.getZoom()), 
						(int) (8 * vs.getZoom()), curve);
			} else {
				g.drawLine(
						(int) (x1 * vs.getZoom()), 
						(int) (y1 * vs.getZoom()), 
						(int) (x2 * vs.getZoom()),
						(int) (y2 * vs.getZoom()));
			}
			if (g2d == null) {
				g2d = (Graphics2D) g;
				/*g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);*/
			}
			g2d = (Graphics2D) g;
			g2d.setFont(new Font("Arial", Font.PLAIN, (int) (16 * vs.getZoom())));
			FontRenderContext frc = g2d.getFontRenderContext();		
			String s = getWS();
			Rectangle2D rc = g2d.getFont().getStringBounds(s, frc);
			int[] c = GraphDrawHelper.getDrawPoints(x1, y1, x2, y2);
			long a = (x1+x2)/2;
			long b = (y1+y2)/2;
			if (curve) {
				a = c[0]; b = c[1];
			}			
			g2d.drawString(s, 
					(float) ((a - rc.getWidth() / 2)* vs.getZoom()), 
					(float) ((b - rc.getHeight() / 2)* vs.getZoom()));	
			//g2d.drawRect((int)(a - rc.getWidth() / 2), (int)(b - rc.getHeight()* 3 / 2), (int)rc.getWidth(), (int)rc.getHeight());
		} else { // Edge is a loop:
			int r = (int) (Node.getRadius());
			g.drawArc(
					(int) ((x1 - 109) * vs.getZoom()),
					(int) ((y1 - (r / 2)) * vs.getZoom()), 
					(int) (100 * vs.getZoom()), 
					(int) (r * vs.getZoom()), 
					45, 270);
			if (vs.directed) {
				// ToDo: Kanten mit Pfeilspitze
			}
		}
	}
	
	private String getWS() {
		String s = (w.toString().equals("NaN")) ? "ε" : ""+w;	
		return s.substring(0, Math.min(5,s.length()));
	}

	public boolean inYou(int x, int y) {
		long x1, x2, y1, y2;
		x1 = von.x + Node.getRadius();
		x2 = nach.x + Node.getRadius();
		y1 = von.y + Node.getRadius();
		y2 = nach.y + Node.getRadius();
		long dx = x1 - x2;
		long dy = y1 - y2;
		double d = Math.sqrt(dx * dx + dy * dy);
		x1 = x1 - (int) (Node.getRadius() * dx / d);
		x2 = x2 + (int) (Node.getRadius() * dx / d);
		y1 = y1 - (int) (Node.getRadius() * dy / d);
		y2 = y2 + (int) (Node.getRadius() * dy / d);
		long a = (x1+x2)/2;
		long b = (y1+y2)/2;
		if (curve) {
			int[] c = GraphDrawHelper.getDrawPoints(x1, y1, x2, y2);
			a = c[0]; b = c[1];
		}
		String s = (w.toString().equals("NaN")) ? "ε" : ""+w;
		FontRenderContext frc = g2d.getFontRenderContext();	
		Rectangle2D rc = (new Font("Arial", Font.PLAIN, (int) (16 * vs.getZoom()))).getStringBounds(s, frc);
		int TOLERANCE = 4; 
		return (x/ vs.getZoom()>a-rc.getWidth()/2-TOLERANCE)&&(x/ vs.getZoom()<a+rc.getWidth()/2+TOLERANCE)&&(y/ vs.getZoom()<b- rc.getHeight()*1/ 2+TOLERANCE)&&(y/ vs.getZoom()>b-rc.getHeight()*3/2-TOLERANCE);
	}

	public void setW(Double w) {
		this.w = w;
		vs.nl.repaint();
	}


}
