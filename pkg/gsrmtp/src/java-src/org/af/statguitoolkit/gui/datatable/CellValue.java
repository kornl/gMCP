package org.af.statguitoolkit.gui.datatable;

import java.text.DecimalFormat;

import org.af.jhlir.call.RVectorFactor;

public class CellValue {
    public final Object val;
    public final RVectorFactor col;

    public CellValue(Object val, RVectorFactor col) {
        this.val = val;
        this.col = col;
    }

    public boolean isNA() {
        return false; /*TODO ObjectUtils.equals(col.getNaVal(), val);*/
    }

    public String toString() {
        Object s = val;
        if (isNA())
            s = CellRenderer.NA_STRING;
        if (val instanceof Double) {
            DecimalFormat df = new DecimalFormat();
            int n = 3; /* TODO Hardcoded 3 digits in table */
            df.setMinimumFractionDigits(n);
            df.setMaximumFractionDigits(n);
            df.setGroupingUsed(false);
            s = df.format(val);
        }
        return s.toString();
    }

}
