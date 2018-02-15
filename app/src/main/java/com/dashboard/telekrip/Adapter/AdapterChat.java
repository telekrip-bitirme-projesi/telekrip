package com.dashboard.telekrip.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.*;



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
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy|HH:mm");
        View v;
        if(messages.get(position).getSender()==1){
            v = View.inflate(mContext, R.layout.item_chat_right, null);
        }
        else{
            v = View.inflate(mContext, R.layout.item_chat_left, null);
        }
        TextView txt_msg =v.findViewById(R.id.txt_msg);
        TextView tarih =v.findViewById(R.id.txtTarih);
        txt_msg.setText(messages.get(position).getMessage());
        tarih.setText(messages.get(position).getDatetime());
        //Save product id to tag
        return v;
    }
}