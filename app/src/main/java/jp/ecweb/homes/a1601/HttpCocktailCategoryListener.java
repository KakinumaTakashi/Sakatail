package jp.ecweb.homes.a1601;

import jp.ecweb.homes.a1601.model.Category;

/**
 * カテゴリ一覧コールバック
 */
interface HttpCocktailCategoryListener {
    void onSuccess(Category category);
    void onError();
}
