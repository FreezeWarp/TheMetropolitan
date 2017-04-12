import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Class to handle the opening and saving of folders and files
 */
public class FileHandler {
    // String.valueOf(System.getProperty("user.home")
    private final JFileChooser chooser = new JFileChooser("U:" + System.getProperty("file.separator") + "Dropbox" + System.getProperty("file.separator") + "Metropolitan Newspaper" + System.getProperty("file.separator") + "Online Administrator Files" + System.getProperty("file.separator") + "Conversion Files" + System.getProperty("file.separator"));
    private ArrayList<File> fileList = new ArrayList<File>();
    private int fileCount = 0;

    /**
     * Calls jfilechooser and loads a file or folder
     */
    public void chooseFiles() {
        this.chooser.setFileSelectionMode(2);
        this.chooser.setDialogTitle("Choose what file or directory to load from");
        int success = this.chooser.showOpenDialog(this.chooser);
        if (success == 0) {
            File chosen = this.chooser.getSelectedFile();
            this.buildFileList(chosen);
            ParserInterface.appendToLog("File chosen: '" + chosen.getName() + "'");
        }
    }

    /**
     * Saves a file in the chosen directory with the chosen name,
     * prints the html code to a file
     * @throws UnsupportedEncodingException
     */
    public void chooseSaveDirectory() throws UnsupportedEncodingException {

    	int userSelection = this.chooser.showSaveDialog(this.chooser);
    	if (userSelection == JFileChooser.APPROVE_OPTION) {
    	    File fileToSave = this.chooser.getSelectedFile();
    	    ParserInterface.appendToLog("Saved as file: " + fileToSave.getAbsolutePath());
    	    File nFile = new File(fileToSave.getAbsolutePath());
    	    if(!fileToSave.getAbsolutePath().toLowerCase().endsWith(".html"))
    	    {
    	    	nFile = new File(fileToSave.getAbsolutePath()+".html");
    	    }
    	    else {
    	    	nFile = new File(fileToSave.getAbsolutePath());
    	    }
    	    
            OutputStream stream;
			try {
				stream = new FileOutputStream(nFile);
			
            OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            String finalString = ViewPanel.getText();
            try {
				writer.write(finalString);
				writer.close();
				fileList.remove(0);
				fileList.add(nFile);
				ParserInterface.fileToView(getExampleFile());
		        ParserInterface.appendToLog("Now viewing: " + ParserInterface.getExampleFile().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ParserInterface.appendToLog("Error saving File");
			}
            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				ParserInterface.appendToLog("Error saving File");
			}
			}
    	}

    
    /**
     * Builds an array of the files in a directory
     * @param file
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
            //String limiter = file.getName();
            if (file.getName().endsWith(".docx") || file.getName().endsWith(".doc")) {
                this.fileList.add(file);
                ++this.fileCount;
            }
        }
    }

    /**
     * Returns the arraylist of files
     * @return Arraylist of files chosen
     */
    public ArrayList<File> getFileList() {
        return this.fileList;
    }

    
    /**
     * Returns the first file in the pool
     * @return the first file in the pool
     */
    public File getExampleFile() {
        return this.fileList.get(0);
    }
    
    
    /**
     * Moves the first file in the queue to the end,
     * If filepool is empty, does nothing
     */
    public void skipFile() {
    	try {
    		if(!fileList.isEmpty()){
			    File temp = fileList.remove(0);
			    fileList.add(temp);
    		}
		} catch (IndexOutOfBoundsException e) {
			ParserInterface.appendToLog("There are no files in the filepool");
		}
    }


    /**
     * Moves the last file in the queue to top.
     * If filepool is empty, does nothing
     */
    public void prevFile() {
        try {
            if(!fileList.isEmpty()){
                File temp = fileList.remove(fileList.size() - 1);
                fileList.add(0,temp);
            }
        } catch (IndexOutOfBoundsException e) {
            ParserInterface.appendToLog("There are no files in the filepool");
        }
    }

    /**
     * Removes first file in the list
     * @param arrayList
     */
    public void removeFromFileList(ArrayList<File> arrayList) {
        while (!arrayList.isEmpty()) {
            this.fileList.remove(arrayList.remove(0));
        }
        this.fileCount = this.fileList.size();
    }

    /**
     * @return The number of files in the pool
     */
    public int getFileCount() {
        return this.fileCount;
    }

    /**
     * Empties the file list/pool
     */
	public void emptyPool() {
		fileList.clear();
        this.fileCount = 0;
		ParserInterface.appendToLog("File Pool Cleared. Please choose a file or folder. \n\r");
		
	}
}

