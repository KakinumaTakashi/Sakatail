package jp.ecweb.homes.a1601;

import java.util.List;

/**
 * カクテル一覧コールバック
 */
interface HttpCocktailListListener {
    void onSuccess(List<Cocktail> cocktailList);
    void onError();
}
