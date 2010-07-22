package org.af.statguitoolkit.io.datasets;

/**
 * Describes R data set
 */
public class RDataFrameDescriptor extends DataFrameDescriptor {
    // name of data set
    private final String name;
    // package of data set
    private final String rPackage;

    /**
     * Constructor
     *
     * @param name name of data set
     * @param rPackage package of data set
     */
    public RDataFrameDescriptor(String name, String rPackage) {
        this.name = name;
        this.rPackage = rPackage;
    }

    /**
     * @return name of data set
     */
    public String getName() {
        return name;
    }

    /**
     * @return package of data set
     */
    public String getRPackage() {
        return rPackage;
    }

	@Override
	public String getTitle() {
		return rPackage+":"+name;
	}
}
