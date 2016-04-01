package xyz.igorgee.imagecreator3d;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.scribejava.core.model.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.shapejs.ShapeJS;
import xyz.igorgee.shapwaysapi.Client;
import xyz.igorgee.utilities.ImageHelper;
import xyz.igorgee.utilities.JavaUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;

public class HomePageFragment extends Fragment {

    private final static int SELECT_PHOTO = 46243;
    private final static String MODELS_DIRECTORY_NAME = "models";

    public static File filesDirectory;
    public static File modelsDirectory;

    @Bind(R.id.empty_home_page_text) TextView textView;
    @Bind(R.id.list) RecyclerView list;

    public static Client client;
    ArrayList<Model> models;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        filesDirectory = getActivity().getFilesDir();
        modelsDirectory = new File(filesDirectory, MODELS_DIRECTORY_NAME);

        list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);

        models = new ArrayList<>();
        adapter = new CustomAdapter(getActivity(), models);
        list.setAdapter(adapter);

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

        checkExistingFiles();
    }

    private void checkExistingFiles() {
        if (modelsDirectory.listFiles() != null) {
            for (final File directory : modelsDirectory.listFiles()) {
                for (final File file : directory.listFiles())
                    if (file.getName().endsWith(".stl")) {
                        String fileName = file.getName().substring(0, file.getName().length() - 4);
                        models.add(new Model(fileName, directory,
                                ImageHelper.decodeSampledBitmapFromResource(
                                        new File(directory, fileName + ".jpg"))
                                )
                        );
                        textView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }

            }
        }
    }

    @OnClick(R.id.selectImageButton)
    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == SELECT_PHOTO) {
                Uri pickedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
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
    }

    private class GenerateObject extends AsyncTask<Void, Void, Void> {

        File file;
        String filename;
        File modelDirectory;
        Bitmap bitmap;
        Context context;
        ShapeJS shapeJS = new ShapeJS();
        boolean error = false;

        GenerateObject(File file, Context context) {
            this.file = file;
            if (file.getName().contains("."))
                filename = file.getName().substring(0, file.getName().indexOf('.'));
            else
                filename = file.getName();
            this.context = context;
            Log.d("FILENAMEDATE", filename);
            String modelDirectoryName = (filename + new Date().toString()).replace(" ", "");
            for (Character c : JavaUtilities.ILLEGAL_CHARACTERS)
                modelDirectoryName = modelDirectoryName.replace(c.toString(), "");
            modelDirectory = new File(modelsDirectory, modelDirectoryName);
            Log.d("FILENAMEDATE", modelDirectory.getName());
            bitmap = ImageHelper.decodeSampledBitmapFromResource(file.getAbsoluteFile());
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            FileOutputStream bitmapOutputStream = null;
            File zipFile = new File(filesDirectory, filename + ".zip");

            try {
                inputStream = shapeJS.uploadImage(file);
                outputStream = context.openFileOutput(zipFile.getName(), Context.MODE_PRIVATE);

                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }

                JavaUtilities.unzip(zipFile, modelDirectory);

                for (File file : modelDirectory.listFiles()) {
                    file.renameTo(new File(modelDirectory,
                            filename + file.getName().substring(file.getName().indexOf('.'))));
                }

                bitmapOutputStream = new FileOutputStream(
                        new File(modelDirectory, filename + ".jpg"));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapOutputStream);

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
                    if (bitmapOutputStream != null)
                        bitmapOutputStream.close();
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
                Model model = new Model(filename, modelDirectory, bitmap);
                models.add(model);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void deleteAllModels() {
        JavaUtilities.deleteDirectory(modelsDirectory);
        models.clear();
        adapter.notifyDataSetChanged();
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homepage_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.clear_list:
                deleteAllModels();
        }

        return super.onOptionsItemSelected(item);
    }
}
