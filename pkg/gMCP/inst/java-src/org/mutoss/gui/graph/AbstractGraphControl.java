package org.mutoss.gui.graph;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.CreateGraphGUI;

public class AbstractGraphControl  {
	
	private static final Log logger = LogFactory.getLog(AbstractGraphControl.class);
	
	String name;
	CreateGraphGUI parent;
	
	public AbstractGraphControl(String name, CreateGraphGUI parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public NetzListe getNL() {
		return null;
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

}
