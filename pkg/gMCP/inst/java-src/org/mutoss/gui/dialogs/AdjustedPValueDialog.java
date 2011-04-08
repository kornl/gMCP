package org.mutoss.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.mutoss.config.Configuration;
import org.mutoss.gui.graph.Node;

public class AdjustedPValueDialog extends JDialog implements ActionListener {

	JButton jb = new JButton("Ok");
	
	public AdjustedPValueDialog(JFrame mainFrame, List<Double> pValues, double[] adjPValues, Vector<Node> vector) {
		super(mainFrame, "Adjusted p-Values");

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		c.gridx=0; 
		(getContentPane()).add(new JLabel("Hypostheses"), c);
		c.gridx=1;
		(getContentPane()).add(new JLabel("raw p-values"), c);
		c.gridx=2;
		(getContentPane()).add(new JLabel("adjusted p-values"), c);
		c.gridy++;		
		
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		for (int i=0; i<adjPValues.length; i++) {
			c.gridx=0; 
			(getContentPane()).add(new JLabel(""+vector.get(i).getName()+":"), c);
			c.gridx=1;
			(getContentPane()).add(new JLabel(""+format.format(pValues.get(i))), c);
			c.gridx=2;
			(getContentPane()).add(new JLabel(""+format.format(adjPValues[i])), c);
			c.gridy++;
		}
		c.gridx = 1;
		jb.addActionListener(this);
		(getContentPane()).add(jb, c);
		pack();	
		
	    setLocationRelativeTo(mainFrame);
	    
		setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();		
	}

}
