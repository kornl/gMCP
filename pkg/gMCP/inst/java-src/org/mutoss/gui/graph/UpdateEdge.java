package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.af.jhlir.call.RErrorException;
import org.mutoss.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UpdateEdge extends JDialog implements ActionListener {
	
	JTextField tf;
	JButton jb = new JButton("Update Edge");
	JButton jbDelete = new JButton("Remove Edge");
	Edge edge;
	NetList netzListe;
	GraphView control;
	
	public UpdateEdge(Edge edge, NetList netzListe, GraphView control) {
		super((JFrame)null, "Updating Edge from node "+edge.from.name+" to "+edge.to.name);
		this.control = control;
		this.edge = edge;
		this.netzListe = netzListe;
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        getContentPane().add(new JLabel("Weight for edge:"), cc.xy(2, 2));

        
        String text = ""+edge.getW();
        if (text.equals("NaN")) text = "ε";
        tf = new JTextField(text);
        tf.addActionListener(this);
        getContentPane().add(tf, cc.xy(4, 2));

        jbDelete.addActionListener(this);
        getContentPane().add(jbDelete, cc.xy(2, 4));
                
        jb.addActionListener(this);
        getContentPane().add(jb, cc.xy(4, 4));
        
        pack();
        this.setLocation(300, 300);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Double w = 0d;		
		if (e.getSource() != jbDelete) {
			String text = tf.getText();
			int letter = -1;
			for (int i=0; i<26; i++) {
				char l = (char) ('a' + i);
				if (text.lastIndexOf(l)!=-1) {
					if (letter!=-1) {
						JOptionPane.showMessageDialog(this, "Only one variable is allowed. " +
								"There are at least '"+(char)letter+"' and '"+l+"'.",
								"Only one variable is allowed.", JOptionPane.ERROR_MESSAGE);
					}
					letter = 'a' + i;
				}				
			}
			
			/* This will be enabled as soon as epsilons are correctly implemented.*/
			/*try {
				w = RControl.getR().eval(tf.getText().replace(",", ".")).asRNumeric().getData()[0];		
				tf.setBackground(Color.WHITE);
			} catch (RErrorException nfe) {		
				tf.setBackground(Color.RED);
				JOptionPane.showMessageDialog(this, "The expression \""+tf.getText()+"\" is not a valid number.", "Not a valid number", JOptionPane.ERROR_MESSAGE);
			}*/
			try {
				w = Double.parseDouble(tf.getText());
			} catch (NumberFormatException ve) {
				w = Double.NaN;
			}
		}
		if (w==0) {
			control.getDataTable().getModel().setValueAt(0, netzListe.getKnoten().indexOf(edge.from), netzListe.getKnoten().indexOf(edge.to));
			netzListe.removeEdge(edge);			
		} else {
			edge.setW(w);	
			control.getDataTable().getModel().setValueAt(w, netzListe.getKnoten().indexOf(edge.from), netzListe.getKnoten().indexOf(edge.to));
		}
		netzListe.repaint();
		dispose();		
	}
}
