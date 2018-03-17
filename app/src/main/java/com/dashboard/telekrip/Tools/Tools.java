package com.dashboard.telekrip.Tools;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import com.dashboard.telekrip.Activity.RegisterActivity;
import com.dashboard.telekrip.model.Contact;
import com.dashboard.telekrip.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tools {

    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor = null;
    private static Date time = null;
    private static SimpleDateFormat df = null;
    private static AlertDialog.Builder alertDialogBuilder = null;
    private static ProgressDialog progressDialog;

    public static String getDate() {
        if (time == null) {
            time = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy");
            return df.format(time);
        } else {
            return df.format(time);
        }
    }

    public static void setSharedPrefences(Context ctx, String key, Object value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

    public static Object getSharedPrefences(Context ctx, String key, Class<?> type) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (type == String.class) {
            return preferences.getString(key, null);
        } else if (type == Boolean.class) {
            return preferences.getBoolean(key, false);
        } else if (type == Integer.class) {
            return preferences.getInt(key, -1);
        } else if (type == Float.class) {
            return preferences.getFloat(key, -1);
        }
        return null;
    }

    public static void removeSharedPrefences(Context ctx, String key) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public static void logoutUser(Context ctx) {
        removeSharedPrefences(ctx, "id");
        removeSharedPrefences(ctx, "username");
        removeSharedPrefences(ctx, "avatar");
    }

    public static AlertDialog.Builder alertDialog(Context ctx, String title, String message) {
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(
                    ctx);
        }
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false);

        return alertDialogBuilder;
    }

    public static List<Contact> getContactList(Context ctx) {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactList.add(new Contact(name,phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return contactList;
    }

    public static String getContactListString(Context ctx) {
        String contactListString="";
        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactListString+=phoneNo.replace("(","").replace(")","").replace("-","").replace(" ","")+",";
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        contactListString=contactListString.substring(0, contactListString.length() - 1);
        return contactListString;
    }

    public static ProgressDialog createProgressDialog(Context ctx,String message){
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return  progressDialog;

    }
}
