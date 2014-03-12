package org.af.gMCP.gui.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.af.gMCP.gui.graph.LaTeXTool;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ScenarioPanel extends JPanel implements ActionListener {
	List<Scenario> sc = new Vector<Scenario>();
	JButton addScenario = new JButton("Add scenario");
	JButton rmScenario = new JButton("Remove last scenario");
	
	PowerDialog pd;
	
	JPanel panel = new JPanel();
	
	public ScenarioPanel(PowerDialog pd) {
		this.pd = pd;		
		
		sc.add(new Scenario(pd, "Scenario "+(sc.size()+1)));
				
		setUpLayout();
	}
	
	public void setUpLayout() {

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, fill:min:grow, pref, 5dlu, pref, 5dlu";
		String rows = "5dlu, fill:min:grow, 5dlu, pref, 5dlu";
		
		setLayout(new FormLayout(cols, rows));

		add(new JScrollPane(getMainPanel()), cc.xyw(2, row, 4));
		
		row += 2;
		
		add(addScenario, cc.xy(3, row));
		add(rmScenario, cc.xy(5, row));
		addScenario.addActionListener(this);
		rmScenario.addActionListener(this);
		rmScenario.setEnabled(false);
		
	}

	public JPanel getMainPanel() {
		panel.removeAll();

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, pref, 5dlu";
		String rows = "5dlu, pref, 5dlu";
		for (Node n : pd.nodes) {
			cols += ", pref, 5dlu";
		}
		for (Scenario s : sc) {
			rows += ", pref, 5dlu";
		}

		panel.setLayout(new FormLayout(cols, rows));

		int col = 2;
		panel.add(new JLabel("Scenario name"), cc.xy(col, row));

		for (Node n : pd.parent.getGraphView().getNL().getNodes()) {
			col += 2;
			panel.add(new JLabel("NCP "+ LaTeXTool.LaTeX2UTF(n.getName())+"    "), cc.xy(col, row));
		}

		for (Scenario s : sc) {
			row += 2;
			s.addComponents(panel, cc, row);
		}
		return panel;
	}

	public String getNCPString() {
		String sList = ", list(";
		for (Scenario s : sc) {
			sList += s.getNCPString()+", ";
		}
		return sList.substring(0, sList.length()-2)+")";
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==addScenario) {
			sc.add(new Scenario(pd, "Scenario "+(sc.size()+1)));
			getMainPanel();
			revalidate();
			repaint();
			rmScenario.setEnabled(true);
		} else if (e.getSource()==rmScenario) {
			if (sc.size()>1) {
				sc.remove(sc.size()-1);
				getMainPanel();
				revalidate();
				repaint();
			}
			if (sc.size()==1) {
				rmScenario.setEnabled(false);
			}
		}		
	}	
	
}
