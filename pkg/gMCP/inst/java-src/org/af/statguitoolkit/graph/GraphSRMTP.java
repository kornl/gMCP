package org.af.statguitoolkit.graph;

import java.util.Vector;

import org.af.jhlir.call.RList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.Edge;
import org.mutoss.gui.graph.NetzListe;
import org.mutoss.gui.graph.Node;
import org.mutoss.gui.graph.VS;

public class GraphSRMTP {

	private static final Log logger = LogFactory.getLog(GraphSRMTP.class);
	
	String name;
	
	public Vector<Edge> kanten = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();
	NetzListe nl;
	
	public NetzListe getNL() {
		return nl;
	}

	public void setNL(NetzListe nl) {
		this.nl = nl;
	}

	public GraphSRMTP(String name, VS vs) {
		this.name = name;		
		if ( RControl.getR().eval("exists(\""+name+"\")").asRLogical().getData()[0] ) {
			String[] nodes = RControl.getR().eval("nodes("+name+")").asRChar().getData();
			double[] alpha = RControl.getR().eval("getAlpha("+name+")").asRNumeric().getData();
			double[] x = RControl.getR().eval("getX("+name+")").asRNumeric().getData();
			double[] y = RControl.getR().eval("getY("+name+")").asRNumeric().getData();
			for (int i=0; i<nodes.length; i++) {
				logger.debug("Adding node "+nodes[i]+" at ("+x[i]+","+y[i]+").");
				knoten.add(new Node(nodes[i], (int) x[i], (int) y[i], alpha[i], vs));
			}
			// Edges:
			RList edgeL = RControl.getR().eval("gsrmtp:::getEdges("+name+")").asRList();
			String[] from = edgeL.get(0).asRChar().getData();
			String[] to = edgeL.get(1).asRChar().getData();
			double[] weight = edgeL.get(2).asRNumeric().getData();
			for (int i=0; i<from.length; i++) {
				kanten.add(new Edge(getNode(from[i]), getNode(to[i]), weight[i], vs));
			}
		}		
		this.nl = vs.nl;
		for (Node k : knoten) {
			nl.addNode(k);
		}		
		for (Edge e : kanten) {
			nl.addEdge(e);
		}
		nl.revalidate();
		nl.repaint();
	}

	private Node getNode(String name) {
		for (Node node : knoten) {
			if (node.name.equals(name)) {
				return node;
			}
		}
		return null;
	}
	
	

}
