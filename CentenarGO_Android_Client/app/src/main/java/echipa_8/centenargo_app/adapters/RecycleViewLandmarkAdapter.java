package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;

import java.lang.ref.WeakReference;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Landmark_Activity;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecycleViewLandmarkAdapter extends RecyclerView.Adapter<RecycleViewLandmarkAdapter.LandmarkViewHolder> {

    private Pair<String[], int[]> mIdentifiers;
    private Integer mRouteId;
    private WeakReference<Context> mContext;
    private Boolean mRouteCompleted;

    public RecycleViewLandmarkAdapter(Context context, Pair<String[], int[]> identifiers, Integer routeId, Boolean completed) {
        mIdentifiers = identifiers;
        mRouteId = routeId;
        mRouteCompleted = completed;
        mContext = new WeakReference<>(context);
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public LinearLayout mLinearLayout;
        public ImageView mImageView;

        public LandmarkViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.list_item_template_title);
            mLinearLayout = view.findViewById(R.id.linear_layout_list);
            mImageView = itemView.findViewById(R.id.list_item_icon);
        }
    }

    @Override
    public RecycleViewLandmarkAdapter.LandmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);
        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewLandmarkAdapter.LandmarkViewHolder holder, final int position) {
        holder.mTextView.setText(mIdentifiers.first[position]);
        holder.mImageView.setBackground(mContext.get().getDrawable(mRouteCompleted
                ? R.mipmap.color_check : position == 0 ? R.mipmap.color_running : R.mipmap.color_check));
        holder.mLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext.get().getApplicationContext(), Landmark_Activity.class);
            intent.putExtra(mContext.get().getString(R.string.landmark_id_key), mIdentifiers.second[position]);
            intent.putExtra(mContext.get().getString(R.string.route_id_key), mRouteId);
            mContext.get().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mIdentifiers.first.length;
    }
}
