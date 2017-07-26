package jp.ecweb.homes.a1601.network;

import android.content.Context;

import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;

/**
 * カクテル情報取得クラス
 */
public class HttpRequestCocktail extends HttpRequestBase {
    private static final String TAG = HttpRequestCocktail.class.getSimpleName();

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestCocktail(Context context) {
        super(context);
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(String cocktailId, final HttpCocktailListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_COCKTAIL + "?id=" + cocktailId;
        boolean resultParamCheck = super.get(url, new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError();
                    return;
                }
                // パース処理
                Cocktail cocktail = result.toCocktail();
                if (cocktail == null) {
                    listener.onError();
                    return;
                }
                listener.onSuccess(cocktail);
            }
            @Override
            public void onError(HttpResponse result) {
                CustomLog.e(TAG, "HTTP connection error "
                        + "[statusCode:" + result.getStatusCode() + " , message:" + result.getMessage() + "]");
                listener.onError();
            }
        });
        if (!resultParamCheck) {
            listener.onError();
        }
    }
}
