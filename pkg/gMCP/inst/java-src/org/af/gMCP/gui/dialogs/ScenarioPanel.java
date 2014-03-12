package org.af.gMCP.gui.dialogs;

import java.util.List;

import javax.swing.JPanel;

import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScenarioPanel extends JPanel {
	List<Scenario> sc;
	
	
	PowerDialog pd;
	
	public ScenarioPanel(PowerDialog pd, String name) {
		this.pd = pd;		
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, pref, 5dlu";
		String rows = "5dlu, pref, 5dlu";
		for (Node n : pd.parent.getGraphView().getNL().getNodes()) {
			cols += ", pref, 5dlu";
		}
		for (Scenario s : sc) {
			rows += ", pref, 5dlu";
		}
		
		setLayout(new FormLayout(cols, rows));
		
		for (Scenario s : sc) {
			s.addComponents(this, cc, row);
		}
	}	
	
}
