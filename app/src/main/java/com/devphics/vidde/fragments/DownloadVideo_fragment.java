package com.devphics.vidde.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devphics.vidde.R;

import java.util.List;


public class DownloadVideo_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_download_video_fragment, container, false);

        List<String> list = getArguments().getStringArrayList("myList");
      String get=list.get(0);

        Toast.makeText(getContext(), "its working or not "+get, Toast.LENGTH_SHORT).show();


        return view;
    }
}