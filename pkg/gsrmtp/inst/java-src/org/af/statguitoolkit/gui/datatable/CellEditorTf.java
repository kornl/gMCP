package org.af.statguitoolkit.gui.datatable;

import javax.swing.DefaultCellEditor;

import org.af.commons.widgets.validate.ValidatedTextField;
import org.af.commons.widgets.validate.ValidationException;

public class CellEditorTf<E> extends DefaultCellEditor {
    private E oldVal;
    public CellEditorTf(ValidatedTextField<E> tf, E val) {
        super(tf);
        getComponent().setValue(val);
        oldVal = val;
    }

    @Override
    public ValidatedTextField<E> getComponent() {
        return (ValidatedTextField<E>) super.getComponent();
    }

    //revert to old val in case of no validation
    public Object getCellEditorValue() {
        try {
            oldVal = getComponent().getValidatedValue();
        } catch (ValidationException e) {
        }
        return oldVal;
    }

    @Override
    public boolean stopCellEditing() {
        try {
            getComponent().getValidatedValue();
        } catch (ValidationException e) {
            return false;
        }
        fireEditingStopped();
        return true;
    }
}
