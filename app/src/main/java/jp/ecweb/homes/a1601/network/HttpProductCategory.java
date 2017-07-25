package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 材料カテゴリ一覧取得クラス
 */
public class HttpProductCategory {
    private static final String TAG = HttpProductCategory.class.getSimpleName();

    private Context mContext;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpProductCategory(Context context) {
        mContext = context;
    }

    /**
     * GETリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void get(final HttpCategoryListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTCATEGORY;
        HttpConnectionManager manager = new HttpConnectionManager(mContext);
        boolean result = manager.get(url, new HttpRequestListener() {
                    @Override
                    public void onSuccess(HttpResponse result) {
                        JSONObject response = result.getResponse();
                        Category category;
                        try {
                            // ヘッダ部処理
                            String status = response.getString(C.RSP_KEY_STATUS);
                            if (status.equals(C.RSP_STATUS_NG)) {
                                // サーバーにてエラーが発生した場合
                                throw new JSONException(response.getString(C.RSP_KEY_MESSAGE));
                            }
                            // データ部処理
                            JSONObject data = response.getJSONObject(C.RSP_KEY_DATA);
                            category = parseJSON(data);
                        } catch (JSONException e) {
                            CustomLog.e(TAG, "Category parsing failed." , e);
                            category = null;
                        }
                        listener.onSuccess(category);
                    }
                    @Override
                    public void onError(HttpResponse result) {
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
            JSONArray category1Items = jsonCategoryObject.getJSONArray(C.RSP_KEY_PRODUCT_CATEGORY1ITEMS);
            for (int i = 0; i < category1Items.length(); i++) {
                JSONObject jsonObject = category1Items.getJSONObject(i);
                category.getCategory1List().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKER));
                category.getCategory1ValueList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKER));
                category.getCategory1NumList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKERNUM));
            }
            // Category2Items
            JSONArray category2Items = jsonCategoryObject.getJSONArray(C.RSP_KEY_PRODUCT_CATEGORY2ITEMS);
            for (int i = 0; i < category2Items.length(); i++) {
                JSONObject jsonObject = category2Items.getJSONObject(i);
                // 表示用文字列構築
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATEGORY1));
                if (!jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATEGORY2).equals("")) {
                    stringBuilder.append("/");
                    stringBuilder.append(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATEGORY2));
                }
                if (!jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATEGORY3).equals("")) {
                    stringBuilder.append("/");
                    stringBuilder.append(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATEGORY3));
                }
                category.getCategory2List().add(stringBuilder.toString());
                category.getCategory2ValueList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATID));
                category.getCategory2NumList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT2_CATNUM));
            }
        } catch (JSONException e) {
            CustomLog.e(TAG, "Category parsing failed.", e);
            category = null;
        }
        return category;
    }
}
