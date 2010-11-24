package org.mutoss.gui.datatable;

import java.text.DecimalFormat;

import org.mutoss.config.Configuration;

public class CellValue {
	
    public final Object val;

    public CellValue(Object val) {
        this.val = val;
    }

    public String toString() {
        Object s = val;
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
