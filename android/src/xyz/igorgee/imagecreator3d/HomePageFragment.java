package xyz.igorgee.imagecreator3d;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import xyz.igorgee.utilities.JavaUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;

public class HomePageFragment extends ListFragment {

    private final static int SELECT_PHOTO = 46243;
    private final static String MODELS_DIRECTORY_NAME = "models";

    public static File filesDirectory;
    public static File modelsDirectory;

    @Bind(R.id.empty_home_page_text) TextView textView;

    public static Client client;
    ArrayList<Model> models;
    CustomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        filesDirectory = getActivity().getFilesDir();
        modelsDirectory = new File(filesDirectory, MODELS_DIRECTORY_NAME);

        initializeClient();

        return view;
    }

    private void initializeClient() {
        client = new Client();
        SharedPreferences preferences = getActivity().
                getSharedPreferences(MainActivity.MY_PREF_NAME, Context.MODE_PRIVATE);

        String accessTokenValue = preferences.getString(MainActivity.ACCESS_TOKEN_VALUE, null);
        String accessTokenSecret = preferences.getString(MainActivity.ACCESS_TOKEN_SECRET, null);

        client.setAccessToken(new Token(accessTokenValue, accessTokenSecret));
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
        if (modelsDirectory.listFiles() != null) {
            for (final File file : modelsDirectory.listFiles()) {
                String fileName = file.getName();
                models.add(new Model(fileName, file));
                textView.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
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

                new GenerateObject(new File(imagePath), getActivity()).execute();

                cursor.close();

                textView.setVisibility(View.GONE);
            }
        }
    }

    private class GenerateObject extends AsyncTask<Void, Void, Void> {

        File file;
        String filename;
        File modelDirectory;
        Context context;
        ShapeJS shapeJS = new ShapeJS();
        boolean error = false;

        GenerateObject(File file, Context context) {
            this.file = file;
            this.filename = file.getName().substring(0, file.getName().indexOf('.'));
            this.context = context;
            modelDirectory = new File(modelsDirectory, filename);
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            File zipFile = new File(filesDirectory, filename + ".zip");

            try {
                inputStream = shapeJS.uploadImage(file);
                outputStream = context.openFileOutput(zipFile.getName(), Context.MODE_PRIVATE);

                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }

                JavaUtilities.unzip(zipFile, modelDirectory);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            } finally {
                try {
                    if (!zipFile.delete())
                        Log.e("GENERATEOBJECT", "Zip file wasn't deleted");
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e ) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (error) {
                makeAlertDialog(context, "Sorry, something went wrong. Try again in a few minutes.");
            } else {
                Model model = new Model(filename, new File(modelsDirectory, filename));
                models.add(model);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
