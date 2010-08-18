package org.mutoss.gui.graph;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;

public class MGraphListe extends NetzListe {
	
	AbstractGraphControl control;

	public MGraphListe(JLabel statusBar, VS vs, AbstractGraphControl abstractGraphControl) {
		super(statusBar, vs);	
		vs.setNL(this);
		this.control = abstractGraphControl;
	}
	
	public void addDefaultNode(int x, int y) {
		knoten.add(new Node(knoten.size() + 1, "HA_" + (knoten.size() + 1), x, y, vs));	
		/*
		try {
			control.getDataTable().getModel().addRow(control.getDataFrame().getRowCount(), RLegalName.makeRLegalNameUnchecked("HA_" + knoten.size()));
			for (int i=0;i<control.getDataFrame().getColumnCount();i++) { 
				control.getDataTable().getModel().setValueAt(0d, control.getDataFrame().getRowCount()-1, i);
			}			
			control.getDataTable().getModel().addCol(control.getDataFrame().getColumnCount(), RLegalName.makeRLegalNameUnchecked("HA_" + knoten.size()), control.getRControl().getRServices().createRObject(0.0));
			control.addNode(knoten.lastElement());
		} catch (RemoteException e) {
			ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e);
		}
		*/
	}
	
	public void addEdge(Node von, Node nach) {
		boolean curve = false;
		for (Edge e : edges) {
			if (e.von == nach && e.nach == von) {
				e.curve = true;
				curve = true;
			}
		}
		super.addEdge(von, nach, Double.NaN);
		edges.lastElement().curve = curve;
		//TODO control.getDataTable().getModel().setValueAt(Double.NaN, knoten.indexOf(von), knoten.indexOf(nach));
		revalidate();
		repaint();
	}
	
	public void addEdge(Node von, Node nach, Double w) {
		if (w!=0) {
			addEdge(von, nach);
			edges.lastElement().setW(w);
		} else {
			for (int i=edges.size()-1; i>=0; i--) {
				Edge e = edges.get(i);
				if (e.von == von && e.nach == nach) {
					edges.remove(e);
				} 
			}
		}
		revalidate();
		repaint();
	}
	
	public Edge findEdge(Node von, Node nach) {
		for (Edge e : edges) {
			if (von == e.von && nach == e.nach) {
				return e;
			}
		}
		return null;
	}
	
	public void removeEdge(Edge edge) {
		for (Edge e : edges) {
			if (e.von == edge.nach && e.nach == edge.von) {
				e.curve = false;				
			}
		}
		edges.remove(edge);
		//TODO control.getDataTable().getModel().setValueAt(0, knoten.indexOf(edge.von), knoten.indexOf(edge.nach));
		revalidate();
		repaint();
	}

	public void acceptNode(Node node) {
		int epsilon = 0; 
		boolean eps = true;
		boolean hasChildren = updateEdges(node);
		for (int i=edges.size()-1; i>=0; i--) {
			Edge e = edges.get(i);
			if (e.nach == node) {				
				removeEdge(e);
			}
			if (e.von == node && !e.w.toString().equals("NaN")) {
				e.nach.alpha = e.nach.alpha + node.alpha * e.w;				
				removeEdge(e);
				eps = false;
			} 
			if (e.von == node && e.w.toString().equals("NaN")) {
				epsilon++;
			}
		}
		if (epsilon>0) {
			for (int i=edges.size()-1; i>=0; i--) {
				Edge e = edges.get(i);
				if (e.von == node && e.w.toString().equals("NaN")) {
					if (eps) {
						e.nach.alpha = e.nach.alpha + node.alpha / epsilon;					
					}
					removeEdge(e);
				} 
			}
		}
		if (hasChildren) {
			node.alpha = 0; 
		}
		node.setColor(Color.MAGENTA);
		revalidate();
		repaint();
	}

	private boolean updateEdges(Node node) {
		Vector<Node> epsChildren = new Vector<Node>();
		Vector<Node> realChildren = new Vector<Node>();
		Vector<Node> allChildren = new Vector<Node>();
		for (Edge e : edges) {
			if (node == e.von) {
				if (e.w.toString().equals("NaN")) {
					epsChildren.add(e.nach);
				} else {
					realChildren.add(e.nach);
				}		
				allChildren.add(e.nach);
			}
		}		
		Vector<Edge> all = new Vector<Edge>();
		all.addAll(edges);
		for (Edge e : all) {			
			if (e.nach == node) {				
				if (e.w.toString().equals("NaN")) { 
					for (Node nach : allChildren) {
						if (findEdge(e.von,nach)==null) {
							if (e.von!=nach) { addEdge(e.von, nach); }
						}
					}
				} else { 
					for (Node nach : epsChildren) {
						if (findEdge(e.von,nach)==null) {
							if (e.von!=nach) { addEdge(e.von, nach); }
						}
					}
					for (Node nach : realChildren) {						
						Double glk = (findEdge(e.von,nach)==null)?0:findEdge(e.von,nach).w;
						Double glj = e.w;
						Double gjk = findEdge(node,nach).w;
						Double gjl = (findEdge(node,e.von)==null)?0:findEdge(node,e.von).w;
						if (e.von!=nach) { addEdge(e.von, nach, (glk+glj*gjk)/(1-glj*gjl)); }						
					}					
				}				
			}
		}		
		return allChildren.size()!=0;
	}

	public void reset() {
		edges.removeAllElements();
    	knoten.removeAllElements();
		statusBar.setText(GraphView.STATUSBAR_DEFAULT);
		firstVertexSelected = false;
		vs.newVertex = false;
		vs.newEdge = false;
		vs.zoom = 1.00;
	}

}
