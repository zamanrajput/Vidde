package com.devphics.vidde.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;
import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.devphics.vidde.Adapters.StatusAdapterAndroidQ;
import com.devphics.vidde.Adapters.VideosAdapterSavedStatus;
import com.devphics.vidde.ModelClasses.StatusModel;
import com.devphics.vidde.ModelClasses.VideosData;
import com.devphics.vidde.databinding.FragmentSavedStatusBinding;

import java.util.ArrayList;

public class SavedStatusFragment extends Fragment {

    FragmentSavedStatusBinding binding;
    String pathTar;
    int REQ_CODE = 10122;
    StatusAdapterAndroidQ adapterAndroidQ;
    VideosAdapterSavedStatus adapter2;
    ArrayList<VideosData> listOfStatus = new ArrayList<>();
    private final ArrayList<StatusModel> listOfStatusAndroidQ = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedStatusBinding.inflate(inflater);
        binding.allPermissionBtn.setOnClickListener(view -> getPermissionsAndroidQ());
        adapterAndroidQ = new StatusAdapterAndroidQ(getContext(), listOfStatusAndroidQ);
        binding.savedStatusRecView.setAdapter(adapterAndroidQ);
        String Path = getContext().getSharedPreferences("PATH_DATA", MODE_PRIVATE).getString("SavedPath", "");
//        Log.i(TAG, "path:"+Path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Path.equals("")) {
                getAllFilesAndroidQ(Path);
            } else {
                binding.noPermissionsAndroidQ.setVisibility(View.VISIBLE);
            }

        } else {

            adapter2 = new VideosAdapterSavedStatus(getContext(), listOfStatus);
            binding.savedStatusRecView.setAdapter(adapter2);
            getAllFromDirectory("SavedVideos");
        }


        return binding.getRoot();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void getAllFilesAndroidQ(String Path) {
        try {
            if (getContext() != null) {
                DocumentFile file = DocumentFile.fromTreeUri(getContext(), Uri.parse(Path));
                if (file != null) {
                    DocumentFile[] files = file.listFiles();
                    for (DocumentFile f : files) {
                        if (f.getUri().toString().endsWith(".mp4")) {
                            listOfStatusAndroidQ.add(new StatusModel(f.getName(), f.getUri().toString()));
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Files Not Found", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        adapterAndroidQ.notifyDataSetChanged();


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getPermissionsAndroidQ() {

        String target = "Download%2FStatus Saver%2FSavedVideos";

        StorageManager manager = (StorageManager) getContext().getSystemService(STORAGE_SERVICE);
        Intent intent = manager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();


        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/tree/");
        scheme += "%3A" + target;
        uri = Uri.parse(scheme);
//        intent.putExtra("android.provider.extra.SHOW_HIDDEN",true);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        intent.putExtra("android.provider.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent, REQ_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    SharedPreferences preferences = getContext().getSharedPreferences("PATH_DATA", MODE_PRIVATE);
                    Uri treeUri = data.getData();

                    if (requestCode == REQ_CODE) {
                        getAllFilesAndroidQ(treeUri.toString());
                        preferences.edit().putString("SavedPath", treeUri.toString()).apply();
                        binding.noPermissionsAndroidQ.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Field to Receive permission", Toast.LENGTH_SHORT).show();
                }

            }

        }


    }


    @SuppressLint("NotifyDataSetChanged")
    public void getAllFromDirectory(String directory) {

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%/" + directory + "/%"};

        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, selection,
                selectionArgs, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));


                @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String DATA = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                // Bitmap bMap = ThumbnailUtils.createVideoThumbnail(getActivity().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                Log.e("VIDEO_DATA", "duration" + DATA);
                long sizeVal = Long.parseLong(size);
                VideosData videoModel = new VideosData(title, mimeType, sizeVal, "", data, "", id, duration);
                listOfStatus.add(videoModel);
                Log.i(TAG, "getAllFromDirectory: " + title);


            } while (cursor.moveToNext());
            if (listOfStatus.size() > 0) {
                binding.noFilesFound.setVisibility(View.GONE);
            } else {
                binding.noFilesFound.setVisibility(View.VISIBLE);
            }

            adapter2.notifyDataSetChanged();
            cursor.close();
        }


    }


}