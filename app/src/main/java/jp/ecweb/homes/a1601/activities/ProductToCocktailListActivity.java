package jp.ecweb.homes.a1601.activities;

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

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.network.HttpCocktailListListener;
import jp.ecweb.homes.a1601.network.HttpProductToCocktailList;
import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;


public class ProductToCocktailListActivity extends AppCompatActivity implements HttpCocktailListListener {

	private static final String TAG = ProductToCocktailListActivity.class.getSimpleName();

	// メンバ変数
	private CocktailListAdapter mListViewAdapter;					// アダプター格納用
	private SQLitePersonalBelongings mSQLitePersonalBelongings;     // SQLite 所持製品テーブル操作クラス

	private List<Cocktail> mCocktailList = new ArrayList<>();		// リスト表示内容

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		CustomLog.d(TAG, "onCreate start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_to_cocktail);

	    // 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
	    // メンバ変数の初期化
	    mSQLitePersonalBelongings = new SQLitePersonalBelongings(this);
	    // ListViewのアダプターを登録
		mListViewAdapter = new CocktailListAdapter(this, R.layout.activity_cocktail_list_item, mCocktailList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mListViewAdapter);
		// アイテムタップ時のリスナーを登録
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// タップされたアイテムのカクテルIDを取得
						Cocktail cocktail = (Cocktail) parent.getItemAtPosition(position);
						CustomLog.d(TAG, "Select Cocktail [ID:" + cocktail.getId()
								+ ", Name:" + cocktail.getName() + "]");
						// 詳細画面に遷移(タップされたカクテルIDを引き渡す)
						Intent intent = new Intent(ProductToCocktailListActivity.this, CocktailActivity.class);
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

		// 所持製品テーブルから所持している製品・材料のリストを取得
		List<HavingProduct> productList = mSQLitePersonalBelongings.getProductList();
		// カクテル一覧の取得
		HttpProductToCocktailList cocktailList = new HttpProductToCocktailList(this);
		cocktailList.setProductList(productList);
		cocktailList.post(this);
	}
/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
		getMenuInflater().inflate(R.menu.menu_product_to_cocktail, menu);
		// タップリスナーの登録
		//材料一覧
		menu.findItem(R.id.menu_productlist).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						// 材料一覧画面に遷移
						Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
						startActivity(intent);
						return true;
					}
				}
		);
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
	非同期コールバック処理
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
        Toast.makeText(this, getString(R.string.ERR_VolleyMessage_text),
                Toast.LENGTH_SHORT).show();
    }
}
