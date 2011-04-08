package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.af.commons.widgets.DesktopPaneBG;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;
import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel implements KeyListener, ActionListener {

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
	CreateGraphGUI parent;
	
	public PView(CreateGraphGUI parent) {
		this.parent = parent;
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
		add(panel, c);
		c.gridy++;
		add(getCorrelatedPanel(), c);
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
	
	JButton refresh;
	
    JRadioButton jrbNoCorrelation = new JRadioButton("No Information about correlations");
    JRadioButton jrbStandardCorrelation = new JRadioButton("Select a standard correlation");
    JRadioButton jrbRCorrelation = new JRadioButton("Select an R correlation matrix");

    JComboBox jcbCorString;
    JComboBox jcbCorObject;
    
    JPanel correlatedPanel = null;
    
	public JPanel getCorrelatedPanel() {
		
		if (correlatedPanel!=null) return correlatedPanel;
		
		try {
			refresh = new JButton(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/mutoss/gui/graph/images/update24.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refresh.setToolTipText("search again for matrices in R");
		
		correlatedPanel = new JPanel();
		
		String[] matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
		
		String[] correlations = new String[] {"Dunnett"};
		//"Dunnett", "Tukey", "Sequen", "AVE", "Changepoint", "Williams", "Marcus", "McDermott", "UmbrellaWilliams", "GrandMean"
		
	    jcbCorString = new JComboBox(correlations);
	    jcbCorObject = new JComboBox(matrices);
		
		if (matrices.length==1 && matrices[0].equals("No quadratic matrices found.")) {
			jcbCorObject.setEnabled(false);
			jrbRCorrelation.setEnabled(false);
		}

	    jrbNoCorrelation.setSelected(true);

	    ButtonGroup group = new ButtonGroup();
	    group.add(jrbNoCorrelation);
	    group.add(jrbStandardCorrelation);
	    group.add(jrbRCorrelation);

	    jrbNoCorrelation.addActionListener(this);
	    jrbStandardCorrelation.addActionListener(this);
	    jrbRCorrelation.addActionListener(this);
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        correlatedPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        correlatedPanel.add(jrbNoCorrelation,     cc.xy(2, row));
        //getContentPane().add(new JLabel(), cc.xy(4, row));        
        
        row += 2;
        
        correlatedPanel.add(jrbStandardCorrelation,     cc.xy(2, row));
        correlatedPanel.add(jcbCorString, cc.xy(4, row));        
        
        row += 2;
        
        correlatedPanel.add(jrbRCorrelation,     cc.xy(2, row));
        correlatedPanel.add(jcbCorObject, cc.xy(4, row));
        correlatedPanel.add(refresh, cc.xy(6, row));  
        refresh.addActionListener(this);
        
        return correlatedPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==refresh) {
			jcbCorObject.removeAllItems();
			String[] matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
			if (matrices.length==1 && matrices[0].equals("No quadratic matrices found.")) {
				jcbCorObject.setEnabled(false);
				jrbRCorrelation.setEnabled(false);
			} else {
				for (String s : matrices) {
					jcbCorObject.addItem(s);
				}
				jcbCorObject.setEnabled(true);
				jrbRCorrelation.setEnabled(true);
			}
		} else if (e.getSource()==jrbNoCorrelation) {
			if (parent.getGraphView().getNL().getKnoten().size()>0) {
				parent.getGraphView().buttonConfInt.setEnabled(true);
				parent.getGraphView().buttonadjPval.setEnabled(true);
			}
		} else if (e.getSource()==jrbStandardCorrelation || e.getSource()==jrbRCorrelation) {
			parent.getGraphView().buttonConfInt.setEnabled(false);
			parent.getGraphView().buttonadjPval.setEnabled(false);
		}
	}
	
}
