package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel implements KeyListener {

	JLabel statusBar;
	private static final Log logger = LogFactory.getLog(PView.class);

	private Vector<PPanel> panels = new Vector<PPanel>();
	CellConstraints cc = new CellConstraints();	
	//JPanel panel = new JPanel();
	JLabel label = new JLabel("");
	JLabel weightLabel = new JLabel("Weight");
	JLabel alphaLabel = new JLabel("Total α: ");
	JTextField totalAlpha = new JTextField("0.05");
	GridBagConstraints c = new GridBagConstraints();
	
	public PView() {  
		setLayout(new GridBagLayout());
				
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		
		totalAlpha.addKeyListener(this);
		
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
			debug += Configuration.getInstance().getGeneralConfig().getDecFormat().format(panel.getP())+"; ";
		}
		logger.debug(debug);
	}
	
	public void setPValues(Double[] pvalues) {
		pValues = Arrays.asList(pvalues);
		restorePValues();
	}
	
	public void restorePValues() {
		String debug = "Restoring : ";
		if (pValues != null) {
			for (int i=0; i<pValues.size(); i++) {
				if (i<panels.size()) {
					panels.get(i).setP(pValues.get(i));
					debug += Configuration.getInstance().getGeneralConfig().getDecFormat().format(pValues.get(i))+"; ";
				}
			}
		}
		logger.debug(debug);
	}
	
	public void setUp() {
		JPanel panel = new JPanel();
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        for (PPanel p : panels) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);        
		
    	panel.add(new JLabel("Hypothesis"), cc.xy(2, 2));
    	
    	panel.add(weightLabel, cc.xy(4, 2));
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
		row += 2;
		panel.add(alphaLabel, cc.xy(2, row));    	
    	panel.add(totalAlpha, cc.xy(4, row));
		
		panel.revalidate();
		removeAll();
		add(new JScrollPane(panel), c);
	}
	
	public void updateLabels() {
		double alpha = 0;
		for (PPanel p : panels) {
			if (!p.rejected) {
				alpha += p.w;
			}
		}
		String text = "Sum of weights: "+Configuration.getInstance().getGeneralConfig().getDecFormat().format(alpha);
		if (alpha>1) {
			label.setForeground(Color.RED);
			text += "; The total weight is greater 1!";
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
			p.updateMe(true);
		}
		totalAlpha.setEditable(!b);
		if (b) {
			weightLabel.setText("α Level");
		} else {
			weightLabel.setText("Weight");
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

	public double getTotalAlpha() {
		return Double.parseDouble(totalAlpha.getText());
	}

	public void keyPressed(KeyEvent e) {keyTyped(e);}

	public void keyReleased(KeyEvent e) {keyTyped(e);}

	public void keyTyped(KeyEvent e) {
		for (PPanel p : panels) {
			p.updateMe(false);
		}
	}
	
}
