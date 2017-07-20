package jp.ecweb.homes.a1601;

/**
 * 定数クラス
 */
@SuppressWarnings("UnusedDeclaration")
public final class Const {
    private Const() {}

    public static final String LIB_VERSION = BuildConfig.VERSION_NAME;
    public static final Boolean DEBUG_MODE = BuildConfig.BUILD_TYPE.equals("debug");

    /** 楽天クレジット表示用HTML **/
    public static final String RAKUTEN_CREDIT_CONTENTS  = "file:///android_asset/rakutencredit.html";

    /** Intent Extra キー **/
    public static final String EXTRA_KEY_COCKTAILID     = "ID";

    /** WEB API URL **/
    public static final String WEBAPI_COCKTAILLIST      = "getCocktailList.php";
    public static final String WEBAPI_FAVORITE          = "getFavoriteCocktailList.php";
    public static final String WEBAPI_CATEGORY          = "getCocktailCategory.php";
    public static final String WEBAPI_COCKTAIL          = "getCocktail.php";

    /** レスポンスJSON **/
    // 共通部キー定義
    public static final String RSP_KEY_STATUS           = "status";
    public static final String RSP_KEY_MESSAGE          = "message";
    public static final String RSP_KEY_DATA             = "data";
    // ステータス定義
    public static final String RSP_STATUS_OK            = "OK";
    public static final String RSP_STATUS_NG            = "NG";
    // カテゴリ情報キー定義
    public static final String RSP_KEY_CATEGORY1ITEMS   = "CATEGORY1ITEMS";
    public static final String RSP_KEY_CATEGORY1        = "CATEGORY1";
    public static final String RSP_KEY_CATEGORY1NUM     = "CATEGORY1NUM";
    public static final String RSP_KEY_CATEGORY2_ITEMS  = "CATEGORY2ITEMS";
    public static final String RSP_KEY_CATEGORY2        = "CATEGORY2";
    public static final String RSP_KEY_CATEGORY2_NUM    = "CATEGORY2NUM";

    /** SQLite **/
    public static final String TABLE_FAVORITE           = "favorite";
    public static final String COLUMN_COCKTAILID        = "CocktailID";

    /** カスタムログ出力レベル設定 **/
    public static final boolean LOG_VERBOSE = DEBUG_MODE;
    public static final boolean LOG_DEBUG   = true;
    public static final boolean LOG_INFO    = true;
    public static final boolean LOG_WARNING = true;
    public static final boolean LOG_ERROR   = true;
}
