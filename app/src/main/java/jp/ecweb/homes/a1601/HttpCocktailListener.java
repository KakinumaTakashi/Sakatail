package jp.ecweb.homes.a1601;

/**
 * カクテル情報コールバック
 */
interface HttpCocktailListener {
    void onSuccess(Cocktail cocktail);
    void onError();
}
