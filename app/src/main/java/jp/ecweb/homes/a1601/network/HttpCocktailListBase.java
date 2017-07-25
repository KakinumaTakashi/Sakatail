package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.models.Favorite;

/**
 * カクテル一覧取得ベースクラス
 */
// TODO 削除予定
abstract class HttpCocktailListBase {

    private static final String TAG = HttpCocktailListBase.class.getSimpleName();

    Context mContext;
    Category mCategory;
    List<Favorite> mFavoriteList;
    List<HavingProduct> mProductList;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    HttpCocktailListBase(Context context) {
        mContext = context;
    }

    /**
     * カテゴリ設定
     * @param category      カテゴリ情報
     */
    public void setCategory(Category category) {
        mCategory = category;
    }

    /**
     * お気に入り設定
     * @param favoriteList  お気に入り情報
     */
    public void setFavoriteList(List<Favorite> favoriteList) {
        mFavoriteList = favoriteList;
    }

    /**
     * 材料設定
     * @param productList   材料情報
     */
    public void setProductList(List<HavingProduct> productList) {
        mProductList = productList;
    }

    /**
     * POSTリクエスト送信
     * @param url           送信先URL
     * @param request       リクエストデータ
     * @param listener      通信完了リスナー
     */
    void post(String url, final JSONObject request, final HttpCocktailListListener listener) {
        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.post(url, request, new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                JSONObject response = result.getResponse();
                List<Cocktail> cocktailList = new ArrayList<>();
                try {
                    // ヘッダ部処理
                    String status = response.getString(C.RSP_KEY_STATUS);
                    if (status.equals(C.RSP_STATUS_NG)) {
                        // サーバーにてエラーが発生した場合はエラーをスロー
                        throw new JSONException(response.getString(C.RSP_KEY_MESSAGE));
                    }
                    // データ部処理
                    JSONArray data = response.getJSONArray(C.RSP_KEY_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonCocktailObject = data.getJSONObject(i);
                        Cocktail cocktail = new Cocktail(jsonCocktailObject);
                        cocktailList.add(cocktail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cocktailList = null;
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
        if (!result) {
            listener.onError();
        }
    }
}
