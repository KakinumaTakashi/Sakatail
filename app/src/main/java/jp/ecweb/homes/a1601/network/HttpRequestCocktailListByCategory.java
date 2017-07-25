package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * カテゴリ別カクテル一覧取得クラス
 */
public class HttpRequestCocktailListByCategory extends HttpRequestBase {
    private static final String TAG = HttpRequestCocktailListByCategory.class.getSimpleName();

    private Category mCategory;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestCocktailListByCategory(Context context) {
        super(context);
    }

    /**
     * カテゴリ設定
     * @param category      カテゴリ情報
     */
    public void setCategory(Category category) {
        mCategory = category;
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(final HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_COCKTAILLIST;
        boolean resultParamCheck = super.post(url, createRequest(), new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError();
                    return;
                }
                // パース処理
                List<Cocktail> cocktailList = result.toCocktailList();
                if (cocktailList == null) {
                    listener.onError();
                    return;
                }
                listener.onSuccess(cocktailList);
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

    /**
     * リクエスト生成
     * @return              リクエスト
     */
    private JSONObject createRequest() {
        JSONObject postData = new JSONObject();
        try {
            postData.put(C.REQ_KEY_CATEGORY1, mCategory.getCategory1());
            postData.put(C.REQ_KEY_CATEGORY2, mCategory.getCategory2());
        } catch (JSONException e) {
            CustomLog.e(TAG, "Create request failed.", e);
            return null;
        }
        return postData;
    }
}
