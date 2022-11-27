package com.devphics.vidde.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devphics.vidde.ModelClasses.webdata;
import com.devphics.vidde.R;
import com.devphics.vidde.VideoContentSearch;
import com.devphics.vidde.facebookapi.FacebookExtractor;
import com.devphics.vidde.facebookapi.FacebookFile;
import com.devphics.vidde.fragments.downloadadapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WebActivity extends AppCompatActivity {

    WebView webview;
    String Downloadurl;
    FloatingActionButton floatingActionButton;
    String url;
    Bitmap websitebitmap;
    ImageView imageViewweb;
    List<String> mainurls;
    List<String> mainsize;
    List<String> mainname;
    List<String> maintype;

    List<Boolean> audiodata;
    String filename;
    ArrayList<webdata> arrayStudent = new ArrayList<webdata>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webview=findViewById(R.id.webview_for_downloading);
        imageViewweb=findViewById(R.id.imageview_we);

        floatingActionButton=findViewById(R.id.fabbtn);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new  IntentFilter("custom-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverfornam,
                new IntentFilter("custom-name"));
         url= getIntent().getStringExtra("txturl");
       String  aboutyes= getIntent().getStringExtra("txtnumber");
       if (aboutyes != null && aboutyes.equals("yes")){
           floatingActionButton.setVisibility(View.GONE);

       }else {
           floatingActionButton.setVisibility(View.VISIBLE);
       }

        mainurls= new ArrayList<String>();
        mainsize= new ArrayList<String>();
        mainname= new ArrayList<String>();
        maintype= new ArrayList<String>();
        audiodata= new ArrayList<Boolean>();
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setWebViewClient(new MyWebViewClient());


        webview.loadUrl(url);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                websitebitmap=icon;
            }
        });
       // final Handler handler = new Handler(Looper.getMainLooper());
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            public void run() {
                if (mainurls.size()>0){
               //     floatingActionButton.setBackgroundColor(R.color.colorAccent);
                 //   floatingActionButton.setBackgroundColor(Color.parseColor("#F44336"));
                   // floatingActionButton.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F44336")));
//                    floatingActionButton.backgroundT

                    // floatingActionButton.backgroundTintList = ColorStateList.valueOf(Color.blue())
//                    YoYo.with(Techniques.ZoomIn)
//                            .duration(70)
//                            .repeat(15)
//                            .playOn(findViewById(R.id.fabbtn));

                    Drawable buttonDrawable = floatingActionButton.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        DrawableCompat.setTint(buttonDrawable, getColor(R.color.accent_green));
                    }else{
                        DrawableCompat.setTint(buttonDrawable, Color.parseColor("#4caf50"));
                    }
                    floatingActionButton.setImageResource(R.drawable.ic_file_download_white);
                    floatingActionButton.setBackground(buttonDrawable);
                    Animation expandIn = AnimationUtils.loadAnimation(WebActivity.this.getApplicationContext(), R.anim.expand_in);
                    floatingActionButton.startAnimation(expandIn);

                }

                handler.postDelayed(this, delay);
            }
        }, delay);

        floatingActionButton.setOnClickListener(view -> {
            if (mainurls.size() > 0 && mainname.size() > 0) {
                mainname.size();

                final Dialog dialog = new BottomSheetDialog(WebActivity.this);
                dialog.setContentView(R.layout.download_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = dialog.findViewById(R.id.download_listview);
                ImageView thumbnail = dialog.findViewById(R.id.videoThumbnailDownloadDailog);
                EditText edtFileName = dialog.findViewById(R.id.editTextFileNameDownloadDialog);

                //String videoUrl = "https://fb.watch/bWWtT-gj_S/";


                thumbnail.setImageBitmap(websitebitmap);
                if (url.contains("fb.watch") || url.contains("facebook.com")) {
                    new FacebookExtractor(WebActivity.this, url, false) {
                        @Override
                        protected void onExtractionComplete(FacebookFile facebookFile) {
                            Log.e("TAG", "---------------------------------------");
                            Log.e("TAG", "facebookFile AutherName :: " + facebookFile.getAuthor());
                            Log.e("TAG", "facebookFile FileName :: " + facebookFile.getFilename());
                            Log.e("TAG", "facebookFile Ext :: " + facebookFile.getExt());
                            Log.e("TAG", "facebookFile SD :: " + facebookFile.getSdUrl());
                            Log.e("TAG", "facebookFile HD :: " + facebookFile.getHdUrl());
                            Log.e("TAG", "---------------------------------------");
                            edtFileName.setText(facebookFile.getFilename());

                        }

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void onExtractionFail(Exception Error) {
                            String fileName = url.substring(url.lastIndexOf('/') + 1);
                            edtFileName.setText(fileName);

                        }


                        @Override
                        protected void onExtractionFail(String Error) {
                            String fileName = url.substring(url.lastIndexOf('/') + 1);
                            edtFileName.setText(fileName);


                        }
                    };
                } else {
                    SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                    String fileNameifnot = url.substring(url.lastIndexOf('/') + 1);

                    String fileName = prefs.getString("nameurl", "videoname");//"No name defined" is the default value.
                    if (fileName.equals("videoname")) {
                        fileName = url.substring(url.lastIndexOf('/') + 1);
                    }
                    //int idName = prefs.getInt("idName", 0);
                    // String fileName = url.substring(url.lastIndexOf('/') + 1);
                    edtFileName.setText(fileName);
                }


                TextView btnDownload = dialog.findViewById(R.id.btndownloadcustomlistview);
                TextView btnCancle = dialog.findViewById(R.id.btncancelcustome);
                btnCancle.setOnClickListener(view1 -> dialog.dismiss());
                btnDownload.setOnClickListener(view12 -> {


                    DownloadManager manager;
                    manager = (DownloadManager) WebActivity.this.getSystemService(DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(Downloadurl);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, "Vidde " + edtFileName.getText().toString());
//                            request.setDestinationInExternalPublicDir(
//                                    Environment.DIRECTORY_DOWNLOADS,    //Download folder
//                                    URLUtil.guessFileName(Downloadurl, null, null));  //Name of file

                    DownloadManager downloadManager = (DownloadManager) WebActivity.this.getSystemService(DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    dialog.dismiss();
//                            if (audiocheckbox.isChecked()){
//                                DownloadManager.Request request3 = new DownloadManager.Request(Uri.parse(Downloadurl));
//                                request3.setDescription("Downloading");
//                                request3.setMimeType("audio/MP3");
//                                request3.setTitle("Audio File");
//                                request3.allowScanningByMediaScanner();
//                                request3.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                                request3.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, etfilename.getText().toString());
//                                DownloadManager managerf = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                                managerf.enqueue(request3);
//
//
//                            }
                });

                String fileName =edtFileName.getText().toString();


                // LinearLayout liner = dialog.findViewById(R.id.dialog_linear_layout);
                // downloadadapter<String> arrayAdapter = new downloadadapter<String>(this, R.layout.download_listview, mainname,mainsize,mainurls);
                downloadadapter customAdapter = new downloadadapter(getApplicationContext(), mainurls, mainsize, mainname, maintype, audiodata, LayoutInflater.from(WebActivity.this));


                listView.setAdapter(customAdapter);
//
                dialog.show();
            }
//                else {
//                    Toast.makeText(WebActivity.this, "Size is less of list", Toast.LENGTH_SHORT).show();
//                    mainname.add("aas");
//                    mainsize.add("12344545");
//                    mainurls.add("8798456");
//                }





//                mainname.add("klh;klhjm");
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("myList", (ArrayList<String>) mainname);
//
//                Fragment fragment = new DownloadVideo_fragment();
//                fragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.webfram, new DownloadVideo_fragment()).commit();
//                webview.setVisibility(View.GONE);
//                if (arrayStudent.size()>0){
//                    arrayStudent.get(1);
//                    webdata listdata = arrayStudent.get(0);
//                    String linkdata=listdata.getLink();
//
//                    Toast.makeText(WebActivity.this, "arraylist"+linkdata, Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(WebActivity.this, "array list is empty", Toast.LENGTH_SHORT).show();
//                }

           // arrayStudent.add()
        });

        updateFoundVideosBar();
        
        
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(WebActivity.this, "check its working or not "+mainlink, Toast.LENGTH_SHORT).show();
//                for (int i=0;i<arrayStudent.size();){
//                    webdata data=arrayStudent.get(i);
//                    Toast.makeText(WebActivity.this, "arraydata"+data, Toast.LENGTH_SHORT).show();
//                    DownloadManager manager;
//                    manager = (DownloadManager) getSystemService(WebActivity.this.DOWNLOAD_SERVICE);
//                    Uri uri = Uri.parse(data);
//                    DownloadManager.Request request = new DownloadManager.Request(uri);
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                    request.setDestinationInExternalPublicDir(
//                            Environment.DIRECTORY_DOWNLOADS,    //Download folder
//                            URLUtil.guessFileName(data, null, null));  //Name of file
//
//                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                    manager.enqueue(request);
//                    i++;
//                }
//            }
//        }, 10000);
//        int size=urls.size();
//        if (size>0){
//            String url=urls.get(0).toString();
//            int i=0;
//            if (i<size){
//                i++;
//                Toast.makeText(this, "chek now "+urls.get(i), Toast.LENGTH_SHORT).show();
//                DownloadManager manager;
//                manager = (DownloadManager) getSystemService(MainActivity.this.DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse("https://www.youtube.com/watch?v=tp4j3ynUnVg");
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                manager.enqueue(request);
//
//
//            }
//
//
//        }

    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
           // String ItemName = intent.getStringExtra("item");
             Downloadurl = intent.getStringExtra("quantity");
            //Toast.makeText(WebActivity.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();
        }
    };
    public BroadcastReceiver mMessageReceiverfornam = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            // String ItemName = intent.getStringExtra("item");
            filename = intent.getStringExtra("mainname");
            //Toast.makeText(WebActivity.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();
        }
    };

    private void updateFoundVideosBar() {
//        if (mainname.size() > 0) {
//            WebActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
//                    Animation expandIn = AnimationUtils.loadAnimation(WebActivity.this.getApplicationContext(), R.anim.expand_in);
//                    floatingActionButton.startAnimation(expandIn);
//                }
//            });
//
//        } else {
////            WebActivity.this.runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
////                    if (foundVideosWindow.getVisibility() == View.VISIBLE)
////                        foundVideosWindow.setVisibility(View.GONE);
////                }
////            });
//        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("youtube") && !url.contains("-youtube")) {
                Toast.makeText(WebActivity.this, "This is youtube url", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                String newUrl = checkUrl(url);
                if (Patterns.WEB_URL.matcher(newUrl).matches()) {
                    //Toast.makeText(MainActivity.this, "This is url : " + newUrl, Toast.LENGTH_SHORT).show();
                    view.loadUrl(newUrl);
                } else {
                    //  Toast.makeText(MainActivity.this, "This is url : " + url, Toast.LENGTH_SHORT).show();
                    view.loadUrl(String.format("http://google.com/search?tbm=vid&q=%s -youtube -site:youtube.com", new Object[]{url}));
                }
                return false;
            }
        }
        @Override
        public void onLoadResource(final WebView view, final String url) {
            Log.d("fb :", "URL: " + url);
            final String viewUrl = view.getUrl();
            final String title = view.getTitle();

            new VideoContentSearch(WebActivity.this, url, viewUrl, title) {
                @Override
                public void onStartInspectingURL() {
                    //Utils.disableSSLCertificateChecking();
                }

                @Override
                public void onFinishedInspectingURL(boolean finishedAll) {
                    // HttpsURLConnection.setDefaultSSLSocketFactory(defaultSSLSF);
                }

                @Override
                public void onVideoFound(String size, String type, String link, String name, String page, boolean chunked, String website, boolean audio) {
                    //videoList.addItem(size, type, link, name, page, chunked, website, audio);
                    // updateFoundVideosBar();
                    //urls.add(link,type);
                    //Toast.makeText(MainActivity.this, "This is mayo : " + link, Toast.LENGTH_SHORT).show();
                   // webdata talk=new webdata(size, type, link, name, page, chunked, website, audio);
                   // arrayStudent.add(talk);
                    //mainlink=link;

                    mainurls.add(link);
                    mainname.add(name);
                    mainsize.add(size);
                    maintype.add(type);
                    audiodata.add(audio);



                }
            }.start();


//            Log.d("fb :", "URL: " + url);
//            final String requestUrl = view.getUrl();
//            final String title = view.getTitle();
//           // String requestUrl = request.getUrl.toString;
//
//            //Get the mime-type from the string
//
//            String extension = MimeTypeMap.getFileExtensionFromUrl(requestUrl);
//
//            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//
//            //make sure the mime-type isn't null
//            if(mimeType != null){
//
//                // check if any of the requestUrls contain the url of a video file
//
//                if(mimeType.startsWith("video/") && requestUrl.contains("youtube.com")){
//
//                    Log.e("Video File" , requestUrl);
//                    geturl=requestUrl;
//                    urls.add(requestUrl);
//                   // Toast.makeText(MainActivity.this, "requesturl"+requestUrl, Toast.LENGTH_SHORT).show();
//
//                }
            //  }
        }

        public String checkUrl(String str) {
            if (str == null) {
                return str;
            }
            StringBuilder stringBuilder;
            if (Build.VERSION.SDK_INT < 28) {
                if (!str.startsWith("http")) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("http://");
                    stringBuilder.append(str);
                    str = stringBuilder.toString();
                }
                return str;

            } else if (str.startsWith("https")) {
                return str;
            } else {
                if (str.startsWith("http")) {
                    return str.replaceFirst("http", "https");
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("https://");
                stringBuilder.append(str);
                return stringBuilder.toString();
            }
        }
    }
    
}