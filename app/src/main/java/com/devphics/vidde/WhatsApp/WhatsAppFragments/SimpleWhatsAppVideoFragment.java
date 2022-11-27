package com.devphics.vidde.WhatsApp.WhatsAppFragments;


import static android.content.ContentValues.TAG;
import static com.devphics.vidde.activities.PermissionActivity.BWAPath;
import static com.devphics.vidde.activities.PermissionActivity.WAPath;
import static com.devphics.vidde.activities.PermissionActivity.WHATSAPP_BUSINESS;
import static com.devphics.vidde.activities.PermissionActivity.WHATSAPP_SIMPLE;
import static com.devphics.vidde.activities.PermissionActivity.isPermissionsAvailAndroidQ;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devphics.vidde.Adapters.StatusAdapterAndroidQ;
import com.devphics.vidde.ModelClasses.StatusModel;
import com.devphics.vidde.activities.PermissionActivity;
import com.devphics.vidde.databinding.FragmentSimpleWhatsappVideoBinding;
import com.devphics.vidde.util.BasicData;
import com.devphics.vidde.Adapters.WhatsAppVideoAdapter;

import java.io.File;
import java.util.ArrayList;


public class SimpleWhatsAppVideoFragment extends Fragment {

    BasicData basicData  = new BasicData(getActivity());
    private final String TARGET_STATUS_LOCATION = basicData.VIDEOS_DESTINATION;
    private final Context context = getContext();
    private File TARGET_FILE;




    private RecyclerView recyclerView;
    FragmentSimpleWhatsappVideoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSimpleWhatsappVideoBinding.inflate(inflater,container,false);


        TARGET_FILE = Build.VERSION.SDK_INT >= 29 ? new File(
                (BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
        ) : new File(
                BasicData.getInternalStorageDirectoryPath(getActivity()) +
                        File.separator + "WhatsApp/Media/.Statuses"
        );
        Log.i(TAG, "onCreateView: "+TARGET_FILE.getAbsolutePath());
        recyclerView = binding.recylerViewVideosSimpleW;








        if(Build.VERSION.SDK_INT>28){
            if(isPermissionsAvailAndroidQ(getContext())){
                ArrayList<StatusModel> listOfFiles = getStatusListAndroidQ(WHATSAPP_SIMPLE);
                ArrayList<StatusModel> listToShow = new ArrayList<>();
                for(StatusModel f: listOfFiles){
                    if(f.getFileUri().endsWith(".mp4")){

                        Log.i(TAG, "onCreateView: FileName"+f.getName());
                        listToShow.add(f);
                    }
                }
                if(listToShow.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    binding.noDataLayout.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(new StatusAdapterAndroidQ(getContext(),listToShow));


            }else {
                startActivity(new Intent(getActivity(), PermissionActivity.class));
            }
        }else{

            getDataWhatsApp(TARGET_FILE,TARGET_STATUS_LOCATION);
        }





        return binding.getRoot();
    }


    @NonNull
    private ArrayList<StatusModel> getStatusListAndroidQ(int Type){
        ArrayList<StatusModel> list = new ArrayList<>();


        switch (Type){
            case WHATSAPP_SIMPLE:
                if(getContext()!=null){

                    if(WAPath!=null && !WAPath.isEmpty() && !WAPath.equals(" ")){
                        DocumentFile file = DocumentFile.fromTreeUri(getContext(), Uri.parse(WAPath));
                        if(file!=null){
                            DocumentFile[] files = file.listFiles();
                            for (DocumentFile f : files){
                                list.add(new StatusModel(f.getName(),f.getUri().toString()));
                            }

                        }
                    }
                }
                break;
            case WHATSAPP_BUSINESS:
                if(getContext()!=null){

                    if(WAPath!=null && !WAPath.isEmpty() && !WAPath.equals(" ")){
                        DocumentFile file = DocumentFile.fromTreeUri(getContext(), Uri.parse(BWAPath));
                        if(file!=null){
                            DocumentFile[] files = file.listFiles();
                            for (DocumentFile f : files){
                                list.add(new StatusModel(f.getName(),f.getUri().toString()));
                            }

                        }
                    }

                }
                break;

        }
        return list;
    }

    private void getDataWhatsApp(File target,String destination) {


        ArrayList<File> statusFiles = GetMyFilesList(target);
        if (!statusFiles.isEmpty()) {
            setUpRecyclerView();
            recyclerView.setAdapter(new WhatsAppVideoAdapter(statusFiles, getContext(), destination));
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
                if (f.getName().endsWith(".mp4")) {
                    if (!MyFiles.contains(f)) MyFiles.add(f);
                }
            }
        }


        return MyFiles;
    }


}