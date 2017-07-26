package jp.ecweb.homes.a1601.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * HTTP通信結果クラス(カクテル情報リスト)
 */
class HttpResponseCocktailList extends HttpResponse {
    private static final String TAG = HttpResponseCocktailList.class.getSimpleName();

    /**
     * レスポンスをオブジェクトに変換
     * @return              カクテル情報リスト
     */
    List<Cocktail> toObject() {
        try {
            List<Cocktail> cocktailList = new ArrayList<>();
            // データ部処理
            JSONArray data = getResponse().getJSONArray(C.RSP_KEY_DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonCocktailObject = data.getJSONObject(i);
                Cocktail cocktail = new Cocktail(jsonCocktailObject);
                cocktailList.add(cocktail);
            }
            return cocktailList;
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
    }
}
