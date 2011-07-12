package org.af.gMCP.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.af.gMCP.gui.MenuBarMGraph;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ParameterDialog extends JDialog implements ActionListener {

	MenuBarMGraph mbar;
	String command;
	JButton ok = new JButton("Ok");
	 JSpinner spinner;
	
	public ParameterDialog(JFrame parent, Hashtable<String,Object> parameters, MenuBarMGraph menuBarMGraph, String command) {
		super(parent, "Number of Hypotheses", true);
		setLocationRelativeTo(parent);
		this.mbar = menuBarMGraph;
		this.command = command;
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        if (parameters.get("n")!=null) {

        	int[] n = (int[]) parameters.get("n");

        	spinner = new JSpinner(new SpinnerNumberModel(n[1], n[0], n[2], 1));    	

        	getContentPane().add(new JLabel("Number of hypotheses:"),     cc.xy(2, row));
        	getContentPane().add(spinner, cc.xy(4, row));        

        	row += 2;

        }

        getContentPane().add(ok, cc.xy(4, row));
        ok.addActionListener(this);        
        
        pack();
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mbar.loadGraph(command+"("+spinner.getModel().getValue()+")");
		dispose();
	}	
}
