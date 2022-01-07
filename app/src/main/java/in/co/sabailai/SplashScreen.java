package in.co.sabailai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import in.co.sabailai.R;
import in.co.sabailai.customer.activities.CustomerDashBoard;
import in.co.sabailai.customer.activities.SelectLocationAfterLogin;
import in.co.sabailai.extras.PermissionUtils;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.technician.activities.TechnicianDashBoard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    PermissionUtils permissionUtils;
    ArrayList<String> permissions = new ArrayList<>();
    private static int SPLASH_TIME_OUT = 3000;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);

        permissionUtils = new PermissionUtils(this);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);

        permissionUtils.check_permission(permissions, "Need Camera and storage permission", 1);


    }

    public void PassIntent() {

        new android.os.Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (session.isLogin() && session.getUserType().equalsIgnoreCase("user")) {
                    if(session.getSelectedCityId().equalsIgnoreCase("")){
                        Intent i = new Intent(getApplicationContext(), SelectLocationAfterLogin.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getApplicationContext(), CustomerDashBoard.class);
                        i.putExtra("intenti", "");
                        startActivity(i);
                        finish();
                    }

                } else if (session.isLogin() && session.getUserType().equalsIgnoreCase("technician")) {
                    Intent i = new Intent(getApplicationContext(), TechnicianDashBoard.class);
                    i.putExtra("intenti", "");
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                    finish();

                }


            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void PermissionGranted(int request_code) {
        PassIntent();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
        finish();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
