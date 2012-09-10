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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.af.commons.widgets.DesktopPaneBG;
import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.MatrixCreationDialog;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PView extends JPanel implements KeyListener, ActionListener {

	private static final Log logger = LogFactory.getLog(PView.class);

	JLabel alphaLabel = new JLabel("Total α: ");
	GridBagConstraints c = new GridBagConstraints();	
	CellConstraints cc = new CellConstraints();
	JPanel correlatedPanel = null;
	JButton createMatrix;
	JButton jbLoadPValues = new JButton("Load p-values from R");
	public JComboBox jcbCorObject;
	
	protected JRadioButton jrbNoCorrelation = new JRadioButton("No Information about correlations");
	public JRadioButton jrbRCorrelation = new JRadioButton("Select an R correlation matrix"); 
	public JRadioButton jrbSimes = new JRadioButton("Correlation applicable for Simes test (new feature that needs still testing)");
	
	private Vector<PPanel> panels = new Vector<PPanel>();
	
	CreateGraphGUI parent;
	
	List<Double> pValues = null;
	
	JButton refresh;
	
	JLabel statusLabel = new JLabel("");
	
	private RealTextField totalAlpha = new RealTextField("totalAlpha", 0, 1);
	
	JLabel weightLabel = new JLabel("Weight");
	
	public PView(CreateGraphGUI parent) {
		this.parent = parent;
		setLayout(new GridBagLayout());
				
		c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
		
		totalAlpha.addKeyListener(this);
		totalAlpha.setText(Configuration.getInstance().getClassProperty(this.getClass(), "alpha level", "0.05"));
		
		setUp();
		
		jbLoadPValues.addActionListener(this);
    }

	public void actionPerformed(ActionEvent e) {
		parent.getGraphView().setResultUpToDate(false);
		if (e.getSource()==refresh) {
			refresh(true);
		} else if (e.getSource()==jrbNoCorrelation) {
			if (parent.getGraphView().getNL().getNodes().size()>0) {
				parent.getGraphView().buttonConfInt.setEnabled(true);
				parent.getGraphView().buttonadjPval.setEnabled(true);
			}
		}  else if (e.getSource()==jrbRCorrelation) {
			parent.getGraphView().buttonConfInt.setEnabled(false);
			parent.getGraphView().buttonadjPval.setEnabled(true);
		} else if (e.getSource()==jrbSimes) {
			parent.getGraphView().buttonConfInt.setEnabled(false);
			parent.getGraphView().buttonadjPval.setEnabled(true);
		} else if (e.getSource()==jbLoadPValues) {
			parent.getGraphView().loadPValuesFromR(); 
		} else if (e.getSource()==createMatrix) {
			if (parent.getGraphView().getNL().getNodes().size()<2) {
				JOptionPane.showMessageDialog(parent, "Correlation makes only sense for more than one hypothesis.", "No correlation for one hypothesis", JOptionPane.ERROR_MESSAGE);
			} else {				
				String obj = jcbCorObject.getSelectedItem().toString();
				String matrix = obj.endsWith("matrices found.")?null:obj;
				new MatrixCreationDialog(parent, matrix, MatrixCreationDialog.getNames(parent.getGraphView().getNL().getNodes()));
				refresh(false);
				jrbRCorrelation.setSelected(true);
			}
		}
	}

	public void addPPanel(Node node) {
		panels.add(new PPanel(node, this));
		//logger.debug("Added panel for node "+node.getName());		
		setUp();
	}

	public JPanel getCorrelatedPanel() {
		
		if (correlatedPanel!=null) {
			refresh(false);		
			return correlatedPanel;
		}
		
		try {
			refresh = new JButton(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/af/gMCP/gui/graph/images/update24.png"))));
			createMatrix = new JButton(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/af/gMCP/gui/graph/images/matrix.png"))));
		} catch (IOException e) {
			logger.error("IOError that should never happen.", e);
		}
		refresh.setToolTipText("search again for matrices in R");
		createMatrix.setToolTipText("create matrix with GUI");
        refresh.addActionListener(this);
        createMatrix.addActionListener(this);
		
	    ButtonGroup group = new ButtonGroup();
	    group.add(jrbNoCorrelation);
	    group.add(jrbRCorrelation);
	    group.add(jrbSimes);

	    jrbNoCorrelation.addActionListener(this);
	    jrbRCorrelation.addActionListener(this);
	    jrbSimes.addActionListener(this);
		
		correlatedPanel = new JPanel();
		
	    jcbCorObject = new JComboBox(new String[] {});
	    jcbCorObject.addActionListener(this);
	    refresh(false);

	    if (!jrbRCorrelation.isSelected() && !jrbSimes.isSelected()) {
	    	jrbNoCorrelation.setSelected(true);
	    }
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        correlatedPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        correlatedPanel.add(jrbNoCorrelation,     cc.xyw(2, row, 7));     
        
        row += 2;
        
        correlatedPanel.add(jrbRCorrelation,     cc.xy(2, row));
        correlatedPanel.add(jcbCorObject, cc.xy(4, row));
        correlatedPanel.add(refresh, cc.xy(6, row));  
        correlatedPanel.add(createMatrix, cc.xy(8, row));
        
        row += 2;
        
        correlatedPanel.add(jrbSimes,     cc.xyw(2, row, 7));
        
        return correlatedPanel;
	}
	
	/**
	 * Constructs a String to be included in the gMCP call.
	 * @return String that is either empty or starts with a comma and 
	 * adds parameters to the gMCP call depending on the selected correlation.
	 */
	public String getParameters() {
		if (jrbRCorrelation.isSelected() && !jrbRCorrelation.isEnabled()) {
			JOptionPane.showMessageDialog(parent, "No correlation matrix available.\nUsing Bonferroni based test.", "No correlation matrix available.", JOptionPane.WARNING_MESSAGE);
			jrbNoCorrelation.setSelected(true);
		}
		String param = ", test=\"Bonferroni\"";
		if (jrbRCorrelation.isSelected()) {
			param = ", correlation="+jcbCorObject.getSelectedItem()+", test=\""+Configuration.getInstance().getGeneralConfig().getParametricTest()+"\"";
		} else if (jrbSimes.isSelected()) {
			param = ", test=\"Simes\"";
		}
		return param;
	}

	public double getPValue(Node node) {
		for (int i=panels.size()-1;i>=0;i--) {
			if (panels.get(i).node==node) {
				return panels.get(i).p;
			}
		}
		throw new RuntimeException("Something happend that should never happen. Please report!");
	}

	public String getPValuesString() {		
		String s = "c(";
		for (PPanel panel : panels) {		
			s += panel.getP()+", ";
		}
		return s.substring(0, s.length()-2)+")";
	}

	public double getTotalAlpha() {
		try {
			return Double.parseDouble(totalAlpha.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parent, "Alpha "+totalAlpha.getText()+" is no valid number between 0 and 1.", "Error parsing alpha", JOptionPane.ERROR_MESSAGE);
			return 1;
		}
	}

	public void keyPressed(KeyEvent e) {keyTyped(e);}

	public void keyReleased(KeyEvent e) {keyTyped(e);}

	public void keyTyped(KeyEvent e) {
		parent.getGraphView().setResultUpToDate(false);
		for (PPanel p : panels) {
			p.updateMe(false);
		}
	}
	
	public void newGraph() {
		panels.removeAllElements();		
	}
	public void recalculate() {
		for (PPanel p : panels) {
			p.updateMe(true);
		}
		revalidate();
		repaint();		
	}
	
	private void refresh(boolean showInfo) {
		jcbCorObject.removeAllItems();
		int dim = parent.getGraphView().getNL().getNodes().size();
		String[] matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices(n="+dim+")").asRChar().getData();
		if (showInfo && !Configuration.getInstance().getClassProperty(this.getClass(), "showRefreshInfo", "yes").equals("no")) {
			JCheckBox tellMeAgain = new JCheckBox("Don't show me this info again.");
			int n = (matrices.length==1 && matrices[0].endsWith("matrices found."))?0:+matrices.length;
			String message = "Searched and found "+n+((n==1)?" matrix":" matrices")+" of\n" +
					"dimension "+dim+" in the R global environment.";
			JOptionPane.showMessageDialog(parent, new Object[] {message, tellMeAgain}, "Info", JOptionPane.INFORMATION_MESSAGE);
			if (tellMeAgain.isSelected()) {
				Configuration.getInstance().setClassProperty(this.getClass(), "showRefreshInfo", "no");
			}
		}
		if (matrices.length==1 && matrices[0].endsWith("matrices found.")) {
			/*if(jrbRCorrelation.isSelected()) {
				jrbNoCorrelation.setSelected(true);
			}*/
			jcbCorObject.setEnabled(false);
			jrbRCorrelation.setEnabled(false);			
		} else {				
			jcbCorObject.setEnabled(true);
			jrbRCorrelation.setEnabled(true);
		}
		for (String s : matrices) {
			jcbCorObject.addItem(s);
		}
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
    public void renameNode(int i, String name) {
		panels.get(i).label.setText(name);		
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
    
    public void savePValues() {
		String debug = "Saving PValues: ";
		pValues = new Vector<Double>();
		for (PPanel panel : panels) {
			/*TODO The following line is a work-around for the following problem:
			 * If I insert pvalues with the middle mouse button no keyTyped event has been raised.
			 * Also InputMethodListener does not work. 
			 */
			panel.keyTyped(null);
			pValues.add(panel.getP());
			debug += Configuration.getInstance().getGeneralConfig().getDecFormat().format(panel.getP())+"; ";
		}
		logger.debug(debug);
	}
    
	public void setPValues(double[] pvalues) {
		setPValues(ArrayUtils.toObject(pvalues));
	}

	public void setPValues(Double[] pvalues) {
		pValues = Arrays.asList(pvalues);
		restorePValues();
	}

	public void setTesting(boolean b) {
		Configuration.getInstance().getClassProperty(this.getClass(), totalAlpha.getText());
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
		refresh.setEnabled(!b);
		createMatrix.setEnabled(!b);
		
		jrbNoCorrelation.setEnabled(!b);
		jrbRCorrelation.setEnabled(!b);
	    jrbSimes.setEnabled(!b);

	    jcbCorObject.setEnabled(!b);
	    jbLoadPValues.setEnabled(!b);
	    if (!b) refresh(false);
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
    	//totalAlpha.setText("0.05");
    	totalAlpha.addKeyListener(this);
    	
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
	
}
