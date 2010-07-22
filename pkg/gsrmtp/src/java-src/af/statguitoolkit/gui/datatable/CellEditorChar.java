package org.af.statguitoolkit.gui.datatable;


public class CellEditorChar extends CellEditorTf<String>{
    public CellEditorChar(String val) {
        super(new TfWithNA(), val);
    }

    @Override
    public Object getCellEditorValue() {
        String s = super.getCellEditorValue().toString();
        /* TODO if (s.equals("") || RChar.isNA(s))
            return RChar.NA_VAL;
        else */
            return s;
    }
}
