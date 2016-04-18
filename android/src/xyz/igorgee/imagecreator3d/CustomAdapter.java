package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.imagecreatorg3dx.ObjectViewer;

import static xyz.igorgee.utilities.UIUtilities.hideKeyboard;
import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;
import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;
import static xyz.igorgee.utilities.UIUtilities.showKeyboard;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static CustomAdapter adapter;
    private final Context context;
    private static ArrayList<Model> models;

    public static CustomAdapter getInstance(Context context, ArrayList<Model> models) {
        if (adapter != null) {
            CustomAdapter.models = models;
        } else {
            adapter = new CustomAdapter(context, models);
        }

        return adapter;
    }

    private CustomAdapter(Context context, ArrayList<Model> models) {
        this.context = context;
        CustomAdapter.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        holder.imageName.setText(models.get(position).getName());
        holder.modelDirectory = models.get(position).getLocation();
        File imageLocation = models.get(position).getImageLocation();
        Picasso.with(context).load(imageLocation).into(holder.imageView);
        Picasso.with(context).load(models.get(position).getPreviewImage()).into(holder.previewImage);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void updateList(ArrayList<Model> models) {
        CustomAdapter.models = models;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_name) TextView imageName;
        @Bind(R.id.edit_text_title) EditText imageNameEditText;
        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.preview_image) ImageView previewImage;
        @Bind(R.id.button_upload_and_buy) ImageView buy;

        int position;
        File modelDirectory;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_upload_and_buy)
        public void buyModel(View view) {
            Model model = models.get(position);

            if (model.getModelID() == null) {
                model.setModelID(0);
                model.uploadModel(buy);
            } else {
                model.updateStatus(buy);
            }

        }

        @OnClick({R.id.button_3d_view, R.id.preview_image})
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

        @OnClick({R.id.button_edit, R.id.image_name})
        public void editTitle(View view) {
            editAndShowKeyboard();
            imageNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        setTextAndHideKeyboard(v);
                    }
                }
            });
        }

        private void editAndShowKeyboard() {
            imageName.setVisibility(View.GONE);
            imageNameEditText.setText(imageName.getText().toString());
            imageNameEditText.setSelection(imageNameEditText.length());
            imageNameEditText.setVisibility(View.VISIBLE);
            imageNameEditText.requestFocus();
            showKeyboard(context, imageNameEditText);
        }

        public void setTextAndHideKeyboard(View v) {
            hideKeyboard(context, v);
            String newName = imageNameEditText.getText().toString();
            imageName.setText(newName);
            imageNameEditText.clearFocus();
            imageNameEditText.setVisibility(View.GONE);
            imageName.setVisibility(View.VISIBLE);
            try {
                models.get(position).setName(newName);
                updateList(models);
            } catch (IOException e) {
                e.printStackTrace();
                makeAlertDialog(context, "Rename Error", "Couldn't rename");
            }
        }

        @OnClick (R.id.button_delete)
        public void deleteModel(View view) {
            //TODO Remove this from the cart as well
            models.get(position).delete();
            models.remove(position);
            updateList(models);
            makeSnackbar(view, "Deleted");
        }
    }
}
