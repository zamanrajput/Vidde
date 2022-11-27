package com.devphics.vidde.WhatsApp.WhatsAppFragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devphics.vidde.databinding.FragmentGbWhatsappImagesBinding;
import com.devphics.vidde.Adapters.WhatsAppImagesAdapter;
import com.devphics.vidde.util.BasicData;

import java.io.File;
import java.util.ArrayList;


public class GBWhatsAppImagesFragment extends Fragment {

    BasicData data = new BasicData(getActivity());

    private final String TARGET_STATUS_LOCATION = data.IMAGES_DESTINATION;
    private final Context context=getContext();
    private RecyclerView recyclerView;
    FragmentGbWhatsappImagesBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGbWhatsappImagesBinding.inflate(inflater,container,false);
        File TARGET_FILE = Build.VERSION.SDK_INT >= 29 ? new File(
                (BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "Android/media/com.whatsapp/GBWhatsApp/Media/.Statuses")
        ) : new File(
                BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "GBWhatsApp/Media/.Statuses"
        );

        recyclerView = binding.recylerViewImagesGB;




        getDataWhatsApp(TARGET_FILE, TARGET_STATUS_LOCATION);


        return binding.getRoot();
    }

    private void getDataWhatsApp(File target, String destination) {


        ArrayList<File> statusFiles = GetMyFilesList(target);
        if (!statusFiles.isEmpty()) {
            setUpRecyclerView();
            recyclerView.setAdapter(new WhatsAppImagesAdapter(statusFiles, getContext(), destination));
        }


    }

    private void setUpRecyclerView() {
        binding.noDataLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager = new GridLayoutManager(context, 2);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
    }

    private ArrayList<File> GetMyFilesList(File file) {

        ArrayList<File> MyFiles = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) {
                    if (!MyFiles.contains(f)) MyFiles.add(f);
                }
            }
        }


        return MyFiles;
    }
}