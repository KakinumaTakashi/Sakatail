package jp.ecweb.homes.a1601;

import android.app.Application;

/**
 * アプリケーションクラス
 */
public class SakatailApplication extends Application {

    private int mCocktailCategoryType;
    private int mCocktailCategoryIndex;

    private int mProductCategoryType;
    private int mProductCategoryIndex;

    @Override
    public void onCreate() {
        super.onCreate();
        mCocktailCategoryType = C.CAT_TYPE_COCKTAIL_ALL;
        mCocktailCategoryIndex = -1;
        mProductCategoryType = C.CAT_TYPE_PRODUCT_ALL;
        mProductCategoryIndex = -1;
    }

    public int getCocktailCategoryType() {
        return mCocktailCategoryType;
    }

    public void setCocktailCategoryType(int cocktailCategoryType) {
        this.mCocktailCategoryType = cocktailCategoryType;
    }

    public int getCocktailCategoryIndex() {
        return mCocktailCategoryIndex;
    }

    public void setCocktailCategoryIndex(int cocktailCategoryIndex) {
        this.mCocktailCategoryIndex = cocktailCategoryIndex;
    }

    public int getProductCategoryType() {
        return mProductCategoryType;
    }

    public void setProductCategoryType(int productCategoryType) {
        this.mProductCategoryType = productCategoryType;
    }

    public int getProductCategoryIndex() {
        return mProductCategoryIndex;
    }

    public void setProductCategoryIndex(int productCategoryIndex) {
        this.mProductCategoryIndex = productCategoryIndex;
    }
}
