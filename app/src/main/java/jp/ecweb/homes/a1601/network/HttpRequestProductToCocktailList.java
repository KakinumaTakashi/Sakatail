package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 所持品から作れるカクテル一覧取得クラス
 */
public class HttpRequestProductToCocktailList extends HttpRequestBase {
    private static final String TAG = HttpRequestProductToCocktailList.class.getSimpleName();

    private List<HavingProduct> mProductList;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestProductToCocktailList(Context context) {
        super(context);
    }

    /**
     * 材料設定
     * @param productList   材料情報
     */
    public void setProductList(List<HavingProduct> productList) {
        mProductList = productList;
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(final HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_PRODUCTTOCOCKTAILLIST;
        boolean resultParamCheck = super.post(url, createRequest(), new HttpRequestListener() {
            @Override
            public void onSuccess(HttpResponse result) {
                // ヘッダーチェック
                if (!result.checkResponseHeader()) {
                    listener.onError();
                    return;
                }
                // パース処理
                List<Cocktail> cocktailList = result.toCocktailList();
                if (cocktailList == null) {
                    listener.onError();
                    return;
                }
                listener.onSuccess(cocktailList);
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
        // POSTリクエスト用に材料IDを連結
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mProductList.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mProductList.get(i).getMaterialID());
        }
        // POSTデータ作成
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", stringBuilder.toString());
        } catch (JSONException e) {
            CustomLog.e(TAG, "Create request failed.", e);
            return null;
        }
        return postData;
    }
}
