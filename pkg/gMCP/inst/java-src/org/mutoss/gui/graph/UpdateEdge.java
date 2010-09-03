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
	Edge edge;
	NetzListe netzListe;
	
	public UpdateEdge(Edge edge, NetzListe netzListe) {
		super((JFrame)null, "Updating Edge from node "+edge.von.name+" to "+edge.nach.name);
		this.edge = edge;
		this.netzListe = netzListe;
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        getContentPane().add(new JLabel("Weight for edge:"), cc.xy(2, 2));

        
        String text = ""+edge.w;
        if (text.equals("NaN")) text = "ε";
        tf = new JTextField(text);
        tf.addActionListener(this);
        getContentPane().add(tf, cc.xy(4, 2));

        jb.addActionListener(this);
        getContentPane().add(jb, cc.xy(4, 4));
        
        pack();
        this.setLocation(300, 300);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Double w = 0d;		
		try {
			w = Double.parseDouble(tf.getText());
		} catch (NumberFormatException ve) {
			w = Double.NaN;
		}
		if (w==0) {
			netzListe.removeEdge(edge);
		} else {
			edge.setW(w);
		}
		netzListe.repaint();
		dispose();		
	}
}
