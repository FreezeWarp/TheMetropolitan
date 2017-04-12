import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class ViewPanel
extends JPanel {
    private static JTextArea textArea;

    public ViewPanel() {
        this.setBorder(new EtchedBorder(1, null, null));
        this.init();
    }

    public void init() {
        this.setPreferredSize(new Dimension(631, 285));
        this.setBackground(SystemColor.window);
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea(20, 20);
        textArea.setFont(new Font("Times New Roman", 0, 12));
        scrollPane.setViewportView(textArea);
        textArea.setLineWrap(true);
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setWrapStyleWord(true);
        JLabel lblDocumentPreview = new JLabel("Document Preview");
        lblDocumentPreview.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        lblDocumentPreview.setHorizontalAlignment(SwingConstants.CENTER);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
        					.addGap(8))
        				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
        					.addGap(246)
        					.addComponent(lblDocumentPreview, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
        					.addGap(221))))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addComponent(lblDocumentPreview)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
        			.addGap(40))
        );
        this.setLayout(groupLayout);
    }

    public static void append(String s) {
        textArea.append(String.valueOf(s) + "\n");
        textArea.setCaretPosition(0);
    }

    public static void clear() {
        textArea.setText(null);
        textArea.setText("");
    }

    public static String getText() {
        return textArea.getText();
    }
}

