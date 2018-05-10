package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Image_Activity;
import echipa_8.centenargo_app.utilities.Image;

public class RecyclerViewImageGalleryAdapter extends RecyclerView.Adapter<RecyclerViewImageGalleryAdapter.GalleryViewHolder> {

    private Context context;
    private ArrayList<Image> images;

    public RecyclerViewImageGalleryAdapter(Context context, ArrayList<Image> images) {
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
                .load("http:10.0.2.2/" + images.get(position).getPath())
                .error(R.drawable.placeholder_image_square)
                .placeholder(R.drawable.placeholder_image_square)
                .into(holder.mImageView);
        holder.mLinearLayout.setOnClickListener(v -> goToImage(v, position));
    }

    private void goToImage(View view, int position) {
        Intent intent = new Intent(context, Image_Activity.class);
        intent.putParcelableArrayListExtra("images", images);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
