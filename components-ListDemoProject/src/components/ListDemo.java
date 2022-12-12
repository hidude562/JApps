/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class ListDemo extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;
    private JLabel label;
    private JLabel description;
    
    // TODO: Test if letter is the same on other school laptops
    private String usbName = "D:/";
    
    //Create and set up the window.
    private static JFrame frame = new JFrame("Nathan's cool installer");

    private static final String installString = "Install";
    
    // TODO: Remove this
    private JTextField employeeName;

    public ListDemo() {
        super(new BorderLayout());
        
        //installPrompt();
         
        // TODO: Load from JSON file in the future
        listModel = new DefaultListModel();
        //listModel.addElement(new Item("Mario Kart Wii", "C:/Users/nathan.mills/Desktop/mario/", "mk.zip"));
        listModel.addElement(new Item("Blender", "D:/", "Blender.zip", 293.5f, "/Blender/blender.exe", "A 3d modeling software"));
        listModel.addElement(new Item("UnCiv", "D:/", "UnCiv.zip", 85.24f, "/UnCiv/Unciv.exe", "A civilization"));
        listModel.addElement(new Item("RetroArch", "D:/", "RetroArch.zip", 273f, "/RetroArch/RetroArch-Win64/retroarch.exe"));
        listModel.addElement(new Item("Notepad++", "D:/", "Notepad++.zip", 3.1f, "/Notepad++/notepad++.exe"));
        listModel.addElement(new Item("Process Explorer", "D:/", "Process Explorer.zip", 3.1f, "/Process Explorer/procexp64.exe"));
        
        
        Item test;
        
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton installButton = new JButton(installString);
        HireListener installListener = new HireListener(installButton);
        installButton.setActionCommand(installString);
        installButton.addActionListener(installListener);
        installButton.setEnabled(true);

        employeeName = new JTextField(10);
        employeeName.addActionListener(installListener);
        employeeName.getDocument().addDocumentListener(installListener);
        String name = listModel.getElementAt(
                              list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        //buttonPane.add(employeeName);
        buttonPane.add(installButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        
        Font bigFont = new Font("serif", Font.PLAIN, 40);
        label = new JLabel("--------------------");
        label.setVerticalAlignment(JLabel.TOP);
        
        label.setFont(bigFont);
        
        
        description = new JLabel("(no description)");
        description.setVerticalAlignment(JLabel.TOP);
        
        
        splitPane.add(label);
        splitPane.add(description);

        add(listScrollPane, BorderLayout.CENTER);
        add(splitPane, BorderLayout.EAST);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) {

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the install butlton.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        // Called when Install button is clipped.
        public void actionPerformed(ActionEvent e) {
            String name = employeeName.getText();
            Item item = (Item) list.getSelectedValue();
            System.out.println("Installing...");
            
            if(!confirmDownload(item)) {
               return;
            }
            
            Copy a = new Copy(item);
            
            //Do whatever
            try {
               a.copyTo("C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/Apps//" + item.toString());
               System.out.println("DONE!!");
               //progress.setToDone();
            } catch(Exception IOException) {
               JOptionPane.showMessageDialog(frame, "Error: " + IOException);  
            }
        }
        
        // TODO: remove alot of these methods

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (true) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(true);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }
    
    public boolean confirmDownload(Item item) {
         String info = "You are going to install: " + item + "\n\nInfo:\nFile Size: " + item.prettyFileSize();
         int quit = 0;
         quit = JOptionPane.showOptionDialog(null, info, item.toString(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
         return (quit == 0) ? true:false;
    }
    
    public void updateDescription() {
      label.setText(list.getSelectedValue().toString());
      
      Item desc = (Item) (list.getSelectedValue());
      description.setText(desc.getDescription());
    }

    public void valueChanged(ListSelectionEvent e) {
      updateDescription();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ListDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}