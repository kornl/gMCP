package org.mutoss.gui.graph;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.datatable.DataTable;

public class ControlMGraph  {
	
	private static final Log logger = LogFactory.getLog(ControlMGraph.class);
	
	String name;
	CreateGraphGUI parent;
	
	public ControlMGraph(String name, CreateGraphGUI parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public NetList getNL() {
		return getGraphView().getNL();
	}
	
	public String getName() {		
		return name;
	}

	public JFrame getMainFrame() {		
		return parent;
	}

	public PView getPView() {		
		return parent.getPView();
	}
	
	public GraphView getGraphView() {
		return parent.getGraphView();
	}

	public void updateEdge(int from, int to, Double w) {
		logger.info("Adding Edge from "+from+" to "+to+" with weight "+w+".");
		Edge e = getNL().findEdge(getNL().getKnoten().get(from), getNL().getKnoten().get(to));
		if (e!=null) {
			int x = e.getK1();
			int y = e.getK2();
			if (w != 0) {
				getNL().addEdge(new Edge(getNL().getKnoten().get(from), getNL().getKnoten().get(to), w, getNL().vs, x, y));
			} else {
				getNL().removeEdge(e);
			}
		} else {
			getNL().addEdge(getNL().getKnoten().get(from), getNL().getKnoten().get(to), w);
		}
		getNL().repaint();
	}

	public DataTable getDataTable() {		
		return parent.getDataTable();
	}

}
