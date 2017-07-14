package jp.ecweb.homes.a1601;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.model.Category;

/**
 * カテゴリ一覧取得クラス
 */
class HttpCocktailCategory {
    private static final String TAG = HttpCocktailListBase.class.getSimpleName();

    private Context mContext;

    HttpCocktailCategory(Context context) {
        mContext = context;
    }

    void get(final HttpCocktailCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_CATEGORY;
        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.get(url, new HttpConnectionListener() {
                    @Override
                    public void onSuccess(HttpResult result) {
                        JSONObject response = result.getResponse();
                        Category category;
                        try {
                            // ヘッダ部処理
                            String status = response.getString(Const.RES_KEY_STATUS);
                            if (status.equals(Const.RES_STATUS_NG)) {
                                // サーバーにてエラーが発生した場合
                                throw new JSONException(response.getString(Const.RES_KEY_MESSAGE));
                            }
                            // データ部処理
                            JSONObject data = response.getJSONObject(Const.RES_KEY_DATA);
                            category = new Category(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            category = null;
                        }
                        listener.onSuccess(category);
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
