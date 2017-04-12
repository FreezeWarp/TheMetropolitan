import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

//Takes a File and adds its contents to a scrollable window in a JPanel
public class CardFilePanel extends JPanel{
	
	private String content;
	private File file;
	private JScrollPane scrollPane;
	private int fileNumber;
	private int totalFiles;
	private String fileName;
	private JTextArea textArea;
	
	//Constructor, fileNum is the number of the current file in the queue, total is the total files in the queue
		//these are so the user knows how many 
		public CardFilePanel(File theFile, int fileNum, int total){
			this.file = theFile;
			this.fileNumber = fileNum;
			this.totalFiles = total;
			this.fileName = file.getName();
			content = fileToSplitString(file);
			//need a method to get content from file and set the "content" variable to set the textArea
			init();
		}

	public void init(){
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane = new JScrollPane();
		
		JLabel label = new JLabel("File Name:");
		
		JLabel lblFileName = new JLabel(fileName);
		
		JLabel lblNumberOfFile = new JLabel();
		lblNumberOfFile.setText(fileNumber + "/" + totalFiles);
		
		lblNumberOfFile.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFileName, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNumberOfFile, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
					.addGap(54))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNumberOfFile, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFileName, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		setLayout(groupLayout);
		
		textArea.setText(content);
		textArea.setCaretPosition(0);
	}
	
	//Return the text of the card for saving
	/**
	 * Return text displayed in the card.
	 * @return
	 */
	public String getText(){
		return textArea.getText();	
	}
	/**
	 * Splits the file into nice easy strings for display purposes.
	 * @param file file to be split into strings
	 * @return
	 */
	public String fileToSplitString(File file) {
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
        return fileString;
    }

	public Component getPanel() {
		// TODO Auto-generated method stub
		return this;
	}
}
