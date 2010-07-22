package org.af.statguitoolkit.gui.datatable;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.af.commons.Localizer;
import org.af.jhlir.call.RDataFrame;
import org.af.jhlir.call.RVectorFactor;


//TODO selection of rows/cols and copy paste
public class DataTable extends JXTable2 {
    public static final Color NUM_COLOR = Color.BLUE;
    public static final Color INT_COLOR = new Color(0, 155, 0);
    public static final Color FACTOR_COLOR = Color.RED;
    public static final Color CHAR_COLOR = Color.GREEN;
    public static final Color LOGICAL_COLOR = Color.PINK;


    public DataTable(RDataFrame df) {
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
        setColumnControlVisible(true);

        getTableHeader().setComponentPopupMenu(new HeaderPopup(this));
        setComponentPopupMenu(new DataTablePopup(this));


        CellRenderer cr = new CellRenderer();
        TableCellEditor ce = new CellEditor();
        setDefaultEditor(CellValue.class, ce);
        setDefaultRenderer(CellValue.class, cr);
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
            RVectorFactor dfCol = getModel().getDataFrame().getCol(i);
            aColumn.setHeaderValue(new HeaderCellValue(name, dfCol));
        }
    }

    @Override
    public void addColumn(TableColumn aColumn) {
        int i = aColumn.getModelIndex();
        String name = getModel().getColumnName(i);
        RVectorFactor dfCol = getModel().getDataFrame().getCol(i);
        aColumn.setHeaderValue(new CellValue(name, dfCol));
        super.addColumn(aColumn);
    }

    /**
     * @return Box with JLabels which describe the color-coding of variable types
     */
    public Box getTypeDescriptionComponent() {
    	JLabel caption = new JLabel(Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLE_COLORKEY"));
        JLabel numeric = new JLabel(Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLE_NUMERIC"));
        JLabel integer = new JLabel(Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLE_INTEGER"));
        JLabel factor =  new JLabel(Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLE_FACTOR"));

        numeric.setForeground(NUM_COLOR);
        integer.setForeground(INT_COLOR);
        factor.setForeground(FACTOR_COLOR);

        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(caption);
        b.add(Box.createHorizontalGlue());
        b.add(numeric);
        b.add(Box.createHorizontalGlue());
        b.add(integer);
        b.add(Box.createHorizontalGlue());
        b.add(factor);
        b.add(Box.createHorizontalGlue());

        return b;
    }
}
