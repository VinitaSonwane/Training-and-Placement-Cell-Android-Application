package com.hire.freshershub.ui.groupChat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.hire.freshershub.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String userName;
    NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        userName = SharedPreferenceForLogin.getUserName(getApplicationContext());
        notificationManager = getSystemService(NotificationManager.class);

        if(!Objects.equals(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), userName)){

            String notificationType = remoteMessage.getData().get("type");

            if(Objects.equals(notificationType, "group_chat")){
                // Create a notification channel
                String channelId = "my_group_chat_channel";
                CharSequence channelName = "My Group Chat";
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_DEFAULT;
                }
                NotificationChannel channel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = new NotificationChannel(channelId, channelName, importance);
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
//                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel.enableVibration(true);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(channel);
                }

                int resourceImage = getResources().getIdentifier(Objects.requireNonNull(remoteMessage.getNotification()).getIcon(), "drawable", getPackageName());

                Intent resultIntent = new Intent(this, MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE);

// Build a notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(resourceImage)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

// Set group properties
                String groupId = "my_group_chat";
                builder.setGroup(groupId);
                builder.setGroupSummary(true);

// Show the notification
                int notificationId = 1;
                notificationManager.notify(notificationId, builder.build());
            }
            else if(Objects.equals(notificationType, "new_job")){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    if(!user.getUid().equals("WNsph1UTslTg27vJS40WHZ0Btbn2")){
                        // Create a notification channel
                        String channelId = "new_job_channel";
                        CharSequence channelName = "New Job";
                        int importance = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            importance = NotificationManager.IMPORTANCE_HIGH;
                        }
                        NotificationChannel channel = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            channel = new NotificationChannel(channelId, channelName, importance);
                        }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
//                }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            channel.enableVibration(true);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(channel);
                        }

                        int resourceImage = getResources().getIdentifier(Objects.requireNonNull(remoteMessage.getNotification()).getIcon(), "drawable", getPackageName());

                        Intent resultIntent = new Intent(this, MainActivity.class);

                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE);

// Build a notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(resourceImage)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                                .setContentIntent(pendingIntent)
                                .setPriority(NotificationCompat.PRIORITY_MAX);

// Set group properties
                        String groupId = "new_job";
                        builder.setGroup(groupId);
                        builder.setGroupSummary(true);

// Show the notification
                        int notificationId = 1;
                        notificationManager.notify(notificationId, builder.build());
                    }
                }
            }

        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

}


