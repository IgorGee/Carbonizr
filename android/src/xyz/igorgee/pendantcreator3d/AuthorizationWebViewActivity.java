package xyz.igorgee.pendantcreator3d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.igorgee.pendantcreatorg3dx.R;

public class AuthorizationWebViewActivity extends AppCompatActivity {

    String url;
    Intent intent;

    @Bind(R.id.webView) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_web_view);
        ButterKnife.bind(this);

        intent = getIntent();
        url = intent.getStringExtra(LogInFragment.EXTRA_URL);

        //TODO Ask Shapeways for a mobile friendly authorization page.
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}
