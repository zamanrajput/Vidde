package com.devphics.vidde.Adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devphics.vidde.ModelClasses.StatusModel;
import com.devphics.vidde.R;
import com.devphics.vidde.activities.ViewingVideoActivity;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class StatusAdapterAndroidQ extends RecyclerView.Adapter<StatusAdapterAndroidQ.VH> {
    Context context;
    ArrayList<StatusModel> dataList;

    public StatusAdapterAndroidQ(Context context, ArrayList<StatusModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.whatsapp_video_status,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StatusModel file = dataList.get(position);

        holder.shareBtn.setOnClickListener(view ->shareVideo(file));


        if(file.getFileUri().endsWith(".mp4")){
            holder.playBtn.setVisibility(View.VISIBLE);
        }else {
            holder.playBtn.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewingVideoActivity.class);
            intent.putExtra("type",file.getFileUri().endsWith(".mp4")?"mp4":"image");
            intent.putExtra("NEW_VIDEODATA",file.getFileUri());
            intent.putExtra("MyFilesList", file.getFileUri());
            context.startActivity(intent);
        });
        if(file.getFileUri().contains("Saved")){
            holder.downloadBtn.setVisibility(View.GONE);
            holder.shareBtn.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.app_logo)
                    .setMessage("Continue To Save?")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(saveFile(file)){
                                Toast.makeText(context,"Saved Successfully",Toast.LENGTH_SHORT).show();
                            };
                        }
                    })
                    .setNegativeButton("No",null);
            builder.show();

        });



        Glide.with(context).load(Uri.parse(file.getFileUri())).into(holder.thumNail);



    }

    private void shareVideo(@NonNull StatusModel data){


        Uri uri = Uri.parse(data.getFileUri());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(Intent.createChooser(shareIntent,"Share Video via"));


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean saveFile(StatusModel file){
        if(file.getFileUri().endsWith(".mp4")){
            try {
                InputStream stream = context.getContentResolver().openInputStream(Uri.parse(file.getFileUri()));
                String fileName = System.currentTimeMillis()+".mp4";
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE,"video/mp4");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/Status Saver/SavedVideos");
                Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"),values);
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (stream!=null){
                    IOUtils.copy(stream,outputStream);
                }
                stream.close();

                outputStream.close();

            } catch (IOException e) {
                Toast.makeText(context,"Saved Failed",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
                return false;
            }
        }else if(file.getFileUri().endsWith(".jpg")){
            try {
                InputStream stream = context.getContentResolver().openInputStream(Uri.parse(file.getFileUri()));
                String fileName = System.currentTimeMillis()+".jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/Status Saver/SavedImages");
                Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"),values);
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (stream!=null){
                    IOUtils.copy(stream,outputStream);
                }
                stream.close();

                outputStream.close();

            } catch (IOException e) {
                Toast.makeText(context,"Saved Failed",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    static class VH extends RecyclerView.ViewHolder{

        ImageView thumNail,playBtn;
        Button downloadBtn,shareBtn;


        public VH(@NonNull View itemView) {
            super(itemView);
            shareBtn = itemView.findViewById(R.id.shareBtnIV);
            downloadBtn = itemView.findViewById(R.id.downloadBtnIV);
            thumNail = itemView.findViewById(R.id.status_image_view);
            playBtn = itemView.findViewById(R.id.playBtnVS);


        }
    }
}
