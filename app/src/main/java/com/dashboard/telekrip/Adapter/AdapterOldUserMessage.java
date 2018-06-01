package com.dashboard.telekrip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashboard.telekrip.Activity.StartSpeechActivity;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.OldMessage;
import com.dashboard.telekrip.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class AdapterOldUserMessage extends BaseAdapter {
    TextView _tvNameSurname;
    EmojiconTextView _tvLastMessage;
    CircleImageView _ivAvatar;
    ImageView _ivQuiet;
    private Context ctx;
    private List<OldMessage> listUser;

    public AdapterOldUserMessage(Context ctx, List<OldMessage> listUser) {
        this.ctx = ctx;
        this.listUser = new ArrayList<>(listUser);
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;
        if (myView == null) {
            LayoutInflater ınflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = ınflater.inflate(R.layout.user_old_message_listview_item, null);
        }
        _tvNameSurname = myView.findViewById(R.id.tvNameSurname);
        _ivAvatar = myView.findViewById(R.id.ivAvatar);
        _ivQuiet = myView.findViewById(R.id.ivQuiet);
        _tvLastMessage = myView.findViewById(R.id.tvLastMessage);


        if(listUser.get(i).isQuiet()){
            _ivQuiet.setVisibility(View.VISIBLE);
        }
        if (!listUser.get(i).getSenderPhone().equals((String) Tools.getSharedPrefences(ctx, "phoneNumber", String.class))) {
            if(!listUser.get(i).getSenderName().equals("")){
                _tvNameSurname.setText(listUser.get(i).getSenderName());
            }
            else {
                _tvNameSurname.setText(listUser.get(i).getSenderPhone());
            }
        }
        else if (!listUser.get(i).getReceiverPhone().equals((String) Tools.getSharedPrefences(ctx, "phoneNumber", String.class))) {
            if(!listUser.get(i).getReceiverName().equals("")){
                _tvNameSurname.setText(listUser.get(i).getReceiverName());
            }
            else {
                _tvNameSurname.setText(listUser.get(i).getReceiverPhone());
            }
        }
        //--------------------
        if (listUser.get(i).getAvatar() != null && !listUser.get(i).getAvatar().equals("")) {
            Picasso.with(ctx).load(listUser.get(i).getAvatar()).fit().centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(_ivAvatar);
        } else {
            _ivAvatar.setImageResource(R.drawable.default_avatar);
        }
        if(listUser.get(i).getLast_message()!=null){
            if (Tools.getDecrypt(listUser.get(i).getLast_message()).length() > 30) {
                _tvLastMessage.setText(Tools.getDecrypt(listUser.get(i).getLast_message()).substring(0, 30) + "...");
            } else {
                _tvLastMessage.setText(Tools.getDecrypt(listUser.get(i).getLast_message()));
            }
        }



        return myView;
    }
}
