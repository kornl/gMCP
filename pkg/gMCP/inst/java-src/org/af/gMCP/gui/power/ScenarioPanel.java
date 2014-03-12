package org.af.gMCP.gui.power;

import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.gMCP.gui.graph.LaTeXTool;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScenarioPanel extends JPanel {
	List<Scenario> sc = new Vector<Scenario>();
	
	
	PowerDialog pd;
	
	public ScenarioPanel(PowerDialog pd) {
		this.pd = pd;		
		
		sc.add(new Scenario(pd, "Scenario "+(sc.size()+1)));
				
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
		
		int col = 2;
		add(new JLabel("Scenario name"), cc.rc(row, col));
		
		for (Node n : pd.parent.getGraphView().getNL().getNodes()) {
			col += 2;
			add(new JLabel("NCP "+ LaTeXTool.LaTeX2UTF(n.getName())+"    "), cc.rc(row, col));
		}
		
		for (Scenario s : sc) {
			row += 2;
			s.addComponents(this, cc, row);
		}
	}

	public String getNCPString() {
		String sList = ", list(";
		for (Scenario s : sc) {
			sList += s.getNCPString()+", ";
		}
		return sList.substring(0, sList.length()-2)+")";
	}	
	
}
