package org.af.gMCP.gui.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.CellEditorE;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.datatable.SingleDataFramePanel;
import org.af.gMCP.gui.dialogs.MatrixCreationDialog;
import org.af.gMCP.gui.dialogs.VariableNameDialog;
import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class CVPanel extends JPanel implements ActionListener {
	
	PowerDialog pd;
	CreateGraphGUI parent;
	Vector<Node> nodes;
    
	SingleDataFramePanel dfp;
    SingleDataFramePanel dfp2;
    
    JCheckBox secondCV = new JCheckBox("Use another correlation matrix of test statistics used by the parametric test (misspecified or contains NA values)");
    
    JButton loadCV = new JButton("Load Matrix from R");
    JButton createCV = new JButton("Advanced Matrix Creation");
    JButton loadCV2 = new JButton("Load Matrix from R");
    JButton createCV2 = new JButton("Advanced Matrix Creation");
	
	public CVPanel(PowerDialog pd) {
		this.pd = pd;
		parent = pd.parent;
		
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
				JOptionPane.showMessageDialog(pd, "Could not load matrix \""+name+"\":\n"+exc.getMessage(), "Could not load matrix", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Layout:
		
		CellConstraints cc = new CellConstraints();

		int row = 2;

		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
		if (parent.getPView().jrbRCorrelation.isSelected()) {
			rows += ", pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
		}

		setLayout(new FormLayout(cols, rows));
		
		add(new JLabel("Correlation matrix of test statistics for power simulations"), cc.xyw(2, row, 3));
		
		row +=2;
		
		add(new JScrollPane(dfp), cc.xyw(2, row, 3));
		
		row +=2;
		
		add(loadCV, cc.xy(2, row));
		loadCV.addActionListener(this);
		
		add(createCV, cc.xy(4, row));
		createCV.addActionListener(this);
		
		row +=2;
		
		if (parent.getPView().jrbRCorrelation.isSelected()) {

			add(secondCV, cc.xyw(2, row, 3));
			secondCV.addActionListener(this);

			row +=2;

			add(new JScrollPane(dfp2), cc.xyw(2, row, 3));

			row +=2;

			add(loadCV2, cc.xy(2, row));
			loadCV2.addActionListener(this);
			loadCV2.setEnabled(false);

			add(createCV2, cc.xy(4, row));
			createCV2.addActionListener(this);
			createCV2.setEnabled(false);

		}
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
			JOptionPane.showMessageDialog(pd, "Could not load matrix \""+name+"\":\n"+exc.getMessage(), "Could not load matrix", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	String getMatrixForParametricTest() {
		if (parent.getPView().jrbRCorrelation.isSelected()) {			
			SingleDataFramePanel df = secondCV.isSelected()?dfp2:dfp;			
			return ", cr="+df.getTable().getModel().getDataFrame().getRMatrix()+", test=\""+Configuration.getInstance().getGeneralConfig().getUpscale()+"\"";
		}
		return "";
	}
	
	public void actionPerformed(ActionEvent e) {
		
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
	}


	public String getSigma() {
		return dfp.getTable().getModel().getDataFrame().getRMatrix();
	}
	
	
	
}
