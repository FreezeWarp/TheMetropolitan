
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * GUI for the file handler, contains the log to which program information is appended
 */
public class FileChoosePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static JTextArea log = null;
    private JButton btnOpen;
    private JButton btnEmpty;
    private JButton btnSkip;
    private JButton btnPrev;
    private JButton btnSaveFile;
    private JButton btnReload;

    /**
     * Constructor, calls init
     */
    public FileChoosePanel() {
        this.setBackground(SystemColor.activeCaptionText);
        this.init();
    }

    /**
     * Appends a string to the log for updates on functionality
     * @param string to be appended
     */
    public static void append(String s) {
        log.append(String.valueOf(s) + "\n");
        log.setCaretPosition(log.getText().length());
    }

    /**
     * Initializes the GUI and places buttons appropriately
     * Adds listeners to buttons
     */
    private void init() {
        this.setPreferredSize(new Dimension(800, 219));
        this.setBackground(Color.DARK_GRAY);
        
        JScrollPane scrollPane = new JScrollPane();
        
        log = new JTextArea(20, 20);
        log.append("Please choose a File or Directory, or paste in document \r\n");
       
        //Buttons and their listeners
        btnOpen = new JButton("Open File or Directory");
        btnOpen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                ParserInterface.chooseFiles();
            }
        });
        scrollPane.setViewportView(log);
        log.setEditable(false);

        btnEmpty = new JButton("Empty File Pool");
        btnEmpty.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
            	ParserInterface.emptyPool();
            }
        });

        btnSkip = new JButton("Skip This File");
        btnSkip.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ParserInterface.skipFile();
            }
        });

        btnPrev = new JButton("Next File");
        btnPrev.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ParserInterface.prevFile();
            }
        });

        
        
        btnReload = new JButton("Reload This File");
        btnReload.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ParserInterface.reloadFile();
            }
        });
        
        btnSaveFile = new JButton("Save This File");
        btnSaveFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ParserInterface.chooseSaveLocation();
            }
        });
        
        //Layout code, make it look nice
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnEmpty, 50, 200, Short.MAX_VALUE)
        						.addComponent(btnOpen, 50, 200, Short.MAX_VALUE))
        					.addGap(0, 20, 20)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnReload, 50, 200, Short.MAX_VALUE)
        						.addComponent(btnSkip, 50, 200, Short.MAX_VALUE))
        					.addGap(0, 20, 20)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(btnSaveFile, 50, 200, Short.MAX_VALUE)
                                .addComponent(btnPrev, 50, 200, Short.MAX_VALUE))
                            .addGap(10)
                        )
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(scrollPane)
                                .addGap(10)
                        )
                    )
                )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(scrollPane)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnOpen)
                                        .addComponent(btnReload)
                                        .addComponent(btnSaveFile))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnEmpty)
                                        .addComponent(btnSkip)
                                        .addComponent(btnPrev))
                                .addGap(5)
                        )
        );
        this.setLayout(groupLayout);
    }

}

