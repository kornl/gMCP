package org.af.statguitoolkit.gui.datatable;

import java.util.Arrays;

import org.af.jhlir.call.RFactor;


public class CellEditorFactor extends CellEditorCB {

    public CellEditorFactor(RFactor v, String sel) {
        super(Arrays.asList(v.getLevels()), /* TODO ObjectUtils.equals(sel, RFactorW.NA_VAL) ?
                CellRenderer.NA_STRING : */ sel);
    }

    protected Object parse(String s) {
        return /* TODO s.equals(CellRenderer.NA_STRING) ?
                RFactor.NA_VAL : */ s;
    }
}
