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
import jp.ecweb.homes.a1601.models.RakutenResponse;
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

    Cocktail toCocktail() {
        try {
            // データ部処理
            JSONObject data = response.getJSONObject(C.RSP_KEY_DATA);
            return new Cocktail(data);
        } catch (JSONException e) {
            CustomLog.e(TAG, "Response parsing failed", e);
            return null;
        }
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

    RakutenResponse toRakutenResponse() {
        return new RakutenResponse(response);
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

    Category toProductCategory() {
        Category category = new Category();
        try {
            JSONObject data = response.getJSONObject(C.RSP_KEY_DATA);
            // TODO Categoryクラスをカクテル一覧と商品一覧で分ける
            // Category1Items
            JSONArray category1Items = data.getJSONArray(C.RSP_KEY_PRODUCT_CATEGORY1ITEMS);
            for (int i = 0; i < category1Items.length(); i++) {
                JSONObject jsonObject = category1Items.getJSONObject(i);
                category.getCategory1List().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKER));
                category.getCategory1ValueList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKER));
                category.getCategory1NumList().add(jsonObject.getString(C.RSP_KEY_PRODUCT_CAT1_MAKERNUM));
            }
            // Category2Items
            JSONArray category2Items = data.getJSONArray(C.RSP_KEY_PRODUCT_CATEGORY2ITEMS);
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
