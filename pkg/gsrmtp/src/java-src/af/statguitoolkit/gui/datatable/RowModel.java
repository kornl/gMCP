package org.af.statguitoolkit.gui.datatable;

import javax.swing.table.AbstractTableModel;

public class RowModel extends AbstractTableModel {

	DataTableModel dataTableModel;
	
    public RowModel(DataTableModel dataTableModel) {
    	this.dataTableModel = dataTableModel;
    }
	
	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		return dataTableModel.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataTableModel.getDataFrame().getRowName(rowIndex);
	}

}
