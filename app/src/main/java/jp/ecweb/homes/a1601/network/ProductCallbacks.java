package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.RakutenResponse;

/**
 * 商品情報　コールバックインターフェイス
 *
 * Created by KakinumaTakashi on 2016/09/06.
 */
public interface ProductCallbacks {
	void ProductCallback(RakutenResponse rakutenResponse);
}
