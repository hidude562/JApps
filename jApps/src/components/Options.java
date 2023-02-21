package components;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// TODO: Add uninstall feature

public class Options extends JPanel {
    private DefaultListModel listModel;
    private DefaultListModel tempList;

    private final String usbName = appConfig.USB_NAME;

    //Create and set up the window.
    private static final JFrame frame = new JFrame("Options");

    public Options() {
        // TODO: Clean up the constructor
        super(new BorderLayout());

        try {
            UIManager.setLookAndFeel(appConfig.LOOK_AND_FEEL);
        } catch (Exception e) {
            System.out.println(e);
        }


        JButton applyButton = new JButton("Apply");

        ApplyListener applyListener = new ApplyListener(applyButton);
        applyButton.addActionListener(applyListener);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(applyButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel contentPanel = new JPanel();

        JLabel searchText = new JLabel("Other options...");
        contentPanel.add(searchText);

        // Font ideas: Poor Richard
        //             Franklin Gothic Demi

        // Top button pane
        JPanel topButtonPane = new JPanel();

        JButton appModifierButton = new JButton("Add/remove apps from list");
        AppModifierListener appModifierListener = new AppModifierListener();
        appModifierButton.addActionListener(appModifierListener);


        topButtonPane.add(appModifierButton);

        topButtonPane.setLayout(new BoxLayout(topButtonPane, BoxLayout.LINE_AXIS));

        add(contentPanel);
        add(buttonPane, BorderLayout.PAGE_END);
        add(topButtonPane, BorderLayout.PAGE_START);
    }


    class ApplyListener implements ActionListener {
        private boolean alreadyEnabled = false;
        private final JButton button;

        public ApplyListener(JButton button) {
            this.button = button;
        }

        // Called when Apply button is pressed
        public void actionPerformed(ActionEvent e) {
            System.out.println("apply clicked");
            jApps.updateConfigJson();
        }
    }

    class AppModifierListener implements ActionListener {
        // Called when new app button is pressed
        public void actionPerformed(ActionEvent e) {
            appModifier.newAppModifier();
        }
    }

    // Create GUI
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Options();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void newOptions() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
