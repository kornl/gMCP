package org.af.gMCP.gui.graph;

import java.util.Vector;

import org.af.gMCP.gui.RControl;
import org.af.jhlir.call.RList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GraphMCP {

	private static final Log logger = LogFactory.getLog(GraphMCP.class);
	
	protected String name; /* Not used up to now */
	
	public Vector<Edge> edges = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();
	NetList nl;
	
	String description;

	public GraphMCP(String name, NetList nl) {
		this.name = name;
		this.nl = nl;
		loadGraph(name);
		nl.revalidate();
		nl.repaint();
	}
	
	public String getDescription() {
		return description;
	}

	protected void loadGraph(String name) {
		if ( RControl.getR().eval("exists(\""+name+"\")").asRLogical().getData()[0] ) {
			String[] nodes = RControl.getR().eval("nodes("+name+")").asRChar().getData();
			double[] alpha = RControl.getR().eval("getWeights("+name+")").asRNumeric().getData();
			double[] x = RControl.getR().eval("getXCoordinates("+name+")").asRNumeric().getData();
			double[] y = RControl.getR().eval("getYCoordinates("+name+")").asRNumeric().getData();
			boolean[] rejected = RControl.getR().eval("getRejected("+name+")").asRLogical().getData();
			for (int i=0; i<nodes.length; i++) {
				logger.debug("Adding node "+nodes[i]+" at ("+x[i]+","+y[i]+").");
				knoten.add(new Node(nodes[i], (int) x[i], (int) y[i], alpha[i], nl));
				if (rejected[i]) knoten.lastElement().reject();
			}
			// Edges:
			RList edgeL = RControl.getR().eval("gMCP:::getEdges("+name+")").asRList();
			/*
			String[] debugEdges = RControl.getR().eval("capture.output(print(gMCP:::getEdges("+name+")))").asRChar().getData();
			for (String s : debugEdges) {
				System.out.println(s);
			}
			*/
			if (edgeL.get(0)!= null) {
				String[] from = edgeL.get(0).asRChar().getData();
				String[] to = edgeL.get(1).asRChar().getData();
				double[] weight = edgeL.get(2).asRNumeric().getData();
				double[] labelX = edgeL.get(3).asRNumeric().getData();
				double[] labelY = edgeL.get(4).asRNumeric().getData();		
				boolean[] curved = edgeL.get(5).asRLogical().getData();
				String[] weightStr = edgeL.get(6).asRChar().getData();
				for (int i=0; i<from.length; i++) {
					Node fromNode = getNode(from[i]);
					Node toNode = getNode(to[i]);
					int xl = (int) labelX[i];
					//if (xl<-50) xl = (fromNode.getX()+toNode.getX())/2;
					int yl = (int) labelY[i];
					//if (yl<-50) yl = (fromNode.getY()+toNode.getY())/2;				
					boolean curve = curved[i];
					if (!((Double)weight[i]).toString().equals("NaN")) {
						if (xl < -50 || yl < -50) {
							edges.add(new Edge(fromNode, toNode, weight[i], nl,  curve));
						} else {
							edges.add(new Edge(fromNode, toNode, weight[i], nl, xl+Node.getRadius(), yl+Node.getRadius()));
						}
					} else {
						if (xl < -50 || yl < -50) {
							edges.add(new Edge(fromNode, toNode, weightStr[i], nl, /* xl+Node.getRadius(), yl+Node.getRadius(),*/ curve));
						} else {
							edges.add(new Edge(fromNode, toNode, weightStr[i], nl, xl+Node.getRadius(), yl+Node.getRadius()));
						}
					}
					
				}
			}
			try {
				description = RControl.getR().eval("attr("+name+", \"description\")").asRChar().getData()[0];
			} catch (Exception e) {
				description = "Enter a description for the graph.";
			}
		}
		for (Node k : knoten) {
			nl.addNode(k);
		}		
		for (Edge e : edges) {
			nl.addEdge(e);
		}
	}

	private Node getNode(String name) {
		for (Node node : knoten) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}	

}
