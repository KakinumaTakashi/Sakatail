package jp.ecweb.homes.a1601.network;

import java.util.List;

import jp.ecweb.homes.a1601.models.Product;

/**
 * 商品一覧コールバック
 */
public interface HttpProductListListener {
    void onSuccess(List<Product> productList);
    void onError(int errorCode);
}
