package in.co.sabailai.customer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.SubSubCatGetSet;

public class SubSubCatAdapter extends RecyclerView.Adapter<SubSubCatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubSubCatGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    SubSubCatGetSet category;
    int selpos = 0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat_name;

        public MyViewHolder(View view) {
            super(view);
            cat_name = (TextView) view.findViewById(R.id.cat_name);


        }
    }
    public SubSubCatAdapter(List<SubSubCatGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public SubSubCatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcat_item_layout, parent, false);
        return new SubSubCatAdapter.MyViewHolder(itemView);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final SubSubCatAdapter.MyViewHolder holder, final int position) {

        category = categoryList.get(position);

        holder.cat_name.setText(category.getSub_sub_category_name());

        holder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selpos = position;
                notifyDataSetChanged();
            }
        });

        if(selpos==position){
            holder.cat_name.setBackgroundResource(R.drawable.cat_selected);
            holder.cat_name.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }else{
            holder.cat_name.setBackgroundResource(R.drawable.cat_notselected);
            holder.cat_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }

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