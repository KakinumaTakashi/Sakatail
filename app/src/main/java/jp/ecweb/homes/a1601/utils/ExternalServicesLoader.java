package jp.ecweb.homes.a1601.utils;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;

/**
 * 外部サービス表示クラス
 */
public class ExternalServicesLoader {

    /**
     * Firebase AdMobのロード
     * @param view  AdMobを表示するView
     */
    public static void loadAdMob(View view) {
        Context context = view.getContext();
        // 広告を表示
        MobileAds.initialize(context, context.getString(R.string.banner_ad_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) view).loadAd(adRequest);
    }

    /**
     * 楽天ウェブサービスクレジット表示
     * @param view  クレジットを表示するWebView
     */
    public static void loadRakutenCredit(View view) {
        ((WebView) view).loadUrl(C.RAKUTEN_CREDIT_CONTENTS);
    }
}
