package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.models.ProductCategory;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 *
 */
public class HttpRequestProductListByCategory extends HttpRequestBase {
    private static final String TAG = HttpRequestProductListByCategory.class.getSimpleName();

    private ProductCategory mProductCategory;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestProductListByCategory(Context context) {
        super(context);
    }

    /**
     * カテゴリ設定
     * @param productCategory      カテゴリ情報
     */
    public void setCategory(ProductCategory productCategory) {
        mProductCategory = productCategory;
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(final HttpProductListListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTLIST;
        boolean resultParamCheck = super.post(url, createRequest(), new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError(C.RSP_CD_HEADERCHECKERROR);
                    return;
                }
                // データ部処理
                try {
                    List<Product> productList = new ArrayList<>();
                    JSONArray data = result.getResponse().getJSONArray(C.RSP_KEY_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonProductObject = data.getJSONObject(i);
                        Product product = new Product(jsonProductObject);
                        productList.add(product);
                    }
                    listener.onSuccess(productList);
                } catch (JSONException e) {
                    listener.onError(C.RSP_CD_PARSINGFAILED);
                }
            }
            @Override
            public void onError(HttpResponse result) {
                CustomLog.e(TAG, "HTTP connection error "
                        + "[statusCode:" + result.getStatusCode() + " , message:" + result.getMessage() + "]");
                listener.onError(C.RSP_CD_HTTPCONNECTIONERROR);
            }
        });
        if (!resultParamCheck) {
            listener.onError(C.RSP_CD_PARAMERROR);
        }
    }

    /**
     * リクエスト生成
     * @return              リクエスト
     */
    private JSONObject createRequest() {
        JSONObject postData = new JSONObject();
        try {
            postData.put(C.REQ_KEY_MAKER, mProductCategory.getCategory1());
            postData.put(C.REQ_KEY_MATERIALID, mProductCategory.getCategory2());
        } catch (JSONException e) {
            CustomLog.e(TAG, "Create request failed.", e);
            return null;
        }
        return postData;
    }

}
