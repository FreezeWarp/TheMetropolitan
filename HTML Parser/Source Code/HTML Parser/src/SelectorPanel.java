import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class SelectorPanel
extends JPanel {
    private ArrayList<String> selectorName = new ArrayList<String>();
    private ArrayList<String> selectorTag = new ArrayList<String>();
    private ArrayList<String> fromWords = new ArrayList<String>();
    private ArrayList<String> toWords = new ArrayList<String>();
    private ArrayList<Integer> deleteFirst = new ArrayList<Integer>();
    private ArrayList<Integer> deleteLast = new ArrayList<Integer>();
    private JTextField textField;
    private JTextField delFirst1;
    private JTextField delFirst2;
    private JTextField delFirst3;
    private JTextField delFirst4;
    private JTextField delLast1;
    private JTextField delLast2;
    private JTextField delLast3;
    private JTextField delLast4;
    private JLabel lblHtml;
    private JTextField textLabel1;
    private JTextField textLabel2;
    private JTextField textLabel3;
    private JTextField textLabel4;
    private JButton btnParseHtml;
    private JTextField textStartTag4;
    private JTextField textEndTag4;
    private JLabel lblStart;
    private JLabel lblEnd;
    private JTextField textStartTag1;
    private JTextField textStartTag2;
    private JTextField textStartTag3;
    private JTextField textEndTag1;
    private JTextField textEndTag2;
    private JTextField textEndTag3;

    public SelectorPanel() {
        this.setBorder(new EtchedBorder(1, null, null));
        this.init();
    }

    private void init() {
        this.setPreferredSize(new Dimension(674, 240));
        this.setBackground(Color.WHITE);
        JTextArea banner = new JTextArea();
        banner.setForeground(Color.BLACK);
        banner.setBackground(SystemColor.window);
        banner.setWrapStyleWord(true);
        banner.setFont(new Font("Lucida Grande", 0, 18));
        banner.setText("Enter The Tags You Want:\n");
        banner.setLineWrap(true);
        this.delFirst1 = new JTextField();
        this.delFirst1.setText("0");
        this.delFirst1.setColumns(10);
        this.delFirst2 = new JTextField();
        this.delFirst2.setText("0");
        this.delFirst2.setColumns(10);
        this.delFirst3 = new JTextField();
        this.delFirst3.setText("0");
        this.delFirst3.setColumns(10);
        this.delFirst4 = new JTextField();
        this.delFirst4.setText("0");
        this.delFirst4.setColumns(10);
        this.delLast1 = new JTextField();
        this.delLast1.setText("0");
        this.delLast1.setColumns(10);
        this.delLast2 = new JTextField();
        this.delLast2.setText("0");
        this.delLast2.setColumns(10);
        this.delLast3 = new JTextField();
        this.delLast3.setText("0");
        this.delLast3.setColumns(10);
        this.delLast4 = new JTextField();
        this.delLast4.setText("0");
        this.delLast4.setColumns(10);
        JLabel lblDelFirst = new JLabel("-words first last\n");
        lblDelFirst.setFont(new Font("Lucida Grande", 0, 12));
        lblDelFirst.setForeground(Color.BLACK);
        lblDelFirst.setBackground(Color.WHITE);
        this.lblHtml = new JLabel("HTML Label");
        this.lblHtml.setFont(new Font("Lucida Grande", 0, 12));
        this.lblHtml.setForeground(Color.BLACK);
        this.lblHtml.setBackground(Color.WHITE);
        this.textLabel1 = new JTextField();
        this.textLabel1.setText("h1 class=\"articletitle\"");
        this.textLabel1.setColumns(10);
        this.textLabel2 = new JTextField();
        this.textLabel2.setText("span class=\"authorname\"");
        this.textLabel2.setColumns(10);
        this.textLabel3 = new JTextField();
        this.textLabel3.setText("span class=\"issuedate\"");
        this.textLabel3.setColumns(10);
        this.textLabel4 = new JTextField();
        this.textLabel4.setText("article");
        this.textLabel4.setColumns(10);
        this.btnParseHtml = new JButton("Parse HTML");
        this.textStartTag4 = new JTextField("Editable name=\"Article\" -->");
        this.textStartTag4.setColumns(10);
        this.textEndTag4 = new JTextField("<!-- InstanceEndEditable -->");
        this.textEndTag4.setColumns(10);
        this.lblStart = new JLabel("Start Tag");
        this.lblStart.setHorizontalAlignment(0);
        this.lblStart.setForeground(Color.BLACK);
        this.lblStart.setBackground(Color.WHITE);
        this.lblEnd = new JLabel("EndTag\n");
        this.lblEnd.setHorizontalAlignment(0);
        this.lblEnd.setForeground(Color.BLACK);
        this.lblEnd.setBackground(Color.WHITE);
        this.textStartTag1 = new JTextField("Editable name=\"Title\" -->");
        this.textStartTag1.setColumns(10);
        this.textStartTag2 = new JTextField("eginEditable name=\"Author\" -->");
        this.textStartTag2.setColumns(10);
        this.textStartTag3 = new JTextField("table name=\"Month and Date \" -->");
        this.textStartTag3.setColumns(10);
        this.textEndTag1 = new JTextField();
        this.textEndTag1.setColumns(10);
        this.textEndTag2 = new JTextField();
        this.textEndTag2.setColumns(10);
        this.textEndTag3 = new JTextField();
        this.textEndTag3.setColumns(10);
        
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblStart, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
        				.addComponent(textStartTag4, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
        				.addComponent(textStartTag3, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
        				.addComponent(textStartTag2, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
        				.addComponent(textStartTag1, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(textEndTag4, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
        						.addComponent(textEndTag1, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
        						.addComponent(textEndTag3, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
        						.addComponent(textEndTag2, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
        						.addGroup(groupLayout.createSequentialGroup()
        							.addComponent(lblEnd, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        							.addGap(7)))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(delFirst2, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delFirst3, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delFirst1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delFirst4, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
        					.addGap(18)
        					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(delLast1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delLast2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delLast3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        						.addComponent(delLast4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
        					.addComponent(lblDelFirst, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
        			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(4)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(textLabel1, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        						.addComponent(textLabel2, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        						.addComponent(textLabel3, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        						.addComponent(textLabel4, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(5)
        					.addComponent(lblHtml, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)))
        			.addGap(16))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(249)
        			.addComponent(banner, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        			.addGap(191))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap(155, Short.MAX_VALUE)
        			.addComponent(btnParseHtml, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
        			.addGap(260))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(banner, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblDelFirst)
        				.addComponent(lblEnd, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblStart, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblHtml, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delLast1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delFirst1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textStartTag1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textEndTag1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textLabel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delLast2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delFirst2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textStartTag2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textEndTag2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delLast3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delFirst3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textStartTag3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textEndTag3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textLabel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delLast4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(delFirst4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textEndTag4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textStartTag4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnParseHtml))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        this.setLayout(groupLayout);
        this.btnParseHtml.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                SelectorPanel.this.selectorName.clear();
                SelectorPanel.this.deleteFirst.clear();
                SelectorPanel.this.deleteLast.clear();
                SelectorPanel.this.selectorTag.clear();
                SelectorPanel.this.fromWords.clear();
                SelectorPanel.this.toWords.clear();
                SelectorPanel.this.deleteFirst.add(Integer.parseInt(SelectorPanel.this.delFirst1.getText()));
                SelectorPanel.this.deleteFirst.add(Integer.parseInt(SelectorPanel.this.delFirst2.getText()));
                SelectorPanel.this.deleteFirst.add(Integer.parseInt(SelectorPanel.this.delFirst3.getText()));
                SelectorPanel.this.deleteFirst.add(Integer.parseInt(SelectorPanel.this.delFirst4.getText()));
                SelectorPanel.this.deleteLast.add(Integer.parseInt(SelectorPanel.this.delLast1.getText()));
                SelectorPanel.this.deleteLast.add(Integer.parseInt(SelectorPanel.this.delLast2.getText()));
                SelectorPanel.this.deleteLast.add(Integer.parseInt(SelectorPanel.this.delLast3.getText()));
                SelectorPanel.this.deleteLast.add(Integer.parseInt(SelectorPanel.this.delLast4.getText()));
                SelectorPanel.this.selectorTag.add(SelectorPanel.this.textLabel1.getText());
                SelectorPanel.this.selectorTag.add(SelectorPanel.this.textLabel2.getText());
                SelectorPanel.this.selectorTag.add(SelectorPanel.this.textLabel3.getText());
                SelectorPanel.this.selectorTag.add(SelectorPanel.this.textLabel4.getText());
                SelectorPanel.this.fromWords.add(SelectorPanel.this.textStartTag1.getText());
                SelectorPanel.this.fromWords.add(SelectorPanel.this.textStartTag2.getText());
                SelectorPanel.this.fromWords.add(SelectorPanel.this.textStartTag3.getText());
                SelectorPanel.this.fromWords.add(SelectorPanel.this.textStartTag4.getText());
                SelectorPanel.this.toWords.add(SelectorPanel.this.textEndTag1.getText());
                SelectorPanel.this.toWords.add(SelectorPanel.this.textEndTag2.getText());
                SelectorPanel.this.toWords.add(SelectorPanel.this.textEndTag3.getText());
                SelectorPanel.this.toWords.add(SelectorPanel.this.textEndTag4.getText());
                parseInterface.setValues(SelectorPanel.this.selectorName, SelectorPanel.this.deleteFirst, SelectorPanel.this.deleteLast, SelectorPanel.this.selectorTag, SelectorPanel.this.fromWords, SelectorPanel.this.toWords);
                ViewPanel.clear();
                parseInterface.parseExampleFile();
                FileChoosePanel.append("Updated current file with chosen selectors.");
            }
        });
    }
}

