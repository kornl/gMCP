package org.af.gMCP.gui.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.DataFramePanel;
import org.af.gMCP.gui.datatable.DataTableModel;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.Node;
import org.af.jhlir.call.RChar;
import org.af.jhlir.call.RInteger;
import org.af.jhlir.call.RList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    JList hypotheses;
    JLabel warning = new JLabel();
    protected static Log logger = LogFactory.getLog(MatrixCreationDialog.class);
    
    JTabbedPane tabbedPane = new JTabbedPane(); 
    
	public MatrixCreationDialog(CreateGraphGUI parent) {
		super(parent, "Specify correlation matrix", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		nodes = parent.getGraphView().getNL().getNodes();
		
		RDataFrameRef df = new RDataFrameRef();
		for (Node n: nodes) {
			df.addRowCol(n.getName());
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfp = new DataFramePanel(df);
		dfp.getTable().getModel().diagEditable = true;		
		
		setUpTabbedPane();
		getPossibleCorrelations();
		
		jta.setText("");
		warning.setForeground(Color.RED);

        ok.addActionListener(this);

        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        getContentPane().setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
		getContentPane().add(tabbedPane, cc.xy(2, row));
        
		getContentPane().add(new JScrollPane(dfp), cc.xy(4, row));
		
		row +=2;
		
		getContentPane().add(warning, cc.xy(2, row));
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
	
	JButton reorder = new JButton("Apply reordering");

	private JPanel getSortPane() {
		JPanel panel = new JPanel();		
		
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
		
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		tfname.setText("corMat");
		
		panel.add(new JLabel("Save matrix as:"), cc.xy(2, row));
        panel.add(tfname, cc.xy(4, row));
        
        row +=2;
        
        panel.add(new JLabel("You can reorder the hypotheses by drag'n'drop:"), cc.xyw(2, row, 3));
        
        row +=2;
        
        DefaultListModel lm = new DefaultListModel();
        for (Node n: nodes) {
			lm.addElement(n);
		}
        
		try {
        	Class cls = Class.forName("org.af.commons.widgets.JListDnD");
        	Constructor ct = cls.getConstructor(new Class[] {ListModel.class});
        	hypotheses = (JList) ct.newInstance(lm);        	
        	panel.add(new JScrollPane(hypotheses), cc.xyw(2, row, 3));
        } catch (Exception e) {
        	// Java 5 will throw an exception.
        	// In this case we set hypotheses to an ordinary JList.
        	logger.warn(e);        
            hypotheses = new JList(lm);
            panel.add(new JScrollPane(hypotheses), cc.xyw(2, row, 3));
            row +=2;
            panel.add(new JLabel("Reordering does currently not work for you due to Java 5"), cc.xyw(2, row, 3));
        }
        
        row +=2;
        
        reorder.addActionListener(this);
        panel.add(reorder, cc.xy(4, row));
        		
		return panel;
	}
	
	JSpinner spinnerN;
	JSpinner spinnerN2;
	JButton jbAdd;
	JLabel jlBlock = new JLabel();
	
	private JPanel getBlockPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
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
    	
		row += 2;
		
		panel.add(new JLabel("Use standard design:"), cc.xy(2, row));
        panel.add(jcbCorString2, cc.xy(4, row));
        jcbCorString2.addActionListener(this);
        
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
	
	JButton applyTE = new JButton("Calculate overall correlation");
	
	private JPanel getTEPane() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		panel.add(getTEPane1(), cc.xyw(2, row, 3));
		
		row += 2;
		
		panel.add(getTEPane2(), cc.xyw(2, row, 3));
		
		row += 2;
		
		panel.add(applyTE, cc.xyw(2, row, 3));
		
		return panel;
	}
	
	JSpinner spinnerNT;
	JSpinner spinnerNE;	
	
	private JPanel getTEPane1() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		spinnerNT = new JSpinner(new SpinnerNumberModel(Math.max(2,nodes.size()/2), 1, nodes.size(), 1));    	
    	spinnerNT.addChangeListener(this);
    	
    	panel.add(new JLabel("Number of Treatment Comparisons:"), cc.xy(2, row));
        panel.add(spinnerNT, cc.xy(4, row));
		
		row += 2;
		
		panel.add(new JLabel("Use standard design:"), cc.xy(2, row));
        panel.add(jcbCorString, cc.xy(4, row));
        jcbCorString.addActionListener(this);
		
		row += 2;
		
		RDataFrameRef df = new RDataFrameRef();
		for (int i=0; i<Math.max(2,nodes.size()/2); i++) {
			df.addRowCol("T"+(i+1));
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfpIntraCor = new DataFramePanel(df);
		
		panel.add(new JScrollPane(dfpIntraCor), cc.xyw(2, row, 3));
		
		TitledBorder title = BorderFactory.createTitledBorder("Treatment correlation.");
		panel.setBorder(title);	
		
		return panel;
	}
	
	private JPanel getTEPane2() {
		JPanel panel = new JPanel();
		String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		spinnerNE = new JSpinner(new SpinnerNumberModel(Math.max(2,nodes.size()/2), 1, nodes.size(), 1));    	
    	spinnerNE.addChangeListener(this);
    	
    	panel.add(new JLabel("Number of Endpoints:"), cc.xy(2, row));
        panel.add(spinnerNE, cc.xy(4, row));
		
		row += 2;
		
		RDataFrameRef df = new RDataFrameRef();
		for (int i=0; i<Math.max(2,nodes.size()/2); i++) {
			df.addRowCol("E"+(i+1));
			df.setValue(df.getColumnCount()-1, df.getColumnCount()-1, new EdgeWeight(1));
		}		
		dfpInterCor = new DataFramePanel(df);
		
		panel.add(new JScrollPane(dfpInterCor), cc.xyw(2, row, 3));
		
		TitledBorder title = BorderFactory.createTitledBorder("Correlation between endpoints");
		panel.setBorder(title);	
		
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
		} else if (e.getSource()==reorder) {
			
		} else if (e.getSource()==applyTE) {
			
		} else if (e.getSource()==jcbCorString2) {
			if (jcbCorString2.getSelectedItem()==null || jcbCorString2.getSelectedItem().toString().equals(NO_SD)) return;
			DataTableModel m = dfpDiag.getTable().getModel();
			int n = Integer.parseInt(spinnerN.getModel().getValue().toString());
			String s = jcbCorString2.getSelectedItem().toString();
			setMatrix(m, s, n);
		} else if (e.getSource()==jcbCorString) {	
			if (jcbCorString.getSelectedItem()==null || jcbCorString.getSelectedItem().toString().equals(NO_SD)) return;
			DataTableModel m = dfpIntraCor.getTable().getModel();
			int n = Integer.parseInt(spinnerNT.getModel().getValue().toString());
			String s = jcbCorString.getSelectedItem().toString();
			setMatrix(m, s, n);
		}
		warning.setText(RControl.getR().eval("gMCP:::checkPSD("+dfp.getTable().getRMatrix()+")").asRChar().getData()[0]);
	}

	private void setMatrix(DataTableModel m, String s, int n) {
		String design = s.substring(0, s.indexOf(" "));
		String groups = s.substring(s.indexOf("(")+1, s.indexOf("groups")-1);				
		GroupDialog gd = new GroupDialog(parent, Integer.parseInt(groups));
		String command = "gMCP:::getCorrMat(n="+gd.getGroups()+", type =\""+ design+"\")";
		double[] m2 = RControl.getR().eval(command).asRNumeric().getData();
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				m.setValueAt(new EdgeWeight(m2[i*n+j]), i, j);
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
			getPossibleCorrelations();
		} else if (e.getSource()==spinnerNT) {			
			int n = Integer.parseInt(spinnerNT.getModel().getValue().toString());
			DataTableModel m = dfpIntraCor.getTable().getModel();
			m.removeAll();
			for (int i=0; i<n; i++) {
				m.addRowCol("T"+(i+1));
				m.setValueAt(new EdgeWeight(1), m.getColumnCount()-1, m.getColumnCount()-1);
			}
			getPossibleCorrelations();
		} else if (e.getSource()==spinnerNE) {
			int n = Integer.parseInt(spinnerNE.getModel().getValue().toString());
			DataTableModel m = dfpInterCor.getTable().getModel();
			m.removeAll();
			for (int i=0; i<n; i++) {
				m.addRowCol("E"+(i+1));
				m.setValueAt(new EdgeWeight(1), m.getColumnCount()-1, m.getColumnCount()-1);
			}
			getPossibleCorrelations();
		}
		
	}
	
	protected JComboBox jcbCorString = new JComboBox(new String[] {NO_SD});
	protected JComboBox jcbCorString2 = new JComboBox(new String[] {NO_SD});
	
	/* Note: the following string must have a certain length, otherwise 
	 * e.g. "UmbrellaWilliams (3 groups)" will cause layout problems. 
	 */
	final static String NO_SD = "User defined design (edit the matrix)";
	
	private void getPossibleCorrelations() {
		jcbCorString.removeAllItems();
		jcbCorString2.removeAllItems();
		int n = Integer.parseInt(spinnerNT.getModel().getValue().toString());
		int n2 = Integer.parseInt(spinnerN.getModel().getValue().toString());
		if (n!=0) {
			RList list = RControl.getR().eval("gMCP:::getAvailableStandardDesigns("+n+")").asRList();
			RChar designs = list.get(0).asRChar();
			RInteger groups = list.get(1).asRInteger();
			jcbCorString.addItem(NO_SD);
			for (int i=0; i<designs.getLength(); i++) {
				jcbCorString.addItem(designs.getData()[i] + " ("+ groups.getData()[i]+" groups)"); 
			}		
		}
		if (n2!=0) {
			RList list = RControl.getR().eval("gMCP:::getAvailableStandardDesigns("+n2+")").asRList();
			RChar designs = list.get(0).asRChar();
			RInteger groups = list.get(1).asRInteger();
			jcbCorString2.addItem(NO_SD);
			for (int i=0; i<designs.getLength(); i++) {
				jcbCorString2.addItem(designs.getData()[i] + " ("+ groups.getData()[i]+" groups)"); 
			}		
		}
	}
	
}