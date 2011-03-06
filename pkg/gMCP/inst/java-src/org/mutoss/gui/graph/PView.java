package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel {

	JLabel statusBar;
	private static final Log logger = LogFactory.getLog(PView.class);

	private ControlMGraph control;
	private Vector<PPanel> panels = new Vector<PPanel>();
	CellConstraints cc = new CellConstraints();	
	//JPanel panel = new JPanel();
	JLabel label = new JLabel("Total α: "+0);
	GridBagConstraints c = new GridBagConstraints();
	
	public PView(ControlMGraph abstractGraphControl) {
		//super("p-Values");
		this.control = abstractGraphControl;        
		setLayout(new GridBagLayout());
				
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		
		setUp();
    }
	
	public void addPPanel(Node node) {
		panels.add(new PPanel(node, this));
		logger.debug("Added panel for node "+node.getName());
		setUp();
	}
	
	List<Double> pValues = null;
	
	public void savePValues() {
		String debug = "Saving : ";
		pValues = new Vector<Double>();
		for (PPanel panel : panels) {
			pValues.add(panel.getP());
			debug += format.format(panel.getP())+"; ";
		}
		logger.debug(debug);
	}
	
	public void restorePValues() {
		String debug = "Restoring : ";
		if (pValues != null) {
			for (int i=0; i<pValues.size(); i++) {
				if (i<panels.size()) {
					panels.get(i).setP(pValues.get(i));
					debug += format.format(pValues.get(i))+"; ";
				}
			}
		}
		logger.debug(debug);
	}
	
	public void setUp() {
		JPanel panel = new JPanel();
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu";
        for (PPanel p : panels) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);        
		
    	panel.add(new JLabel("Hypothesis"), cc.xy(2, 2));
    	
    	panel.add(new JLabel("α Level"), cc.xy(4, 2));
    	//panel.add(new JLabel("Signif. Level"), cc.xy(4, 2));

    	panel.add(new JLabel("P-Value"), cc.xy(6, 2));
				
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
		removeAll();
		add(new JScrollPane(panel), c);
	}

	DecimalFormat format = new DecimalFormat("#.####");
	
	public void updateLabels() {
		double alpha = 0;
		for (PPanel p : panels) {
			if (!p.rejected) {
				alpha += p.w;
			}
		}
		String text = "Total α: "+format.format(alpha);
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
		/*		panel.remove(panels.get(i).label);
				panel.remove(panels.get(i).jb);
				panel.remove(panels.get(i).pTF);
				panel.remove(panels.get(i).wTF); */
				panels.remove(i);
				logger.debug("Removed panel for node "+node.getName());
			}
		}
		setUp();		
	}
	
	public void setTesting(boolean b) {
		PPanel.setTesting(b);
		for (PPanel p : panels) {
			p.keyTyped(null);
		}
	}

	public String getPValuesString() {
		savePValues();
		String s = "c(";
		for (double p : pValues) {
			s += p+", ";
		}
		return s.substring(0, s.length()-2)+")";
	}

	public double getPValue(Node node) {
		for (int i=panels.size()-1;i>=0;i--) {
			if (panels.get(i).node==node) {
				return panels.get(i).p;
			}
		}
		throw new RuntimeException("Something happend that should never happen. Please report!");
	}
	
}
