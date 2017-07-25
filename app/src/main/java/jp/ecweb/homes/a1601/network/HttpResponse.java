package jp.ecweb.homes.a1601.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * HTTP通信結果クラス
 */
class HttpResponse {
    private static final String TAG = HttpResponse.class.getSimpleName();

    private boolean success;
    private int statusCode;
    private String message;
    private JSONObject response;

    @SuppressWarnings("UnusedDeclaration")
    boolean isSuccess() {
        return success;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }

    int getStatusCode() {
        return statusCode;
    }

    void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    JSONObject getResponse() {
        return response;
    }

    void setResponse(JSONObject response) {
        this.response = response;
    }

    boolean checkResponseHeader() {
        if (response == null) return false;
        try {
            // ヘッダ部処理
            String status = response.getString(C.RSP_KEY_STATUS);
            if (status.equals(C.RSP_STATUS_NG)) {
                return false;
            }
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return false;
        }
        return true;
    }

    List<Cocktail> toCocktailList() {
        try {
            List<Cocktail> cocktailList = new ArrayList<>();
            // データ部処理
            JSONArray data = response.getJSONArray(C.RSP_KEY_DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonCocktailObject = data.getJSONObject(i);
                Cocktail cocktail = new Cocktail(jsonCocktailObject);
                cocktailList.add(cocktail);
            }
            return cocktailList;
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
    }

    List<Product> toProductList() {
        try {
            List<Product> productList = new ArrayList<>();
            // データ部処理
            JSONArray data = response.getJSONArray(C.RSP_KEY_DATA);
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonProductObject = data.getJSONObject(i);
                Product product = new Product(jsonProductObject);
                productList.add(product);
            }
            return productList;
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
    }

    Category toCategory() {
        try {
            JSONObject data = response.getJSONObject(C.RSP_KEY_DATA);
            return new Category(data);
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
    }
}
