package echipa_8.centenargo_app.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.database.AppDatabase;
import echipa_8.centenargo_app.database.Image;

public class RecyclerViewImageGalleryAdapter extends RecyclerView.Adapter<RecyclerViewImageGalleryAdapter.LandmarkViewHolder> {

    private List<Map<String, Object>> images;
    private AppDatabase database;
    private Resources resources;
    private RequestQueue requestQueue;
    private File fileDir;

    public RecyclerViewImageGalleryAdapter(List<Map<String, Object>> images,
                                           AppDatabase database,
                                           Resources resources,
                                           RequestQueue requestQueue,
                                           File fileDir) {
        this.images = images;
        this.database = database;
        this.resources = resources;
        this.requestQueue = requestQueue;
        this.fileDir = fileDir;
    }

    static class LandmarkViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        ImageView mImageView;
        LandmarkViewHolder(View view) {
            super(view);
            mLinearLayout = view.findViewById(R.id.gallery_image_layout);
            mImageView = mLinearLayout.findViewById(R.id.gallery_image);
        }
    }

    @NonNull
    @Override
    public LandmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_template, parent, false);
        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkViewHolder holder, final int position) {
        Map<String, Object> data = images.get(position);
        Image image = database.imageDAO().findById((Integer) data.get("id"));
        if (image == null) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://10.0.2.2:8080/" + data.get("path"),
                    null,
                    response -> {
                        String encodedString = response.toString().split(",")[1];
                        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
                        String extension = ((String) data.get("path")).split("\\.")[((String) data.get("path")).split("\\.").length-1];
                        String localPath = data.get("id").toString() + extension;
                        File file = new File(fileDir, localPath);
                        try(FileOutputStream outputStream = new FileOutputStream(file)) {
                            outputStream.write(decodedString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        database.imageDAO().insertAll(new Image((Integer) data.get("id"), file.getAbsolutePath(), (String) data.get("title")));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.mImageView.setImageDrawable(new BitmapDrawable(resources, bitmap));
                    },
                    error -> Log.println(Log.ERROR, "ImageError", "Volley request error for url " + data.get("path") + " failed: " + error.getMessage())
            );
            requestQueue.add(request);
        }
        else {
            File file = new File(image.getLocalPath());
            try(FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[(int) file.getTotalSpace()];
                int length = inputStream.read(buffer);
                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, length);
                holder.mImageView.setImageDrawable(new BitmapDrawable(resources, bitmap));
            } catch (IOException e) {
                e.printStackTrace();
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
