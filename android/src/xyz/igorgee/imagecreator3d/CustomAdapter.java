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

import xyz.igorgee.imagecreatorg3dx.ObjectViewer;
import xyz.igorgee.utilities.UIUtilities;

public class CustomAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<String> items;

    public CustomAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        final TextView textView = (TextView) rowView.findViewById(R.id.image_name);
        Button buy = (Button) rowView.findViewById(R.id.button_buy);
        Button view3d = (Button) rowView.findViewById(R.id.button_3d_view);

        textView.setText(items.get(position));

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtilities.makeSnackbar(v, "Added to Cart!");
            }
        });

        view3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = (textView).getText().toString();
                File model = new File(HomePageFragment.modelsDirectory + "/" + fileName + "/test.g3db");
                Intent viewModel = new Intent(context, ObjectViewer.class);
                viewModel.putExtra("model", model);
                context.startActivity(viewModel);
            }
        });

        return rowView;
    }
}
