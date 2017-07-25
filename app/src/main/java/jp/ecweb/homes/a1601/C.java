package jp.ecweb.homes.a1601;

/**
 * 定数クラス
 */
@SuppressWarnings("UnusedDeclaration")
public final class C {
    private C() {}

    public static final String LIB_VERSION = BuildConfig.VERSION_NAME;
    public static final Boolean DEBUG_MODE = BuildConfig.BUILD_TYPE.equals("debug");

    /** 楽天クレジット表示用HTML **/
    public static final String RAKUTEN_CREDIT_CONTENTS      = "file:///android_asset/rakutencredit.html";
    /** Intent Extra キー **/
    public static final String EXTRA_KEY_COCKTAILID         = "ID";
    public static final String EXTRA_KEY_PRODUCTITEMCODE    = "itemCode";
    public static final String EXTRA_KEY_PRODUCTID          = "productId";
    public static final String EXTRA_KEY_MATERIALID         = "materialId";
    /** WEB API URL **/
    public static final String WEBAPI_COCKTAILLIST          = "getCocktailList.php";
    public static final String WEBAPI_FAVORITE              = "getFavoriteCocktailList.php";
    public static final String WEBAPI_CATEGORY              = "getCocktailCategory.php";
    public static final String WEBAPI_COCKTAIL              = "getCocktail.php";
    public static final String WEBAPI_PRODUCTTOCOCKTAILLIST = "getProductToCocktailList.php";
    public static final String WEBAPI_PRODUCTCATEGORY       = "getProductCategory.php";
    public static final String WEBAPI_PRODUCTLIST           = "getProductList.php";
    /** リクエストキー **/
    public static final String REQ_KEY_CATEGORY1            = "Category1";
    public static final String REQ_KEY_CATEGORY2            = "Category2";
    public static final String REQ_KEY_MAKER                = "Maker";
    public static final String REQ_KEY_MATERIALID           = "MaterialId";
    /** レスポンスJSON **/
    // 共通部キー定義
    public static final String RSP_KEY_STATUS               = "status";
    public static final String RSP_KEY_MESSAGE              = "message";
    public static final String RSP_KEY_DATA                 = "data";
    // ステータス定義
    public static final String RSP_STATUS_OK                = "OK";
    public static final String RSP_STATUS_NG                = "NG";
    // カクテルカテゴリ情報キー定義
    public static final String RSP_KEY_CATEGORY1ITEMS       = "CATEGORY1ITEMS";
    public static final String RSP_KEY_CATEGORY1            = "CATEGORY1";
    public static final String RSP_KEY_CATEGORY1NUM         = "CATEGORY1NUM";
    public static final String RSP_KEY_CATEGORY2ITEMS       = "CATEGORY2ITEMS";
    public static final String RSP_KEY_CATEGORY2            = "CATEGORY2";
    public static final String RSP_KEY_CATEGORY2_NUM        = "CATEGORY2NUM";
    // 商品カテゴリ情報キー定義
    public static final String RSP_KEY_PRODUCT_CATEGORY1ITEMS   = "CATEGORY1ITEMS";
    public static final String RSP_KEY_PRODUCT_CAT1_MAKER       = "MAKER";
    public static final String RSP_KEY_PRODUCT_CAT1_MAKERNUM    = "MAKERNUM";
    public static final String RSP_KEY_PRODUCT_CATEGORY2ITEMS   = "CATEGORY2ITEMS";
    public static final String RSP_KEY_PRODUCT_CAT2_CATEGORY1   = "CATEGORY1";
    public static final String RSP_KEY_PRODUCT_CAT2_CATEGORY2   = "CATEGORY2";
    public static final String RSP_KEY_PRODUCT_CAT2_CATEGORY3   = "CATEGORY3";
    public static final String RSP_KEY_PRODUCT_CAT2_CATID       = "ID";
    public static final String RSP_KEY_PRODUCT_CAT2_CATNUM      = "CATEGORYNUM";
    /** SQLite **/
    public static final String TABLE_FAVORITE               = "favorite";
    public static final String COLUMN_COCKTAILID            = "CocktailID";
    public static final String TABLE_HAVINGPRODUCT          = "HavingProduct";
    public static final String COLUMN_PRODUCTID             = "ProductID";
    public static final String COLUMN_MATERIALID            = "MaterialID";
    /** カスタムログ出力レベル設定 **/
    public static final boolean LOG_VERBOSE                 = DEBUG_MODE;
    public static final boolean LOG_DEBUG                   = true;
    public static final boolean LOG_INFO                    = true;
    public static final boolean LOG_WARNING                 = true;
    public static final boolean LOG_ERROR                   = true;
}
