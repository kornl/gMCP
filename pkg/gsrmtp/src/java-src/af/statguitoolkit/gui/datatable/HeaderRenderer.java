package org.af.statguitoolkit.gui.datatable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.af.jhlir.call.RChar;
import org.af.jhlir.call.RFactor;
import org.af.jhlir.call.RInteger;
import org.af.jhlir.call.RLogical;
import org.af.jhlir.call.RNumeric;
import org.af.jhlir.call.RVectorFactor;

public class HeaderRenderer extends DefaultTableCellRenderer {
    private TableCellRenderer renderer;

    public HeaderRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        HeaderCellValue hcv = (HeaderCellValue) value;
        RVectorFactor dfColW = hcv.col;
        Component comp = renderer.getTableCellRendererComponent(table, hcv.val, isSelected, hasFocus, row, col);

        if (dfColW instanceof RNumeric) comp.setForeground(DataTable.NUM_COLOR);
        if (dfColW instanceof RInteger) comp.setForeground(DataTable.INT_COLOR);
        if (dfColW instanceof RLogical) comp.setForeground(DataTable.LOGICAL_COLOR);
        if (dfColW instanceof RFactor) comp.setForeground(DataTable.FACTOR_COLOR);
        if (dfColW instanceof RChar) comp.setForeground(DataTable.CHAR_COLOR);
        return comp;
    }
}
