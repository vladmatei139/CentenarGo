package echipa_8.centenargo_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Route_Activity;
import echipa_8.centenargo_core.wrappers.Route;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecyclerViewRouteAdapter extends RecyclerView.Adapter<RecyclerViewRouteAdapter.RecyclerViewRouteHolder> {

    private Pair<String[], int[]> mIdentifiers;
    private WeakReference<Context> mContext;
    private String mToken;

    public RecyclerViewRouteAdapter(Context context, Pair<String[], int[]> identifiers, String token) {
        mIdentifiers = identifiers;
        mContext = new WeakReference<>(context);
        mToken = token;
    }

    @Override
    public RecyclerViewRouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);
        return new RecyclerViewRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewRouteHolder holder, final int position) {
        holder.mTextView.setText(mIdentifiers.first[position]);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.get().getApplicationContext(), Route_Activity.class);
                intent.putExtra("RouteId", mIdentifiers.second[position]);
                intent.putExtra("TOKEN", mToken);
                mContext.get().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIdentifiers.first.length;
    }

    public class RecyclerViewRouteHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public LinearLayout mLinearLayout;

        public RecyclerViewRouteHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_template_title);
            mLinearLayout = itemView.findViewById(R.id.linear_layout_list);
        }
    }
}
