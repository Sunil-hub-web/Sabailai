package in.co.sabailai.technician.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;
import in.co.sabailai.technician.activities.TechnicianDashBoard;
import in.co.sabailai.technician.activities.TechnicianOTP;


public class TechnicianProfileFragment extends Fragment {

    ViewDialog progressbar;
    SessionManager session;
    TextView name_ed, email_ed, phone_ed, city_ed, state_ed, country_ed, pincode_ed;
    ImageView profileimage, logoutbtn;

    public TechnicianProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_technician_profile, container, false);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        logoutbtn = v.findViewById(R.id.logoutbtn);
        name_ed = v.findViewById(R.id.name);
        email_ed = v.findViewById(R.id.email);
        phone_ed = v.findViewById(R.id.phone);
        city_ed = v.findViewById(R.id.city);
        state_ed = v.findViewById(R.id.state);
        country_ed = v.findViewById(R.id.country);
        pincode_ed = v.findViewById(R.id.pincode);
        profileimage = v.findViewById(R.id.profileimage);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout!!")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                session.logoutUser();
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

        getProfiledata();
        return v;
    }

    private void getProfiledata() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorProfile_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String img = jsonObject.getString("img");
                                String phone = jsonObject.getString("contact_no");
                                String country = jsonObject.getString("country");
                                String state = jsonObject.getString("state");
                                String city = jsonObject.getString("city");
                                String area = jsonObject.getString("area");
//
                                session.setUserID(id);
                                session.setUserName(name);
                                session.setEmail(email);
                                session.setPhone(phone);
                                session.setImgUrl(img);
                                session.setcountry(country);
                                session.setState(state);
                                session.setCity(city);
                                session.setPinCode(area);
                                session.setUserType("technician");

                                Glide.with(getActivity())
                                        .load(img)
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                                                .error(R.drawable.dummy_profilepic))
                                        .into(profileimage);

                                name_ed.setText(name);
                                email_ed.setText(email);
                                phone_ed.setText(phone);
                                city_ed.setText(city);
                                state_ed.setText(state);
                                country_ed.setText(country);
                                pincode_ed.setText(area);


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
}