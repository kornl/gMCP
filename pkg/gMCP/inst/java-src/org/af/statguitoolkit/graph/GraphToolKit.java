package org.af.statguitoolkit.graph;

import java.awt.Graphics;

import org.af.commons.images.GraphDrawHelper;

public class GraphToolKit {

	/*
	 * Given three points (a1, a2), (b1, b2), (c1, c2) this function returns
	 * the center of the well-defined circle that goes through all of the three points.  
	 */
	public static double[] getCenter(double a1, double a2, double b1, double b2, double c1, double c2) throws GraphException {
		double x1, x2;
		if ((b2-c2)==0) {
			x2=1; x1=0;
		} else {
			x2 = - (b1-c1)/(b2-c2);
			x1 = 1;
		}		
		if ((2*(b1-a1)*(b2-c2)-2*(c1-b1)*(a2-b2))==0) {
			throw new GraphException("All three points are on a line.");
		}
		double d = ((c2-a2)*(a2-b2)*(b2-c2)-(c1-a1)*(b1-a1)*(b2-c2))/(2*(b1-a1)*(b2-c2)-2*(c1-b1)*(a2-b2));		
		//
		double m1 = (b1+c1)/2+d*x1;
		double m2 = (b2+c2)/2+d*x2;
		return new double[] {m1, m2};
	}
	
	public static double[] getAngle(double a1, double a2, double b1, double b2, double c1, double c2, double m1, double m2) {
		double phi1;
		double phi2;
		double phi3;
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
				return new double[] {phi1, phi2-phi1, phi1, phi2};			
			} else {
				return new double[] {phi2, (phi1-phi2+360) % 360, phi1, phi2};			
			}
		}
		if (phi1 > phi3 && phi3 > phi2) {
			return new double[] {phi1, phi2-phi1, phi1, phi2};
		} else {
			return new double[] {phi1, (phi2-phi1+360) % 360, phi1, phi2};
		}
	}
	
	/**
	 * 
	 * @param g das zu bearbeitende Graphics-Objekt
	 * @param a1 die Abszisse des Pfeilanfangs
	 * @param a2 die Ordinate des Pfeilanfangs
	 * @param b1 die Abszisse des Hilfspunktes
	 * @param b2 die Ordinate des Hilfspunktes
	 * @param c1 die Abszisse des Pfeilendes
	 * @param c2 die Ordinate des Pfeilendes
	 * @param l die Länge der Pfeilspitze
	 * @param grad der Winkel zwischen Pfeilspitzenschenkeln und Pfeilrumpf (0-360)
	 */
	public static void drawEdge(Graphics g, double a1, double a2, double b1, double b2, double c1, double c2, int l, int grad) {
		try {
			double[] m = GraphToolKit.getCenter(a1, a2, b1, b2, c1, c2);
			g.drawOval((int)m[0]-1, (int)m[1]-1, 2, 2);
			g.drawString("M", (int)m[0], (int)m[1]);
			double r = Math.sqrt((m[0]-a1)*(m[0]-a1)+(m[1]-a2)*(m[1]-a2));
			double d = Math.sqrt((c1-a1)*(c1-a1)+(c2-a2)*(c2-a2));
			if (r/d>10) throw new GraphException("Edge is too linear.");			
			double[] phi = GraphToolKit.getAngle(a1, a2, b1, b2, c1, c2, m[0], m[1]);
			g.drawArc((int)(m[0]-r), (int)(m[1]-r), (int)(2*r), (int)(2*r), (int)(phi[0]), (int)(phi[1]));
			drawArrowHead(g, c1, c2, (phi[0]==phi[2]&&phi[1]>0)||(phi[0]==phi[1]&&phi[1]<0)?phi[3]+90:(phi[3]+90+180)%360, l, grad);			
		} catch (GraphException e) {
			GraphDrawHelper.malPfeil(g, (int)a1, (int)a2, (int)c1, (int)c2, l, grad);			
		}
	}

	private static void drawArrowHead(Graphics g, double c1, double c2, double phi, int l, int grad) {	
		phi = (phi + 180) % 360;
		int px = (int) (c1 + Math.cos((2 * Math.PI * (phi+grad) / 360)) * l );
		int py = (int) (c2 - Math.sin((2 * Math.PI * (phi+grad) / 360)) * l );
		g.drawLine((int)c1, (int)c2, px, py);		
		px = (int) (c1 + Math.cos((2 * Math.PI * (phi-grad) / 360)) * l );
		py = (int) (c2 - Math.sin((2 * Math.PI * (phi-grad) / 360)) * l );
		g.drawLine((int)c1, (int)c2, px, py);			
	}
	
}
