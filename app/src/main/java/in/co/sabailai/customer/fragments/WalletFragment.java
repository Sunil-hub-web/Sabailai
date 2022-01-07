package in.co.sabailai.customer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.co.sabailai.R;
import in.co.sabailai.customer.adapters.PaymentAdapter;
import in.co.sabailai.customer.models.PaymentGetSet;

public class WalletFragment extends Fragment {

    RecyclerView payment_recycler;
    private ArrayList<PaymentGetSet> paymentsarray = new ArrayList<PaymentGetSet>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_wallet, container, false);

        payment_recycler = v.findViewById(R.id.payment_recycler);

        SetPayments();
        return v;
    }

    public void SetPayments(){
        paymentsarray = new ArrayList<PaymentGetSet>();
        paymentsarray.add(new PaymentGetSet("Jay Prakash Dubey", "Plumber", "SVM 98755 5555 222", "498.00", "12 July 2021"));
        paymentsarray.add(new PaymentGetSet("Jay Prakash Dubey", "Plumber", "SVM 98755 5555 222", "498.00", "12 July 2021"));
        paymentsarray.add(new PaymentGetSet("Jay Prakash Dubey", "Plumber", "SVM 98755 5555 222", "498.00", "12 July 2021"));


        PaymentAdapter catAdapter = new PaymentAdapter(paymentsarray, getActivity());
        payment_recycler.setHasFixedSize(true);
        payment_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        payment_recycler.setAdapter(catAdapter);

    }
}
