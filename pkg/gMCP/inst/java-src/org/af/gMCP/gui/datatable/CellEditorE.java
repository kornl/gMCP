package org.af.gMCP.gui.datatable;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.af.gMCP.gui.graph.GraphView;

public class CellEditorE extends AbstractCellEditor implements TableCellEditor {
    private DefaultCellEditor ed;
    GraphView agc;
    DataTable dt;
    
    public CellEditorE(GraphView agc, DataTable dt) {    	
    	this.agc = agc;
    	this.dt = dt;
    }

    public Object getCellEditorValue() {
        return ed.getCellEditorValue();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int col) {
        
        ed = new CellEditorEps(agc, dt, row, col, value.toString());
        
        ed.addCellEditorListener(table);
        return ed.getComponent();
    }

}
