package xyz.igorgee.utilities;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CreateJson {

    public static JSONObject uploadFile(File file, String filename) {
        JSONObject json = new JSONObject();
        try {
            String encodedFile = Base64.encodeToString(
                    JavaUtilities.loadFileAsBytesArray(file), Base64.DEFAULT);
            String urlEncoded = null;
            try {
                urlEncoded = URLEncoder.encode(encodedFile, "UTF-8")
                        .replace("+", "%20")
                        .replace("*", "%2A")
                        .replace("%7E", "~");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            json.put("file", urlEncoded);
            json.put("fileName", filename);
            json.put("hasRightsToModel", 1);
            json.put("acceptTermsAndConditions", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }
}
