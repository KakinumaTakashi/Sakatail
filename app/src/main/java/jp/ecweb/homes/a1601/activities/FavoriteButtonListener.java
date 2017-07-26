package jp.ecweb.homes.a1601.activities;

import android.view.View;
import android.widget.Toast;

import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.Favorite;
import jp.ecweb.homes.a1601.storage.SQLiteFavorite;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * お気に入りボタン クリックリスナー
 */
class FavoriteButtonListener implements View.OnClickListener {

    private static final String TAG = FavoriteButtonListener.class.getSimpleName();

    /**
     * クリック処理
     * @param view          ボタンビュー
     */
    @Override
    public void onClick(View view) {
        CustomLog.d(TAG, "FavoriteButtonListener.onClick");
        SQLiteFavorite sqliteFavorite = new SQLiteFavorite(view.getContext());
        FavoriteButton btn = (FavoriteButton) view;
        if (btn.isChecked()) {
            // ボタンがONになった場合はお気に入りテーブルにカクテルIDを追加
            Favorite favorite = new Favorite();
            favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));
            if (!sqliteFavorite.insertFavorite(favorite)) {
                Toast.makeText(
                        view.getContext(),
                        view.getContext().getString(R.string.ERR_InsertFavoriteFailure),
                        Toast.LENGTH_SHORT
                ).show();
                btn.setChecked(false);
            }
        } else {
            // ボタンがOFFになった場合はお気に入りテーブルからカクテルIDを削除
            Favorite favorite = new Favorite();
            favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));
            if (!sqliteFavorite.deleteFavorite(favorite)) {
                Toast.makeText(
                        view.getContext(),
                        view.getContext().getString(R.string.ERR_DeleteFavoriteFailure),
                        Toast.LENGTH_SHORT
                ).show();
                btn.setChecked(true);
            }
        }
        sqliteFavorite.close();
    }
}
