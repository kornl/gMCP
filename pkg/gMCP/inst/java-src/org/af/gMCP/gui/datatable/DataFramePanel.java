package org.af.gMCP.gui.datatable;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.GraphView;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DataFramePanel extends JTabbedPane {
    private Vector<DataTable> tables = new Vector<DataTable>();
    private JScrollPane scrollPane;
    GraphView control;

    public DataFramePanel(RDataFrameRef dfRefW) {
    	tables.add(new DataTable(dfRefW));
    	this.addTab("Adjacency Matrix", getPanel(tables.get(0)));
    }
    
    private JPanel getPanel(DataTable table) {
    	JPanel panel = new JPanel();
    	/*
    	 * if AutoReziseMode is set to something different to JTable.AUTO_RESIZE_OFF
    	 * the table will resize itself to fit into the width of the JScrollPane
    	 */
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	JTable rowHeader = new JTable(new RowModel(table.getModel()));
		rowHeader.setRowHeight(table.getRowHeight());
        scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeader);
        rowHeader.setPreferredScrollableViewportSize(rowHeader.getPreferredSize());
        
        String cols = "fill:pref:grow";
		String rows = "fill:pref:grow";

		FormLayout layout = new FormLayout(cols, rows);
		panel.setLayout(layout);
		CellConstraints cc = new CellConstraints();
        
		panel.add(scrollPane, cc.xy(1, 1)); 
        return panel;
    }
    
    public Vector<DataTable> getTable() {
        return tables;
    }

	public void addLayer() {
		RDataFrameRef dfRefW = new RDataFrameRef();
		DataTable dt = new DataTable(dfRefW);
		dt.setDefaultEditor(EdgeWeight.class, new CellEditorE(control, dt, tables.size()));
		for (String s : tables.get(0).getNames()) {
			dt.getModel().addRowCol(s);
		}
		tables.add(dt);
		this.addTab("Entangled graph layer "+tables.size(), getPanel(dt));
	}

	public void renameNode(int i, String name) {
		for (DataTable dt : getTable()) {
			dt.renameNode(i, name);
		}
	}

	public void registerControl(GraphView control) {
		this.control = control;
		for (DataTable dt : getTable()) {
			dt.setDefaultEditor(EdgeWeight.class, new CellEditorE(control, dt, 0));
		}
	}

	public void setTesting(boolean b) {
		for (DataTable dt : getTable()) {
			dt.setTesting(b);
		}		
	}

	public void delRowCol(int node) {
		for (DataTable dt : getTable()) {
			dt.getModel().delRowCol(node);
		}
	}

	public void addRowCol(String name) {
		for (DataTable dt : getTable()) {
			dt.getModel().addRowCol(name);
		}
	}

	public void setValueAt(EdgeWeight value, int row, int col, int layer) {
		getTable().get(layer).getModel().setValueAt(value, row, col);		
	}
}
