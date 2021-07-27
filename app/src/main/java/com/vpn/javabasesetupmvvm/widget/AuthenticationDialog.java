package com.vpn.javabasesetupmvvm.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vpn.javabasesetupmvvm.R;
import com.vpn.javabasesetupmvvm.callbacks.AuthenticationListener;

import timber.log.Timber;

/**
 * Created by SAM on 20/05/21.
 */
public class AuthenticationDialog extends Dialog {
    private Context context;
    private String request_url;
    private String redirect_url;
    private AuthenticationListener listener;


    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.redirect_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&display=touch&scope=user_profile,user_media";
    }


    public AuthenticationDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    public AuthenticationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    protected AuthenticationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private String access_token = "";

    private void initializeWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                if (url.contains("code=")) {
                    access_token = url.substring(url.lastIndexOf("=") + 1);
                }
                AuthenticationDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.toString().contains("code=") || url.toString().contains("code%3D")) {
//                Uri uri = Uri.EMPTY.parse(url);
//                String access_token = uri.getEncodedFragment();
//                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                Timber.tag("access_token").e(access_token);

                if (access_token != null && !access_token.isEmpty())
                    listener.onTokenReceived(access_token);
                dismiss();
            } else if (url.contains("?error")) {
                Timber.tag("access_token").e("getting error fetching access token");
                dismiss();
            }
        }
    };

}
