package practice.com.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {



            final Intent restartMainActivityIntent = new Intent(context,
                    DailyActivity.class);
            restartMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                                // TODO: If not, create a PendingIntent using
                                // the
                                // restartMainActivityIntent and set its flags
                                // to FLAG_UPDATE_CURRENT


                                PendingIntent pendingIntent;
                                pendingIntent = PendingIntent.getActivity(context,0,restartMainActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);



                                CharSequence tickerText = "Time for selfie";
                                long[] mVibratePattern = { 0, 200, 200, 300 };


                                // TODO: Use the Notification.Builder class to
                                // create the Notification. You will have to set
                                // several pieces of information. You can use
                                // android.R.drawable.stat_sys_warning
                                // for the small icon. You should also
                                // setAutoCancel(true).

                                //  Notification.Builder notificationBuilder = null;

                                Notification.Builder notificationBuilder = new Notification.Builder(
                                        context)
                                        .setTicker(tickerText)
                                        .setSmallIcon(android.R.drawable.ic_menu_camera)
                                        .setAutoCancel(true)
                                        .setContentTitle("Daily Selfie")
                                        .setContentText("Time for new selfie"
                                        )
                                        .setContentIntent(pendingIntent)
                                        .setVibrate(mVibratePattern);

                                // TODO: Send the notification
                                //   notificationBuilder.notify();




                                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0,
                                        notificationBuilder.build());



        }
    }
