package in.co.sabailai.customer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prabhutech.prabhupay_sdk.PrabhuSdk;

import java.util.HashMap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import in.co.sabailai.R;

public class CustomerDashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash_board);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //locationPermission();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        CallPaymentGateway();

        if(getIntent().getStringExtra("intenti").equalsIgnoreCase("Order Assigned") || getIntent().getStringExtra("intenti").equalsIgnoreCase("Work Started")){
            navView.setSelectedItemId(R.id.navigation_dashboard);
        }
    }
    public void CallPaymentGateway(){

        new PrabhuSdk(CustomerDashBoard.this, PrabhuSdk.ENV_TEST, "sabailai001", "1oapb7cj", "INV9865232152", "100", "Payment Against Order", new PrabhuSdk.PrabhuCallBack() {
            @Override
            public void onSuccess(HashMap<String, String> response) {
                Log.i("success", response.toString());
            }

            @Override
            public void onError(HashMap<String, String> error) {
                Log.i("error", error.toString());
            }
        });

    }

    public void locationPermission() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable Your GPS Location").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                //getLocation();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
