package xyz.igorgee.imagecreator3d;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.igorgee.Api.Model.ModelResponse;
import xyz.igorgee.utilities.CreateJson;
import xyz.igorgee.utilities.JavaUtilities;
import xyz.igorgee.utilities.ModelToUpload;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;
import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;


public class Model {
    String name;
    String originalName;
    File location; // Folder that contains the g3db and stl files.
    Integer modelID;

    public Model(String name, File location) {
        this.name = name;
        this.originalName = name;
        this.location = location;
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

    public void uploadModel(final View buyButton) {
        ModelToUpload model = CreateJson.uploadFile(getStlLocation(), getName() + ".stl");
        Call<ModelResponse> call = HomePageFragment.apiService.uploadToShop(model);
        call.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                Log.d("RETROFIT", response.raw().toString());
                ((ImageView) buyButton).setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
                makeSnackbar(buyButton, "Processing started.\nPlease wait 10-20 minutes.",
                        Snackbar.LENGTH_INDEFINITE);
                ModelResponse modelResponse = response.body();
                Log.d("MODELRESPONSE", modelResponse.getUrls().getPublicProductUrl().getAddress());
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                makeAlertDialog(buyButton.getContext(), "Error", "Please make sure you have a stable" +
                        "internet connection. If this problem persists, contact the developer.");
                Log.d("RETROFIT", "Fail");
                t.printStackTrace();
            }
        });
    }

    public void delete() {
        JavaUtilities.deleteDirectory(location);
    }
}
