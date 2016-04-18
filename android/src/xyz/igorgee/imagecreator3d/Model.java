package xyz.igorgee.imagecreator3d;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.scribejava.core.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xyz.igorgee.shapwaysapi.Materials;
import xyz.igorgee.utilities.JavaUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;
import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;


public class Model {
    String name;
    String originalName;
    File location; // Folder that contains the g3db and stl files.
    Integer modelID;
    ModelStatus status;
    ArrayList<Materials> availableMaterials;

    public Model(String name, File location) {
        this.name = name;
        this.originalName = name;
        this.location = location;
        availableMaterials = new ArrayList<>();
    }

    public String getName() {
        return HomePageFragment.sharedPreferences.getString(location.getName(), name);
    }

    public void setName(String name) throws IOException {
        SharedPreferences.Editor editor = HomePageFragment.sharedPreferences.edit();
        editor.putString(location.getName(), name);
        editor.apply();
    }

    public File getLocation() {
        return location;
    }

    public File getImageLocation() {
        return new File(location, originalName + ".jpg");
    }

    public File getStlLocation() {
        return new File(location, originalName + ".stl");
    }

    public File getG3dbLocation() {
        return new File(location, originalName + ".g3db");
    }

    public File getPreviewImage() {
        return new File(location, "preview.jpg");
    }

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public ArrayList<Materials> getAvailableMaterials() {
        return availableMaterials;
    }

    public void uploadModel(View buyButton) {
        new UploadModelAsyncTask(buyButton).execute();
    }

    public void updateStatus(View buyButton) {
        new CheckIfProcessingAsyncTask(buyButton).execute();
    }

    public void delete() {
        JavaUtilities.deleteDirectory(location);
    }

    class CheckIfProcessingAsyncTask extends AsyncTask<Void, Void, Void> {

        Response response;
        JSONObject json;
        View buyButton;
        ProgressDialog progressDialog;

        public CheckIfProcessingAsyncTask(View buyButton) {

            this.buyButton = buyButton;
            progressDialog = new ProgressDialog(buyButton.getContext());
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Checking status...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = HomePageFragment.client.checkIfProcessing(modelID);
            try {
                json = new JSONObject(response.getBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            String result;
            try {
                result = json.getString("printable");
                Log.d("PRINTABILITY", result);
                status = ModelStatus.getModelStatus(result);

                switch (status) {
                    case PENDING:
                        makeSnackbar(buyButton, "Still processing. Try again in a few minutes.",
                                Snackbar.LENGTH_LONG);
                        break;
                    case SUCCESS:
                        //TODO Actually add it to cart.
                        makeSnackbar(buyButton, "Added to Cart!", Snackbar.LENGTH_SHORT);
                        break;
                    case FAILED:
                        makeAlertDialog(buyButton.getContext(), "Improper model",
                                "Your image didn't pass the initial round of processing. " +
                                        "If this problem persists, contact the developer.");
                        break;
                    case UNKNOWN:
                        makeAlertDialog(buyButton.getContext(), "Spooky Scary Skeletons",
                                "No idea what's going on. Best bet is to try again or contact the" +
                                        "developer");
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class UploadModelAsyncTask extends AsyncTask<Void, Void, Void> {

        JSONObject json;
        Response response;
        View buyButton;
        ProgressDialog progressDialog;

        public UploadModelAsyncTask(View buyButton) {

            this.buyButton = buyButton;
            progressDialog = new ProgressDialog(buyButton.getContext());
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            response = HomePageFragment.client.uploadModel(getStlLocation(), getName() + ".stl");

            if (response.getCode() == 200) {
                try {
                    json = new JSONObject(response.getBody());
                    modelID = json.getInt("modelId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (response.getCode() != 200) {
                makeAlertDialog(buyButton.getContext(), "Error", "Please make sure you have a stable" +
                        "internet connection. If this problem persists, contact the developer.");
                setModelID(null);
            } else {
                setModelID(modelID);
                ((ImageView) buyButton).setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
                makeSnackbar(buyButton, "Processing started.\nPlease wait 10-20 minutes.",
                        Snackbar.LENGTH_INDEFINITE);
            }
        }
    }
}
