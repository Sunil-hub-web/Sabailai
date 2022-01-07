package in.co.sabailai.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.adapters.FAQAdapter;
import in.co.sabailai.customer.adapters.SubCatAdapter;
import in.co.sabailai.customer.adapters.SubSubCatAdapter;
import in.co.sabailai.customer.models.CustomServiceGetSet;
import in.co.sabailai.customer.models.FAQGetSet;
import in.co.sabailai.customer.models.ServiceGetSet;
import in.co.sabailai.customer.models.ServiceItemGetSet;
import in.co.sabailai.customer.models.SubCatGetSet;
import in.co.sabailai.customer.models.SubSubCatGetSet;
import in.co.sabailai.extras.RecyclerTouchListener;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceList extends AppCompatActivity {

    ImageView backicon;
    TextView continuetxt, itemcounter, count, crtprice, servicetitle;
    RelativeLayout footer, cart_layout;
    RecyclerView subcatrecycler, servicelistrecycler;
    ArrayList<ServiceItemGetSet> itemarray = new ArrayList<ServiceItemGetSet>();
    ArrayList<ServiceItemGetSet> filtereditemarray = new ArrayList<ServiceItemGetSet>();
    ArrayList<SubSubCatGetSet> subcatarray = new ArrayList<SubSubCatGetSet>();
    ArrayList<CustomServiceGetSet> customizationarray = new ArrayList<CustomServiceGetSet>();
    SessionManager session;
    public static ViewDialog progressbar;
    String subcatid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        subcatid = getIntent().getStringExtra("subcatid");

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        servicetitle = findViewById(R.id.servicetitle);
        servicetitle.setText(getIntent().getStringExtra("catname"));

        cart_layout = findViewById(R.id.cart_layout);
        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Cart.class);
                startActivity(i);
            }
        });

        footer = findViewById(R.id.footer);
        count = findViewById(R.id.count);
        crtprice = findViewById(R.id.crtprice);
        itemcounter = findViewById(R.id.itemcounter);
        continuetxt = findViewById(R.id.continuetxt);
        continuetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Cart.class);
                startActivity(i);
            }
        });

        subcatrecycler = findViewById(R.id.subcatrecycler);
        servicelistrecycler = findViewById(R.id.servicelistrecycler);

        subcatrecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), subcatrecycler, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                SubSubCatGetSet catlist = subcatarray.get(position);

                filtereditemarray = new ArrayList<ServiceItemGetSet>();
                filtereditemarray = catlist.getItemarray();

                ServiceItemAdapter catAdapter = new ServiceItemAdapter(filtereditemarray, ServiceList.this);
                servicelistrecycler.setHasFixedSize(true);
                servicelistrecycler.setLayoutManager(new LinearLayoutManager(ServiceList.this));
                servicelistrecycler.setAdapter(catAdapter);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        GetSubSubCatDetails();
    }

    private void GetSubSubCatDetails() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.SubCategoryPage_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            subcatarray = new ArrayList<SubCatGetSet>();
