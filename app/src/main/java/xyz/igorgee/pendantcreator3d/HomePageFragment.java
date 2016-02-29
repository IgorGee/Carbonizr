package xyz.igorgee.pendantcreator3d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.scribejava.core.model.Token;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.shapwaysapi.Client;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;
import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;

public class HomePageFragment extends Fragment {

    private final static int SELECT_PHOTO = 46243;

    Client client;
    String imageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        client = new Client();
        SharedPreferences preferences = getActivity().
                getSharedPreferences(MainActivity.MY_PREF_NAME, Context.MODE_PRIVATE);
        
        String accessTokenValue = preferences.getString(MainActivity.ACCESS_TOKEN_VALUE, null);
        String accessTokenSecret = preferences.getString(MainActivity.ACCESS_TOKEN_SECRET, null);

        client.setAccessToken(new Token(accessTokenValue, accessTokenSecret));
        new connectToClient().execute();

        return view;
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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                int height = bitmap.getHeight();
                int width = bitmap.getWidth();

                String info = "Title: " + new File(imagePath).getName() + "\n";
                info += "Height: " + height + "\n" + "Width: " + width;

                makeAlertDialog(getActivity(), info, android.R.drawable.ic_menu_report_image);

                if (height > 2000 || width > 2000) {
                    makeSnackbar(getActivity().findViewById(R.id.rootLayout),
                            "Image too big.\nMax: 2000px x 2000px");
                } else {
                    makeSnackbar(getActivity().findViewById(R.id.rootLayout),
                            new File(imagePath).getName());
                }

                cursor.close();
            }
        }
    }
}
