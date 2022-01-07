package in.co.sabailai.technician.activities;

import androidx.appcompat.app.AppCompatActivity;
import in.co.sabailai.R;
import in.co.sabailai.customer.activities.CustomerOTP;
import in.co.sabailai.customer.activities.SelectLocationAfterLogin;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.chaos.view.PinView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TechnicianOTP extends AppCompatActivity {

    PinView pinView;
    Button verifyotp;
    SessionManager session;
    ViewDialog progressbar;
    String id, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_otp);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        id = getIntent().getStringExtra("id");
        otp = getIntent().getStringExtra("otp");

        pinView = findViewById(R.id.pinView);

        verifyotp = findViewById(R.id.verifyotp);
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinView.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(), "enter OTP", Toast.LENGTH_SHORT).show();

                }else if(pinView.getText().toString().trim().length()!=4){
                    Toast.makeText(getApplicationContext(), "OTP must be 4 digits", Toast.LENGTH_SHORT).show();

                }else if(!pinView.getText().toString().equalsIgnoreCase(otp)){
                    Toast.makeText(getApplicationContext(), "OTP didn't match", Toast.LENGTH_SHORT).show();

                }else{

                    VerifyOTP();
                }

            }
        });
    }

    private void VerifyOTP() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorVerifyOTP_url,
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
//
                                session.setLogin();
//
                                progressbar.hideDialog();
//
                                Intent i = new Intent(TechnicianOTP.this, TechnicianDashBoard.class);
                                i.putExtra("intenti", "");
                                startActivity(i);
                                finish();

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


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", id);
                params.put("fcm_token", session.getFCMId());
                params.put("otp", pinView.getText().toString());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}