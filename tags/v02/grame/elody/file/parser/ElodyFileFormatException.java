package grame.elody.file.parser;

/*******************************************************************************************
*
*	 ElodyFileFormatException (classe) : exception lancée eventuellement lors du parsing
*                                        en cas de non reconnaissance d'un format
* 
*******************************************************************************************/

public final class ElodyFileFormatException extends Exception {
	   /**
     * Constructor.
     */
    public ElodyFileFormatException() {
		super();
    }

    /**
     * Constructor with error number.
     */
    public ElodyFileFormatException(int rc) {
		super("ElodyFileFormatException error: " + rc);
    }

    /**
     * Constructor with string.
     */
    public ElodyFileFormatException(String string) {
		super("ElodyFileFormatException error: " + string);
    }
}
