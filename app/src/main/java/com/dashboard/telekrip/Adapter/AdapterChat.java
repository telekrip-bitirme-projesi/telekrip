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

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import se.simbio.encryption.Encryption;
import third.part.android.util.Base64;


public class AdapterChat extends BaseAdapter {

    private Context mContext;
    private List<Message> messages;

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
        EmojiconTextView txt_msg =v.findViewById(R.id.txt_msg);
        TextView tarih =v.findViewById(R.id.txtTarih);
        txt_msg.setText(Tools.getDecrypt(messages.get(position).getText()));
        tarih.setText(messages.get(position).getDateTime());
        //Save product id to tag
        return v;
    }
}