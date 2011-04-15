package org.mutoss.gui.datatable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.mutoss.gui.graph.EdgeWeight;
import org.mutoss.gui.graph.GraphView;

public class CellEditorEps extends DefaultCellEditor {
	
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
    }

    public EdgeWeight getCellEditorValue() {
    	String s = ((JTextField)getComponent()).getText();
    	oldVal = new EdgeWeight(s);
    	agc.updateEdge(row, col, oldVal); 
    	return oldVal;
    }

}
