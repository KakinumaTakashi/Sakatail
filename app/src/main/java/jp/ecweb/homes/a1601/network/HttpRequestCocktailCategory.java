package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.models.CocktailCategory;
import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;

/**
 * カテゴリ一覧取得クラス
 */
public class HttpRequestCocktailCategory extends HttpRequestBase {
    private static final String TAG = HttpRequestCocktailCategory.class.getSimpleName();

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestCocktailCategory(Context context) {
        super(context);
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(final HttpCocktailCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_CATEGORY;
        boolean result = super.get(url, new HttpRequestListener() {
                    @Override
                    public void onSuccess(HttpResponse result) {
                        // ヘッダーチェック
                        if (!result.checkResponseHeader()) {
                            listener.onError(C.RSP_CD_HEADERCHECKERROR);
                            return;
                        }
                        // データ部処理
                        try {
                            JSONObject data = result.getResponse().getJSONObject(C.RSP_KEY_DATA);
                            listener.onSuccess(new CocktailCategory(data));
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
                }
        );
        if (!result) {
            listener.onError(C.RSP_CD_PARAMERROR);
        }
    }
}
