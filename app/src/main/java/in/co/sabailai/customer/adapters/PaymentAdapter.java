package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.PaymentGetSet;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Context context;
    private List<PaymentGetSet> accepetedJobLists;
    private List<PaymentGetSet> filteredlist;
    public int cnt;
    

    public PaymentAdapter(List<PaymentGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.filteredlist = accepetedJobLists;
        this.context = context;

    }


    @Override
    public PaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.payment_item_layout, parent, false);
        PaymentAdapter.MyViewHolder holder = new PaymentAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final PaymentAdapter.MyViewHolder holder, final int position) {

        PaymentGetSet movie = filteredlist.get(position);

        holder.servicename.setText(movie.getService());
        holder.serviceid.setText("Service ID : "+movie.getOrderid());
        holder.assignedto.setText(movie.getAssignedto());
        holder.datetime.setText(movie.getDate());
        holder.amount.setText(movie.getAmount());


    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView servicename, serviceid, assignedto, datetime, amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            servicename = (TextView) itemView.findViewById(R.id.servicename);
            serviceid = (TextView) itemView.findViewById(R.id.serviceid);
            assignedto = (TextView) itemView.findViewById(R.id.assignedto);
            datetime = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredlist = accepetedJobLists;
                } else {
                    ArrayList<PaymentGetSet> filteredList = new ArrayList<>();
                    for (PaymentGetSet row : accepetedJobLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getService().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredlist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredlist = (ArrayList<PaymentGetSet>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

//    public boolean getindex(String checkProduct) {
//        boolean check = false;
//        ArrayList<PaymentGetSet> favorites = ServerLinks.arraCart;
//        if (favorites != null) {
//            for (CartItem product : favorites) {
//
//                if (product.getVarient_id().equals(checkProduct)) {
//
//                    indx = favorites.indexOf(product);
//
//                    Log.d("indx", String.valueOf(indx));
//
//                    check = true;
//                    break;
//
//                }
//
//            }
//        }
//        return check;
//
//
//    }

}