package components;
import java.lang.reflect.Constructor;

import java.util.ArrayList;
// TODO: Abstract class

public class Item {
   private String displayName;
   private String dir;
   private double fileSizeMB;
   private String zipName;
   private String exeLoc;
   private String description;

   private String installLoc = appConfig.INSTALL_DIRECTORY;
   private boolean networkDownload = false;
   private boolean usbDownload = true;
   private String urlDownload = "";
   private String asJson = "";
   
   // stores if it is a program, game iso, or something else
   private byte type;
   
   // type = 0: Application type
   // type = 1: Roms files
   // type = 2: non-runnable application
   
   
   // Constructors for network OR USB downloads
   
   
   public Item(String displayName, String dir, String zipName, Double fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
      this.type = 0;
      autoDetect();

      asJson =  "["
              + "\"" + displayName + "\","
              + "\"" + dir + "\","
              + "\"" + zipName + "\","
              + fileSize + ","
              + "\"" + exeLoc + "\","
              + "\"" + description.replaceAll("\n","\\n") + "\""
              + "]";
   }
   
   // TODO: Merge the above and below constructor into one
   
   // For other types of files
   // This reads type as a double for JSON compatibility
   public Item(Double type, String displayName, String dir, String zipName, Double fileSize, String description, String wildcard) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = null;
      this.description = description;
      this.type = (byte) type.intValue();
      if (type == 0) {
         System.out.println("WARNING: You set the type to be an application but you haven't set an exe location!");
      }
      if (type == 1) {
         this.installLoc = System.getProperty("user.home") + "/Desktop/Games/";
      }
      autoDetect();

      // TODO: do via reflection
      asJson =  "["
              +  type + ","
              + "\"" + displayName + "\","
              + "\"" + dir + "\","
              + "\"" + zipName + "\","
              +  fileSize + ","
              + "\"" + description.replaceAll("\n","\\n") + "\","
              + "\"" + wildcard + "\""
              + "]";
   }
   
   
   
   // Constructors for network AND USB downloads
   
   public Item(String displayName, String urlDownload, String dir, String zipName, Double fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.urlDownload = urlDownload;
      
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
      this.type = 0;
      
      networkDownload = true;
      usbDownload     = true;

      asJson =  "["
              + "\"" +  displayName + "\","
              + "\"" + urlDownload + "\","
              + "\"" + dir + "\","
              + "\"" + zipName + "\","
              + fileSize + ","
              + "\"" + exeLoc + "\","
              + "\"" + description.replaceAll("\n","\\n") + "\""
              + "]";
   }

   public Item() {}

   public static Item convertToItem(ArrayList init) {
      // Use the magic of reflection to programatically call a constructor based off an Arraylist
      try {
         Class<?> cl = Class.forName("components.Item");

         // Copy all the types of init to a Class type
         Class<?>[] type = new Class[init.size()];
         for(int i = 0; i < init.size(); i++)
            type[i] = init.get(i).getClass();

         // Find a constructor that has these specific types
         Constructor<?> cons = cl.getConstructor(type);

         // Copy init to an Object array
         Object[] obj = new Object[init.size()];
         for(int i = 0; i < init.size(); i++)
            obj[i] = init.get(i);

         // Finally instance the constructor
         Item newItem = (Item) cons.newInstance(obj);
         return newItem;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

  
   
   private void autoDetect() {
      if(dir.length() > 4) {
         if(dir.substring(0, 4).equals("http")) {
            networkDownload = true;
            usbDownload = false;
            urlDownload = dir;
            dir = appConfig.INSTALL_DIRECTORY;
            zipName = appConfig.TEMP_ZIP_NAME;
         }
      }
   }
   
   
   public double getFileSize() {
      return fileSizeMB;
   }
   public boolean getIfNetworkDownload() {
      return networkDownload;
   }
   
   public boolean getIfUsbDownload() {
      return usbDownload;
   }
   
   public String getExeLoc() {
      return exeLoc;
   }
   
   public String getDescription() {
      return description;
   }
   
   public byte getType() {
      return type;
   }
   
   public String getURL() {
      return urlDownload;
   }
   
   public String prettyFileSize() {
      if(fileSizeMB < 0.8) {
         return (fileSizeMB * 1000) + " KB";
      } else if(fileSizeMB < 1000) {
         return fileSizeMB + " MB";
      } else {
         return (fileSizeMB / 1000) + " G";
      }
   }
  
   public String getDirectory() {
      return this.dir;
   }
   
   public String eta() {
      double time = Math.round(fileSizeMB / 0.8 / 10);
      
      if(time > 60) {
         return Math.ceil(time / 60) + " minutes";
      }
      return time + " seconds";
   }
   
   public String getZipName() {
      return zipName;
   }

   public String getAsJson() {
      return asJson;
   }

   public String getInstallLoc() {
      return installLoc;
   }
   
   public String toString() {
      return displayName;
   }
}