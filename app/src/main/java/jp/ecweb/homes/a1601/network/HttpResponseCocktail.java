package jp.ecweb.homes.a1601.network;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * HTTP通信結果クラス(カクテル情報)
 */
class HttpResponseCocktail extends HttpResponse {
    private static final String TAG = HttpResponseCocktail.class.getSimpleName();

    /**
     * レスポンスをオブジェクトに変換
     * @return              カクテル情報
     */
    Cocktail toObject() {
        try {
            // データ部処理
            JSONObject data = getResponse().getJSONObject(C.RSP_KEY_DATA);
            return new Cocktail(data);
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
    }
}
