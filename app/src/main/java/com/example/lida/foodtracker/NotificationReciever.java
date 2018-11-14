package com.example.lida.foodtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class NotificationReciever extends BroadcastReceiver {
    //private String channelId = "channel_id";
    //private NotificationChannel
    private NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =  PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        Resources resources = context.getResources();
        buildNotification(context, pendingIntent, resources);
    }

    private void buildNotification(Context context, PendingIntent pendingIntent, Resources res) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.carrot))
                .setSmallIcon(R.mipmap.carrot)
                .setContentTitle("Загляните в холодильник! :)")
                .setContentText("Кажется нужно съесть пару продуктов")
                .setTicker("Подчисти холодос")
                .setAutoCancel(true);
        notificationManager.notify(100, builder.build());

    }
}
