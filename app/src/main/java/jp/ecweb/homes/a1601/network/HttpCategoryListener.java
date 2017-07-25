package jp.ecweb.homes.a1601.network;

import jp.ecweb.homes.a1601.models.Category;

/**
 * カテゴリ一覧コールバック
 */
// TODO 材料カテゴリと共通化
public interface HttpCategoryListener {
    void onSuccess(Category category);
    void onError();
}
