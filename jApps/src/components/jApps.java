package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

import java.nio.file.Path;
import com.google.gson.Gson;


// TODO: Add uninstall feature

public class jApps extends JPanel
        implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;
    private DefaultListModel tempList;
    private final JLabel title;
    private final JLabel description;
    private final JLabel usbDetectedText;

    private boolean usbDetected = false;

    // TODO: Checkbox for network/USB downloads

    private final JCheckBox showApps  = new JCheckBox("Display Apps", true);
    private final JCheckBox showRoms  = new JCheckBox("Display ROMS", true);
    private final JCheckBox showOther = new JCheckBox("Display 'Other'", true);
    private final JTextField search;

    private final String usbName = appConfig.USB_NAME;

    //Create and set up the window.
    private static final JFrame frame = new JFrame("jApps");

    private static final String installString = "Install";

    public jApps() {
        // TODO: Clean up the constructor
        super(new BorderLayout());

        updateList();
        try {
            UIManager.setLookAndFeel(appConfig.lookAndFeel);
        } catch (Exception e) {
            System.out.println(e);
        }

        // Initialize layout
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(15);

        JScrollPane listScrollPane = new JScrollPane(list);

        JButton installButton = new JButton(installString);
        search = new JTextField();

        InstallListener installListener = new InstallListener(installButton);
        installButton.addActionListener(installListener);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        JLabel searchText = new JLabel("Search");
        searchText.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        searchText.setBorder(new EmptyBorder(0,0,0,5));
        buttonPane.add(searchText);
        buttonPane.add(search);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(installButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);


        // Font ideas: Poor Richard
        //             Franklin Gothic Demi

        Font bigFont = new Font("Poor Richard", Font.PLAIN, 40);
        title = new JLabel("");
        title.setVerticalAlignment(JLabel.TOP);

        title.setFont(bigFont);


        description = new JLabel("");
        description.setVerticalAlignment(JLabel.TOP);

        Box box = Box.createVerticalBox();
        splitPane.add(box);

        box.add(title);
        box.add(description);

        box.add(showApps);
        box.add(showRoms);
        box.add(showOther);

        showApps.addActionListener(showListener);
        showRoms.addActionListener(showListener);
        showOther.addActionListener(showListener);


        search.getDocument().addDocumentListener(new SearchListener());

        // Top button pane
        JPanel topButtonPane = new JPanel();
        usbDetectedText = new JLabel();

        JButton refreshButton = new JButton("Refresh");
        RefreshListener refreshListener = new RefreshListener(refreshButton);
        refreshButton.addActionListener(refreshListener);

        JButton optionsButton = new JButton(UIManager.getIcon("FileView.fileIcon"));
        OptionListener optionsListener = new OptionListener();
        optionsButton.addActionListener(optionsListener);

        topButtonPane.setLayout(new BoxLayout(topButtonPane, BoxLayout.LINE_AXIS));

        topButtonPane.add(refreshButton);
        topButtonPane.add(optionsButton);
        topButtonPane.add(usbDetectedText);

        add(listScrollPane, BorderLayout.CENTER);
        add(splitPane, BorderLayout.EAST);
        add(buttonPane, BorderLayout.PAGE_END);
        add(topButtonPane, BorderLayout.PAGE_START);

        checkForUSB();
        sortList();
        updateDescription();

    }

    private final ActionListener showListener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            sortList();
        }
    };

    class SearchListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            sortList();
        }
        public void removeUpdate(DocumentEvent e) {
            sortList();
        }
        public void changedUpdate(DocumentEvent e) {}
    }

    class InstallListener implements ActionListener {
        private final JButton button;

        public InstallListener(JButton button) {
            this.button = button;
        }

        // Called when Install button is pressed
        public void actionPerformed(ActionEvent e) {
            Item item = (Item) list.getSelectedValue();

            if(!confirmDownload(item)) {
                return;
            }

            Copy a = new Copy(item);

            //Do whatever
            try {
                a.copyTo(item.getInstallLoc() + item.toString());
            } catch(Exception IOException) {
                JOptionPane.showMessageDialog(frame, "Error: " + IOException);
            }
        }
    }


    class RefreshListener implements ActionListener {
        private boolean alreadyEnabled = false;
        private final JButton button;

        public RefreshListener(JButton button) {
            this.button = button;
        }

        // Called when Install button is pressed
        public void actionPerformed(ActionEvent e) {
            checkForUSB();
            checkForNewApps();
            sortList();
        }
    }

    class OptionListener implements ActionListener {
        // Called when Install button is pressed
        public void actionPerformed(ActionEvent e) {
            Options.newOptions();
        }
    }

    private void checkForUSB() {
        usbDetected = new File(appConfig.USB_NAME + appConfig.USB_DETECTOR_NAME).isFile();
        System.out.println(usbDetected);
        usbDetectedText.setText("<html><p style=\"padding-left: 5px; padding-right: 5px; color: #9A9A9A\">" + (usbDetected ? "USB detected!" : "No USB found...") + "</p></html>");
    }

    private void checkForNewApps() {
        // This loads apps from the apps.json file in the root directory
        try {
            String content = Files.readString(Path.of(System.getProperty("user.dir") + "/apps.json"));
            Gson gson = new Gson();
            final ArrayList allItems = ((ArrayList) gson.fromJson(content, Map.class).get("apps"));
            for(int i = 0; i < allItems.size(); i++) {
                Item currentItem = Item.convertToItem((ArrayList) allItems.get(i));
                listModel.addElement(currentItem);
            }
        } catch(Exception e) {
            JOptionPane.showOptionDialog(null, e.toString(), "", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        }
    }

    private boolean confirmDownload(Item item) {
        String info = "You are going to install: " + item + "\n\nInfo:\nFile Size: " + item.prettyFileSize() + "\nETA: " + item.eta();
        int quit;
        quit = JOptionPane.showOptionDialog(null, info, item.toString(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        return quit == 0;
    }

    private void updateDescription() {
        // Updates the description when the selection is changed on the list
        try {
            title.setText(list.getSelectedValue().toString());

            Item desc = (Item) (list.getSelectedValue());
            description.setText("<html><p style=\"width:250px\">" + desc.getDescription() + "<br><br>" + "<b style=" + (desc.getIfNetworkDownload() ? "\"color: blue\">Network download avaiable!" : "\"color: #BBBBFF\">No network download..") + "</b><br>" + "<b style=" + (desc.getIfUsbDownload() ? "\"color: red\">USB download avaiable!" : "\"color: #FFBBBB\">No usb download..") + "</b>" + "</b>" + "<br><br> " + "</p></html>");
        } catch(Exception e) {
            title.setText("No apps found");
            description.setText("No apps currently available");
        }
    }

    private void updateList() {
		// Loads list items
		
        listModel = new DefaultListModel();
        tempList = new DefaultListModel();

        // Load apps from json
        checkForNewApps();

        for(int i = 0; i < listModel.size(); i++) {
            tempList.addElement(listModel.get(i));
        }
    }

    private void sortList() {
        // Remove elements in listModel that do not follow the checkboxes criteria
        listModel.removeAllElements();

        for (int i = 0; i < tempList.size(); i++) {
            Item currentItem = ((Item) tempList.get(i));
            byte type = currentItem.getType();

            // Check if search query is in the current item
            // Filter USB items if not detected
            if((currentItem.getIfUsbDownload() && usbDetected) || currentItem.getIfNetworkDownload()) {
                if(currentItem.toString().toUpperCase().contains(search.getText().toUpperCase())) {
                    if (type == 0) {
                        if (showApps.isSelected()) {
                            listModel.addElement(currentItem);
                        }
                    } else if (type == 1) {
                        if (showRoms.isSelected()) {
                            listModel.addElement(currentItem);
                        }
                    } else if (type == 2) {
                        if (showOther.isSelected()) {
                            listModel.addElement(currentItem);
                        }
                    }
                }
            }

        }
        list.setSelectedIndex(0);
    }

    public void valueChanged(ListSelectionEvent e) {
        // TODO: Stop double-updating the description
        try {
            updateDescription();
        } catch(Exception err) {}
    }

    // Create GUI
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new jApps();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
