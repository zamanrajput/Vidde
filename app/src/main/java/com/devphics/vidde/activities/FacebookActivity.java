package com.devphics.vidde.activities;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.devphics.vidde.R;
import com.devphics.vidde.util.WebLayout;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

public class FacebookActivity extends AppCompatActivity {
    public String target_url = "http://m.facebook.com/";
    LinearLayout mLinearLayout;

    AgentWeb mAgentWeb;
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        mLinearLayout = findViewById(R.id.mLinearLayout);


            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .addJavascriptInterface("FBDownloader",this)
                    .setMainFrameErrorView(R.layout.activity_facebook, -1)
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                    .setWebLayout(new WebLayout(this))
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                    .interceptUnkownUrl()
                    .createAgentWeb()
                    .ready()
                    .go(target_url)
            ;

            WebLayout.mWebView.getSettings().setJavaScriptEnabled(true);
            WebLayout.mWebView.addJavascriptInterface(this, "FBDownloader");



    }

    private final com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("onPageFinished", url);
            WebView webView2 = WebLayout.mWebView;
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            StringBuffer stringBuffer3 = new StringBuffer();
            StringBuffer stringBuffer4 = new StringBuffer();
            StringBuffer stringBuffer5 = new StringBuffer();
            StringBuffer stringBuffer6 = new StringBuffer();
            StringBuffer stringBuffer7 = new StringBuffer();
            StringBuffer stringBuffer8 = new StringBuffer();
            StringBuffer stringBuffer9 = new StringBuffer();
            StringBuffer stringBuffer10 = new StringBuffer();
            StringBuffer stringBuffer11 = new StringBuffer();
            webView2.loadUrl(stringBuffer.append(stringBuffer2.append(stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(stringBuffer6.append(stringBuffer7.append(stringBuffer8.append(stringBuffer9.append(stringBuffer10.append(stringBuffer11.append("javascript:(function() { ").append("var el = document.querySelectorAll('div[data-sigil]');").toString()).append("for(var i=0;i<el.length; i++)").toString()).append("{").toString()).append("var sigil = el[i].dataset.sigil;").toString()).append("if(sigil.indexOf('inlineVideo') > -1){").toString()).append("delete el[i].dataset.sigil;").toString()).append("var jsonData = JSON.parse(el[i].dataset.store);").toString()).append("el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\");');").toString()).append("}").toString()).append("}").toString()).append("})()").toString());

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
           // Log.e("urlLoadResponse", url);
            WebView webView2 = WebLayout.mWebView;
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            StringBuffer stringBuffer3 = new StringBuffer();
            StringBuffer stringBuffer4 = new StringBuffer();
            StringBuffer stringBuffer5 = new StringBuffer();
            StringBuffer stringBuffer6 = new StringBuffer();
            StringBuffer stringBuffer7 = new StringBuffer();
            StringBuffer stringBuffer8 = new StringBuffer();
            StringBuffer stringBuffer9 = new StringBuffer();
            StringBuffer stringBuffer10 = new StringBuffer();
            StringBuffer stringBuffer11 = new StringBuffer();
            StringBuffer stringBuffer12 = new StringBuffer();
            webView2.loadUrl(stringBuffer.append(stringBuffer2.append(stringBuffer3.append(stringBuffer4.append(stringBuffer5.append(stringBuffer6.append(stringBuffer7.append(stringBuffer8.append(stringBuffer9.append(stringBuffer10.append(stringBuffer11.append(stringBuffer12.append("javascript:(function prepareVideo() { ").append("var el = document.querySelectorAll('div[data-sigil]');").toString()).append("for(var i=0;i<el.length; i++)").toString()).append("{").toString()).append("var sigil = el[i].dataset.sigil;").toString()).append("if(sigil.indexOf('inlineVideo') > -1){").toString()).append("delete el[i].dataset.sigil;").toString()).append("console.log(i);").toString()).append("var jsonData = JSON.parse(el[i].dataset.store);").toString()).append("el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');").toString()).append("}").toString()).append("}").toString()).append("})()").toString());
            WebView webView3 = WebLayout.mWebView;
            StringBuffer stringBuffer13 = new StringBuffer();
            webView3.loadUrl(stringBuffer13.append("javascript:( window.onload=prepareVideo;").append(")()").toString());

        }
    };

    private final com.just.agentweb.WebChromeClient mWebChromeClient = new com.just.agentweb.WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    @JavascriptInterface
    public void processVideo(final String vidData, final String vidID) {
        try {

            new AlertDialog.Builder(this)
                    .setTitle("VIDDE Says")
                    .setMessage("Are you sure you want to download this video?")

                    .setPositiveButton("Yes", (dialog, which) -> {
                        Uri downloadUri = Uri.parse(vidData);
                        DownloadManager.Request req = new DownloadManager.Request(downloadUri);
                        req.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                vidID + ".mp4"
                        );

                        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        DownloadManager dm = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);

                        dm.enqueue(req);
                        
                    })
                    .setNegativeButton("No", null)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }



}