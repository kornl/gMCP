package org.mutoss.gui.graph;


public class VS {
	
	public NetList nl = null;
	
	public void setNL(NetList nl) {
		this.nl = nl;
	}

	public double zoom = 1.00;

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double p) {
		zoom = p;
	}

	public boolean newVertex = false;
	public boolean newEdge = false;

	public void repaint() {
		if (nl!=null) {
			nl.repaint();
		}
	}

}
