package com.devphics.vidde.twitter;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.devphics.vidde.R;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Formatter;
import java.util.Objects;

public class TwitterVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;
    public static long[] DownloadIDs = new long[100];
     static int index=0;
    public TwitterVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    @Override
    public String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "My Video Downloader");

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            boolean success1 = true;
            subFolder = new File(folder.getPath() + File.separator + "Twitter Videos");
            if (!subFolder.exists()) {
                success1 = subFolder.mkdirs();
            }
        }

        return Objects.requireNonNull(subFolder).getPath();
    }

    @Override
    public String getVideoId(String link) {
        if (link.contains("?")) {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1, link.indexOf("?"));
        } else {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/") + 1);
        }
        return link;
    }

    @Override
    public void DownloadVideo() {
        AndroidNetworking.post("https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php")
                .addBodyParameter("id", getVideoId(VideoURL))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("Range")
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Hello", response.toString());
                        String URL = response.toString();
                        if (URL.contains("url")) {
                            URL = URL.substring(URL.indexOf("url"));
                            URL = URL.substring(ordinalIndexOf(URL, "\"", 1) + 1, ordinalIndexOf(URL, "\"", 2));
                            if (URL.contains("\\")) {
                                URL = URL.replace("\\", "");
                            }
                            //Log.e("HelloURL",URL);
                            if (URLUtil.isValidUrl(URL)) {
//                                String path = Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS));
                                if (VideoTitle == null || VideoTitle.equals("")) {
                                    VideoTitle = "Twitter Video" + new Date().getDay()+"-"+new Date().getMonth()+"-"+new Date().getYear() + ".mp4";
                                } else {
                                    VideoTitle = VideoTitle + ".mp4";
                                }
                                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), VideoTitle);
                                try {
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL));
                                    request.allowScanningByMediaScanner();
                                    request.setDescription("Downloading")
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setDestinationUri(Uri.fromFile(newFile))
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            .setVisibleInDownloadsUi(true)
                                            .setTitle(VideoTitle);

                                    DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                                    long downLoadID = manager.enqueue(request);
                                    DownloadIDs[index]= downLoadID;
                                    index++;
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.dialog_custom_layout);
                                    dialog.show();
                                    ProgressBar mProgressBar = dialog.findViewById(R.id.progressBar1);
                                    TextView percentage = dialog.findViewById(R.id.percentage);
                                    TextView title = dialog.findViewById(R.id.getFileNameId);
                                    Button btndone = dialog.findViewById(R.id.btDismissCustomDialog);
                                    btndone.setOnClickListener(v -> dialog.dismiss());


                                    new Thread(() -> {

                                        boolean downloading = true;

                                        while (downloading) {

                                            DownloadManager.Query q = new DownloadManager.Query();
                                            q.setFilterById(downLoadID);

                                            Cursor cursor = manager.query(q);
                                            cursor.moveToFirst();
                                            @SuppressLint("Range") final int bytes_downloaded = cursor.getInt(cursor
                                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                            final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                                downloading = false;
                                            }

                                            //   final double dl_progress = (bytes_downloaded / bytes_total) * 100;

                                            final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);


                                            new Handler(Looper.getMainLooper()).post(() -> {
                                                mProgressBar.setProgress(dl_progress);
                                                Formatter f1=new Formatter(),f2 = new Formatter();
                                                String c = String.valueOf(f1.format("%3.2f",(bytes_downloaded / 1e6) ));
                                                String t = String.valueOf(f2.format("%3.2f",(bytes_total/ 1e6) ));
                                                percentage.setText(c+ "mb/" +t+"mb");
                                                if(bytes_downloaded==bytes_total){
                                                    title.setText("File Successfully Downloaded!");
                                                    title.setTextSize(18);
                                                }
                                                // code goes here
                                            });

//                                                runOnUiThread(new Runnable() {
//
//                                                    @Override
//                                                    public void run() {
//
//                                                        mProgressBar.setProgress((int) dl_progress);
//
//                                                    }
//                                                });

//                                                Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                                            cursor.close();
                                        }

                                    }).start();


                                } catch (Exception e) {
                                    if (Looper.myLooper() == null)
                                        Looper.prepare();
                                    Toast.makeText(context, "Error:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                if (Looper.myLooper() == null)
                                    Looper.prepare();
                                Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } else {
                            if (Looper.myLooper() == null)
                                Looper.prepare();
                            Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (Looper.myLooper() == null)
                            Looper.prepare();
                        Toast.makeText(context, "Error"+anError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
}
