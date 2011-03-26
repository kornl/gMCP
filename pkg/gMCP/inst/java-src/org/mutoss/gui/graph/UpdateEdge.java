package org.mutoss.gui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

        
        String text = edge.getWS();
        
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
			edge.setW(tf.getText());	
			control.getDataTable().getModel().setValueAt(tf.getText(), netzListe.getKnoten().indexOf(edge.from), netzListe.getKnoten().indexOf(edge.to));
		}
		netzListe.repaint();
		dispose();		
	}
}
