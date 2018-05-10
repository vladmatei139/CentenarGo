package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.content.Intent;
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
import echipa_8.centenargo_app.activities.Image_Activity;

public class RecyclerViewImageGalleryAdapter extends RecyclerView.Adapter<RecyclerViewImageGalleryAdapter.GalleryViewHolder> {

    private Context context;
    private List<Map<String, Object>> images;

    public RecyclerViewImageGalleryAdapter(Context context, List<Map<String, Object>> images) {
        this.context = context;
        this.images = images;
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
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_image_template, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, final int position) {
        Picasso.get()
                .load("http:192.168.1.101:8080/" + images.get(position).get("path"))
                .placeholder(R.drawable.placeholder_image_square)
                .into(holder.mImageView);
        holder.mLinearLayout.setOnClickListener(v -> goToImage(v, position));
    }

    private void goToImage(View view, int position) {

        Intent intent = new Intent(context, Image_Activity.class);
        intent.putExtra("imageId", Integer.parseInt(images.get(position).get("id").toString()));
        intent.putExtra("imageTitle", (String) images.get(position).get("title"));
        intent.putExtra("imageAuthor", (String) images.get(position).get("username"));
        intent.putExtra("imagePath", (String) images.get(position).get("path"));
        intent.putExtra("likes", (String) images.get(position).get("likes"));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
