package in.co.sabailai.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.adapters.CartOfferAdapter;
import in.co.sabailai.customer.adapters.OfferAdapter;
import in.co.sabailai.customer.adapters.ServiceItemAdapter;
import in.co.sabailai.customer.adapters.SubSubCatAdapter;
import in.co.sabailai.customer.models.CartGetSet;
import in.co.sabailai.customer.models.CartOffersGetSet;
import in.co.sabailai.customer.models.CustomServiceGetSet;
import in.co.sabailai.customer.models.OffersGetSet;
import in.co.sabailai.customer.models.ServiceItemGetSet;
import in.co.sabailai.customer.models.SubSubCatGetSet;
import in.co.sabailai.extras.RecyclerTouchListener;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    RecyclerView recycle_rest_cats;
    TextView itemtotl, payble, totalAmount, couponamount;
    RelativeLayout couponlayout;
    LinearLayout coupon_linear, orderlayout, removecoupon;
    SessionManager session;
    ViewDialog progressbar;
    int quantity = 0;
    String carttotal_value;
    String serviceid = "", OfferID = "", CouponCode = "", offercode = "", grand_total = "0", cupon_discount_amount = "0";
    ImageView backicon;
    CartAdapter cartAdapter;
    EditText couponcode_ed;
    ArrayList<CartGetSet> cartarray = new ArrayList<CartGetSet>();
    ArrayList<CartOffersGetSet> offers = new ArrayList<CartOffersGetSet>();
    Dialog dialogBeneficiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderlayout = findViewById(R.id.orderlayout);
        orderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OfferID.length()!=0) {
                    carttotal_value = grand_total;
                }
                Intent i = new Intent(getApplicationContext(), BookingForm.class);
                i.putExtra("couponid", OfferID);
                i.putExtra("totalamt", carttotal_value);
                i.putExtra("discountamt", cupon_discount_amount);
                startActivity(i);
            }
        });
        itemtotl = findViewById(R.id.itemtotl);
        payble = findViewById(R.id.payble);
        totalAmount = findViewById(R.id.totalAmount);
        couponamount = findViewById(R.id.couponamount);
        couponlayout = findViewById(R.id.couponlayout);
        coupon_linear = findViewById(R.id.coupon_linear);
        removecoupon = findViewById(R.id.removecoupon);
        recycle_rest_cats = findViewById(R.id.recycle_rest_cats);

        removecoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_linear.setVisibility(View.VISIBLE);
                couponlayout.setVisibility(View.GONE);
                OfferID = "";
                grand_total = "0";
                cupon_discount_amount = "0";
                couponamount.setText("- ₹ 0");
                itemtotl.setText("₹ "+carttotal_value);
                payble.setText("₹ "+carttotal_value);
                totalAmount.setText("₹ "+carttotal_value);
            }
        });

        coupon_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCouponData();
            }
        });

        getCartITems();
    }

    private void getCartITems() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.getCartItems_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            subcatarray = new ArrayList<SubCatGetSet>();
