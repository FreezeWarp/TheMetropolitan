import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CardFrameView {
	private static ArrayList<File> fileList = new ArrayList<File>();
	
	private static JPanel contentPane = new JPanel();
	private static JFrame frame;
	
	public CardFrameView(ArrayList<File> theFileList){
		this.fileList = theFileList;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the GUI with all generated cards and displays it.
	 * @throws IOException
	 */
	public static void createAndShowGUI() throws IOException {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1200,800);
		contentPane.setLayout(new CardLayout());
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.setBackground(Color.DARK_GRAY);
		final JButton previousButton = new JButton("PREVIOUS PAGE");
		previousButton.setBackground(Color.WHITE);
		previousButton.setForeground(Color.BLACK);
		buttonPanel.add(previousButton);
		previousButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.previous(contentPane);
			}
		});
		final JButton nextButton = new JButton("NEXT PAGE");
		buttonPanel.add(nextButton);

		nextButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.next(contentPane);   
			}
		});

		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		frame.setVisible(true);
		//need to make the frame go to the right of the other frame somehow...
		//frame.setLocationRelativeTo();
		addCards(fileList);

	}

	/**
	 * Adds all cards to the displayed list.
	 * @param fileList list of files to be turned into card panels
	 */
	public static void addCards(ArrayList<File> fileList){
		int fileListSize = fileList.size();
		for(int i = 0; i<fileList.size(); i++){
			CardFilePanel newFile = new CardFilePanel(fileList.remove(0), i+1, fileListSize);
			contentPane.add(newFile, "File" + i);
		}
		//System.out.println("flow goes to the adding of cards");
		contentPane.revalidate();
		contentPane.validate();
		frame.validate();
	}
}

