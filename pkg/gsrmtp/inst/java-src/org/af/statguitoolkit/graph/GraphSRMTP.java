package org.af.statguitoolkit.graph;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.Edge;
import org.mutoss.gui.graph.Node;
import org.mutoss.gui.graph.VS;

public class GraphSRMTP {

	private static final Log logger = LogFactory.getLog(GraphSRMTP.class);
	
	String name;
	
	public Vector<Edge> kanten = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();
	
	public GraphSRMTP(String name, VS vs) {
		this.name = name;		 
		if ( RControl.getR().eval("exists(\""+name+"\")").asRLogical().getData()[0] ) {
			String[] nodes = RControl.getR().eval("nodes("+name+")").asRChar().getData();
			double[] alpha = RControl.getR().eval("getAlpha("+name+")").asRNumeric().getData();
			double[] x = RControl.getR().eval("getX("+name+")").asRNumeric().getData();
			double[] y = RControl.getR().eval("getY("+name+")").asRNumeric().getData();
			for (int i=0; i<nodes.length; i++) {
				logger.debug("Adding node "+nodes[i]+" at ("+x[i]+","+y[i]+").");
				knoten.add(new Node(nodes[i], (int) x[i], (int) y[i], vs));
			}
		}
		
	}

}
