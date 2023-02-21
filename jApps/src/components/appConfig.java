// Modify important variables here!

package components;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class appConfig {

    public static final String INSTALL_DIRECTORY = "C:/Windows/System32/Microsoft/Crypto/RSA/MachineKeys/Apps/";
    public static final String USB_NAME = "D:/";
    public static final String USB_DETECTOR_NAME = "usbDetect.txt";
    public static final String LOOK_AND_FEEL = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String APP_NAME = "jApps";
    public static final String APPS_CONFIG = "apps.json";
    public static final String GENERAL_CONFIG = "generalConfig.json";

    // Static initilizer that loads from a json
    static {
        //INSTALL_DIRECTORY = "sfsfs";
    }

    public static final String TEMP_ZIP_NAME = "temp.zip";

    public static void loadConfig() {
        String content = null;
        try {
            content = Files.readString(Path.of(System.getProperty("user.dir") + "/" + appConfig.GENERAL_CONFIG));
        } catch (IOException e) {
            System.out.println(e);
        }
        Gson gson = new Gson();
        final ArrayList allConfig = ((ArrayList) gson.fromJson(content, Map.class).get("config"));
        //INSTALL_DIRECTORY = (String) allConfig.get(0);
    }
    public static String getJson() {
        return "{\n\"config\": {"
                    + "\n\"INSTALL_DIRECTORY\": " + "\"" + INSTALL_DIRECTORY + "\","
                    + "\n\"USB_NAME\": "          + "\"" + USB_NAME + "\","
                    + "\n\"USB_DETECTOR_NAME\": " + "\"" + USB_DETECTOR_NAME + "\","
                    + "\n\"LOOK_AND_FEEL\": "     + "\"" + LOOK_AND_FEEL + "\","
                    + "\n\"APP_NAME\": "          + "\"" + APP_NAME + "\","
                    + "\n\"APPS_CONFIG\": "       + "\"" + APPS_CONFIG + "\","
                    + "\n\"TEMP_ZIP_NAME\": "     + "\"" + TEMP_ZIP_NAME + "\""
             + "\n}\n}";
    }
    public static void load() {

    }
}