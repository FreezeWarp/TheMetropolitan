import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class HtmlParser {
	ArrayList<File> parsedCardFiles = new ArrayList<File>();
	String images = "";
	String sql = "insert into table_name values(";
	private File saveLocation = null;

    /** 
     * parses the file based on its many characteristics
     * @param f the file to be parsed
     * @param selectorName 
     * @param deleteFirst number of words to remove from the front of the string
     * @param deleteLast number of words to be removed from the back of the string
     * @param selectorTag tags to be appended to the beginning and end of each row
     * @param fromWords beginning of parsed area
     * @param toWords end of parsed area
     * @return
     */
    public String parseFile(File f, ArrayList<String> selectorName, ArrayList<Integer> deleteFirst, ArrayList<Integer> deleteLast, ArrayList<String> selectorTag, ArrayList<String> fromWords, ArrayList<String> toWords) {
        Boolean hasAllStrings = true;
        String fileString = this.fileToString(f);
        //Search the string to make sure it contains the tags needed
        for (String piece2 : fromWords) {
            if (fileString.contains(piece2)) continue;
            hasAllStrings = false;
        }
        for (String piece2 : toWords) {
            if (fileString.contains(piece2)) continue;
            hasAllStrings = false;
        }
        //If the string contains all the expected tags parse it.
        if (hasAllStrings.booleanValue()) {
            String finalString = "";
            int i = 0;
            while (i < fromWords.size()) {
                String fileCrawler = fileString.substring(fileString.indexOf(fromWords.get(i)));
                //is there a toWords / did the user expect a specific end tag.
                if (!toWords.get(i).equals("")) {
                    fileCrawler = fileCrawler.substring(fileCrawler.indexOf(">") + 1);
                    if (toWords.get(i).startsWith("<")) {
                        fileCrawler = fileCrawler.substring(0, fileCrawler.indexOf(toWords.get(i)));
                    } else {
                        fileCrawler = fileCrawler.substring(0, fileCrawler.indexOf(toWords.get(i)));
                        fileCrawler = fileCrawler.substring(0, fileCrawler.lastIndexOf("<"));
                    }
                    if(selectorTag.get(i).equals("article")){
                    	fileCrawler = cleanPTags(fileCrawler);
                    	fileCrawler = findImages(fileCrawler);
                    }
                    String endTag = selectorTag.get(i).split("\\s")[0];
                    if(deleteFirst.get(i) > 0 || deleteLast.get(i) > 0){
                    	fileCrawler = removeFirstLastWords(fileCrawler,deleteFirst.get(i), deleteLast.get(i));
                    }
                    //attach images
                    if(selectorTag.get(i).equals("article") && !images.equals("0")){
                    	String image = System.getProperty("line.separator") + images.substring(0,images.lastIndexOf(">") + 1);
                    	image = System.getProperty("line.separator") + "<div id=\"slider\">" 
                    			+ image + System.getProperty("line.separator") + "</div>" 
                    			+ System.getProperty("line.separator") 
                    			+ "<span class=\"slidecaption\">CAPTION FOR SLIDE SHOW GOES HERE</span>";
                		fileCrawler = image + fileCrawler;
                	}
                    
                    
                    finalString = String.valueOf(finalString) + "<" + selectorTag.get(i) + ">" + fileCrawler + "</" + endTag + ">";
                } else {
                	fileCrawler = this.findNextNonEmpty(fileCrawler);
                	if(selectorTag.get(i).equals("article")){
                    	fileCrawler = cleanPTags(fileCrawler);
                    	fileCrawler = findImages(fileCrawler);
                    }
                	if(deleteFirst.get(i) > 0 || deleteLast.get(i) > 0){
                    	fileCrawler = removeFirstLastWords(fileCrawler,deleteFirst.get(i), deleteLast.get(i));
                    }
                	String endTag = selectorTag.get(i).split("\\s")[0];
                	//attach images
                	if(selectorTag.get(i).equals("article") && !images.equals("0")){
                    	String image = System.getProperty("line.separator") + images.substring(0,images.lastIndexOf(">") + 1);
                    	image = System.getProperty("line.separator") + "<div id=\"slider\">" + 
                    			images + System.getProperty("line.separator") + "</div>";
                		fileCrawler = image + fileCrawler;
                	}
                	
                    finalString = String.valueOf(finalString) + "<" + selectorTag.get(i) + ">" + fileCrawler + "</" + endTag + ">";
                }
                finalString = String.valueOf(finalString) + System.getProperty("line.separator");
                ++i;
                
            }
            ViewPanel.clear();
            ViewPanel.append(finalString);
            return finalString;
        }
        parseInterface.appendToLog("File did not contain all intended selectors");
        this.fileToSplitString(f);
        return null;
    }

    public String createSQL(File sqlFile){
    	String sql = "";
    	try {
			BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
			while(reader.ready()){
				String possibleSQL = reader.readLine();
				if (possibleSQL.contains("\"articletitle\"") || 
						possibleSQL.contains("\"authorname\"") || 
						possibleSQL.contains("\"issuedate\"")){
					sql += possibleSQL + ",";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return sql;
    }
    /**
     * finds all images in the file and returns their names in a list, 
     * with any characters after the final '>' sign being the number of images found.
     * @param beginner the String to be searched
     * @return image string
     */
    public String findImages(String beginner){
    	String withoutImages = beginner;
    	images = "";
    	int imageCount = 0;
    	while (beginner.contains("<img")){
    		beginner = beginner.substring(beginner.indexOf("<img"));
    		String imageRemover = beginner.substring(0, beginner.indexOf(">") + 1);
    		String trimmedImages = trimImageTag(imageRemover);
    		images += trimmedImages + System.getProperty("line.separator");
    		beginner = beginner.substring(beginner.indexOf(">"));
    		withoutImages = withoutImages.replace(imageRemover, "");
    		imageCount++;
    	}
    	images += imageCount;
    	images = images.replace("Images", "images");
    	return withoutImages;
    }
    
    /**
     * Used to removing all unnecessary formatting from strings that locate images
     * @param image full uncleaned image string
     * @return cleaned image string
     */
    public String trimImageTag(String image){
    	String srcFinder = image.substring(image.indexOf("src"));
    	srcFinder = srcFinder.substring(srcFinder.indexOf("\""));
    	image = srcFinder.substring(1);
    	srcFinder = srcFinder.substring(0, image.indexOf("\"") + 2);
    	String altFinder = "";
    	if(image.contains("alt")){
	    	altFinder = image.substring(image.indexOf("alt"));
	    	altFinder = altFinder.substring(altFinder.indexOf("\""));
	    	image = altFinder.substring(1);
	    	altFinder = altFinder.substring(0, image.indexOf("\"") + 2);
    	}
    	image = "<img src=" + srcFinder;
    	if(!altFinder.equals("")){
    		image += " alt=" + altFinder + "/>";
    	}else{
    		image += "/>";
    	}
    	return image;
    }
    
    /**
     * shows the example file unedited.
     * @param f file to be split into strings.
     */
    public void showUneditedExampleFile(File f) {
        this.fileToSplitString(f);
    }
    
    public void clearParsedCardFiles(){
		parsedCardFiles.clear();
	}
    public ArrayList<File> getParsedCardFiles() {
    	System.out.println(parsedCardFiles.get(0).getName());
		return parsedCardFiles;
	}
    
    /**
     * Sets the article up to be a lot cleaner than it would be otherwise.
     * @param toBeCleaned String containing the article.
     * @return
     */
    public String cleanPTags(String toBeCleaned){
    	String[] spaceRemover = toBeCleaned.split("\\s{3}");
    	toBeCleaned = "";
    	for(String part : spaceRemover){
    		toBeCleaned += part;
    	}
    	String[] cleaner = toBeCleaned.split("<p>");
    	toBeCleaned = "";
    	for(String clean : cleaner){
    		clean = clean.trim();
    		if(!clean.trim().isEmpty()){
    			toBeCleaned += System.getProperty("line.separator") + "<p>" + clean;
    		}
    	}
    	toBeCleaned += System.getProperty("line.separator");
    	return toBeCleaned;
    }
    /**
     * remove the first 'x' words, and last 'y' words
     * @param toBeTrimmed untrimmed string 
     * @param x how many words to remove from the front of a string
     * @param y how many words to remove from the back of a string
     * @return
     */
    public String removeFirstLastWords(String toBeTrimmed, Integer x, Integer y){
    	String[] trimmer = toBeTrimmed.split("\\s+");
    	toBeTrimmed = "";
    	for(int i = x; i < (trimmer.length - y); i++){
    		toBeTrimmed += trimmer[i] + " ";
    	}
    	return toBeTrimmed;
    }
    /**
     * loops through all files attempting to save / parse them.
     * @param filePool pool of files to iterate over
     * @param saveLocation location to save these files to
     * @param selectorName 
     * @param deleteFirst number of words to delete from the beginning of each row
     * @param deleteLast  number of words to delete from the end of each row
     * @param selectorTag tags to be appended to each row
     * @param fromWords first tags to start parsing at
     * @param toWords end tags to start parsing at.
     * @return
     */
    public ArrayList<File> saveFiles(ArrayList<File> filePool, File saveLocation, ArrayList<String> selectorName, ArrayList<Integer> deleteFirst, ArrayList<Integer> deleteLast, ArrayList<String> selectorTag, ArrayList<String> fromWords, ArrayList<String> toWords) {
        ArrayList<File> successfulFiles = new ArrayList<File>();
        for (File f : filePool) {
            Boolean hasAllStrings = true;
            String fileString = this.fileToString(f);
            for (String piece2 : fromWords) {
                if (fileString.contains(piece2)) continue;
                hasAllStrings = false;
            }
            for (String piece2 : toWords) {
                if (fileString.contains(piece2)) continue;
                hasAllStrings = false;
            }
            if (!hasAllStrings.booleanValue()) continue;
            String finalString = this.parseFile(f, selectorName, deleteFirst, deleteLast, selectorTag, fromWords, toWords);
            parseInterface.appendToLog("Successfully parsed file: '" + f.getName() + "'");
            publishNewVersion(f,finalString, saveLocation);
            successfulFiles.add(f);
        }
        ViewPanel.clear();
        parseInterface.appendToLog("Successfully parsed: '" + successfulFiles.size() + "' files.");
        return successfulFiles;
    }
    
    /**
     * turns a file into a string so that it can be parsed
     * @param file file to be split into strings
     * @return
     */
    public String fileToString(File file) {
        String fileString = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                fileString = String.valueOf(fileString) + " " + reader.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return fileString;
    }

    /**
     * splits the file into strings to be displayed.
     * @param file
     */
    public void fileToSplitString(File file) {
        String fileString = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                fileString = String.valueOf(fileString) + reader.readLine() + System.getProperty("line.separator");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ViewPanel.clear();
        ViewPanel.append(fileString);
    }

    /**
     * finds the next non empty space in between tags and chooses it.
     * @param s string to be scanned for the next visible text
     * @return
     */
    public String findNextNonEmpty(String s) {
        while (s.length() > 0) {
            if (s.indexOf(">") + 1 > s.indexOf("<")) {
                s = s.substring(s.indexOf("<") + 1);
            }
            String between = s.substring(s.indexOf(">") + 1, s.indexOf("<"));
            if (!(between = between.trim()).equals("")) {
                return between;
            }
            s = s.substring(s.indexOf("<") + 1);
        }
        return "not found";
    }

    /**
     * saves the new version of the chosen file with the string to be written.
     * @param f file to be saved.
     * @param finalString
     */
    public void publishNewVersion(File f, String finalString, File saveLocation) {
        try {
        	if(!images.equals("0") && ! images.equals("")){
        		int imageCount = Integer.parseInt(images.substring(images.lastIndexOf(">") + 1).trim());
        		images = images.substring(0, images.lastIndexOf(">")+2);
        		File nFile = new File(saveLocation + "/" + f.getName().substring(0, f.getName().indexOf("."))
        				+ "_images" + imageCount + ".txt");
                OutputStream stream = new FileOutputStream(nFile);
                OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                writer.write(images);
                writer.close();
        	}
            File nFile = new File(saveLocation + "/" + f.getName().substring(0, f.getName().indexOf(".")) + ".html");
            OutputStream stream = new FileOutputStream(nFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            writer.write(finalString);
            writer.close();
            parsedCardFiles.add(nFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

