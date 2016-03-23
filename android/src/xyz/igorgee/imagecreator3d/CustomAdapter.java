package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.imagecreatorg3dx.ObjectViewer;
import xyz.igorgee.utilities.UIUtilities;

public class CustomAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Model> models;

    class ViewHolder {
        @Bind(R.id.image_name) TextView textView;
        @Bind(R.id.button_buy) Button buy;
        @Bind(R.id.button_3d_view) Button view3d;

        String previewFilename = textView.getText().toString() + "/test.g3db";
        File model = new File(HomePageFragment.modelsDirectory, previewFilename);

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_buy)
        public void buyModel(View view) {
            UIUtilities.makeSnackbar(view, R.string.add_to_cart_text);
        }

        @OnClick(R.id.button_3d_view)
        public void viewIn3D(View view) {
            Intent viewModel = new Intent(context, ObjectViewer.class);
            viewModel.putExtra(ObjectViewer.EXTRA_MODEL_FILE, model);
            context.startActivity(viewModel);
        }
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<Model> models) {
        super(context, resource, textViewResourceId, models);
        this.context = context;
        this.models = models;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.row, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(models.get(position).getName());

        return view;
    }
}
