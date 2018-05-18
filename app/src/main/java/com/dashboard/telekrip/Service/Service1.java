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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Activity.MainActivity;
import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.Adapter.AdapterOldUserMessage;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.Message;
import com.dashboard.telekrip.model.OldMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("deprecation")
public class Service1 extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        connectWebSocket(getApplicationContext());
        return flags;
    }

    private void connectWebSocket(final Context ctx) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://yazlab.xyz:8000/chat/kullaniciBasliklari/?telefonNumarasi=" + Tools.getSharedPrefences(this, "phoneNumber", String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ja = new JSONArray(response);
                            for (int i = 0; i < ja.length(); i++) {
                                URI uri;
                                try {
                                    uri = new URI("http://yazlab.xyz:8000/chat/message/" + ja.getJSONObject(i).getString("key"));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                    return;
                                }

                                new WebSocketClient(uri) {
                                    @Override
                                    public void onOpen(ServerHandshake serverHandshake) {
                                        System.out.println("open");
                                    }

                                    @Override
                                    public void onMessage(final String s) {
                                        Random r = new Random();
                                        Message message = new Gson().fromJson(s, Message.class);
                                        boolean sound=(boolean) Tools.getSharedPrefences(ctx, "sound", Boolean.class);
                                        boolean vibrate=(boolean) Tools.getSharedPrefences(ctx, "vibration", Boolean.class);
                                        if ((boolean) Tools.getSharedPrefences(ctx, "notification", Boolean.class)) {
                                            if (!Tools.getSharedPrefences(ctx, "position", String.class).toString().equals("chating")) {
                                                showNotification(sound, vibrate, message.getSenderNameSurname(), Tools.getDecrypt(message.getText()), r.nextInt(989456464));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onClose(int i, String s, boolean b) {
                                        System.out.println("closed");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        System.out.println("error");
                                    }
                                }.connect();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        Volley.newRequestQueue(this).add(postRequest);


    }

    void showNotification(boolean sound, boolean vibrate, String title, String message, int id) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = null;
        if (sound && vibrate) {
            noti = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pIntent)
                    .setSound(alarmSound)
                    .build();

        } else if(!sound && vibrate) {
            noti = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pIntent)
                    .build();
        }
        else if(sound && !vibrate){
            noti = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(pIntent)
                    .build();
        }
        else if(!sound && !vibrate){
            noti = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setContentIntent(pIntent)
                    .build();
        }
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