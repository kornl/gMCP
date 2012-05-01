package org.af.gMCP.gui.dialogs;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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

        c.fill = GridBagConstraints.BOTH;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		tPanel.addTab("Single Setting", getSingleSettingPanel());
		tPanel.addTab("Multiple Setting", getMultiSettingPanel());
		tPanel.addTab("User defined power function", getUserDefinedFunctions());
		
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
		
		RDataFrameRef df = new RDataFrameRef();
		for (Node n: parent.getGraphView().getNL().getNodes()) {
			df.addRowCol(n.getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfp = new DataFramePanel(df);
		dfp.getTable().getModel().diagEditable = true;
		dfp.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, dfp.getTable()));
				
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
        
		mPanel.add(new JLabel("Covariance matrix"), cc.xy(4, row));
		
		row +=2;
		
		panel.setLayout(new GridBagLayout());	
		panel.add(singleNCP, c);
		
		mPanel.add(new JScrollPane(panel), cc.xy(2, row));
        
		mPanel.add(new JScrollPane(dfp), cc.xy(4, row));
		
		row +=2;
		
		mPanel.add(switchNCP, cc.xy(2, row));
		switchNCP.addActionListener(this);
		
		mPanel.add(ok, cc.xy(4, row));
		ok.addActionListener(this);
		
		return mPanel;
	}
	
	public JPanel getMultiSettingPanel() {
		JPanel mPanel = new JPanel();
		
		JTabbedPane parameters = new JTabbedPane();
		
		parameters.addTab("Mean µ", new ParameterPanel(0d, nodes, parent));
		parameters.addTab("Standard deviation σ", new ParameterPanel(1d, nodes, parent));
		parameters.addTab("Sample size n", new ParameterPanel(10d, nodes, parent));
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        mPanel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		mPanel.add(parameters, cc.xy(2, row));
        
		//mPanel.add(new JScrollPane(dfp), cc.xy(4, row));
		
		row +=2;
		
		//mPanel.add(ok, cc.xy(4, row));
		//ok.addActionListener(this);		
		
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

	public void actionPerformed(ActionEvent e) {
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
		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha = parent.getPView().getTotalAlpha();
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		for (int i=0; i<means.length; i++) {
			if (ncp) {
				means[i] = Double.parseDouble(jtl.get(i).getText());
			} else {
				means[i] = Double.parseDouble(jtlMu.get(i).getText())*Math.sqrt(Double.parseDouble(jtlN.get(i).getText()))/Double.parseDouble(jtlSigma.get(i).getText());
			}
		}
		String userDefinedF = getUserDefined();
		String mean = RControl.getRString(means);
		RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");
		RControl.getR().eval(".powerResult <- calcPower(weights="+weights+", alpha="+alpha+", G="+G+", mean="+mean
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
		dispose();
	}

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