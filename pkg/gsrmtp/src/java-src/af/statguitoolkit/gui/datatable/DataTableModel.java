package org.af.statguitoolkit.gui.datatable;

import java.rmi.RemoteException;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import org.af.jhlir.call.RDataFrame;
import org.af.jhlir.call.RFactor;
import org.af.jhlir.call.RLegalName;
import org.af.jhlir.call.RVector;
import org.af.jhlir.call.RVectorFactor;

public class DataTableModel extends AbstractTableModel{


	protected RDataFrame df;

    public DataTableModel(RDataFrame df) {
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

    /* TODO public void setColumnName(int col, RLegalName newName) {
        RLegalName oldName = df.getColNameLN(col);
        try {
			df.setVarName(oldName, newName);
		} catch (RemoteException e) {
			ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e);
		}
        fireTableStructureChanged();
    } */

    // we do rendering and editing ourselves anyway, so don't need this
    public Class<?> getColumnClass(int col) {
        return CellValue.class;
    }

    public boolean isCellEditable(int rowIndex, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
		getDataFrame().setValue(row, col, value);
		fireTableChanged(new TableModelEvent(this, row));
    }

    public CellValue getValueAt(int row, int col) {
        return new CellValue(df.get(row, col), df.getCol(col));
    }

    public void addRow(int row) throws RemoteException {
        df.addRow(row);
        fireTableRowsInserted(row, row);
    }
    
    public void addRow(int row, RLegalName name) throws RemoteException {
        df.addRow(row, name);
        fireTableRowsInserted(row, row);    	
	}

    public void addCol(int col, RLegalName name, RVector v) throws RemoteException {
        df.addCol(col, name, v);
        fireTableStructureChanged();
    }

    public void addCol(int col, RLegalName name, RFactor v) throws RemoteException {
        df.addCol(col, name, v);
        fireTableStructureChanged();
    }

    public void addCol(RLegalName name, RVectorFactor v) throws RemoteException {
        df.addCol(name, v);
        fireTableStructureChanged();
    }

    public void delRow(int row) throws RemoteException {
        df.delRow(row);
        fireTableRowsDeleted(row, row);
    }

    public void delCol(int col) throws RemoteException {
        df.delCol(col);
        fireTableStructureChanged();
    }


    public RDataFrame getDataFrame() {
        return df;
    }

}

