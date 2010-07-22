package org.af.statguitoolkit.gui.datatable;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.af.commons.Localizer;
import org.af.jhlir.call.RDataFrame;
import org.af.jhlir.call.RIllegalNameException;
import org.af.jhlir.call.RLegalName;

public class VarNameDialog {
    private Component parent;
    private String msg;
    private RDataFrame dfRefW;

    public VarNameDialog(Component parent, String msg, RDataFrame dfRefW) {
        this.parent = parent;
        this.msg = msg;
        this.dfRefW = dfRefW;
    }

    public RLegalName show() {
        String newName = JOptionPane.showInputDialog(parent, msg,
                Localizer.getInstance().getString("SGTK_DATATABLE_VARNAMEDIALOG_VARNAME"), 
                JOptionPane.QUESTION_MESSAGE);
        if (newName != null) {
            List<String> vars = Arrays.asList(dfRefW.getColNames());
            String err = null;
            if (vars.contains(newName))
                err = "Variable name already in use!";
            else {
                try {
                    return new RLegalName(newName);
                } catch (RIllegalNameException e) {
                    err = e.getMessage();
                }
            }
            if (err != null)
                JOptionPane.showMessageDialog(parent,
                        Localizer.getInstance().getString("SGTK_DATATABLE_VARNAMEDIALOG_VARNAMEINSUSE"));
        }
        return null;
    }

    protected String checkNameConstraints(String name, List<String> allVarNames) {
        if (allVarNames.contains(name))
            return Localizer.getInstance().getString("SGTK_DATATABLE_VARNAMEDIALOG_VARNAMEINSUSE");
        try {
            new RLegalName(name);
        } catch (RIllegalNameException e) {
            return e.getMessage();
        }
        return null;
    }
}
