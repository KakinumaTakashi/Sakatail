package jp.ecweb.homes.a1601.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.storage.SQLiteFavorite;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.network.HttpCocktailCategory;
import jp.ecweb.homes.a1601.network.HttpCocktailCategoryListener;
import jp.ecweb.homes.a1601.network.HttpCocktailListByCategory;
import jp.ecweb.homes.a1601.network.HttpCocktailListByFavorite;
import jp.ecweb.homes.a1601.network.HttpCocktailListListener;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;


public class CocktailListActivity extends AppCompatActivity implements HttpCocktailListListener {

	private static final String TAG = CocktailListActivity.class.getSimpleName();

	private CocktailListAdapter mListViewAdapter;					// ListViewアダプター
	private SQLiteFavorite mSQLiteFavorite;                               // SQLite お気に入りテーブル

	private List<Cocktail> mCocktailList = new ArrayList<>();		// カクテル一覧
	private Category mCategory = new Category();                    // 選択カテゴリ

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CustomLog.d(TAG, "onCreate start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cocktail_list);

		// 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
		// メンバ変数の初期化
		mSQLiteFavorite = new SQLiteFavorite(this);
		// ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(this, R.layout.activity_cocktail_list_item, mCocktailList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mListViewAdapter);
		// アイテムのリスナーを登録(詳細画面にカクテルIDを渡して遷移)
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// タップされたアイテムのカクテルIDを取得
						Cocktail cocktail = (Cocktail) parent.getItemAtPosition(position);
						CustomLog.d(TAG, "Select Cocktail=" + "ID:" + cocktail.getId() + "/Name:" + cocktail.getName());
						// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
						Intent intent = new Intent(CocktailListActivity.this, CocktailActivity.class);
						intent.putExtra("ID", cocktail.getId());
						startActivity(intent);
					}
				}
		);
		// 絞り込み用カテゴリ一覧の取得
        HttpCocktailCategory categoryList = new HttpCocktailCategory(this);
        categoryList.get(new HttpCocktailCategoryListener() {
            @Override
            public void onSuccess(Category category) {
                // カテゴリ情報を設定
                mCategory = category;
            }
            @Override
            public void onError() {

            }
        });
	}

	@Override
	protected void onStart() {
		CustomLog.d(TAG, "onStart start");

		super.onStart();

		// カクテル一覧の取得
        HttpCocktailListByCategory cocktailList = new HttpCocktailListByCategory(this);
        cocktailList.setCategory(mCategory);
        cocktailList.post(this);
	}
/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
		getMenuInflater().inflate(R.menu.menu_cocktail_list, menu);
		// タップリスナーの登録
		// 戻る
		menu.findItem(R.id.menu_back).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						finish();
						return true;
					}
				}
		);
		return true;
	}
/*--------------------------------------------------------------------------------------------------
	ボタンタップイベント処理
--------------------------------------------------------------------------------------------------*/
	/**
	 * 「全て」ボタン押下処理
	 * @param view  ボタンオブジェクト
	 */
	public void onAllButtonTapped(View view) {
		// 絞り込みをリセット
		mCategory.resetCategoryAll();
		// カクテル一覧の取得
        HttpCocktailListByCategory cocktailList = new HttpCocktailListByCategory(this);
        cocktailList.setCategory(mCategory);
        cocktailList.post(this);
	}

    /**
     * 「五十音」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onJapaneseSyllabaryButtonTapped(View view) {
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(CocktailListActivity.this);
		//ダイアログタイトルをセット
		builder.setTitle("頭文字を選択");
		// 表示項目の作成
		CharSequence[] items = new CharSequence[mCategory.getCategory1List().size()];
		for (int i = 0; i < mCategory.getCategory1List().size(); i++) {
			items[i] = mCategory.getCategory1List().get(i) + "  （" +
						mCategory.getCategory1NumList().get(i) + " 件）";
		}
		// 表示項目・リスナーの登録
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した五十音をセット(ベースの絞り込みは解除)
				mCategory.setCategory1(mCategory.getCategory1List().get(which));
				mCategory.resetCategory2();
                // カクテル一覧の取得
                HttpCocktailListByCategory cocktailList = new HttpCocktailListByCategory(CocktailListActivity.this);
                cocktailList.setCategory(mCategory);
                cocktailList.post(CocktailListActivity.this);
			}
		});
		// ダイアログ表示
		builder.show();
	}

    /**
     * 「ベース」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onBaseButtonTapped(View view) {
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(CocktailListActivity.this);
		//ダイアログタイトルをセット
		builder.setTitle("ベースを選択");
		// 表示項目の作成
		CharSequence[] items = new CharSequence[mCategory.getCategory2List().size()];
		for (int i = 0; i < mCategory.getCategory2List().size(); i++) {
			items[i] = mCategory.getCategory2List().get(i) + "  （" +
					mCategory.getCategory2NumList().get(i) + " 件）";
		}
        // 表示項目・リスナーの登録
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択したベースをセット(五十音の絞り込みは解除)
				mCategory.resetCategory1();
				mCategory.setCategory2(mCategory.getCategory2List().get(which));
				// カクテル一覧の取得
                HttpCocktailListByCategory cocktailList = new HttpCocktailListByCategory(CocktailListActivity.this);
                cocktailList.setCategory(mCategory);
                cocktailList.post(CocktailListActivity.this);
			}
		});
		// ダイアログ表示
		builder.show();
	}

    /**
     * 「お気に入り」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onFavoriteButtonTapped(View view) {
		// カクテル一覧の取得
        HttpCocktailListByFavorite cocktailList = new HttpCocktailListByFavorite(this);
        cocktailList.setFavoriteList(mSQLiteFavorite.getFavoriteList());
        cocktailList.post(this);
	}
/*--------------------------------------------------------------------------------------------------
	非同期コールバック
--------------------------------------------------------------------------------------------------*/
    /**
     * カクテル一覧取得処理の正常終了コールバック
     * @param cocktailList  カクテル一覧
     */
	@Override
	public void onSuccess(List<Cocktail> cocktailList) {
		CustomLog.d(TAG, "onSuccess start");
		// ListView表示用カクテル一覧を更新
		mCocktailList.clear();
		mCocktailList.addAll(cocktailList);
		// ListViewに更新を通知
		mListViewAdapter.notifyDataSetChanged();
        // リストを先頭に戻す
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setSelection(0);
	}

    /**
     * カクテル一覧取得処理の異常終了コールバック
     */
    @Override
    public void onError() {
        CustomLog.d(TAG, "onError start");
        Toast.makeText(this, getString(R.string.ERR_VolleyMessage_text), Toast.LENGTH_SHORT).show();
    }
}
