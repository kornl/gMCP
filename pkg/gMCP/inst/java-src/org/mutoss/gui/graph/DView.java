package org.mutoss.gui.graph;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.mutoss.gui.CreateGraphGUI;

public class DView extends JTabbedPane {

	CreateGraphGUI control;
	JPanel description = null;
	JPanel analysis = null;
	
	public DView(CreateGraphGUI control) {
		this.control = control;
		setDescription("jkdfhsajf");
		setAnalysis("jkdfhsajf");
	}
	
	public void setDescription(String s) {
		if (s == null && description != null) {
			remove(description);
			description = null;
			return;
		}
		description = new JPanel();
		this.insertTab("Description", null, description, "Description", 0);
	}

	public void setAnalysis(String s) {
		if (s == null && analysis != null) {
			remove(analysis);
			analysis = null;
			return;
		}
		analysis = new JPanel();
		this.insertTab("Analysis", null, analysis, "Analysis", 0);
	}
	
}
