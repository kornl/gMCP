package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import org.af.commons.images.GraphDrawHelper;
import org.af.commons.images.GraphException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Edge {

	public Node von;
	public Node nach;
	Double w;
	VS vs;
	public boolean curve = false;
	private static final Log logger = LogFactory.getLog(Edge.class);
	int k1, k2;

	public Edge(Node von, Node nach, Double w, VS vs, int k1, int k2) {
		this.von = von;
		this.nach = nach;
		this.w = w;
		this.vs = vs;
		this.k1 = k1;
		this.k2 = k2;
	}
	
	public Edge(Node von, Node nach, Double w, VS vs) {		
		int x1, x2, y1, y2;
		x1 = von.getX() + Node.getRadius();
		x2 = nach.getX() + Node.getRadius();
		y1 = von.getY() + Node.getRadius();
		y2 = nach.getY() + Node.getRadius();
		k1 = (x1+x2)/2;
		k2 = (y1+y2)/2;
		this.von = von;
		this.nach = nach;
		this.w = w;
		this.vs = vs;
	}
	
	public Edge(Node von, Node nach, Double w, VS vs, boolean curve) {
		this(von, nach, w, vs);
		int x1, x2, y1, y2;
		x1 = von.getX() + Node.getRadius();
		x2 = nach.getX() + Node.getRadius();
		y1 = von.getY() + Node.getRadius();
		y2 = nach.getY() + Node.getRadius();
		if (curve) {
			double d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
			double y = y2-y1;
			double x = x2-x1;
			double alpha;
			if (x==0 && y<0) {
				alpha = 90;
			} else if (x==0&&y>0) {
				alpha = -90;
			} else {
				alpha = Math.atan(-y/x)/(Math.PI*2)*360+((x<0)?180:0);
			}
			alpha = alpha + 10;
			k1 = x1 + (int)(Math.cos(alpha*(Math.PI*2)/360)*d/2);
			k2 = y1 - (int)(Math.sin(alpha*(Math.PI*2)/360)*d/2);
		} else {
			k1 = (x1+x2)/2;
			k2 = (y1+y2)/2;				
		}
	}

	Graphics2D g2d;
	FontRenderContext frc = null;
	
	public void paintYou(Graphics g) {
		int x1, x2, y1, y2;
		x1 = von.x + Node.getRadius();
		x2 = nach.x + Node.getRadius();
		y1 = von.y + Node.getRadius();
		y2 = nach.y + Node.getRadius();
		if (von != nach) {
			int dx = x1 - k1;
			int dy = y1 - k2;
			double d = Math.sqrt(dx * dx + dy * dy);
			x1 = x1 - (int) (Node.getRadius() * dx / d);
			y1 = y1 - (int) (Node.getRadius() * dy / d);
			dx = k1 - x2;
			dy = k2 - y2;
			d = Math.sqrt(dx * dx + dy * dy);			
			x2 = x2 + (int) (Node.getRadius() * dx / d);
			y2 = y2 + (int) (Node.getRadius() * dy / d);		
			
			
			if (g2d == null) {
				g2d = (Graphics2D) g;
				/*g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);*/
			}
			g2d = (Graphics2D) g;			

			GraphDrawHelper.drawEdge(g,	(int) (x1 * vs.getZoom()), (int) (y1 * vs.getZoom()), 
					(int) (k1* vs.getZoom()),
					(int) (k2 * vs.getZoom()),
					(int) (x2 * vs.getZoom()), (int) (y2 * vs.getZoom()), 
					(int) (8 * vs.getZoom()), 35, true);
			
			//QuadCurve2D quadcurve = new QuadCurve2D.Float(x1, y1, k1, k2 ,x2, y2);
			//g2d.draw(quadcurve);
			
			g2d.setFont(new Font("Arial", Font.PLAIN, (int) (16 * vs.getZoom())));
			frc = g2d.getFontRenderContext();		
			String s = getWS();
			Rectangle2D rc = g2d.getFont().getStringBounds(s, frc);
			g2d.setColor(new Color(0.99f,0.99f,0.99f));
			g2d.fillRect((int)((k1* vs.getZoom() - rc.getWidth() / 2)), (int)((k2* vs.getZoom() - rc.getHeight()* 3 / 2)), (int)((rc.getWidth()+5)), (int)((rc.getHeight()+5)));
			g2d.setColor(Color.BLACK);
			
			g2d.drawString(s, 
					(float) ((k1* vs.getZoom() - rc.getWidth() / 2)), 
					(float) ((k2* vs.getZoom() - rc.getHeight() / 2)));

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
	
	DecimalFormat format = new DecimalFormat("#.###");
	
	private String getWS() {
		if (w.toString().equals("NaN")) return "ε";	
		return format.format(w);
	}

	public boolean inYou(int x, int y) {
		String s = getWS();
		FontRenderContext frc = g2d.getFontRenderContext();	
		Rectangle2D rc = (new Font("Arial", Font.PLAIN, (int) (16 * vs.getZoom()))).getStringBounds(s, frc);
		int TOLERANCE = 4; 
		return (x/ vs.getZoom()>k1-rc.getWidth()/2-TOLERANCE)&&(x/ vs.getZoom()<k1+rc.getWidth()/2+TOLERANCE)&&(y/ vs.getZoom()<k2- rc.getHeight()*1/ 2+TOLERANCE)&&(y/ vs.getZoom()>k2-rc.getHeight()*3/2-TOLERANCE);
	}

	public void setW(Double w) {
		this.w = w;
		vs.nl.repaint();
	}

	public int getK1() {
		return k1;
	}

	public void setK1(int k1) {
		double correction = 0;
		/*if (frc != null) {					
			Rectangle2D rc = g2d.getFont().getStringBounds(getWS(), frc);
			correction = rc.getWidth()/2;
		}*/
		this.k1 = k1 + (int) correction;
		if (this.k1 < 0) this.k1 = 0;
	}

	public int getK2() {
		return k2;
	}
	
	public int getBendLeft() {
		int x1, x2, y1, y2;
		x1 = von.getX() + Node.getRadius();
		x2 = nach.getX() + Node.getRadius();
		y1 = von.getY() + Node.getRadius();
		y2 = nach.getY() + Node.getRadius();
		double[] m;
		try {
			m = GraphDrawHelper.getCenter(x1, y1, k1, k2, x2, y2);
		} catch (GraphException e) {
			return 0; // Seriously, this is the right answer!
		}
		double[] phi = GraphDrawHelper.getAngle(x1, y1, k1, k2, x2, y2, m[0], m[1]);
		double gamma;
		if ((x1-x2)==0) {
			gamma = 90 + ((y2-y1>0)?0:180);
		} else {
			gamma = Math.atan((-y1+y2)/(x1-x2))*360/(2*Math.PI)+((x1-x2<0)?180:0);
		}
		return ((int)(phi[2]+(phi[1]>0?180:0)+90-gamma)+360)%360;
	}

	public double getPos() {
		int x1, x2, y1, y2;
		x1 = von.getX() + Node.getRadius();
		x2 = nach.getX() + Node.getRadius();
		y1 = von.getY() + Node.getRadius();
		y2 = nach.getY() + Node.getRadius();		
		double[] m;
		try {
			m = GraphDrawHelper.getCenter(x1, y1, k1, k2, x2, y2);
			double d = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			double r = Math.sqrt((m[0]-x1)*(m[0]-x1)+(m[1]-y1)*(m[1]-y1));
			if (2*Math.PI*r/360>6*d/200) throw new GraphException("Edge is too linear.");	
		} catch (GraphException e) {			
			double n2 = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			double k = Math.sqrt((k1-x1)*(k1-x1)+(k2-y1)*(k2-y1));
			if (k>n2) return 1;
			return (k/n2);			
		}
		double[] phi = GraphDrawHelper.getAngle(x1, y1, k1, k2, x2, y2, m[0], m[1]);
		double phiA = phi[2];
		double phiC = phi[3];
		double phiK = phi[4];		
		if (phi[1]*(phi[0]==phi[2]?1:-1)>0) {
			if (phiK<phiA) phiK = phiK + 360;
			if (phiC<phiK) phiC = phiC + 360;
			return ((double)(phiK-phiA))/((double)(phiC-phiA));
		} else {
			if (phiK>phiA) phiK = phiK - 360;
			if (phiC>phiK) phiC = phiC - 360;
			return ((double)(phiK-phiA))/((double)(phiC-phiA));
		}
	}

	public Double getW() {
		return w;
	}

	public void setK2(int k2) {
		double correction = 0;
		if (frc != null) {					
			Rectangle2D rc = g2d.getFont().getStringBounds(getWS(), frc);
			correction = rc.getHeight()/2;
		}
		this.k2 = k2 + (int) correction;
		if (this.k2 < 0) this.k2 = 0;
	}

}
