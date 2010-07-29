package org.mutoss.gui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.af.commons.widgets.validate.RealTextField;
import org.af.commons.widgets.validate.ValidationException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UpdateNode extends JDialog implements ActionListener {
	
	RealTextField tf;
	JButton jb = new JButton("Update Node");
	Node node;
	
	public UpdateNode(Node node) {
		super((JFrame)null, "Updating Node "+node.name);
		this.node = node;
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        getContentPane().add(new JLabel("α for node "+node.name), cc.xy(2, 2));

        tf = new RealTextField("α for node", 0d,1d, true, false);
        tf.setText(""+node.w);
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
			w = tf.getValidatedValue();
		} catch (ValidationException ve) {}
		node.setW(w, null);		
		dispose();		
	}
}
