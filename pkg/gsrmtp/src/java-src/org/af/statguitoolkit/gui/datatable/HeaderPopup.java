package org.af.statguitoolkit.gui.datatable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.JTableHeader;

import org.af.commons.Localizer;
import org.af.commons.widgets.MyJPopupMenu;
import org.af.jhlir.call.RChar;
import org.af.jhlir.call.RFactor;
import org.af.jhlir.call.RInteger;
import org.af.jhlir.call.RLogical;
import org.af.jhlir.call.RNumeric;

//TODO change factor levels
public class HeaderPopup extends MyJPopupMenu implements ActionListener {
    private DataTable dataTable;
    private JTableHeader header;
    private DataTableModel model;

    public HeaderPopup(DataTable dataTable) {
        super(new String[]{
                Localizer.getInstance().getString("SGTK_DATATABLE_HEADERPOPUP_RENAME"),
                Localizer.getInstance().getString("SGTK_DATATABLE_HEADERPOPUP_SELECTTYPE")},
                new String[]{"rename", "change_type"});
        addActionListener(this);
        this.dataTable = dataTable;
        header = dataTable.getTableHeader();
        model = dataTable.getModel();
    }

    public void actionPerformed(ActionEvent e) {
        int col = dataTable.getTableHeader().columnAtPoint(getInvocationPoint());
        if (col >= 0 && col < header.getColumnModel().getColumnCount()) {
            String var = dataTable.getColumnVar(col);
            if (e.getActionCommand().equals("rename")) {
                /* TODO rename(var, col); */
            } else if (e.getActionCommand().equals("change_type")) {
                /*TODO try {
                    changeType(var, col);
                } catch (RemoteException e1) {
                    ErrorHandler.getInstance().makeErrDialog("Error during type conversion.", e1);
                } */
            }
        }
    }



    /* TODO private void rename(String var, int col) {
        RLegalName newName = new VarRenameDialog(header, model.getDataFrame(), var).show();
        if (newName != null) {
                model.setColumnName(col, newName);
        }
    } */


    /**
     * Change the interpretation of the encoding of a variable
     * - integer --> integer, numeric or factor
     * - numeric --> numeric or integer (only if really is made of ints)
     * - factor --> factor, numeric or factor (last two only if alls levels are from given set)
     *
     * @param var variable in dataframe
     * @param col column in table header
     */
    /* TODO private void changeType(String var, int col) throws RemoteException {
        String[] newTypesStr = null;
        RDataFrame df = dataTable.getModel().getDataFrame();
        // current type is always first
        if (df.isRInt(col)) {
            newTypesStr = new String[]{"Integer", "Factor", "Numeric"};
        } else if (df.isRNum(col)) {
            RNumeric x = df.getAsRNumW(col);
            newTypesStr = x.canBeInterpretedAsInt() ? new String[]{"Numeric", "Integer", "Factor"} : new String[]{"Numeric", "Factor"};
        } else if (df.isRFactor(col)) {
            RFactor x = df.getAsRFactorW(col);
            if (x.allLevelsIntegers()) newTypesStr = new String[]{"Factor", "Integer", "Numeric"};
            else if (x.allLevelsNumerics()) newTypesStr = new String[]{"Factor", "Numeric"};
            else newTypesStr = new String[]{"Factor"};
        }

        if (newTypesStr != null) {
            Class[] newTypes = typeStringsToClass(newTypesStr);
            Class newType = new ComboBoxDialog<Class>(header,
                    "Select Type", "Select type of " + var + ":",
                    newTypes, newTypesStr).showAndWaitForInput();

            // if something was selected and different from current type
            if (newType != null && newType != newTypes[0]) {
                df.changeColumnType(col, newType);
                dataTable.update();
            }
        } else {
            JOptionPane.showMessageDialog(header, Localizer.getInstance().getString("SGTK_DATATABLE_HEADERPOPUP_NOTPOSSIBLE"));
        }
    } */

    private Class typeStringToClass(String type) {
        if (type.equals("Numeric")) return RNumeric.class;
        if (type.equals("Integer")) return RInteger.class;
        if (type.equals("Factor")) return RFactor.class;
        if (type.equals("Character")) return RChar.class;
        if (type.equals("Logical")) return RLogical.class;
        return null;
    }

    private Class[] typeStringsToClass(String[] types) {
        Class[] result = new Class[types.length];
        for (int i=0; i<types.length; i++) result[i] = typeStringToClass(types[i]);
        return result;
    }



}
