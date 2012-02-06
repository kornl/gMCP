package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
    List<JTextField> jtl;
    JTextArea jta = new JTextArea();
    JPanel panel = new JPanel();
    DataFramePanel dfp;
    JTextField jtUserDefined = new JTextField();
    DefaultListModel listModel;
    JList listUserDefined;
    
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
				"Hit return to add further user defined power functions.");
				
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
                
        
        ok.addActionListener(this);        
        
        cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        
        getContentPane().setLayout(new FormLayout(cols, rows));
        cc = new CellConstraints();
		
		row = 2;
		
		getContentPane().add(new JLabel("Mean of multivariate normal distribution"), cc.xy(2, row));
        
		getContentPane().add(new JLabel("Covariance matrix"), cc.xy(4, row));
		
		row +=2;
		
		getContentPane().add(new JScrollPane(panel), cc.xy(2, row));
        
		getContentPane().add(new JScrollPane(dfp), cc.xy(4, row));
		
		row +=2;
		
		listModel = new DefaultListModel();
		listUserDefined = new JList(listModel);
		
		getContentPane().add(new JScrollPane(jta), cc.xy(2, row));
	
		getContentPane().add(new JScrollPane(listUserDefined), cc.xy(4, row));
	
		row +=2;
		
		getContentPane().add(jtUserDefined, cc.xy(4, row));
		jtUserDefined.addActionListener(this);
		
		row +=2;
		
		getContentPane().add(ok, cc.xy(4, row));
		
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (jtUserDefined.getText().length()>0) {
			listModel.insertElementAt(jtUserDefined.getText(), 0);
			//listUserDefined.ensureIndexIsVisible(0);
			jtUserDefined.setText("");
		}
		if (e.getSource()==jtUserDefined) {
			return;
		}
		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha = parent.getPView().getTotalAlpha();
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		for (int i=0; i<means.length; i++) {
			means[i] = Double.parseDouble(jtl.get(i).getText());
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