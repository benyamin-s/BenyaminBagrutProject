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
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * BroadcastReceiver for handling meeting notifications and alarms in the youth movement guide application.
 * This class manages scheduling and displaying notifications for upcoming meetings, ensuring guides
 * are reminded of their scheduled activities.
 * 
 * @author Benyamin
 * @version 1.0
 */
public class AlarmReciever extends BroadcastReceiver {

    /** Notification channel ID for Android O and above */
    private static final String CHANNEL_ID = "alarm_channel";
    
    /** Display name for the notification channel */
    private static final String channelName = "MyChanel_aaa";
    
    /** Description for the notification channel */
    private static final String channelDescription = "MyChannelDescription";

    /**
     * Called when a scheduled alarm is triggered.
     * This method creates and displays a notification for an upcoming meeting.
     * 
     * @param context The Context in which the receiver is running
     * @param intent The Intent being received, containing the meeting index
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(context);

        int Index =  intent.getIntExtra("Index",-1);
        Log.d("my_debugger", "onReceive: alarm" + Index);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                Notification notification;
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Index, intent, PendingIntent.FLAG_IMMUTABLE |PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,channelName, importance);

                    channel.setDescription(channelDescription);
                    notificationManager.createNotificationChannel(channel);
                    notification = new Notification.Builder(context, CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_notification_overlay)
                            .setContentTitle("title")
                            .setContentText("you have meet \"" + firebaseHelper.getUser().getMeetsList().get(Index).getName() + "\" in "+ firebaseHelper.getUser().getTimeBeforeMeetNotif()+" minutes" )
                            .setContentIntent(pendingIntent)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notificationManager.notify(Index,notification);

                } else
                {
                    notification = new Notification.Builder(
                            context)
                            .setSmallIcon(android.R.drawable.ic_notification_overlay)
                            .setContentTitle("title")
                            .setContentText("content")
                            .setPriority(Notification.PRIORITY_MIN)
                            .setContentIntent(pendingIntent)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notificationManager.notify(Index,notification);
                }


                return true;
            }
        });

        firebaseHelper.retrieveUserData(handler);

    }

    /**
     * Schedules a notification for an upcoming meeting.
     * The notification will be shown at the specified time before the meeting starts.
     * 
     * @param context The Context from which this method is called
     * @param index The index of the meeting in the user's meetings list
     * @param date The timestamp when the meeting is scheduled (in milliseconds since epoch)
     */
    public static void ScheduleMeetAlarm(Context context, int index, Long date) {
        Intent intent = new Intent(context, AlarmReciever.class);
        intent.putExtra("Index", index);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,index, intent, PendingIntent.FLAG_IMMUTABLE |PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        if (date > calendar.getTimeInMillis())
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,  date, pendingIntent);
        else
            Toast.makeText(context, "time set in past ", Toast.LENGTH_LONG).show();
    }

    /**
     * Cancels a previously scheduled meeting alarm.
     * This should be called when a meeting is deleted or rescheduled.
     * 
     * @param context The Context from which this method is called
     * @param index The index of the meeting whose alarm should be cancelled
     */
    public static void cancelAlarm(Context context, int index) {
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,index , intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
