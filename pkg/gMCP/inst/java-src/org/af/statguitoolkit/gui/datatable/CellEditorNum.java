package org.af.statguitoolkit.gui.datatable;

public class CellEditorNum extends CellEditorTf<Double> {
    public CellEditorNum(Double val) {
        super(new TfRealWithNA(), val);
    }
}
