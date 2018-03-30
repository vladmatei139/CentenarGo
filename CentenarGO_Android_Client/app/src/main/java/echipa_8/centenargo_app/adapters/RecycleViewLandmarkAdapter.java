package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import echipa_8.centenargo_app.R;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecycleViewLandmarkAdapter extends RecyclerView.Adapter<RecycleViewLandmarkAdapter.LandmarkViewHolder> {

    private Pair<String[], int[]> mIdentifiers;

    public RecycleViewLandmarkAdapter(Context context, Pair<String[], int[]> identifiers) {
        mIdentifiers = identifiers;
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public LandmarkViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.list_item_template_title);
        }
    }

    @Override
    public RecycleViewLandmarkAdapter.LandmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);

        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewLandmarkAdapter.LandmarkViewHolder holder, int position) {
        holder.mTextView.setText(mIdentifiers.first[position]);
    }

    @Override
    public int getItemCount() {
        return mIdentifiers.first.length;
    }
}
