package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 *
 */
public class HttpRequestProductListByHaving extends HttpRequestBase {
    private static final String TAG = HttpRequestProductListByHaving.class.getSimpleName();

    private List<HavingProduct> mHavingProductList;

    /**
     * コンストラクタ
     * @param context           コンテキスト
     */
    public HttpRequestProductListByHaving(Context context) {
        super(context);
    }

    /**
     * 所持商品リスト設定
     * @param havingProductList 所持商品リスト
     */
    public void setHavingProductList(List<HavingProduct> havingProductList) {
        mHavingProductList = havingProductList;
    }

    /**
     * POSTリクエスト送信
     * @param listener          通信完了リスナー
     */
    public void post(final HttpProductListListener listener) {
//        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTLIST;
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTHAVELIST;
        boolean resultParamCheck = super.post(url, createRequest(), new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError();
                    return;
                }
                // パース処理
                List<Product> productList = result.toProductList();
                if (productList == null) {
                    listener.onError();
                    return;
                }
                listener.onSuccess(productList);
            }
            @Override
            public void onError(HttpResponse result) {
                CustomLog.e(TAG, "HTTP connection error "
                        + "[statusCode:" + result.getStatusCode() + " , message:" + result.getMessage() + "]");
                listener.onError();
            }
        });
        if (!resultParamCheck) {
            listener.onError();
        }
    }

    /**
     * リクエスト生成
     * @return              リクエスト
     */
    private JSONObject createRequest() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mHavingProductList.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mHavingProductList.get(i).getProductID());
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", stringBuilder);
        } catch (JSONException e) {
            CustomLog.e(TAG, "Create request failed.", e);
            return null;
        }
//
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put(C.REQ_KEY_MAKER, mCategory.getCategory1());
//            postData.put(C.REQ_KEY_MATERIALID, mCategory.getCategory2());
//        } catch (JSONException e) {
//            CustomLog.e(TAG, "Create request failed.", e);
//            return null;
//        }
        return postData;
    }

}