//                            faqarray = new ArrayList<FAQGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String total_quantity = jsonObject.getString("total_quantity");
                                String Total_price = jsonObject.getString("Total_price");
                                crtprice.setText("₹"+Total_price);
                                itemcounter.setText(total_quantity);
                                count.setText(total_quantity);
                                if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
                                    footer.setVisibility(View.GONE);
                                }else{
                                    footer.setVisibility(View.VISIBLE);
                                }

                                subcatarray = new ArrayList<SubSubCatGetSet>();
                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Product");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String sub_sub_category_id = jsobjectitem.getString("sub_sub_category_id");
                                    String sub_sub_category_name = jsobjectitem.getString("sub_sub_category_name");


                                    itemarray = new ArrayList<ServiceItemGetSet>();
                                    JSONArray jsonArrayAllservices = jsobjectitem.getJSONArray("Allservices");
                                    for (int j = 0; j < jsonArrayAllservices.length(); j++) {

                                        JSONObject jstobjectitem = jsonArrayAllservices.getJSONObject(j);

                                        String service_id = jstobjectitem.getString("service_id");
                                        String service_title = jstobjectitem.getString("service_title");
                                        String description = jstobjectitem.getString("description");
                                        String total_no_of_rating = jstobjectitem.getString("total_no_of_rating");
                                        String price_id = jstobjectitem.getString("price_id");
                                        String regular_price = jstobjectitem.getString("regular_price");
                                        String discouted_price = jstobjectitem.getString("discouted_price");
                                        String image = jstobjectitem.getString("image");
                                        String present_in_cart = jstobjectitem.getString("present_in_cart");
                                        String total_qty = jstobjectitem.getString("total_qty");

                                        customizationarray = new ArrayList<CustomServiceGetSet>();
                                        JSONArray jsonArrayAll_custom_service = jstobjectitem.getJSONArray("All_custom_service");
                                        for (int k = 0; k < jsonArrayAll_custom_service.length(); k++) {

                                            JSONObject custobjectitem = jsonArrayAll_custom_service.getJSONObject(k);

                                            String c_service_id = custobjectitem.getString("service_id");
                                            String c_service_title = custobjectitem.getString("service_title");
                                            String c_price = custobjectitem.getString("price");
                                            String c_image = custobjectitem.getString("image");
                                            String checked = custobjectitem.getString("checked");

                                            customizationarray.add(new CustomServiceGetSet(c_service_id, c_service_title, c_image, c_price, checked));
                                        }

                                        itemarray.add(new ServiceItemGetSet(service_id, service_title, description, total_no_of_rating, price_id, regular_price, discouted_price, image, present_in_cart, total_qty, customizationarray));
                                    }

                                    subcatarray.add(new SubSubCatGetSet(sub_sub_category_id, sub_sub_category_name, itemarray));
                                }

                                SubSubCatAdapter scatAdapter = new SubSubCatAdapter(subcatarray, ServiceList.this);
                                subcatrecycler.setHasFixedSize(true);
                                subcatrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                subcatrecycler.setAdapter(scatAdapter);


                                filtereditemarray = new ArrayList<ServiceItemGetSet>();
                                filtereditemarray = subcatarray.get(0).getItemarray();

                                ServiceItemAdapter catAdapter = new ServiceItemAdapter(filtereditemarray, ServiceList.this);
                                servicelistrecycler.setHasFixedSize(true);
                                servicelistrecycler.setLayoutManager(new LinearLayoutManager(ServiceList.this));
                                servicelistrecycler.setAdapter(catAdapter);

//                                if(subcatarray.size()==0){
//                                    mainlayout.setVisibility(View.GONE);
//                                    noservices.setVisibility(View.VISIBLE);
//                                }else{
//                                    mainlayout.setVisibility(View.VISIBLE);
//                                    noservices.setVisibility(View.GONE);
//                                }

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
                params.put("city_id", session.getSelectedCityId());
                params.put("user_id", session.getUserID());
                params.put("subcategory_id", subcatid);
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

    public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.MyViewHolder> implements Filterable {

        private LayoutInflater inflater;
        private Context context;
        private List<ServiceItemGetSet> accepetedJobLists;
        private List<ServiceItemGetSet> filteredlist;
        public int cnt;
        public ArrayList<CustomServiceGetSet> customizationarray = new ArrayList<CustomServiceGetSet>();
        Dialog dialogConfirm;
        boolean ifnotselected;
        SessionManager session;
        String serviceid, discountprice, customserviceids;
        int selectionprice = 0;
        int quantity = 0;

        public ServiceItemAdapter(List<ServiceItemGetSet> accepetedJobLists, Context context) {
            this.accepetedJobLists = accepetedJobLists;
            this.filteredlist = accepetedJobLists;
            this.context = context;
        }

        @Override
        public ServiceItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.from(parent.getContext()).inflate(R.layout.service_list_item_layout, parent, false);
            ServiceItemAdapter.MyViewHolder holder = new ServiceItemAdapter.MyViewHolder(view);
            return holder;
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final ServiceItemAdapter.MyViewHolder holder, final int position) {

            ServiceItemGetSet movie = filteredlist.get(position);


            holder.servicename.setText(movie.getService_title());
            holder.ratings.setText("★ ");
            holder.ratingcount.setText(movie.getTotal_no_of_rating() + " ratings");
            holder.price.setText("₹" + movie.getDiscouted_price());
            holder.description.setText(movie.getDescription());

            Glide.with(context).load(
                    movie.getImage()).into(holder.imag_food);

            holder.addtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    session = new SessionManager(context);
                    customserviceids = "";
                    serviceid = filteredlist.get(position).getService_id();
                    discountprice = filteredlist.get(position).getDiscouted_price();

                    AddtoCart(holder.addtext, holder.tv_count, holder.l_add_cart);

                }
            });
            holder.tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    session = new SessionManager(context);
                    serviceid = filteredlist.get(position).getService_id();
                    quantity = Integer.parseInt(holder.tv_count.getText().toString());
                    quantity = quantity + 1;
                    UpdateCart(holder.tv_count);
                }
            });
            holder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    session = new SessionManager(context);
                    serviceid = filteredlist.get(position).getService_id();
                    quantity = Integer.parseInt(holder.tv_count.getText().toString());
                    quantity = quantity - 1;
                    if (quantity == 0) {
                        DeletefromCart(holder.addtext, holder.l_add_cart);
                    } else {
                        UpdateCart(holder.tv_count);
                    }
                }
            });

            if (movie.getCustomservarray().size() == 0) {
                holder.custom.setVisibility(View.GONE);
            } else {
                holder.custom.setVisibility(View.VISIBLE);
            }

            holder.custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session = new SessionManager(context);
                    serviceid = filteredlist.get(position).getService_id();
                    customizationarray = new ArrayList<CustomServiceGetSet>();
                    customizationarray = filteredlist.get(position).getCustomservarray();
                    ShowConfrmPay(holder.price, filteredlist.get(position).getService_title(), filteredlist.get(position).getService_id(), holder.addtext, holder.tv_count, holder.l_add_cart);
