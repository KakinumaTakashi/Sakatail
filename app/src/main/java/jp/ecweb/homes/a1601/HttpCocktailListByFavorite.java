package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * お気に入り別カクテル一覧取得クラス
 */
class HttpCocktailListByFavorite extends HttpCocktailListBase {

    HttpCocktailListByFavorite(Context context) {
        super(context);
    }

    @Override
    JSONObject createRequest() {
        // POSTデータの作成
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mFavoriteList.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mFavoriteList.get(i).getCocktailId());
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", stringBuilder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    void post(HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_FAVORITE;
        super.post(url, createRequest(), listener);
    }
}
