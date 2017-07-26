package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.RakutenResponse;

/**
 * 楽天商品検索API 商品情報コールバック
 */
public interface HttpRakutenListener {
    void onSuccess(RakutenResponse rakutenResponse);
    void onError(int errorCode);
}
