package in.co.sabailai.fcm;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import androidx.core.app.NotificationCompat;
import in.co.sabailai.R;
import in.co.sabailai.customer.activities.CustomerDashBoard;
import in.co.sabailai.extras.SessionManager;
import in.co.sabailai.technician.activities.TechnicianDashBoard;


public class NotificationHelper {


    private Context mContext;
    public static NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    Intent resultIntent;
    SessionManager session;
    public static final String NOTIFICATION_CHANNEL_ID = "in.co.sabailai";


    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createNotification(String title, String message, String user, String activity,String amount)
    {

        try {
            session = new SessionManager(mContext);

            Log.d("hrseg",amount);

            if (session.isLogin()) {

                /**Creates an explicit intent for an Activity in your app**/
                if (session.getUserType().equalsIgnoreCase("technician")) {
                    resultIntent = new Intent(mContext, TechnicianDashBoard.class);
                    resultIntent.putExtra("intenti", activity);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    resultIntent = new Intent(mContext, CustomerDashBoard.class);
                    resultIntent.putExtra("intenti", activity);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
                mBuilder.setSmallIcon(R.drawable.ic_stat_name);
                mBuilder.setContentTitle(title)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(message + "," +"\n" + "Total Amount"+" : "+ amount)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(resultPendingIntent);

                mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.BLUE);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                    assert mNotificationManager != null;
                    mNotificationManager.notify(965824521 /* Request Code */, mBuilder.build());

                }
            }
        }catch (Exception e){
            Log.d("nddq", ""+e);
        }

    }


}
