package in.co.sabailai.customer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class SelectLocationAfterLogin extends AppCompatActivity {

    Spinner edcity, edstate;
    Button proceed;
    ArrayList<String> statearr;
    ArrayList<String> cityarr;
    ArrayList<String> pincodearr;
    HashMap<String, ArrayList<String>> hash_statecity;
    HashMap<String, ArrayList<String>> hash_citypin;
    HashMap<String, String> hash_stateid;
    HashMap<String, String> hash_cityid;
    String value, stnm = "", stid = "", ctnm = "", ctid = "";
    SessionManager session;
    ViewDialog progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_after_login);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps
            locationPermission();

        } else { }

        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stid.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_SHORT).show();

                } else if (ctid.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Select City", Toast.LENGTH_SHORT).show();

                } else {

                    session.setSelectedCityName(ctnm+","+stnm);
                    session.setSelectedCityId(ctid);

                    Intent i = new Intent(SelectLocationAfterLogin.this, CustomerDashBoard.class);
                    i.putExtra("intenti", "");
                    startActivity(i);
                    finish();
                }

            }
        });

        edcity = (Spinner) findViewById(R.id.edcity);
        edstate = (Spinner) findViewById(R.id.edstate);


        edstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stnm = edstate.getItemAtPosition(edstate.getSelectedItemPosition()).toString();

                if (stnm.equalsIgnoreCase("Select State") || hash_statecity.get(stnm) == null) {

                    stid = "";
                    cityarr = new ArrayList<String>();
                    cityarr.add(0, "Select City");

                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinnerfront2, cityarr);
                    cityAdapter.setDropDownViewResource(R.layout.spinneritem);
                    edcity.setAdapter(cityAdapter);

                } else {

                    stid = hash_stateid.get(stnm);
                    cityarr = new ArrayList<String>();
                    cityarr = hash_statecity.get(stnm);

                    cityarr.add(0, "Select City");

                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinnerfront2, cityarr);
                    cityAdapter.setDropDownViewResource(R.layout.spinneritem);
                    edcity.setAdapter(cityAdapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });

        edcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ctnm = edcity.getItemAtPosition(edcity.getSelectedItemPosition()).toString();

                if (ctnm.equalsIgnoreCase("Select City")) {

                    ctid = "";
                    pincodearr = new ArrayList<>();

                } else {

                    ctid = hash_cityid.get(ctnm);
                    pincodearr = new ArrayList<>();
                    pincodearr = hash_citypin.get(ctnm);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });


        getLocation();
    }

    private void getLocation() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.Locations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {

                            hash_statecity = new HashMap<>();
                            hash_citypin = new HashMap<>();
                            hash_stateid = new HashMap<>();
                            hash_cityid = new HashMap<>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                JSONArray categoryJSON = jsonObject.getJSONArray("All_loc");

                                for (int i = 0; i < categoryJSON.length(); i++) {

                                    JSONObject jsobjectitem = (JSONObject) categoryJSON.get(i);

                                    String country_id = jsobjectitem.getString("country_id");
                                    String country_name = jsobjectitem.getString("country_name");

                                    if (country_id.equalsIgnoreCase("8")) {

                                        statearr = new ArrayList<>();
                                        JSONArray SubcateJSON = jsobjectitem.getJSONArray("State_list");
                                        for (int k = 0; k < SubcateJSON.length(); k++) {
                                            JSONObject jsobjectSubcate = (JSONObject) SubcateJSON.get(k);

                                            String state_id = jsobjectSubcate.getString("state_id");
                                            String state_name = jsobjectSubcate.getString("state_name");

                                            cityarr = new ArrayList<>();
                                            JSONArray cityJSON = jsobjectSubcate.getJSONArray("city_list");
                                            for (int l = 0; l < cityJSON.length(); l++) {
                                                JSONObject jsobjectcity = (JSONObject) cityJSON.get(l);

                                                String city_id = jsobjectcity.getString("city_id");
                                                String city_name = jsobjectcity.getString("city_name");

                                                pincodearr = new ArrayList<>();
                                                JSONArray pinJSON = jsobjectcity.getJSONArray("pincode_list");
                                                for (int m = 0; m < pinJSON.length(); m++) {
                                                    JSONObject jsobjectpin = (JSONObject) pinJSON.get(m);

                                                    String pin_id = jsobjectpin.getString("pin_id");
                                                    String pincode = jsobjectpin.getString("pincode");

                                                    pincodearr.add(pincode);

                                                }

                                                cityarr.add(city_name);
                                                hash_cityid.put(city_name, city_id);
                                                hash_citypin.put(city_name, pincodearr);

                                            }
                                            statearr.add(state_name);
                                            hash_stateid.put(state_name, state_id);
                                            hash_statecity.put(state_name, cityarr);

                                        }

                                    }

                                }

                                statearr.add(0, "Select State");

                                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.spinnerfront2, statearr);
                                stateAdapter.setDropDownViewResource(R.layout.spinneritem);
                                edstate.setAdapter(stateAdapter);

                                progressbar.hideDialog();
//
                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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
//                        Toast.makeText(SignUp_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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


//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("usr_name", et_email_address.getText().toString());
//                params.put("password", et_password.getText().toString());
//                Log.d("fvsDevbf", "" + params);
//                return params;
//            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void locationPermission() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
