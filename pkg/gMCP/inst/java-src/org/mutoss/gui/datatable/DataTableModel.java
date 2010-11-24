package org.mutoss.gui.datatable;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {

	protected RDataFrameRef df;
	private RowModel rowModel = null;

    public DataTableModel(RDataFrameRef df) {
        this.df = df;
    }

    public int getColumnCount() {
        return df.getColumnCount();
    }

    public int getRowCount() {
        return df.getRowCount();
    }

    public String getColumnName(int col) {
        return df.getColName(col);
    }

    // we do rendering and editing ourselves anyway, so don't need this
    public Class<?> getColumnClass(int col) {
        return CellValue.class;
    }

    public boolean isCellEditable(int rowIndex, int col) {
        return true;
    }

    public void setValueAt(Double value, int row, int col) {
        getDataFrame().setValue(row, col, value);		
		fireTableChanged(new TableModelEvent(this, row));
    }

    public CellValue getValueAt(int row, int col) {
        return new CellValue(df.getElement(row, col));
    }
    
    public void addRowCol(String name) {
        df.addRowCol(name);
        fireTableRowsInserted(df.getColumnCount(), df.getColumnCount());        
        fireTableStructureChanged();  	
	}

    public void delRowCol(int col) {
        df.delRowCol(col);
        fireTableStructureChanged();
    }

    public RDataFrameRef getDataFrame() {
        return df;
    }
    
    public void fireTableStructureChanged() {
    	super.fireTableStructureChanged();
    	if (rowModel!=null) rowModel.fireTableStructureChanged();
    }    
    
	public void setRowModel(RowModel rowModel) {
		this.rowModel  = rowModel;		
	}

}

