package jp.ecweb.homes.a1601.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * ユーティリティクラス
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * アプリケーションのバージョンを返却
     * @param context   コンテキスト
     * @return          バージョン番号
     */
    public static String getAppVersion(Context context) {
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

    public static String nullToEmpty(String text) {
        return text != null ? text : "";
    }
}
