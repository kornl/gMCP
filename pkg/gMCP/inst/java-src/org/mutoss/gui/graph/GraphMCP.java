package org.mutoss.gui.graph;

import java.util.Vector;

import org.af.jhlir.call.RList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.RControl;

public class GraphMCP {

	private static final Log logger = LogFactory.getLog(GraphMCP.class);
	
	String name;
	
	public Vector<Edge> edges = new Vector<Edge>();
	public Vector<Node> knoten = new Vector<Node>();
	NetList nl;
	VS vs;

	public GraphMCP(String name, VS vs) {
		this.name = name;
		this.vs = vs;
		loadGraph(name);
		nl.revalidate();
		nl.repaint();
	}
	
	public void loadGraph(String name) {
		if ( RControl.getR().eval("exists(\""+name+"\")").asRLogical().getData()[0] ) {
			String[] nodes = RControl.getR().eval("nodes("+name+")").asRChar().getData();
			double[] alpha = RControl.getR().eval("getAlpha("+name+")").asRNumeric().getData();
			double[] x = RControl.getR().eval("getX("+name+")").asRNumeric().getData();
			double[] y = RControl.getR().eval("getY("+name+")").asRNumeric().getData();
			boolean[] rejected = RControl.getR().eval("getRejected("+name+")").asRLogical().getData();
			for (int i=0; i<nodes.length; i++) {
				logger.debug("Adding node "+nodes[i]+" at ("+x[i]+","+y[i]+").");
				knoten.add(new Node(nodes[i], (int) x[i], (int) y[i], alpha[i], vs));
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
				for (int i=0; i<from.length; i++) {
					Node fromNode = getNode(from[i]);
					Node toNode = getNode(to[i]);
					int xl = (int) labelX[i];
					//if (xl<-50) xl = (fromNode.getX()+toNode.getX())/2;
					int yl = (int) labelY[i];
					//if (yl<-50) yl = (fromNode.getY()+toNode.getY())/2;				
					boolean curve = curved[i];
					if (xl < -50 || yl < -50) {
						edges.add(new Edge(fromNode, toNode, weight[i], vs, /* xl+Node.getRadius(), yl+Node.getRadius(),*/ curve));
					} else {
						edges.add(new Edge(fromNode, toNode, weight[i], vs, xl+Node.getRadius(), yl+Node.getRadius()));
					}
					
				}
			}
		}		
		this.nl = vs.nl;
		for (Node k : knoten) {
			nl.addNode(k);
		}		
		for (Edge e : edges) {
			nl.addEdge(e);
		}
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
