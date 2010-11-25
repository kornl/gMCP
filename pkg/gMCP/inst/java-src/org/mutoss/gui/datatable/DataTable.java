package org.mutoss.gui.datatable;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;

public class DataTable extends JTable {
    public static final Color NUM_COLOR = Color.BLUE;
    public static final Color INT_COLOR = new Color(0, 155, 0);
    public static final Color FACTOR_COLOR = Color.RED;
    public static final Color CHAR_COLOR = Color.GREEN;
    public static final Color LOGICAL_COLOR = Color.PINK;


    public DataTable(RDataFrameRef df) {
        this(new DataTableModel(df));
    }
    
    public DataTable(DataTableModel dataTableModel) {
        super(dataTableModel);
        // otherwise there seems to be a bug with pop-up, as col. dragged too
        // think about renderer / editor when allowing this!
        getTableHeader().setReorderingAllowed(false);
        getColumnModel().setColumnSelectionAllowed(false);
        setRowSelectionAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    	setDefaultRenderer(CellValue.class, new EpsilonTableCellRenderer());
    }

    @Override
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
}
