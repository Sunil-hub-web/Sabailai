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
import in.co.sabailai.customer.models.TestimonialsGetSet;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.MyViewHolder> {

    private Context mContext;
    private List<TestimonialsGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    TestimonialsGetSet category;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);

            icon = (ImageView) view.findViewById(R.id.icon);


        }
    }
    public TestimonialAdapter(List<TestimonialsGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public TestimonialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.testimonial_item_layout, parent, false);
        return new TestimonialAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final TestimonialAdapter.MyViewHolder holder, int position) {

        category = categoryList.get(position);

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