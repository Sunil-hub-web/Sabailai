package in.co.sabailai.technician.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.co.sabailai.R;
import in.co.sabailai.customer.activities.BookingForm;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;
import in.co.sabailai.technician.activities.ShowLocationOnMap;
import in.co.sabailai.technician.models.OngoingGetSet;


public class TechnicianHistoryFragment extends Fragment {

    ViewDialog progressbar;
    SessionManager session;
    RecyclerView ongoing_recyclerview;
    ArrayList<OngoingGetSet> ongoungarray = new ArrayList<OngoingGetSet>();
    SwipeRefreshLayout swiprefresh;

    String latitude,longitude;
    Double d_latitude,d_longitude;

    OngoingAdapter catAdapter;


    public TechnicianHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_technician_history, container, false);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        ongoing_recyclerview = v.findViewById(R.id.ongoing_recyclerview);
        swiprefresh = v.findViewById(R.id.swiprefresh);

        swiprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetHistory();
                catAdapter.notifyDataSetChanged();

                swiprefresh.setRefreshing(false);

            }
        });

        GetHistory();
        return v;
    }

    private void GetHistory() {

        ongoungarray.clear();

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorHistory_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Taskhistory");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String service_id = jsobjectitem.getString("service_id");
                                    String service_title = jsobjectitem.getString("service_title");
                                    String contact_person = jsobjectitem.getString("contact_person");
                                    String date = jsobjectitem.getString("date");
                                    String area = jsobjectitem.getString("area");
                                    String address = jsobjectitem.getString("address");
                                    String contact_no = jsobjectitem.getString("contact_no");
                                    String apart_noplot_no = jsobjectitem.getString("apart_no/plot_no");
                                    String time_slot = jsobjectitem.getString("time_slot");
                                    String city = jsobjectitem.getString("city");
                                    String State = jsobjectitem.getString("State");
                                    String status = jsobjectitem.getString("status");
                                    String longitute = jsobjectitem.getString("longitute");
                                    String latitute = jsobjectitem.getString("latitute");


                                    ongoungarray.add(new OngoingGetSet("", service_id, service_title, date, time_slot, contact_person,
                                            contact_no, apart_noplot_no, address, area, city, State, status,longitute,latitute));

                                }

                                catAdapter = new OngoingAdapter(ongoungarray, getActivity());
                                ongoing_recyclerview.setHasFixedSize(true);
                                ongoing_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                ongoing_recyclerview.setAdapter(catAdapter);
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

    public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.MyViewHolder> {

        private Context mContext;
        private List<OngoingGetSet> categoryList;
        //    private RecyclerTouchListener listener;
        OngoingGetSet category;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView personname, servicename, service_id, datetime, contactperson, contactnumber, plotapartment,
                    address, citystate, status, viewdetails,latlong,viewaddress;
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
                status = (TextView) view.findViewById(R.id.status);
                viewdetails = (TextView) view.findViewById(R.id.viewdetails);
                contactinfolayout = (LinearLayout) view.findViewById(R.id.contactinfolayout);
                latlong =  view.findViewById(R.id.latlong);
                viewaddress =  view.findViewById(R.id.viewaddress);


            }
        }

        public OngoingAdapter(List<OngoingGetSet> categoryList, Context mContext) {
            this.mContext = mContext;
            this.categoryList = categoryList;
//        this.listener = listener;
        }

        @Override
        public OngoingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ongoing_item_layout, parent, false);
            return new OngoingAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final OngoingAdapter.MyViewHolder holder, int position) {

            category = categoryList.get(position);

            holder.personname.setText(category.getContact_person());
            holder.servicename.setText(category.getService_title());
            holder.service_id.setText("Service ID : "+category.getService_id());
            holder.datetime.setText(category.getDate()+"\n"+category.getTime_slot());
            holder.contactperson.setText("Contact Person : "+category.getContact_person());
            holder.contactnumber.setText("Contact No. : +91 "+category.getContact_no());
            holder.plotapartment.setText(category.getApart_noplot_no());
            holder.address.setText(category.getAddress());
            holder.status.setText(category.getStatus());
            holder.citystate.setText(category.getArea()+","+category.getCity()+","+category.getState());
            holder.latlong.setText(category.getLongitute()+","+category.getLatitute());


            //holder.viewdetails.setVisibility(View.GONE);

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

            holder.viewaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    latitude = category.getLatitute();
                    longitude = category.getLongitute();

                    d_latitude = Double.valueOf(latitude);
                    d_longitude = Double.valueOf(longitude);

                    if(latitude.equals("null")){

                        Toast.makeText(mContext, "Address Not Found", Toast.LENGTH_SHORT).show();

                    }else {

                        //showmap(d_latitude,d_longitude);

                        Intent intent = new Intent(getContext(), ShowLocationOnMap.class);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);
                        startActivity(intent);
                    }


                }
            });
            if(category.getStatus().equalsIgnoreCase("Complete")) {
                holder.status.setBackgroundColor(Color.parseColor("#275800"));
            }else{
                holder.status.setBackgroundColor(Color.parseColor("#ff0000"));
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

   /* public void showmap(double latitude,double longitude){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.viewaddressdetails);
        dialog.setCancelable(false);

        Button btn_dismiss = dialog.findViewById(R.id.btn_dismiss);

        SupportMapFragment mapFragment1 = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.geofence_map);

        mapFragment1.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(longitude,latitude))
                        .zoom(17)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(longitude,latitude ))
                        .title("Spider Man").snippet("His Talent : Plenty of money"));

                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);

            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


    }*/
}