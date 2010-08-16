package org.af.statguitoolkit.graph;

import java.util.Vector;

import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.Edge;
import org.mutoss.gui.graph.Node;

public class GraphSRMTP {

	String name;
	
	public Vector<Edge> kanten = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();
	
	public GraphSRMTP(String name) {
		this.name = name;		 
		if ( RControl.getR().eval("exists(\""+name+"\")").asRLogical().getData()[0] ) {
			String[] nodes = RControl.getR().eval("nodes("+name+")").asRChar().getData();
			double[] alpha = RControl.getR().eval("getAlpha("+name+")").asRNumeric().getData();
			
		}
		
	}

}
