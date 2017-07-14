package jp.ecweb.homes.a1601;

import java.util.List;

import jp.ecweb.homes.a1601.model.Cocktail;

/**
 * カクテル一覧コールバック
 */
interface HttpCocktailListListener {
    void onSuccess(List<Cocktail> cocktailList);
    void onError();
}
