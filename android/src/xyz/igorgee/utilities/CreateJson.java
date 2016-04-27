package xyz.igorgee.utilities;

import android.util.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CreateJson {

    public static ModelToUpload uploadFile(File file, String filename) {
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
        return new ModelToUpload(urlEncoded, filename, true, true);
    }
}
