package echipa_8.centenargo_app.adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.activities.Routes_Activity;
import echipa_8.centenargo_app.services.CurrentInfo_Service;
import echipa_8.centenargo_app.utilities.RouteInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class RecyclerViewRouteAdapter extends RecyclerView.Adapter<RecyclerViewRouteAdapter.RecyclerViewRouteHolder> {

    private List<RouteInfo> mRoutesInfo;
    private WeakReference<Routes_Activity> mActivity;

    public RecyclerViewRouteAdapter(final Routes_Activity activity, List<RouteInfo> routesInfo) {
        mRoutesInfo = routesInfo;
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public RecyclerViewRouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtemplate, parent, false);
        return new RecyclerViewRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewRouteHolder holder, final int position) {
        holder.mTextView.setText(mRoutesInfo.get(position).name);
        holder.mImageView.setBackground(mActivity.get().getDrawable(mRoutesInfo.get(position).completed
                ? R.mipmap.color_check : R.mipmap.color_running));
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentInfo_Service currentInfo_service = new CurrentInfo_Service(mActivity.get());
                currentInfo_service.execute(Integer.toString(mRoutesInfo.get(position).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRoutesInfo.size();
    }

    public class RecyclerViewRouteHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public LinearLayout mLinearLayout;
        public ImageView mImageView;

        public RecyclerViewRouteHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_template_title);
            mLinearLayout = itemView.findViewById(R.id.linear_layout_list);
            mImageView = itemView.findViewById(R.id.list_item_icon);
        }
    }
}
