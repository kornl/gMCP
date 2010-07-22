package org.af.statguitoolkit.gui.datatable;

import java.awt.Component;
import java.util.List;

import org.af.commons.Localizer;
import org.af.jhlir.call.RDataFrame;

public class VarRenameDialog extends VarNameDialog{

    private String var;

    public VarRenameDialog(Component parent, RDataFrame dfRefW, String var) {
        super(parent,
                Localizer.getInstance().getString("SGTK_DATATABLE_VARRENAMEDIALOG_RENAMEVAR") + " " + var, dfRefW);
        this.var = var;
    }

    protected String checkNameConstraints(String name, List<String> allVarNames) {
        return !var.equals(name) ? super.checkNameConstraints(name, allVarNames) : null;
    }
}
