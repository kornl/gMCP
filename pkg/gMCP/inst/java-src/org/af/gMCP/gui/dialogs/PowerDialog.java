package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.graph.GraphView;

public class PowerDialog extends JDialog {
	GraphView control;
    
	public PowerDialog(CreateGraphGUI mainFrame) {
		super(mainFrame, "Power");
		control = mainFrame.getGraphView();

		getContentPane().setLayout(new GridBagLayout());		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
	
		
	}
}
