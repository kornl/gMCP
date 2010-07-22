package org.af.statguitoolkit.gui.datatable;

public class CellEditorInt extends CellEditorTf<Integer> {
    public CellEditorInt(Integer val) {
        super(new TfIntWithNA(), val);
    }
}
