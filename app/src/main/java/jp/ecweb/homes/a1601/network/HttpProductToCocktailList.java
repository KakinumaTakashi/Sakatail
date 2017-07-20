package jp.ecweb.homes.a1601.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.Const;
import jp.ecweb.homes.a1601.R;

/**
 * 所持品から作れるカクテル一覧取得クラス
 */
public class HttpProductToCocktailList extends HttpCocktailListBase {

//    private static final String TAG = HttpProductToCocktailList.class.getSimpleName();

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpProductToCocktailList(Context context) {
        super(context);
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + Const.WEBAPI_PRODUCTTOCOCKTAILLIST;
        super.post(url, createRequest(), listener);
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
            e.printStackTrace();
        }
        return postData;
    }
}
