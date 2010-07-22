package org.af.statguitoolkit.gui.datatable;

import java.util.ArrayList;
import java.util.Arrays;

public class CellEditorLog extends CellEditorCB {
    public CellEditorLog(Boolean sel) {
        super(new ArrayList<String>(Arrays.asList(""+true, ""+false)),
              /* TODO RLogical.isNA(sel)  ? CellRenderer.NA_STRING : */ sel.toString());
    }

    protected Object parse(String s) {
        /*TODO if (s.equals(CellRenderer.NA_STRING))
            return RLogical.NA_VAL;
        else */
            return Boolean.parseBoolean(s);
    }
}
