package in.co.sabailai.technician.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.technician.fragments.TechnicianOngoingFragment;
import in.co.sabailai.technician.models.VendorNotiGetSet;

public class VendorNotiAdapter extends RecyclerView.Adapter<VendorNotiAdapter.MyViewHolder> {

    private Context mContext;
    private List<VendorNotiGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    VendorNotiGetSet category;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notititle, serviceid, datetime;

        public MyViewHolder(View view) {
            super(view);
            notititle = (TextView) view.findViewById(R.id.notititle);
            serviceid = (TextView) view.findViewById(R.id.serviceid);
            datetime = (TextView) view.findViewById(R.id.datetime);


        }
    }

    public VendorNotiAdapter(List<VendorNotiGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }

    @Override
    public VendorNotiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_layout, parent, false);
        return new VendorNotiAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VendorNotiAdapter.MyViewHolder holder, final int position) {

        category = categoryList.get(position);

        holder.notititle.setText(category.getMessage());
        holder.serviceid.setText("Service ID : " + category.getBooking_id());
        holder.datetime.setText(category.getDate());

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