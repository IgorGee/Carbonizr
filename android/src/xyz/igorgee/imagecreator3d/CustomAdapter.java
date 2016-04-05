package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.imagecreatorg3dx.ObjectViewer;

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

            if (model.getModelID() == null) {
                model.setModelID(0);
                model.uploadModel(buy);
            } else {
                model.updateStatus(buy);
            }

        }

        @OnClick(R.id.button_3d_view)
        public void viewIn3D(View view) {
            Model model = models.get(position);
            File previewModel = model.getG3dbLocation();

            if (previewModel.exists()) {
                Intent viewModel = new Intent(context, ObjectViewer.class);
                viewModel.putExtra(ObjectViewer.EXTRA_MODEL_FILE, previewModel);
                context.startActivity(viewModel);
            } else {
                makeAlertDialog(context, "Error", "File not found.");
            }
        }
    }
}
