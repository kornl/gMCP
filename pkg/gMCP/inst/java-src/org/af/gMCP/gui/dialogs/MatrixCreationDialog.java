package org.af.gMCP.gui.dialogs;

import java.awt.Component;
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

public class MatrixCreationDialog extends JDialog implements ActionListener {
	JButton ok = new JButton("Save matrix to R");

    CreateGraphGUI parent;
    Vector<Node> nodes;
    JTextArea jta = new JTextArea();
    DataFramePanel dfp;
    DataFramePanel dfpDiag;
    DataFramePanel dfpInterCor;
    DataFramePanel dfpIntraCor;
    
    JTabbedPane tabbedPane = new JTabbedPane(); 
    
	public MatrixCreationDialog(CreateGraphGUI parent) {
		super(parent, "Specify correlation matrix", true);
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
		
		setUpTabbedPane();
		
		jta.setText("");

        ok.addActionListener(this);

        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        getContentPane().setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
		getContentPane().add(new JScrollPane(tabbedPane), cc.xy(2, row));
        
		getContentPane().add(new JScrollPane(dfp), cc.xy(4, row));
		
		row +=2;
		
		getContentPane().add(ok, cc.xy(4, row));
		
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
	}

	private void setUpTabbedPane() {
		tabbedPane.add("Sort Hypotheses", getSortPane());
		tabbedPane.add("Block Diagonal", getBlockPane());
		tabbedPane.add("Treatments and Endpoints", getTEPane());		
	}

	private JPanel getSortPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		
		
		return panel;
	}
	
	private JPanel getBlockPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		
		
		return panel;
	}
	
	private JPanel getTEPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		
		
		return panel;
	}

	public void actionPerformed(ActionEvent e) {		
		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha = parent.getPView().getTotalAlpha();
		String G = parent.getGraphView().getNL().getGraphName() + "@m";
		double[] means = new double[nodes.size()];
		RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");
		RControl.getR().eval("");
	
		dispose();
	}
	
}