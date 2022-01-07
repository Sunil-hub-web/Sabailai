package in.co.sabailai.customer.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.activities.ServiceList;
import in.co.sabailai.customer.adapters.ServiceItemAdapter;
import in.co.sabailai.customer.adapters.SubSubCatAdapter;
import in.co.sabailai.customer.models.BookingsGetSet;
import in.co.sabailai.customer.models.CompletedBookingGetSet;
import in.co.sabailai.customer.models.CustomServiceGetSet;
import in.co.sabailai.customer.models.ServiceItemGetSet;
import in.co.sabailai.customer.models.SubSubCatGetSet;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

public class BookingsFragment extends Fragment {

    SessionManager session;
    ViewDialog progressbar;
    TextView completed, cancelled, requested;
    RecyclerView booking_recyclerview;
    BookingAdapter catAdapter;
    private ArrayList<BookingsGetSet> bookingssarray = new ArrayList<BookingsGetSet>();
    private ArrayList<CompletedBookingGetSet> completedarray = new ArrayList<CompletedBookingGetSet>();
    private ArrayList<CompletedBookingGetSet> requestedarray = new ArrayList<CompletedBookingGetSet>();
    private ArrayList<CompletedBookingGetSet> cancelledarray = new ArrayList<CompletedBookingGetSet>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_bookings, container, false);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        completed = v.findViewById(R.id.completed);
        cancelled = v.findViewById(R.id.cancelled);
        requested = v.findViewById(R.id.requested);
        booking_recyclerview = v.findViewById(R.id.booking_recyclerview);

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookingAdapter catAdapter = new BookingAdapter(completedarray, getActivity());
                booking_recyclerview.setHasFixedSize(true);
                booking_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                booking_recyclerview.setAdapter(catAdapter);

                completed.setBackgroundResource(R.drawable.tab_selected);
                cancelled.setBackgroundResource(R.drawable.tab_normal);
                requested.setBackgroundResource(R.drawable.tab_normal);

                completed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                cancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
                requested.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
            }
        });

        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookingAdapter catAdapter = new BookingAdapter(cancelledarray, getActivity());
                booking_recyclerview.setHasFixedSize(true);
                booking_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                booking_recyclerview.setAdapter(catAdapter);

                completed.setBackgroundResource(R.drawable.tab_normal);
                cancelled.setBackgroundResource(R.drawable.tab_selected);
                requested.setBackgroundResource(R.drawable.tab_normal);

                completed.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
                cancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                requested.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
            }
        });

        requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookingAdapter catAdapter = new BookingAdapter(requestedarray, getActivity());
                booking_recyclerview.setHasFixedSize(true);
                booking_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                booking_recyclerview.setAdapter(catAdapter);

                completed.setBackgroundResource(R.drawable.tab_normal);
                cancelled.setBackgroundResource(R.drawable.tab_normal);
                requested.setBackgroundResource(R.drawable.tab_selected);

                completed.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
                cancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.textcol));
                requested.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        });


        GetBookings();
//        SetBookings();

        return v;
    }

    private void GetBookings() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.bookingHistory_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            completedarray = new ArrayList<CompletedBookingGetSet>();
                            requestedarray = new ArrayList<CompletedBookingGetSet>();
                            cancelledarray = new ArrayList<CompletedBookingGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_booking");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String order_id = jsobjectitem.getString("order_id");
                                    String booking_id = jsobjectitem.getString("booking_id");
                                    String contact_person = jsobjectitem.getString("contact_person");
                                    String contact_phon = jsobjectitem.getString("contact_phon");
                                    String area = jsobjectitem.getString("area");
                                    String address = jsobjectitem.getString("address");
                                    String aprts = jsobjectitem.getString("aprts");
                                    String Service_date = jsobjectitem.getString("Service_date");
                                    String time_slot = jsobjectitem.getString("time_slot");
                                    String payment = jsobjectitem.getString("payment");
                                    String total_amount = jsobjectitem.getString("total_amount");
                                    String discount = jsobjectitem.getString("discount");
                                    String cupon = jsobjectitem.getString("cupon");
                                    String status = jsobjectitem.getString("status");
                                    String Service_title = jsobjectitem.getString("Service_title");
                                    String service_qty = jsobjectitem.getString("service_qty");
                                    String service_price = jsobjectitem.getString("service_price");
                                    String vendor_id = jsobjectitem.getString("vendor_id");


                                    if (status.equalsIgnoreCase("4")) {

                                        cancelledarray.add(new CompletedBookingGetSet(vendor_id, order_id, booking_id, contact_person, contact_phon, area, address, aprts, Service_date, time_slot, payment, total_amount,
                                                discount, cupon, status, Service_title, service_qty, service_price));
                                    } else if (status.equalsIgnoreCase("3")) {

                                        completedarray.add(new CompletedBookingGetSet(vendor_id, order_id, booking_id, contact_person, contact_phon, area, address, aprts, Service_date, time_slot, payment, total_amount,
                                                discount, cupon, status, Service_title, service_qty, service_price));
                                    } else {

                                        requestedarray.add(new CompletedBookingGetSet(vendor_id, order_id, booking_id, contact_person, contact_phon, area, address, aprts, Service_date, time_slot, payment, total_amount,
                                                discount, cupon, status, Service_title, service_qty, service_price));
                                    }
                                }

                                catAdapter = new BookingAdapter(requestedarray, getActivity());
                                booking_recyclerview.setHasFixedSize(true);
                                booking_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                booking_recyclerview.setAdapter(catAdapter);
//
                                progressbar.hideDialog();
//

                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                            progressbar.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.hideDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    Log.d("successresponceVolley", "" + jsonError);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                            }
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getUserID());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

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

        @SuppressLint("RecyclerView")
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

            if (movie.getStatus().equalsIgnoreCase("2") || movie.getStatus().equalsIgnoreCase("1")) {
                holder.workstartedlayout.setVisibility(View.VISIBLE);
                if (movie.getStatus().equalsIgnoreCase("2")) {
                    holder.markascomplete.setVisibility(View.VISIBLE);
                    holder.workstatus.setText("Started");
                } else {
                    holder.markascomplete.setVisibility(View.GONE);
                    holder.workstatus.setText("Assigned");
                }
            } else {
                holder.workstartedlayout.setVisibility(View.GONE);
            }

            holder.markascomplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Mark as Complete!!")
                            .setMessage("Are you sure you want to Mark this Task as Complete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String ii = filteredlist.get(position).getOrder_id();
                                    String vii = filteredlist.get(position).getVendor_id();
                                    CompleteTask(position, ii, vii);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
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

        private void CompleteTask(final int pos, final String id, final String vid) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.completeTask_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show();

                                    accepetedJobLists.get(pos).setStatus("3");
                                    catAdapter.getFilter().filter("");
//                                    catAdapter.notifyDataSetChanged();
////
                                    progressbar.hideDialog();
////
//
                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("successresponceVolley", "" + e);
                                progressbar.hideDialog();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.hideDialog();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                            } else {
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null && networkResponse.data != null) {
                                    try {
                                        String jError = new String(networkResponse.data);
                                        JSONObject jsonError = new JSONObject(jError);
                                        Log.d("successresponceVolley", "" + jsonError);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("successresponceVolley", "" + e);
                                    }

                                }
                            }
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("vendor_id", vid);
                    params.put("order_id", id);
                    Log.d("fvsDevbf", "" + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

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
}
