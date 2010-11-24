package org.mutoss.gui.datatable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class EpsilonTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final Log logger = LogFactory.getLog(EpsilonTableCellRenderer.class);
	
	public EpsilonTableCellRenderer() { super(); }

	public String getString (Object value) {
		CellValue cv = (CellValue) value;
    	try {
    		// TODO: WHY DO I NEED THIS s.replace(',','.');
    		Double d = Double.parseDouble(cv.toString().replace(',', '.'));    		
        	return ""+d;        	
        } catch (NumberFormatException e) {
        	return "ε";
        }  
	}
	
    public void setValue(Object value) {
    	setText(getString(value));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)	{    	
    	DataTableModel model =  (DataTableModel) table.getModel();

    	double sum = 0;

    	for (int i=0; i<model.getColumnCount(); i++) {
    		Double d = (Double) model.getValueAt(row, i).val;
    		//logger.debug("d="+d);
    		if (d>1||d<0) sum=1000;
    		sum += (d>=0&&d<=1)?d:0;
    	}
    	//logger.debug("sum="+sum);

    	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        JLabel label = (JLabel) dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);       
    	
        label.setText(getString(value));
        
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
