package components;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import vbs_sc.ShortcutFactory;


public class Copy {
   private Item item;
   public Copy(Item item) {
      this.item = item;
   }
   public void copyTo(String dir) throws IOException {
      // Start the progress bar
      
      ZipperProgress progress = new ZipperProgress(item);
      progress.progressBar();
      
      new Thread(() -> {
         // Unzip the zip
         String zipLoc = item.getDirectory() + item.getZipName();
         
         String msg = unzip(zipLoc, dir);
         
         if(msg.equals("")) {
            System.out.println("Showing 'Done!'");
            progress.quit();
            JOptionPane.showMessageDialog(null, "Done!");
         } else {
            //
            System.out.println("Showing error");
            
            progress.setToDone();
            JOptionPane.showMessageDialog(null, msg, null, JOptionPane.ERROR_MESSAGE);
            progress.quit();
         }
         
         try {
            ShortcutFactory.createDesktopShortcut(dir + item.getExeLoc(), item.toString() + ".lnk");
         } catch(Exception e) {
            System.out.println("Tried to make a shortcut but failed??" + e);
         }
      }).start();
   }
   
   private static String unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null) {
                String fileName = ze.getName();
                
                // TODO: Send to ZipperProgress log
                // Also initialize zipperProgress inside of here instead of in the main
                // System.out.println("File name: " + fileName);
                
                boolean isFileDirectory = false;
                if(fileName.substring(fileName.length() - 1, fileName.length()).equals("/")) {
                  isFileDirectory = true;
                }
                
                if(!isFileDirectory) {
                   File newFile = new File(destDir + File.separator + fileName);
                   
                   //create directories for sub directories in zip
                   new File(newFile.getParent()).mkdirs();
                   FileOutputStream fos = new FileOutputStream(newFile);
                   
                   int len;
                   while ((len = zis.read(buffer)) > 0) {
                     fos.write(buffer, 0, len);
                   }
                   fos.close();
                   //close this ZipEntry
                   zis.closeEntry();
                }
                ze = zis.getNextEntry();
                
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            
            return "";
        } catch (Exception exeption) {
            System.out.println("Exception!!!: " + exeption);
            return "Error: " + exeption;
        }
        
    }
}