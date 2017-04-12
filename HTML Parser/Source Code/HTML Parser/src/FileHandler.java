
import java.awt.Component;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFileChooser;

public class FileHandler {
    private final JFileChooser chooser = new JFileChooser(String.valueOf(System.getProperty("user.home")) + System.getProperty("file.separator") + "Desktop");
    private ArrayList<File> fileList = new ArrayList<File>();
    private ArrayList<File> parsedList = new ArrayList<File>();
    private int fileCount = 0;
    private File saveLocation = null;
    
    /**
     * gets the current ParsedList
     * @return the list that holds the current parsed files
     */
    public ArrayList<File> getParsedList() {
		return parsedList;
	}

    /**
     * Sets the parsedList currently in use.
     * @param parsedList list of parsed files
     */
	public void setParsedList(ArrayList<File> parsedList) {
		this.parsedList = parsedList;
	}

	

	/**
	 * displays the filechoosedialog for where to load from.
	 */
    public void chooseFiles() {
        this.chooser.setFileSelectionMode(2);
        this.chooser.setDialogTitle("Choose what file or directory to load from");
        int success = this.chooser.showOpenDialog(this.chooser);
        if (success == 0) {
            File chosen = this.chooser.getSelectedFile();
            this.buildFileList(chosen);
            parseInterface.appendToLog("File chosen: '" + chosen.getName() + "'");
        }
    }

    /**
     * returns the current specified save location
     * @return the address of the save location
     */
    public File getSaveLocation() {
        return this.saveLocation;
    }

    /**
     * chooses the save directory / where we will be saving to.
     */
    public String chooseSaveDirectory() {
        this.chooser.setFileSelectionMode(1);
        int success = 1;
        this.chooser.setDialogTitle("Choose where to save the parsed files");
        this.chooser.setApproveButtonText("Save");
        success = this.chooser.showOpenDialog(this.chooser);
        if (success == 0) {
            this.saveLocation = this.chooser.getSelectedFile();
            parseInterface.appendToLog("Save location chosen: '" + this.saveLocation + "'");
            return "chosen";
        }
        System.out.println("Please choose a Directory");
        return null;
    }

    /**
     * builds our file list from the chosen directory.
     * @param file File or directory to be scanned for html files matching the correct number format.
     */
    public void buildFileList(File file) {
        if (file.isDirectory()) {
            File[] arrfile = file.listFiles();
            int n = arrfile.length;
            int n2 = 0;
            while (n2 < n) {
                File f = arrfile[n2];
                this.buildFileList(f);
                ++n2;
            }
        } else {
            String limiter = file.getName();
            if (file.getName().endsWith(".html") && limiter.substring(0, limiter.indexOf(".html")).matches("\\d+")) {
                this.fileList.add(file);
                ++this.fileCount;
            }
        }
    }

    /**
     * moves the current file to the back of our queue.
     */
    public void skipCurrentFile(){
    	fileList.add(fileList.remove(0));
    	parseInterface.getExampleFile();
    }
    
    /**
     * returns our current full file pool.
     * @return current file pool
     */
    public ArrayList<File> getFileList() {
        return this.fileList;
    }

    /**
     * returns the file at index 0;
     * @return the first file in the file pool
     */
    public File getExampleFile() {
        return this.fileList.get(0);
    }

    /**
     * removes the specified file from our file list.
     * @param file file to be removed from the pool
     */
    public void removeFromFileList(File file) {
        fileList.remove(file);
        this.fileCount = this.fileList.size();
    }

    /**
     * gets the file count.
     * @return number of files in the file pool
     */
    public int getFileCount() {
        return this.fileCount;
    }
}

