package org.af.gMCP.gui.datatable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.af.gMCP.gui.graph.EdgeWeight;

public class DataFramePanel extends JPanel {
    private DataTable table;
    private JScrollPane scrollPane;

    public DataFramePanel(RDataFrameRef dfRefW) {
    	table = new DataTable(dfRefW);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
    	JTable rowHeader = new JTable(new RowModel(table.getModel()));
		rowHeader.setRowHeight(table.getRowHeight());
        scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeader);
        rowHeader.setPreferredScrollableViewportSize(rowHeader.getPreferredSize());
        getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(null, getTable()));
        add(scrollPane);        
    }
    
    public DataTable getTable() {
        return table;
    }
}
