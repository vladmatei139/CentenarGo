package echipa_8.centenargo_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;

public class RecyclerViewImageGalleryAdapter extends RecyclerView.Adapter<RecyclerViewImageGalleryAdapter.GalleryViewHolder> {

    private List<Map<String, Object>> images;
    private LayoutInflater inflater;

    public RecyclerViewImageGalleryAdapter(List<Map<String, Object>> images, LayoutInflater inflater) {
        this.images = images;
        this.inflater = inflater;
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
        Picasso.get()
                .load("http:10.0.2.2:8080/" + data.get("path"))
                .placeholder(R.drawable.placeholder_image_square)
                .into(holder.mImageView);
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
