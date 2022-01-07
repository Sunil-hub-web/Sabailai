package in.co.sabailai.customer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import in.co.sabailai.R;
import in.co.sabailai.customer.activities.CustomerOTP;
import in.co.sabailai.customer.activities.SelectLocationAfterLogin;
import in.co.sabailai.extras.ServerLinks;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.extras.ViewDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    SessionManager session;
    ViewDialog progressbar;
    EditText fullname_ed, email_ed, phonenumber_ed;
    ImageView edit_ic, locicon, profilepic;
    Button updateprofilebtn;
    private String userChoosenTask;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    private static String encodedrediept = "";
    String extension, uriString;
    Uri uri;
    byte[] bytes;
    String baseimageDoc;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        locicon = v.findViewById(R.id.locicon);
        locicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout!!")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                session.logoutUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        profilepic = v.findViewById(R.id.profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittPhoto();
            }
        });
        updateprofilebtn = v.findViewById(R.id.updateprofilebtn);
        fullname_ed = v.findViewById(R.id.fullname_ed);
        email_ed = v.findViewById(R.id.email_ed);
        phonenumber_ed = v.findViewById(R.id.phonenumber_ed);
        edit_ic = v.findViewById(R.id.edit_ic);
        edit_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname_ed.setEnabled(true);
                email_ed.setEnabled(true);
                phonenumber_ed.setEnabled(true);
                phonenumber_ed.setText(session.getPhone());
                fullname_ed.requestFocus();
                updateprofilebtn.setVisibility(View.VISIBLE);
                edit_ic.setVisibility(View.GONE);
            }
        });

        updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullname_ed.getText().length()==0){
                    fullname_ed.setError("enter name");
                    fullname_ed.requestFocus();

                }else if(email_ed.getText().length()==0){
                    email_ed.setError("enter email");
                    email_ed.requestFocus();

                }else if(phonenumber_ed.getText().length()==0){
                    phonenumber_ed.setError("enter phone number");
                    phonenumber_ed.requestFocus();

                }else if(phonenumber_ed.getText().length()!=10){
                    phonenumber_ed.setError("must be 10 digit");
                    phonenumber_ed.requestFocus();

                }else{

                    updateProfileDetails();
                }
            }
        });

        getProfileDetails();
        return v;
    }


    private void getProfileDetails() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.GetProfileDetails_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String img = jsonObject.getString("img");
                                String phone = jsonObject.getString("contact_no");
//
                                session.setUserID(id);
                                session.setUserName(name);
                                session.setEmail(email);
                                session.setPhone(phone);
                                session.setImgUrl(img);
                                session.setUserType("user");

                                Glide.with(getActivity()).load(img).placeholder(R.drawable.dummy_profilepic).into(profilepic);

                                fullname_ed.setText(name);
                                email_ed.setText(email);
                                phonenumber_ed.setText("+91 "+phone);

                                fullname_ed.setEnabled(false);
                                email_ed.setEnabled(false);
                                phonenumber_ed.setEnabled(false);

                                updateprofilebtn.setVisibility(View.GONE);
                                edit_ic.setVisibility(View.VISIBLE);

//
                                session.setLogin();
//
                                progressbar.hideDialog();
//


                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                params.put("id", session.getUserID());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void updateProfileDetails() {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.updateProfile_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//
                                progressbar.hideDialog();
//
                                getProfileDetails();

                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                params.put("id", session.getUserID());
                params.put("name", fullname_ed.getText().toString());
                params.put("email", email_ed.getText().toString());
                params.put("contact_no", phonenumber_ed.getText().toString());
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void updateProfilePhoto(final String camgal) {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.uploadProfilePhoto_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fvsDevbf", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {

                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                progressbar.hideDialog();
                                if(camgal.equalsIgnoreCase("cam")){
                                    profilepic.setImageBitmap(bitmap);
                                }else{
                                    Glide.with(getActivity()).load(
                                            uri).into(profilepic);
                                }



                            } else {
                                progressbar.hideDialog();
                                Toast.makeText(getActivity(), "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

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
                params.put("img", baseimageDoc);
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void edittPhoto(){
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {


        final String[] ACCEPT_MIME_TYPES = {
                "image/jpg",
                "image/jpeg",
                "image/png"
        };
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
        }



    }

    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "in.co.sabailai.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {

            try {
                if (resultCode == RESULT_OK) {

//                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());


                    baseimageDoc = encodeTobase64(bitmap);
                    Log.d("fehsrdtteg", "onActivityResult: Base64string=" + baseimageDoc);

                    updateProfilePhoto("cam");

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("pjendm_gal", String.valueOf(e));
            }

        } else if (requestCode == PICK_IMAGE_GALLERY) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                uriString = uri.toString();
                Log.d("fehsrdtteg", "onActivityResult: uri" + uriString);

                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(uri);

                    bytes = getBytes(in);
                    Log.d("fehsrdtteg", "onActivityResult: bytes size=" + bytes.length);
//                    String

                    long fileSizeInBytes = bytes.length;
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    Log.d("fehsrdtteg", "onActivityResult: bytes size=" + fileSizeInMB);
                    Log.d("oevdvesvdsd", "" + uriString.substring(uriString.length() - 4));




                    String path = new File(data.getData().getPath()).getAbsolutePath();

                    if(path != null) {
                        uri = data.getData();

                        String filename;
                        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

                        if (cursor == null) filename = uri.getPath();
                        else {
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                            filename = cursor.getString(idx);
                            cursor.close();
                        }

                        String name = filename.substring(filename.lastIndexOf("."));
                        extension = filename.substring(filename.lastIndexOf(".") + 1);
                        Log.d("oevdvesvdsd", "" + name);
                        Log.d("oevdvesvdsd", "" + extension);


                        if (extension.equalsIgnoreCase("jpeg")) {
                            if (fileSizeInMB < 24) {
                                baseimageDoc = Base64.encodeToString(bytes, Base64.DEFAULT);
                                Log.d("fehsrdtteg", "onActivityResult: Base64string=" + baseimageDoc);
                                updateProfilePhoto("gal");

                            } else {
                                Toast.makeText(getActivity(), "File too large", Toast.LENGTH_SHORT).show();
                            }
                        } else if (extension.equalsIgnoreCase("jpg")) {
                            if (fileSizeInMB < 24) {
                                baseimageDoc = Base64.encodeToString(bytes, Base64.DEFAULT);
                                Log.d("fehsrdtteg", "onActivityResult: Base64string=" + baseimageDoc);
                                updateProfilePhoto("gal");

                            } else {
                                Toast.makeText(getActivity(), "File too large", Toast.LENGTH_SHORT).show();
                            }
                        } else if (extension.equalsIgnoreCase("png")) {
                            if (fileSizeInMB < 24) {
                                baseimageDoc = Base64.encodeToString(bytes, Base64.DEFAULT);
                                Log.d("fehsrdtteg", "onActivityResult: Base64string=" + baseimageDoc);
                                updateProfilePhoto("gal");

                            } else {
                                Toast.makeText(getActivity(), "File too large", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Format Not Supported", Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    Log.d("fehsrdtteg_error", "onActivityResult: " + e.toString());
                }
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] b = baos.toByteArray();
        encodedrediept = Base64.encodeToString(b, Base64.DEFAULT);

//        Log.e("LOOK", imageEncoded);
        String str = encodedrediept;
        int maxLogSize = 1000;
        for (int i = 0; i <= str.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > str.length() ? str.length() : end;
            Log.v("hjkfvbn", str.substring(start, end));
        }
        return encodedrediept;
    }

}
