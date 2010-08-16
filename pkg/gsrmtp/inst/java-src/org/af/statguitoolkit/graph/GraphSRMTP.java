package org.af.statguitoolkit.graph;

import org.mutoss.gui.RControl;

public class GraphSRMTP {

	String name;
	
	public GraphSRMTP(String name) {
		this.name = name;
		RControl.getR().eval("");
	}

}
