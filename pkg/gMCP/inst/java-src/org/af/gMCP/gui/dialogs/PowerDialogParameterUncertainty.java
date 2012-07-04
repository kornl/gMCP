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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.CellEditorE;
import org.af.gMCP.gui.datatable.DataFramePanel;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerDialogParameterUncertainty extends JDialog implements ActionListener {
	JButton ok = new JButton("Ok");

    CreateGraphGUI parent;
    Vector<Node> nodes;
    List<JTextField> jtl, jtlMu, jtlN, jtlSigma;
    List<JTextField> jtlVar = new Vector<JTextField>();
    JTextArea jta = new JTextArea();
    DataFramePanel dfp;
    JTextField jtUserDefined = new JTextField();
    DefaultListModel listModel;
    JList listUserDefined;
    JTabbedPane tPanel = new JTabbedPane();
    
    GridBagConstraints c = new GridBagConstraints();
    
    List<JButton> buttons = new Vector<JButton>();
    List<JButton> buttons2 = new Vector<JButton>();
    
	public PowerDialogParameterUncertainty(CreateGraphGUI parent) {
		super(parent, "Power Simulation - specify probability distribution of test statistics", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		nodes = parent.getGraphView().getNL().getNodes();
		
		if (parent.getPView().jrbRCorrelation.isSelected()) {
			//TODO Set df to this object:
			parent.getPView().jcbCorObject.getSelectedItem();
		}
		parent.getPView().getParameters();

        c.fill = GridBagConstraints.BOTH;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		tPanel.addTab("Single NCP Setting", getSingleSettingPanel());
		tPanel.addTab("Multiple NCP Settings", getMultiSettingPanel());
		tPanel.addTab("Covariance Matrix", getCVPanel());
		tPanel.addTab("User defined power function", getUserDefinedFunctions());
		Set<String> variables = parent.getGraphView().getNL().getAllVariables();
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}
		if (variables.size()>0) {
			tPanel.addTab("Variables", getVariablePanel(variables));
		}
		
		getContentPane().add(tPanel);
		
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
	}
	
	/*
	  "Above you can specify the noncentrality parameter and covariance matrix of a multivariate\n" +
				"normal distribution that is used for power calculations.\n" +
				"\n" +
	 */
	
	JButton switchNCP = new JButton("Enter µ, σ and n instead of ncp");
	boolean ncp = true;
	JPanel singleNCP = new JPanel();
	JPanel singleMuSigmaN = new JPanel();
	JPanel panel = new JPanel();
	
	public JPanel getSingleSettingPanel() {
		JPanel mPanel = new JPanel();
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        String cols2 = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu";
        
        for (Node n : nodes) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        singleNCP.setLayout(layout);
        
        layout = new FormLayout(cols2, rows);
        singleMuSigmaN.setLayout(layout);
        
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        singleMuSigmaN.add(new JLabel("µ"), cc.xy(4, row));
        
        singleMuSigmaN.add(new JLabel("σ"), cc.xy(6, row));
        
        singleMuSigmaN.add(new JLabel("n"), cc.xy(8, row));
        
        jtl = new Vector<JTextField>();
        jtlMu = new Vector<JTextField>();
        jtlSigma = new Vector<JTextField>();
        jtlN = new Vector<JTextField>();
        
        for (Node n : nodes) {        	
        	JTextField jt = new JTextField("0");        	
        	singleNCP.add(new JLabel("Noncentrality parameter for '"+n.getName()+"':"), cc.xy(2, row));
        	singleNCP.add(jt, cc.xy(4, row));
        	jtl.add(jt);

        	row += 2;
        	
        	singleMuSigmaN.add(new JLabel("Parameters for '"+n.getName()+"':"), cc.xy(2, row));
        	
        	jt = new JTextField("0");        	
        	singleMuSigmaN.add(jt, cc.xy(4, row));
        	jtlMu.add(jt);
            
        	jt = new JTextField("1");
            singleMuSigmaN.add(jt, cc.xy(6, row));
            jtlSigma.add(jt);
            
            jt = new JTextField("10");
            singleMuSigmaN.add(jt, cc.xy(8, row));
            jtlN.add(jt);
        }
        
        cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        cc = new CellConstraints();
		
		row = 2;
		
		mPanel.add(new JLabel("Noncentrality parameter of multivariate normal distribution"), cc.xy(2, row));
		
		row +=2;
		
		panel.setLayout(new GridBagLayout());	
		panel.add(singleNCP, c);
		
		mPanel.add(new JScrollPane(panel), cc.xy(2, row));
        
		row +=2;
		
		mPanel.add(switchNCP, cc.xy(2, row));
		switchNCP.addActionListener(this);
		
		mPanel.add(ok, cc.xy(4, row));
		ok.addActionListener(this);
		
		return mPanel;
	}
	
	JButton createCV = new JButton("Advanced Matrix Creation");
	JButton loadCV = new JButton("Load Matrix from R");

	/**
	 * Constructs and returns the panel for the covariance matrix.
	 * @return the panel for the covariance matrix
	 */
	public JPanel getCVPanel() {
		JPanel mPanel = new JPanel();
		
		RDataFrameRef df = new RDataFrameRef();
		for (Node n: parent.getGraphView().getNL().getNodes()) {
			df.addRowCol(n.getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfp = new DataFramePanel(df);
		dfp.getTable().getModel().diagEditable = true;
		dfp.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, dfp.getTable()));
		dfp.getTable().getModel().setCheckRowSum(false);
		
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        cc = new CellConstraints();
		
		mPanel.add(new JLabel("Covariance matrix"), cc.xyw(2, row, 3));
		
		row +=2;
		
		panel.setLayout(new GridBagLayout());	
		panel.add(singleNCP, c);
		
		mPanel.add(new JScrollPane(dfp), cc.xyw(2, row, 3));
		
		row +=2;
		
		mPanel.add(loadCV, cc.xy(2, row));
		loadCV.addActionListener(this);
		
		mPanel.add(createCV, cc.xy(4, row));
		createCV.addActionListener(this);
		
		return mPanel;
	}
	
	JButton ok2 = new JButton("Ok");
	PowerParameterPanel pPanelMeans, pPanelSigmas, pPanelN; 
	
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
		
		row +=2;
		
		mPanel.add(ok2, cc.xy(4, row));
		ok2.addActionListener(this);		
		
		return mPanel;
	}

	JButton addAnother = new JButton("Add another power function");
	
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
				"Hit return to add another user defined power functions.");
		

        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		jtUserDefined.addActionListener(this);
		mPanel.add(jtUserDefined, cc.xy(2, row));
		
		addAnother.addActionListener(this);
		mPanel.add(addAnother, cc.xy(4, row));
		
		row +=2;
		
		listModel = new DefaultListModel();
		listUserDefined = new JList(listModel);
		
		mPanel.add(new JScrollPane(jta), cc.xy(2, row));
	
		mPanel.add(new JScrollPane(listUserDefined), cc.xy(4, row));
	
		row +=2;		
				
		mPanel.add(new JScrollPane(hypPanel), cc.xyw(2, row, 3));

		row +=2;
		
		mPanel.add(new JScrollPane(opPanel), cc.xyw(2, row, 3));

		row +=2;
		
		return mPanel;
	}
	
	Object[] variables;
	
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createCV) {			
			MatrixCreationDialog mcd = new MatrixCreationDialog(parent, dfp.getTable().getRMatrix(), MatrixCreationDialog.getNames(parent.getGraphView().getNL().getNodes()));
			dfp.getTable().getModel().copy(mcd.dfp.getTable().getModel()); 
			return;
		}
		if (e.getSource() == loadCV) {
			VariableNameDialog vnd = new VariableNameDialog(parent);
			double[] result = RControl.getR().eval("as.numeric("+vnd.getName()+")").asRNumeric().getData();
			int n = nodes.size();
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					dfp.getTable().getModel().setValueAt(new EdgeWeight(result[i*n+j]), i, j);
				}
			}
			return;
		}
		if (switchNCP == e.getSource()) {
			panel.removeAll();
			if (ncp) {
				switchNCP.setText("Enter ncp instead of µ, σ and n");
				ncp = false;				
				panel.add(singleMuSigmaN, c);
			} else {
				switchNCP.setText("Enter µ, σ and n instead of ncp");
				ncp = true;			
				panel.add(singleNCP, c);
			}			
			panel.revalidate();
			panel.repaint();
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
		double alpha = parent.getPView().getTotalAlpha();
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		String settings = null;
		String userDefinedF = getUserDefined();
		// TODO: Do we still need sometimes something as parse2numeric? I guess yes.
		//RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");

		if (e.getSource()==ok) { /** Single Setting */
			for (int i=0; i<means.length; i++) {
				if (ncp) {
					means[i] = Double.parseDouble(jtl.get(i).getText());
				} else {
					means[i] = Double.parseDouble(jtlMu.get(i).getText())*Math.sqrt(Double.parseDouble(jtlN.get(i).getText()))/Double.parseDouble(jtlSigma.get(i).getText());
				}
			}
			String mean = RControl.getRString(means);
			settings = ", mean="+mean;

			RControl.getR().eval(".powerResult <- calcPower(weights="+weights+", alpha="+alpha+", G="+G+settings
					+","+"sigma = " + dfp.getTable().getModel().getDataFrame().getRMatrix() //diag(length(mean)),corr = NULL,"+
					+userDefinedF
					+", nSim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
					+", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+")");
			double[] localPower = RControl.getR().eval(".powerResult$LocalPower").asRNumeric().getData();
			double expRejections = RControl.getR().eval(".powerResult$ExpRejections").asRNumeric().getData()[0];
			double powAtlst1 = RControl.getR().eval(".powerResult$PowAtlst1").asRNumeric().getData()[0];
			double rejectAll = RControl.getR().eval(".powerResult$RejectAll").asRNumeric().getData()[0];
			Double[] userDefined = new Double[listModel.getSize()];
			String[] functions = new String[listModel.getSize()];
			for (int i=0; i<listModel.getSize(); i++) {
				functions[i] = listModel.get(i).toString();
				userDefined[i] = RControl.getR().eval(".powerResult$userDefined"+i).asRNumeric().getData()[0];
			}
			parent.getGraphView().getNL().setPower(localPower, expRejections, powAtlst1, rejectAll, userDefined, functions);

		} else { /** Multiple Settings */
			settings = ", muL = " + pPanelMeans.getRList()
					+ ", sigmaL = " + pPanelSigmas.getRList()
					+ ", nL = " + pPanelN.getRList();
			String result = RControl.getR().eval("gMCP:::calcMultiPower(weights="+weights+", alpha="+alpha+", G="+G+settings
					+","+"sigma = " + dfp.getTable().getModel().getDataFrame().getRMatrix() //diag(length(mean)),corr = NULL,"+
					+userDefinedF
					+", nSim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
					+", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+getVariables()
					+")").asRChar().getData()[0];
			new TextFileViewer(parent, "Power results", result, true);
		}				
		dispose();
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
	
}