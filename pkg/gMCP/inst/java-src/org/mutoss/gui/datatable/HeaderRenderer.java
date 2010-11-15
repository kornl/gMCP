package org.mutoss.gui.datatable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class HeaderRenderer extends DefaultTableCellRenderer {
    private TableCellRenderer renderer;

    public HeaderRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    	if (value instanceof HeaderCellValue) {
    		HeaderCellValue hcv = (HeaderCellValue) value;
    		Component comp = renderer.getTableCellRendererComponent(table, hcv.val, isSelected, hasFocus, row, col);

    		comp.setForeground(DataTable.NUM_COLOR);  	

    		return comp;
    	} else {
    		Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        	comp.setForeground(DataTable.NUM_COLOR);  	

        	return comp;
		
    	}
    }
}
