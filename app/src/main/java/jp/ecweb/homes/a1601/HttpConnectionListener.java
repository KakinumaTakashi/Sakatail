package jp.ecweb.homes.a1601;


/**
 * HTTP通信コールバック
 */
interface HttpConnectionListener {
    void onSuccess(HttpResult result);
    void onError(HttpResult result);
}
