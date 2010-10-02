package org.mutoss.gui.graph;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.CreateGraphGUI;

public class ControlMGraph  {
	
	private static final Log logger = LogFactory.getLog(ControlMGraph.class);
	
	String name;
	CreateGraphGUI parent;
	
	public ControlMGraph(String name, CreateGraphGUI parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public NetzListe getNL() {
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

}
