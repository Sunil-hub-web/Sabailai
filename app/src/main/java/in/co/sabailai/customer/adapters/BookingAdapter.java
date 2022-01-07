package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.CompletedBookingGetSet;
import in.co.sabailai.extras.ServerLinks;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private Context context;
    private List<CompletedBookingGetSet> accepetedJobLists;
    private List<CompletedBookingGetSet> filteredlist;
    public int cnt;


    public BookingAdapter(List<CompletedBookingGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.filteredlist = accepetedJobLists;
        this.context = context;

    }


    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.booking_item_layout, parent, false);
        BookingAdapter.MyViewHolder holder = new BookingAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final BookingAdapter.MyViewHolder holder, final int position) {

        CompletedBookingGetSet movie = filteredlist.get(position);

        holder.servicename.setText(movie.getService_title());
        holder.serviceid.setText(movie.getBooking_id());
        holder.assignedto.setText("₹" + movie.getTotal_amount());

        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null; // Format String to a dateobject with the format provided by the String.
            d = f.parse(movie.getService_date());
            SimpleDateFormat f2 = new SimpleDateFormat("dd MMM yyyy"); // MMMM for full month name
            System.out.println(f2.format(d));

            holder.datetime.setText(f2.format(d) + "\n" + movie.getTime_slot());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(movie.getStatus().equalsIgnoreCase("2") || movie.getStatus().equalsIgnoreCase("1")){
            holder.workstartedlayout.setVisibility(View.VISIBLE);
            if(movie.getStatus().equalsIgnoreCase("2")){
                holder.markascomplete.setVisibility(View.VISIBLE);
                holder.workstatus.setText("Started");
            }else{
                holder.markascomplete.setVisibility(View.GONE);
                holder.workstatus.setText("Assigned");
            }
        }else{
            holder.workstartedlayout.setVisibility(View.GONE);
        }

        holder.markascomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView servicename, serviceid, assignedto, datetime, workstatus;
        public LinearLayout workstartedlayout;
        public Button markascomplete;

        public MyViewHolder(View itemView) {
            super(itemView);

            servicename = (TextView) itemView.findViewById(R.id.servicename);
            serviceid = (TextView) itemView.findViewById(R.id.serviceid);
            assignedto = (TextView) itemView.findViewById(R.id.assignedto);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            workstatus = (TextView) itemView.findViewById(R.id.workstatus);
            workstartedlayout = (LinearLayout) itemView.findViewById(R.id.workstartedlayout);
            markascomplete = (Button) itemView.findViewById(R.id.markascomplete);

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
                    ArrayList<CompletedBookingGetSet> filteredList = new ArrayList<>();
                    for (CompletedBookingGetSet row : accepetedJobLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAprts().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredlist = (ArrayList<CompletedBookingGetSet>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
//
//    private void CompleteTask(final int pos, final String id) {
//
//        progressbar.showDialog();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorAccept_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("fvsDevbf", response);
//
//                        try {
////                            completedarray = new ArrayList<CompletedBookingGetSet>();
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("success").equals("true")) {
//
//                                Toast.makeText(mContext, "Task Accepted Successfully", Toast.LENGTH_SHORT).show();
//
//                                categoryList.remove(pos);
//                                notifyItemRemoved(pos);
//////
//                                progressbar.hideDialog();
//////
////
//                            } else {
//                                progressbar.hideDialog();
//                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.d("successresponceVolley", "" + e);
//                            progressbar.hideDialog();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressbar.hideDialog();
//
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//
//                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            NetworkResponse networkResponse = error.networkResponse;
//                            if (networkResponse != null && networkResponse.data != null) {
//                                try {
//                                    String jError = new String(networkResponse.data);
//                                    JSONObject jsonError = new JSONObject(jError);
//                                    Log.d("successresponceVolley", "" + jsonError);
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Log.d("successresponceVolley", "" + e);
//                                }
//
//                            }
//                        }
//                    }
//                }) {
//
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", session.getUserID());
//                params.put("order_id", id);
//                Log.d("fvsDevbf", "" + params);
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new
//                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.getCache().clear();
//        requestQueue.add(stringRequest);
//    }

//    public boolean getindex(String checkProduct) {
//        boolean check = false;
//        ArrayList<CompletedBookingGetSet> favorites = ServerLinks.arraCart;
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