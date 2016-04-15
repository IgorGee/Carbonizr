package xyz.igorgee.imagecreator3d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.floatingactionbutton.FloatingActionsMenu;
import xyz.igorgee.shapejs.ShapeJS;
import xyz.igorgee.shapwaysapi.Client;
import xyz.igorgee.utilities.ImageHelper;
import xyz.igorgee.utilities.JavaUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;
import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;

public class HomePageFragment extends Fragment {

    private final static int SELECT_PHOTO = 46243;
    private final static int TAKE_PICTURE = 7428873;
    private final static String MODELS_DIRECTORY_NAME = "models";

    public static File filesDirectory;
    public static File modelsDirectory;

    @Bind(R.id.empty_home_page_text) TextView textView;
    @Bind(R.id.image_options_fam) FloatingActionsMenu fam;
    @Bind(R.id.list) RecyclerView list;

    public static Client client;
    ArrayList<Model> models;
    CustomAdapter adapter;
    RecyclerView.LayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        filesDirectory = Environment.getExternalStorageDirectory();
        modelsDirectory = new File(filesDirectory, MODELS_DIRECTORY_NAME);
        modelsDirectory.mkdirs();

        list.setHasFixedSize(true);
        linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        list.setLayoutManager(linearLayoutManager);

        models = new ArrayList<>();
        adapter = CustomAdapter.getInstance(getActivity(), models);
        list.setAdapter(adapter);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fam.hide();
                } else {
                    fam.show();
                }
            }
        });

        initializeClient();

        return view;
    }

    public void refresh() {
        adapter.updateList(models);
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
                        models.add(new Model(fileName, directory));
                        textView.setVisibility(View.GONE);
                        refresh();
                    }

            }
        }
    }

    @OnClick(R.id.gallery_fab)
    public void selectImage(View view) {
        Crop.pickImage(getActivity(), this);
        fam.collapse();
    }

    @OnClick(R.id.camera_fab)
    public void takePicture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, TAKE_PICTURE);
        } else {
            makeSnackbar(view, "Sorry you need to have a camera app.");
        }
        fam.collapse();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Crop.REQUEST_PICK || requestCode == TAKE_PICTURE) {
                Uri pickedImage = data.getData();
                String filename = getNameFromUri(getActivity(), pickedImage);

                Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), filename));
                Crop.of(pickedImage, destination).asSquare().start(getActivity(), this);
            } else if (requestCode == Crop.REQUEST_CROP) {
                Uri croppedImage = Crop.getOutput(data);
                File imagePath = new File(croppedImage.getPath());
                new GenerateObject(imagePath, getActivity()).execute();
                textView.setVisibility(View.GONE);
            }
        }
    }

    public String getNameFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        String name;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            File file = new File(cursor.getString(column_index));
            name = file.getName();
            file.delete();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return name;
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
            modelDirectory.mkdirs();
            Log.d("FILELOCATION", modelDirectory.getAbsolutePath());
            Log.d("FILENAMEDATE", modelDirectory.getName());
            bitmap = ImageHelper.decodeSampledBitmapFromResource(file.getAbsoluteFile());
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            FileOutputStream bitmapOutputStream = null;
            File zipFile = new File(modelDirectory, filename + ".zip");

            try {
                inputStream = shapeJS.uploadImage(file);
                outputStream = new FileOutputStream(zipFile);

                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }

                JavaUtilities.unzip(zipFile, modelDirectory);

                for (File file : modelDirectory.listFiles()) {
                    if (file.getName().endsWith(".jpg")) {
                        file.renameTo(new File(modelDirectory, "preview.jpg"));
                    } else {
                        file.renameTo(new File(modelDirectory,
                                filename + file.getName().substring(file.getName().indexOf('.'))));
                    }
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
                makeAlertDialog(context, "Error", "Sorry, something went wrong. Try again in a few minutes.");
            } else {
                final Model model = new Model(filename, modelDirectory);
                models.add(0, model);
                refresh();
            }
        }
    }

    private void deleteAllModels() {
        JavaUtilities.deleteDirectory(modelsDirectory);
        models.clear();
        refresh();
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
            case R.id.cart:
                String urlString="https://shapeways.com/cart";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    this.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    this.startActivity(intent);
                }
                break;
            case R.id.clear_list:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Clear List")
                        .setMessage("Are you sure you want to delete all these images?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllModels();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
