package org.af.statguitoolkit.gui.datatable;

import org.af.jhlir.call.RVectorFactor;


public class HeaderCellValue {
    public final String val;
    public final RVectorFactor col;

     public HeaderCellValue(String val, RVectorFactor col) {
         this.val = val;
         this.col = col;
     }

     public String toString() {
         return val;
     }
}
