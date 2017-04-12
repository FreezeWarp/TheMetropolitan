import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;


/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Class to build the user interface panel containing buttons to
 * parse Docx into Html files
 *
 */
public class ButtonPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtCustom1;
	private JTextField textCustomOpen1;
	private JTextField textCustomClose1;
	private JTextField txtCustom2;
	private JTextField textCustomOpen2;
	private JTextField textCustomClose2;
	private JTextField txtCustom3;
	private JTextField textCustomOpen3;
	private JTextField textCustomClose3;

	
    /**
     * Constructor for Button Panel class, calls init()
     */
    public ButtonPanel() {
        this.setBorder(new EtchedBorder(1, null, null));
        this.init();
    }

    /**
     * Initializes the Button Panel GUI
     * Contains all of the button listeners that call methods to wrap text and undo
     * the previous function
     * @param none
     * @return none
     */
    public void init() {
        this.setPreferredSize(new Dimension(350, 700));
        this.setBackground(SystemColor.window);
        JLabel lblDocumentPreview = new JLabel("Custom HTML Tags");
        //lblDocumentPreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        lblDocumentPreview.setFont(new Font(null, Font.PLAIN, 16));
        lblDocumentPreview.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton btnBold = new JButton("<str>");
        btnBold.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.wrapSelected("<strong>", "</strong>");
        	}
        });
        
        JButton btnWrap1 = new JButton("Wrap!");
        btnWrap1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.wrapSelected(textCustomOpen1.getText(), textCustomClose1.getText());
        	}
        });
        
        JLabel lblTagName = new JLabel("Tag Name:");
        
        txtCustom1 = new JTextField();
        txtCustom1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        txtCustom1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCustom1.setText("Custom 1: Title");
        txtCustom1.setColumns(10);
        
        JLabel lblTagOpen = new JLabel("Tag Open:");
        
        textCustomOpen1 = new JTextField();
        textCustomOpen1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomOpen1.setText("<h1 class=\"articletitle\">");
        textCustomOpen1.setHorizontalAlignment(SwingConstants.CENTER);
        textCustomOpen1.setColumns(10);
        
        JLabel lblTagClose = new JLabel("Tag Close:");
        
        textCustomClose1 = new JTextField();
        textCustomClose1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomClose1.setText("</h1>");
        textCustomClose1.setColumns(10);
        
        JLabel lblCommonTags = new JLabel("Common Tags");
        lblCommonTags.setHorizontalAlignment(SwingConstants.CENTER);
        lblCommonTags.setFont(new Font(null, Font.PLAIN, 16));
        
        JLabel lblBold = new JLabel("Bold:");
        lblBold.setFont(new Font(null, Font.PLAIN, 14));
        
        JLabel lblParagraph = new JLabel("List:");
        lblParagraph.setFont(new Font(null, Font.PLAIN, 14));
        
        JButton button = new JButton("<li>");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.wrapSelected("<li>", "</li>");
        	}
        });
        
        JLabel label = new JLabel("Tag Name:");
        
        txtCustom2 = new JTextField();
        txtCustom2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        txtCustom2.setText("Custom 2: Author");
        txtCustom2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCustom2.setColumns(10);
        
        JLabel label_1 = new JLabel("Tag Open:");
        
        textCustomOpen2 = new JTextField();
        textCustomOpen2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomOpen2.setText("<span class=\"authorname\">");
        textCustomOpen2.setHorizontalAlignment(SwingConstants.CENTER);
        textCustomOpen2.setColumns(10);
        
        JLabel label_2 = new JLabel("Tag Close:");
        
        textCustomClose2 = new JTextField();
        textCustomClose2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomClose2.setText("</span>");
        textCustomClose2.setColumns(10);
        
        JButton btnWrap2 = new JButton("Wrap!");
        btnWrap2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            		ParserInterface.wrapSelected(textCustomOpen2.getText(), textCustomClose2.getText());
        	}
        });
        JButton btnWrap3 = new JButton("Wrap!");
        btnWrap3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            		ParserInterface.wrapSelected(textCustomOpen3.getText(), textCustomClose3.getText());
        	}
        });
        
        JLabel label_3 = new JLabel("Tag Name:");
        
        txtCustom3 = new JTextField();
        txtCustom3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        txtCustom3.setText("Custom 3: Date");
        txtCustom3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCustom3.setColumns(10);
        
        JLabel label_4 = new JLabel("Tag Open:");
        
        textCustomOpen3 = new JTextField();
        textCustomOpen3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomOpen3.setText("<header>");
        textCustomOpen3.setHorizontalAlignment(SwingConstants.CENTER);
        textCustomOpen3.setColumns(10);
        
        JLabel label_5 = new JLabel("Tag Close:");
        
        textCustomClose3 = new JTextField();
        textCustomClose3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textCustomClose3.setText("<span class=\"issuedate\">April 2016</span>");
        textCustomClose3.setColumns(10);
        
        JButton button_1 = new JButton("<article>");
        button_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.wrapSelected("<article>", "</article>");
    	}
    	});
        
        JLabel lblArticle = new JLabel("Article:");
        lblArticle.setFont(new Font(null, Font.PLAIN, 14));
        
        JLabel lblImagesInsert = new JLabel("Images Insert:");
        lblImagesInsert.setFont(new Font(null, Font.PLAIN, 14));
        
        JButton button_2 = new JButton("<img...>");
        button_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.wrapSelected("<div id=\"slider\" data-caption=\"\">\n"
        				+ "<img src=\"images/MMYYYYNUM-1.jpg\" alt=\"\" />\n"
        				+ "<img src=\"images/MMYYYYNUM-2.jpg\" alt=\"\" />\n"
        				+ "</div>\n");
        	}
        });
        
        JButton btnUndo = new JButton("Undo");
        btnUndo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ParserInterface.undoLast();
        	}
        });
        
        
        
        
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(39)
        			.addComponent(lblDocumentPreview, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
        			.addGap(49))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblTagName)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(txtCustom1, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblTagOpen, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textCustomOpen1, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblTagClose, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textCustomClose1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnWrap1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(55, Short.MAX_VALUE))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(label, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        					.addGap(6)
        					.addComponent(txtCustom2, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textCustomOpen2, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        					.addGap(6)
        					.addComponent(textCustomClose2, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
        					.addGap(6)
        					.addComponent(btnWrap2, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap(55, Short.MAX_VALUE)
        			.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        			.addGap(6)
        			.addComponent(txtCustom3, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textCustomOpen3, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(32)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(groupLayout.createSequentialGroup()
        							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(lblParagraph, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
        								.addComponent(lblBold, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
        								.addComponent(lblArticle, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
        							.addGap(50)
        							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE)
        								.addComponent(btnBold, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
        								.addComponent(button, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)))
        						.addGroup(groupLayout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblImagesInsert, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        								.addComponent(btnUndo, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
        								.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(label_5, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
        					.addGap(6)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblCommonTags, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
        						.addGroup(groupLayout.createSequentialGroup()
        							.addComponent(textCustomClose3, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
        							.addGap(6)
        							.addComponent(btnWrap3, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))))
        			.addGap(55))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblDocumentPreview)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTagName)
        				.addComponent(txtCustom1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTagOpen)
        				.addComponent(textCustomOpen1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(13)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textCustomClose1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblTagClose)
        				.addComponent(btnWrap1))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(5)
        					.addComponent(label))
        				.addComponent(txtCustom2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(11)
        					.addComponent(label_1))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(6)
        					.addComponent(textCustomOpen2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(13)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(5)
        					.addComponent(label_2))
        				.addComponent(textCustomClose2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnWrap2))
        			.addGap(18)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(5)
        					.addComponent(label_3))
        				.addComponent(txtCustom3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(17)
        					.addComponent(label_4))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(textCustomOpen3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(btnWrap3)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(5)
        					.addComponent(label_5))
        				.addComponent(textCustomClose3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(lblCommonTags)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(button)
        				.addComponent(lblParagraph, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnBold)
        				.addComponent(lblBold))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(button_1)
        				.addComponent(lblArticle, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(button_2)
        				.addComponent(lblImagesInsert, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnUndo)
        			.addContainerGap(140, Short.MAX_VALUE))
        );
        this.setLayout(groupLayout);
    }
}

