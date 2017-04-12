import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Interface to reduce coupling between the parser components.
 * All calls pass through this interface
 */
public class ParserInterface {
    private static final FileHandler fh = new FileHandler();
    private static final DocxParser parser = new DocxParser();

    /**
     * Appends a string to the log in the file choose panel
     * @param String to append
     */
    public static void appendToLog(String toBeAppended) {
        FileChoosePanel.append(toBeAppended);
    }

    /**
     * Calls the parser and filehandler to set the filepool and 
     * display first file in the directory
     * @param none
     * @return none
     */
    public static void chooseFiles() {
        fh.chooseFiles();
        if (ParserInterface.getFilePoolSize() > 0) {
            ParserInterface.appendToLog("Current File-Pool Size: '" + ParserInterface.getFilePoolSize() + "'");
            parser.fileToView(ParserInterface.getExampleFile());
            ParserInterface.appendToLog("Now viewing: " + ParserInterface.getExampleFile().getName());
        } else {
            ParserInterface.appendToLog("No File or Directory chosen. Please select a File or Directory.");
        }
    }

    /**
     * Calls the parser and filehandler to set the filepool and 
     * display first file in the directory
     * @param none
     * @return none
     */
    public static void chooseSaveLocation() {
        try {
			fh.chooseSaveDirectory();
		} catch (UnsupportedEncodingException e) {
			ParserInterface.appendToLog("Error Saving file: unsupported encoding");
		}
        
    }

    /**
     * Returns the example file which is the first file in the filepool
     * @param none
     * @return File that is first in the directory
     */
    public static File getExampleFile() {
        if (!fh.getFileList().isEmpty()) {
            return fh.getExampleFile();
        }
        return null;
    }

    
    /**
     * Uses Apache POI to get text from a doc or docx file
     */
    public static void parseExampleFile() {
        try {
        	parser.fileToView(fh.getExampleFile());
        }
        catch (NullPointerException e) {
            ParserInterface.appendToLog("There are currently no files in the pool");
        }
    }
    
    
    /**
     * Reloads the original unedited version of the file
     */
    public static void reloadFile() {
        try {
        	parser.fileToView(fh.getExampleFile());
        }
        catch (NullPointerException e) {
            ParserInterface.appendToLog("There are currently no files in the pool");
        }
    }
    
    
    /**
     * Returns the current size of the file pool
     * @return number of files in the filepool
     */
    public static int getFilePoolSize() {
        return fh.getFileCount();
    }
    
    /**
     * Skips the current file, loads the next in the View Panel
     * Moves beginning file to the end of the queue
     */
    public static void skipFile() {
		fh.skipFile();
		parser.fileToView(fh.getExampleFile());
		ParserInterface.appendToLog("File Skipped");
		ParserInterface.appendToLog("Currently viewing: " + ParserInterface.getExampleFile().getName());
	}

    /**
     * Skips the current file, loads the next in the View Panel
     * Moves beginning file to the end of the queue
     */
    public static void prevFile() {
        fh.prevFile();
        parser.fileToView(fh.getExampleFile());
        ParserInterface.appendToLog("File Skipped");
        ParserInterface.appendToLog("Currently viewing: " + ParserInterface.getExampleFile().getName());
    }

	/**
	 * Wraps the selected text in the chosen html tag
	 * @param Tag to be appended before text
	 * @param Tag to be appended after text
	 */
	public static void wrapSelected(String open, String close) {
		parser.htmlWrapHeader(open, close, ViewPanel.getSelectionStart(), ViewPanel.getSelectionEnd());
		
	}

	/**
	 * Empties the filehandler file pool,
	 * clears the view panel
	 */
	public static void emptyPool() {
		fh.emptyPool();
		ViewPanel.clear();
	}

	/**
	 * Wraps the text selection in the chosen HTML tag
	 * @param string
	 */
	public static void wrapSelected(String string) {
		parser.htmlWrapHeader(string);
		
	}

	/**
	 * Undoes the last edit in the parser
	 */
	public static void undoLast() {
		ViewPanel.undoLast();
		
	}

	/**
	 * Parses file and returns string of contents
	 * @param exampleFile to be viewed
	 */
	public static void fileToView(File exampleFile) {
		parser.fileToView(exampleFile);
	}
}

