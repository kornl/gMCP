package org.af.gMCP.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.DataFramePanel;
import org.af.gMCP.gui.datatable.DataTableModel;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class MatrixCreationDialog extends JDialog implements ActionListener, ChangeListener {
	JButton ok = new JButton("Save matrix to R");

    CreateGraphGUI parent;
    Vector<Node> nodes;
    JTextArea jta = new JTextArea();
    DataFramePanel dfp;
    DataFramePanel dfpDiag;
    DataFramePanel dfpInterCor;
    DataFramePanel dfpIntraCor;
    JTextField tfname = new JTextField();
    
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
		tabbedPane.add("General", getSortPane());
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
		
		tfname.setText("corMat");
		
		panel.add(new JLabel("Save matrix as:"), cc.xy(2, row));
        panel.add(tfname, cc.xy(4, row));
        
        row +=2;
		
		return panel;
	}
	
	JSpinner spinnerN;
	JSpinner spinnerN2;
	JButton jbAdd;
	JLabel jlBlock = new JLabel();
	
	private JPanel getBlockPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		spinnerN = new JSpinner(new SpinnerNumberModel(2, 1, nodes.size(), 1));    	
    	spinnerN.addChangeListener(this);
    	
    	panel.add(new JLabel("Insert matrix of size:"), cc.xy(2, row));
        panel.add(spinnerN, cc.xy(4, row));
        
        row +=2;
        
        spinnerN2 = new JSpinner(new SpinnerNumberModel(1, 1, nodes.size()-1, 1));    	
    	spinnerN2.addChangeListener(this);
    	
    	panel.add(new JLabel("Insert matrix at position:"), cc.xy(2, row));
        panel.add(spinnerN2, cc.xy(4, row));        
    	
    	row +=2;
    	
    	panel.add(jlBlock, cc.xyw(2, row, 3));  
    	
    	row +=2;
    	
    	RDataFrameRef df = new RDataFrameRef();
		for (int i=0; i<2; i++) {
			df.addRowCol(nodes.get(i).getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfpDiag = new DataFramePanel(df);
		
		panel.add(new JScrollPane(dfpDiag), cc.xyw(2, row, 3));
		
        row +=2;
        
        jbAdd = new JButton("Add matrix on diagonal");
        jbAdd.addActionListener(this);
        panel.add(jbAdd, cc.xyw(2, row, 3));
		
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
		if (e.getSource()==ok) {
			String name = RControl.getR().eval("make.names(\""+tfname.getText()+"\")").asRChar().getData()[0];
			RControl.getR().eval(name+" <- "+dfp.getTable().getModel().getDataFrame().getRMatrix());
			dispose();
		} else if (e.getSource()==jbAdd) {
			int k = Integer.parseInt(spinnerN2.getModel().getValue().toString());
			int n = Integer.parseInt(spinnerN.getModel().getValue().toString());
			DataTableModel m = dfpDiag.getTable().getModel();
			DataTableModel m2 = dfp.getTable().getModel();
			for (int i=0; i<n; i++) {
				for (int j=0; j<n; j++) {
					m2.setValueAt(m.getValueAt(i, j), i+k-1, j+k-1);
				}
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==spinnerN || e.getSource()==spinnerN2) {
			int n = Integer.parseInt(spinnerN.getModel().getValue().toString());
			int j = Integer.parseInt(spinnerN2.getModel().getValue().toString());
			DataTableModel m = dfpDiag.getTable().getModel();
			m.removeAll();
			if (n+j-1>nodes.size()) {
				JOptionPane.showMessageDialog(parent, "The selected values "+n+"+"+j+" exceed the number of nodes+1.", "Impossible parameter combination", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (int i=j-1; i<j-1+n; i++) {
				m.addRowCol(nodes.get(i).getName());
				m.setValueAt(new EdgeWeight(1), m.getColumnCount()-1, m.getColumnCount()-1);
			}
		}
		
	}
	
}