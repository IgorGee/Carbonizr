package xyz.igorgee.shapejs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShapeJS {

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private final OkHttpClient client = new OkHttpClient();

    InputStream uploadImageToMe(File image) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("shapeJS_img", image.getName(),
                        RequestBody.create(MEDIA_TYPE_JPG, image))
                .build();

        Request request = new Request.Builder()
                .url("http://52.90.86.247/image")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    public static void main(String[] args) throws IOException {
        ShapeJS shapeJS = new ShapeJS();

        InputStream inputStream = null;
        FileOutputStream generatedObjectFile = null;

        try {
            inputStream = shapeJS.uploadImageToMe(new File("app/src/main/java/xyz/igorgee/shapejs/key-pendant-kmz.jpg"));
            generatedObjectFile = new FileOutputStream("app/src/main/java/xyz/igorgee/shapejs/generatedObject3D.g3db");
            int b;

            while ((b = inputStream.read()) != -1) {
                generatedObjectFile.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (generatedObjectFile != null)
                generatedObjectFile.close();
        }
    }

}
