package jp.ecweb.homes.a1601.network;

import org.json.JSONObject;

/**
 * HTTP通信結果クラス
 */
class HttpResult {

    private boolean success;
    private int statusCode;
    private String message;
    private JSONObject response;

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
}
