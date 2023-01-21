package components;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.*;
import vbs_sc.ShortcutFactory;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;

public class Copy {
   private Item item;
   public Copy(Item item) {
      this.item = item;
   }
   public void copyTo(String dir) throws IOException {
     
      boolean usbDetected = new File(item.getDirectory()).isFile();
            
      // Start the progress bar
      
      ZipperProgress progress = new ZipperProgress(item);
      progress.progressBar();
      
      new Thread(() -> {
         boolean tempZipCreated = false;
         
         // TODO: Split network download and usb download into seperate methods
         // Download from network only if its the only option or if the usb is not detected
         
         boolean downloadingFromNetwork = false;
         
         if((item.getIfNetworkDownload() && !item.getIfUsbDownload()) || (!usbDetected && item.getIfNetworkDownload())) {
            System.out.println("Downloading from the web..." );
            downloadingFromNetwork = true;
            tempZipCreated = true;
            try {
               URL website = new URL(item.getURL());
               ReadableByteChannel rbc = Channels.newChannel(website.openStream());
               FileOutputStream fos = new FileOutputStream(appConfig.INSTALL_DIRECTORY + appConfig.TEMP_ZIP_NAME);
               fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
               fos.close();
            }
            catch(IOException e) {
               System.out.println(e);
            }
         } else {
            System.out.println("Installing from usb... ");
         }
         
         String zipLoc;
         
         if(downloadingFromNetwork) {
            zipLoc = appConfig.INSTALL_DIRECTORY + appConfig.TEMP_ZIP_NAME;
         } else {
            zipLoc = item.getDirectory() + item.getZipName();
         }
         
         System.out.println(zipLoc);
         String msg = "";
         
         // Check if it is a zip
         if(zipLoc.substring(zipLoc.length() - 3, zipLoc.length()).equals("zip")) {
            System.out.println("Unzipped!");
            msg = unzip(zipLoc, dir);
         } else {
            // TODO for files that aren't a zip
         }
         
         // If a temporary zip is downloaded in the process, delete it here
         if(tempZipCreated) {
            try {
               Files.delete(((new File(zipLoc)).toPath()));
            } catch(IOException e) {
               System.out.println(e);
            }
         }
         
         if(msg.equals("")) {
            progress.quit();
            JOptionPane.showMessageDialog(null, "Done!");
         } else {
            progress.setToDone();
            JOptionPane.showMessageDialog(null, msg, null, JOptionPane.ERROR_MESSAGE);
            progress.quit();
         }
         
         if(item.getType() == 0) {
            try {
               ShortcutFactory.createDesktopShortcut(dir + item.getExeLoc(), item.toString() + ".lnk");
               // Run the app once finished
               System.out.println(dir + item.getExeLoc());
               ProcessBuilder movieProcess = new ProcessBuilder(dir + item.getExeLoc());
               movieProcess.start();
            } catch(Exception e) {
               System.out.println("Tried to make a shortcut but failed??" + e);
            }
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