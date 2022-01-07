package in.co.sabailai.technician.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;
import in.co.sabailai.technician.models.UpcomingGetSet;

public class TechnicianUpcomingFragment extends Fragment {

    ViewDialog progressbar;
    SessionManager session;
    RecyclerView upcoming_recyclerview;
    ArrayList<UpcomingGetSet> upcomingarray = new ArrayList<UpcomingGetSet>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technician_upcoming, container, false);

        upcoming_recyclerview = v.findViewById(R.id.upcoming_recyclerview);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());


        GetUpcomings();
        return v;
    }

    private void GetUpcomings() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorUpcomingTasks_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Tasks_upcomming");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String order_id = jsobjectitem.getString("order_id");
                                    String service_id = jsobjectitem.getString("service_id");
                                    String service_title = jsobjectitem.getString("service_title");
//                                    String contact_person = jsobjectitem.getString("contact_person");
                                    String date = jsobjectitem.getString("date");
//                                    String area = jsobjectitem.getString("area");
//                                    String address = jsobjectitem.getString("address");
//                                    String contact_no = jsobjectitem.getString("contact_no");
//                                    String apart_noplot_no = jsobjectitem.getString("apart_no/plot_no");
//                                    String time_slot = jsobjectitem.getString("time_slot");
//                                    String city = jsobjectitem.getString("city");
//                                    String State = jsobjectitem.getString("State");
                                    String status = jsobjectitem.getString("status");

                                    upcomingarray.add(new UpcomingGetSet(order_id, service_id, service_title, date, "time_slot", "contact_person",
                                            "contact_no", "apart_noplot_no", "address", "area", "city", "State", status));

                                }
                                UpcomingAdapter catAdapter = new UpcomingAdapter(upcomingarray, getActivity());
                                upcoming_recyclerview.setHasFixedSize(true);
                                upcoming_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                upcoming_recyclerview.setAdapter(catAdapter);
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

    public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder> {

        private Context mContext;
        private List<UpcomingGetSet> categoryList;
        //    private RecyclerTouchListener listener;
        UpcomingGetSet category;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView personname, servicename, service_id, datetime, contactperson, contactnumber, plotapartment,
                    address, citystate, viewdetails, acceptbtn, rejectbtn;
            public LinearLayout contactinfolayout;

            public MyViewHolder(View view) {
                super(view);
                personname = (TextView) view.findViewById(R.id.personname);
                servicename = (TextView) view.findViewById(R.id.servicename);
                service_id = (TextView) view.findViewById(R.id.service_id);
                datetime = (TextView) view.findViewById(R.id.datetime);
                contactperson = (TextView) view.findViewById(R.id.contactperson);
                contactnumber = (TextView) view.findViewById(R.id.contactnumber);
                plotapartment = (TextView) view.findViewById(R.id.plotapartment);
                address = (TextView) view.findViewById(R.id.address);
                citystate = (TextView) view.findViewById(R.id.citystate);
                viewdetails = (TextView) view.findViewById(R.id.viewdetails);
                acceptbtn = (TextView) view.findViewById(R.id.acceptbtn);
                rejectbtn = (TextView) view.findViewById(R.id.rejectbtn);
                contactinfolayout = (LinearLayout) view.findViewById(R.id.contactinfolayout);


            }
        }

        public UpcomingAdapter(List<UpcomingGetSet> categoryList, Context mContext) {
            this.mContext = mContext;
            this.categoryList = categoryList;
//        this.listener = listener;
        }

        @Override
        public UpcomingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_item_layout, parent, false);
            return new UpcomingAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final UpcomingAdapter.MyViewHolder holder, final int position) {

            category = categoryList.get(position);

            holder.personname.setText(category.getContact_person());
            holder.servicename.setText(category.getService_title());
            holder.service_id.setText("Service ID : "+category.getService_id());
            holder.datetime.setText(category.getDate()+"\n"+category.getTime_slot());
            holder.contactperson.setText("Contact Person : "+category.getContact_person());
            holder.contactnumber.setText("Contact No. : +91 "+category.getContact_no());
            holder.plotapartment.setText(category.getApart_noplot_no());
            holder.address.setText(category.getAddress());
//            holder.status.setText(category.getStatus());
            holder.citystate.setText(category.getArea()+","+category.getCity()+","+category.getState());
            holder.viewdetails.setVisibility(View.GONE);
            holder.personname.setVisibility(View.GONE);
            holder.viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.contactinfolayout.getVisibility()==View.VISIBLE){
                        holder.viewdetails.setText("VIEW DETAILS");
                        holder.contactinfolayout.setVisibility(View.GONE);

                    }else{
                        holder.viewdetails.setText("VIEW LESS");
                        holder.contactinfolayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.acceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Accept Task!!")
                            .setMessage("Are you sure you want to Accept this Task?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String ii = categoryList.get(position).getOrder_id();
                                    AcceptTask(position, ii);
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

            holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Reject Task!!")
                            .setMessage("Are you sure you want to Reject this Task?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String ii = categoryList.get(position).getOrder_id();
                                    RejectTask(position, ii);
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
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }


        private void RejectTask(final int pos, final String id) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorReject_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    Toast.makeText(mContext, "Task Rejected Successfully", Toast.LENGTH_SHORT).show();

                                    categoryList.remove(pos);
                                    notifyItemRemoved(pos);
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
                    params.put("user_id", session.getUserID());
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

        private void AcceptTask(final int pos, final String id) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorAccept_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    Toast.makeText(mContext, "Task Accepted Successfully", Toast.LENGTH_SHORT).show();

                                    categoryList.remove(pos);
                                    notifyItemRemoved(pos);
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
                    params.put("user_id", session.getUserID());
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


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}