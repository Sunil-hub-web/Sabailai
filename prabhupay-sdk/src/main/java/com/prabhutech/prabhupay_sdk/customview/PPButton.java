package com.prabhutech.prabhupay_sdk.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.prabhutech.prabhupay_sdk.R;

/**
 * Created by Niken on 8/2/2020.
 */
public class PPButton extends FrameLayout implements View.OnClickListener {
    Context context;
    Button button;
    ProgressBar progressBar;
    String text = "Button";
    int btnType;
    OnClickListener onClickListener;

    public PPButton(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PPButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_buttom_layout, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PPButton, 0, 0);
        text = a.getString(R.styleable.PPButton_text);
        btnType = a.getInt(R.styleable.PPButton_type, 0);
        a.recycle();
        button = view.findViewById(R.id.btn_custom);
        progressBar = view.findViewById(R.id.progress_custom);
        setButton();
        setText(text);
        button.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setButton() {
        if(btnType == 0){
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pp_primary)));
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

        }
    }

    public void setText(String text) {
        button.setText(text);
    }

    public PPButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBusy(Boolean isBusy, String title){
        button.setText(title);
        if(isBusy){
            button.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            button.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }

    }

    public String getText(){
        return button.getText().toString();
    }


    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null) onClickListener.onClick(this);
    }
}
