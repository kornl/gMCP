package org.mutoss.gui.datatable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DataFramePanel extends JPanel {
    private DataTable table;
    private JScrollPane scrollPane;

    public DataFramePanel(RDataFrameRef dfRefW, boolean doLayout, boolean Sortable) {
    	table = new DataTable(dfRefW);
    	JTable rowHeader = new JTable(new RowModel(table.getModel()));
		rowHeader.setRowHeight(table.getRowHeight());
        scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeader);
        rowHeader.setPreferredScrollableViewportSize(rowHeader.getPreferredSize());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //table.setSortable(false);
        //rowHeader.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        { 
            //GridBagConstraints c = new GridBagConstraints();		
    		//setLayout(new GridBagLayout());
    		//c.weightx=1; c.weighty=1; c.fill = GridBagConstraints.BOTH;
    		//c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=0; c.ipady=0;
        	add(scrollPane);
        }
    }
    
    public DataFramePanel(RDataFrameRef dfRefW) {
    	this(dfRefW, true, true);
    }

    public DataTable getTable() {
        return table;
    }
}
