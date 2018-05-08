package com.dashboard.telekrip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class AdapterOldUserMessage extends BaseAdapter {
    TextView _tvNameSurname, _tvLastMessage;
    CircleImageView _ivAvatar;
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
        _tvLastMessage = myView.findViewById(R.id.tvLastMessage);

        if (!listUser.get(i).getReceiverName().equals("")) {
            _tvNameSurname.setText(listUser.get(i).getReceiverName());
        } else if (!listUser.get(i).getReceiverPhone().equals("")) {
            if (listUser.get(i).getReceiverPhone().equals((String) Tools.getSharedPrefences(ctx, "phoneNumber", String.class))) {
                _tvNameSurname.setText(listUser.get(i).getSenderPhone());
            }
        } else {
            _tvNameSurname.setText(listUser.get(i).getReceiverName());
        }
        if (listUser.get(i).getAvatar() != null && !listUser.get(i).getAvatar().equals("")) {
            Picasso.with(ctx).load(listUser.get(i).getAvatar()).fit().centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(_ivAvatar);
        } else {
            _ivAvatar.setImageResource(R.drawable.default_avatar);
        }
        if (listUser.get(i).getLastMessage().length() > 30) {
            _tvLastMessage.setText(listUser.get(i).getLastMessage().substring(0, 30) + "...");
        } else {
            _tvLastMessage.setText(listUser.get(i).getLastMessage());
        }


        return myView;
    }
}