package jp.ecweb.homes.a1601.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Const;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.models.Favorite;

/**
 * SQLite お気に入りテーブル操作クラス
 */
public class SQLiteFavorite {

    private static final String TAG = SQLiteFavorite.class.getSimpleName();

	private SQLiteDatabase mDb;

	private static final String SQL_SELECT_COCKTAILID
            = "SELECT " + Const.COLUMN_COCKTAILID + " FROM " + Const.TABLE_FAVORITE;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
    public SQLiteFavorite(Context context) {
		SQLiteHelper SQLiteHelper = new SQLiteHelper(context);
		mDb = SQLiteHelper.getWritableDatabase();
	}

    /**
     * DBクローズ
     */
    @SuppressWarnings("UnusedDeclaration")
    public void close() {
		if (mDb != null) mDb.close();
	}

    /**
     * カクテルID存在チェック処理
     * @param cocktailId        チェック対象カクテルID
     * @return                  存在チェック結果
     */
	public Boolean ExistCocktailId(String cocktailId) {
		// 抽出クエリ作成
		String sql = SQL_SELECT_COCKTAILID + " WHERE " + Const.COLUMN_COCKTAILID + "=?";
		// 選択クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{cocktailId});
		if (cursor != null) {
            boolean result = cursor.moveToFirst();
            cursor.close();
            // チェック結果を返す
            return result;
        } else {
            CustomLog.e(TAG, "Failed to select table.");
            return false;
        }
	}

    /**
     * お気に入りリスト取得処理
     * @return                  お気に入りリスト
     */
	public List<Favorite> getFavoriteList() {
        List<Favorite> favoriteList = new ArrayList<>();
		// 選択クエリ実行
		Cursor cursor= mDb.rawQuery(SQL_SELECT_COCKTAILID, null);
		if (cursor != null) {
            // カクテルIDをListに格納
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Favorite favorite = new Favorite();
                favorite.setCocktailId(cursor.getString(cursor.getColumnIndex(Const.COLUMN_COCKTAILID)));
                favoriteList.add(favorite);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
		    CustomLog.e(TAG, "Failed to select table.");
        }
		return favoriteList;
	}

    /**
     * お気に入りリスト追加処理
     * @param favorite      お気に入り情報
     * @return              追加結果
     */
	public Boolean insertFavorite(Favorite favorite) {
		// 追加するレコードを作成
		ContentValues values = new ContentValues();
		values.put(Const.COLUMN_COCKTAILID, favorite.getCocktailId());
        // お気に入りテーブルにレコードを追加
        long insertCount = mDb.insert(Const.TABLE_FAVORITE, null, values);
        if (insertCount >= 0) {
            CustomLog.d(TAG, "Successful insertion into favorite table. [values:" + favorite.getCocktailId() + "]");
            return true;
        } else {
            CustomLog.e(TAG, "Failed to insert into favorite table. [values:" + favorite.getCocktailId() + "]");
            return false;
        }
	}

    /**
     * お気に入りリスト削除処理
     * @param favorite      お気に入り情報
     * @return              削除結果
     */
	public Boolean deleteFavorite(Favorite favorite) {
        // 削除追加するレコードを作成
	    String[] values = new String[]{favorite.getCocktailId()};
		// お気に入りテーブルからレコードを削除
        int deleteCount = mDb.delete(Const.TABLE_FAVORITE, Const.COLUMN_COCKTAILID + "=?", values);
        if (deleteCount >= 0) {
            CustomLog.d(TAG, "Successfully deleted from favorite table. [values:" + favorite.getCocktailId() + "]");
            return true;
        } else {
            CustomLog.e(TAG, "Failed to insert into favorite table. [values:" + favorite.getCocktailId() + "]");
            return false;
        }
	}
}
