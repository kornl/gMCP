package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.af.commons.widgets.buttons.HorizontalButtonPane;
import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.CellEditorE;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.datatable.SingleDataFramePanel;
import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.Node;
import org.jdesktop.swingworker.SwingWorker;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SampleSizeDialog extends JDialog implements ActionListener {

	JButton addAnother = new JButton("Add another power function");
    List<JButton> buttons = new Vector<JButton>();
    List<JButton> buttons2 = new Vector<JButton>();    

    SingleDataFramePanel dfp;
    SingleDataFramePanel dfp2;
    JTextArea jta = new JTextArea();
    List<JTextField> jtl, jtlMu, jtlN, jtlSigma;
    List<JTextField> jtlVar = new Vector<JTextField>();
    JTextField jtUserDefined = new JTextField();
    DefaultListModel listModel;
    
    JButton loadUDPF = new JButton("Load"); //"Load Power Functions");
    JButton saveUDPF = new JButton("Save");
    
    JList listUserDefined;
    JCheckBox secondCV = new JCheckBox("Use another correlation matrix of test statistics used by the parametric test (misspecified or contains NA values)");
    
    JButton loadCV = new JButton("Load Matrix from R");
    JButton createCV = new JButton("Advanced Matrix Creation");
    JButton loadCV2 = new JButton("Load Matrix from R");
    JButton createCV2 = new JButton("Advanced Matrix Creation");
    JButton clearList = new JButton("Clear");
    
    boolean ncp = true;
    Vector<Node> nodes;

	CreateGraphGUI parent;
	PowerParameterPanel pPanelMeans, pPanelSigmas, pPanelN;
	
	JPanel panelMany = new JPanel();
	
	JTabbedPane tPanel = new JTabbedPane();

	Object[] variables;
	
	public static GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		return c;
	}
	
	/**
	 * Constructor
	 * @param parent Parent JFrame
	 */
	public SampleSizeDialog(CreateGraphGUI parent) {
		super(parent, "Sample Size Calculations", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		nodes = parent.getGraphView().getNL().getNodes();
		
		RDataFrameRef df = new RDataFrameRef();
		RDataFrameRef df2 = new RDataFrameRef();
		for (Node n: nodes) {
			df.addRowCol(n.getName());
			df2.addRowCol(n.getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
			df2.setValue(df2.getColumnCount()-1, df2.getColumnCount()-1, new EdgeWeight(1));
		}		

		dfp = new SingleDataFramePanel(df);
		dfp.getTable().getModel().checkCorMat();
		dfp.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, dfp.getTable()));
		dfp.getTable().getModel().setCheckRowSum(false);
		
		dfp2 = new SingleDataFramePanel(df);
		dfp2.getTable().getModel().checkCorMat();
		dfp2.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, dfp.getTable()));
		dfp2.getTable().getModel().setCheckRowSum(false);
		dfp2.setEnabled(false);		
		
		if (parent.getPView().jrbRCorrelation.isSelected()) {
			try {
			String mat = parent.getPView().jcbCorObject.getSelectedItem().toString();
			load(dfp, mat);
			load(dfp2, mat);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (parent.getPView().jrbRCorrelation.isSelected()) {			
			String name = parent.getPView().jcbCorObject.getSelectedItem().toString();
			try {
				double[] result = RControl.getR().eval("as.numeric("+name+")").asRNumeric().getData();
				int n = nodes.size();
				for (int i=0; i<n; i++) {
					for (int j=0; j<n; j++) {
						dfp.getTable().getModel().setValueAt(new EdgeWeight(result[i*n+j]), i, j);
						dfp2.getTable().getModel().setValueAt(new EdgeWeight(result[i*n+j]), i, j);
					}
				}
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(this, "Could not load matrix \""+name+"\":\n"+exc.getMessage(), "Could not load matrix", JOptionPane.ERROR_MESSAGE);
			}
		}
		parent.getPView().getParameters();
		GridBagConstraints c = getDefaultGridBagConstraints();
		
		getContentPane().setLayout(new GridBagLayout());
		//getContentPane().add(numberOfSettings, c);
		
		tPanel.addTab("NCP Settings", getMultiSettingPanel());
		//tPanel.addTab("Multiple NCP Settings", getMultiSettingPanel());
		tPanel.addTab("Correlation Matrix", getCVPanel());
		tPanel.addTab("User defined power function", getUserDefinedFunctions());
		Set<String> variables = parent.getGraphView().getNL().getAllVariables();
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}
		if (variables.size()>0) {
			tPanel.addTab("Variables", getVariablePanel(variables));
		}
		
		getContentPane().add(tPanel, c);
		
		c.weighty=0; c.gridy++; c.weightx=0; c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		HorizontalButtonPane bp = new OkCancelButtonPane();
		getContentPane().add(bp, c);
		bp.addActionListener(this);		
		
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
	} 
	
	String rCommand = "";
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clearList) {
			listModel.removeAllElements();
			return;
		}		
		if (e.getSource() == secondCV) {
			dfp2.setEnabled(secondCV.isSelected());
			loadCV2.setEnabled(secondCV.isSelected());
			createCV2.setEnabled(secondCV.isSelected());
			return;
		}
		if (e.getSource() == createCV) {			
			MatrixCreationDialog mcd = new MatrixCreationDialog(parent, dfp.getTable().getRMatrix(), MatrixCreationDialog.getNames(parent.getGraphView().getNL().getNodes()));
			dfp.getTable().getModel().copy(mcd.dfp.getTable().getModel()); 
			return;
		}
		if (e.getSource() == createCV2) {			
			MatrixCreationDialog mcd = new MatrixCreationDialog(parent, dfp2.getTable().getRMatrix(), MatrixCreationDialog.getNames(parent.getGraphView().getNL().getNodes()));
			dfp2.getTable().getModel().copy(mcd.dfp.getTable().getModel()); 
			return;
		}		
		if (e.getSource() == loadCV) {
			load(dfp);
			return;
		}
		if (e.getSource() == loadCV2) {
			load(dfp2);
			return;
		}
		if (buttons.contains(e.getSource()) || buttons2.contains(e.getSource())) {
			jtUserDefined.setText(jtUserDefined.getText()+" "+((JButton)e.getSource()).getActionCommand());
			return;
		}
		if (jtUserDefined.getText().length()>0) {
			listModel.insertElementAt(jtUserDefined.getText(), 0);
			//listUserDefined.ensureIndexIsVisible(0);
			jtUserDefined.setText("");
		}
		if (e.getSource()==jtUserDefined || e.getSource()==addAnother) {
			return;
		}
		//Hashtable<String,Double> ht = getVariables();
		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha;
		try {
			alpha = parent.getPView().getTotalAlpha();
		} catch (Exception e1) {
			return;
		}
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		String settings = null;
		String userDefinedF = getUserDefined();
		// TODO: Do we still need sometimes something as parse2numeric? I guess yes.
		//RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");

		if (e.getActionCommand().equals(HorizontalButtonPane.OK_CMD)) {
			settings = ", muL = " + pPanelMeans.getRList()
					+ ", sigmaL = " + pPanelSigmas.getRList()
					+ ", nL = " + pPanelN.getRList();

			rCommand = "gMCP:::calcMultiPower(weights="+weights+", alpha="+alpha+", G="+G+settings
					+","+"sigma = " + dfp.getTable().getModel().getDataFrame().getRMatrix() //diag(length(mean)),corr = NULL,"+
					+getMatrixForParametricTest()
					+userDefinedF
					+", nSim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
					+", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+getVariables()
					+")";				

			parent.glassPane.start();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {					
					try {
						String result = RControl.getR().eval(rCommand).asRChar().getData()[0];
						new TextFileViewer(parent, "Power results", result, true);
					} catch (Exception e) {
						String message = e.getMessage();
						JOptionPane.showMessageDialog(parent, "R call produced an error:\n\n"+message+"\nWe will open a window with R code to reproduce this error for investigation.", "Error in R Call", JOptionPane.ERROR_MESSAGE);
						JDialog d = new JDialog(parent, "R Error", true);
						d.add(
								new TextFileViewer(parent, "R Objects", "The following R code produced the following error:\n\n" +message+
										rCommand, true)
								);
						d.pack();
						d.setSize(800, 600);
						d.setVisible(true);
						e.printStackTrace();						
					} finally {
						parent.glassPane.stop();
					}
					return null;
				}					 
			};
			worker.execute();				
		}
		dispose();
	}

	private void load(SingleDataFramePanel dfp) {
		VariableNameDialog vnd = new VariableNameDialog(parent);
		load(dfp, vnd.getName());		
	}

	private void load(SingleDataFramePanel dfp3, String name) {
		try {
			double[] result = RControl.getR().eval("as.numeric("+name+")").asRNumeric().getData();
			int n = nodes.size();
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					dfp.getTable().getModel().setValueAt(new EdgeWeight(result[i*n+j]), i, j);
				}
			}
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(this, "Could not load matrix \""+name+"\":\n"+exc.getMessage(), "Could not load matrix", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Constructs and returns the panel for the correlation matrix.
	 * @return the panel for the correlation matrix
	 */
	public JPanel getCVPanel() {
		JPanel mPanel = new JPanel();
		GridBagConstraints c = getDefaultGridBagConstraints();
		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
		if (parent.getPView().jrbRCorrelation.isSelected()) {
			rows += ", pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
		}

		mPanel.setLayout(new FormLayout(cols, rows));
		cc = new CellConstraints();
		
		mPanel.add(new JLabel("Correlation matrix of test statistics for power simulations"), cc.xyw(2, row, 3));
		
		row +=2;
		
		mPanel.add(new JScrollPane(dfp), cc.xyw(2, row, 3));
		
		row +=2;
		
		mPanel.add(loadCV, cc.xy(2, row));
		loadCV.addActionListener(this);
		
		mPanel.add(createCV, cc.xy(4, row));
		createCV.addActionListener(this);
		
		row +=2;
		
		if (parent.getPView().jrbRCorrelation.isSelected()) {

			mPanel.add(secondCV, cc.xyw(2, row, 3));
			secondCV.addActionListener(this);

			row +=2;

			mPanel.add(new JScrollPane(dfp2), cc.xyw(2, row, 3));

			row +=2;

			mPanel.add(loadCV2, cc.xy(2, row));
			loadCV2.addActionListener(this);
			loadCV2.setEnabled(false);

			mPanel.add(createCV2, cc.xy(4, row));
			createCV2.addActionListener(this);
			createCV2.setEnabled(false);

		}
		return mPanel;
	}
	
	private String getMatrixForParametricTest() {
		if (parent.getPView().jrbRCorrelation.isSelected()) {			
			SingleDataFramePanel df = secondCV.isSelected()?dfp2:dfp;			
			return ", cr="+df.getTable().getModel().getDataFrame().getRMatrix()+", test=\""+Configuration.getInstance().getGeneralConfig().getParametricTest()+"\"";
		}
		return "";
	}
	
	public JPanel getMultiSettingPanel() {
		JPanel mPanel = new JPanel();
		
		JTabbedPane parameters = new JTabbedPane();
		
		pPanelMeans = new PowerParameterPanel("mean", 0d, nodes, parent);
		pPanelSigmas = new PowerParameterPanel("sd", 1d, nodes, parent);
		pPanelN = new PowerParameterPanel("sample size", 10d, nodes, parent);
		parameters.addTab("Mean µ", pPanelMeans);
		parameters.addTab("Standard deviation σ", pPanelSigmas);
		parameters.addTab("Sample size n", pPanelN);
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		mPanel.add(parameters, cc.xy(2, row));
        
		//mPanel.add(new JScrollPane(dfp), cc.xy(4, row));
		
		return mPanel;
	}
	
	/**
	 * Constructs String that contains the parameter f for user defined
	 * functions used by calcPower and extractPower
	 * @return String that contains the parameter f for user defined
	 * functions used by calcPower and extractPower. Either empty or
	 * of the form ", f=list(...)".
	 */
	private String getUserDefined() {
		if (listModel.getSize()==0) return "";
		String s = ", f=list(";
		for (int i=0; i<listModel.getSize(); i++) {
			s +="userDefined"+i+"=function(x) {"+listModel.get(i)+"}";
			if (i!=listModel.getSize()-1) s+= ",";
		}		
		return s + ")";
	}

	public JPanel getUserDefinedFunctions() {
		JPanel mPanel = new JPanel();
		
		JButton b = new JButton("(");
		b.setActionCommand("(");
		buttons.add(b);

		b = new JButton(")");
		b.setActionCommand(")");
		buttons.add(b);
		
		b = new JButton("AND");
		b.setActionCommand("&&");
		buttons.add(b);
		
		b = new JButton("OR");
		b.setActionCommand("||");
		buttons.add(b);
		
		b = new JButton("NOT");
		b.setActionCommand("!");		
		buttons.add(b);		
		
		for (int i=0; i<nodes.size(); i++) {
			b = new JButton(nodes.get(i).getName());
			b.setActionCommand("x["+(i+1)+"]");			
			buttons2.add(b);
		}
		
		JPanel hypPanel = new JPanel();
		for (JButton button : buttons2) {
			button.addActionListener(this);
			hypPanel.add(button);
		}
		
		JPanel opPanel = new JPanel();
		for (JButton button : buttons) {
			button.addActionListener(this);
			opPanel.add(button);
		}
		
		jta.setMargin(new Insets(4,4,4,4));
		jta.setText(
				"In the text field above you can enter an user defined power function.\n" +
				"Use the R syntax and \"x[i]\" to specify the proposition that hypothesis i\n"+
				"could be rejected. Alternatively use the buttons below.\n" +
				"Example:  (x[1] && x[2]) || x[4]\n" +
				"This calculates the probability that the first and second\n" +
				"or (not exclusive) the fourth null hypothesis can be rejected.\n"+
				/*"- if the test statistic follows a t-distribution, enter the non-centrality parameter µ*sqrt(n)/σ\n"+
				"  (µ=difference of real mean and mean under null hypothesis, n=sample size, σ=standard deviation)\n"+
				"- triangle(min, peak, max)\n"+
				"- rnorm(1, mean=0.5, sd=1)\n"+*/
				"Note that you can use all R commands, for example also\n"+
				"any(x) to see whether any hypotheses was rejected or\n" +
				"all(x[1:4]) to see whether all of the first four hypotheses were rejected.\n"+
				"Hit return to add another power function.");
		

        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		jtUserDefined.addActionListener(this);
		mPanel.add(jtUserDefined, cc.xy(2, row));
		
		addAnother.addActionListener(this);
		mPanel.add(addAnother, cc.xy(4, row));
		
		/*clearList.addActionListener(this);
		mPanel.add(addAnother, cc.xy(4, row));*/		
		
		row +=2;
		
		listModel = new DefaultListModel();
		listUserDefined = new JList(listModel);
		
		mPanel.add(new JScrollPane(jta), cc.xywh(2, row, 1, 3));
	
		mPanel.add(new JScrollPane(listUserDefined), cc.xy(4, row));
	
		row +=2;
		
		clearList.addActionListener(this);
		mPanel.add(clearList, cc.xy(4, row));		
		//mPanel.add(saveUDPF, cc.xy(6, row));
		
		row +=2;		
				
		mPanel.add(new JScrollPane(hypPanel), cc.xyw(2, row, 3));

		row +=2;
		
		mPanel.add(new JScrollPane(opPanel), cc.xyw(2, row, 3));
		
		

		row +=2;
		
		return mPanel;
	}

	public JPanel getVariablePanel(Set<String> v) {
		JPanel vPanel = new JPanel();		
		variables = v.toArray();
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu";
        
        for (Object s : variables) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        vPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        jtlVar = new Vector<JTextField>();
        
        for (Object s : variables) {        	
        	JTextField jt = new JTextField("0");
        	if (s.equals("ε")) {
        		jt.setText(""+Configuration.getInstance().getGeneralConfig().getEpsilon());
        	} else {
        		jt.setText(""+Configuration.getInstance().getGeneralConfig().getVariable(s.toString()));
        	}
        	vPanel.add(new JLabel("Value for '"+s+"':"), cc.xy(2, row));
        	vPanel.add(jt, cc.xy(4, row));
        	jtlVar.add(jt);        	
        	
        	row += 2;
        }
        
        return vPanel;
	}

	public String getVariables() {
		if (jtlVar.size()>0) {
			String s = ", variables=list("; 
			for (int i=0; i<variables.length; i++) {
				s = s + EdgeWeight.UTF2LaTeX(variables[i].toString().charAt(0))+" = "+ jtlVar.get(i).getText();
				if (i!=variables.length-1) s = s + ", ";
			}		
			return s+")";
		} else {
			return "";
		}
	}	
}