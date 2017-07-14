package jp.ecweb.homes.a1601;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * 外部サービス表示クラス
 */
class ExternalServicesLoader {

    /**
     * Firebase AdMobのロード
     * @param view  AdMobを表示するView
     */
    static void loadAdMob(View view) {
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
    static void loadRakutenCredit(View view) {
        ((WebView) view).loadUrl(Const.RANKUTEN_CREDIT_CONTENTS);
    }
}
