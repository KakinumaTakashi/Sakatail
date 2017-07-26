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
import jp.ecweb.homes.a1601.models.Favorite;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * お気に入り別カクテル一覧取得クラス
 */
public class HttpRequestCocktailListByFavorite extends HttpRequestBase {
    private static final String TAG = HttpRequestCocktailListByFavorite.class.getSimpleName();

    private List<Favorite> mFavoriteList;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public HttpRequestCocktailListByFavorite(Context context) {
        super(context);
    }

    /**
     * お気に入り設定
     * @param favoriteList  お気に入り情報
     */
    public void setFavoriteList(List<Favorite> favoriteList) {
        mFavoriteList = favoriteList;
    }

    /**
     * POSTリクエスト送信
     * @param listener      通信完了リスナー
     */
    public void post(final HttpCocktailListListener listener) {
        String url = mContext.getString(R.string.server_URL) + C.WEBAPI_FAVORITE;
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
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mFavoriteList.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(mFavoriteList.get(i).getCocktailId());
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", stringBuilder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }
}
