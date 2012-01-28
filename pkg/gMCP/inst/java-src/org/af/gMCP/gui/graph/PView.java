package org.af.gMCP.gui.graph;

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
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.GroupDialog;
import org.af.jhlir.call.RChar;
import org.af.jhlir.call.RInteger;
import org.af.jhlir.call.RList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel implements KeyListener, ActionListener {

	private static final Log logger = LogFactory.getLog(PView.class);

	private Vector<PPanel> panels = new Vector<PPanel>();
	CellConstraints cc = new CellConstraints();	
	JLabel statusLabel = new JLabel("");
	JLabel weightLabel = new JLabel("Weight");
	JLabel alphaLabel = new JLabel("Total α: ");
	JTextField totalAlpha = new JTextField("0.05");
	GridBagConstraints c = new GridBagConstraints();
	CreateGraphGUI parent;
	JButton jbLoadPValues = new JButton("Load p-values from R"); 
	
	public PView(CreateGraphGUI parent) {
		this.parent = parent;
		setLayout(new GridBagLayout());
				
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		
		totalAlpha.addKeyListener(this);
		
		setUp();
		
		jbLoadPValues.addActionListener(this);
    }
	
	public void addPPanel(Node node) {
		panels.add(new PPanel(node, this));
		//logger.debug("Added panel for node "+node.getName());		
		setUp();
	}
	
	List<Double> pValues = null;
	
	public void savePValues() {
		String debug = "Saving PValues: ";
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
	
	public void setPValues(double[] pvalues) {
		setPValues(ArrayUtils.toObject(pvalues));
	}

	
	public void restorePValues() {
		String debug = "Restoring PValues: ";
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
		panel.add(statusLabel, cc.xyw(2, row, 3));
		panel.add(jbLoadPValues, cc.xy(6, row));
		row += 2;
		panel.add(alphaLabel, cc.xy(2, row));    	
    	panel.add(totalAlpha, cc.xy(4, row));
    	
    	updateLabels();
		panel.revalidate();		
		removeAll();		
		add(panel, c);
		c.gridy++;
		add(getCorrelatedPanel(), c);
		revalidate();
	}
	
	public void updateLabels() {
		double weight = 0;
		for (PPanel p : panels) {
			if (!p.rejected) {
				weight += p.w;
			}
		}
		String text = "Sum of weights: "+Configuration.getInstance().getGeneralConfig().getDecFormat().format(weight);
		if (weight>1.0001) {
			statusLabel.setForeground(Color.RED);
			text += "; The total weight is greater 1!";
		} else {
			statusLabel.setForeground(Color.BLACK);
		}		
		statusLabel.setText(text);
	}

	public void recalculate() {
		for (PPanel p : panels) {
			p.updateMe(true);
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
				//logger.debug("Removed panel for node "+node.getName());
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
	
	protected JRadioButton jrbNoCorrelation = new JRadioButton("No Information about correlations");
    protected JRadioButton jrbStandardCorrelation = new JRadioButton("Select a standard correlation");
    protected JRadioButton jrbRCorrelation = new JRadioButton("Select an R correlation matrix");
    protected JRadioButton jrbSimes = new JRadioButton("Correlation applicable for Simes test (new feature that needs still testing)");

    protected JComboBox jcbCorString;
    protected JComboBox jcbCorObject;
    
    JPanel correlatedPanel = null;
    
	public JPanel getCorrelatedPanel() {
		
		if (correlatedPanel!=null) {
			getPossibleCorrelations();		
			return correlatedPanel;
		}
		
		try {
			refresh = new JButton(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/af/gMCP/gui/graph/images/update24.png"))));
		} catch (IOException e) {
			logger.error("IOError that should never happen.", e);
		}
		refresh.setToolTipText("search again for matrices in R");
		
		correlatedPanel = new JPanel();
		
		String[] matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
		
	    jcbCorString = new JComboBox(new String[] {});
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
	    group.add(jrbSimes);

	    jrbNoCorrelation.addActionListener(this);
	    jrbStandardCorrelation.addActionListener(this);
	    jrbRCorrelation.addActionListener(this);
	    jrbSimes.addActionListener(this);
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        correlatedPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        correlatedPanel.add(jrbNoCorrelation,     cc.xyw(2, row, 3));     
        
        row += 2;
        
        correlatedPanel.add(jrbStandardCorrelation,     cc.xy(2, row));
        correlatedPanel.add(jcbCorString, cc.xy(4, row));        
        
        row += 2;
        
        correlatedPanel.add(jrbRCorrelation,     cc.xy(2, row));
        correlatedPanel.add(jcbCorObject, cc.xy(4, row));
        correlatedPanel.add(refresh, cc.xy(6, row));  
        
        row += 2;
        
        correlatedPanel.add(jrbSimes,     cc.xyw(2, row, 3));
                
        refresh.addActionListener(this);
        
        return correlatedPanel;
	}

	private void getPossibleCorrelations() {
		jcbCorString.removeAllItems();
		int n = parent.getGraphView().getNL().getNodes().size();
		if (n!=0) {
			RList list = RControl.getR().eval("gMCP:::getAvailableStandardDesigns("+n+")").asRList();
			RChar designs = list.get(0).asRChar();
			RInteger groups = list.get(1).asRInteger();
			String[] s = new String[designs.getLength()];
			for (int i=0; i<s.length; i++) {
				jcbCorString.addItem(designs.getData()[i] + " ("+ groups.getData()[i]+" groups)"); 
			}		
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==refresh) {
			jcbCorObject.removeAllItems();
			String[] matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
			if (matrices.length==1 && matrices[0].equals("No quadratic matrices found.")) {
				jcbCorObject.setEnabled(false);
				jrbRCorrelation.setEnabled(false);
			} else {				
				jcbCorObject.setEnabled(true);
				jrbRCorrelation.setEnabled(true);
			}
			for (String s : matrices) {
				jcbCorObject.addItem(s);
			}
		} else if (e.getSource()==jrbNoCorrelation) {
			if (parent.getGraphView().getNL().getNodes().size()>0) {
				parent.getGraphView().buttonConfInt.setEnabled(true);
				parent.getGraphView().buttonadjPval.setEnabled(true);
			}
		} else if (e.getSource()==jrbStandardCorrelation || e.getSource()==jrbRCorrelation) {
			parent.getGraphView().buttonConfInt.setEnabled(false);
			parent.getGraphView().buttonadjPval.setEnabled(true);
		} else if (e.getSource()==jrbSimes) {
			parent.getGraphView().buttonConfInt.setEnabled(false);
			parent.getGraphView().buttonadjPval.setEnabled(false);
		} else if (e.getSource()==jbLoadPValues) {
			parent.getGraphView().loadPValuesFromR(); 
		}
	}

	public String getParameters() {
		String param = "";
		if (jrbStandardCorrelation.isSelected()) {
			String s = jcbCorString.getSelectedItem().toString();
			String design = s.substring(0, s.indexOf(" "));
			String n = s.substring(s.indexOf("(")+1, s.indexOf("groups")-1);
			logger.warn("Design: \""+design+"\", n=\""+n+"\"");
			GroupDialog gd = new GroupDialog(parent, Integer.parseInt(n));
			param = ", correlation=\""+design+"\", samplesize="+gd.getGroups();
		} else if (jrbRCorrelation.isSelected()) {
			param = ", correlation="+jcbCorObject.getSelectedItem()+"";
		} else if (jrbSimes.isSelected()) {
			param = ", test=\"Simes\"";
		}
		return param;
	}
	
}
