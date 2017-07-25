package jp.ecweb.homes.a1601.network;

import android.content.Context;

import jp.ecweb.homes.a1601.models.Category;
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
    public void get(final HttpCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_CATEGORY;
        boolean result = super.get(url, new HttpRequestListener() {
                    @Override
                    public void onSuccess(HttpResponse result) {
                        // ヘッダーチェック
                        if (!result.checkResponseHeader()) {
                            listener.onError();
                            return;
                        }
                        // パース処理
                        Category category = result.toCategory();
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
                }
        );
        if (!result) {
            listener.onError();
        }
    }
}
