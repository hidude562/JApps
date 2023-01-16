package components;

// TODO: Abstract class

public class Item {
   private String displayName;
   private String dir;
   private float fileSizeMB;
   private String zipName;
   private String exeLoc;
   private String description;
   private String installLoc = "C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/Apps/";
   private boolean networkDownload = false;
   private boolean usbDownload = true;
   private String urlDownload = "";
   
   // stores if it is a program, game iso, or something else
   private byte type;
   
   // type = 0: Application type
   // type = 1: Roms files
   // type = 2: non-runnable application
   
   public Item(String displayName, String dir, String zipName, float fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
      this.type = 0;
      autoDetect();
   }
   
   
   // For other types of files
   public Item(byte type, String displayName, String dir, String zipName, float fileSize, String description, String wildcard) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = null;
      this.description = description;
      this.type = type;
      if(type == 0) {
         System.out.println("WARNING: You set the type to be an application but you haven't set an exe location!");
      }
      if(type == 1) {
         this.installLoc = "C:/Users/nathan.mills/Desktop/Games/";
      }
      autoDetect();
   }
   
   
   public Item(String displayName, String networkDir, String dir, String zipName, float fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
      this.type = 0;
      autoDetect();
   }
   
   
   private void autoDetect() {
      if(dir.length() > 4) {
         if(dir.substring(0, 4).equals("http")) {
            networkDownload = true;
            usbDownload = false;
            urlDownload = dir;
            dir = "C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/";
            zipName = "temp.zip";
         }
      }
   }
   
   public float getFileSize() {
      return fileSizeMB;
   }
   public boolean getIfNetworkDownload() {
      return networkDownload;
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
   
   public String getInstallLoc() {
      return installLoc;
   }
   
   public String toString() {
      return displayName;
   }
}