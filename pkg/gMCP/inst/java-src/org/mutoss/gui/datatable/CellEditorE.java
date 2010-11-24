package org.mutoss.gui.datatable;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.mutoss.gui.graph.ControlMGraph;

public class CellEditorE  extends AbstractCellEditor implements TableCellEditor {
    private DefaultCellEditor ed;
    ControlMGraph agc;
    
    public CellEditorE(ControlMGraph agc) {    	
    	this.agc = agc;
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return ed.getCellEditorValue();
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int col) {
        
        ed = new CellEditorEps(agc, row, col, value.toString());
        
        ed.addCellEditorListener(table);
        return ed.getComponent();
    }

}
