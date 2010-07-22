package org.af.statguitoolkit.gui.datatable;

import org.af.commons.widgets.validate.IntegerTextField;

public class TfIntWithNA extends IntegerTextField {
    public TfIntWithNA() {
        super("");
    }

    public void setValue(Integer x) {
        setText( /*TODO RInteger.isNA(x) ? "" : */ x.toString() );
    }

    @Override
    protected Integer parseNumberString(String s) {
        return /*TODO s.equals("") ? RInteger.NA_VAL : */ super.parseNumberString(s);
    }
}
