package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                    listener.onError(C.RSP_CD_HEADERCHECKERROR);
                    return;
                }
                // データ部処理
                try {
                    List<Cocktail> cocktailList = new ArrayList<>();
                    JSONArray data = result.getResponse().getJSONArray(C.RSP_KEY_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonCocktailObject = data.getJSONObject(i);
                        Cocktail cocktail = new Cocktail(jsonCocktailObject);
                        cocktailList.add(cocktail);
                    }
                    listener.onSuccess(cocktailList);
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
