package com.devphics.vidde.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devphics.vidde.R;
import com.just.agentweb.IWebLayout;

public class WebLayout implements IWebLayout {

    private Activity mActivity;
    private final LinearLayout mTwinklingRefreshLayout;
    public static WebView mWebView = null;

    public WebLayout(Activity activity) {
        this.mActivity = activity;
        mTwinklingRefreshLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_twk_web, null);
        mWebView = (WebView) mTwinklingRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mTwinklingRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }



}