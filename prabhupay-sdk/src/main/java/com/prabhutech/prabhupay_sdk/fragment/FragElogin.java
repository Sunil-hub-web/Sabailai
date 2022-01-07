package com.prabhutech.prabhupay_sdk.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupay_sdk.R;
import com.prabhutech.prabhupay_sdk.activity.EpaymentLoginActivity;
import com.prabhutech.prabhupay_sdk.apicore.ApiRepo;
import com.prabhutech.prabhupay_sdk.customclass.TransactionDetailAdapter;
import com.prabhutech.prabhupay_sdk.customclass.TransactionPairData;
import com.prabhutech.prabhupay_sdk.customview.PPButton;
import com.prabhutech.prabhupay_sdk.quickui.QuickUI;
import com.prabhutech.prabhupay_sdk.quickui.SimpleCallback;
import com.prabhutech.prabhupay_sdk.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import io.reactivex.observers.DisposableObserver;

public class FragElogin extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<TransactionPairData> transactionDetailList;
    private TransactionDetailAdapter adapter;
    private TextInputLayout userNameLayout, passwordLayout, pinLayout;
    private EditText userNameEditText, passwordEditText, eCommercePinEditText;
    private PPButton btnLogin, btnCancel;
    private RelativeLayout busyLayout;
    private ScrollView detailLayout;
    private String processId, customerId;
    private ApiRepo repo;
    private LinearLayout passwordLinearLayout, ecommerceLinearLayout;
    private TextView userNameErrorView, passwordErrorView, pinErrorView, note;
    private static String eMerchantId, ePassword, eInvoiceNo, eTotalAmount, eRemarks;

    public FragElogin() {
    }

    public static FragElogin newInstance(String merchantId, String password, String inVoiceNo, String totalAmount, String remarks) {
        FragElogin fragment = new FragElogin();
        eMerchantId = merchantId;
        ePassword = password;
        eInvoiceNo = inVoiceNo;
        eTotalAmount = totalAmount;
        eRemarks = remarks;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        repo = new ApiRepo();
        busyLayout.setVisibility(View.VISIBLE);
        getList();
        addTextWatcher(userNameLayout, userNameEditText, userNameErrorView);
        addTextWatcher(passwordLayout, passwordEditText, passwordErrorView);
        addTextWatcher(pinLayout, eCommercePinEditText, pinErrorView);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnLogin.getText().toLowerCase().equals("login")){
                    if(isValid()){
                        login();
                    }
                } else {
                    if(pinValid()){
                        confirmTransaction();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish();
        });
    }

    private void showErrorText(EditText editText, TextView errorTextView, String errorMessage){
        editText.setBackground(getResources().getDrawable(R.drawable.bg_error_edittext));
        errorTextView.setText(errorMessage);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorText(EditText editText, TextView errorTextView){
        editText.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
        errorTextView.setVisibility(View.GONE);
    }



    private void confirmTransaction() {
        btnCancel.setEnabled(false);
        btnLogin.setBusy(true, "");
        repo.confirmPayment(getContext(), getConfirmReq()).safeSubscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(JsonObject value) {
                btnLogin.setBusy(false, getResources().getString(R.string.confirm));
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, FragFinalResult.newInstance(true, getResources().getString(R.string.success), JsonUtils.safeString(value.get("message"), ""), value), "FRAG_SDK").commit();
            }

            @Override
            public void onError(Throwable e) {
                JsonObject obj = new JsonObject();
                obj.addProperty("success", false);
                obj.addProperty("message", e.getMessage());
                btnLogin.setBusy(false, getResources().getString(R.string.confirm));
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().add(R.id.frame_layout, FragFinalResult.newInstance(false, getResources().getString(R.string.failed), e.getMessage(), obj), "FRAG_SDK").addToBackStack("FRAGELOGIN").commit();
            }

            @Override
            public void onComplete() {
                btnCancel.setEnabled(true);
            }
        });
    }

    private JsonObject getConfirmReq() {
        JsonObject confirmReq = new JsonObject();
        confirmReq.addProperty("processId", processId);
        confirmReq.addProperty("customerId", customerId);
        confirmReq.addProperty("trxnStatus", getResources().getString(R.string.confirm));
        confirmReq.addProperty("username", userNameEditText.getText().toString());
        confirmReq.addProperty("trxnPin", eCommercePinEditText.getText().toString());
        return confirmReq;
    }

    private boolean pinValid() {
        if(eCommercePinEditText.getText() != null && eCommercePinEditText.getText().toString().length() < 4){
            showErrorText(eCommercePinEditText, pinErrorView, getResources().getString(R.string.please_enter_valid_4_digit_pin));
        }
        return eCommercePinEditText.getText() != null && !eCommercePinEditText.getText().toString().isEmpty();
    }

    private void addTextWatcher(TextInputLayout layout, EditText editText, TextView errorView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideErrorText(editText, errorView);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void login() {
        btnCancel.setEnabled(false);
        btnLogin.setBusy(true, "");
        repo.login(getContext(), getLoginReq()).safeSubscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(JsonObject value) {
                customerId = JsonUtils.safeString(value.getAsJsonObject("data").get("customerId"), "");
                btnLogin.setBusy(false, getResources().getString(R.string.confirm));
                btnCancel.setVisibility(View.GONE);
                note.setText(getResources().getString(R.string.please_enter_ecommerce_mpin_to_continue));
                userNameEditText.setEnabled(false);
                passwordLinearLayout.setVisibility(View.GONE);
                ecommerceLinearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                btnLogin.setBusy(false, getResources().getString(R.string.login));
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                btnCancel.setEnabled(true);
            }
        });

    }

    private JsonObject getLoginReq() {
        JsonObject logReq = new JsonObject();
        logReq.addProperty("processId", processId);
        logReq.addProperty("trxnStatus", "CONFIRM");
        logReq.addProperty("username", userNameEditText.getText().toString());
        logReq.addProperty("password", passwordEditText.getText().toString());
        return logReq;
    }

    private boolean isValid() {
        if(userNameEditText.getText() == null || userNameEditText.getText().toString().isEmpty()){
            showErrorText(userNameEditText, userNameErrorView, getResources().getString(R.string.please_enter_mobile_number_or_email));
            return false;
        }
        if(passwordEditText.getText() == null || passwordEditText.getText().toString().isEmpty()){
            showErrorText(passwordEditText, passwordErrorView, getResources().getString(R.string.please_enter_password));
            return false;
        }
        return true;
    }


    private void getList() {
        repo.initiateTransaction(getContext(), getRequest()).safeSubscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(JsonObject value) {
                if(value.get("success").getAsBoolean()) {
                    processId = value.getAsJsonObject("data").get("processId").getAsString();
                    JsonObject req = new JsonObject();
                    req.addProperty("processId", processId);
                    repo.getTrxnDetail(getContext(), req).safeSubscribe(new DisposableObserver<JsonObject>() {
                        @Override
                        public void onNext(JsonObject value) {
                            if(value.get("success").getAsBoolean()){
                                notifyChangedDataSet(value.getAsJsonObject("data"));
                                busyLayout.setVisibility(View.GONE);
                                detailLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            QuickUI.showBasicAlertDialog(getActivity(), getResources().getString(R.string.sorry), e.getMessage(), false, new SimpleCallback() {
                                @Override
                                public void call() {
                                    Objects.requireNonNull(getActivity()).finish();
                                }
                            });
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
                QuickUI.showBasicAlertDialog(getActivity(), getResources().getString(R.string.sorry), e.getMessage(), false, new SimpleCallback() {
                    @Override
                    public void call() {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                });
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * shows details in recycler view
     * @param data
     */
    private void notifyChangedDataSet(JsonObject data) {
        transactionDetailList = new ArrayList<>();
        transactionDetailList.add(new TransactionPairData("Merchant Name", JsonUtils.safeString(data.get("merchantName"), "")));
        transactionDetailList.add(new TransactionPairData("Total Amount", JsonUtils.safeString(data.get("totalAmount"), "")));
        transactionDetailList.add(new TransactionPairData("Invoice Number", JsonUtils.safeString(data.get("invoiceNo"), "")));
        adapter = new TransactionDetailAdapter(getContext(), transactionDetailList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private JsonObject getRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalAmount", eTotalAmount);
        jsonObject.addProperty("merchantId", eMerchantId);
        jsonObject.addProperty("password", ePassword);
        jsonObject.addProperty("invoiceNo", eInvoiceNo);
        jsonObject.addProperty("remarks", eRemarks);
        jsonObject.addProperty("returnUrl", "https://client.prabhupay.com");
        return jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_elogin, container, false);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        btnLogin = view.findViewById(R.id.bt_login);
        busyLayout = view.findViewById(R.id.busy_layout);
        detailLayout = view.findViewById(R.id.details_layout);
        userNameLayout = view.findViewById(R.id.tl_username);
        passwordLayout = view.findViewById(R.id.tl_password);
        userNameEditText = view.findViewById(R.id.et_username);
        passwordEditText = view.findViewById(R.id.et_password);
        passwordLinearLayout = view.findViewById(R.id.password_linear_layout);
        ecommerceLinearLayout = view.findViewById(R.id.ecommerce_layout);
        eCommercePinEditText = view.findViewById(R.id.et_ecommerce_pin);
        userNameErrorView = view.findViewById(R.id.tv_username_error_msg);
        passwordErrorView = view.findViewById(R.id.tv_password_error_msg);
        pinErrorView = view.findViewById(R.id.tv_pin_error_msg);
        pinLayout = view.findViewById(R.id.tl_ecommerce_pin);
        btnCancel = view.findViewById(R.id.btn_cancel);
        note = view.findViewById(R.id.tv_note);
    }
}