//                ShowConfrmPay(holder.tv_count, holder.l_add_cart, holder.addtext, filteredlist.get(position).getService_title(), filteredlist.get(position).getService_id());
                }
            });

            if (movie.getIfaddedtocart().equalsIgnoreCase("0")) {
                holder.addtext.setVisibility(View.VISIBLE);
                holder.l_add_cart.setVisibility(View.GONE);
                holder.tv_count.setText("0");
            } else {
                holder.addtext.setVisibility(View.GONE);
                holder.l_add_cart.setVisibility(View.VISIBLE);
                holder.tv_count.setText(movie.getCartquantity());
            }

        }

        @Override
        public int getItemCount() {

            return filteredlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView addtext, tv_minus, tv_count, tv_plus, custom;
            public TextView servicename, ratings, ratingcount, price, description;
            public ImageView imag_food;
            public LinearLayout l_add_cart;

            public MyViewHolder(View itemView) {
                super(itemView);

                custom = (TextView) itemView.findViewById(R.id.custom);
                servicename = (TextView) itemView.findViewById(R.id.servicename);
                ratings = (TextView) itemView.findViewById(R.id.ratings);
                ratingcount = (TextView) itemView.findViewById(R.id.ratingcount);
                price = (TextView) itemView.findViewById(R.id.price);
                description = (TextView) itemView.findViewById(R.id.description);
                imag_food = (ImageView) itemView.findViewById(R.id.imag_food);

                addtext = (TextView) itemView.findViewById(R.id.addtext);
                tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
                tv_count = (TextView) itemView.findViewById(R.id.tv_count);
                tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
                l_add_cart = (LinearLayout) itemView.findViewById(R.id.l_add_cart);

            }
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        filteredlist = accepetedJobLists;
                    } else {
                        ArrayList<ServiceItemGetSet> filteredList = new ArrayList<>();
                        for (ServiceItemGetSet row : accepetedJobLists) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getService_title().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        filteredlist = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredlist;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filteredlist = (ArrayList<ServiceItemGetSet>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void ShowConfrmPay(final TextView price, String servtit, String servtid, final TextView addtext, final TextView tv_count, final LinearLayout l_add_cart) {

            dialogConfirm = new Dialog(context);
            dialogConfirm.getWindow().setWindowAnimations(R.style.DialogAnimation);
//        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogConfirm.setContentView(R.layout.customizable_layout);
            dialogConfirm.setCancelable(true);
//        dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogConfirm.setCanceledOnTouchOutside(true);
            dialogConfirm.getWindow().setGravity(Gravity.BOTTOM);
            Window window = dialogConfirm.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            TextView dialheading;
            ImageView close;
            RecyclerView itemsincart;
            Button addtocart;

            itemsincart = dialogConfirm.findViewById(R.id.itemsincart);
            addtocart = dialogConfirm.findViewById(R.id.addtocart);

            dialheading = dialogConfirm.findViewById(R.id.dialheading);

            dialheading.setText(servtit);

            close = dialogConfirm.findViewById(R.id.close_btn);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogConfirm.dismiss();

                }
            });

            ServiceItemAdapter.CustomServiceAdapter cadpater = new ServiceItemAdapter.CustomServiceAdapter(context, customizationarray);
            itemsincart.setLayoutManager(new LinearLayoutManager(context));
            itemsincart.setItemAnimator(new DefaultItemAnimator());
            itemsincart.setAdapter(cadpater);

            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectionprice = 0;
                    ifnotselected = false;
                    customserviceids = "";
                    for (int i = 0; i < customizationarray.size(); i++) {

                        if (customizationarray.get(i).getIftaken().equalsIgnoreCase("1")) {
                            int pri = Integer.parseInt(customizationarray.get(i).getPrice());
                            selectionprice = selectionprice + pri;
                            if (customserviceids.length() == 0) {
                                customserviceids = customizationarray.get(i).getService_id();
                            } else {
                                customserviceids = customserviceids + "," + customizationarray.get(i).getService_id();
                            }
                            ifnotselected = true;
                            Log.d("gdzxfved", customizationarray.get(i).getService_title());
                        }
                    }

                    if (ifnotselected == true) {


                        price.setText("₹" + selectionprice);
                        discountprice = String.valueOf(selectionprice);

                        AddCustomServtoCart(addtext, tv_count, l_add_cart);

                    } else {
                        Toast.makeText(context, "Select Items to proceed", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            dialogConfirm.show();

        }

        public class CustomServiceAdapter extends RecyclerView.Adapter<ServiceItemAdapter.CustomServiceAdapter.MyViewHolder> {

            private Context mContext;
            private List<CustomServiceGetSet> categoryList;
            CustomServiceGetSet category;


            public class MyViewHolder extends RecyclerView.ViewHolder {

                public TextView customservicename, customserviceprice;
                public ImageView imag_service;
                public CheckBox servicetaken;
                public RelativeLayout mainlay;

                public MyViewHolder(View view) {
                    super(view);

                    mainlay = (RelativeLayout) view.findViewById(R.id.mainlay);
                    imag_service = (ImageView) view.findViewById(R.id.imag_service);
                    customservicename = (TextView) view.findViewById(R.id.customservicename);
                    customserviceprice = (TextView) view.findViewById(R.id.customserviceprice);
                    servicetaken = (CheckBox) view.findViewById(R.id.servicetaken);

                }
            }

            public CustomServiceAdapter(Context mContext, List<CustomServiceGetSet> categoryList) {
                this.mContext = mContext;
                this.categoryList = categoryList;
            }

            @Override
            public ServiceItemAdapter.CustomServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_service_layout, parent, false);
                return new ServiceItemAdapter.CustomServiceAdapter.MyViewHolder(itemView);
            }

            @SuppressLint("RecyclerView")
            @Override
            public void onBindViewHolder(final ServiceItemAdapter.CustomServiceAdapter.MyViewHolder holder, final int pos) {

                category = categoryList.get(pos);

                holder.customservicename.setText(category.getService_title());
                holder.customserviceprice.setText("₹" + category.getPrice());
                Glide.with(context).load(
                        category.getImage()).into(holder.imag_service);

                if (category.getIftaken().equalsIgnoreCase("0")) {
                    holder.servicetaken.setChecked(false);
                } else {
                    holder.servicetaken.setChecked(true);
                }

                holder.mainlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.servicetaken.isChecked()) {
                            holder.servicetaken.setChecked(false);
                        } else {
                            holder.servicetaken.setChecked(true);
                        }
                    }
                });

                holder.customservicename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.servicetaken.isChecked()) {
                            holder.servicetaken.setChecked(false);
                        } else {
                            holder.servicetaken.setChecked(true);
                        }
                    }
                });

                holder.imag_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.servicetaken.isChecked()) {
                            holder.servicetaken.setChecked(false);
                        } else {
                            holder.servicetaken.setChecked(true);
                        }
                    }
                });

                holder.servicetaken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                   @Override
                                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                                       if (isChecked == true) {
                                                                           categoryList.get(pos).setIftaken("1");
                                                                       } else {
                                                                           categoryList.get(pos).setIftaken("0");
                                                                       }
                                                                   }
                                                               }
                );

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

        private void AddtoCart(final TextView addtext, final TextView tv_count, final LinearLayout l_add_cart) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.addtocart_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    addtext.setVisibility(View.GONE);
                                    l_add_cart.setVisibility(View.VISIBLE);
                                    tv_count.setText("1");
