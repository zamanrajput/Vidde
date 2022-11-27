package com.devphics.vidde.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devphics.vidde.ModelClasses.VideosData;
import com.devphics.vidde.R;
import com.devphics.vidde.activities.ViewingVideoActivity;
import com.devphics.vidde.databinding.ActivitySplashScreenBinding;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class VideosAdapterSavedStatus extends RecyclerView.Adapter<VideosAdapterSavedStatus.viewHolder> {
    Context context;
    Dialog moreOptDialog;
    ArrayList<VideosData> arrayList;

    private static final int REQ_COD2 = 113;
    ActivitySplashScreenBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;

    public VideosAdapterSavedStatus(Context context, ArrayList<VideosData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_status_list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final viewHolder holder, int i) {
        VideosData vIdeosData = arrayList.get(i);



        holder.moreBtn.setOnClickListener(view -> openPopUp(vIdeosData, i));
        holder.itemView.setOnLongClickListener(view -> openPopUp(vIdeosData, i));
        holder.itemView.setOnClickListener(view -> playVideo(vIdeosData));
        holder.delBtn.setOnClickListener(view -> deleteVideo(vIdeosData,i));
        holder.shareBtn.setOnClickListener(view -> shareVideo(vIdeosData));


        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(vIdeosData.getData())
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(vIdeosData.getData()))
                .into(holder.image);

    }




    @SuppressLint("SimpleDateFormat")
    private String getDuration(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat formatter;
        formatter = millis >= 3.6e6 ? new SimpleDateFormat("HH:mm:ss") : new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean openPopUp(VideosData vIdeosData, int i) {
        moreOptDialog = new Dialog(context);
        moreOptDialog.setContentView(R.layout.more_option_dailog);
        Button playBtn = moreOptDialog.findViewById(R.id.playBtn);
        Button renameBtn = moreOptDialog.findViewById(R.id.renameBtn);
        Button openWithBtn = moreOptDialog.findViewById(R.id.openWithBtn);
        Button fileInfoBtn = moreOptDialog.findViewById(R.id.infoFileBtn);
        Button shareBtn = moreOptDialog.findViewById(R.id.quickShareBtn);
        Button deleteBtn = moreOptDialog.findViewById(R.id.deleteBtn);
        ImageView imageView = moreOptDialog.findViewById(R.id.thumbnailIV);
        TextView filenameTv = moreOptDialog.findViewById(R.id.filenamTv);

        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(vIdeosData.getData())
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(vIdeosData.getData()))
                .into(imageView);


        filenameTv.setText(vIdeosData.getTitle());
        renameBtn.setOnClickListener(view -> renameVideo(vIdeosData));
        playBtn.setOnClickListener(view1 -> playVideo(vIdeosData));
        openWithBtn.setOnClickListener(view1 -> openWith(vIdeosData));
        fileInfoBtn.setOnClickListener(view1 -> fileInfo(vIdeosData));
        shareBtn.setOnClickListener(view1 -> shareVideo(vIdeosData));
        deleteBtn.setOnClickListener(view1 -> deleteVideo(vIdeosData, i));
        imageView.setOnClickListener(view -> playVideo(vIdeosData));

        moreOptDialog.show();
        return false;
    }

    private void renameVideo(VideosData vIdeosData) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename To");
        EditText editText = new EditText(context);
        editText.setPadding(10, 0, 0, 10);
        File file = new File(vIdeosData.getData());
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        editText.setText(fileName);
        builder.setView(editText);
        editText.requestFocus();
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String onlyPath = file.getParentFile().getAbsolutePath();
                String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                String newPah = onlyPath + "/" + editText.getText().toString() + ext;

                File newFile = new File(newPah);
                boolean ren = file.renameTo(newFile);
                if (ren) {
                    ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
                    contentResolver.delete(MediaStore.Video.Media.getContentUri("external"),
                            MediaStore.MediaColumns.DATA + "=?", new String[]{
                                    file.getAbsolutePath()
                            });
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(newFile));
                    context.getApplicationContext().sendBroadcast(intent);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Video Renamed Successfully", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null);

        builder.create().show();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void fileInfo(VideosData vIdeosData) {

        String PATH = vIdeosData.getData().substring(0, vIdeosData.getData().lastIndexOf("/"));
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(vIdeosData.getData());
        String height = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String millis = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.properties_layout);
        TextView title = dialog.findViewById(R.id.fileTitle);
        TextView type = dialog.findViewById(R.id.fileType);
        TextView path = dialog.findViewById(R.id.filePath);
        TextView duration = dialog.findViewById(R.id.fileDuration);
        TextView size = dialog.findViewById(R.id.fileSize);
        TextView resolution = dialog.findViewById(R.id.fileResolution);

        Button buttonOK = dialog.findViewById(R.id.okBtn);
        ImageView thumbnail = dialog.findViewById(R.id.thumbnailProperties);


        title.setText(vIdeosData.getTitle());
        type.setText(vIdeosData.getMimeType().split("/")[1]);
        path.setText(PATH);


        duration.setText(getDuration(Long.parseLong(millis)));
        size.setText(getFileSize(vIdeosData.getSize()));
        buttonOK.setOnClickListener(view -> dialog.dismiss());
        resolution.setText(width + "x" + height);


        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(vIdeosData.getData())
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(vIdeosData.getData()))
                .into(thumbnail);

        dialog.show();

        if (moreOptDialog != null) {
            moreOptDialog.dismiss();
        }

    }

    private void openWith(VideosData data) {
        Uri uri = Uri.parse(data.getData());
        Intent shareIntent = new Intent(Intent.CATEGORY_OPENABLE);


        shareIntent.setType("video/* ");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent, "Share Video via"));

        if (moreOptDialog != null) {
            moreOptDialog.cancel();
        }
    }

    private void playVideo(@NonNull VideosData data) {
        Intent intent = new Intent(context, ViewingVideoActivity.class);
        intent.putExtra("NEW_VIDEODATA", data.getData());
        intent.putExtra("type", "Video_Adapter");
        context.startActivity(intent);

    }

    private void shareVideo(@NonNull VideosData data) {
        Uri uri = Uri.parse(data.getData());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent, "Share Video via"));

        if (moreOptDialog != null) {
            moreOptDialog.cancel();
        }

    }

    private void deleteVideo(@NonNull VideosData data, int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true).setMessage("are you sure to delete video?")
                .setIcon(R.drawable.app_logo)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, data.getID());

                    File file = new File(data.getData());
                    boolean delete = file.delete();
                    if (delete) {
                        context.getContentResolver().delete(contentUri, null, null);
                        arrayList.remove(data);
                        notifyItemRemoved(index);

                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Can't Deleted", Toast.LENGTH_SHORT).show();
                    }
                    if(moreOptDialog!=null){
                        moreOptDialog.dismiss();
                    }

                }).setNegativeButton("No", null);

        builder.show();


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        Button moreBtn, shareBtn, delBtn;

        ImageView image;

        public viewHolder(View itemView) {
            super(itemView);
            moreBtn = itemView.findViewById(R.id.moreBtnSSIV);
            image = itemView.findViewById(R.id.imageviewSSIV);
            shareBtn = itemView.findViewById(R.id.shareBtnSSIV);
            delBtn = itemView.findViewById(R.id.deleteBtnSSIV);


        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}