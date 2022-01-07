package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.OffersGetSet;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private Context mContext;
    private List<OffersGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    OffersGetSet category;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat_name;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            cat_name = (TextView) view.findViewById(R.id.cat_name);
            icon = (ImageView) view.findViewById(R.id.icon);


        }
    }
    public OfferAdapter(List<OffersGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_layout, parent, false);
        return new OfferAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final OfferAdapter.MyViewHolder holder, int position) {

        category = categoryList.get(position);

        holder.cat_name.setText(category.getName());
        Glide.with(mContext)
                .load( category.getImg())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.app_icon))
                .into(holder.icon);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}