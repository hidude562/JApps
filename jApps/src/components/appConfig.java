// Modify important variables here!

package components;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class appConfig {

    public static String INSTALL_DIRECTORY;
    public static String USB_NAME;
    public static String USB_DETECTOR_NAME;
    public static String LOOK_AND_FEEL;
    public static String APP_NAME;
    public static String APPS_CONFIG;
    public static String GENERAL_CONFIG = "generalConfig.json";
    public static String TEMP_ZIP_NAME;

    // Static initilizer that loads from a json
    static {
        String content = null;
        try {
            content = Files.readString(Path.of(System.getProperty("user.dir") + "/" + appConfig.GENERAL_CONFIG));
        } catch (IOException e) {
            System.out.println(e);
        }
        Gson gson = new Gson();
        final Map allConfig = (Map) gson.fromJson(content, Map.class).get("config");

        INSTALL_DIRECTORY = (String) allConfig.get("INSTALL_DIRECTORY");
        USB_NAME = (String) allConfig.get("USB_NAME");
        USB_DETECTOR_NAME = (String) allConfig.get("USB_DETECTOR_NAME");
        LOOK_AND_FEEL = (String) allConfig.get("LOOK_AND_FEEL");
        APP_NAME = (String) allConfig.get("APP_NAME");
        APPS_CONFIG = (String) allConfig.get("APPS_CONFIG");
        TEMP_ZIP_NAME = (String) allConfig.get("TEMP_ZIP_NAME");
    }
    public static String getAsJson() {
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
}