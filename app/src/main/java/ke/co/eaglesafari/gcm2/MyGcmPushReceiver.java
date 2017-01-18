package ke.co.eaglesafari.gcm2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;


import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import ke.co.eaglesafari.Receipt;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.items.GcmItem;
import ke.co.eaglesafari.items.UserItem;


public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        Gson gson= new Gson();
        GcmItem gcmItem= new GcmItem();
        gcmItem.setContent(bundle.getString("content")).setType(bundle.getString("type")).setUser(gson.fromJson(bundle.getString("user"),UserItem.class));

        Intent resultIntent = new Intent(getApplicationContext(), Receipt.class);
        resultIntent.putExtra(Constant.KEY_ID,"1");
        resultIntent.putExtra(Constant.KEY_VALUE,bundle.getString("content"));
        showNotificationMessage(getApplicationContext(), bundle.getString("title"), bundle.getString("explanation"), "", resultIntent);
        NotificationUtils notificationUtils = new NotificationUtils();
        notificationUtils.playNotificationSound();


        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(600);

/*
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra(Constant.KEY_VALUE,gson.toJson(gcmItem));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound

        } else {



     *//*       if (TextUtils.isEmpty(image)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, image);
            }*//*
        }*/
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}