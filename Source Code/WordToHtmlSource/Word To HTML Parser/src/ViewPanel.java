import java.awt.*;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Class to build the user interface panel to display documents
 *
 */
public class ViewPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private static ArrayList<String> undoList;

	
    /**
     * Constructor class for view panel, runs init
     */
    public ViewPanel() {
        this.setBorder(new EtchedBorder(1, null, null));
        this.init();
        undoList = new ArrayList<String>();
    }

    /**
     * Initializes the GUI components and sets layout for the View Panel
     */
    public void init() {
        // Get the screen size, for relative font sizes.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        // Set dimensions.
        this.setPreferredSize(new Dimension(800, 700));
        this.setBackground(SystemColor.window);
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", 0, (int)(width/120)));
        scrollPane.setViewportView(textArea);
        textArea.setLineWrap(true);
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setWrapStyleWord(true);

        // Document Preview label
        JLabel lblDocumentPreview = new JLabel("Document Preview");
        lblDocumentPreview.setFont(new Font(null, Font.PLAIN, 16));
        lblDocumentPreview.setHorizontalAlignment(SwingConstants.CENTER);
        
        // ...
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(scrollPane, 100, 1000, Short.MAX_VALUE)
        					.addGap(5))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(246)
        					.addComponent(lblDocumentPreview, 100, 323, Short.MAX_VALUE)
        					.addGap(221))))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(lblDocumentPreview)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, 100, 800, Short.MAX_VALUE)
        			.addGap(10))
        );
        this.setLayout(groupLayout);
    }

    /**
     * Method to post a string in the ViewPanel text area
     * @param String to be appended
     * @return none
     */
    public static void append(String s) {
        textArea.append(String.valueOf(s));
        textArea.setCaretPosition(0);
        undoList.add(s);
    }
    
    /**
     * Method to post a string in the ViewPanel text area after an
     * undo action is performed, clears the list of the undone strings
     * @param String to be appended
     * @return none
     */
    public static void undoAppend(String s) {
        textArea.append(String.valueOf(s));
        textArea.setCaretPosition(0);
        undoList.remove(undoList.size()-1);
    }
    
    /**
     * Clears the text area in the View panel
     * @param none
     * @return none
     */
    public static void clear() {
        textArea.setText(null);
        textArea.setText("");
        
    }
    
    /**
     * Clears the text area in the View panel, and initializes the undo
     * arraylist, only called when a document is opened.
     * @param none
     * @return none
     */
    public static void resetTextArea(){
    	undoList = new ArrayList<String>();
    	textArea.setText(null);
        textArea.setText("");
    }
    
    /**
     * Returns the text from the text area in the View panel
     * @param none
     * @return String of the text in the text area in View Panel
     */
    public static String getText() {
        return textArea.getText();
    }
    
    /**
     * Returns the selected text from the text area in the View panel
     * @param none
     * @return String of the text selection in View Panel
     */
    public static String getSelectedText() {
        return textArea.getSelectedText();
    }
    
    /**
     * Returns the integer value of the beginning of the text selection
     * @param none
     * @return Integer value of the beginning of selection
     */
    public static int getSelectionStart(){
    	return textArea.getSelectionStart();
    }
    
    /**
     * Returns the integer value of the beginning of the text selection
     * @param none
     * @return Integer value of the end of selection
     */
    public static int getSelectionEnd(){
    	return textArea.getSelectionEnd();
    }
    
    /**
     * Inserts text at the current cursor position
     * @param none
     * @return Integer value of the beginning of selection
     */
    public static void insertText(String string) {
    	textArea.insert(string, textArea.getCaretPosition());
    }

    /**
     * Sets the selected text in the View Panel
     * @param none
     * @return none
     */
	public static void setSelection(int start, int end) {
		textArea.setCaretPosition(0);
		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(end);
		
	}

	/**
     * Resets the text as the last version saved before the update
     * @param none
     * @return none
     */
	public static void undoLast() {
		if(undoList.size()>1){
			clear();
			undoAppend(undoList.get(undoList.size()-2));
			FileChoosePanel.append("Last action undone");
		}
		else
			FileChoosePanel.append("Nothing to undo");
	}
    
}

