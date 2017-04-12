import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Class that handles the conversion of Docx, doc and HTML files into strings,
 * Wraps text with Html tags
 *
 */
public class DocxParser {
	public void fileToView(File f) {
    	if(f.getAbsolutePath().toLowerCase().endsWith(".docx")){
			try {
				InputStream is = new FileInputStream(f);
				XWPFDocument document = new XWPFDocument(is);
				XWPFWordExtractor doc = new XWPFWordExtractor(document);
				String text = WordExtractor.stripFields(doc.getText());
				ViewPanel.resetTextArea();
	            ViewPanel.append(text);
	            doc.close();
	
			} catch (Throwable e) {
				e.printStackTrace();
			}
    	}
    	
    	if(f.getAbsolutePath().toLowerCase().endsWith(".doc")){
    		try{
    		InputStream is = new FileInputStream(f);
			WordExtractor doc = new WordExtractor(is);
			String text = doc.getText();
			ViewPanel.resetTextArea();
            ViewPanel.append(text);
            doc.close();
    		}catch (Throwable e){
    			e.printStackTrace();
    		}
    	}
    	if(f.getAbsolutePath().toLowerCase().endsWith(".html")){
    		String text = "";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                while (reader.ready()) {
                	text = String.valueOf(text) + " " + reader.readLine() + "\n";
                    
                }
                reader.close();
                ViewPanel.resetTextArea();
                ViewPanel.append(text);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    	}
	}
   
	public void htmlWrapHeader(String open, String close, int start, int end) {
		String selected1 = ViewPanel.getSelectedText();
		if(selected1.contentEquals("")){
			return;
		}
		String docText = ViewPanel.getText();

		String selected2 = "";
		if (open.equals("<article>")) {
			selected2 += "<article>\n\n";
			for (String retval: selected1.split("\n")){
				if (retval.trim().length() == 0)
					selected2 += "\n";
				else if (retval.trim().startsWith("<")) // e.g. lists
					selected2 += (retval + "\n");
				else if (retval.trim().length() < 50)
					selected2 += ("\n<h2>" + retval.trim() + "</h2>\n");
				else
					selected2 += ("<p>" + retval.trim() + "</p>\n");
			}
			selected2 += "\n</article>";
		}
		else if (open.equals("<li>")) {
			selected2 += "<ul>\n";

			for (String retval: selected1.split("\n")){
				if (retval.length() == 0)
					selected2 += "\n";
				else
					selected2 += ("  <li>" + retval.replaceAll("^(—|\\-|\\*)", "") + "</li>\n");
			}

			selected2 += "</ul>";
		}
		else if (open.equals("<header>")) { // Assumes first two lines are author and title, and the longer is the title. Will ignore lines 3+ (e.g. for email). Will add date at end
			String[] selectedsplit = selected1.split("\n");

			selected2 = "<header>\n";
			if (selectedsplit[0].length() > selectedsplit[1].length()) {
				if (selectedsplit[1].toLowerCase().startsWith("by "))
					selectedsplit[1] = selectedsplit[1].substring(3);

				selected2 += "<h1 class=\"articletitle\">" + selectedsplit[0] + "</h1>\n<span class=\"authorname\">" + selectedsplit[1] + "</span>\n";
			}
			else {
				selected2 += "<h1 class=\"articletitle\">" + selectedsplit[1] + "</h1>\n<span class=\"authorname\">" + selectedsplit[0] + "</span>\n";
			}
			selected2 += (close + "\n</header>\n\n");
		}
		else {
			selected2 = open + selected1.trim() + close;
		}

		String docFirst = docText.substring(0, start);
		String docLast = docText.substring(end, docText.length());
		String newDocText = docFirst + selected2 + docLast;
		
		ViewPanel.clear();
		ViewPanel.append(newDocText.trim());
		ViewPanel.setSelection(start, start+selected2.length());
	}

	public void htmlWrapHeader(String string) {
		ViewPanel.insertText(string.trim());
		
	}
}
