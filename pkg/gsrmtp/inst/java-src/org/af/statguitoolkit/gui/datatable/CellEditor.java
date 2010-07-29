package org.af.statguitoolkit.gui.datatable;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.af.jhlir.call.RChar;
import org.af.jhlir.call.RFactor;
import org.af.jhlir.call.RInteger;
import org.af.jhlir.call.RLogical;
import org.af.jhlir.call.RNumeric;
import org.af.jhlir.call.RVectorFactor;

public class CellEditor  extends AbstractCellEditor implements TableCellEditor {
    private DefaultCellEditor ed;

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
        CellValue cv = (CellValue) value;
        RVectorFactor dfCol = cv.col;

        if (dfCol instanceof RFactor) 
            ed = new CellEditorFactor((RFactor)dfCol, (String)cv.val);
        else if (dfCol instanceof RLogical)
            ed = new CellEditorLog((Boolean)cv.val);
        else if (dfCol instanceof RChar)
            ed = new CellEditorChar((String)cv.val);
        else if (dfCol instanceof RNumeric)
            ed = new CellEditorNum((Double)cv.val);
        else if (dfCol instanceof RInteger)
            ed = new CellEditorInt((Integer)cv.val);
        ed.addCellEditorListener(table);
        return ed.getComponent();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 2;
        }
        return true;
    }
}
