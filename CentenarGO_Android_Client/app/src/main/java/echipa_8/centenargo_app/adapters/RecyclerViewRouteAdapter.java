package echipa_8.centenargo_app.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_core.wrappers.Route;

import java.util.ArrayList;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecyclerViewRouteAdapter extends RecyclerView.Adapter<RecyclerViewRouteAdapter.RecyclerViewRouteHolder> {

    private String[] mDataset;

    public RecyclerViewRouteAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerViewRouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);
        return new RecyclerViewRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewRouteHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public class RecyclerViewRouteHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public RecyclerViewRouteHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tvListItemTitle);

        }
    }
}
