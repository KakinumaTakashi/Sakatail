package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * カテゴリ別カクテル一覧取得クラス
 */
class HttpCocktailListByCategory extends HttpCocktailListBase {

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    HttpCocktailListByCategory(Context context) {
        super(context);
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    void post(HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_COCKTAILLIST;
        super.post(url, createRequest(), listener);
    }

    /**
     * リクエスト生成
     * @return              リクエスト
     */
    private JSONObject createRequest() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("Category1", mCategory.getCategory1());
            postData.put("Category2", mCategory.getCategory2());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }
}
