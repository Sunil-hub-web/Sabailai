package in.co.sabailai.fcm;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import in.co.sabailai.R;
import in.co.sabailai.extras.SessionManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title, body;
    NotificationHelper NotificationHelper;
    LocalBroadcastManager broadcaster;
    public static Handler handler = new Handler();
    SessionManager session;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        session = new SessionManager(getApplicationContext());

        NotificationHelper = new NotificationHelper(this);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FCMService", "From: " + remoteMessage.getFrom());


        try {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();

            Log.e("ghrtfhg", title);
            Log.e("ghrtfhg", body);
        } catch (Exception e) {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e("jodnm1", "Data Payload: " + remoteMessage.getData().toString());

            try {

                Log.d("PayFCMDAta", "Data Payload: " + remoteMessage.getData().get("title"));

                Log.d("PayAmount", "Data Payload: " + remoteMessage.getData().get("total_amount"));

                String tit = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                String event = remoteMessage.getData().get("event");
                String user_type = remoteMessage.getData().get("user_type");
                String total_amount = remoteMessage.getData().get("total_amount");

                NotificationHelper.createNotification(tit, message, user_type, event,total_amount);

            } catch (Exception e) {
                Log.e("jodnm2", "Exception: " + e.getMessage());
            }
        } else {

            NotificationHelper.createNotification(title, body, "customer", "","");

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d("FCMID", "Refreshed token: " + token);

    }


}
