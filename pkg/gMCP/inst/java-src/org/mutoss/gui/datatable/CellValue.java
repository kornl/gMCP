package org.mutoss.gui.datatable;

import java.text.DecimalFormat;

import org.mutoss.config.Configuration;
import org.mutoss.gui.graph.EdgeWeight;

public class CellValue {
	
    public final EdgeWeight val;

    public CellValue(EdgeWeight val) {
        this.val = val;
    }

    public String toString() {
        Object s = val;
        if (false) {
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
