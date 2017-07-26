package jp.ecweb.homes.a1601.network;

import android.content.Context;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 材料カテゴリ一覧取得クラス
 */
public class HttpRequestProductCategory extends HttpRequestBase {
    private static final String TAG = HttpRequestProductCategory.class.getSimpleName();

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestProductCategory(Context context) {
        super(context);
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(final HttpCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTCATEGORY;
        boolean resultParamCheck = super.get(url, new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError();
                    return;
                }
                // パース処理
                Category category = result.toProductCategory();
                if (category == null) {
                    listener.onError();
                    return;
                }
                listener.onSuccess(category);
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
