package xyz.igorgee.shapejs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShapeJS {

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private final OkHttpClient client = new OkHttpClient();

    InputStream get3DObject(String jobID) throws IOException{
        Request request = new Request.Builder()
                .url(Constants.SAVE_MODEL_CACHED_ENDPOINT.toString() + "?jobID=" + jobID)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    String uploadImage(File image, String jobID) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("shapeJS_img", image.getName(),
                        RequestBody.create(MEDIA_TYPE_JPG, image))
                .addFormDataPart("jobID", jobID)
                .addFormDataPart("script", Constants.JS_2D_TO_3D.toString())
                .build();

        Request request = new Request.Builder()
                .url(Constants.UPDATE_SCENE_ENDPOINT.toString())
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        ShapeJS shapeJS = new ShapeJS();
        String uuid = UUID.randomUUID().toString();

        InputStream inputStream = null;
        FileOutputStream generatedObjectFile = null;

        try {
            System.out.println(shapeJS.uploadImage(new File("app/src/main/java/xyz/igorgee/shapejs/key-pendant-kmz.jpg"), uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            generatedObjectFile = new FileOutputStream("app/src/main/java/xyz/igorgee/shapejs/generatedObject3D.stl");
            inputStream = shapeJS.get3DObject(uuid);
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
