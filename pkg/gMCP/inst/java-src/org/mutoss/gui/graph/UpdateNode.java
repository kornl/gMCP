package org.mutoss.gui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.commons.widgets.validate.ValidationException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UpdateNode extends JDialog implements ActionListener {
	
	RealTextField tf;
	JTextField tfname;
	JButton jb = new JButton("Update Node");
	JButton jbDeleteNode = new JButton("Delete Node");
	Node node;
	NetzListe netzListe;
	
	public UpdateNode(Node node, NetzListe netzListe) {
		super((JFrame)null, "Updating Node "+node.name);
		this.node = node;
		this.netzListe = netzListe;
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        int row = 2;
        
        getContentPane().add(new JLabel("α for node "+node.name), cc.xy(2, row));

        tf = new RealTextField("α for node", 0d,1d, true, false);
        tf.setText(""+node.getAlpha());
        tf.addActionListener(this);
        getContentPane().add(tf, cc.xy(4, row));

        row += 2;
        
        getContentPane().add(new JLabel("New name"), cc.xy(2, row));

        tfname = new JTextField();
        tfname.addActionListener(this);
        tfname.setText(node.getName());
        getContentPane().add(tfname, cc.xy(4, row));

        row += 2;
        
        jbDeleteNode.addActionListener(this);
        getContentPane().add(jbDeleteNode, cc.xy(2, row));
                
        jb.addActionListener(this);
        getContentPane().add(jb, cc.xy(4, row));
        
        pack();
        this.setLocation(300, 300);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jbDeleteNode)) {
			netzListe.removeNode(node);
			dispose();		
			return;
		}	
		Double w = 0d;		
		try {
			w = tf.getValidatedValue();
		} catch (ValidationException ve) {}
		node.setAlpha(w, null);	
		node.setName(tfname.getText());
		netzListe.repaint();
		dispose();		
	}
}
