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

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.network.HttpCategoryListener;
import jp.ecweb.homes.a1601.network.HttpRequestProductCategory;
import jp.ecweb.homes.a1601.network.HttpProductListListener;
import jp.ecweb.homes.a1601.network.HttpRequestProductListByCategory;
import jp.ecweb.homes.a1601.network.HttpRequestProductListByHaving;
import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;

public class ProductListActivity extends AppCompatActivity implements HttpProductListListener {

	private static final String TAG = ProductListActivity.class.getSimpleName();

    // メンバ変数
	private ProductListAdapter mListViewAdapter;					// アダプター格納用
	private SQLitePersonalBelongings mSQLitePersonalBelongings;     // 所持製品テーブル操作クラス

    private List<Product> mProductList = new ArrayList<>();         // 商品一覧
	private Category mCategory = new Category();                    // 選択カテゴリ

/*--------------------------------------------------------------------------------------------------
    Activityイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		CustomLog.d(TAG, "onCreate start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

	    // 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
	    // メンバ変数の初期化
	    mSQLitePersonalBelongings = new SQLitePersonalBelongings(this);
	    // ListViewのアダプターを登録
	    mListViewAdapter = new ProductListAdapter(getBaseContext(),
			    R.layout.activity_product_list_item, mProductList);
		ListView listView = (ListView) findViewById(R.id.listView);
	    listView.setAdapter(mListViewAdapter);
		// アイテムタップ時のリスナーを登録
	    listView.setOnItemClickListener(
			    new AdapterView.OnItemClickListener() {
				    @Override
				    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					    // タップされたアイテムのインスタンスを取得
					    Product product = (Product) parent.getItemAtPosition(position);
						CustomLog.d(TAG, "Select Product [ID:" + product.getId()
								+ ", Name:" + product.getName() + "]");
					    // 詳細画面に遷移
					    Intent intent = new Intent(ProductListActivity.this, ProductActivity.class);
					    intent.putExtra(C.EXTRA_KEY_PRODUCTITEMCODE, product.getItemCode());
					    intent.putExtra(C.EXTRA_KEY_PRODUCTID, product.getId());
					    intent.putExtra(C.EXTRA_KEY_MATERIALID, product.getMaterialID());
						startActivity(intent);
				    }
			    }
	    );
		// 絞り込み用カテゴリ一覧の取得
		HttpRequestProductCategory categoryList = new HttpRequestProductCategory(this);
		categoryList.get(new HttpCategoryListener() {
			@Override
			public void onSuccess(Category category) {
				// カテゴリ情報を設定
				mCategory = category;
			}
			@Override
			public void onError() {
				Toast.makeText(ProductListActivity.this,
						getString(R.string.ERR_DownloadCategoryFailure), Toast.LENGTH_SHORT).show();
			}
		});
    }

	@Override
	protected void onStart() {
		CustomLog.d(TAG, "onStart start");

		super.onStart();

		// 商品一覧の取得
		HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(this);
		productList.setCategory(mCategory);
		productList.post(this);
	}
/*--------------------------------------------------------------------------------------------------
    メニューイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // リソースの登録
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
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
	// 全て
	public void onAllButtonTapped(View view) {
		CustomLog.d(TAG, "onAllButtonTapped start");

		// 絞り込みをリセット
		mCategory.resetCategoryAll();

        // 商品一覧の取得
        HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(this);
        productList.setCategory(mCategory);
        productList.post(this);
	}

	// 製造・販売
	public void onMakerButtonTapped(View view) {
		CustomLog.d(TAG, "onMakerButtonTapped start");

		CharSequence[] makerListItems = new CharSequence[mCategory.getCategory1List().size()];

		for (int i = 0; i < mCategory.getCategory1List().size(); i++) {
            makerListItems[i] = makeCategoryString(
                    mCategory.getCategory1List().get(i),
                    mCategory.getCategory1NumList().get(i)
            );
        }

		// ダイアログを表示
		AlertDialog.Builder builder =
				new AlertDialog.Builder(ProductListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("製造/販売会社を選択");

		// 表示項目とリスナの設定
		builder.setItems(makerListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した製造/販売会社をセット(カテゴリの絞り込みは解除)
				mCategory.setCategory1(mCategory.getCategory1ValueList().get(which));
				mCategory.resetCategory2();

                // 商品一覧の取得
                HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(ProductListActivity.this);
                productList.setCategory(mCategory);
                productList.post(ProductListActivity.this);
			}
		});

		builder.show();
	}

	// 材料カテゴリ
	public void onCategoryButtonTapped(View view) {
		CustomLog.d(TAG, "onCategoryButtonTapped start");

		CharSequence[] categoryListItems = new CharSequence[mCategory.getCategory2List().size()];

		for (int i = 0; i < mCategory.getCategory2List().size(); i++) {
			// 表示用文字列構築
            categoryListItems[i] = makeCategoryString(
                    mCategory.getCategory2List().get(i),
                    mCategory.getCategory2NumList().get(i)
            );
		}

		// ダイアログを表示
		AlertDialog.Builder builder =
				new AlertDialog.Builder(ProductListActivity.this);

		//ダイアログタイトルをセット
		builder.setTitle("材料カテゴリを選択");

		// 表示項目とリスナの設定
		builder.setItems(categoryListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 選択した材料カテゴリをセット(製造/販売会社の絞り込みは解除)
				mCategory.resetCategory1();
				mCategory.setCategory2(mCategory.getCategory2ValueList().get(which));

                // 商品一覧の取得
                HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(ProductListActivity.this);
                productList.setCategory(mCategory);
                productList.post(ProductListActivity.this);
			}
		});

		builder.show();
	}

	// 持っている
	public void onHoldButtonTapped(View view) {
		CustomLog.d(TAG, "onHoldButtonTapped start");

		// 所持商品IDを取得
		List<HavingProduct> havingProductList = mSQLitePersonalBelongings.getProductList();

		// 商品一覧の取得
		HttpRequestProductListByHaving productList = new HttpRequestProductListByHaving(this);
		productList.setHavingProductList(havingProductList);
		productList.post(this);
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
/*--------------------------------------------------------------------------------------------------
	非同期コールバック処理
--------------------------------------------------------------------------------------------------*/
    @Override
    public void onSuccess(List<Product> productList) {
		CustomLog.d(TAG, "onSuccess start");
		// ListView表示用商品一覧を更新
		mProductList.clear();
		mProductList.addAll(productList);
		// ListViewに更新を通知
		mListViewAdapter.notifyDataSetChanged();
		// リストを先頭に戻す
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setSelection(0);
    }

    @Override
    public void onError() {
        CustomLog.d(TAG, "onError start");
        Toast.makeText(this, getString(R.string.ERR_VolleyMessage_text),
                Toast.LENGTH_SHORT).show();
    }
}

