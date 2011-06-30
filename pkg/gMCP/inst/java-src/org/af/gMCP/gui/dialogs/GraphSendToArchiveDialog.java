package org.af.gMCP.gui.dialogs;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.MenuBarMGraph;
import org.af.gMCP.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GraphSendToArchiveDialog extends JDialog implements ActionListener {
	JButton ok = new JButton("Load");

    CreateGraphGUI parent;    
    JList jlMatrices;
    JList jlGraphs;
    String[] matrices;
    String[] graphs;
    JTextArea jtInfo = new JTextArea(12, 40);;
    
	public GraphSendToArchiveDialog(CreateGraphGUI parent) {
		super(parent, "Select an R object to load", true);
		this.parent = parent;
		jtInfo.setEditable(false);
		jtInfo.setFont(new Font("Monospaced", Font.PLAIN, 10));
		
		matrices = RControl.getR().eval("gMCP:::getAllQuadraticMatrices()").asRChar().getData();
		graphs = RControl.getR().eval("gMCP:::getAllGraphs()").asRChar().getData();		
				
		jlMatrices = new JList(matrices);

		jlMatrices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlGraphs = new JList(graphs);

		jlGraphs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		if (matrices.length==1 && matrices[0].equals("No quadratic matrices found.")) {
			jlMatrices.setEnabled(false);
		}

		if (graphs.length==1 && graphs[0].equals("No graphMCP objects found.")) {
			jlGraphs.setEnabled(false);
		}

		if (!jlGraphs.isEnabled() && !jlMatrices.isEnabled()) {
			JOptionPane.showMessageDialog(this, "No applicable R objects (quadratic matrices or graphMCP objects) found.", "No applicable R objects found.", JOptionPane.INFORMATION_MESSAGE);
			return;
		}		
		
        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;

        getContentPane().add(new JLabel("Graph objects"), cc.xy(2, row));
        getContentPane().add(new JLabel("Quadratic matrices"), cc.xy(4, row));
        getContentPane().add(new JLabel("Object info"), cc.xy(6, row));

        row += 2;
        
        getContentPane().add(new JScrollPane(jlGraphs), cc.xy(2, row));
        getContentPane().add(new JScrollPane(jlMatrices), cc.xy(4, row));
        getContentPane().add(new JScrollPane(jtInfo), cc.xy(6, row));

        row += 2;
                        
        getContentPane().add(ok, cc.xy(4, row));
        ok.addActionListener(this);        

        pack();
        setSize(760,500);
		setLocationRelativeTo(parent);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (jlMatrices.getSelectedIndex() == -1 && jlGraphs.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "Please select an R object for loading from one of the lists.", "Please select an object.", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		String object;
		if (jlMatrices.getSelectedIndex() != -1) {
			object = jlMatrices.getSelectedValue().toString();
		} else {
			object = jlGraphs.getSelectedValue().toString();
		}   
		((MenuBarMGraph)parent.getJMenuBar()).loadGraph(object);
		Configuration.getInstance().getGeneralConfig().addGraph("R Object: "+object);
		dispose();
	}

}