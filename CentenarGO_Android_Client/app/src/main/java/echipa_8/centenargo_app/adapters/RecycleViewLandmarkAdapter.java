package echipa_8.centenargo_app.adapters;

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

    private String[] mDataset;

    public RecycleViewLandmarkAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public LandmarkViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.tvListItemTitle);
        }
    }

    @Override
    public RecycleViewLandmarkAdapter.LandmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);

        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewLandmarkAdapter.LandmarkViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
