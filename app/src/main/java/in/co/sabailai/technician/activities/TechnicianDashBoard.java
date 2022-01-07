package in.co.sabailai.technician.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import in.co.sabailai.R;
import in.co.sabailai.extras.SessionManager;

public class TechnicianDashBoard extends AppCompatActivity {

    TextView techniciallocation;
    SessionManager session;
    RelativeLayout notification_layout;
    public static TextView notificationcounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_dash_board);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);

        notificationcounter = findViewById(R.id.notificationcounter);
        notification_layout = findViewById(R.id.notification_layout);
        techniciallocation = findViewById(R.id.techniciallocation);
        techniciallocation.setText(session.getCity()+","+session.getState());

        //locationPermission();

        notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VendorNotification.class);
                startActivity(i);
            }
        });
//
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_technician_ongoing, R.id.navigation_technician_upcoming, R.id.navigation_technician_wallet, R.id.navigation_technician_history, R.id.navigation_technician_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(getIntent().getStringExtra("intenti").equalsIgnoreCase("Booking Assign")){
            navView.setSelectedItemId(R.id.navigation_technician_upcoming);
        }else if(getIntent().getStringExtra("intenti").equalsIgnoreCase("Work Completed")){
            navView.setSelectedItemId(R.id.navigation_technician_history);
        }

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