package in.co.sabailai.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import in.co.sabailai.R;
import in.co.sabailai.customer.adapters.AreaSpinerAdapter;
import in.co.sabailai.customer.models.ShowAreaDetails;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.prabhutech.prabhupay_sdk.PrabhuSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingForm extends AppCompatActivity implements OnMapReadyCallback {

    SessionManager session;
    ViewDialog progressbar;
    ImageView backicon;
    Dialog offerdialog;
    TextView selectdate,yourlocation,latlong,text_yourlocation;
    DatePicker picker;
    Spinner timeslot_spinner, areaspinner;
    RadioGroup payment_radio;
    Button bookingbtn;
    String amount, invoiceNo, dateselected = "", timeslotselected = "", paymode = "", areaid = "", cupon_id,
            totalAMT, discount,str_latitude,str_longitude,areaname = "";
    EditText firstname, lastname, email, phone, contactpersonname, contactnumber, contactaddress, appsuit, citychoosen;
    ArrayList<String> slotsarray = new ArrayList<String>();
    ArrayList<ShowAreaDetails> areaarray  = new ArrayList<>();
    Double latitude,longitude;


    FusedLocationProviderClient fusedLocationProviderClient;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    LocationManager locationManager;
    GoogleMap mMap;

    String latlng,str_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        cupon_id = getIntent().getStringExtra("couponid");
        totalAMT = getIntent().getStringExtra("totalamt");
        discount = getIntent().getStringExtra("discountamt");

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        contactpersonname = findViewById(R.id.contactpersonname);
        contactnumber = findViewById(R.id.contactnumber);
        contactaddress = findViewById(R.id.contactaddress);
        appsuit = findViewById(R.id.appsuit);
        yourlocation = findViewById(R.id.yourlocation);

        citychoosen = findViewById(R.id.citychoosen);
        citychoosen.setText(session.getSelectedCityName());

        bookingbtn = findViewById(R.id.bookingbtn);
        payment_radio = findViewById(R.id.payment_radio);
        selectdate = findViewById(R.id.selectdate);
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        payment_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.online) {
                    paymode = "Online Payment";

                } else if(checkedId == R.id.cod) {
                    paymode = "Pay on Service Time";

                }
            }

        });

        timeslot_spinner = findViewById(R.id.timeslot_spinner);
        timeslot_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeslotselected = slotsarray.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        areaspinner = findViewById(R.id.areaspinner);
        areaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ShowAreaDetails mystate=(ShowAreaDetails) parent.getSelectedItem();

                areaid = mystate.getAreaId();

                //areaid = areaarray.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bookingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().length()==0){
                    email.setError("enter email");
                    email.requestFocus();

                }else if(phone.getText().length()==0){
                    phone.setError("enter phone");
                    phone.requestFocus();

                }else if(phone.getText().length()!=10){
                    phone.setError("must be 10 digit");
                    phone.requestFocus();

                }else if(contactpersonname.getText().length()==0){
                    contactpersonname.setError("enter contact person name");
                    contactpersonname.requestFocus();

                }else if(contactnumber.getText().length()==0){
                    contactnumber.setError("enter contact number");
                    contactnumber.requestFocus();

                }else if(contactnumber.getText().length()!=10){
                    contactnumber.setError("must be 10 digit");
                    contactnumber.requestFocus();

                }else if(contactaddress.getText().length()==0){
                    contactaddress.setError("enter sddress");
                    contactaddress.requestFocus();

                }else if(areaid.length()==0){
                    Toast.makeText(getApplicationContext(), "Select Area", Toast.LENGTH_SHORT).show();
                }else if(dateselected.length()==0){
                    Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT).show();
                }else if(timeslotselected.length()==0){
                    Toast.makeText(getApplicationContext(), "Select Timeslot", Toast.LENGTH_SHORT).show();
                }else if(paymode.length()==0){
                    Toast.makeText(getApplicationContext(), "Select Payment mode", Toast.LENGTH_SHORT).show();
                }else if(yourlocation.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Click your Location", Toast.LENGTH_SHORT).show();
                }else {

                    PlaceOrder();
                    Toast.makeText(BookingForm.this, "success", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        CallPaymentGateway();
        GetTimeSlots();

        yourlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(yourlocation.getText().toString().trim().equals("")){

                    showGppglemap();

                }else{

                    //Check gps is enable or not
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Write Function To enable gps
                        locationPermission();
                    } else {
                        //GPS is already On then
                        getLocation();
                    }

                    yourlocation.setText(str_address);

                    Toast.makeText(BookingForm.this, "Your Address Already set", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void showDateDialog() {

        offerdialog  = new Dialog(BookingForm.this);
        offerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        offerdialog.setCancelable(false);
        //...that's the viewquiz i told you will inflate later
        offerdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        offerdialog.setContentView(R.layout.selectdate_dialog);

        ImageView closedalog = offerdialog.findViewById(R.id.closedalog);
        closedalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerdialog.dismiss();
            }
        });

        picker=(DatePicker)offerdialog.findViewById(R.id.datePicker);

        TextView submit_btn = offerdialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerdialog.dismiss();
                dateselected = picker.getYear()+"-"+(picker.getMonth() + 1)+"-"+picker.getDayOfMonth();
                dateselected = dateselected.trim().toString();
                selectdate.setText(picker.getDayOfMonth()+"-"+ (picker.getMonth() + 1)+"-"+picker.getYear());

                ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), R.layout.spinneritem, slotsarray);
                ad.setDropDownViewResource(R.layout.spinnerdropdownitem);
                timeslot_spinner.setAdapter(ad);
            }
        });


        offerdialog.show();
    }

    private void GetTimeSlots() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.timeSLots_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                slotsarray = new ArrayList<String>();
                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Timeslot");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    String jsobjectitem = jsonArraycategories.getString(l);

                                    slotsarray.add(jsobjectitem);
                                }

                                progressbar.hideDialog();

                                GetArea();

                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                            progressbar.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.hideDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    Log.d("successresponceVolley", "" + jsonError);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                            }
                        }
                    }
                }) {

        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void GetArea() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.getArea_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressbar.hideDialog();

                        Log.d("fvsDevbf", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                areaarray = new ArrayList<ShowAreaDetails>();
                                JSONArray jsonArraycategories = jsonObject.getJSONArray("All_Area");
                                for (int l = 0; l < jsonArraycategories.length(); l++) {

                                    JSONObject jsonObject_area = jsonArraycategories.getJSONObject(l);


                                    String area_id = jsonObject_area.getString("area_id");
                                    String area_name = jsonObject_area.getString("area_name");

                                    ShowAreaDetails showAreaDetails = new ShowAreaDetails(
                                            area_name,area_id
                                    );

                                    areaarray.add(showAreaDetails);
                                }

                                AreaSpinerAdapter adapter = new AreaSpinerAdapter(BookingForm.this,
                                        R.layout.spinneritem,areaarray);
                                adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
                                areaspinner.setAdapter(adapter);
                                progressbar.hideDialog();

                                Log.d("areaarray",areaarray.toString());

                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                            progressbar.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.hideDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    Log.d("successresponceVolley", "" + jsonError);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                            }
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("city_id", session.getSelectedCityId());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void PlaceOrder() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.placeOrder_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
//                            subcatarray = new ArrayList<SubCatGetSet>();
//                            faqarray = new ArrayList<FAQGetSet>();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                progressbar.hideDialog();

                                if(paymode.equalsIgnoreCase("Online Payment")){

                                     amount = jsonObject.getString("amount");
                                     invoiceNo = jsonObject.getString("invoiceNo");

                                     CallPaymentGateway();
                                }else {

                                    Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(), CustomerDashBoard.class);
                                    i.putExtra("intenti", "");
                                    startActivity(i);
                                    finish();
                                }

