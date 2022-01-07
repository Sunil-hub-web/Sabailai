package in.co.sabailai.technician.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;
import in.co.sabailai.technician.activities.TechnicianDashBoard;
import in.co.sabailai.technician.models.OngoingGetSet;

public class TechnicianOngoingFragment extends Fragment {

    ViewDialog progressbar;
    SessionManager session;
    RecyclerView ongoing_recyclerview;
    ArrayList<OngoingGetSet> ongoungarray = new ArrayList<OngoingGetSet>();


    String latitude,longitude;
    Double d_latitude,d_longitude;

    public void mapFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technician_ongoing, container, false);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        ongoing_recyclerview = v.findViewById(R.id.ongoing_recyclerview);


        return v;
//        TechnicianDashBoard.notificationcounter.setText();
    }

    @Override
    public void onResume() {
        super.onResume();
        GetOngoings();
    }

    private void GetOngoings() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorOngoingTasks_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            ongoungarray = new ArrayList<OngoingGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String count = jsonObject.getString("count");
                                TechnicianDashBoard.notificationcounter.setText(count);

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Tasks_ongoing");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String order_id = jsobjectitem.getString("order_id");
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


                                    ongoungarray.add(new OngoingGetSet(order_id, service_id, service_title, date, time_slot, contact_person,
                                            contact_no, apart_noplot_no, address, area, city, State, status,longitute,latitute));

                                }

                                OngoingAdapter catAdapter = new OngoingAdapter(ongoungarray, getActivity());
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

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final OngoingAdapter.MyViewHolder holder, final int position) {

            category = categoryList.get(position);

            holder.personname.setText(category.getContact_person());
            holder.servicename.setText(category.getService_title());
            holder.service_id.setText("Service ID : " + category.getService_id());
            holder.datetime.setText(category.getDate() + "\n" + category.getTime_slot());
            holder.contactperson.setText("Contact Person : " + category.getContact_person());
            holder.contactnumber.setText("Contact No. : +91 " + category.getContact_no());
            holder.plotapartment.setText(category.getApart_noplot_no());
            holder.address.setText(category.getAddress());
            holder.latlong.setText(category.getLongitute()+","+category.getLatitute());


            if(category.getStatus().equalsIgnoreCase("Accepted")){
                holder.status.setText("Start Work");
            }else{
                holder.status.setText("Ongoing");
            }

            holder.citystate.setText(category.getArea() + "," + category.getCity() + "," + category.getState());

            holder.viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.contactinfolayout.getVisibility() == View.VISIBLE) {
                        holder.viewdetails.setText("VIEW DETAILS");
                        holder.contactinfolayout.setVisibility(View.GONE);

                    } else {
                        holder.viewdetails.setText("VIEW LESS");
                        holder.contactinfolayout.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(categoryList.get(position).getStatus().equalsIgnoreCase("Accepted")){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Start Work!!")
                                .setMessage("Are you sure you want to Start this Work?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String ii = categoryList.get(position).getOrder_id();
                                        StartTask(position, ii, holder.status);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }else{

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

                    showmap(d_latitude,d_longitude);
                }
            });

            if((category.getContact_no().equalsIgnoreCase("null") || category.getContact_no().equalsIgnoreCase("")) || (category.getContact_person().equalsIgnoreCase("null") || category.getContact_person().equalsIgnoreCase(""))){
                holder.viewdetails.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
            }else{
                holder.viewdetails.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.VISIBLE);
            }

//            Date today = new Date();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
//            String dateToStr = format.format(today);
//
//            Log.d("fvdsdfdvc", "" + dateToStr);
//            Log.d("fvdsdfdvc", "" + category.getDate());
//            if(dateToStr.equalsIgnoreCase(category.getDate())) {
//
//            String string = category.getTime_slot();
//            String[] parts = string.split(" to ");
//            String part1 = parts[0]; // 004
//            String part2 = parts[1]; // 034556
//
//            if (part1.contains("am")) {
//                part1 = part1.replace("am", "AM");
//            } else if (part1.contains("pm")) {
//                part1 = part1.replace("pm", "PM");
//            }
//            if (part2.contains("am")) {
//                part2 = part2.replace("am", "AM");
//            } else if (part2.contains("pm")) {
//                part2 = part2.replace("pm", "PM");
//            }
//
//            final SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
//            String time = dateFormat.format(new Date());
//            Log.d("htdrdfaz", "" + time);
//
//
//            try {
//                String string1 = part1;
//                Date time1 = new SimpleDateFormat("h:mm a").parse(string1);
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.setTime(time1);
//                calendar1.add(Calendar.DATE, 1);
//
//
//                String string2 = part2;
//                Date time2 = new SimpleDateFormat("h:mm a").parse(string2);
//                Calendar calendar2 = Calendar.getInstance();
//                calendar2.setTime(time2);
//                calendar2.add(Calendar.DATE, 1);
//
//                int hours = new Time(System.currentTimeMillis()).getHours() + 1;
//                int mins = new Time(System.currentTimeMillis()).getHours();
//                int sec = new Time(System.currentTimeMillis()).getHours();
//
//                String someRandomTime = hours + ":" + mins + ":" + sec;
//                Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
//                Calendar calendar3 = Calendar.getInstance();
//                calendar3.setTime(d);
//                calendar3.add(Calendar.DATE, 1);
//
//
//                Log.d("fghjtgbvdrzdfx", "" + Calendar.getInstance().getTime());
//                Log.d("fvdsdfdvc", "" + calendar1.getTime());
//                Log.d("fvdsdfdvc", "" + calendar2.getTime());
//                Log.d("fvdsdfdvc", "" + calendar3.getTime());
//
//                Date x = calendar3.getTime();
//                if (x.after(calendar1.getTime())) {
//                    //checkes whether the current time is between 14:49:00 and 20:11:13.
//
//                    Log.d("fvdsdfdvc", "between");
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//                Log.d("fvdsdfdvc", "" + e);
//            }
//            }

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        private void StartTask(final int pos, final String id, final TextView stat) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorStartWork_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    Toast.makeText(mContext, "Work Started Successfully", Toast.LENGTH_SHORT).show();

                                    stat.setText("Ongoing");
//                                    categoryList.remove(pos);
//                                    notifyItemRemoved(pos);
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

    public void showmap(double latitude,double longitude){


        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.viewaddressdetails);
        //dialog.setCancelable(false);

        //Button btn_dismiss = dialog.findViewById(R.id.btn_dismiss);

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.geofence_map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
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

        /*btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });*/

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


    }

}