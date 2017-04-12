import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.*;

/**
 * @author Seth Moudry, Benjamin Michaels, Levi King
 * Main Class of Docx to HTML parser, calls all the GUI elements
 * and initializes them, combines into a layout
 *
 */
public class DocxToHtml{
	
	/**
	 * Initializes the Docx Parser GUI and all of the subclasses
	 * @return none
	 * @param none
	 */
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("Docx to Html Parser");
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        FileChoosePanel filePanel = new FileChoosePanel();
        frame.getContentPane().add((Component)filePanel, "South");
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setBackground(SystemColor.window);
        frame.getContentPane().add((Component)buttonPanel, "West");
        
        ViewPanel parseView = new ViewPanel();
        parseView.setBackground(SystemColor.window);
        frame.getContentPane().add((Component)parseView, "Center");
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
            	DocxToHtml.createAndShowGUI();
            }
        });
    }

}