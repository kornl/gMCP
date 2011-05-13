package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.af.gMCP.gui.graph.Node;

public class RejectedDialog extends JDialog implements ActionListener {

	JButton jb = new JButton("Ok");
	
	public RejectedDialog(JFrame mainFrame, boolean[] rejected, Vector<Node> vector) {
		super(mainFrame, "Rejected Null Hypotheses");

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
		(getContentPane()).add(new JLabel(""), c);
		c.gridy++;		
		for (int i=0; i<rejected.length; i++) {
			c.gridx=0; 
			(getContentPane()).add(new JLabel(""+vector.get(i).getName()+":"), c);
			c.gridx=1;
			(getContentPane()).add(new JLabel(""+(rejected[i]?"rejected":"not rejected")), c);
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
