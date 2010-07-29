package org.mutoss.gui.graph;


public class VS {

	public boolean moreInfo = false;
	public boolean directed = true;
	public boolean shownr = false;
	
	NetzListe nl = null;
	
	public void setNL(NetzListe nl) {
		this.nl = nl;
	}

	public int graphdrawalgo = 1;

	public int getGraphDrawAlgo() {
		return graphdrawalgo;
	}

	public void setGraphDrawAlgo(int p) {
		graphdrawalgo = p;
	}

	public double zoom = 1.00;

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double p) {
		zoom = p;
	}

	public boolean force;

	public boolean getforce() {
		return force;
	}

	public void setforce(boolean p) {
		force = p;
	}

	public boolean forceSO;

	public boolean getforceSO() {
		return forceSO;
	}

	public void setforceSO(boolean p) {
		forceSO = p;
	}

	public boolean shake = true;

	public void setShake(boolean s) {
		shake = s;
	}

	public boolean getShake() {
		return shake;
	}

	public boolean newVertex = false;
	public boolean newEdge = false;

	public void repaint() {
		if (nl!=null) {
			nl.repaint();
		}
	}

}
