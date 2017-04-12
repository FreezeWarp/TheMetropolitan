import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ParserGUI {
    private static File file;

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("HTML Parser");
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        FileChoosePanel fileFinder = new FileChoosePanel();
        fileFinder.setBackground(SystemColor.activeCaptionText);
        SelectorPanel selectorInput = new SelectorPanel();
        selectorInput.setBackground(SystemColor.window);
        ViewPanel parseView = new ViewPanel();
        parseView.setBackground(SystemColor.window);
        frame.getContentPane().add((Component)selectorInput, "First");
        frame.getContentPane().add((Component)parseView, "Center");
        frame.getContentPane().add((Component)fileFinder, "Last");
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
    }

    public static void main(String ... args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                ParserGUI.createAndShowGUI();
            }
        });
    }

}

