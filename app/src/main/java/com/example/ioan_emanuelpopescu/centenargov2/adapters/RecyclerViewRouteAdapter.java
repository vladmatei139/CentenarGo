package com.example.ioan_emanuelpopescu.centenargov2.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ioan_emanuelpopescu.centenargov2.R;
import com.example.ioan_emanuelpopescu.centenargov2.wrappers.Route;

import java.util.ArrayList;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecyclerViewRouteAdapter extends RecyclerView.Adapter<RecyclerViewRouteAdapter.RecyclerViewRouteHolder> {

    private ArrayList<Route> routes = new ArrayList<>();

    public RecyclerViewRouteAdapter(ArrayList<Route> routes) {
        this.routes = routes;
    }

    @Override
    public RecyclerViewRouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);
        return new RecyclerViewRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewRouteHolder holder, int position) {
        Route route = routes.get(position);
        if(route == null){
            return;
        }

        holder.tvIndex.setText(String.valueOf(route.getUuid()));
        holder.tvTitle.setText(route.getName());
        holder.cbIsCompleted.setChecked(route.checkIfCompleted());


    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RecyclerViewRouteHolder extends RecyclerView.ViewHolder {

        private TextView tvIndex;
        private TextView tvTitle;
        private AppCompatCheckBox cbIsCompleted;

        public RecyclerViewRouteHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tvListItemUID);
            tvTitle = itemView.findViewById(R.id.tvListItemTitle);
            cbIsCompleted = itemView.findViewById(R.id.cbListItemCheck);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        Route clickedDataItem = routes.get(pos);
                        Log.i("MESSAGE", "You clicked item " + pos + " named " + clickedDataItem.getName());
                    }
                }
            });

        }
    }
}
