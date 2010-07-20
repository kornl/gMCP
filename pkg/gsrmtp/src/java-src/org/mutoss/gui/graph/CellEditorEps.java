package af.statguitoolkit.graph;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;


public class CellEditorEps extends DefaultCellEditor {
	
	AbstractGraphControl agc;

    private Object oldVal;
    int row;
    int col;
    
    public CellEditorEps(AbstractGraphControl agc, int row, int col, String s) {
        super(new JTextField());
		this.agc = agc;
		this.row = row;
		this.col = col;
		// TODO: WHY DO I NEED THIS s.replace(',','.');
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

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }
}
