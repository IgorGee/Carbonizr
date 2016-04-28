package xyz.igorgee.imagecreator3d;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;

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


public class Model {
    String name;
    String originalName;
    File location; // Folder that contains the g3db and stl files.
    ModelResponse modelResponse;

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

    public void uploadModel(final View buyButton) {
        if (modelResponse != null) {
            String urlString = modelResponse.getUrls().getPublicProductUrl().getAddress();
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.android.chrome");
            try {
                buyButton.getContext().startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // Chrome browser presumably not installed so allow user to choose instead
                intent.setPackage(null);
                buyButton.getContext().startActivity(intent);
            }
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(buyButton.getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Carbonizing...");
            progressDialog.show();

            ModelToUpload model = CreateJson.uploadFile(getStlLocation(), getName() + ".stl");
            final Call<ModelResponse> call = HomePageFragment.apiService.uploadToShop(model);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    call.cancel();
                }
            });

            call.enqueue(new Callback<ModelResponse>() {
                @Override
                public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                    progressDialog.dismiss();
                    Log.d("RETROFIT", response.raw().toString());
                    modelResponse = response.body();
                    String urlString = modelResponse.getUrls().getPublicProductUrl().getAddress();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        buyButton.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        buyButton.getContext().startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ModelResponse> call, Throwable t) {
                    if (!call.isCanceled())
                        makeAlertDialog(buyButton.getContext(), "Error", "Please make sure you have a stable" +
                                "internet connection. If this problem persists, contact the developer.");
                    Log.d("RETROFIT", "Fail");
                    t.printStackTrace();
                }
            });
        }
    }

    public void delete() {
        JavaUtilities.deleteDirectory(location);
    }
}
