package org.af.statguitoolkit.gui.datatable;

import java.util.List;

import javax.swing.DefaultCellEditor;

import org.af.commons.widgets.lists.MyJComboBox;

public abstract class CellEditorCB extends DefaultCellEditor {
    private static List<String> addNA(List<String> ss) {
        ss.add(CellRenderer.NA_STRING);
        return ss;
    }

    public CellEditorCB(List<String> ss, String sel) {
        super(new MyJComboBox<String>(addNA(ss)));
        getComponent().setSelectedObject(sel);
    }

    @Override
    public MyJComboBox<String> getComponent() {
        return (MyJComboBox<String>) super.getComponent();
    }

    protected abstract Object parse(String s);

    @Override
    public Object getCellEditorValue() {
        return parse(getComponent().getSelectedItem());
    }

}
