package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.RakutenResponse;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 楽天商品検索API 商品情報取得クラス
 */
public class HttpRequestRakuten extends HttpRequestBase {
    private static final String TAG = HttpRequestRakuten.class.getSimpleName();

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestRakuten(Context context) {
        super(context);
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(String itemCode, final HttpRakutenListener listener) {
        String url = String.format(mContext.getString(R.string.rakuten_URL) +
                "?applicationId=%s&affiliateId=%s&formatVersion=2&itemCode=%s",
                mContext.getString(R.string.rakuten_applicationId),
                mContext.getString(R.string.rakuten_affiliateId),
                itemCode);
        boolean resultParamCheck = super.get(url, new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // データ部処理
                try {
                    int count = result.getResponse().getInt("count");
                    if (count > 0) {
                        listener.onSuccess(new RakutenResponse(result.getResponse()));
                    } else {
                        listener.onError(C.RSP_CD_ITEMNOTFOUND);
                    }
                } catch (JSONException e) {
                    listener.onError(C.RSP_CD_PARSINGFAILED);
                }
            }
            @Override
            public void onError(HttpResponse result) {
                CustomLog.e(TAG, "HTTP connection error "
                        + "[statusCode:" + result.getStatusCode() + " , message:" + result.getMessage() + "]");
                listener.onError(C.RSP_CD_HTTPCONNECTIONERROR);
            }
        });
        if (!resultParamCheck) {
            listener.onError(C.RSP_CD_PARAMERROR);
        }
    }
}
