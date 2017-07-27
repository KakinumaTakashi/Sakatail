package jp.ecweb.homes.a1601.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import jp.ecweb.homes.a1601.R;

/**
 * ユーティリティクラス
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * アプリケーションのバージョンを返却
     * @param context           コンテキスト
     * @return                  バージョン番号
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

    /**
     * Stringがnullなら空文字に変換
     * @param text              Stringオブジェクト
     * @return                  変換結果
     */
    public static String nullToEmpty(String text) {
        return text != null ? text : "";
    }

    /**
     * プログレスダイアログ表示
     * @param context           コンテキスト
     * @return                  プログレスダイアログインスタンス
     */
    public static ProgressDialog startProgress(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.Acquiring));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * プログレスダイアログを閉じる
     * @param progressDialog    プログレスダイアログインスタンス
     */
    public static void stopProgress(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
