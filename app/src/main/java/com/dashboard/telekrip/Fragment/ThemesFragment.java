package com.dashboard.telekrip.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;

@SuppressLint("ValidFragment")
public class ThemesFragment extends Fragment {

    FrameLayout _flTheme;
    int position=0;
    @SuppressLint("ValidFragment")
    public ThemesFragment(int position) {
        this.position=position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_themes, container, false);
        _flTheme=view.findViewById(R.id.flTheme);
        switch (position){
            case 0:{
                _flTheme.setBackgroundResource(R.mipmap.bg1);
                break;
            }
            case 1:{_flTheme.setBackgroundResource(R.mipmap.bg2);
                break;}
            case 2:{
                _flTheme.setBackgroundResource(R.mipmap.bg3);
                break;
            }
            case 3:{
                _flTheme.setBackgroundResource(R.mipmap.bg4);
                break;
            }
            case 4:{
                _flTheme.setBackgroundResource(R.mipmap.bg5);
                break;
            }
            case 5:{
                _flTheme.setBackgroundResource(R.mipmap.bg6);
                break;
            }
            case 6:{
                _flTheme.setBackgroundResource(R.mipmap.bg7);
                break;
            }
        }
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.setSharedPrefences(getActivity(),"theme",position);
                Snackbar.make(view, "Tema seçimi başarılı", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
