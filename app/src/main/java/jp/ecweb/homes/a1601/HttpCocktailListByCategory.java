package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * カテゴリ別カクテル一覧取得クラス
 */
class HttpCocktailListByCategory extends HttpCocktailListBase {

    HttpCocktailListByCategory(Context context) {
        super(context);
    }

    @Override
    JSONObject createRequest() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("Category1", mCategory.getCategory1());
            postData.put("Category2", mCategory.getCategory2());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    void post(HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_COCKTAIL;
        super.post(url, createRequest(), listener);
    }
}
