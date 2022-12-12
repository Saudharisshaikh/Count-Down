package com.example.countingdays.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.countingdays.MainActivity;
import com.example.countingdays.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("--onReceive", "onReceive: called ");

        if(intent.getAction().equals("xyz.games.pacman.controller.BROADCAST")){
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,AppConstant.SERVICE_REQUEST_CODE,intent1,0);

        String extra = intent.getStringExtra("scheduleContent");

        Notification notification = new NotificationCompat.Builder(context,
                AppConstant.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Counting Days Notification")
                .setContentText(extra)
                .setColor(ContextCompat.getColor(context, R.color.general_color))
                .setSmallIcon(R.drawable.app_logo)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(123, notification);
    }
    }
}