//

                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                            progressbar.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.hideDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                try {
                                    String jError = new String(networkResponse.data);
                                    JSONObject jsonError = new JSONObject(jError);
                                    Log.d("successresponceVolley", "" + jsonError);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("successresponceVolley", "" + e);
                                }

                            }
                        }
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getUserID());
                params.put("city_id", session.getSelectedCityId());
                params.put("fname", firstname.getText().toString());
                params.put("lname", lastname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("contact_person", contactpersonname.getText().toString());
                params.put("contact_no", contactnumber.getText().toString());
                params.put("address", contactaddress.getText().toString());
                params.put("apt_suite", appsuit.getText().toString());
                params.put("area", areaid);
                params.put("service_date", dateselected);
                params.put("service_time", timeslotselected);
                params.put("pay_method", paymode);
                params.put("cupon_id", cupon_id);
                params.put("totalAMT", totalAMT);
                params.put("discount", discount);
                params.put("longitute",str_longitude );
                params.put("latitute", str_latitude);
                Log.d("fvsDevbf", "" + params);
                return params;

            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void CallPaymentGateway(){

        new PrabhuSdk(BookingForm.this, PrabhuSdk.ENV_STAGE, "sabailai001", "1oapb7cj", invoiceNo, amount, "Payment Against Order", new PrabhuSdk.PrabhuCallBack() {
            @Override
            public void onSuccess(HashMap<String, String> response) {
                Log.d("wrytjyf", response.toString());
            }

            @Override
            public void onError(HashMap<String, String> error) {
                Log.d("wrytjyf", error.toString());
            }
        });

    }

    /*public void serachlocation(){


    }*/

    public void showGppglemap(){

        Dialog dialog = new Dialog(BookingForm.this);
        dialog.setContentView(R.layout.serachlocation);
        dialog.setCancelable(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.geofence_map);
        mapFragment.getMapAsync(this);

        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps
            locationPermission();
        } else {
            //GPS is already On then
            getLocation();
        }

        latlong = dialog.findViewById(R.id.latlong);
        text_yourlocation = dialog.findViewById(R.id.text_yourlocation);
        Button btn_Save = dialog.findViewById(R.id.btn_Save);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                yourlocation.setText(str_address);

               /* if(btn_Save.getText().toString().trim().equals("Save Address")){

                    dialog.dismiss();

                    yourlocation.setText(str_address);

                }else{

                    btn_Save.setText("Save Address");

                    //Check gps is enable or not
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Write Function To enable gps
                        locationPermission();
                    } else {
                        //GPS is already On then
                        getLocation();
                    }
                }*/

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        enableUserLocation();
        getLocation();
        getTopLocation(googleMap);

    }


    private void getTopLocation(GoogleMap googleMap) {

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                //Check gps is enable or not
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps
                    locationPermission();
                } else {
                    //GPS is already On then
                    LatLng topLocation = googleMap.getCameraPosition().target;

                    Geocoder geocoder = new Geocoder(BookingForm.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(topLocation.latitude, topLocation.longitude, 1);
                        if (addresses.size() > 0) {

                            Address address = addresses.get(0);
                          /*  String streetAddress = address.getCountryName();
                            String streetAddress1 = address.getLocality();
                            String streetAddress2 = address.getPostalCode();*/
                            str_address = address.getAddressLine(0);
                            latlng = address.getLatitude() +" , "+ address.getLongitude();

                         /*   textView3.setText(streetAddress);
                            textView4.setText(streetAddress1);
                            textView5.setText(streetAddress2);*/
                            text_yourlocation.setText(str_address);
                            latlong.setText(latlng);


                            //set address On Text View
                            text_yourlocation.setText(Html.fromHtml(str_address));

                        /*mMap.addMarker(new MarkerOptions()
                                .position(topLocation)
                                .title(streetAddress)
                                .draggable(true)
                        );*/

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                //initialize location
                Location location = task.getResult();

                if (location != null) {

                    try {
                        //initialize geocoder
                        Geocoder geocoder = new Geocoder(BookingForm.this, Locale.getDefault());

                        //initialize AddressList
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        //set address On Text View
                        text_yourlocation.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
                        latlng = addresses.get(0).getLatitude() +" , "+ addresses.get(0).getLongitude();
                        latlong.setText(latlng);

                        latitude = addresses.get(0).getLatitude();
                        str_latitude = String.valueOf(latitude);

                        longitude = addresses.get(0).getLongitude();
                        str_longitude = String.valueOf(longitude);

                        str_address = addresses.get(0).getAddressLine(0);
                        yourlocation.setText(str_address);

                        LatLng sydney = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f));

                        mMap.setBuildingsEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                //We do not have the permission..
            }
        }
    }

    public void locationPermission() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable Your GPS Location").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                getLocation();
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