//                            faqarray = new ArrayList<FAQGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                carttotal_value = jsonObject.getString("total_value");
                                grand_total = carttotal_value;

                                itemtotl.setText("₹ "+carttotal_value);
                                payble.setText("₹ "+carttotal_value);
                                totalAmount.setText("₹ "+carttotal_value);
                                couponamount.setText("-₹ 0");


                                cartarray = new ArrayList<CartGetSet>();
                                JSONArray jsonArrayAll_Cart = jsonObject.getJSONArray("All_Cart");
                                for (int l = 0; l < jsonArrayAll_Cart.length(); l++) {

                                    JSONObject jsobjectitem = jsonArrayAll_Cart.getJSONObject(l);

                                    String service_id = jsobjectitem.getString("service_id");
                                    String service_name = jsobjectitem.getString("service_name");
                                    String service_qty = jsobjectitem.getString("service_qty");
                                    String service_price = jsobjectitem.getString("service_price");


                                    cartarray.add(new CartGetSet(service_id, service_name, service_qty, service_price));
                                }

                                cartAdapter = new CartAdapter(cartarray, Cart.this);
                                recycle_rest_cats.setHasFixedSize(true);
                                recycle_rest_cats.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                recycle_rest_cats.setAdapter(cartAdapter);


                                progressbar.hideDialog();


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
                params.put("city_id", session.getSelectedCityId());
                params.put("user_id", session.getUserID());
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

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

        private Context mContext;
        private List<CartGetSet> categoryList;
        //    private RecyclerTouchListener listener;
        CartGetSet category;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_minus, tv_count, tv_plus;
            public TextView servicename,price;
            public ImageView deleteic;
            public LinearLayout l_add_cart;

            public MyViewHolder(View view) {
                super(view);
                servicename = (TextView) itemView.findViewById(R.id.servicename);
                price = (TextView) itemView.findViewById(R.id.price);
                deleteic = (ImageView) itemView.findViewById(R.id.deleteic);

                tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
                tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
                l_add_cart = (LinearLayout) itemView.findViewById(R.id.l_add_cart);


            }
        }
        public CartAdapter(List<CartGetSet> categoryList, Context mContext) {
            this.mContext = mContext;
            this.categoryList = categoryList;
//        this.listener = listener;
        }
        @Override
        public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_item_layout, parent, false);
            return new CartAdapter.MyViewHolder(itemView);
        }
        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {

            category = categoryList.get(position);

            holder.servicename.setText(category.getService_name());
            holder.tv_count.setText(category.getService_qty());
            holder.price.setText("₹" + category.getService_price());

            holder.tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceid = categoryList.get(position).getService_id();
                    quantity = Integer.parseInt(holder.tv_count.getText().toString());
                    quantity = quantity + 1;
                    UpdateCart(holder.tv_count);
                }
            });

            holder.deleteic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceid = categoryList.get(position).getService_id();
                    DeletefromCart(position);
                }
            });
            holder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    quantity = Integer.parseInt(holder.tv_count.getText().toString());
                    if(quantity==1){

                    }else {
                        quantity = quantity - 1;
                        if (quantity == 0) {
                            DeletefromCart(position);
                        } else {
                            UpdateCart(holder.tv_count);
                        }
                    }
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

        public void removeItem(int position) {
            categoryList.remove(position);
            cartAdapter.notifyDataSetChanged();
//            notifyItemRemoved(position);
        }

        private void UpdateCart(final TextView tv_count) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.updatecart_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {
                                    carttotal_value = jsonObject.getString("Total_price");
                                    grand_total = carttotal_value;

                                    coupon_linear.setVisibility(View.VISIBLE);
                                    couponlayout.setVisibility(View.GONE);
                                    cupon_discount_amount = "0";
                                    itemtotl.setText("₹ "+carttotal_value);
                                    payble.setText("₹ "+carttotal_value);
                                    totalAmount.setText("₹ "+carttotal_value);
                                    couponamount.setText("-₹ 0");
                                    tv_count.setText(""+quantity);

                                    progressbar.hideDialog();

                                } else {
                                    progressbar.hideDialog();
//                                Toast.makeText(context, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(mContext, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                    params.put("service_id", serviceid);
                    params.put("city_id", session.getSelectedCityId());
                    params.put("qty", String.valueOf(quantity));
                    Log.d("fvsDevbf", "" + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

        private void DeletefromCart(final int pos) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.deletefromcart_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    carttotal_value = jsonObject.getString("Total_price");
                                    progressbar.hideDialog();
                                    if(carttotal_value.equalsIgnoreCase("null")){
                                        finish();
                                    }else {


                                        grand_total = carttotal_value;
                                        cupon_discount_amount = "0";
                                        coupon_linear.setVisibility(View.VISIBLE);
                                        couponlayout.setVisibility(View.GONE);

                                        itemtotl.setText("₹ "+carttotal_value);
                                        payble.setText("₹ "+carttotal_value);
                                        totalAmount.setText("₹ "+carttotal_value);
                                        couponamount.setText("-₹ 0");

                                        removeItem(pos);
                                    }


                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(mContext, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(mContext, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                    params.put("service_id", serviceid);
                    params.put("city_id", session.getSelectedCityId());
                    Log.d("fvsDevbf", "" + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }
    }

    public void ShowBeneficiary() {

        dialogBeneficiary = new Dialog(Cart.this, android.R.style.Theme_Light_NoTitleBar);
        dialogBeneficiary.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBeneficiary.setContentView(R.layout.cart_coupon_layout);
        dialogBeneficiary.setCancelable(true);
        dialogBeneficiary.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBeneficiary.setCanceledOnTouchOutside(true);

        TextView applycoupon;
        ImageView back_img;
        RecyclerView coupons;

        couponcode_ed = (EditText) dialogBeneficiary.findViewById(R.id.couponcode_ed);
        coupons = (RecyclerView) dialogBeneficiary.findViewById(R.id.coupons);
        applycoupon = (TextView) dialogBeneficiary.findViewById(R.id.applycoupon);
        back_img = (ImageView) dialogBeneficiary.findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBeneficiary.dismiss();
            }
        });

        coupons.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), coupons, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                CartOffersGetSet beneficiary = offers.get(position);

                OfferID = beneficiary.getCupon_id();
                CouponCode = beneficiary.getDiscount_value();
