/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Hanh Nguyen
 */
public class ReadJSON {
    private final ArrayList<String> list;
    private final static String path = "E:/Study/TTTN/piStar/iStar2005_done";
    private final String ACTORS = "actors";
    private JSONObject jsonObject;

    /**
     *
     */
    public ReadJSON() {
        this.list = new ArrayList();
    }

    public JSONObject readJson(String path) throws ParseException {
        
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(path));

            jsonObject = (JSONObject) obj;

        } catch (IOException | ParseException e) {

            System.out.println("ParseException");
        }
        
        return jsonObject;
    }
    // reading folder
    public JSONArray readFolder(String path) {
        
        JSONArray objArray = new JSONArray();
        
        JSONObject obj = null;
        File file = new File(path);

        File[] a = file.listFiles();
        for (File f : a) {
            try {
                obj = readJson(f.toString());
                objArray.add(obj);
            } catch (ParseException ex) {
                Logger.getLogger(ReadJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     
        return objArray;
    }
    
 
}
    