
// Credit: Pyro ()for this script
// This class is used for loading a JSON and returning a JSONObject

package com.JSONReader;

import java.io.*;
import org.json.simple.JSONObject;
import java.lang.String;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSON {
    static JSONObject jsonObject = new JSONObject();
    public static String installLocation = "";
    public static void create(String JSONPath, String path) throws Exception {

        jsonObject.put("Install Location", path);
        try {
            FileWriter file = new FileWriter(JSONPath);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: "+jsonObject);
        setVars();
    }

    public static void checkJSON(String JSONPath) throws Exception {
        File JSONLocation = new File(JSONPath);
        if (JSONLocation.exists()){
            readJSON();
        }else{
            create();
        }
    }

    public static void readJSON(String JSONPath) throws IOException, ParseException {
        File JSONFile = new File(JSONPath);
        JSONParser parser = new JSONParser();
        FileReader jsonReader = new FileReader(JSONFile);
        JSONObject json = (JSONObject) parser.parse(jsonReader);
        installLocation = (String) json.get("Install Location");
    }
    public static void main(String args[]) throws Exception {
        checkJSON();
    }
}