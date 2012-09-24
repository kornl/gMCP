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
	public Vector<Node> nodes = new Vector<Node>();
	NetList nl;
	
	String description;
	double[] pvalues = null;

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
			if ( RControl.getR().eval("\"entangledMCP\" %in% class("+name+")").asRLogical().getData()[0] ) {
				String[] nodeArray = RControl.getR().eval("getNodes("+name+")").asRChar().getData();
				double[] x = RControl.getR().eval("getXCoordinates("+name+")").asRNumeric().getData();
				double[] y = RControl.getR().eval("getYCoordinates("+name+")").asRNumeric().getData();
				boolean[] rejected = RControl.getR().eval("getRejected("+name+")").asRLogical().getData();
				for (int i=0; i<RControl.getR().eval("gMCP:::layers("+name+")").asRInteger().getData()[0]; i++) {
					logger.debug("Adding node "+nodeArray[i]+" at ("+x[i]+","+y[i]+").");
					double[] alpha = RControl.getR().eval("getWeights("+name+")["+(i+1)+",]").asRNumeric().getData();
					nodes.add(new Node(nodeArray[i], (int) x[i], (int) y[i], alpha, nl));
					if (rejected[i]) nodes.lastElement().rejected = true;
					nl.control.addEntangledLayer();
				}
			} else {
				String[] nodeArray = RControl.getR().eval("getNodes("+name+")").asRChar().getData();
				double[] alpha = RControl.getR().eval("getWeights("+name+")").asRNumeric().getData();
				double[] x = RControl.getR().eval("getXCoordinates("+name+")").asRNumeric().getData();
				double[] y = RControl.getR().eval("getYCoordinates("+name+")").asRNumeric().getData();
				boolean[] rejected = RControl.getR().eval("getRejected("+name+")").asRLogical().getData();
				for (int i=0; i<nodeArray.length; i++) {
					logger.debug("Adding node "+nodeArray[i]+" at ("+x[i]+","+y[i]+").");
					nodes.add(new Node(nodeArray[i], (int) x[i], (int) y[i], new double[] {alpha[i]}, nl));
					if (rejected[i]) nodes.lastElement().rejected = true;
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
						int layer = 0;
						if (!((Double)weight[i]).toString().equals("NaN")) {
							if (xl < -50 || yl < -50) {
								edges.add(new Edge(fromNode, toNode, weight[i], nl,  curve, layer));
							} else {
								edges.add(new Edge(fromNode, toNode, weight[i], nl, xl+Node.getRadius(), yl+Node.getRadius(), layer));
								edges.lastElement().setFixed(true);
							}
						} else {
							if (xl < -50 || yl < -50) {
								edges.add(new Edge(fromNode, toNode, weightStr[i], nl, /* xl+Node.getRadius(), yl+Node.getRadius(),*/ curve, layer));
							} else {
								edges.add(new Edge(fromNode, toNode, weightStr[i], nl, xl+Node.getRadius(), yl+Node.getRadius(), layer));
								edges.lastElement().setFixed(true);
							}
						}

					}
				}
				try {
					description = RControl.getR().eval("attr("+name+", \"description\")").asRChar().getData()[0];
				} catch (Exception e) {
					description = "Enter a description for the graph.";
				}
				try {
					pvalues = RControl.getR().eval("attr("+name+", \"pvalues\")").asRNumeric().getData();
				} catch (Exception e) {
					// Nothing to do here.
				}
			}
		}
		for (Node k : nodes) {
			nl.addNode(k);
		}		
		for (Edge e : edges) {
			nl.setEdge(e);
		}
	}

	private Node getNode(String name) {
		for (Node node : nodes) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}
}
