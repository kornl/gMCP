package af.statguitoolkit.graph;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.af.jhlir.call.RVectorFactor;

import af.statguitoolkit.gui.datatable.CellValue;

public class CellEditorE  extends AbstractCellEditor implements TableCellEditor {
    private DefaultCellEditor ed;
    AbstractGraphControl agc;
    
    public CellEditorE(AbstractGraphControl agc) {    	
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
        CellValue cv = (CellValue) value;
        RVectorFactor dfCol = cv.col;
        
        ed = new CellEditorEps(agc, row, col, value.toString());
        
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
