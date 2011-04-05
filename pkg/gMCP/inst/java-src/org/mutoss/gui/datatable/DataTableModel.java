package org.mutoss.gui.datatable;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import org.mutoss.gui.graph.EdgeWeight;

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

    public boolean isCellEditable(int rowIndex, int col) {
        return rowIndex != col;
    }

    public void setValueAt(EdgeWeight value, int row, int col) {
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

