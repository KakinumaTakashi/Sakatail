package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.Category;

/**
 * カテゴリ一覧コールバック
 */
public interface HttpCocktailCategoryListener {
    void onSuccess(Category category);
    void onError();
}
