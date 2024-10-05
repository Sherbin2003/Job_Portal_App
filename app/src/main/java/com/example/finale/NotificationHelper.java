package com.example.finale;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHelper {
    private static final String NOTIFICATION_CHANNEL_ID = "location_notification_channel";

    public static void createNotificationChannel(Context context, String name) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Congratulations "+ name + " !!", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String message, String name) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Congratulations"+ name)
                    .setContentText(message)
                    .setSmallIcon(android .R.drawable.ic_dialog_info) // Use a default system icon
                    .build();
        }
        notificationManager.notify(0, notification);
    }
}
