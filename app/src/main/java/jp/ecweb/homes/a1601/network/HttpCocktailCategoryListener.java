package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.CocktailCategory;

/**
 * カテゴリ一覧コールバック
 */
public interface HttpCocktailCategoryListener {
    void onSuccess(CocktailCategory cocktailCategory);
    void onError(int errorCode);
}
