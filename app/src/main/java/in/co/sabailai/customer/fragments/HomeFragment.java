package in.co.sabailai.customer.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.activities.Cart;
import in.co.sabailai.customer.activities.CustomerDashBoard;
import in.co.sabailai.customer.activities.CustomerOTP;
import in.co.sabailai.customer.activities.SelectLocationAfterLogin;
import in.co.sabailai.customer.activities.ServiceDetails;
import in.co.sabailai.customer.adapters.OfferAdapter;
import in.co.sabailai.customer.adapters.ServiceAdapter;
import in.co.sabailai.customer.adapters.TestimonialAdapter;
import in.co.sabailai.extras.RecyclerTouchListener;
import in.co.sabailai.customer.models.OffersGetSet;
import in.co.sabailai.customer.models.ServiceGetSet;
import in.co.sabailai.customer.models.TestimonialsGetSet;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

public class HomeFragment extends Fragment {

    SessionManager session;
    ViewDialog progressbar;
    RecyclerView service_recycler, offers_recycler, testimonials_recycler;
    private ArrayList<ServiceGetSet> servicearray = new ArrayList<ServiceGetSet>();
    private ArrayList<OffersGetSet> offerarray = new ArrayList<OffersGetSet>();
    private ArrayList<TestimonialsGetSet> testimonialsarray = new ArrayList<TestimonialsGetSet>();
    TextView selectedlocation, itemcounter;
    ImageView locicon;
    LinearLayout offers_layout;
    RelativeLayout cart_layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_home, container, false);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        itemcounter = v.findViewById(R.id.itemcounter);
        offers_layout = v.findViewById(R.id.offers_layout);
        service_recycler = v.findViewById(R.id.service_recycler);
        offers_recycler = v.findViewById(R.id.offers_recycler);
        testimonials_recycler = v.findViewById(R.id.testimonials_recycler);
        selectedlocation = v.findViewById(R.id.selectedlocation);
        locicon = v.findViewById(R.id.locicon);
        cart_layout = v.findViewById(R.id.cart_layout);
        selectedlocation.setText(session.getSelectedCityName());

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps
            locationPermission();

        } else { }

        selectedlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectLocationAfterLogin.class);
                startActivity(i);
            }
        });
        locicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectLocationAfterLogin.class);
                startActivity(i);
            }
        });
        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Cart.class);
                startActivity(i);
            }
        });

        service_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), service_recycler, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                ServiceGetSet catlist = servicearray.get(position);

                Intent i = new Intent(getActivity(), ServiceDetails.class);
                i.putExtra("catid", catlist.getId());
                i.putExtra("catname", catlist.getName());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetHomeData();
    }

    private void GetHomeData() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.Home_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            servicearray = new ArrayList<ServiceGetSet>();
                            testimonialsarray = new ArrayList<TestimonialsGetSet>();
                            offerarray = new ArrayList<OffersGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String total_quantity = jsonObject.getString("total_quantity");
                                itemcounter.setText(total_quantity);

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_category");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String category_id = jsobjectitem.getString("category_id");
                                    String category_name = jsobjectitem.getString("category_name");
                                    String img = jsobjectitem.getString("img");

                                    servicearray.add(new ServiceGetSet(category_id, category_name, img));
                                }

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
                                service_recycler.setLayoutManager(gridLayoutManager);
                                service_recycler.setNestedScrollingEnabled(false);
                                ServiceAdapter serviceAdapter = new ServiceAdapter( servicearray, getActivity());
                                service_recycler.setAdapter(serviceAdapter);

                                JSONArray jsonArraytestimonials = jsonObject.getJSONArray("All_Testimonial");
                                for (int j = 0; j < jsonArraytestimonials.length(); j++) {

                                    JSONObject jstobjectitem = jsonArraytestimonials.getJSONObject(j);

                                    String testimonial_id = jstobjectitem.getString("testimonial_id");
                                    String username = jstobjectitem.getString("username");
                                    String message = jstobjectitem.getString("message");
                                    String img = jstobjectitem.getString("img");

                                    testimonialsarray.add(new TestimonialsGetSet(img));
                                }

                                TestimonialAdapter testiAdapter = new TestimonialAdapter(testimonialsarray, getActivity());
                                testimonials_recycler.setHasFixedSize(true);
                                testimonials_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                testimonials_recycler.setAdapter(testiAdapter);

                                JSONArray jsonArrayoffers = jsonObject.getJSONArray("All_offer");
                                for (int k = 0; k < jsonArrayoffers.length(); k++) {

                                    JSONObject jsoobjectitem = jsonArrayoffers.getJSONObject(k);

//                                    String testimonial_id = jsoobjectitem.getString("testimonial_id");

                                    offerarray.add(new OffersGetSet(R.drawable.offer_a, "15% off on all\nPlumbing Services*"));
                                }

                                if(offerarray.size()==0){
                                    offers_layout.setVisibility(View.GONE);
                                }else {
                                    OfferAdapter catAdapter = new OfferAdapter(offerarray, getActivity());
                                    offers_recycler.setHasFixedSize(true);
                                    offers_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                    offers_recycler.setAdapter(catAdapter);

                                    offers_layout.setVisibility(View.VISIBLE);
                                }
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
                params.put("city_id", session.getSelectedCityId());
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

    public void locationPermission() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Enable Your GPS Location").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                //getLocation();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
