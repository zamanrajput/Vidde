package com.devphics.vidde.fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devphics.vidde.Adapters.VideosAdapter;
import com.devphics.vidde.ModelClasses.VideosData;
import com.devphics.vidde.R;

import java.util.ArrayList;
import java.util.Collections;

public class PastDownloadsFragment extends Fragment {
    VideosAdapter videoAdapter;
    ArrayList<VideosData> arrayList = new ArrayList<VideosData>();
    View view;
    RecyclerView rv;

    public PastDownloadsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_past_downloads, container, false);
        rv = view.findViewById(R.id.recyclerview);

        getAllFromDownloads();
        getAllFromStatus();
        return view;
    }

    public void getAllFromDownloads() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " like?";
        String c = "Download";
        String[] selectionArgs = new String[]{"%/"+c+"/%"};

        Cursor cursor = contentResolver.query(uri, null, selection,
                selectionArgs, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String DATA = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                // Bitmap bMap = ThumbnailUtils.createVideoThumbnail(getActivity().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);


                Log.e("VIDEO_DATA", "duration" + DATA);
                long sizeVal = Long.parseLong(size);
                //               int durationIs = Integer.parseInt(duration);
//                if (data.endsWith(".mp4")){
                Collections.reverse(arrayList);
                VideosData videoModel = new VideosData(title, mimeType, sizeVal, "", data,"",id,0);
                arrayList.add(videoModel);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                rv.setAdapter(videoAdapter);
//                }
//                else {
//                    Log.e("VIDEO_DATA", "DAta" + DATA);
//                }


                //                VideoModel videoModel = new VideoModel();


            } while (cursor.moveToNext());
        }


    }
    public void getAllFromStatus() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%/SavedVideos/%"};

        Cursor cursor = contentResolver.query(uri, null, selection,
                selectionArgs, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));


                @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String DATA = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                // Bitmap bMap = ThumbnailUtils.createVideoThumbnail(getActivity().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);

                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                Log.e("VIDEO_DATA", "duration" + DATA);
                long sizeVal = Long.parseLong(size);                //               int durationIs = Integer.parseInt(duration);
//                if (data.endsWith(".mp4")){
                Collections.reverse(arrayList);
                VideosData videoModel = new VideosData(title, mimeType, sizeVal, "", data,"",id,0);
                arrayList.add(videoModel);


//

            } while (cursor.moveToNext());
            videoAdapter = new VideosAdapter(getActivity(),arrayList);
            rv.setAdapter(videoAdapter);
        }



    }




}

