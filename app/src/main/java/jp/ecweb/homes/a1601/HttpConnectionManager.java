package jp.ecweb.homes.a1601;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * HTTP通信クラス
 */
class HttpConnectionManager {

    private static final String TAG = HttpConnectionManager.class.getSimpleName();

    private Context mContext;

    /**
     * コンストラクタ
     * @param context   コンテキスト
     */
    HttpConnectionManager(Context context) {
        mContext = context;
    }

    /**
     * GETリクエスト送信
     * @param url           送信先URL
     * @param listener      通信完了リスナー
     * @return              パラメタチェック結果
     */
    boolean get(String url, HttpConnectionListener listener) {
        // パラメタチェック
        if (url == null || url.isEmpty()) {
            CustomLog.e(TAG, "url is null");
            return false;
        }
        if (listener == null) {
            CustomLog.e(TAG, "listener is null");
            return false;
        }
        sendRequest(Request.Method.GET, url, null, listener);
        return true;
    }

    /**
     * POSTリクエスト送信
     * @param url           送信先URL
     * @param request       リクエストデータ
     * @param listener      通信完了リスナー
     * @return              パラメタチェック結果
     */
    boolean post(String url, JSONObject request, HttpConnectionListener listener) {
        // パラメタチェック
        if (url == null || url.isEmpty()) {
            CustomLog.e(TAG, "url is null or empty");
            return false;
        }
        if (request == null) {
            CustomLog.e(TAG, "request is null");
            return false;
        }
        if (listener == null) {
            CustomLog.e(TAG, "listener is null");
            return false;
        }
        sendRequest(Request.Method.POST, url, request, listener);
        return true;
    }

    /**
     * リクエスト送信
     * @param action        送信アクション
     * @param url           送信先URL
     * @param request       リクエストデータ
     * @param listener      通信完了リスナー
     */
    private void sendRequest(int action, String url, final JSONObject request, final HttpConnectionListener listener) {
        // Volleyリクエストの作成
        if (Const.LOG_SENSITIVE) CustomLog.d(TAG, "WEB API URL=" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                action, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CustomLog.d(TAG, "Server connection success [response:" + response.toString() + "]");
                        HttpResult result = new HttpResult();
                        result.setSuccess(true);
                        result.setStatusCode(200);
                        result.setMessage("");
                        result.setResponse(response);
                        listener.onSuccess(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomLog.d(TAG, "Server connection failure");
                        HttpResult result = new HttpResult();
                        result.setSuccess(false);
                        result.setStatusCode(error.networkResponse != null ? error.networkResponse.statusCode : -1);
                        result.setMessage(error.getLocalizedMessage());
                        result.setResponse(null);
                        listener.onError(result);
                    }
                }
        );
        // リクエストの送信
        VolleyManager.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }
}
