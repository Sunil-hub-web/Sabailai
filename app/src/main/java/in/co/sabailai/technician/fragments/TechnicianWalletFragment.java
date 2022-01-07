package in.co.sabailai.technician.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.co.sabailai.technician.models.WalletGetSet;
import in.co.sabailai.technician.models.WalletGetSet;

public class TechnicianWalletFragment extends Fragment {

    ViewDialog progressbar;
    SessionManager session;
    RecyclerView payment_recyclerview;
    TextView walletamount;
    ArrayList<WalletGetSet> walletaray = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technician_wallet, container, false);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        payment_recyclerview = v.findViewById(R.id.payment_recyclerview);
        walletamount = v.findViewById(R.id.walletamount);

        GetWalletDatas();

        return v;
    }

    private void GetWalletDatas() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.vendorWallet_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            completedarray = new ArrayList<CompletedBookingGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String total_amount = jsonObject.getString("total_amount");
                                walletamount.setText(total_amount);

                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_wallet_history");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsobjectitem = jsonArraycategories.getJSONObject(l);

                                    String order_id = jsobjectitem.getString("order_id");
                                    String amount = jsobjectitem.getString("amount");
                                    String contact_person = jsobjectitem.getString("customer_name");
                                    String date = jsobjectitem.getString("date");


                                    walletaray.add(new WalletGetSet(order_id, amount, date, contact_person));

                                }

                                WalletAdapter catAdapter = new WalletAdapter(walletaray, getActivity());
                                payment_recyclerview.setHasFixedSize(true);
                                payment_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                payment_recyclerview.setAdapter(catAdapter);
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

    public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

        private Context mContext;
        private List<WalletGetSet> categoryList;
        //    private RecyclerTouchListener listener;
        WalletGetSet category;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView personname, amount, serviceid, date;

            public MyViewHolder(View view) {
                super(view);
                personname = (TextView) view.findViewById(R.id.personname);
                amount = (TextView) view.findViewById(R.id.amount);
                serviceid = (TextView) view.findViewById(R.id.serviceid);
                date = (TextView) view.findViewById(R.id.date);


            }
        }

        public WalletAdapter(List<WalletGetSet> categoryList, Context mContext) {
            this.mContext = mContext;
            this.categoryList = categoryList;
//        this.listener = listener;
        }

        @Override
        public WalletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wallet_item_layout, parent, false);
            return new WalletAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final WalletAdapter.MyViewHolder holder, int position) {

            category = categoryList.get(position);

            holder.personname.setText(category.getContact_person());
            holder.serviceid.setText("Service ID : "+category.getOrder_id());
            holder.amount.setText("Rs."+category.getAmount());
            holder.date.setText(category.getDate());


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}