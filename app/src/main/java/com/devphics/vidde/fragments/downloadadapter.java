package com.devphics.vidde.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.devphics.vidde.R;

import java.util.List;

public class downloadadapter extends BaseAdapter {
    Context context;
    List<String> mainurls;
    List<String> mainsize;
    List<String> mainname;
    List<String> maintype;
    List<Boolean> audio;
    long downloadId = -199;
    LayoutInflater inflater;

    public downloadadapter(Context context, List<String> mainurls, List<String> mainsize,List <String> mainname, List<String> maintype, List<Boolean> audio, LayoutInflater inflater) {
        this.context = context;
        this.mainurls = mainurls;
        this.mainsize = mainsize;
        this.mainname = mainname;
        this.maintype = maintype;
        this.audio = audio;
        this.inflater = inflater;

    }


    @Override
    public int getCount() {
        return mainurls.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.download_listview, null);
        TextView size = view.findViewById(R.id.fileSizeDownloadListItem);
        CheckBox checkBox = view.findViewById(R.id.checkboxlist);
        TextView frame = view.findViewById(R.id.fileFrameDownloadListItem);


        Button downloadBtn = view.findViewById(R.id.downloadBtnDownloadList);

//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkBox.isChecked()) {
//                    String reacquiredUrl = mainurls.get(i);
//                    Intent intent = new Intent("custom-message");
//                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
//                    intent.putExtra("quantity", reacquiredUrl);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//
//
//                }
//
//            }
//        });
//        if (mainname.get(i) != null) {
//            String sendurl = mainurls.get(0);
//            Intent intentname = new Intent("video-name");
//            //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
//            intentname.putExtra("videosname", mainname.get(0));
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intentname);
//
//            SharedPreferences.Editor editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
//            editor.putString("nameurl", sendurl);
//            // editor.putInt("idName", 12);
//            editor.apply();
//            editor.clear();
//            editor.apply();
//
//        }
        //  Toast.makeText(context, "check mp3 file"+mainname.get(i), Toast.LENGTH_SHORT).show();
//        if (maintype.get(i).contains("mp3")) {
//            Toast.makeText(context, "this mp3 url ", Toast.LENGTH_SHORT).show();
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mainurls.get(i)));
//            request.setDescription("Downloading");
//            request.setMimeType("audio/MP3");
//            request.setTitle("File :");
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "audio.mp3");
//            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            manager.enqueue(request);
//        }


//        Intent intentname = new Intent("custom-name");
//        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
//        intentname.putExtra("mainname", mainname.get(i));
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intentname);

//        if (maintype.get(i).equals("mp4")){
//            String sizete=(mainsize.get(i));
//        try {
//            long fileSizeInBytes=Long.parseLong(sizete);
//            long fileSizeInKB = fileSizeInBytes / 1024;
//            long fileSizeInMB = fileSizeInKB / 1024;
//            String s=String.valueOf(fileSizeInMB);
//            size.setText("Size"+"\t\t"+s+"mb");
//            name.setText(mainname.get(i));
//
//        }
//        catch(Exception e) {
//            //  Block of code to handle errors
//        }
        if (mainsize.get(i) != null) {
            String sizeFormatted = Formatter.formatShortFileSize(context,
                    Long.parseLong(mainsize.get(i)));
            size.setText(sizeFormatted);
            //name.setText(mainframe.get(i));
        } else size.setText("");

        if (!maintype.get(i).isEmpty()) {
            frame.setText(maintype.get(i));

        }


        // }
        String dataUrl = mainurls.get(i);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                DownloadManager manager;
                manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                if (downloadId!=-199) {
                    manager.remove(downloadId);
                    downloadBtn.setBackground(context.getResources().getDrawable(R.drawable.ic_file_download_green));
                    downloadId = -199;

                } else {

                    Uri uri = Uri.parse(dataUrl);
                    String fileName = URLUtil.guessFileName(dataUrl,null,null);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, fileName    //Download folder
                    );  //Name of file
                    downloadId = manager.enqueue(request);
                    downloadBtn.setBackground(context.getResources().getDrawable(R.drawable.stop));

                }


            }
        });
        return view;

    }

//    public CustomAdapter(Context applicationContext, String[] countryList, int[] flags) {
//        this.context = context;
//        this.countryList = countryList;
//        this.flags = flags;
//        inflter = (LayoutInflater.from(applicationContext));
//    }

//    @Override
//    public int getCount() {
//        return countryList.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = inflter.inflate(R.layout.activity_listview, null);
//        TextView country = (TextView)           view.findViewById(R.id.textView);
//        ImageView icon = (ImageView) view.findViewById(R.id.icon);
//        country.setText(countryList[i]);
//        icon.setImageResource(flags[i]);
//        return view;
//    }
}