package com.example.countingdays.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.countingdays.MainActivity;
import com.example.countingdays.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,AppConstant.SERVICE_REQUEST_CODE,intent1,0);

        Notification notification = new NotificationCompat.Builder(context,
                AppConstant.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is first Notifiation")
                .setContentText("Our believe is we can do it")
                .setSmallIcon(R.drawable.main_blue_icon)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(123, notification);
    }
}
