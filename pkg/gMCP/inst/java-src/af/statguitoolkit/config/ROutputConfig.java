package af.statguitoolkit.config;


public class ROutputConfig extends SpecificConfig {
	
	/**
	 * Constructor - use Configuration.getInstance().getROutputConfig() to access it.
	 * @param conf ROutputConfig object
	 */
    ROutputConfig(Configuration conf) {
        super(conf);
    }

    public boolean showCopyPasteTableButtons() {
        return getBoolProperty("show.copy.paste.table.buttons", "true");
    }

    public void setShowCopyPasteTableButtons(boolean b) {
       setBoolProperty("show.copy.paste.table.buttons", b);
    }

    public void setDigitsInTables(int d) {
        setIntProperty("digits.in.tables", d);
    }

    public int getDigitsInTables() {
        return getIntProperty("digits.in.tables", "3");
    }
}
