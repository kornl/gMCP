package org.af.gMCP.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	
	public Edge(Node von, Node nach, Double w, NetList nl, boolean curve) {
		this(von, nach, w, nl);
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

	String getWS() {		
		return ew.toString();
	}

	public boolean inYou(int x, int y) {
		if (icon==null) {
			icon = getTeXIcon(this.nl.control.getGraphGUI(), getWS(), (int) (16 * nl.getZoom()));			
		}		
		int TOLERANCE = 5; 
		
		if (!Configuration.getInstance().getGeneralConfig().useJLaTeXMath()) {
			String s = getWS();
			FontRenderContext frc = g2d.getFontRenderContext();	
			Rectangle2D rc = (new Font("Arial", Font.PLAIN, (int) (16 * nl.getZoom()))).getStringBounds(s, frc); 
			return (x/ nl.getZoom()>k1-rc.getWidth()/2-TOLERANCE)&&(x/ nl.getZoom()<k1+rc.getWidth()/2+TOLERANCE)&&(y/ nl.getZoom()<k2- rc.getHeight()*1/ 2+TOLERANCE)&&(y/ nl.getZoom()>k2-rc.getHeight()*3/2-TOLERANCE);
		} else {
			return (x/nl.getZoom()>k1-icon.getIconWidth()/2-TOLERANCE)
			&& (x/nl.getZoom()<k1+icon.getIconWidth()/2+TOLERANCE)
			&& (y/nl.getZoom()<k2+icon.getIconHeight()/2+TOLERANCE)
			&& (y/nl.getZoom()>k2-icon.getIconHeight()/2-TOLERANCE);
		}
	}
	
	public void paintEdge(Graphics g) {
		int x1, x2, y1, y2;
		x1 = from.x + Node.getRadius();
		x2 = to.x + Node.getRadius();
		y1 = from.y + Node.getRadius();
		y2 = to.y + Node.getRadius();
		if (from != to) {
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
			}
			g2d = (Graphics2D) g;			

			GraphDrawHelper.drawEdge(g,	(int) (x1 * nl.getZoom()), (int) (y1 * nl.getZoom()), 
					(int) (k1* nl.getZoom()),
					(int) (k2 * nl.getZoom()),
					(int) (x2 * nl.getZoom()), (int) (y2 * nl.getZoom()), 
					(int) (8 * nl.getZoom()), 35, true);
		} 
	}
	
	public void paintEdgeLabel(Graphics g) {
		g2d.setFont(new Font("Arial", Font.PLAIN, (int) (16 * nl.getZoom())));
		frc = g2d.getFontRenderContext();		
		String s = getWS();	

		if (!Configuration.getInstance().getGeneralConfig().useJLaTeXMath()) {
			Rectangle2D rc = g2d.getFont().getStringBounds(s, frc);
			g2d.setColor(new Color(0.99f,0.99f,0.99f));
			g2d.fillRect((int)((k1* nl.getZoom() - rc.getWidth() / 2)), 
					(int)((k2* nl.getZoom() - rc.getHeight()* 3 / 2)), 
					(int)((rc.getWidth()+5)), (int)((rc.getHeight()+5)));
			g2d.setColor(Color.BLACK);

			g2d.drawString(s, 
					(float) ((k1* nl.getZoom() - rc.getWidth() / 2)), 
					(float) ((k2* nl.getZoom() - rc.getHeight() / 2)));
		} else {
			if (icon==null || lastFontSize != (int) (16 * nl.getZoom())) {
				lastFontSize = (int) (16 * nl.getZoom());
				icon = getTeXIcon(this.nl.control.getGraphGUI(), s, lastFontSize);				
			}
			g2d.setColor(new Color(0.99f,0.99f,0.99f));
			g2d.fillRect((int)((k1* nl.getZoom() - icon.getIconWidth() / 2)-5), 
					(int)((k2* nl.getZoom() - icon.getIconHeight() / 2)-5), 
					(int)((icon.getIconWidth()+10)),
					(int)((icon.getIconHeight()+10)));
			g2d.setColor(Color.BLACK);

			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1));
			g2d.drawRect((int)((k1* nl.getZoom() - icon.getIconWidth() / 2)-5), 
					(int)((k2* nl.getZoom() - icon.getIconHeight() / 2)-5), 
					(int)((icon.getIconWidth()+10)),
					(int)((icon.getIconHeight()+10)));		
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
			logger.debug("LaTeX string:"+latex);		
			TeXFormula formula = new TeXFormula(latex);//
			formula = new TeXFormula("\\mathbf{"+latex+"}");			
			if (latex.indexOf("frac")==-1 && latex.length()>4) points = (int) (points*0.7);
			return formula.createTeXIcon(TeXConstants.ALIGN_CENTER, points);
		} catch(Exception e) {
			//e.printStackTrace();
			//System.out.println("Error: "+latex);
			JOptionPane.showMessageDialog(parent, "Invalid weight string:\n"+latex+"\nError:\n"+e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
			TeXFormula formula = new TeXFormula("Syntax Error");
			return formula.createTeXIcon(TeXConstants.ALIGN_CENTER, points); 
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
}