//                offercode = beneficiary.getCode();
                ApplyCouponn();
//                BeneficiaryID = beneficiary.getID();
//                benfname = beneficiary.getName() + " " + beneficiary.getSurname();
//                benfadd = beneficiary.getAddress() + ", \n" + beneficiary.getCity();
//                dialogBeneficiary.dismiss();
//                ShowConfrmPay();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(couponcode_ed.getText().toString().trim().length()==0){

                }else{
                    offercode = couponcode_ed.getText().toString().trim();
                    ApplyCouponn();
                }


//                AddBeneficiary();
            }
        });

        CartOfferAdapter adpater = new CartOfferAdapter(offers, Cart.this);
        coupons.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
        coupons.setItemAnimator(new DefaultItemAnimator());
        coupons.setAdapter(adpater);



        dialogBeneficiary.show();

    }

    private void getCouponData() {
        progressbar.showDialog();


        Log.d("fvsDevbf", ServerLinks.getCoupons_url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.getCoupons_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();
                        progressbar.hideDialog();
                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {


                                offers = new ArrayList<CartOffersGetSet>();
                                JSONArray offeryarr = jsonObject.getJSONArray("All_Cupon");
                                for (int k = 0; k < offeryarr.length(); k++) {

                                    JSONObject offerarray = offeryarr.getJSONObject(k);

                                    String cupon_id = offerarray.getString("cupon_id");
                                    String cupon_name = offerarray.getString("cupon_name");
                                    String discount_value = offerarray.getString("discount_value");
                                    String minimum_cart_val = offerarray.getString("minimum_cart_val");
                                    String Cupon_status = offerarray.getString("Cupon_status");

//                                    offers.add(new CartOffersGetSet(id, code, description, offer));
                                    offers.add(new CartOffersGetSet(cupon_id, cupon_name, discount_value, minimum_cart_val, Cupon_status));

                                }

                                ShowBeneficiary();

//                                HomeOfferAdapter offerAdapter = new HomeOfferAdapter(offers, Offers.this);
//                                coupons.setHasFixedSize(true);
//                                coupons.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                                coupons.setAdapter(offerAdapter);
//                                coupons.setVisibility(View.VISIBLE);


                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                ShowBeneficiary();
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
                            // ...
                        } else {

                            Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    if (error.networkResponse.statusCode == 401) {
                                        String data = jsonError.getString("message");
                                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                                    } else if (error.networkResponse.statusCode == 404) {
                                        String data = jsonError.getString("message");
                                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                                // Print Error!
                            }

                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getUserID());
                params.put("city_id", session.getSelectedCityId());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void ApplyCouponn() {
        progressbar.showDialog();

        Log.d("fvsDevbf", ServerLinks.applyCoupons_url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.applyCoupons_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(SignUp_Activity.this, response, Toast.LENGTH_LONG).show();
                        progressbar.hideDialog();
                        Log.d("fvsDevbf", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                coupon_linear.setVisibility(View.GONE);
                                couponlayout.setVisibility(View.VISIBLE);

                                cupon_discount_amount = jsonObject.getString("cupon_discount_amount");
                                grand_total = jsonObject.getString("grand_total");
                                couponamount.setText("- ₹ "+cupon_discount_amount);

                                payble.setText("₹ "+grand_total);
                                totalAmount.setText("₹ "+grand_total);

//                                String message = jsonObject.getString("message");
//                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//
//                                JSONObject data = jsonObject.getJSONObject("data");
//                                String disamount = data.getString("DiscountAmount");
//                                String BillAmount = data.getString("BillAmount");
//                                String TotalAmount = data.getString("TotalAmount");
//                                String PayableAmount = data.getString("PayableAmount");
//
//                                couponlayout.setVisibility(View.VISIBLE);
//                                coupon_linear.setVisibility(View.GONE);
//                                couponamount.setText("- ₹"+disamount);
//
//                                payble.setText("₹"+PayableAmount);
//                                totalAmount.setText("₹"+PayableAmount);

                                dialogBeneficiary.dismiss();

//                                discountam = disamount;


                            } else {
                                OfferID = "";
                                CouponCode = "";
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                            // ...
                        } else {

                            Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    if (error.networkResponse.statusCode == 401) {
                                        String data = jsonError.getString("message");
                                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                                    } else if (error.networkResponse.statusCode == 404) {
                                        String data = jsonError.getString("message");
                                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                                // Print Error!
                            }

                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getUserID());
                params.put("city_id", session.getSelectedCityId());
                params.put("cuponcode", OfferID);
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