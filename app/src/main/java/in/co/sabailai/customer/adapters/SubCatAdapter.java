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
import in.co.sabailai.customer.models.SubCatGetSet;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubCatGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    SubCatGetSet category;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subcatname;

        public MyViewHolder(View view) {
            super(view);

            subcatname = (TextView) view.findViewById(R.id.subcatname);


        }
    }

    public SubCatAdapter(List<SubCatGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }

    @Override
    public SubCatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcat_layout_item, parent, false);
        return new SubCatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SubCatAdapter.MyViewHolder holder, int position) {

        category = categoryList.get(position);

        holder.subcatname.setText(category.getName());

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