package jp.ecweb.homes.a1601;

/**
 * 定数クラス
 */
final class Const {
    private Const() {}

    static final String LIB_VERSION = BuildConfig.VERSION_NAME;
    static final Boolean DEBUG_MODE = BuildConfig.BUILD_TYPE.equals("debug");

    /** 楽天クレジット表示用HTML **/
    static final String RANKUTEN_CREDIT_CONTENTS = "file:///android_asset/rakutencredit.html";

    /** WEB API エンドポイント **/
    static final String WEBAPI_COCKTAILLIST = "getCocktailList.php";
    static final String WEBAPI_FAVORITE     = "getFavoriteCocktailList.php";
    static final String WEBAPI_CATEGORY     = "getCocktailCategory.php";
    static final String WEBAPI_COCKTAIL     = "getCocktail.php";

    /** レスポンスJSON **/
    // 共通部キー定義
    static final String RES_KEY_STATUS      = "status";
    static final String RES_KEY_MESSAGE     = "message";
    static final String RES_KEY_DATA        = "data";
    // ステータス定義
    static final String RES_STATUS_OK       = "OK";
    static final String RES_STATUS_NG       = "NG";

    /** カスタムログ出力レベル設定 **/
    static final boolean LOG_VERBOSE = DEBUG_MODE;
    static final boolean LOG_DEBUG   = true;
    static final boolean LOG_INFO    = true;
    static final boolean LOG_WARNING = true;
    static final boolean LOG_ERROR   = true;
    /** Sensitiveログ **/
    static final boolean LOG_SENSITIVE = DEBUG_MODE;
}
