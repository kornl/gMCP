package org.af.statguitoolkit.gui.datatable;

import org.af.commons.widgets.validate.ValidatedTextField;
import org.af.commons.widgets.validate.ValidationException;

public class TfWithNA extends ValidatedTextField<String> {
    public TfWithNA() {
        super("");
    }

    public String getValidatedValue() throws ValidationException {
        return getText();
    }

    public String getValidationErrorMsg() {
        return null;
    }
}
