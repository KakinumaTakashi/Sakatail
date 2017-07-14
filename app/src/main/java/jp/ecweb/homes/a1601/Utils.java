package jp.ecweb.homes.a1601;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * ユーティリティクラス
 */
class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * アプリケーションのバージョンを返却
     * @param context   コンテキスト
     * @return          バージョン番号
     */
    static String getAppVersion(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getAppVersion failure", e);
        }
        return versionName;
    }
}
