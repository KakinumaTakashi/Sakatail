package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.Const;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;

/**
 * カテゴリ一覧取得クラス
 */
public class HttpCocktailCategory {
    private static final String TAG = HttpCocktailListBase.class.getSimpleName();

    private Context mContext;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpCocktailCategory(Context context) {
        mContext = context;
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(final HttpCocktailCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_CATEGORY;
        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.get(url, new HttpConnectionListener() {
                    @Override
                    public void onSuccess(HttpResult result) {
                        JSONObject response = result.getResponse();
                        Category category;
                        try {
                            // ヘッダ部処理
                            String status = response.getString(Const.RSP_KEY_STATUS);
                            if (status.equals(Const.RSP_STATUS_NG)) {
                                // サーバーにてエラーが発生した場合
                                throw new JSONException(response.getString(Const.RSP_KEY_MESSAGE));
                            }
                            // データ部処理
                            JSONObject data = response.getJSONObject(Const.RSP_KEY_DATA);
                            category = parseJSON(data);
                        } catch (JSONException e) {
                            CustomLog.e(TAG, "Category parsing failed." , e);
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

    /**
     * レスポンスデータ部パース処理
     * @param jsonCategoryObject    レスポンスデータ部
     * @return                      カテゴリ情報オブジェクト
     */
    private Category parseJSON(JSONObject jsonCategoryObject) {
        Category category = new Category();
        try {
            // Category1Items
            JSONArray category1Items = jsonCategoryObject.getJSONArray(Const.RSP_KEY_CATEGORY1ITEMS);
            for (int i = 0; i < category1Items.length(); i++) {
                JSONObject jsonObject = category1Items.getJSONObject(i);
                category.getCategory1List().add(jsonObject.getString(Const.RSP_KEY_CATEGORY1));
                category.getCategory1NumList().add(jsonObject.getString(Const.RSP_KEY_CATEGORY1NUM));
            }
            // Category2Items
            JSONArray category2Items = jsonCategoryObject.getJSONArray(Const.RSP_KEY_CATEGORY2_ITEMS);
            for (int i = 0; i < category2Items.length(); i++) {
                JSONObject jsonObject = category2Items.getJSONObject(i);
                category.getCategory2List().add(jsonObject.getString(Const.RSP_KEY_CATEGORY2));
                category.getCategory2NumList().add(jsonObject.getString(Const.RSP_KEY_CATEGORY2_NUM));
            }
        } catch (JSONException e) {
            CustomLog.e(TAG, "Category parsing failed.", e);
            category = null;
        }
        return category;
    }
}