//                                dialogConfirm.cancel();

                                    String total_quantity = jsonObject.getString("total_quantity");
                                    String Total_price = jsonObject.getString("Total_price");
                                    crtprice.setText("₹"+Total_price);
                                    itemcounter.setText(total_quantity);
                                    count.setText(total_quantity);
                                    if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
                                        footer.setVisibility(View.GONE);
                                    }else{
                                        footer.setVisibility(View.VISIBLE);
                                    }

                                    progressbar.hideDialog();

                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(context, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                    params.put("discounted_price", discountprice);
                    params.put("quantity", "1");
                    params.put("customise_id", "");
                    Log.d("fvsDevbf", "" + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

        private void AddCustomServtoCart(final TextView addtext, final TextView tv_count, final LinearLayout l_add_cart) {

            progressbar.showDialog();
            Log.d("fvsDevbf", "starts");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.addtocart_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    addtext.setVisibility(View.GONE);
                                    l_add_cart.setVisibility(View.VISIBLE);
                                    tv_count.setText("1");
                                    dialogConfirm.cancel();

                                    String total_quantity = jsonObject.getString("total_quantity");
                                    String Total_price = jsonObject.getString("Total_price");
                                    crtprice.setText("₹"+Total_price);
                                    itemcounter.setText(total_quantity);
                                    count.setText(total_quantity);
                                    if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
                                        footer.setVisibility(View.GONE);
                                    }else{
                                        footer.setVisibility(View.VISIBLE);
                                    }

                                    progressbar.hideDialog();

                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(context, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                    params.put("discounted_price", discountprice);
                    params.put("quantity", "1");
                    params.put("customise_id", customserviceids);
                    Log.d("fvsDevbf", "" + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
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

                                    tv_count.setText("" + quantity);

                                    String total_quantity = jsonObject.getString("total_quantity");
                                    String Total_price = jsonObject.getString("Total_price");
                                    crtprice.setText("₹"+Total_price);
                                    itemcounter.setText(total_quantity);
                                    count.setText(total_quantity);
                                    if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
                                        footer.setVisibility(View.GONE);
                                    }else{
                                        footer.setVisibility(View.VISIBLE);
                                    }

                                    progressbar.hideDialog();

                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(context, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

        private void DeletefromCart(final TextView addtext, final LinearLayout l_add_cart) {

            progressbar.showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.deletefromcart_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("fvsDevbf", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("success").equals("true")) {

                                    addtext.setVisibility(View.VISIBLE);
                                    l_add_cart.setVisibility(View.GONE);

                                    String total_quantity = jsonObject.getString("total_quantity");
                                    String Total_price = jsonObject.getString("Total_price");
                                    crtprice.setText("₹"+Total_price);
                                    itemcounter.setText(total_quantity);
                                    count.setText(total_quantity);
                                    if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
                                        footer.setVisibility(View.GONE);
                                    }else{
                                        footer.setVisibility(View.VISIBLE);
                                    }

                                    progressbar.hideDialog();

                                } else {
                                    progressbar.hideDialog();
                                    Toast.makeText(context, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                                Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

    }

}
//    String total_quantity = jsonObject.getString("total_quantity");
//    String Total_price = jsonObject.getString("Total_price");
//                                crtprice.setText("₹"+Total_price);
//                                        itemcounter.setText(total_quantity);
//                                        count.setText(total_quantity);
//                                        if(total_quantity.equalsIgnoreCase("null") || total_quantity.equalsIgnoreCase("0")){
//                                        footer.setVisibility(View.GONE);
//                                        }else{
//                                        footer.setVisibility(View.VISIBLE);
//                                        }