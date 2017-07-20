package jp.ecweb.homes.a1601.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteヘルパークラス
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	// メンバ変数
	private static final String DATABASE_NAME = "A1601.db";				// ローカルDB名
	private static final int DATABASE_VERSION = 1;						// DBバージョン

	// 所持製品テーブル
	private static final String PRODUCT_TABLE_NAME = "HavingProduct";	// 所持製品テーブル名
	private static final String SQL_CREATE_PRODUCT_TABLE =		        // DB作成SQL
			"CREATE TABLE " +
					PRODUCT_TABLE_NAME + " (" +
					"ProductID text primary key, " +
					"MaterialID text" +
					");";
	private static final String SQL_DROP_PRODUCT_TABLE =                // DB破棄SQL
			"DROP TABLE " + PRODUCT_TABLE_NAME + ";";

	// お気に入りテーブル
	private static final String FAVORITE_TABLE_NAME = "favorite";
	private static final String SQL_CREATE_FAVORITE_TABLE =		        // DB作成SQL
			"CREATE TABLE " +
					FAVORITE_TABLE_NAME + " (" +
					"CocktailID text primary key" +
					");";
	private static final String SQL_DROP_FAVORITE_TABLE =                // DB破棄SQL
			"DROP TABLE " + PRODUCT_TABLE_NAME + ";";

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
	SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    /**
     * DB作成処理
     * @param sqLiteDatabase    DBオブジェクト
     */
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
	}

    /**
     * DB更新処理
     * @param sqLiteDatabase    DBオブジェクト
     * @param oldVersion        旧DBバージョン
     * @param newVersion        新DBバージョン
     */
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL(SQL_DROP_PRODUCT_TABLE);
		sqLiteDatabase.execSQL(SQL_DROP_FAVORITE_TABLE);
		onCreate(sqLiteDatabase);
	}
}
