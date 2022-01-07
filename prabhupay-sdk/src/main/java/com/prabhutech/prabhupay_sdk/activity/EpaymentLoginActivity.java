package com.prabhutech.prabhupay_sdk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupay_sdk.PrabhuSdk;
import com.prabhutech.prabhupay_sdk.R;
import com.prabhutech.prabhupay_sdk.fragment.FragElogin;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EpaymentLoginActivity extends AppCompatActivity {
    public static Boolean isSuccess;
    public static HashMap<String, String> response;
    static PrabhuSdk.PrabhuCallBack prabhuCallBack;
    String merchantId, password, inVoiceNo, totalAmount, remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epayment_login);
        isSuccess = false;
        response = new HashMap<>();
        merchantId = getIntent().getStringExtra("merchantId");
        password = getIntent().getStringExtra("password");
        inVoiceNo = getIntent().getStringExtra("inVoiceNo");
        totalAmount = getIntent().getStringExtra("totalAmount");
        remarks = getIntent().getStringExtra("remarks");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, FragElogin.newInstance(merchantId, password, inVoiceNo, totalAmount, remarks), "FRAG_SDK").commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isSuccess){
            prabhuCallBack.onSuccess(response);
        } else {
            if(response.size() == 0){
                response = new HashMap<>();
                response.put("success", "false");
            }
            prabhuCallBack.onError(response);
        }
    }


    public static void  setPrabhuCallBack(PrabhuSdk.PrabhuCallBack callBack){
        prabhuCallBack = callBack;
    }

}