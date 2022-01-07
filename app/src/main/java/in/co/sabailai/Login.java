package in.co.sabailai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import in.co.sabailai.customer.activities.CustomerOTP;
import in.co.sabailai.customer.activities.UserSignUp;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;
import in.co.sabailai.technician.activities.TechnicianLogin;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText userphonenumber_ed;
    Button sendotp;
    SessionManager session;
    ViewDialog progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        userphonenumber_ed = findViewById(R.id.userphonenumber_ed);
        sendotp = findViewById(R.id.sendotp);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userphonenumber_ed.getText().toString().trim().length()==0){
                    userphonenumber_ed.setError("enter phone number");
                    userphonenumber_ed.requestFocus();

                }else if(userphonenumber_ed.getText().toString().trim().length()!=10){
                    userphonenumber_ed.setError("must be 10 digits");
                    userphonenumber_ed.requestFocus();

                }else{
                    ValidateSignin();
                }
            }
        });
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("rtghgujf", "Fetching FCM registration token failed"+task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        session.setFCMId(token);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("rtghgujf", token);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void ValidateSignin() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.SignIn_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {


                                String id = jsonObject.getString("user_id");
                                String otp = jsonObject.getString("otp");
                                String msg = jsonObject.getString("msg");

                                progressbar.hideDialog();

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
                                Intent i = new Intent(Login.this, CustomerOTP.class);
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
                                    Log.d("successresponceVolley", "" + jsonError);

                                    String msg = jsonError.getString("msg");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


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
                params.put("contact no", userphonenumber_ed.getText().toString());
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

    public void gotoTech(View v){

        Intent i = new Intent(Login.this, TechnicianLogin.class);
        startActivity(i);
    }

    public void gotoSignUp(View v){

        Intent i = new Intent(Login.this, UserSignUp.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
