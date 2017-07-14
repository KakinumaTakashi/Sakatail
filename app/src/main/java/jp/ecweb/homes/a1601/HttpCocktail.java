package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * カクテル情報取得クラス
 */
class HttpCocktail {
    private static final String TAG = HttpCocktail.class.getSimpleName();

    private Context mContext;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    HttpCocktail(Context context) {
        mContext = context;
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    void get(String cocktailId, final HttpCocktailListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_COCKTAIL + "?id=" + cocktailId;

        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.get(url, new HttpConnectionListener() {
                    @Override
                    public void onSuccess(HttpResult result) {
                        JSONObject response = result.getResponse();
                        Cocktail cocktail;
                        try {
                            // ヘッダ部処理
                            String status = response.getString(Const.RES_KEY_STATUS);
                            if (status.equals(Const.RES_STATUS_NG)) {
                                // サーバーにてエラーが発生した場合
                                throw new JSONException(response.getString(Const.RES_KEY_MESSAGE));
                            }
                            // データ部処理
                            JSONObject data = response.getJSONObject(Const.RES_KEY_DATA);
                            cocktail = new Cocktail(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cocktail = null;
                        }
                        listener.onSuccess(cocktail);
                    }
                    @Override
                    public void onError(HttpResult result) {
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
