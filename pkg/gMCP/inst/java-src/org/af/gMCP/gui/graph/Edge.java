package org.af.gMCP.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.af.commons.images.GraphDrawHelper;
import org.af.commons.images.GraphException;
import org.af.gMCP.config.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class Edge {

	private static final Log logger = LogFactory.getLog(Edge.class);
	public static Component panel = new JPanel();
	public boolean curve = false;
	FontRenderContext frc = null;
	Graphics2D g2d;
	int k1, k2;
	public Node to;
	public Node from;
	public boolean fixed = false;
	public Color color = Color.BLACK;
	
	NetList nl;
	
	private EdgeWeight ew;
	
	int lastFontSize = 16;

	public Edge(Node von, Node nach, Double w, NetList nl) {		
		int x1, x2, y1, y2;
		x1 = von.getX() + Node.getRadius();
		x2 = nach.getX() + Node.getRadius();
		y1 = von.getY() + Node.getRadius();
		y2 = nach.getY() + Node.getRadius();
		k1 = x1 + (x2-x1)/4; //(x1+x2)/2;
		k2 = y1 + (y2-y1)/4; //(y1+y2)/2;
		this.from = von;
		this.to = nach;
		this.ew = new EdgeWeight(w);
		this.nl = nl;
	}
	
	public static int[] getK(Node from, Node to, boolean curve) {
		int x1, x2, y1, y2, k1, k2;
		x1 = from.getX() + Node.getRadius();
		x2 = to.getX() + Node.getRadius();
		y1 = from.getY() + Node.getRadius();
		y2 = to.getY() + Node.getRadius();
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
			alpha = alpha + 15;
			k1 = x1 + (int)(Math.cos(alpha*(Math.PI*2)/360)*d/2);
			k2 = y1 - (int)(Math.sin(alpha*(Math.PI*2)/360)*d/2);
		} else {
			if (Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))>200) { // For long edges a placement at the beginning is good.
				k1 = x1 + (x2-x1)/4;
				k2 = y1 + (y2-y1)/4;
			} else {                 // But for short edges we prefer pleacement in the middle. 
				k1 = x1 + (x2-x1)/2;
				k2 = y1 + (y2-y1)/2;
			}
		}
		return new int[] {k1, k2};
	}
	
	public Edge(Node from, Node to, Double w, NetList nl, boolean curve) {
		this(from, to, w, nl);
		int[] k = getK(from, to, curve);
		k1 = k[0];
		k2 = k[1];
	}
	
	public Edge(Node von, Node nach, Double w, NetList nl, int k1, int k2) {
		this.from = von;
		this.to = nach;
		this.ew = new EdgeWeight(w);
		this.nl = nl;
		this.k1 = k1;
		this.k2 = k2;
	}
	
	public Edge(Node from, Node to, String wStr, NetList nl, boolean curve) {
		this(from, to, new EdgeWeight(wStr), nl, curve);
	}

	public Edge(Node from, Node to, String wStr, NetList nl, int i, int j) {
		this(from, to, new EdgeWeight(wStr), nl, i, j);	
	}

	public Edge(Node from, Node to, EdgeWeight ew, NetList nl, int k1, int k2) {
		this(from, to, 0d, nl, k1, k2);
		this.ew = ew;
	}

	public Edge(Node from, Node to, EdgeWeight ew, NetList nl, boolean curve) {
		this(from, to, 0d, nl, curve);
		this.ew = ew;
	}

	public int getBendLeft() {
		int x1, x2, y1, y2;
		x1 = from.getX() + Node.getRadius();
		x2 = to.getX() + Node.getRadius();
		y1 = from.getY() + Node.getRadius();
		y2 = to.getY() + Node.getRadius();
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
	
	public int getK1() {
		return k1;
	}

	public int getK2() {
		return k2;
	}

	public double getPos() {
		int x1, x2, y1, y2;
		x1 = from.getX() + Node.getRadius();
		x2 = to.getX() + Node.getRadius();
		y1 = from.getY() + Node.getRadius();
		y2 = to.getY() + Node.getRadius();		
		double[] m;
		try {
			m = GraphDrawHelper.getCenter(x1, y1, k1, k2, x2, y2);
			double d = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			double r = Math.sqrt((m[0]-x1)*(m[0]-x1)+(m[1]-y1)*(m[1]-y1));
			//if (2*Math.PI*r/360>6*d/200) throw new GraphException("Edge is too linear.");	
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

	String getWS() {		
		return ew.toString();
	}

	public boolean inYou(int x2, int y2) {
		return x2>=x && x2 <= x+w && y2 >= y && y2 <= y+h;
	}
	
	public int[] offset(int x2, int y2) {
		return new int[] {(int) (k1* nl.getZoom())-x2, (int) (k2* nl.getZoom())-y2};
	}
	
	public void paintEdge(Graphics g) {
		int x1, x2, y1, y2;
		x1 = from.x + Node.getRadius();
		x2 = to.x + Node.getRadius();
		y1 = from.y + Node.getRadius();
		y2 = to.y + Node.getRadius();
		if (from != to) {
			/*int dx = x1 - k1;
			int dy = y1 - k2;
			double d = Math.sqrt(dx * dx + dy * dy);
			x1 = x1 - (int) (Node.getRadius() * dx / d);
			y1 = y1 - (int) (Node.getRadius() * dy / d);
			dx = k1 - x2;
			dy = k2 - y2;
			d = Math.sqrt(dx * dx + dy * dy);			
			x2 = x2 + (int) (Node.getRadius() * dx / d);
			y2 = y2 + (int) (Node.getRadius() * dy / d);		
			*/
			g2d = (Graphics2D) g;			
			g2d.setColor(color);
			drawEdge(g,	(int) (x1 * nl.getZoom()), (int) (y1 * nl.getZoom()), 
					(int) (k1* nl.getZoom()),
					(int) (k2 * nl.getZoom()),
					(int) (x2 * nl.getZoom()), (int) (y2 * nl.getZoom()), 
					(int) (8 * nl.getZoom()), 35, true);
			g2d.setColor(Color.BLACK);
		} 
	}
	
	public void drawEdge(Graphics g, double a1, double a2, double b1, double b2, double c1, double c2, int l, int grad, boolean fill) {
		try {
			double[] m = GraphDrawHelper.getCenter(a1, a2, b1, b2, c1, c2, 0.001);
			double r = Math.sqrt((m[0]-a1)*(m[0]-a1)+(m[1]-a2)*(m[1]-a2));
			double d = Math.sqrt((c1-a1)*(c1-a1)+(c2-a2)*(c2-a2));
			//if (2*Math.PI*r/360>6*d/200) throw new GraphException("Edge is too linear.");			
			double[] phi = getAngle(a1, a2, b1, b2, c1, c2, m[0], m[1]);			
			try {
				java.awt.geom.Arc2D.Double arc = new java.awt.geom.Arc2D.Double(m[0]-r, m[1]-r, 2*r, 2*r, phi[0], phi[1], Arc2D.OPEN);
				Graphics2D g2d = (Graphics2D)g;
				g2d.draw(arc);
				Point2D p1 = arc.getEndPoint();
				Point2D p2 = arc.getStartPoint();
				Point2D p = (Math.sqrt((p1.getX()-c1)*(p1.getX()-c1)+(p1.getY()-c2)*(p1.getY()-c2))<
							 Math.sqrt((p2.getX()-c1)*(p2.getX()-c1)+(p2.getY()-c2)*(p2.getY()-c2)))?p1:p2;
				
				GraphDrawHelper.drawArrowHead(g, p.getX(), p.getY(), (phi[0]==phi[2]&&phi[1]>0)||(phi[0]==phi[1]&&phi[1]<0)?phi[3]+90:(phi[3]+90+180)%360, l, grad, fill);
			} catch (Exception e) {
				phi = GraphDrawHelper.getAngle(a1, a2, b1, b2, c1, c2, m[0], m[1]);	
				g.drawArc((int)(m[0]-r), (int)(m[1]-r), (int)(2*r), (int)(2*r), (int)(phi[0]), (int)(phi[1]));
				GraphDrawHelper.drawArrowHead(g, c1, c2, (phi[0]==phi[2]&&phi[1]>0)||(phi[0]==phi[1]&&phi[1]<0)?phi[3]+90:(phi[3]+90+180)%360, l, grad, fill);
			}			
		} catch (GraphException e) {
			double dx = a1 - b1;
			double dy = a2 - b2;
			double d = Math.sqrt(dx * dx + dy * dy);
			a1 = a1 - ((Node.getRadius()*nl.getZoom()) * dx / d);
			a2 = a2 - ((Node.getRadius()*nl.getZoom()) * dy / d);
			dx = b1 - c1;
			dy = b2 - c2;
			d = Math.sqrt(dx * dx + dy * dy);			
			c1 = c1 + ((Node.getRadius()*nl.getZoom()) * dx / d);
			c2 = c2 + ((Node.getRadius()*nl.getZoom()) * dy / d);	
			GraphDrawHelper.malVollenPfeil(g, (int)a1, (int)a2, (int)c1, (int)c2, l, grad);			
		}
	}
	
	public double[] getAngle(double a1, double a2, double b1, double b2, double c1, double c2, double m1, double m2) {
		double phi1;
		double phi2;
		double phi3;
		// phi correction factor:
		double r = Math.sqrt((m1-a1)*(m1-a1)+(m2-a2)*(m2-a2));
		double phiCF = (Node.r*360*nl.getZoom())/(2*Math.PI*r);
		
		
		if ((a1-m1)==0) {
			phi1 = 90 + ((m2-a2>0)?0:180);
		} else {
			phi1 = Math.atan((-a2+m2)/(a1-m1))*360/(2*Math.PI)+((a1-m1<0)?180:0);
		}
		if ((c1-m1)==0) {
			phi2 = 90 + ((m2-c2>0)?0:180);
		} else {
			phi2 = Math.atan((-c2+m2)/(c1-m1))*360/(2*Math.PI)+((c1-m1<0)?180:0);
		}
		if ((b1-m1)==0) {
			phi3 = 90 + ((m2-b2>0)?0:180);
		} else {
			phi3 = Math.atan((-b2+m2)/(b1-m1))*360/(2*Math.PI)+((b1-m1<0)?180:0);
		}		
		phi1 = (phi1 + 360) % 360;
		phi2 = (phi2 + 360) % 360;
		phi3 = (phi3 + 360) % 360;
		if (phi2 > phi1) {
			if (phi2 > phi3 && phi3 > phi1) {	
				phi1 += phiCF;
				phi2 += -phiCF;
				return new double[] {phi1, phi2-phi1, phi1, phi2, phi3};			
			} else {
				phi1 += -phiCF;
				phi2 += phiCF;
				return new double[] {phi2, (phi1-phi2+360) % 360, phi1, phi2, phi3};			
			}
		}
		if (phi1 > phi3 && phi3 > phi2) {
			phi1 += -phiCF;
			phi2 += phiCF;
			return new double[] {phi1, phi2-phi1, phi1, phi2, phi3};
		} else {
			phi1 += phiCF;
			phi2 += -phiCF;
			return new double[] {phi1, (phi2-phi1+360) % 360, phi1, phi2, phi3};
		}
	}
	
	
	protected int x,y,w,h;
	
	public void paintEdgeLabel(Graphics g) {
		g2d.setFont(new Font("Arial", Font.PLAIN, (int) (16 * nl.getZoom())));
		frc = g2d.getFontRenderContext();		
		String s = getWS();	

		if (!Configuration.getInstance().getGeneralConfig().useJLaTeXMath()) {
			Rectangle2D rc = g2d.getFont().getStringBounds(s, frc);
			g2d.setColor(new Color(0.99f,0.99f,0.99f));
			x = (int)((k1* nl.getZoom() - rc.getWidth() / 2));
			y = (int)((k2* nl.getZoom() - rc.getHeight()* 3 / 2));
			w = (int)((rc.getWidth()+5));
			h = (int)((rc.getHeight()+5));
			g2d.fillRect(x, y, w, h);
			g2d.setColor(Color.BLACK);

			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1));
			g2d.drawRect(x, y, w, h);		
			g2d.setStroke(oldStroke);
			
			g2d.drawString(s, 
					(float) ((k1* nl.getZoom() - rc.getWidth() / 2)), 
					(float) ((k2* nl.getZoom() - rc.getHeight() / 2)));
		} else {
			if (icon==null || lastFontSize != (int) (16 * nl.getZoom())) {
				lastFontSize = (int) (16 * nl.getZoom());
				icon = getTeXIcon(this.nl.control.getGraphGUI(), s, lastFontSize);				
			}
			g2d.setColor(new Color(0.99f,0.99f,0.99f));
			x = (int)((k1* nl.getZoom() - icon.getIconWidth() / 2)-5);
			y = (int)((k2* nl.getZoom() - icon.getIconHeight() / 2)-5); 
			w = (int)((icon.getIconWidth()+10));
			h = (int)((icon.getIconHeight()+10));
			g2d.fillRect(x, y, w, h);
			g2d.setColor(Color.BLACK);

			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1));
			g2d.drawRect(x, y, w, h);		
			g2d.setStroke(oldStroke);		

			icon.paintIcon(panel, g2d,
					(int) ((k1* nl.getZoom() - icon.getIconWidth() / 2)), 
					(int) ((k2* nl.getZoom() - icon.getIconHeight() / 2)));
		}
	}

	TeXIcon icon = null;

	/**
	 * This function takes a string and creates a TeXIcon from this.
	 * @param s String to be parsed.
	 * @return
	 */
	public static TeXIcon getTeXIcon(JFrame parent, String s, int points) {
		String latex = "";
		try {	
			if (s.indexOf("E-")!=-1) {
				latex = s.replaceAll("E-", "}{10^{");
				latex = "\\frac{"+latex+"}}";
			} else {
				int openBracket = 0;
				boolean waitingForDenominator = false;
				String nominator = "";			
				s.replaceAll("ε", "\\varepsilon");	
				s.replaceAll(" ", "");
				for (int i=0;i<s.length(); i++) {
					String c = ""+s.charAt(i);	
					if (c.equals("(")) openBracket++;				
					if (c.equals(")")) openBracket--;				
					if ( (c.equals("+") || c.equals("-") || c.equals("*") || 
							(c.equals(")") &&  (i+1)<s.length() && !(s.charAt(i+1)+"").equals("/")) ) && openBracket == 0) {
						String start = s.substring(0, i+1);										
						if (waitingForDenominator) {
							if (c.equals(")")) {
								latex += "\\frac{"+nominator+"}{"+start+"}";
							} else {
								latex += "\\frac{"+nominator+"}{"+start.substring(0, i)+"}"+c;
							}
							waitingForDenominator = false;
						} else {
							latex += start;
						}
						s = s.substring(i+1, s.length());
						i=-1;
					}
					if (c.equals("/")) {					
						nominator = s.substring(0, i);
						s = s.substring(i+1, s.length());
						i=-1;
						waitingForDenominator = true;
					}
				}
				if (waitingForDenominator) {
					latex += "\\frac{"+nominator+"}{"+s+"}";				
				} else {
					latex += s;
				}			
				latex = latex.replaceAll("\\*", Configuration.getInstance().getGeneralConfig().getTimesSymbol());			
				latex = latex.replaceAll("\\(", "{(");
				latex = latex.replaceAll("\\)", ")}");
			}
			//logger.debug("LaTeX string:"+latex);		
			TeXFormula formula = new TeXFormula(latex);//
			formula = new TeXFormula("\\mathbf{"+latex+"}");		
			TeXIcon result = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, points);
			// TODO What about getIconHeight()/
			if (result.getIconWidth()>60) {
				result = formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (points*0.7));
			}
			//if (latex.indexOf("frac")==-1 && latex.length()>4) points = (int) (points*0.7);
			return result;
		} catch(Exception e) {
			//e.printStackTrace();
			//System.out.println("Error: "+latex);
			//TODO This is not allowed while painting:
			//JOptionPane.showMessageDialog(parent, "Invalid weight string:\n"+latex+"\nError:\n"+e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
			TeXFormula formula = new TeXFormula("Syntax Error");
			return formula.createTeXIcon(TeXConstants.ALIGN_CENTER, (int) (points*0.7)); 
		}		
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

	public void setK2(int k2) {
		double correction = 0;
		if (frc != null) {					
			Rectangle2D rc = g2d.getFont().getStringBounds(getWS(), frc);
			correction = rc.getHeight()/2;
		}
		this.k2 = k2 + (int) correction;
		if (this.k2 < 0) this.k2 = 0;
	}

	public void setW(Double w) {
		ew = new EdgeWeight(w);
		icon=null;
		nl.repaint();
		nl.graphHasChanged();
	}
	
	public void setW(String text) {
		ew = new EdgeWeight(text);
		icon=null;
		nl.repaint();
		nl.graphHasChanged();
	}

	public String getWLaTeX() {		
		return ew.getLaTeXStr();
	}

	public Collection<String> getVariable() {
		return ew.getVariables();
	}

	public double getW(Hashtable<String, Double> ht) {
		return ew.getWeight(ht);
	}
	
	public EdgeWeight getEdgeWeight() {
		return ew;
	}
	
	public String toString() {
		return "edge from "+from+" to "+to;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;		
	}
	
	public boolean isFixed() {
		return fixed;		
	}
	
	public boolean isCurved() {
		return curve;		
	}
	
	public void move() {
		int[] k = getK(from, to, curve);
		k1 = k[0];
		k2 = k[1];
	}

	public String getPreciseWeightStr() {		
		return ew.getPreciseWeightStr();
	}
}
