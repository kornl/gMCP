package org.mutoss.gui.datatable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

// TODO Most likely this class is superfluous, isn't it?
public class DataView extends JPanel {
	
	public DataView(RDataFrameRef df) {
		//super("Adjacency Matrix");
		GridBagConstraints c = new GridBagConstraints();		
		setLayout(new GridBagLayout());
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		panel = new DataFramePanel(df);
		add(panel, c);
	}

	DataFramePanel panel;
	
	public DataFramePanel getPanel() {
		return panel;
	}
	
}
