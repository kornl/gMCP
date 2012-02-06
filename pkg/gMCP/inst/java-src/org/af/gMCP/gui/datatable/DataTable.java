package org.af.gMCP.gui.datatable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.JTextField;

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
              
    	setDefaultRenderer(EdgeWeight.class, new EpsilonTableCellRenderer());
    }

    public DataTableModel getModel() {
        return (DataTableModel) super.getModel();
    }

    public void update() {
        getModel().fireTableStructureChanged();
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension size = super.getPreferredScrollableViewportSize();
        return new Dimension(Math.min(getPreferredSize().width, size.width), size.height);
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
	
    /**
     * We change single left clicks to double clicks here.
     */ 
    protected void processMouseEvent(MouseEvent e) {
    	if (e.getClickCount()==1 && e.getButton() == MouseEvent.BUTTON1) {
    		super.processMouseEvent(new MouseEvent((Component)e.getSource(), 
    				e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), 
    				e.getClickCount()+1, false, e.getButton()));
    		Component editorComponent = getEditorComponent();
    		if (editorComponent instanceof JTextField) {
				((JTextField) editorComponent).selectAll();
			}
    	} else if (e.getButton() == MouseEvent.BUTTON3) {
    		super.processMouseEvent(new MouseEvent((Component)e.getSource(), 
    				 MouseEvent.MOUSE_PRESSED, e.getWhen(), MouseEvent.BUTTON1_MASK, e.getX(), e.getY(), 
    				1, false, MouseEvent.BUTTON1));
    		super.processMouseEvent(e);
    	} else {			
    		super.processMouseEvent(e);
    	}
    }
}
