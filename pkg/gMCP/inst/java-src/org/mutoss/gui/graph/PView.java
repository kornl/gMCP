package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel implements ActionListener {

	JLabel statusBar;

	private AbstractGraphControl control;
	private Vector<PPanel> panels = new Vector<PPanel>();
	CellConstraints cc = new CellConstraints();	
	JPanel panel = new JPanel();
	JLabel label = new JLabel("Total α: "+0);

	public PView(AbstractGraphControl abstractGraphControl) {
		//super("p-Values");
		this.control = abstractGraphControl;        
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();		
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		add(new JScrollPane(panel), c);
    }
	
	public void addPPanel(Node node) {
		panels.add(new PPanel(node, this));
		setUp();
	}
	
	public void setUp() {
		getHeaderPanel();
		int row = 4;
		for (PPanel p : panels) {
			int col=2;
			for (Component c : p.getComponent()) {
				panel.add(c, cc.xy(col, row));	
				col += 2;
			}
			row += 2;
		}
		panel.add(label,cc.xyw(2, row, 7));
		panel.revalidate();
		//setComponent(new JScrollPane(panel));
	}

	private void getHeaderPanel() {
		panel.removeAll();
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        for (PPanel p : panels) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);        
		
    	panel.add(new JLabel("Hypothesis"), cc.xy(2, 2));

    	panel.add(new JLabel("Significance Level"), cc.xy(4, 2));

    	panel.add(new JLabel("P-Value"), cc.xy(6, 2));
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateLabels() {
		double alpha = 0;
		for (PPanel p : panels) {
			if (!p.rejected) {
				alpha += p.w;
			}
		}
		String text = "Total α: "+(""+alpha).substring(0, Math.min(7,(""+alpha).length()));
		if (alpha>=1) {
			label.setForeground(Color.RED);
			text += "; The total α is greater or equal 1!";
		} else {
			label.setForeground(Color.BLACK);
		}
		
		label.setText(text);
	}

	public void recalculate() {
		for (PPanel p : panels) {
			p.update();
		}
		revalidate();
		repaint();		
	}

	public void newGraph() {
		panels.removeAllElements();		
	}

	public void removePPanel(Node node) {
		for (int i=panels.size()-1;i>=0;i--) {
			if (panels.get(i).node==node) {
				panels.remove(i);
			}
		}
		setUp();
	}
	
}
