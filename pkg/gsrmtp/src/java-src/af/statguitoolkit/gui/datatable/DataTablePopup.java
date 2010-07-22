package org.af.statguitoolkit.gui.datatable;

import java.awt.event.ActionListener;

import javax.swing.JMenu;

import org.af.commons.Localizer;
import org.af.commons.widgets.tables.TablePopupMenu;

//TODO delete columns/rows
public class DataTablePopup extends TablePopupMenu implements ActionListener {
    public DataTablePopup(DataTable dt) {
        super(dt);
        add(makeMenuItem(
                Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLEPOPUP_DELROW"),
                "del_row"));
        add(makeMenuItem(
                Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLEPOPUP_DELCOL"),
                "del_col"));
        add(makeMenuItem(
                Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLEPOPUP_ADDROWBEFORE"),
                "add_row_before"));
        add(makeMenuItem(
                Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLEPOPUP_ADDROWAFTER"),
                "add_row_after"));

        JMenu menu = new JMenu("Add column before cell");
        Localizer loc = Localizer.getInstance();
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_NUMERIC"), "add_col_before_num"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_INTEGER"), "add_col_before_int"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_FACTOR"), "add_col_before_fact"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_CHAR"), "add_col_before_char"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_LOGICAL"), "add_col_before_log"));
        add(menu);

        menu = new JMenu("Add column after cell");
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_NUMERIC"), "add_col_after_num"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_INTEGER"), "add_col_after_int"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_FACTOR"), "add_col_after_fact"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_CHAR"), "add_col_after_char"));
        menu.add(makeMenuItem(loc.getString("SGTK_DATATABLE_DATATABLE_LOGICAL"), "add_col_after_log"));
        add(menu);
    }

    @Override
    public DataTable getTable() {
        return (DataTable) super.getTable();
    }
    
    @Override
    protected void actionPerformed(int row, int col, String cmd) {
    	/* TODO Uncommented adding of rows
        int rowNr = getTable().getRowCount();
        DataTableModel dtm = getTable().getModel();
        try {
            if (cmd.equals("del_row"))
                dtm.delRow(row);
            if (cmd.equals("del_col"))
                dtm.delCol(col);
            if (cmd.equals("add_row_before"))
                dtm.addRow(row);
            if (cmd.equals("add_row_after"))
                dtm.addRow(row + 1);

            RLegalName name = null;
            if (cmd.startsWith("add_col"))
                name = new VarNameDialog(this,
                        Localizer.getInstance().getString("SGTK_DATATABLE_DATATABLEPOPUP_CHOOSENAME"),
                        dtm.getDataFrame()).show();

            if (name != null) {
                if (cmd.equals("add_col_before_num"))
                    dtm.addCol(col, name, new RNumeric(new double[rowNr]));
                if (cmd.equals("add_col_after_num"))
                    dtm.addCol(col+1, name, new RNumeric(new double[rowNr]));
                if (cmd.equals("add_col_before_int"))
                    dtm.addCol(col, name, new RInteger(new int[rowNr]));
                if (cmd.equals("add_col_after_int"))
                    dtm.addCol(col+1, name, new RInteger(new int[rowNr]));
                if (cmd.equals("add_col_before_log"))
                    dtm.addCol(col, name, new RLogical(new boolean[rowNr]));
                if (cmd.equals("add_col_after_log"))
                    dtm.addCol(col+1, name, new RLogical(new boolean[rowNr]));
                if (cmd.equals("add_col_before_char"))
                    dtm.addCol(col, name, new RChar(new String[rowNr]));
                if (cmd.equals("add_col_after_char"))
                    dtm.addCol(col+1, name, new RChar(new String[rowNr]));
                if (cmd.equals("add_col_before_fact")) {
                    int[] code = new int[rowNr];
                    Arrays.fill(code, 1);
                    dtm.addCol(col, name, new RFactor(new String[]{"x"}, code));
                }
                if (cmd.equals("add_col_after_fact")) {
                    int[] code = new int[rowNr];
                    Arrays.fill(code, 1);
                    dtm.addCol(col+1, name, new RFactor(new String[]{"x"}, code));
                }
            }
        } catch (RemoteException e1) {
            ErrorHandler.getInstance().makeErrDialog("Error while adding row/col!", e1);
        }
        */
    }
}
