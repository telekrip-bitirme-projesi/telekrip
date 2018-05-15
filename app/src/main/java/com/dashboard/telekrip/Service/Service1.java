package com.dashboard.telekrip.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.dashboard.telekrip.Activity.MainActivity;
import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.Message;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("deprecation")
public class Service1 extends Service {
    public WebSocketClient mWebSocketClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        connectWebSocket();
        return flags;
    }
    private void connectWebSocket() {
        System.out.println("xx");
        URI uri;
        try {
            uri = new URI("http://yazlab.xyz:8000/chat/message/540106955d5d42434abc49a5bf61cb0a9262e14de9b40ffa344c0d2cc5e49b78");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("open");
            }

            @Override
            public void onMessage(final String s) {
                Message message = new Gson().fromJson(s, Message.class);
                System.out.println(s);
                showNotification("Ahmet Bilgili","bugün kaçta geliceksin ?",1);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("closed");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("error");
            }
        };
        mWebSocketClient.connect();
    }
     void showNotification(String title,String message,int id){
         Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         Intent intent = new Intent(this, MainActivity.class);
         PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
         Notification noti =
                 new NotificationCompat.Builder(this)
                         .setSmallIcon(R.mipmap.icon)
                         .setContentTitle(title)
                         .setContentText(message)
                         .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                         .setContentIntent(pIntent)
                         .setSound(alarmSound)
                         .build();

         // Hide the notification after its selected
         noti.flags |= Notification.FLAG_AUTO_CANCEL;

         NotificationManager mNotificationManager =
                 (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         // mId allows you to update the notification later on.
         mNotificationManager.notify(id, noti);
     }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }
}