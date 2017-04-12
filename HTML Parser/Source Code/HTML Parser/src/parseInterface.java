import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Queue;

public class parseInterface {
    private static final FileHandler fh = new FileHandler();
    private static final HtmlParser parser = new HtmlParser();
    private static ArrayList<String> selectorName = new ArrayList<String>();
    private static ArrayList<Integer> deleteFirst = new ArrayList<Integer>();
    private static ArrayList<Integer> deleteLast = new ArrayList<Integer>();
    private static ArrayList<String> selectorTag = new ArrayList<String>();
    private static ArrayList<String> fromWords = new ArrayList<String>();
    private static ArrayList<String> toWords = new ArrayList<String>();

    /**
     * Use in any occasion where the api needs to print something to the log.
     * @param toBeAppended
     */
    public static void appendToLog(String toBeAppended) {
        FileChoosePanel.append(toBeAppended);
    }
    
	/**
	 * For loading a new file pool.
	 */
    public static void chooseFiles() {
        fh.chooseFiles();
        if (parseInterface.getFilePoolSize() > 0) {
            parseInterface.appendToLog("Current File-Pool Size: '" + parseInterface.getFilePoolSize() + "'");
            parser.fileToSplitString(parseInterface.getExampleFile());
            parseInterface.appendToLog("Now viewing: " + parseInterface.getExampleFile().getName());
        } else {
            parseInterface.appendToLog("No File or Directory chosen. Please select a File or Directory.");
        }
    }

    /**
     * sends the un-appended version of the exampleFile to the view panel.
     */
    public static void resetExampleFile() {
    	if (parseInterface.getFilePoolSize() > 0) {
    		parser.fileToSplitString(parseInterface.getExampleFile());
    		parseInterface.appendToLog("Currently viewing: " + parseInterface.getExampleFile().getName());
    	}else{
    		parseInterface.appendToLog("There is no file to reset.");
    	}
    }
    
    /**
     * Choose the save file location.
     */
    public static void chooseSaveLocation() {
        fh.chooseSaveDirectory();
    }

    /**
     * Return the chosen example file.
     * This can be used as a reset as well (print to the file parsing card this example file again.
     * @return
     */
    public static File getExampleFile() {
        if (!fh.getFileList().isEmpty()) {
            return fh.getExampleFile();
        }
        return null;
    }

    /**
     * Saves the file that fits the specified integer and removes it from all relevant lists.
     * @param finalString
     */
    public static void saveNewVersion(){
    	if (!fh.getFileList().isEmpty()) {
	    	String isChosen = fh.chooseSaveDirectory();
	    	if(fh.getSaveLocation() == null){
	    		return;
	    	}
	    	if(isChosen != null){
		    	File chosen = fh.getExampleFile();
		    	String chosenString = parser.parseFile(chosen, selectorName, deleteFirst, deleteLast, selectorTag, fromWords, toWords);
		    	if(chosenString != null){
			    	parser.publishNewVersion(chosen, ViewPanel.getText(), fh.getSaveLocation());
			    	fh.removeFromFileList(chosen);
			    	parseExampleFile();
			    	parseInterface.appendToLog("Current File-Pool Size: '" + parseInterface.getFilePoolSize() + "'");
		    	}
	    	}
    	}else{
    		parseInterface.appendToLog("There is no file to save.");
    	}
    }
    
    /**
     * calls fileHandler's skip file ability.
     */
    public static void skipFile(){
    	if (!fh.getFileList().isEmpty()) {
	    	fh.skipCurrentFile();
	    	appendToLog("Skipping current File.");
	    	resetExampleFile();
    	}else{
    		parseInterface.appendToLog("There is no file to skip.");
    	}
    }
    
    /**
     * parses the first file in the current filePool.
     */
    public static void parseExampleFile() {
        try {
            if (!fromWords.get(0).equals("")) {
                parser.parseFile(parseInterface.getExampleFile(), selectorName, deleteFirst, deleteLast, selectorTag, fromWords, toWords);
            } else {
                parser.fileToSplitString(parseInterface.getExampleFile());
            }
        }
        catch (NullPointerException e) {
            parseInterface.appendToLog("There are currently no files in the pool");
        }
    }
    
    /**
     * removes all files from the file pool.
     */
    public static void emptyFilePool() {
    	ArrayList<File> emptying = new ArrayList<File>();
    	for(File file : fh.getFileList()){
    		emptying.add(file);
    	}
    	for(File file : emptying){
    		fh.removeFromFileList(file);
    	}
    	parseInterface.appendToLog("File pool is now empty.");
    	ViewPanel.clear();
    }

    /**
     * returns the current list of parsedfiles from fileHandler (each file can be parsed and shown individually with this.)
     * @return
     */
    public static ArrayList<File> getParsedFiles(){
    	return fh.getParsedList();
    }
    
    /**
     * Returns the current number of files in the file pool.
     * @return
     */
    public static int getFilePoolSize() {
        return fh.getFileCount();
    }

    /**
     * Commits the first parse (or batching of) files and fills the parsed pool in the file handler, builds the CardFrameView,
     * and creates sql queries for the articles.
     */
    public static void parsePool() {
        if (!fh.getFileList().isEmpty()) {
            if (fh.getSaveLocation() != null) {
            	fh.setParsedList(parser.saveFiles(fh.getFileList(), fh.getSaveLocation(), selectorName, deleteFirst, deleteLast, selectorTag, fromWords, toWords));
            	new CardFrameView(parser.getParsedCardFiles());
            	for(File f : fh.getParsedList()){
            		String sql = parser.createSQL(f);
            		System.out.println(sql);
            		fh.removeFromFileList(f);
            	}
                parseInterface.appendToLog("Current File-Pool Size: '" + parseInterface.getFilePoolSize() + "'");
            } else {
                System.out.println("save Location not yet specified");
            }
        } else {
            System.out.println("filePool is empty");
        }
        if (!fh.getFileList().isEmpty()) {
            parser.fileToSplitString(parseInterface.getExampleFile());
            parseInterface.appendToLog("Currently viewing: " + parseInterface.getExampleFile().getName());
        }
    }

    public static void setValues(ArrayList<String> newSelectorName, ArrayList<Integer> newDeleteFirst, ArrayList<Integer> newDeleteLast, ArrayList<String> newSelectorTag, ArrayList<String> fromWords2, ArrayList<String> toWords2) {
        selectorName = newSelectorName;
        deleteFirst = newDeleteFirst;
        deleteLast = newDeleteLast;
        selectorTag = newSelectorTag;
        fromWords = fromWords2;
        toWords = toWords2;
    }
}

