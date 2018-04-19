package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Landmark_Activity;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecycleViewLandmarkAdapter extends RecyclerView.Adapter<RecycleViewLandmarkAdapter.LandmarkViewHolder> {

    private Pair<String[], int[]> mIdentifiers;
    private WeakReference<Context> mContext;
    private String mToken;

    public RecycleViewLandmarkAdapter(Context context, Pair<String[], int[]> identifiers, String token) {
        mIdentifiers = identifiers;
        mContext = new WeakReference<>(context);
        this.mToken = token;
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public LinearLayout mLinearLayout;
        public LandmarkViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.list_item_template_title);
            mLinearLayout = view.findViewById(R.id.linear_layout_list);
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
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.get().getApplicationContext(), Landmark_Activity.class);
                intent.putExtra("LandmarkId", mIdentifiers.second[position]);
                intent.putExtra("TOKEN", mToken);
                mContext.get().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mIdentifiers.first.length;
    }
}
