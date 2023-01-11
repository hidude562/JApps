package components;

public class Item {
   private String displayName;
   private String dir;
   private float fileSizeMB;
   private String zipName;
   private String exeLoc;
   private String description;
   private String installLoc = "C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/Apps/";
   
   // stores if it is a program, game iso, or something else
   private byte type;
   
   // type = 0: Application type
   // type = 1: Roms files
   // type = 2: non-runnable application
   
   
   public Item(String displayName, String dir, String zipName, float fileSize, String exeLoc) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = "(No description)";
      this.type = 0;
   }
   
   public Item(String displayName, String dir, String zipName, float fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
      this.type = 0;
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
   }
   
   public float getFileSize() {
      return fileSizeMB;
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