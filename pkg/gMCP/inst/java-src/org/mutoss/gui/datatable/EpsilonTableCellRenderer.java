package org.mutoss.gui.datatable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.graph.EdgeWeight;

public class EpsilonTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final Log logger = LogFactory.getLog(EpsilonTableCellRenderer.class);
	
	public EpsilonTableCellRenderer() { super(); }

    public void setValue(Object value) {
    	setText(value.toString());
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)	{    	
    	DataTableModel model = (DataTableModel) table.getModel();

    	double sum = 0;

    	for (int i=0; i<model.getColumnCount(); i++) {
    		EdgeWeight ew = model.getValueAt(row, i);
    		Double d = 0.0;
    		try {
    			d = ew.getWeight(null);
    		} catch (Exception e) {
    			// Seriously - we don't want to do anything: d=0.0 is fine.
    		}
    		if (d>1||d<0) sum=1000;
    		sum += (d>=0&&d<=1)?d:0;
    	}

    	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        JLabel label = (JLabel) dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);       
   
        label.setText(value.toString());
        
    	if(sum>1.0001) {
    		label.setForeground(Color.RED);
    		label.setBackground(Color.ORANGE);
    	} else {
    		label.setForeground(null);
    		label.setBackground(null);
    	}
    	
    	return label;

    }


}
