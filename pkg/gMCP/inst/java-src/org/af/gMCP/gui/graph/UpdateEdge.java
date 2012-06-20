package org.af.gMCP.gui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.dialogs.ColorChooseDialog;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UpdateEdge extends JDialog implements ActionListener {
	
	JTextField tf;
	JButton jb = new JButton("Update Edge");
	JButton jbDelete = new JButton("Remove Edge");
	JButton jbColor = new JButton("Choose Color");
	Edge edge;
	NetList netzListe;
	GraphView control;
	JTabbedPane tabbedPane = new JTabbedPane();
	JCheckBox jcbAnchored = new JCheckBox("Weight is anchored and does not follows nodes when moved.");
	
	public UpdateEdge(Edge edge, NetList netzListe, GraphView control) {
		super(netzListe.control.parent, "Updating Edge from node "+edge.from.getName()+" to "+edge.to.getName(), true);
		this.control = control;
		this.edge = edge;
		this.netzListe = netzListe;
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        tabbedPane.addTab("Weight & Anchor", getMainPanel());
        tabbedPane.addTab("Further Attributes", getSubPanel());
        getContentPane().add(tabbedPane, cc.xyw(1, 2, 5));

        jbDelete.addActionListener(this);
        getContentPane().add(jbDelete, cc.xy(2, 4));
                
        jb.addActionListener(this);
        getContentPane().add(jb, cc.xy(4, 4));
        
        pack();
        setSize(Math.max(this.getSize().width, 450), this.getSize().height);
        this.setLocation(300, 300);
        setVisible(true);
	}
	
	protected JPanel getMainPanel() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        panel.add(new JLabel("Weight for edge:"), cc.xy(2, 2));

        String text = edge.getWS();
        
        tf = new JTextField(text);
        tf.addActionListener(this);
        panel.add(tf, cc.xy(4, 2));
        
        jcbAnchored.addActionListener(this);
        jcbAnchored.setSelected(edge.isFixed());
        if (!Configuration.getInstance().getGeneralConfig().getUnAnchor()) {        
        	panel.add(jcbAnchored, cc.xyw(2, 4, 3));
        }
        
		return panel;
	}

	JLabel colorLabel = new JLabel("     ");
	
	protected JPanel getSubPanel() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);
        CellConstraints cc = new CellConstraints();
        
        panel.add(new JLabel("Color:"), cc.xy(2, 2));
		
        colorLabel.setOpaque(true);
        colorLabel.setBackground(edge.color);
        panel.add(colorLabel, cc.xy(4, 2));
        
        jbColor.addActionListener(this);
        panel.add(jbColor, cc.xy(6, 2));
        
		return panel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbColor) {
			ColorChooseDialog ccd = new ColorChooseDialog(this);
			edge.color = ccd.getColor();
			colorLabel.setBackground(edge.color);
			return;
		}
		if (e.getSource() == jcbAnchored) {
			edge.setFixed(jcbAnchored.isSelected());
			return;
		}
		Double w = 0d;		
		if (e.getSource() != jbDelete) {			
			try {
				w = Double.parseDouble(tf.getText());
			} catch (NumberFormatException ve) {
				w = Double.NaN;
			}
			// An empty String is considered as 0.
			if(tf.getText().length()==0) w = 0d;
		}
		if (w==0) {
			control.getDataTable().getModel().setValueAt(new EdgeWeight(0), netzListe.getNodes().indexOf(edge.from), netzListe.getNodes().indexOf(edge.to));
			netzListe.removeEdge(edge);			
		} else {
			edge.setW(tf.getText());	
			control.getDataTable().getModel().setValueAt(new EdgeWeight(tf.getText()), netzListe.getNodes().indexOf(edge.from), netzListe.getNodes().indexOf(edge.to));
		}
		netzListe.repaint();
		dispose();		
	}
}
