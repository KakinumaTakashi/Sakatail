package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.model.Category;
import jp.ecweb.homes.a1601.model.Cocktail;
import jp.ecweb.homes.a1601.model.Favorite;

/**
 * カクテル一覧取得ベースクラス
 */
abstract class HttpCocktailListBase {

    private static final String TAG = HttpCocktailListBase.class.getSimpleName();

    Context mContext;
    Category mCategory;
    List<Favorite> mFavoriteList;

    HttpCocktailListBase(Context context) {
        mContext = context;
    }

    void setCategory(Category category) {
        mCategory = category;
    }

    void setFavoriteList(List<Favorite> favoriteList) {
        mFavoriteList = favoriteList;
    }

    abstract JSONObject createRequest();

    void post(String url, final JSONObject request, final HttpCocktailListListener listener) {
        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.post(url, request, new HttpConnectionListener() {
            @Override
            public void onSuccess(HttpResult result) {
                JSONObject response = result.getResponse();
                List<Cocktail> cocktailList = new ArrayList<>();
                try {
                    // ヘッダ部処理
                    String status = response.getString(Const.RES_KEY_STATUS);
                    if (status.equals(Const.RES_STATUS_NG)) {
                        // サーバーにてエラーが発生した場合はエラーをスロー
                        throw new JSONException(response.getString(Const.RES_KEY_MESSAGE));
                    }
                    // データ部処理
                    JSONArray data = response.getJSONArray(Const.RES_KEY_DATA);
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
            public void onError(HttpResult result) {
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
