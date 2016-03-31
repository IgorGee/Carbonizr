package xyz.igorgee.imagecreator3d;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.igorgee.imagecreatorg3dx.ObjectViewer;
import xyz.igorgee.utilities.ImageHelper;
import xyz.igorgee.utilities.UIUtilities;

import static xyz.igorgee.utilities.UIUtilities.makeAlertDialog;

public class CustomAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Model> models;

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

        holder.position = position;
        holder.textView.setText(models.get(position).getName());
        holder.modelDirectory = models.get(position).getLocation();
        File imageLocation = new File(models.get(position).getLocation(), models.get(position).getName() + ".jpg");
        loadBitmap(imageLocation, holder.imageView);

        return view;
    }

    class ViewHolder {
        @Bind(R.id.image_name) TextView textView;
        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.button_buy) Button buy;
        @Bind(R.id.button_3d_view) Button view3d;

        int position;
        File modelDirectory;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_buy)
        public void buyModel(View view) {
            File uploadModel = new File(modelDirectory, models.get(position).getName() + ".stl");
            new UploadModelAsyncTask(uploadModel, uploadModel.getName(), position).execute();
            UIUtilities.makeSnackbar(view, R.string.add_to_cart_text);
        }

        @OnClick(R.id.button_3d_view)
        public void viewIn3D(View view) {
            File previewModel = new File(modelDirectory, models.get(position).getName() + ".g3db");

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
            int position;
            JSONObject json;
            int modelID;

            UploadModelAsyncTask(File uploadModel, String baseFileName, int position) {
                this.uploadModel = uploadModel;
                this.baseFileName = baseFileName;
                this.position = position;
            }

            @Override
            protected Void doInBackground(Void... params) {

                if (models.get(position).getModelID() == null) {
                    String response = HomePageFragment.client.uploadModel(uploadModel, baseFileName);

                    try {
                        json = new JSONObject(response);
                        modelID = json.getInt("modelId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    modelID = models.get(position).modelID;
                }

                HomePageFragment.client.addToCart(modelID);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                models.get(position).setModelID(modelID);
            }
        }
    }

    class BitmapWorkerTask extends AsyncTask<File, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private File file;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(File... params) {
            file = params[0];
            return ImageHelper.decodeSampledBitmapFromResource(file);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public void loadBitmap(File file, ImageView imageView) {
        Bitmap bplaceholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        if (cancelPotentialWork(file, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(file);
        }
    }

    public static boolean cancelPotentialWork(File file, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final File bitmapData = bitmapWorkerTask.file;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || bitmapData != file) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
