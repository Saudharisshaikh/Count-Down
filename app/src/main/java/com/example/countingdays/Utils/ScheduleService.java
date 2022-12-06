package com.example.countingdays.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.countingdays.MainActivity;
import com.example.countingdays.R;

public class ScheduleService  extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        Log.d("--onStart", "onStartCommand: ");
        String notiText = intent.getStringExtra("notiText");
        Intent intentOne = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                AppConstant.SERVICE_REQUEST_CODE,
                intentOne,0);

        Notification notification = new NotificationCompat.Builder(this,
                AppConstant.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is first Notifiation")
                .setContentText(notiText)
                .setSmallIcon(R.drawable.main_blue_icon)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .build();

        Log.d("--setNotification", "onStartCommand: called...");

        startForeground(1,notification);

        return START_STICKY;
    }

    private void createNotificationChannel() {


        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(
                    AppConstant.NOTIFICATION_CHANNEL_ID,
                    AppConstant.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
