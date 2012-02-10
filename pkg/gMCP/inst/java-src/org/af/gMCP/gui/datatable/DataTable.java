package org.af.gMCP.gui.datatable;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.graph.EdgeWeight;

public class DataTable extends JTable {

    public DataTable(RDataFrameRef df) {
        this(new DataTableModel(df));
    }
    
    public DataTable(DataTableModel dataTableModel) {
        super(dataTableModel);
        getTableHeader().setReorderingAllowed(false);
        getColumnModel().setColumnSelectionAllowed(false);
        setRowSelectionAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setDefaultEditor(EdgeWeight.class, new CellEditorE(null, this));
    	setDefaultRenderer(EdgeWeight.class, new EpsilonTableCellRenderer());
    }

    public DataTableModel getModel() {
        return (DataTableModel) super.getModel();
    }

    public void update() {
        getModel().fireTableStructureChanged();
    }

    //TODO:
   public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();
        return new Dimension(Math.min(getPreferredSize().width, size.width), getPreferredSize().height);
        //return new Dimension(getPreferredSize().width, getPreferredSize().height);
    }

    public boolean getScrollableTracksViewportWidth() {
        return this.getAutoResizeMode() == AUTO_RESIZE_OFF ?
                this.getParent().getWidth() > this.getPreferredSize().width :
                super.getScrollableTracksViewportWidth();
    }

    public String getColumnVar(int col) {    	
        return getModel().getColumnName(col);
    }

	public void setTesting(boolean testing) {
		getModel().setTesting(testing);
	}
	
	public void changeSelection(final int row, final int column, boolean toggle, boolean extend) {
		super.changeSelection(row, column, toggle, extend);
		if (Configuration.getInstance().getGeneralConfig().focusEqualsEdit()) {		
			editCellAt(row, column);
			transferFocus();
			Component editorComponent = getEditorComponent();
			if (editorComponent instanceof JTextField) {
				((JTextField) editorComponent).selectAll();
			}
		}
    }
}
