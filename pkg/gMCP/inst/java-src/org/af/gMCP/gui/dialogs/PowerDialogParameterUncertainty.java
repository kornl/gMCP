package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    List<JTextField> jtl;
    JTextArea jta = new JTextArea();
    JPanel panel = new JPanel();
    DataFramePanel dfp;
    JTextField jtUserDefined = new JTextField();
    
	public PowerDialogParameterUncertainty(CreateGraphGUI parent) {
		super(parent, "Power Simulation - specify probability distribution of test statistics", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		nodes = parent.getGraphView().getNL().getNodes();
		
		RDataFrameRef df = new RDataFrameRef();
		for (Node n: parent.getGraphView().getNL().getNodes()) {
			df.addRowCol(n.getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfp = new DataFramePanel(df);
		dfp.getTable().getModel().diagEditable = true;
		dfp.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, dfp.getTable()));
		
		jta.setText("Above you can specify the mean and covariance matrix of a multivariate\n" +
				"normal distribution that is used for power calculations.\n" +
				"\n" +
				"In the text field on the right you can enter an user defined power function.\n" +
				"Example:  (x[1] && x[2]) || x[4]\n" +
				"This calculates the probability that the first and second\n" +
				"or (not exclusive) the fourth null hypothesis can be rejected."+
				/*"- if the test statistic follows a t-distribution, enter the non-centrality parameter µ*sqrt(n)/σ\n"+
				"  (µ=difference of real mean and mean under null hypothesis, n=sample size, σ=standard deviation)\n"+
				"- triangle(min, peak, max)\n"+
				"- rnorm(1, mean=0.5, sd=1)\n"+*/
				"");
				
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu";
        
        for (Node n : nodes) {
        	rows += ", pref, 5dlu";
        }
        
        FormLayout layout = new FormLayout(cols, rows);
        panel.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        jtl = new Vector<JTextField>();
        
        for (Node n : nodes) {        	
        	JTextField jt = new JTextField("0");        	
        	panel.add(new JLabel("Mean for '"+n.getName()+"':"), cc.xy(2, row));
        	panel.add(jt, cc.xy(4, row));
        	jtl.add(jt);

        	row += 2;
        }
                
        panel.add(ok, cc.xy(4, row));
        ok.addActionListener(this);        
        
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		getContentPane().setLayout(new GridBagLayout());
		
		getContentPane().add(new JScrollPane(panel), c);
        
		c.gridx++;
		
		getContentPane().add(new JScrollPane(dfp), c);
		
		c.gridx=0; c.gridy++;
		
		getContentPane().add(new JScrollPane(jta), c);
		
		c.gridx++;
		
		getContentPane().add(new JScrollPane(jtUserDefined), c);
		
        pack();
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha = parent.getPView().getTotalAlpha();
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		for (int i=0; i<means.length; i++) {
			means[i] = Double.parseDouble(jtl.get(i).getText());
		}
		String userDefinedF = jtUserDefined.getText().length()>1?", f=list(userDefined=function(x) {"+jtUserDefined.getText()+"})":"";
		String mean = RControl.getRString(means);
		RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");
		RControl.getR().eval(".powerResult <- calcPower(weights="+weights+", alpha="+alpha+", G="+G+", mean="+mean
                      +","+"sigma = " + dfp.getTable().getModel().getDataFrame().getRMatrix() //diag(length(mean)),corr = NULL,"+
                      +userDefinedF
                      //"nSim = 10000, seed = 4711, type = c(\"quasirandom\", \"pseudorandom\")"+
				+")");
		double[] localPower = RControl.getR().eval(".powerResult$LocalPower").asRNumeric().getData();
		double expRejections = RControl.getR().eval(".powerResult$ExpRejections").asRNumeric().getData()[0];
		double powAtlst1 = RControl.getR().eval(".powerResult$PowAtlst1").asRNumeric().getData()[0];
		double rejectAll = RControl.getR().eval(".powerResult$RejectAll").asRNumeric().getData()[0];
		Double userDefined = null;
		if (jtUserDefined.getText().length()>1) {
			userDefined = RControl.getR().eval(".powerResult$userDefined").asRNumeric().getData()[0];
		}
		parent.getGraphView().getNL().setPower(localPower, expRejections, powAtlst1, rejectAll, userDefined);
		dispose();
	}	
	
}