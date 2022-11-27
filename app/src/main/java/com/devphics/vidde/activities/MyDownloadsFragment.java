package com.devphics.vidde.activities;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devphics.vidde.Adapters.DownloadingAdapter;
import com.devphics.vidde.Adapters.StatusAdapterAndroidQ;
import com.devphics.vidde.Adapters.VideosAdapter;
import com.devphics.vidde.ModelClasses.DownloadModel;
import com.devphics.vidde.ModelClasses.StatusModel;
import com.devphics.vidde.ModelClasses.VideosData;
import com.devphics.vidde.R;
import com.devphics.vidde.databinding.FragmentMyDownloadsBinding;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MyDownloadsFragment extends Fragment {

    FragmentMyDownloadsBinding binding;

    ArrayList<DownloadModel> downloadingList = new ArrayList<>();
    ArrayList<VideosData> listOfCompletedDownloads = new ArrayList<>();


    VideosAdapter adapterDownloaded;
    static DownloadingAdapter adapterDownloading;
    VideosAdapter adapterStatus;
    private ArrayList<StatusModel> listOfStatusAndroidQ=new ArrayList<>();
    private Adapter adapterAndroidQ;
    private int REQ_CODE=132;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyDownloadsBinding.inflate(inflater,container,false);

        adapterAndroidQ = new Adapter(listOfStatusAndroidQ);
        adapterDownloaded = new VideosAdapter(getContext(), listOfCompletedDownloads);
        adapterDownloading = new DownloadingAdapter(getContext(), downloadingList);




        binding.recViewAndroidQ.setAdapter(adapterAndroidQ);
        binding.downloadingRecView.setAdapter(adapterDownloading);
        binding.downloadedRecView.setAdapter(adapterDownloaded);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            binding.allPermissionBtn.setOnClickListener(view1->getPermissionsAndroidQ());

            String Path = getContext().getSharedPreferences("PATH_DATA", MODE_PRIVATE).getString("SavedDownload","");
            Log.i(TAG, "path"+Path);

            if(!Path.equals("")){
                binding.textDownloadTv.setVisibility(View.VISIBLE);
                binding.recViewAndroidQ.setVisibility(View.VISIBLE);
                getAllFilesAndroidQ(Path);
            }else {
                binding.noPermissionsAndroidQ.setVisibility(View.VISIBLE);
            }
        }else {

            loadFiles();
        }





        return binding.getRoot();
    }


    class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        ArrayList<StatusModel> list;

        public Adapter(ArrayList<StatusModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.item_view_downloads_android_q,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.mName.setText(list.get(position).getName());
            holder.share.setOnClickListener(view -> shareVideo(list.get(position)));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), ViewingVideoActivity.class);
                intent.putExtra("type",list.get(position).getFileUri().endsWith(".mp4")?"mp4":"image");
                intent.putExtra("NEW_VIDEODATA",list.get(position).getFileUri());
                intent.putExtra("MyFilesList", list.get(position).getFileUri());
               getContext().startActivity(intent);

            });
            Glide.with(getContext()).load(list.get(position).getFileUri()).into(holder.thumb);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        private void shareVideo(@NonNull StatusModel data){

            Uri uri = Uri.parse(data.getFileUri());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(Intent.createChooser(shareIntent,"Share Video via"));


        }


        class VH extends RecyclerView.ViewHolder{
            TextView mName;
            ImageView thumb,share;
            public VH(@NonNull View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.item_view_d_android_q_name);
                thumb = itemView.findViewById(R.id.item_view_d_android_q_thumnail);
                share =itemView.findViewById(R.id.item_view_downlaod_android_q_share);
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getPermissionsAndroidQ() {

        String target = "Download";

        StorageManager manager = (StorageManager)getContext().getSystemService(STORAGE_SERVICE);
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
    @SuppressLint("NotifyDataSetChanged")
    private void  getAllFilesAndroidQ(String Path) {
        try {
            if(getContext()!=null){
                DocumentFile file = DocumentFile.fromTreeUri(getContext(),Uri.parse(Path));
                if(file!=null){
                    DocumentFile[] files = file.listFiles();
                    for(DocumentFile f :files){
                        if(f.getUri().toString().endsWith(".mp4")){
                            listOfStatusAndroidQ.add(new StatusModel(f.getName(),f.getUri().toString()));
                        }
                    }
                }else {
                    Toast.makeText(getContext(),"Files Not Found",Toast.LENGTH_SHORT).show();
                }

            }

        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(listOfStatusAndroidQ.size()==0){
            binding.noFilesFound.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(),"Files Not Found",Toast.LENGTH_SHORT).show();

        }

        adapterAndroidQ.notifyDataSetChanged();


    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(data!=null){
                try {
                    SharedPreferences preferences = getContext().getSharedPreferences("PATH_DATA", MODE_PRIVATE);
                    Uri treeUri = data.getData();

                    if(requestCode==REQ_CODE){
                        getAllFilesAndroidQ(treeUri.toString());
                        preferences.edit().putString("SavedDownload",treeUri.toString()).apply();
                        binding.noPermissionsAndroidQ.setVisibility(View.GONE);
                        getAllFilesAndroidQ(treeUri.toString());
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

        }




    }



    private void loadFiles() {

        getAllDownloading();
        getAllFromDirectory("Twitter Videos");
        getAllFromDirectory("Download");
    }

    @SuppressLint("Range")
    private void getAllDownloading() {
        DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query q = new DownloadManager.Query();
        Cursor cursor = manager.query(q);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI));
                @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                @SuppressLint("Range") String totalSize = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                @SuppressLint("Range") String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                long mId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                Collections.reverse(downloadingList);
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_RUNNING) {
                    DownloadModel videoModel = new DownloadModel(title, mimeType, path, mId, false, 0, Integer.parseInt(totalSize));
                    downloadingList.add(videoModel);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        adapterDownloading = new DownloadingAdapter(getContext(), downloadingList);
        binding.downloadingRecView.setAdapter(adapterDownloading);
        if(downloadingList.size()>0){
            binding.downloadingHeading.setVisibility(View.VISIBLE);
        }

        for (DownloadModel model : downloadingList) {
            runTask(new DownloadTask(model), model.getId());
        }

    }


    public class DownloadTask extends AsyncTask<String, String, String> {
        DownloadModel downloadModel;

        public DownloadTask(DownloadModel downloadModel) {
            this.downloadModel = downloadModel;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            this.downloadModel.setTotalSize(Integer.parseInt(values[1]));
            this.downloadModel.setProgress(Integer.parseInt(values[0]));
            adapterDownloading.changeItemById(downloadModel.getId());

            if (Integer.parseInt(values[0]) == downloadModel.getTotalSize()) {
                downloadingList.remove(downloadModel);
                adapterDownloading.notifyDataSetChanged();
                adapterDownloading.refreshDownloaded(downloadModel, listOfCompletedDownloads, adapterDownloaded);
                if (downloadingList.size() == 0) {
                    binding.downloadingHeading.setVisibility(View.GONE);
                }
            }


        }

        @SuppressLint("Range")
        @Override
        protected String doInBackground(String... strings) {

            DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            String progress;
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(Long.parseLong(strings[0]));
                Cursor cursor = manager.query(query);
                cursor.moveToFirst();

                int d, t;
                d = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                t = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;

                }
                cursor.close();
                int p = (int) (d);

                progress = String.valueOf(p);
                publishProgress(progress, String.valueOf(t));

            }
            return null;
        }


    }


    public void runTask(DownloadTask task, long ID) {
        try {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(ID));

        } catch (Exception e) {
            e.printStackTrace();
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
                listOfCompletedDownloads.add(videoModel);
            } while (cursor.moveToNext());
            adapterDownloaded.notifyDataSetChanged();
            cursor.close();
        }

        if(listOfCompletedDownloads.size()>0){
            binding.downloadedHeading.setVisibility(View.VISIBLE);
        }





    }



}