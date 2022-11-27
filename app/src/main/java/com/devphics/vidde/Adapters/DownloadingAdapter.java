package com.devphics.vidde.Adapters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devphics.vidde.ModelClasses.DownloadModel;
import com.devphics.vidde.ModelClasses.VideosData;
import com.devphics.vidde.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.viewHolder> {
    Context context;
    ArrayList<DownloadModel> arrayList;

    public DownloadingAdapter(Context context, ArrayList<DownloadModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.downloading_now_item, viewGroup, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        DownloadModel vIdeosData = arrayList.get(position);


        holder.title.setText(vIdeosData.getTitle());
        holder.tvSize.setText(getFileSize(vIdeosData.getTotalSize()));
        holder.tvCompleted.setText(getFileSize(vIdeosData.getProgress()));
        holder.bar.setMax(vIdeosData.getTotalSize());
        holder.bar.setProgress(vIdeosData.getProgress());
        if(vIdeosData.getProgress()==vIdeosData.getTotalSize()){
            arrayList.remove(position);
            notifyDataSetChanged();

            if(arrayList.size()==0){
                holder.headingDownloading.setVisibility(View.GONE);
            }
        }

        holder.button.setOnClickListener(v -> {
            if (vIdeosData.isPaused()) {

                vIdeosData.setPaused(false);
                holder.button.setForeground(context.getDrawable(R.drawable.play_arrow));
                holder.button.setBackground(context.getDrawable(R.drawable.play_arrow));
                if (resumeDownload(vIdeosData)) {
                    Toast.makeText(context, "Field to Resume!", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            } else {

                vIdeosData.setPaused(true);
                holder.button.setForeground(context.getDrawable(R.drawable.pause));
                holder.button.setBackground(context.getDrawable(R.drawable.pause));
                if (!pauseDownload(vIdeosData)) {
                    Toast.makeText(context, "Field to Pause!", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            }
        });

        if (vIdeosData.isPaused()) {
            holder.button.setForeground(context.getDrawable(R.drawable.play_arrow));
            holder.button.setBackground(context.getDrawable(R.drawable.play_arrow));
        } else {
            holder.button.setForeground(context.getDrawable(R.drawable.pause));
            holder.button.setBackground(context.getDrawable(R.drawable.pause));
        }


//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ViewingVideoActivity.class);
//            intent.putExtra("NEW_VIDEODATA", vIdeosData.getData());
//            intent.putExtra("type", "Video_Adapter");
//            context.startActivity(intent);
//        });

//        RequestOptions requestOptions = new RequestOptions();
//        Glide.with(context)
//                .load(vIdeosData.getData())
//                .apply(requestOptions)
//                .thumbnail(Glide.with(context).load(vIdeosData.getData()))
//                .into(holder.image);
    }

    public void refreshDownloaded(DownloadModel model, ArrayList<VideosData> list , VideosAdapter adapter){

        VideosData m = new VideosData(model.getTitle(),model.getMimeType(),model.getTotalSize(),"",model.getData(),model.getData(),model.getId(),0);
        list.add(m);

        adapter.notifyDataSetChanged();
    }

    private boolean resumeDownload(DownloadModel model) {
        int updatedRow = 0;
        ContentValues values = new ContentValues();
        values.put("control", 1);
        try {
            updatedRow = context.getContentResolver().update(Uri.parse(model.getData()), values, "title=?", new String[]{model.getTitle()});
            ;

        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return 0 < updatedRow;
    }

    private boolean pauseDownload(DownloadModel model) {
        int updatedRow = 0;
        ContentValues values = new ContentValues();
        values.put("control", 0);
        try {
            updatedRow = context.getContentResolver().update(Uri.parse(model.getData()), values, "title=?", new String[]{model.getTitle()});;

        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return 0 < updatedRow;
    }

    public void changeItemById(long id) {
        int i = 0;
        for (DownloadModel model : arrayList) {
            if (id == model.getId()) {
                notifyItemChanged(i);

            }
            i++;
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView title, tvSize, tvCompleted;
        ProgressBar bar;TextView headingDownloading;
        Button button;
        ImageView image;

        public viewHolder(View itemView) {
            super(itemView);
            headingDownloading = itemView.findViewById(R.id.downloadingHeading);
            button = itemView.findViewById(R.id.pause_resume);
            bar = itemView.findViewById(R.id.downloading_progress);
            image = itemView.findViewById(R.id.downloading_thumb);
            title = itemView.findViewById(R.id.downloading_title);
            tvSize = itemView.findViewById(R.id.downloading_total_size);
            tvCompleted = itemView.findViewById(R.id.downloading_downloaded_size);
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