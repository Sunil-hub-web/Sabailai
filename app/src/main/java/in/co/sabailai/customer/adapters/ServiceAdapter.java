package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.ServiceGetSet;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Context context;
    private List<ServiceGetSet> accepetedJobLists;
    private List<ServiceGetSet> filteredlist;
    public int cnt;




    public ServiceAdapter(List<ServiceGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.filteredlist = accepetedJobLists;
        this.context = context;

    }


    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        ServiceAdapter.MyViewHolder holder = new ServiceAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ServiceAdapter.MyViewHolder holder, final int position) {

        ServiceGetSet movie = filteredlist.get(position);

        holder.txt1.setText(movie.getName());
        Glide.with(context).load(
                movie.getImage()).into(holder.service_icon);

    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;
        public ImageView service_icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt1 = (TextView) itemView.findViewById(R.id.txt1);
            service_icon = (ImageView) itemView.findViewById(R.id.service_icon);

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
                    ArrayList<ServiceGetSet> filteredList = new ArrayList<>();
                    for (ServiceGetSet row : accepetedJobLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredlist = (ArrayList<ServiceGetSet>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

//    public boolean getindex(String checkProduct) {
//        boolean check = false;
//        ArrayList<ServiceGetSet> favorites = ServerLinks.arraCart;
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