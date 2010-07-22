package org.af.statguitoolkit.gui.datatable;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer {

    public static final String NA_STRING = "<NA>";

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int col) {
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        CellValue cv = (CellValue) value;
        JLabel label = (JLabel) dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        if (cv.val instanceof Number)
            label.setHorizontalAlignment(JLabel.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        return label;
    }

//    private Object formatVal(Object val) {
//        if (val instanceof Double)
//            
//        return val;
//    }
}
