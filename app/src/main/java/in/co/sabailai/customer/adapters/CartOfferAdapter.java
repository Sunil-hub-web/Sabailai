package in.co.sabailai.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.models.CartOffersGetSet;

public class CartOfferAdapter extends RecyclerView.Adapter<CartOfferAdapter.ProgramViewHolder> {

    private ArrayList<CartOffersGetSet> foodGetSet;
    Context context;

    public CartOfferAdapter(ArrayList<CartOffersGetSet> foodGetSet, Context context) {
        this.foodGetSet = foodGetSet;
        this.context = context;

    }

    @NonNull
    @Override
    public CartOfferAdapter.ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.coupons_item_list, viewGroup, false);
        return new CartOfferAdapter.ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartOfferAdapter.ProgramViewHolder programViewHolder, final int i) {
        final CartOffersGetSet My_list = foodGetSet.get(i);

//        programViewHolder.detail.setText(My_list.getDetail());
        programViewHolder.name.setText("Get "+My_list.getDiscount_value()+" OFF");
        programViewHolder.code.setText(My_list.getCupon_name());

    }

    @Override
    public int getItemCount() {

        return foodGetSet.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView name, detail, code;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            detail = (TextView) itemView.findViewById(R.id.detail);
            code = (TextView) itemView.findViewById(R.id.code);

        }
    }

}