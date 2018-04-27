package com.dashboard.telekrip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.OldMessage;
import com.dashboard.telekrip.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterOldUserMessage extends BaseAdapter {
    TextView _tvNameSurname;
    CircleImageView _ivAvatar;
    private Context ctx;
    private List<OldMessage> listUser;

    public AdapterOldUserMessage(Context ctx, List<OldMessage> listUser){
        this.ctx=ctx;
        this.listUser=new ArrayList<>(listUser);
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
        View myView =view;
        if(myView==null){
            LayoutInflater ınflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView= ınflater.inflate(R.layout.user_listview_item,null);
        }
        _tvNameSurname=myView.findViewById(R.id.tvNameSurname);
        _ivAvatar=myView.findViewById(R.id.ivAvatar);

        if(listUser.get(i).getSenderName().equals("")){
            _tvNameSurname.setText(listUser.get(i).getReceiverPhone());
        }
        else {
            _tvNameSurname.setText(listUser.get(i).getReceiverName());
        }
        /*Picasso.with(ctx).load(listUser.get(i).getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);*/


        return myView;
    }
}
