package in.co.sabailai.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import in.co.sabailai.Login;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserSignUp extends AppCompatActivity {

    EditText fullname_ed, phonenumber_ed, email_ed;
    SessionManager session;
    ViewDialog progressbar;
    Button sendotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        fullname_ed = findViewById(R.id.fullname_ed);
        phonenumber_ed = findViewById(R.id.phonenumber_ed);
        email_ed = findViewById(R.id.email_ed);
        sendotp = findViewById(R.id.sendotp);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullname_ed.getText().toString().trim().length()==0){
                    fullname_ed.setError("enter fullname");
                    fullname_ed.requestFocus();

                }else if(phonenumber_ed.getText().toString().trim().length()==0){
                    phonenumber_ed.setError("enter phone number");
                    phonenumber_ed.requestFocus();

                }else if(phonenumber_ed.getText().toString().trim().length()!=10){
                    phonenumber_ed.setError("must be 10 digits");
                    phonenumber_ed.requestFocus();

                }else if(email_ed.getText().toString().trim().length()==0){
                    email_ed.setError("enter email");
                    email_ed.requestFocus();

                }else{
                    SignUp();
                }
            }
        });
    }

    public void gotoSignin(View v){

        finish();
    }

    private void SignUp() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.SignUp_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String id = jsonObject.getString("id");
                                String otp = jsonObject.getString("otp");
//
                                progressbar.hideDialog();
//
                                Intent i = new Intent(UserSignUp.this, CustomerOTP.class);
                                i.putExtra("id", id);
                                i.putExtra("otp", otp);
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

                                    String msg = jsonError.getString("msg");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                params.put("fullname", fullname_ed.getText().toString());
                params.put("contact", phonenumber_ed.getText().toString());
                params.put("email", email_ed.getText().toString());
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
