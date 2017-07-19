package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.Cocktail;

/**
 * カクテル情報コールバック
 */
public interface HttpCocktailListener {
    void onSuccess(Cocktail cocktail);
    void onError();
}
