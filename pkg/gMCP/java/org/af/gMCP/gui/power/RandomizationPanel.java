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

public class RandomizationPanel extends JPanel implements ActionListener {
	List<Arm> gv = new Vector<Arm>();
	JButton addScenario = new JButton("Add scenario");
	JButton rmScenario = new JButton("Remove last scenario");
	
	JButton jbEndpoint = new JButton("Endpoint");
	JButton jbPopulation = new JButton("Population");
	
	SampleSizeDialog sd;
	
	JPanel panel = new JPanel();
	
	public RandomizationPanel(SampleSizeDialog sd) {
		this.sd = sd;
		gv.add(new Arm(sd, "Arm "+(gv.size()+1)));
		setUpLayout();
	}
	
	
	// Add θ and standard error of θ.
	// Allocation ratio?
	
	public void setUpLayout() {

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, fill:min:grow, pref, 5dlu, pref, 5dlu";
		String rows = "5dlu, fill:min:grow, 5dlu, pref, 5dlu, pref, 5dlu";
		
		setLayout(new FormLayout(cols, rows));

		add(new JScrollPane(getMainPanel()), cc.xyw(2, row, 4));
		
		row += 2;
		
		add(addScenario, cc.xy(3, row));
		add(rmScenario, cc.xy(5, row));
		addScenario.addActionListener(this);
		rmScenario.addActionListener(this);
		rmScenario.setEnabled(false);
		
		row += 2;
		
		add(jbEndpoint, cc.xy(3, row));
		add(jbPopulation, cc.xy(5, row));
		jbEndpoint.addActionListener(this);
		jbPopulation.addActionListener(this);
		
	}

	public JPanel getMainPanel() {
		panel.removeAll();

		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, pref, 5dlu, pref, 5dlu";
		String rows = "5dlu, pref, 5dlu";
		for (Node n : sd.getNodes()) {
			cols += ", pref, 5dlu";
		}
		for (Arm g : gv) {
			rows += ", pref, 5dlu";
		}

		panel.setLayout(new FormLayout(cols, rows));

		int col = 2;
		panel.add(new JLabel("Arm"), cc.xy(col, row));

		col += 2;
		panel.add(new JLabel("Ratio to first Arm "), cc.xy(col, row));
		
		for (Node n : sd.getParent().getGraphView().getNL().getNodes()) {
			col += 2;
			panel.add(new JLabel(LaTeXTool.LaTeX2UTF(n.getName())), cc.xy(col, row));
		}

		for (Arm g : gv) {
			row += 2;
			g.addComponents(panel, cc, row);
		}
		return panel;
	}

	public String getRatio() {
		String ratio = "c(";
		for (Arm g : gv) {
			ratio += g.getRatio()+", ";
		}
		return ratio.substring(0, ratio.length()-2)+")";
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==addScenario) {
			gv.add(new Arm(sd, "Arm "+(gv.size()+1)));
			getMainPanel();
			revalidate();
			repaint();
			rmScenario.setEnabled(true);
		} else if (e.getSource()==rmScenario) {
			if (gv.size()>1) {
				gv.remove(gv.size()-1);
				getMainPanel();
				revalidate();
				repaint();
			}
			if (gv.size()==1) {
				rmScenario.setEnabled(false);
			}
		} else if (e.getSource()==jbEndpoint) {
			new EndpointDialog(sd);
		} else if (e.getSource()==jbPopulation) {
			new PopulationDialog(sd);
		}
	}
	
}
