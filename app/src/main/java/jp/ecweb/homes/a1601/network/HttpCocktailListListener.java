package jp.ecweb.homes.a1601.network;

import java.util.List;

import jp.ecweb.homes.a1601.models.Cocktail;

/**
 * カクテル一覧コールバック
 */
public interface HttpCocktailListListener {
    void onSuccess(List<Cocktail> cocktailList);
    void onError();
}
