package jp.ecweb.homes.a1601.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.managers.VolleyManager;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * HTTPリクエスト ベースクラス
 */
public class HttpRequestBase {

    private static final String TAG = HttpRequestBase.class.getSimpleName();

    protected Context mContext;

    HttpRequestBase(Context context) {
        mContext = context;
    }

    /**
     * GETリクエスト送信
     * @param url           送信先URL
     * @param listener      通信完了リスナー
     * @return              パラメタチェック結果
     */
    boolean get(String url, HttpRequestListener listener) {
        // パラメタチェック
        if (url == null || url.isEmpty()) {
            CustomLog.e(TAG, "url is null or empty");
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
    boolean post(String url, JSONObject request, HttpRequestListener listener) {
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
    private void sendRequest(int action, String url, final JSONObject request, final HttpRequestListener listener) {
        // Volleyリクエストの作成
        if (C.DEBUG_MODE) CustomLog.d(TAG, "WEB API URL=" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                action, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CustomLog.d(TAG, "Server connection success [response:" + response.toString() + "]");
                        HttpResponse result = new HttpResponse();
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
                        HttpResponse result = new HttpResponse();
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
