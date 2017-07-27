package jp.ecweb.homes.a1601.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.SakatailApplication;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.storage.SQLiteFavorite;
import jp.ecweb.homes.a1601.models.CocktailCategory;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.network.HttpRequestCocktailCategory;
import jp.ecweb.homes.a1601.network.HttpCocktailCategoryListener;
import jp.ecweb.homes.a1601.network.HttpRequestCocktailListByCategory;
import jp.ecweb.homes.a1601.network.HttpRequestCocktailListByFavorite;
import jp.ecweb.homes.a1601.network.HttpCocktailListListener;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;

import static jp.ecweb.homes.a1601.utils.Utils.startProgress;
import static jp.ecweb.homes.a1601.utils.Utils.stopProgress;


public class CocktailListActivity extends AppCompatActivity implements HttpCocktailListListener {

	private static final String TAG = CocktailListActivity.class.getSimpleName();

	private CocktailListAdapter mListViewAdapter;					// ListViewアダプター
	private SQLiteFavorite mSQLiteFavorite;                         // SQLite お気に入りテーブル
    private ProgressDialog mProgressDialog;                         // プログレスダイアログ

	private List<Cocktail> mCocktailList = new ArrayList<>();		// カクテル一覧

    private List<Category> mJapaneseCategoryList;					// 頭文字カテゴリリスト
    private List<Category> mBaseCategoryList;						// ベースカテゴリリスト

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
						CustomLog.d(TAG, "Select Cocktail [ID:" + cocktail.getId()
                                + ", Name:" + cocktail.getName() + "]");
						// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
						Intent intent = new Intent(CocktailListActivity.this, CocktailActivity.class);
						intent.putExtra(C.EXTRA_KEY_COCKTAILID, cocktail.getId());
						startActivity(intent);
					}
				}
		);
	}

	@Override
	protected void onStart() {
		CustomLog.d(TAG, "onStart start");
		super.onStart();
		// プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // 絞り込み用カテゴリ一覧の取得
        HttpRequestCocktailCategory categoryList = new HttpRequestCocktailCategory(this);
        categoryList.get(new HttpCocktailCategoryListener() {
            @Override
            public void onSuccess(CocktailCategory cocktailCategory) {
                // 頭文字カテゴリ情報を設定
                mJapaneseCategoryList = new ArrayList<>();
                for (int i = 0; i < cocktailCategory.getCategory1List().size(); i++) {
                    Category category = new Category();
                    category.setValue(cocktailCategory.getCategory1List().get(i));
                    category.setCount(Integer.valueOf(cocktailCategory.getCategory1NumList().get(i)));
                    mJapaneseCategoryList.add(category);
                }
                // ベースカテゴリ情報を設定
                mBaseCategoryList = new ArrayList<>();
                for (int i = 0; i < cocktailCategory.getCategory2List().size(); i++) {
                    Category category = new Category();
                    category.setValue(cocktailCategory.getCategory2List().get(i));
                    category.setCount(Integer.valueOf(cocktailCategory.getCategory2NumList().get(i)));
                    mBaseCategoryList.add(category);
                }
                // 初期リストの取得
                execInitialList();
            }
            @Override
            public void onError(int errorCode) {
                Toast.makeText(CocktailListActivity.this,
                        getString(R.string.ERR_DownloadCategoryFailure), Toast.LENGTH_SHORT).show();
                // 初期リストの取得
                execInitialList();
            }
        });
 	}

    /**
     * 初期リストの取得
     */
 	private void execInitialList() {
        int type = ((SakatailApplication) getApplication()).getCocktailCategoryType();
        int index = ((SakatailApplication) getApplication()).getCocktailCategoryIndex();
        switch (type) {
            case C.CAT_TYPE_COCKTAIL_ALL:
                setButtonBackgroundColor(findViewById(R.id.AllButton));
                execPostByAll();
                break;
            case C.CAT_TYPE_COCKTAIL_JAPANESE:
                setButtonBackgroundColor(findViewById(R.id.JapaneseSyllabaryButton));
                execPostByJapanese(index);
                break;
            case C.CAT_TYPE_COCKTAIL_BASE:
                setButtonBackgroundColor(findViewById(R.id.BaseButton));
                execPostByBase(index);
                break;
            case C.CAT_TYPE_COCKTAIL_FAVORITE:
                setButtonBackgroundColor(findViewById(R.id.favoriteButton));
                execPostByFavorite();
                break;
        }
    }

    @Override
    protected void onDestroy() {
	    // DBをクローズ
	    if (mListViewAdapter != null) {
	        mListViewAdapter.destroy();
        }
	    if (mSQLiteFavorite != null) {
	        mSQLiteFavorite.close();
        }
        super.onDestroy();
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
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // カテゴリ選択情報を保存
        ((SakatailApplication) getApplication()).setCocktailCategoryType(C.CAT_TYPE_COCKTAIL_ALL);
        ((SakatailApplication) getApplication()).setCocktailCategoryIndex(-1);
		// カクテル一覧の取得
        execPostByAll();
	}

    /**
     * カクテル一覧の取得(全て)
     */
    private void execPostByAll() {
        HttpRequestCocktailListByCategory cocktailList = new HttpRequestCocktailListByCategory(this);
        cocktailList.setCategory(null, null);
        cocktailList.post(this);
    }

    /**
     * 「五十音」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onJapaneseSyllabaryButtonTapped(View view) {
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
		// 表示用カテゴリリスト生成
		CharSequence[] items = new CharSequence[mJapaneseCategoryList.size()];
		for (int i = 0; i < mJapaneseCategoryList.size(); i++) {
			items[i] = makeCategoryString(
					mJapaneseCategoryList.get(i).getValue(),
					String.valueOf(mJapaneseCategoryList.get(i).getCount()));
		}
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(CocktailListActivity.this);
		builder.setTitle("頭文字を選択");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			    // カテゴリ選択情報を保存
                ((SakatailApplication) getApplication()).setCocktailCategoryType(C.CAT_TYPE_COCKTAIL_JAPANESE);
                ((SakatailApplication) getApplication()).setCocktailCategoryIndex(which);
                // カクテル一覧の取得
                execPostByJapanese(which);
			}
		});
		// ダイアログ表示
		builder.show();
	}

    /**
     * カクテル一覧の取得(五十音)
     */
	private void execPostByJapanese(int which) {
        HttpRequestCocktailListByCategory cocktailList
                = new HttpRequestCocktailListByCategory(CocktailListActivity.this);
        cocktailList.setCategory(mJapaneseCategoryList.get(which).getValue(), null);
        cocktailList.post(CocktailListActivity.this);
    }

    /**
     * 「ベース」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onBaseButtonTapped(View view) {
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
		// 表示用カテゴリリスト生成
		CharSequence[] items = new CharSequence[mBaseCategoryList.size()];
		for (int i = 0; i < mBaseCategoryList.size(); i++) {
			items[i] = makeCategoryString(
					mBaseCategoryList.get(i).getValue(),
					String.valueOf(mBaseCategoryList.get(i).getCount()));
		}
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(CocktailListActivity.this);
		builder.setTitle("ベースを選択");
        // 表示項目・リスナーの登録
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                // カテゴリ選択情報を保存
                ((SakatailApplication) getApplication()).setCocktailCategoryType(C.CAT_TYPE_COCKTAIL_BASE);
                ((SakatailApplication) getApplication()).setCocktailCategoryIndex(which);
				// カクテル一覧の取得
                execPostByBase(which);
			}
		});
		// ダイアログ表示
		builder.show();
	}

    /**
     * カクテル一覧の取得(ベース)
     */
    private void execPostByBase(int which) {
        HttpRequestCocktailListByCategory cocktailList
                = new HttpRequestCocktailListByCategory(CocktailListActivity.this);
        cocktailList.setCategory(null, mBaseCategoryList.get(which).getValue());
        cocktailList.post(CocktailListActivity.this);
    }

    /**
     * 「お気に入り」ボタン押下処理
     * @param view  ボタンオブジェクト
     */
	public void onFavoriteButtonTapped(View view) {
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // カテゴリ選択情報を保存
        ((SakatailApplication) getApplication()).setCocktailCategoryType(C.CAT_TYPE_COCKTAIL_FAVORITE);
        ((SakatailApplication) getApplication()).setCocktailCategoryIndex(-1);
		// カクテル一覧の取得
        execPostByFavorite();
	}

    /**
     * カクテル一覧の取得(お気に入り)
     */
    private void execPostByFavorite() {
        HttpRequestCocktailListByFavorite cocktailList = new HttpRequestCocktailListByFavorite(this);
        cocktailList.setFavoriteList(mSQLiteFavorite.getFavoriteList());
        cocktailList.post(this);
    }

    /**
     * 表示用カテゴリ生成
     * @param name          カテゴリ名
     * @param number        件数
     * @return              表示用カテゴリ
     */
    private String makeCategoryString(String name, String number) {
        return name + "  （" + number + " 件）";
    }

    /**
     * ボタン背景色変更
     * @param view          選択中ボタンビュー
     */
    private void setButtonBackgroundColor(View view) {
        // 背景色を全て透明に戻す
        findViewById(R.id.AllButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.JapaneseSyllabaryButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.BaseButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.favoriteButton).setBackgroundColor(Color.TRANSPARENT);
        // 選択中ボタンの背景色を変更
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
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
        // プログレスダイアログを閉じる
        stopProgress(mProgressDialog);
	}

    /**
     * カクテル一覧取得処理の異常終了コールバック
     */
    @Override
    public void onError(int errorCode) {
        CustomLog.d(TAG, "onError start");
        // プログレスダイアログを閉じる
        stopProgress(mProgressDialog);
        Toast.makeText(this, getString(R.string.ERR_VolleyMessage_text),
                Toast.LENGTH_SHORT).show();
    }
}
