package org.af.statguitoolkit.gui.datatable;

import org.af.commons.widgets.validate.RealTextField;

public class TfRealWithNA extends RealTextField {
    public TfRealWithNA() {
        super("");
    }

    public void setValue(Double x) {
        setText( /* TODO x.equals(RNumeric.NA_VAL) ? "" : */ x.toString() );
    }

    @Override
    protected Double parseNumberString(String s) {
        return /* TODO s.equals("") ? RNumeric.NA_VAL : */ super.parseNumberString(s);
    }
}
