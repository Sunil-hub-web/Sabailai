package com.prabhutech.prabhupay_sdk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupay_sdk.R;
import com.prabhutech.prabhupay_sdk.activity.EpaymentLoginActivity;
import com.prabhutech.prabhupay_sdk.customview.PPButton;
import com.prabhutech.prabhupay_sdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Niken on 8/2/2020.
 */
public class FragFinalResult extends Fragment {
    ImageView successResponseImage, failedResponseImage;
    TextView title, message;
    PPButton btnGoBack;
    private static Boolean isSuccess = true;
    private static String header, body;
    private static JsonObject finalResponse;

    public FragFinalResult(Boolean result, String title, String message,JsonObject response) {
        isSuccess = result;
        header = title;
        body = message;
        finalResponse = response;
    }

    public static FragFinalResult newInstance(Boolean result, String title, String message, JsonObject response) {
        FragFinalResult fragment = new FragFinalResult(result, title, message, response);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if(isSuccess){
            getSuccessData();
            successResponseImage.setVisibility(View.VISIBLE);
            failedResponseImage.setVisibility(View.GONE);
        } else {
            getFailureData();
            successResponseImage.setVisibility(View.GONE);
            failedResponseImage.setVisibility(View.VISIBLE);
        }
        title.setText(header);
        message.setText(body);
        btnGoBack.setOnClickListener(v -> {
            btnGoBack.setBusy(true, "");
            if(isSuccess){
                Objects.requireNonNull(getActivity()).finish();
                getSuccessData();
            } else {
                btnGoBack.setBusy(false, getResources().getString(R.string.go_back));
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
                getFailureData();
            }
        });
    }

    private void getFailureData() {
        EpaymentLoginActivity.isSuccess = false;
        HashMap<String, String> response = new HashMap<>();
        response.put("message", JsonUtils.safeString(finalResponse.get("message"), ""));
        response.put("success", finalResponse.get("success").getAsString());
        EpaymentLoginActivity.response = response;
    }

    private void getSuccessData() {
        EpaymentLoginActivity.isSuccess = true;
        HashMap<String, String> response = new HashMap<>();
        response.put("success", finalResponse.get("success").getAsString());
        response.put("status", JsonUtils.safeString(finalResponse.get("status"), ""));
        response.put("message", JsonUtils.safeString(finalResponse.get("message"), ""));
        response.put("transactionId", JsonUtils.safeString(finalResponse.get("data").getAsJsonObject().get("transactionId"), ""));
        EpaymentLoginActivity.response = response;
    }


    private void initView(View view) {
        successResponseImage = view.findViewById(R.id.success_response_image);
        failedResponseImage = view.findViewById(R.id.failed_response_image);
        title = view.findViewById(R.id.responseTitle);
        message = view.findViewById(R.id.responseMessage);
        btnGoBack = view.findViewById(R.id.bt_go_back);
    }
}
