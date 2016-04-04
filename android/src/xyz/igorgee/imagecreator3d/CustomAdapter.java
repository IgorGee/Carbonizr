package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.scribejava.core.model.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.imagecreatorg3dx.ObjectViewer;
import xyz.igorgee.utilities.UIUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Model> models;

    public CustomAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        holder.textView.setText(models.get(position).getName());
        holder.modelDirectory = models.get(position).getLocation();
        File imageLocation = models.get(position).getImageLocation();
        Picasso.with(context).load(imageLocation).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_name) TextView textView;
        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.button_buy) ImageButton buy;
        @Bind(R.id.button_3d_view) ImageButton view3d;

        int position;
        File modelDirectory;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_buy)
        public void buyModel(View view) {
            Model model = models.get(position);

            if (model.modelID == null) {
                model.setModelID(0);
                new UploadModelAsyncTask(model).execute();
                UIUtilities.makeSnackbar(view, "Processing model...");
                buy.setBackgroundColor(Color.YELLOW);
            } else {
                UIUtilities.makeSnackbar(view, "Still processing, please be patient.");
            }

        }

        @OnClick(R.id.button_3d_view)
        public void viewIn3D(View view) {
            Model model = models.get(position);
            File previewModel = model.getG3dbLocation();

            if (previewModel.exists()) {
                Log.d("FILES", "Opened: " + previewModel.getAbsolutePath());
                Intent viewModel = new Intent(context, ObjectViewer.class);
                viewModel.putExtra(ObjectViewer.EXTRA_MODEL_FILE, previewModel);
                context.startActivity(viewModel);
            } else {
                makeAlertDialog(context, "File not found.");
                Log.e("FILES", "Tried to open: " + previewModel.getAbsolutePath());
            }
        }

        class UploadModelAsyncTask extends AsyncTask<Void, Void, Void> {

            File uploadModel;
            String baseFileName;
            Model model;
            JSONObject json;
            Response response;
            int modelID;

            UploadModelAsyncTask(Model model) {
                this.model = model;
                this.uploadModel = model.getStlLocation();
                this.baseFileName = model.getName();
            }

            @Override
            protected Void doInBackground(Void... params) {
                
                response = HomePageFragment.client.uploadModel(uploadModel, baseFileName + ".stl");

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
                super.onPostExecute(aVoid);
                if (response.getCode() != 200) {
                    makeAlertDialog(context, "Sorry, there was an error uploading your model." +
                            "Please check that your internet connection is stable.");
                    model.setModelID(null);
                } else {
                    model.setModelID(modelID);
                }
            }
        }
    }
}
