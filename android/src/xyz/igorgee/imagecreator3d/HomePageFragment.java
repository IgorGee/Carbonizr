package xyz.igorgee.imagecreator3d;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.scribejava.core.model.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.shapejs.ShapeJS;
import xyz.igorgee.shapwaysapi.Client;
import xyz.igorgee.utilities.ImageHelper;
import xyz.igorgee.utilities.JavaUtilities;

public class HomePageFragment extends ListFragment {

    private final static int SELECT_PHOTO = 46243;

    @Bind(android.R.id.list) ListView list;
    @Bind(R.id.empty_home_page_text) TextView textView;

    Client client;
    ArrayList<Model> models;
    CustomAdapter adapter;
    Activity thisActivity;
    public static File filesDirectory;
    public static File modelsDirectory;
    ShapeJS shapeJS = new ShapeJS();

    BitmapFactory.Options options;
    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        thisActivity = getActivity();
        filesDirectory = thisActivity.getFilesDir();
        modelsDirectory = new File(filesDirectory + "/models");

        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        initializeClient();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        models = new ArrayList<>();
        adapter = new CustomAdapter(getActivity(), R.layout.row, R.id.image_name, models);
        setListAdapter(adapter);

        checkExistingFiles();
    }

    private void checkExistingFiles() {
        if (modelsDirectory.listFiles() == null) {
            return;
        }
        for (final File file : modelsDirectory.listFiles()) {
            String fileName = file.getName();
            bitmap = ImageHelper.decodeSampledBitmapFromResource(
                    new File(file + "/image.jpg"), 64, 64);
            models.add(new Model(fileName, file, bitmap));
            textView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void initializeClient() {
        client = new Client();
        SharedPreferences preferences = getActivity().
                getSharedPreferences(MainActivity.MY_PREF_NAME, Context.MODE_PRIVATE);

        String accessTokenValue = preferences.getString(MainActivity.ACCESS_TOKEN_VALUE, null);
        String accessTokenSecret = preferences.getString(MainActivity.ACCESS_TOKEN_SECRET, null);

        client.setAccessToken(new Token(accessTokenValue, accessTokenSecret));
        new connectToClient().execute();
    }

    private class connectToClient extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("TEST", client.getCart());

            return null;
        }
    }

    @OnClick(R.id.selectImageButton)
    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath,
                    null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                new GenerateObject(new File(imagePath)).execute();

                cursor.close();

                textView.setVisibility(View.GONE);
            }
        }
    }

    private class GenerateObject extends AsyncTask<Void, Void, Void> {

        File file;
        String filename;
        Model model;
        Bitmap bitmap;

        GenerateObject(File file) {
            this.file = file;
            this.filename = file.getName().substring(0, file.getName().indexOf('.'));

            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;
            FileOutputStream generatedObjectFile = null;
            String zipFileName = filename + ".zip";

            try {
                inputStream = shapeJS.uploadImage(file);
                generatedObjectFile = thisActivity.openFileOutput(zipFileName, Context.MODE_PRIVATE);
                int b;

                while ((b = inputStream.read()) != -1) {
                    generatedObjectFile.write(b);
                }

                JavaUtilities.unzip(new File(filesDirectory + "/" + zipFileName),
                        new File(modelsDirectory + "/" + filename));

                JavaUtilities.copyFile(file, new File(modelsDirectory + "/" + filename + "/" + "image.jpg"));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (generatedObjectFile != null)
                        generatedObjectFile.close();
                } catch (IOException e ) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            model = new Model(filename, new File(modelsDirectory + "/" + filename), bitmap);
            models.add(model);
            adapter.notifyDataSetChanged();
        }
    }
}
