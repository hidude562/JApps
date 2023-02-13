package components;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.nio.file.Path;
import com.google.gson.Gson;


// TODO: Add uninstall feature

public class appModifier extends JPanel {
    private DefaultListModel listModel;
    private DefaultListModel tempList;

    private final String usbName = appConfig.USB_NAME;
    private jApps appChooser = new jApps(false);

    //Create and set up the window.
    private static final JFrame frame = new JFrame("Edit apps");

    public appModifier() {
        // TODO: Clean up the constructor
        super(new BorderLayout());

        try {
            UIManager.setLookAndFeel(appConfig.lookAndFeel);
        } catch (Exception e) {
            System.out.println(e);
        }


        JButton applyButton = new JButton("Apply");

        ApplyListener applyListener = new ApplyListener(applyButton);
        applyButton.addActionListener(applyListener);

        JPanel bottomButtonPane = new JPanel();
        bottomButtonPane.setLayout(new BoxLayout(bottomButtonPane,
                BoxLayout.LINE_AXIS));
        bottomButtonPane.add(Box.createHorizontalStrut(5));
        bottomButtonPane.add(new JSeparator(SwingConstants.VERTICAL));
        bottomButtonPane.add(Box.createHorizontalStrut(5));
        bottomButtonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel contentPanel = new JPanel();

        JLabel searchText = new JLabel("Search");
        contentPanel.add(appChooser);

        // Font ideas: Poor Richard
        //             Franklin Gothic Demi

        // Top button pane

        JButton newAppButton = new JButton("Add a new app");
        NewAppListener newAppListener = new NewAppListener();
        newAppButton.addActionListener(newAppListener);

        JButton removeAppButton = new JButton("Remove an app");
        RemoveAppListener removeAppListener = new RemoveAppListener();
        removeAppButton.addActionListener(removeAppListener);


        bottomButtonPane.add(newAppButton);
        bottomButtonPane.add(removeAppButton);
        bottomButtonPane.add(applyButton);

        add(contentPanel);
        add(bottomButtonPane, BorderLayout.PAGE_END);
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
            jApps.updateAppJson();
            JOptionPane.showMessageDialog(null, "Restart the app to see your changes");
        }
    }

    class NewAppListener implements ActionListener {
        // Called when new app button is pressed
        public void actionPerformed(ActionEvent e) {
            NewAppWizard.newApp(appChooser);
        }
    }

    class RemoveAppListener implements ActionListener {
        // Called when new app button is pressed
        public void actionPerformed(ActionEvent e) {
            JList list = appChooser.getList();
            int previousIndex = list.getSelectedIndex();
            appChooser.delete(list.getSelectedIndex());
            appChooser.list.setSelectedIndex(previousIndex);
        }
    }

    // Create GUI
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new appModifier();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void newAppModifier() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
