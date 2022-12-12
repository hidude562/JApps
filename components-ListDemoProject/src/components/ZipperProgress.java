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
import java.beans.*;
import java.util.Random;


public class ZipperProgress extends JPanel
                             implements ActionListener, 
                                        PropertyChangeListener {

    private JProgressBar progressBar;
    private JTextArea taskOutput;
    private Task task;
    private final int SPEED;
    private static Item item;
    private boolean isReallyDone = false;
    private int progress = 0;
    
    //Create and set up the window.
    private static JFrame frame;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        
        int progress = 0;
        
        @Override
        public Void doInBackground() {
            Random random = new Random();
            //Initialize progress property
            
            setProgress(0);
            while (progress < 100) {
                try {
                  if(progress < 80) {
                     Thread.sleep(SPEED);
                  } else {
                     Thread.sleep(SPEED * (20 / (100 - progress)));
                  }
                } catch (InterruptedException ignore) {}
                if(progress < 99 && !(isReallyDone)) {
                  progress += 1;
                }
                this.progress = progress;
                
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            taskOutput.append("Done!\n");
        }
    }

    public ZipperProgress(Item item) {
        super(new BorderLayout());
        
        this.item = item;
        SPEED = (int) ((double) item.getFileSize() / 1.4);

        //Create the demo's UI.

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();

    }
    
    // TODO: Remove
    public void actionPerformed(ActionEvent evt){}

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                    "Completed %d%% of task.\n", task.getProgress()));
        } 
    }
    
    public void quit() {
      frame.dispose();
      frame.setVisible(false);
      isReallyDone = true;
    }
    
    public void setToDone() {
      isReallyDone = true;
    }
    
    public int getProgress() {
      return progress;
    }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public static void createAndShowGUI() {
        frame = new JFrame("Installing " + item.toString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ZipperProgress(item);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void progressBar() {
         javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               // TODO: return from progress bar method, not just this silly excuse of a fix
               createAndShowGUI();
            }
        });
    }
}