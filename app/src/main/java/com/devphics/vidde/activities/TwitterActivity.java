
package com.devphics.vidde.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.devphics.vidde.databinding.TwitterActivityBinding;
import com.devphics.vidde.twitter.VideoDownloader;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Formatter;


public class TwitterActivity extends AppCompatActivity implements VideoDownloader {
    Context context;
    private String VideoURL;
    private String VideoTitle;
    public static long[] DownloadIDs = new long[100];
    static int index = 0;

    TwitterActivityBinding binding;

    private final int REQ_COD = 111;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TwitterActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getApplicationContext();


        binding.btnDownloadTF.setOnClickListener(view -> {
            String url = URLUtil.guessUrl(binding.editTxtTF.getText().toString());
            if (url.contains("twitter")) {
                VideoURL = url;

                DownloadVideo();

            } else {
                Toast.makeText(getApplicationContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
            }
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            TwitterActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions Not Granted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


    }

    private boolean CheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int ReadCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int WriteCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return ReadCheck == PackageManager.PERMISSION_GRANTED && WriteCheck == PackageManager.PERMISSION_GRANTED;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_COD) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Permissions Not Granted", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void RequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(TwitterActivity.this, permissions, REQ_COD);
            }
        }
    }


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
        return subFolder.getPath();
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
                        binding.downloadingLayout.setVisibility(View.VISIBLE);
                        if (URL.contains("url")) {
                            URL = URL.substring(URL.indexOf("url"));
                            URL = URL.substring(ordinalIndexOf(URL, "\"", 1) + 1, ordinalIndexOf(URL, "\"", 2));
                            if (URL.contains("\\")) {
                                URL = URL.replace("\\", "");
                            }
                            //Log.e("HelloURL",URL);
                            if (URLUtil.isValidUrl(URL)) {
//                                String path = createDirectory();
                                if (VideoTitle == null || VideoTitle.equals("")) {
                                    VideoTitle = "Twitter Video" + new Date().getDay() + "-" + new Date().getMonth() + "-" + new Date().getYear() + ".mp4";
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
                                    DownloadIDs[index] = downLoadID;
                                    index++;


                                    ProgressBar mProgressBar = binding.progressBarTF;
                                    TextView percentage = binding.percentageTA;
                                    TextView title = binding.fileDownloadingTxtTF;

                                    new Thread(() -> {

                                        boolean downloading = true;

                                        while (downloading) {

                                            DownloadManager.Query q = new DownloadManager.Query();
                                            q.setFilterById(downLoadID);

                                            Cursor cursor = manager.query(q);
                                            cursor.moveToFirst();
                                            @SuppressLint("Range") final int bytes_downloaded = cursor.getInt(cursor
                                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                            @SuppressLint("Range") final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                                downloading = false;
                                            }

                                            //   final double dl_progress = (bytes_downloaded / bytes_total) * 100;

                                            final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);


                                            new Handler(Looper.getMainLooper()).post(() -> {
                                                mProgressBar.setProgress(dl_progress);
                                                Formatter f1 = new Formatter(), f2 = new Formatter();
                                                String c = String.valueOf(f1.format("%3.2f", (bytes_downloaded / 1e6)));
                                                String t = String.valueOf(f2.format("%3.2f", (bytes_total / 1e6)));
                                                percentage.setText(c + "mb/" + t + "mb");
                                                if (bytes_downloaded == bytes_total) {
                                                    binding.downloadingLayout.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                                                    title.setTextSize(18);
                                                }
                                                // code goes here
                                            });

                                            cursor.close();
                                        }

                                    }).start();


                                } catch (Exception e) {
                                    if (Looper.myLooper() == null)
                                        Looper.prepare();
                                    Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Invalid Video URL", Toast.LENGTH_SHORT).show();
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