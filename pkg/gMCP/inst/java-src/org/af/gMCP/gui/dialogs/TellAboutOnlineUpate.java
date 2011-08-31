package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.af.gMCP.gui.graph.Node;

public class TellAboutOnlineUpate extends JDialog implements ActionListener {

	JButton jb = new JButton("Ok");
	private JCheckBox checkOnlineForUpdate = new JCheckBox("Check online for updates");
	
	public TellAboutOnlineUpate(JFrame mainFrame) {
		super(mainFrame, "Check for online updates");

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		checkOnlineForUpdate.setSelected(true);
		
		c.gridx=0; 
		(getContentPane()).add(new JLabel("Hypostheses"), c);
		c.gridx=1;
		(getContentPane()).add(new JLabel(""), c);
		c.gridy++;		

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
