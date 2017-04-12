
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;

public class FileChoosePanel
extends JPanel {
    static JTextArea log = null;
    private JButton btnOpen;
    private JButton btnEmpty;
    private JButton btnSkip;
    private JButton btnRunOnAll;
    private JButton btnSaveFile;
    private JButton btnReload;

    public FileChoosePanel() {
        this.setBackground(SystemColor.activeCaptionText);
        this.init();
    }
	/**
	 * displays information in the activity log
	 * @param s Information to be appended to the activity log
	 */
    public static void append(String s) {
        log.append(String.valueOf(s) + "\n");
        log.setCaretPosition(log.getText().length());
    }
	/**
	 * creates all buttons and prepares them for display purposes.
	 */
    private void init() {
        this.setPreferredSize(new Dimension(613, 219));
        this.setBackground(Color.DARK_GRAY);
        
        JScrollPane scrollPane = new JScrollPane();
        
        JLabel lblLog = new JLabel("Activity Log");
        lblLog.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        lblLog.setForeground(SystemColor.activeCaption);
        lblLog.setHorizontalAlignment(0);
        
        log = new JTextArea(20, 20);
        log.append("Please choose a File or Directory" + System.getProperty("line.separator"));
       
        //Buttons and their listeners
        btnOpen = new JButton("Open File or Directory");
        btnOpen.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnOpen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.chooseFiles();
            }
        });
        btnRunOnAll = new JButton("Run On All Files");
        btnRunOnAll.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnRunOnAll.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.chooseSaveLocation();
                parseInterface.parsePool();
            }
        });
        scrollPane.setViewportView(log);
        log.setEditable(false);
        
        btnEmpty = new JButton("Empty File Pool");
        btnEmpty.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnEmpty.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.emptyFilePool();
            }
        });
        
        btnSkip = new JButton("Skip This File");
        btnSkip.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnSkip.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.skipFile();
            }
        });
        
        
        
        btnReload = new JButton("Reload This File");
        btnReload.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnReload.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.resetExampleFile();
            }
        });
        
        btnSaveFile = new JButton("Save This File");
        btnSaveFile.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnSaveFile.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parseInterface.saveNewVersion();
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
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(btnEmpty, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 142, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(btnReload, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(btnSkip, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
        					.addGap(94)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnRunOnAll, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnSaveFile, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
        					.addGap(21))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(lblLog, GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
        					.addGap(20))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        					.addGap(1))))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(lblLog)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnOpen)
        				.addComponent(btnReload)
        				.addComponent(btnSaveFile))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnEmpty)
        				.addComponent(btnSkip)
        				.addComponent(btnRunOnAll, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
        			.addGap(20))
        );
        this.setLayout(groupLayout);
    }

}

