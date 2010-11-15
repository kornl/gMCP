package org.mutoss.gui.datatable;

import java.text.DecimalFormat;

import org.mutoss.config.Configuration;

public class CellValue {
	
	public static final String NA_STRING = "<NA>";
	
    public final Object val;
    public final double[] col;

    public CellValue(Object val, double[] col) {
        this.val = val;
        this.col = col;
    }

    public boolean isNA() {
        return val.equals(Double.NaN);
    }

    public String toString() {
        Object s = val;
        if (isNA())
            s = NA_STRING;
        if (val instanceof Double) {
            DecimalFormat df = new DecimalFormat();
            int n = Configuration.getInstance().getGeneralConfig().getDigitsInTables();
            df.setMinimumFractionDigits(n);
            df.setMaximumFractionDigits(n);
            df.setGroupingUsed(false);
            s = df.format(val);
        }
        return s.toString();
    }

}
