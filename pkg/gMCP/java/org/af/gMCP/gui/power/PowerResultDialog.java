package org.af.gMCP.gui.power;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.jhlir.call.RDataFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerResultDialog extends JDialog implements ActionListener {
	
	public String NEW_CELL = "\t";
	public String NEW_LINE = "\n";    
	
	JButton jbCopy = new JButton("Copy to Clipboard");
	JButton jbOk = new JButton("Ok");
	Object[][] data;
	
	public PowerResultDialog(CreateGraphGUI parent, RDataFrame result) {
		super(parent, "Power Results");
		String[] colnames = result.getColNames();		
		//JTable jt = new JTable(result.getRowCount(), result.getColumnCount());
		data = new Object[result.getRowCount()][result.getColumnCount()];
		for (int i=0; i<result.getRowCount(); i++) {
			for (int j=0; j<result.getColumnCount(); j++) {
				data[i][j] = result.get(i, j);
			}
		}
		JTable jt = new JTable(data, colnames);
		
		String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        String rows = "5dlu, fill:200dlu:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
		JScrollPane jsp = new JScrollPane(jt);
		
		int row = 2;
		
		getContentPane().add(jsp, cc.xyw(row, 2, 3));
		
		row += 2;
		
		jbCopy.addActionListener(this);
		jbOk.addActionListener(this);
		getContentPane().add(jbCopy, cc.xy(row, 4));
		getContentPane().add(jbOk, cc.xy(row, 6));
		
		pack();
		//setSize(800,600);
		setLocationRelativeTo(parent);
		
		setVisible(true);
		
	}

	public void copyTableToClipboard() {
		StringBuffer s = new StringBuffer();
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[0].length; j++) {
				s.append(data[i][j].toString()+NEW_CELL);
			}
			s.append(NEW_LINE);
		}
		StringSelection stringTable  = new StringSelection(s.toString()); 
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringTable, stringTable);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jbOk) {
			dispose();
		}
		if (e.getSource()==jbCopy) {
			copyTableToClipboard();
		}
	}
	
	
}
