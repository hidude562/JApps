package components;

public class Item {
   private String displayName;
   private String dir;
   private float fileSizeMB;
   private String zipName;
   private String exeLoc;
   private String description;
   
   
   public Item(String displayName, String dir, String zipName, float fileSize, String exeLoc) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = "(No description)";
   }
   
   public Item(String displayName, String dir, String zipName, float fileSize, String exeLoc, String description) {
      this.displayName = displayName;
      this.dir = dir;
      this.zipName = zipName;
      this.fileSizeMB = fileSize;
      this.exeLoc = exeLoc;
      this.description = description;
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
   
   public String prettyFileSize() {
      if(fileSizeMB < 0.8) {
         return (fileSizeMB * 1024) + " KB";
      } else if(fileSizeMB < 800) {
         return fileSizeMB + " MB";
      } else {
         return (fileSizeMB / 1024) + " G";
      }
   }
   
   public String getDirectory() {
      return this.dir;
   }
   
   public String getZipName() {
      return zipName;
   }
   
   public String toString() {
      return displayName;
   }
}