package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.ProductCategory;

/**
 * カテゴリ一覧コールバック
 */
public interface HttpProductCategoryListener {
    void onSuccess(ProductCategory productCategory);
    void onError(int errorCode);
}
