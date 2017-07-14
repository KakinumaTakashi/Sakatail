package jp.ecweb.homes.a1601;

/**
 * カテゴリ一覧コールバック
 */
interface HttpCocktailCategoryListener {
    void onSuccess(Category category);
    void onError();
}
