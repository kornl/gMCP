package org.mutoss.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.mutoss.config.Configuration;
import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.Node;

public class RearrangeNodesDialog extends JDialog implements ActionListener {

	OkCancelButtonPane pane = new OkCancelButtonPane();
	
	protected JRadioButton jrbCircle = new JRadioButton("Arrange on a circle");
    protected JRadioButton jrbMatrix = new JRadioButton("Arrange as matrix");
    JSpinner tfRows;
    JSpinner tfCols;
	JCheckBox jByRow = new JCheckBox("place by row");
	GraphView control;
    
	public RearrangeNodesDialog(CreateGraphGUI mainFrame) {
		super(mainFrame, "Specify layout for nodes");
		control = mainFrame.getGraphView();

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		ButtonGroup group = new ButtonGroup();
	    group.add(jrbCircle);
	    group.add(jrbMatrix);
		
		int n = mainFrame.getGraphView().getNL().getKnoten().size();
		tfRows = new JSpinner(new SpinnerNumberModel(Math.round(Math.sqrt(n)), 1, n, 1));
		tfCols = new JSpinner(new SpinnerNumberModel(Math.round(Math.sqrt(n)+1), 1, n, 1));
		
		jrbMatrix.setSelected(true);
		
		getContentPane().add(jrbCircle, c);
		c.gridy++;
		getContentPane().add(jrbMatrix, c);
		c.gridy++;
		
		getContentPane().add(new JLabel("Number of rows"), c);
		c.gridx++;
		getContentPane().add(tfRows, c);
		c.gridy++;c.gridx=0;
		
		getContentPane().add(new JLabel("Number of columns"), c);
		c.gridx++;
		getContentPane().add(tfCols, c);
		c.gridy++;c.gridx=0;
		
		c.gridx = 1; c.gridwidth = 2;
		pane.addActionListener(this);
		getContentPane().add(pane, c);
		pack();	
		
	    setLocationRelativeTo(mainFrame);
	    
		setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(OkCancelButtonPane.OK_CMD)) {			
			String rows = tfRows.getModel().getValue().toString();
			String cols = tfCols.getModel().getValue().toString();
			String byrow = jByRow.isSelected()?"TRUE":"FALSE";
			String layout = jrbCircle.isSelected()?"":", nrow="+rows+", ncol="+cols+", byrow = "+byrow;
			String graphName = ".tmpGraph" + (new Date()).getTime();
			control.getNL().saveGraph(graphName, false);
			RControl.getR().eval(graphName +" <- placeNodes("+graphName+layout+", force = TRUE)");
			control.getNL().loadGraph(graphName);
		}
		dispose();		
	}

}
