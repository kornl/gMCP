package org.mutoss.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class RObjectLoadingDialog extends JDialog implements ActionListener, ListSelectionListener {
	JButton ok = new JButton("Load");

    CreateGraphGUI parent;
    JTextField jtGraphName;
    JList jlMatrices;
    JList jlGraphs;
    String[] matrices;
    String[] graphs;
    
	public RObjectLoadingDialog(CreateGraphGUI parent) {
		super(parent, "Variables", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		
		matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
		graphs = RControl.getR().eval("gMCP:::getAllGraphs()").asRChar().getData();
				
		jlMatrices = new JList(matrices);
		jlMatrices.addListSelectionListener(this);
		jlMatrices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlGraphs = new JList(graphs);
		jlGraphs.addListSelectionListener(this);
		jlGraphs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;

        getContentPane().add(new JScrollPane(jlMatrices), cc.xy(2, row));
        getContentPane().add(new JScrollPane(jlGraphs), cc.xy(4, row));

        row += 2;
                        
        getContentPane().add(ok, cc.xy(4, row));
        ok.addActionListener(this);        
        
        actionPerformed(null);
        
        pack();
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		dispose();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource()==jlMatrices && jlMatrices.getSelectedIndex() != -1) {
			jlGraphs.removeSelectionInterval(0, graphs.length-1);			
		} else if (e.getSource()==jlGraphs && jlGraphs.getSelectedIndex() != -1) {
			jlMatrices.removeSelectionInterval(0, matrices.length-1);
		}
		
	}	
	
}