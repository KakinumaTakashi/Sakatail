package jp.ecweb.homes.a1601;

import android.util.Log;

/**
 * カスタムログクラス
 */
public class CustomLog {
    private static final String TAG = "Sakatail";
    private CustomLog(){}

    static void i(String tag, String msg) {
        if (Const.LOG_INFO) Log.i(TAG, createMsg(tag, msg));
    }

    static void v(String tag, String msg) {
        if (Const.LOG_VERBOSE) Log.v(TAG, createMsg(tag, msg));
    }

    static void d(String tag, String msg) {
        if (Const.LOG_DEBUG) Log.d(TAG, createMsg(tag, msg));
    }

    static void e(String tag, String msg) {
        if (Const.LOG_ERROR) Log.e(TAG, createMsg(tag, msg));
    }

    static void e(String tag, String msg, Throwable tr) {
        if (Const.LOG_ERROR) Log.e(TAG, createMsg(tag, msg), tr);
    }

    static void w(String tag, String msg) {
        if (Const.LOG_WARNING) Log.w(TAG, createMsg(tag, msg));
    }

    static void w(String tag, String msg, Throwable tr) {
        if (Const.LOG_WARNING) Log.w(TAG, createMsg(tag, msg), tr);
    }

    private static String createMsg(String tag, String msg) {
        return "[" + Const.LIB_VERSION + "] " + tag + "> " +msg;
    }
}
