package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.FAQGetSet;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> {

    private Context mContext;
    private List<FAQGetSet> categoryList;
    FAQGetSet category;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView que, ans;

        public MyViewHolder(View view) {
            super(view);

            ans = (TextView) view.findViewById(R.id.ans);
            que = (TextView) view.findViewById(R.id.que);

        }
    }

    public FAQAdapter(List<FAQGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @Override
    public FAQAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_layout_item, parent, false);
        return new FAQAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.MyViewHolder holder, int position) {

        category = categoryList.get(position);

        holder.ans.setText(category.getAnswer());
        holder.que.setText(category.getQuestion());

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