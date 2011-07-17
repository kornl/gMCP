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
    
    public CellEditorEps(GraphView agc, int row, int col, String s) {
        super(new JTextField());
		this.agc = agc;
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
    	agc.updateEdge(row, col, oldVal); 
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
