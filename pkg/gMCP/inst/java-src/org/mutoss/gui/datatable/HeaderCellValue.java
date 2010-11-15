package org.mutoss.gui.datatable;

public class HeaderCellValue {
    public final String val;
    public final double[] col;

     public HeaderCellValue(String val, double[] col) {
         this.val = val;
         this.col = col;
     }

     public String toString() {
         return val;
     }
}
