package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private final ArrayList<Model> items;

    private LayoutInflater inflater;

    class ViewHolder {
        @Bind(R.id.image) ImageView image;
        @Bind(R.id.image_name) TextView textView;
        @Bind(R.id.button_buy) Button buy;
        @Bind(R.id.button_3d_view) Button view3d;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_buy)
        public void buyModel(View view) {
            UIUtilities.makeSnackbar(view, "Added to Cart!");
        }

        @OnClick(R.id.button_3d_view)
        public void viewIn3D(View view) {
            String fileName = (textView).getText().toString();
            File model = new File(HomePageFragment.modelsDirectory + "/" + fileName + "/test.g3db");
            Intent viewModel = new Intent(context, ObjectViewer.class);
            viewModel.putExtra("model", model);
            context.startActivity(viewModel);
        }
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId, ArrayList<Model> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.row, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(items.get(position).getName());

        Bitmap original = items.get(position).getImage();
        Bitmap scaled = null;
        if (original != null) {
            int nh = (int) (original.getHeight() * (512 / original.getWidth()));
            scaled = Bitmap.createScaledBitmap(original, 512, nh, true);
        }
        viewHolder.image.setImageBitmap(scaled);

        return view;
    }
}
