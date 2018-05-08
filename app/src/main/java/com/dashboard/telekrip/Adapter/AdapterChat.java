package com.dashboard.telekrip.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dashboard.telekrip.Activity.ChatActivity;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import se.simbio.encryption.Encryption;
import third.part.android.util.Base64;


public class AdapterChat extends BaseAdapter {

    private Context mContext;
    private List<Message> messages;
    private Encryption encryption=null;

    //Constructor

    public AdapterChat(Context mContext, List<Message> messages) {
        this.mContext = mContext;
        this.messages = messages;
    }

    void addListItemToAdapter(List<Message> list) {
        //Add list to current array list of data
        messages.addAll(list);
        //Notify UI
        //this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(messages.get(position).getSender().equals((String) Tools.getSharedPrefences(mContext,"phoneNumber",String.class))){
            v = View.inflate(mContext, R.layout.item_chat_right, null);
            ImageView _ivSaveNoSave =v.findViewById(R.id.ivSaveNoSave);
            if(!messages.get(position).isSave()){
                _ivSaveNoSave.setVisibility(View.VISIBLE);
            }
        }
        else{
            v = View.inflate(mContext, R.layout.item_chat_left, null);
        }
        TextView txt_msg =v.findViewById(R.id.txt_msg);
        TextView tarih =v.findViewById(R.id.txtTarih);
        try {
            encryption = Encryption.Builder.getDefaultBuilder("MyKey", "MySalt", new byte[16])
                    .setIterationCount(1) // use 1 instead the default of 65536
                    .build();
        } catch (NoSuchAlgorithmException e) {

        }
        try {
            encryption = new Encryption.Builder()
                    .setKeyLength(128)
                    .setKeyAlgorithm("AES")
                    .setCharsetName("UTF8")
                    .setIterationCount(100)
                    .setKey("mor€Z€cr€tKYss")
                    .setDigestAlgorithm("SHA1")
                    .setSalt("tuzluk")
                    .setBase64Mode(Base64.DEFAULT)
                    .setAlgorithm("AES/CBC/PKCS5Padding")
                    .setSecureRandomAlgorithm("SHA1PRNG")
                    .setSecretKeyType("PBKDF2WithHmacSHA1")
                    .setIv(new byte[] { 29, 88, -79, -101, -108, -38, -126, 90, 52, 101, -35, 114, 12, -48, -66, -30 })
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            txt_msg.setText(encryption.decrypt(messages.get(position).getText()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        tarih.setText(messages.get(position).getDateTime());
        //Save product id to tag
        return v;
    }
}