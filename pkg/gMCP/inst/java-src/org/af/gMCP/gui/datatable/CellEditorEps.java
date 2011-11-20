package org.af.gMCP.gui.datatable;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.af.gMCP.gui.graph.EdgeWeight;
import org.af.gMCP.gui.graph.GraphView;

public class CellEditorEps extends DefaultCellEditor implements FocusListener {
	
	GraphView agc;

    private EdgeWeight oldVal;
    int row;
    int col;
    DataTable dt;
    
    public CellEditorEps(GraphView agc, DataTable dt, int row, int col, String s) {
        super(new JTextField());
		this.agc = agc;
		this.dt = dt;
		this.row = row;
		this.col = col;
		// TODO: WHY DO I NEED THIS s.replace(',','.'); Yes - I know, this looks simple, but there are strange things out there.
		s = s.replace(',','.');
		oldVal = new EdgeWeight(s);
		((JTextField)getComponent()).setText(oldVal.toString());
		((JTextField)getComponent()).addFocusListener(this);	 
    }

    public EdgeWeight getCellEditorValue() {
    	String s = ((JTextField)getComponent()).getText();
    	oldVal = new EdgeWeight(s);
    	if (agc!=null) { 
    		agc.updateEdge(row, col, oldVal); 
    	} else {
    		dt.getModel().setValueAt(oldVal, row, col);
    		dt.getModel().setValueAt(oldVal, col, row);
    	}
    	return oldVal;
    }
    
	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		try {
			stopCellEditing();
		} catch(Exception ex) {
			// Nothing to do
		}
	}
    
}
