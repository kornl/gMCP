package org.mutoss.gui.datatable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.mutoss.gui.graph.ControlMGraph;


public class CellEditorEps extends DefaultCellEditor {
	
	ControlMGraph agc;

    private Object oldVal;
    int row;
    int col;
    
    public CellEditorEps(ControlMGraph agc, int row, int col, String s) {
        super(new JTextField());
		this.agc = agc;
		this.row = row;
		this.col = col;
		// TODO: WHY DO I NEED THIS s.replace(',','.'); No, this looks simple, but there are strange things out there.
		s = s.replace(',','.');
        try {        	
        	oldVal = Double.parseDouble(s);
        } catch (NumberFormatException e) {        	
        	oldVal = "ε";
        }
        ((JTextField)getComponent()).setText(oldVal.toString());
    }

    public Object getCellEditorValue() {
    	String s = ((JTextField)getComponent()).getText();
    	try {
        	oldVal = Double.parseDouble(s);
        } catch (NumberFormatException e) {
        	oldVal = Double.NaN;
        }    
        agc.updateEdge(row, col, (Double)oldVal); 
    	return oldVal;
    }

}
