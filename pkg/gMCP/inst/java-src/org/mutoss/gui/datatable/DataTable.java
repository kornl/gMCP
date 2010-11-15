package org.mutoss.gui.datatable;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


//TODO selection of rows/cols and copy paste
public class DataTable extends JXTable2 {
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
        setTableHeaderRenderer();
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


    private void setTableHeaderRenderer() {
        TableCellRenderer r = getTableHeader().getDefaultRenderer();
        getTableHeader().setDefaultRenderer(new HeaderRenderer(r));
    }


    @Override
    public void createDefaultColumnsFromModel() {
        super.createDefaultColumnsFromModel();
        for (int j = 0; j < getColumnCount(); j++) {
            TableColumn aColumn = getColumn(j);
            int i = aColumn.getModelIndex();
            String name = getModel().getColumnName(i);
            double[] dfCol = getModel().getDataFrame().getCol(i);
            aColumn.setHeaderValue(new HeaderCellValue(name, dfCol));
        }
    }

    @Override
    public void addColumn(TableColumn aColumn) {
        int i = aColumn.getModelIndex();
        String name = getModel().getColumnName(i);
        double[] dfCol = getModel().getDataFrame().getCol(i);
        aColumn.setHeaderValue(new CellValue(name, dfCol));
        super.addColumn(aColumn);
    }

}
