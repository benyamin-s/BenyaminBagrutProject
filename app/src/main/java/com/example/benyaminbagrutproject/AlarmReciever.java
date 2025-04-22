package com.example.benyaminbagrutproject;

import static android.content.Context.ALARM_SERVICE;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;

public class AlarmReciever extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(context);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Meet meet = firebaseHelper.getUser().getMeetsList().get(intent.getIntExtra("Index",-1));

                // Create a notification
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // For Android O and above, create a notification channel
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarms", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                // Create the notification
                Notification notification = new Notification.Builder(context)
                        .setContentText("you have the meet \" "  + meet.getName() + " \" in " + firebaseHelper.getUser().TimeBeforeMeetNotif  + " minutes")
                        .setSmallIcon(android.R.drawable.ic_notification_overlay)
                        .build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify( /*TODO redo id method*/, notification);

                return true;
            }
        });

        firebaseHelper.retrieveUserData(handler);


    }


    public static void ScheduleMeetAlarm(Context context, int index, Long date)
    {
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(context);

        Intent intent = new Intent(context, AlarmReciever.class);
        intent.putExtra("Index", index);
        Meet m = firebaseHelper.getUser().getMeetsList().get(index);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, /*TODO redo id method*/, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmManager != null ) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date, pendingIntent);
        }
    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  /*TODO redo id method*/, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
