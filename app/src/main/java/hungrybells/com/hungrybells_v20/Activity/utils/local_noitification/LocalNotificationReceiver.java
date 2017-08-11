package hungrybells.com.hungrybells_v20.Activity.utils.local_noitification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import hungrybells.com.hungrybells_v20.Activity.activity.AppLaunchActivity;
import hungrybells.com.hungrybells_v20.R;

/**
 * Created by ajeetkumar on 08/07/15.
 */
public class LocalNotificationReceiver extends BroadcastReceiver {

    int MID=0;
    NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        // Notification manager - we already know how to use it
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.ic_launcher, "Hungry Bells", System.currentTimeMillis());

        // This is intent, we want to launch when user clicks on the notification.
        Intent intentTL = new Intent(context, AppLaunchActivity.class);

        notification.setLatestEventInfo(context, "Aren't you feeling hungry?", "Get 20% off on your order. Coupon - FIRSTORDER",
                PendingIntent.getActivity(context, 0, intentTL,
                        PendingIntent.FLAG_CANCEL_CURRENT));

        notification.flags =Notification.FLAG_ONLY_ALERT_ONCE;
        notification.sound = alarmSound;
        nm.notify(1, notification);

        /*
        // Here we set next notification, in day interval
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
        */

    }

}