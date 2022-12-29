package components;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Add uninstall feature
// TODO: Network download if usb not detected

public class jApps extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;
    private DefaultListModel tempList;
    private final JLabel title;
    private final JLabel description;
    
    private final JCheckBox showApps  = new JCheckBox("Display Apps", true);
    private final JCheckBox showRoms  = new JCheckBox("Display ROMS", true);
    private final JCheckBox showOther = new JCheckBox("Display 'Other'", true);
    
    // TODO: Test if the USB letter is the same on other school laptops
    private final String usbName = "D:/";
    
    //Create and set up the window.
    private static final JFrame frame = new JFrame("jApps");

    private static final String installString = "Install";

    public jApps() {
        super(new BorderLayout());
         
        updateList();

        // Initialize layout
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(15);

        JScrollPane listScrollPane = new JScrollPane(list);

        JButton installButton = new JButton(installString);
        InstallListener installListener = new InstallListener(installButton);
        installButton.setActionCommand(installString);
        installButton.addActionListener(installListener);
        installButton.setEnabled(true);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
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
        
        
        add(listScrollPane, BorderLayout.CENTER);
        add(splitPane, BorderLayout.EAST);
        add(buttonPane, BorderLayout.PAGE_END);
        
        updateDescription();
    }

    private final ActionListener showListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        // TODO: remove reduntant items

        // Remove elements in listModel that do not follow the checkboxes criteria

        for(int i = 0; i < listModel.size(); i++) {
            byte type = ((Item) tempList.get(i)).getType();
            listModel.set(i, tempList.get(i));
            if(type == 0) {
               if(!showApps.isSelected()) {
                  listModel.set(i, new Item("", "", "", 0.0f, ""));
               }
            } else if(type == 1) {
               if(!showRoms.isSelected()) {
                  listModel.set(i, new Item("", "", "", 0.0f, ""));
               }
            } else if(type == 2) {
               if(!showOther.isSelected()) {
                  listModel.set(i, new Item("", "", "", 0.0f, ""));
               }
            }

        }
      }
    };

    //This listener is shared by the text field and the install butlton.
    class InstallListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
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
               //progress.setToDone();
            } catch(Exception IOException) {
               JOptionPane.showMessageDialog(frame, "Error: " + IOException);  
            }
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {}
        public void removeUpdate(DocumentEvent e) {}
        public void changedUpdate(DocumentEvent e) {}

    }
    
    public boolean confirmDownload(Item item) {
         String info = "You are going to install: " + item + "\n\nInfo:\nFile Size: " + item.prettyFileSize() + "\nETA: " + item.eta();
         int quit;
         quit = JOptionPane.showOptionDialog(null, info, item.toString(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
         return quit == 0;
    }
    
    public void updateDescription() {
      // Updates the description when the selection is changed on the list
      title.setText(list.getSelectedValue().toString());
      
      Item desc = (Item) (list.getSelectedValue());
      description.setText("<html><p style=\"width:250px\">" + desc.getDescription() + "<br><br></p></html>");
    }
    
    public void updateList() {
        // TODO: Load from JSON file in the future
        // Loads list items
        listModel = new DefaultListModel();
        tempList = new DefaultListModel();
        listModel.addElement(new Item("Blender", usbName, "Blender.zip", 293.5f, "/Blender/blender.exe", "A 3d modeling software"));
        listModel.addElement(new Item("UnCiv", usbName, "UnCiv.zip", 85.24f, "/UnCiv/Unciv.exe", "A civilization building game, similar to the game Civilization 5, if you had heard of that game before. "));
        listModel.addElement(new Item("RetroArch", usbName, "RetroArch.zip", 273f, "/RetroArch/RetroArch-Win64/retroarch.exe", "With this software, you can emulate a lot of consoles, like the NES, PS1, and even the 3DS."));
        listModel.addElement(new Item("Notepad++", usbName, "Notepad++.zip", 3.1f, "/Notepad++/notepad++.exe", "Notepad, but better"));
        listModel.addElement(new Item("Vim", usbName, "Vim.zip", 19f, "/Vim/vim90/vim.exe", "Notepad, but for smart people"));
        
        // TODO: Change directories to be correct
        listModel.addElement(new Item("Process Explorer", usbName, "Process Explorer.zip", 3.1f, "/Process Explorer/procexp64.exe", "Pretty much just Task Manager"));
        listModel.addElement(new Item("InteliJ", usbName, "InteliJ.zip", 830f, "/JetBrains/bin/idea64.exe", "A Java IDE by the company 'JetBrains'"));
        listModel.addElement(new Item("PyCharm", usbName, "PyCharm.zip", 565f, "/Process Explorer/procexp64.exe", "A Python IDE by the company 'JetBrains' (IMPORTANT) You need to install Python from here for this to work!"));
        listModel.addElement(new Item((byte) 2, "Python", usbName, "Python3.10.zip", 14.5f, "You either love it or hate it, Python, (Version 3.10). You need this for Pycharm since the auto-installer will not work. Also installing libraries is really difficult since not only is pip not automatically installed. Not only that but also the package distrubution site is blocked!", "C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/Apps//"));
        
        listModel.addElement(new Item("Dolphin", usbName, "Dolphin-x64.zip", 42.4f, "/Dolphin-x64/Dolphin.exe", "A Nintendo Wii emulator, (IMPORTANT) This does not include any games! There are several games listed here and you can also find the files online.\n\nTo install games, open the app, and click 'Open'. Then navigate to your game file and select it. Then just click the icon and press 'Play' on the menu."));
        listModel.addElement(new Item((byte) 1, "Mario Kart Wii", usbName, "MarioKartWii.zip", 2210f, "(IMPORTANT) You must already have installed Dolphin for this to work (Look at the description for dolphin to see how to use this game file)! Mario Kart Wii, a racing game for the Wii", ""));

        for(int i = 0; i < listModel.size(); i++) {
            tempList.addElement(listModel.get(i));
        }
    }

    public void valueChanged(ListSelectionEvent e) {
      updateDescription();
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