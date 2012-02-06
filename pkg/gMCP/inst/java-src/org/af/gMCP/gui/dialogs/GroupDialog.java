package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GroupDialog extends JDialog implements ActionListener, ChangeListener {

	JButton ok = new JButton("Ok");
	JPanel weightsPanel;
	List<JTextField> weightsV = new Vector<JTextField>();

	public GroupDialog(JFrame parent, int n) {
		super(parent, "Sample Sizes", true);
		setLocationRelativeTo(parent);

		String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, pref:grow, 5dlu, pref, 5dlu";


		FormLayout layout = new FormLayout(cols, rows);
		getContentPane().setLayout(layout);
		CellConstraints cc = new CellConstraints();

		int row = 2;

		createGroupPanel(n);

		JScrollPane sp = new JScrollPane(weightsPanel);
		getContentPane().add(sp, cc.xyw(2, row, 3));

		row += 2;

		getContentPane().add(ok, cc.xy(4, row));
		ok.addActionListener(this);        

		pack();
		setVisible(true);
	}

	private void createGroupPanel(int n) {
		weightsPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;

		weightsPanel.setLayout(new GridBagLayout());

		for (int i=0;i<n;i++) {        		
			weightsV.add(new JTextField("10", 10));
			weightsPanel.add(new JLabel("Group "+(i+1)), c);
			c.gridx++;
			weightsPanel.add(weightsV.get(i), c);
			c.gridx=0;c.gridy++;
		}
	}
	
	public String getGroups() {
		String s = "c(";
		for (int i=0; i<weightsV.size(); i++) {
			s += weightsV.get(i).getText()+(i!=weightsV.size()-1?", ":"");
		}
		return s+")";
	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}

	public void stateChanged(ChangeEvent e) {}	
}
