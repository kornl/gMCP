package org.af.gMCP.gui.power;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.datatable.RowModel;
import org.af.jhlir.call.RDataFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerResultDialog extends JDialog implements ActionListener {
	
	public String NEW_CELL = "\t";
	public String NEW_LINE = "\n";    
	
	JButton jbCopy = new JButton("Copy to Clipboard");
	JButton jbOk = new JButton("Ok");
	Object[][] data;
	String[] colnames;
	
	public PowerResultDialog(CreateGraphGUI parent, RDataFrame result, String[] colnames) {
		super(parent, "Power Results");
		this.colnames = colnames;		

		data = new Object[result.getRowCount()][result.getColumnCount()];
		for (int i=0; i<result.getRowCount(); i++) {
			for (int j=0; j<result.getColumnCount(); j++) {
				data[i][j] = result.get(i, j);
			}
		}
		JTable jt = new JTable(new DefaultTableModel(data, colnames));
		
		for (int i=0; i < jt.getColumnCount(); i++) {
			jt.getColumnModel().getColumn(i).setMinWidth(110);

		}   
		
		jt.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		
		String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        String rows = "5dlu, fill:200dlu:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
        
		JScrollPane jsp = new JScrollPane(jt);
		
		int row = 2;
		
		getContentPane().add(jsp, cc.xyw(2, row, 5));
		
		row += 2;
		
		jbCopy.addActionListener(this);
		jbOk.addActionListener(this);
		getContentPane().add(jbCopy, cc.xy(4, row));
		getContentPane().add(jbOk, cc.xy(6, row));
		
		pack();
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(screenSize.width-200, getHeight());
		setLocationRelativeTo(parent);
		
		setVisible(true);
		
	}

	public void copyTableToClipboard() {
		StringBuffer s = new StringBuffer();

		for (int j=0; j<colnames.length; j++) {
			s.append(colnames[j]+NEW_CELL);
		}
		s.append(NEW_LINE);

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
