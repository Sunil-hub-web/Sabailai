package in.co.sabailai.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.adapters.FAQAdapter;
import in.co.sabailai.customer.adapters.OfferAdapter;
import in.co.sabailai.customer.adapters.ServiceAdapter;
import in.co.sabailai.customer.adapters.SubCatAdapter;
import in.co.sabailai.customer.adapters.TestimonialAdapter;
import in.co.sabailai.customer.models.FAQGetSet;
import in.co.sabailai.customer.models.OffersGetSet;
import in.co.sabailai.customer.models.ServiceGetSet;
import in.co.sabailai.customer.models.SubCatGetSet;
import in.co.sabailai.customer.models.TestimonialsGetSet;
import in.co.sabailai.extras.RecyclerTouchListener;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceDetails extends AppCompatActivity {

    SessionManager session;
    ViewDialog progressbar;
    ImageView backicon, noservices;
    RecyclerView subcatrecycler, faqrecycler;
    TextView aboutcategory, cat_name, reviewcount, ratings;
    String catid, catname;
    ArrayList<SubCatGetSet> subcatarray = new ArrayList<SubCatGetSet>();
    ArrayList<FAQGetSet> faqarray = new ArrayList<FAQGetSet>();

    NestedScrollView mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        catid = getIntent().getStringExtra("catid");
        catname = getIntent().getStringExtra("catname");

        subcatrecycler = findViewById(R.id.subcatrecycler);
        faqrecycler = findViewById(R.id.faqrecycler);
        aboutcategory = findViewById(R.id.aboutcategory);
        cat_name = findViewById(R.id.cat_name);
        ratings = findViewById(R.id.ratings);
        reviewcount = findViewById(R.id.reviewcount);
        mainlayout = findViewById(R.id.mainlayout);
        noservices = findViewById(R.id.noservicesavailable);

        cat_name.setText(catname);

        subcatrecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), subcatrecycler, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                SubCatGetSet catlist = subcatarray.get(position);

                Intent i = new Intent(getApplicationContext(), ServiceList.class);
                i.putExtra("subcatid", catlist.getId());
                i.putExtra("catname", catname);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    protected void onResume() {
        GetSubCatDetails();
        super.onResume();
    }

    private void GetSubCatDetails() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.CategoryPage_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            subcatarray = new ArrayList<SubCatGetSet>();
                            faqarray = new ArrayList<FAQGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String About = jsonObject.getString("About");
                                String Avg_rating = jsonObject.getString("Avg_rating");
                                String Total_review = jsonObject.getString("Total_review");

                                ratings.setText(Avg_rating+" out of 5 stars");
                                reviewcount.setText(Total_review+" reviews");

                                aboutcategory.setText(About);

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_subcategory");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String sub_category_id = jsobjectitem.getString("sub_category_id");
                                    String sub_category_name = jsobjectitem.getString("sub_category_name");

                                    subcatarray.add(new SubCatGetSet(sub_category_id, sub_category_name));
                                }

                                SubCatAdapter testiAdapter = new SubCatAdapter(subcatarray, ServiceDetails.this);
                                subcatrecycler.setHasFixedSize(true);
                                subcatrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                subcatrecycler.setAdapter(testiAdapter);

                                JSONArray jsonArraytestimonials = jsonObject.getJSONArray("All_FAQs");
                                for (int j = 0; j < jsonArraytestimonials.length(); j++) {

                                    JSONObject jstobjectitem = jsonArraytestimonials.getJSONObject(j);

                                    String question = jstobjectitem.getString("question");
                                    String answer = jstobjectitem.getString("answer");

                                    faqarray.add(new FAQGetSet(question, answer));
                                }

                                FAQAdapter faqAdapter = new FAQAdapter(faqarray, ServiceDetails.this);
                                faqrecycler.setHasFixedSize(true);
                                faqrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                faqrecycler.setAdapter(faqAdapter);

                                if(subcatarray.size()==0){
                                    mainlayout.setVisibility(View.GONE);
                                    noservices.setVisibility(View.VISIBLE);
                                }else{
                                    mainlayout.setVisibility(View.VISIBLE);
                                    noservices.setVisibility(View.GONE);
                                }

//
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
                params.put("category_id", catid);
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
