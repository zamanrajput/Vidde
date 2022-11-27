package com.devphics.vidde.WhatsApp.WhatsAppFragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.devphics.vidde.databinding.FragmentGbWhatsappVideoBinding;
import com.devphics.vidde.util.BasicData;
import com.devphics.vidde.Adapters.WhatsAppVideoAdapter;

import java.io.File;
import java.util.ArrayList;


public class GBWhatsAppVideoFragment extends Fragment {
    BasicData basicData = new BasicData(getActivity());
    private final String TARGET_STATUS_LOCATION = BasicData.VIDEOS_DESTINATION;
    private final Context context=getContext();

    FragmentGbWhatsappVideoBinding binding;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGbWhatsappVideoBinding.inflate(inflater,container,false);
        File TARGET_FILE = Build.VERSION.SDK_INT >= 29 ? new File(
                (BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "Android/media/com.whatsapp/GBWhatsApp/Media/.Statuses")
        ) : new File(
                BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "GBWhatsApp/Media/.Statuses"
        );


        getDataWhatsApp(TARGET_FILE,TARGET_STATUS_LOCATION);



        return binding.getRoot();
    }

    private void getDataWhatsApp(File target,String destination) {


        ArrayList<File> statusFiles = GetMyFilesList(target);
        if (!statusFiles.isEmpty()) {
            setUpRecyclerView();
            binding.recylerViewVideosGBW.setAdapter(new WhatsAppVideoAdapter(statusFiles, getContext(), destination));
        }


    }

    private void setUpRecyclerView() {
        binding.noDataLayout.setVisibility(View.GONE);
        binding.recylerViewVideosGBW.setVisibility(View.VISIBLE);
        binding.recylerViewVideosGBW.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager = new GridLayoutManager(context, 2);
        layoutManager.scrollToPosition(0);
        binding.recylerViewVideosGBW.setLayoutManager(layoutManager);
    }

    private ArrayList<File> GetMyFilesList(File file) {

        ArrayList<File> MyFiles = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.getName().endsWith(".mp4")) {
                    if (!MyFiles.contains(f)) MyFiles.add(f);
                }
            }
        }


        return MyFiles;
    }
}