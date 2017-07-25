package jp.ecweb.homes.a1601.network;


/**
 * HTTP通信コールバック
 */
interface HttpRequestListener {
    void onSuccess(HttpResponse result);
    void onError(HttpResponse result);
}
