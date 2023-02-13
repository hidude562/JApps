package components;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// TODO: add support for other apps like ROMS and stuff

public class NewAppWizard extends JPanel {
    private static jApps editJAppsInstance;
    private final JTextField descriptionInput;
    private DefaultListModel listModel;
    private DefaultListModel tempList;
    private final JCheckBox haveNetworkDownload  = new JCheckBox("Network download?", true);
    private final JCheckBox haveUsbDownload  = new JCheckBox("USB download?", true);
    private CheckBoxListener checkboxListener = new CheckBoxListener();
    private JTextField displayNameInput;
    private JTextField usbDownloadInput = new JTextField();
    private JTextField networkDownloadInput = new JTextField();
    private final JTextField zipNameInput = new JTextField();
    private JTextField fileSizeInput = new JTextField();
    private final String usbName = appConfig.USB_NAME;

    //Create and set up the window.
    private static final JFrame frame = new JFrame("Add new app");

    public NewAppWizard() {
        // TODO: Clean up the constructor
        super(new BorderLayout());

        enableTextboxesThatAreSelected();

        try {
            UIManager.setLookAndFeel(appConfig.lookAndFeel);
        } catch (Exception e) {
            System.out.println(e);
        }


        JButton applyButton = new JButton("Add");

        ApplyListener applyListener = new ApplyListener(applyButton);
        applyButton.addActionListener(applyListener);
        haveNetworkDownload.addActionListener(checkboxListener);
        haveUsbDownload.addActionListener(checkboxListener);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(applyButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel contentPanel = new JPanel(new GridLayout(7,1));
        displayNameInput = new JTextField();
        descriptionInput = new JTextField();
        
        contentPanel.add(new JLabel("Display Name")); contentPanel.add(displayNameInput);
        contentPanel.add(new JLabel("Description")); contentPanel.add(descriptionInput);
        contentPanel.add(new JLabel("File Size (MB)")); contentPanel.add(fileSizeInput);

        contentPanel.add(haveNetworkDownload); contentPanel.add(haveUsbDownload);


        // Only show if haveUsbDownload is true
        contentPanel.add(new JLabel("App location on USB")); contentPanel.add(usbDownloadInput);
        contentPanel.add(new JLabel("Zip Name")); contentPanel.add(zipNameInput);

        // Only show if haveNetworkDownload is true
        contentPanel.add(new JLabel("App URL")); contentPanel.add(networkDownloadInput);

        add(contentPanel);
        add(buttonPane, BorderLayout.PAGE_END);
    }


    class ApplyListener implements ActionListener {
        private boolean alreadyEnabled = false;
        private final JButton button;

        public ApplyListener(JButton button) {
            this.button = button;
        }

        // Called when Apply button is pressed
        public void actionPerformed(ActionEvent e) {
            System.out.println("add clicked");
            Item newItem = null;

            // Sadly i can't easily use reflection (without making it less user-friendly) for this so its kinda speghetti
            if(!haveUsbDownload.isSelected() && !haveNetworkDownload.isSelected()) {
                JOptionPane.showMessageDialog(frame, "You can't create an Item without any installation methods!");
            }
            if(haveUsbDownload.isSelected() && !haveNetworkDownload.isSelected()) {
                newItem = new Item(
                   displayNameInput.getText(),
                   "D:/",
                   zipNameInput.getText(),
                   Double.valueOf(fileSizeInput.getText()),
                   usbDownloadInput.getText(),
                   descriptionInput.getText()
                );
            }

            if(!haveUsbDownload.isSelected() && haveNetworkDownload.isSelected()) {
                newItem = new Item(
                        displayNameInput.getText(),
                        "D:/",
                        zipNameInput.getText(),
                        Double.valueOf(fileSizeInput.getText()),
                        networkDownloadInput.getText(),
                        descriptionInput.getText()
                );
            }

            if(haveUsbDownload.isSelected() && haveNetworkDownload.isSelected()) {
                newItem = new Item(
                        displayNameInput.getText(),
                        networkDownloadInput.getText(),
                        "D:/",
                        zipNameInput.getText(),
                        Double.valueOf(fileSizeInput.getText()),
                        usbDownloadInput.getText(),
                        descriptionInput.getText()
                );
            }

            editJAppsInstance.add(newItem);
            jApps.updateConfigJson();
        }
    }

    class CheckBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Hide all dependent checkbox inputs
            System.out.println("checkbox clicked");
            enableTextboxesThatAreSelected();
        }
    }

    // Create GUI
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new NewAppWizard();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void setDependentTextboxesToNotEnabled() {
        usbDownloadInput.setEnabled(false);
        networkDownloadInput.setEnabled(false);
        zipNameInput.setEnabled(false);
    }

    private void enableTextboxesThatAreSelected() {
        setDependentTextboxesToNotEnabled();
        if(haveUsbDownload.isSelected()) {
            usbDownloadInput.setEnabled(true);
            zipNameInput.setEnabled(true);
        }
        if(haveNetworkDownload.isSelected()) {
            networkDownloadInput.setEnabled(true);
        }
    }

    public static void newApp(jApps editJAppsInstance) {
        NewAppWizard.editJAppsInstance = editJAppsInstance;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
