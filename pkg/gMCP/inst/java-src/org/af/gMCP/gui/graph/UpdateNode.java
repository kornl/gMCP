package org.af.gMCP.gui.graph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.af.gMCP.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UpdateNode extends JDialog implements ActionListener {
	
	JTextField tf;
	JTextField tfname;
	JButton jb = new JButton("Update Node");
	JButton jbDeleteNode = new JButton("Delete Node");
	Node node;
	NetList netzListe;
	GraphView gv;
	
	public UpdateNode(Node node, GraphView gv) {
		super(gv.parent, "Updating Node "+node.getName(), true);
		this.node = node;
		this.gv = gv;
		this.netzListe = gv.getNL();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        int row = 2;
        
        getContentPane().add(new JLabel("Weight for node "+node.getName()), cc.xy(2, row));

        tf = new JTextField("", 7);
        tf.setText(""+RControl.getFraction(node.getWeight()));
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
			w = RControl.getR().eval(tf.getText().replace(",", ".")).asRNumeric().getData()[0];		
			tf.setBackground(Color.WHITE);
		} catch (Exception nfe) {		
			tf.setBackground(Color.RED);
			JOptionPane.showMessageDialog(this, "The expression \""+tf.getText()+"\" is not a valid number.", "Not a valid number", JOptionPane.ERROR_MESSAGE);
		}
		node.setWeight(w, null);
		int which = netzListe.whichNode(tfname.getText());
		if (which == -1 || netzListe.getNodes().get(which) == node) {
			gv.renameNode(node, tfname.getText());			
			//TODO Change Name in PView and RDataFrameRef(which, tfname.getText())
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "There is already a node with name \""+tfname.getText()+"\"", "Node name already in use", JOptionPane.ERROR_MESSAGE);
		}		
		netzListe.repaint();				
	}
}
