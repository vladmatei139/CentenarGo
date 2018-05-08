package echipa_8.centenargo_app.adapters;

import android.content.AsyncTaskLoader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Gallery_Activity;
import echipa_8.centenargo_app.database.AppDatabase;
import echipa_8.centenargo_app.database.Image;
import echipa_8.centenargo_app.utilities.FixedQueue;

public class RecyclerViewImageGalleryAdapter extends RecyclerView.Adapter<RecyclerViewImageGalleryAdapter.GalleryViewHolder> {

    private List<Map<String, Object>> images;
    private LayoutInflater inflater;
    private AppDatabase database;
    private Resources resources;
    private File fileDir;
    private ImageLoader imageLoader;
    private FixedQueue<Pair<Integer, BitmapDrawable>> cache;

    public RecyclerViewImageGalleryAdapter(List<Map<String, Object>> images,
                                           LayoutInflater inflater,
                                           AppDatabase database,
                                           Resources resources,
                                           RequestQueue requestQueue,
                                           File fileDir) {
        this.images = images;
        this.inflater = inflater;
        this.database = database;
        this.resources = resources;
        this.requestQueue = requestQueue;
        this.fileDir = fileDir;
        this.imageLoader = ImageLoader.getInstance();
        this.cache = new FixedQueue<>(100);
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        ImageView mImageView;
        GalleryViewHolder(View view) {
            super(view);
            mLinearLayout = view.findViewById(R.id.gallery_image_layout);
            mImageView = mLinearLayout.findViewById(R.id.gallery_image);
        }
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallery_image_template, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {

        Map<String, Object> data = images.get(position);
        Integer id = (Integer) images.get(position).get("id");
        String localPath = data.get("id") + ".png";

        boolean found = false;
        for (Pair<Integer, BitmapDrawable> pair : cache) {
            if (pair.first.equals(id)) {
                found = true;
                holder.mImageView.setImageDrawable(pair.second);
                break;
            }
        }

        if (!found) {
            Image image = database.imageDAO().findById(id);
            if (image == null) {
                imageLoader.loadImage("http://10.0.2.2:8080/" + data.get("path"), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {

                        try (FileOutputStream outputStream = new FileOutputStream(new File(fileDir, localPath))) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
                        cache.add(new Pair<>(id, bitmapDrawable));
                        holder.mImageView.setImageDrawable(bitmapDrawable);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.println(Log.ERROR, "ImageError", "Image loading failed: " + failReason.toString());
                    }
                });
            }
            else {
                File file = new File(localPath);
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[(int) file.getTotalSpace()];
                    int length = inputStream.read(buffer);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, length);
                    holder.mImageView.setImageDrawable(new BitmapDrawable(resources, bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        holder.mLinearLayout.setOnClickListener(v -> goToImage(v, position));
    }

    private void goToImage(View view, int position) {
        //TODO
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
