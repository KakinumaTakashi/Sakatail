package jp.ecweb.homes.a1601.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.Const;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.models.HavingProduct;

/**
 * SQLite 所持製品テーブル操作クラス
 */
public class SQLitePersonalBelongings {

	private static final String TAG = SQLitePersonalBelongings.class.getSimpleName();

	private SQLiteDatabase mDb;

	/**
	 * コンストラクタ
	 * @param context       コンテキスト
	 */
	public SQLitePersonalBelongings(Context context) {
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

	private static final String SQL_SELECT_PRODUCTID_AND_MATERIALID
			= "SELECT " + Const.COLUMN_PRODUCTID + "," + Const.COLUMN_MATERIALID
			+ " FROM " + Const.TABLE_HAVINGPRODUCT;

	/**
	 * 製品ID存在チェック処理
	 * @param productId         チェック対象製品ID
	 * @return                  存在チェック結果
	 */
	public Boolean ExistProductID(String productId) {
		// 抽出クエリ作成
        String sql = SQL_SELECT_PRODUCTID_AND_MATERIALID + " WHERE " + Const.COLUMN_PRODUCTID + "=?";
		// 抽出クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{productId});
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
	 * 材料ID存在チェック処理
	 * @param materialId        チェック対象材料ID
	 * @return                  存在チェック結果
	 */
	public Boolean ExistMaterialID(String materialId) {
		// 抽出クエリ作成
        String sql = SQL_SELECT_PRODUCTID_AND_MATERIALID + " WHERE " + Const.COLUMN_MATERIALID + "=?";
		// 抽出クエリ実行
		Cursor cursor = mDb.rawQuery(sql, new String[]{materialId});
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
     * 所持製品リスト取得処理
     * @return                  所持製品リスト
     */
	public List<HavingProduct> getProductList() {
        List<HavingProduct> productList = new ArrayList<>();
		// 選択クエリ実行
		Cursor cursor= mDb.rawQuery(SQL_SELECT_PRODUCTID_AND_MATERIALID, null);
        if (cursor != null) {
            // カクテルIDをListに格納
            cursor.moveToFirst();
            // 所持製品テーブルをListに格納
            for (int i = 0; i < cursor.getCount(); i++) {
                HavingProduct product = new HavingProduct();
                product.setProductID(cursor.getString(cursor.getColumnIndex(Const.COLUMN_PRODUCTID)));
                product.setMaterialID(cursor.getString(cursor.getColumnIndex(Const.COLUMN_MATERIALID)));
                productList.add(product);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            CustomLog.e(TAG, "Failed to select table.");
        }
		return productList;
	}

    /**
     * 所持製品テーブル追加処理
     * @param product           所持製品情報
     * @return                  追加結果
     */
	public Boolean insertProduct(HavingProduct product) {
		// 追加するレコードを作成
		ContentValues values = new ContentValues();
		values.put(Const.COLUMN_PRODUCTID, product.getProductID());
		values.put(Const.COLUMN_MATERIALID, product.getMaterialID());
		// 所持製品テーブルにレコードを追加
        long insertCount = mDb.insert(Const.TABLE_HAVINGPRODUCT, null, values);
        if (insertCount >= 0) {
            CustomLog.d(TAG, "Successful insertion into HavingProduct table. [values:" + values.toString() + "]");
            return true;
        } else {
            CustomLog.e(TAG, "Failed to insert into HavingProduct table. [values:" + values.toString() + "]");
            return false;
        }
	}

    /**
     * 所持製品テーブル削除処理
     * @param product           所持製品情報
     * @return                  削除結果
     */
	public Boolean deleteProduct(HavingProduct product) {
        // 削除追加するレコードを作成
        String[] values = new String[]{product.getProductID()};
        // 所持製品テーブルからレコードを削除
        int deleteCount = mDb.delete(Const.TABLE_HAVINGPRODUCT, Const.COLUMN_PRODUCTID + "=?", values);
        if (deleteCount >= 0) {
            CustomLog.d(TAG, "Successfully deleted from HavingProduct table. [values:" + product.getProductID() + "]");
            return true;
        } else {
            CustomLog.e(TAG, "Failed to insert into HavingProduct table. [values:" + product.getProductID() + "]");
            return false;
        }
	}

}
