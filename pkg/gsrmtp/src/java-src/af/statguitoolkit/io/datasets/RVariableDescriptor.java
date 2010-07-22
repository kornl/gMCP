package org.af.statguitoolkit.io.datasets;

import org.af.jhlir.call.RDataFrame;


/**
 * Describes R data set which is already stored in an R variabale
 * (probably because it was read in in a .R-source file)
 */
public class RVariableDescriptor extends DataFrameDescriptor {
    private final RDataFrame df;
    private final String title;

    public RVariableDescriptor(RDataFrame df, String title) {
        this.df = df;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public RDataFrame getDataFrame() {
        return df;
    }
}
