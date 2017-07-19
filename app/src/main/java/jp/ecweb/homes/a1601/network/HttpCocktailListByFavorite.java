package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.Const;
import jp.ecweb.homes.a1601.R;

/**
 * お気に入り別カクテル一覧取得クラス
 */
public class HttpCocktailListByFavorite extends HttpCocktailListBase {

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpCocktailListByFavorite(Context context) {
        super(context);
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_FAVORITE;
        super.post(url, createRequest(), listener);
    }

    /**
     * リクエスト生成
     * @return              リクエスト
     */
    private JSONObject createRequest() {
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
}