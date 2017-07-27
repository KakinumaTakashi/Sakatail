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
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.SakatailApplication;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.ProductCategory;
import jp.ecweb.homes.a1601.network.HttpProductCategoryListener;
import jp.ecweb.homes.a1601.network.HttpRequestProductCategory;
import jp.ecweb.homes.a1601.network.HttpProductListListener;
import jp.ecweb.homes.a1601.network.HttpRequestProductListByCategory;
import jp.ecweb.homes.a1601.network.HttpRequestProductListByHaving;
import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;

import static jp.ecweb.homes.a1601.utils.Utils.startProgress;
import static jp.ecweb.homes.a1601.utils.Utils.stopProgress;

public class ProductListActivity extends AppCompatActivity implements HttpProductListListener {

	private static final String TAG = ProductListActivity.class.getSimpleName();

    // メンバ変数
	private ProductListAdapter mListViewAdapter;					// アダプター格納用
	private SQLitePersonalBelongings mSQLitePersonalBelongings;     // 所持製品テーブル操作クラス
    private ProgressDialog mProgressDialog;                         // プログレスダイアログ

    private List<Product> mProductList = new ArrayList<>();         // 商品一覧

    private List<Category> mMakerCategoryList;                      // メーカーカテゴリリスト
    private List<Category> mProductCategoryList;                    // 商品カテゴリリスト

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
	    mListViewAdapter = new ProductListAdapter(getBaseContext(), R.layout.activity_product_list_item, mProductList);
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
    }

	@Override
	protected void onStart() {
		CustomLog.d(TAG, "onStart start");
		super.onStart();
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // 絞り込み用カテゴリ一覧の取得
        HttpRequestProductCategory categoryList = new HttpRequestProductCategory(this);
        categoryList.get(new HttpProductCategoryListener() {
            @Override
            public void onSuccess(ProductCategory productCategory) {
                // メーカーカテゴリリストを設定
                mMakerCategoryList = new ArrayList<>();
                for (int i = 0; i < productCategory.getCategory1List().size(); i++) {
                    Category category = new Category();
                    category.setValue(productCategory.getCategory1List().get(i));
                    category.setKey(productCategory.getCategory1ValueList().get(i));
                    category.setCount(Integer.valueOf(productCategory.getCategory1NumList().get(i)));
                    mMakerCategoryList.add(category);
                }
                // 商品カテゴリリストを設定
                mProductCategoryList = new ArrayList<>();
                for (int i = 0; i < productCategory.getCategory2List().size(); i++) {
                    Category category = new Category();
                    category.setValue(productCategory.getCategory2List().get(i));
                    category.setKey(productCategory.getCategory2ValueList().get(i));
                    category.setCount(Integer.valueOf(productCategory.getCategory2NumList().get(i)));
                    mProductCategoryList.add(category);
                }
                // 初期リストの取得
                execInitialList();
            }
            @Override
            public void onError(int errorCode) {
                Toast.makeText(ProductListActivity.this, getString(R.string.ERR_DownloadCategoryFailure), Toast.LENGTH_SHORT).show();
                // 初期リストの取得
                execInitialList();
            }
        });
	}

    /**
     * 初期リストの取得
     */
    private void execInitialList() {
        int type = ((SakatailApplication) getApplication()).getProductCategoryType();
        int index = ((SakatailApplication) getApplication()).getProductCategoryIndex();
        switch (type) {
            case C.CAT_TYPE_PRODUCT_ALL:
                setButtonBackgroundColor(findViewById(R.id.AllButton));
                execPostByAll();
                break;
            case C.CAT_TYPE_PRODUCT_MAKER:
                setButtonBackgroundColor(findViewById(R.id.MakerButton));
                execPostByMaker(index);
                break;
            case C.CAT_TYPE_PRODUCT_CATEGORY:
                setButtonBackgroundColor(findViewById(R.id.CategoryButton));
                execPostByCategory(index);
                break;
            case C.CAT_TYPE_PRODUCT_HOLD:
                setButtonBackgroundColor(findViewById(R.id.HoldButton));
                execPostByHold();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // DBをクローズ
        if (mListViewAdapter != null) {
            mListViewAdapter.destroy();
        }
        if (mSQLitePersonalBelongings != null) {
            mSQLitePersonalBelongings.close();
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
    /**
     * 全てボタン押下処理
     * @param view          ボタンビュー
     */
	public void onAllButtonTapped(View view) {
		CustomLog.d(TAG, "onAllButtonTapped start");
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // カテゴリ選択情報を保存
        ((SakatailApplication) getApplication()).setProductCategoryType(C.CAT_TYPE_PRODUCT_ALL);
        ((SakatailApplication) getApplication()).setProductCategoryIndex(-1);
        // 商品一覧の取得
        execPostByAll();
	}

    /**
     * カクテル一覧の取得(全て)
     */
    private void execPostByAll() {
        HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(this);
        productList.setCategory(null, null);
        productList.post(this);
    }

    /**
     * 製造・販売ボタン押下処理
     * @param view          ボタンビュー
     */
	public void onMakerButtonTapped(View view) {
		CustomLog.d(TAG, "onMakerButtonTapped start");
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // 表示用カテゴリリスト生成
		CharSequence[] makerListItems = new CharSequence[mMakerCategoryList.size()];
		for (int i = 0; i < mMakerCategoryList.size(); i++) {
            makerListItems[i] = makeCategoryString(
                    mMakerCategoryList.get(i).getValue(),
                    String.valueOf(mMakerCategoryList.get(i).getCount())
            );
        }
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
		builder.setTitle("製造/販売会社を選択");
		builder.setItems(makerListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                // カテゴリ選択情報を保存
                ((SakatailApplication) getApplication()).setProductCategoryType(C.CAT_TYPE_PRODUCT_MAKER);
                ((SakatailApplication) getApplication()).setProductCategoryIndex(which);
                // 商品一覧の取得
                execPostByMaker(which);
			}
		});
		builder.show();
	}

    /**
     * カクテル一覧の取得(製造・販売)
     */
    private void execPostByMaker(int which) {
        HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(ProductListActivity.this);
        productList.setCategory(mMakerCategoryList.get(which).getKey(), null);
        productList.post(ProductListActivity.this);
    }

    /**
     * カテゴリボタン押下処理
     * @param view          ボタンビュー
     */
	public void onCategoryButtonTapped(View view) {
		CustomLog.d(TAG, "onCategoryButtonTapped start");
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // 表示用カテゴリリスト生成
		CharSequence[] categoryListItems = new CharSequence[mProductCategoryList.size()];
		for (int i = 0; i < mProductCategoryList.size(); i++) {
            categoryListItems[i] = makeCategoryString(
                    mProductCategoryList.get(i).getValue(),
                    String.valueOf(mProductCategoryList.get(i).getCount())
            );
		}
		// ダイアログを表示
		AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
		builder.setTitle("商品カテゴリを選択");
		builder.setItems(categoryListItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                // カテゴリ選択情報を保存
                ((SakatailApplication) getApplication()).setProductCategoryType(C.CAT_TYPE_PRODUCT_CATEGORY);
                ((SakatailApplication) getApplication()).setProductCategoryIndex(which);
                // 商品一覧の取得
                execPostByCategory(which);
			}
		});
		builder.show();
	}

    /**
     * カクテル一覧の取得(カテゴリ)
     */
    private void execPostByCategory(int which) {
        HttpRequestProductListByCategory productList = new HttpRequestProductListByCategory(ProductListActivity.this);
        productList.setCategory(null, mProductCategoryList.get(which).getKey());
        productList.post(ProductListActivity.this);
    }

    /**
     * 持っているボタン押下処理
     * @param view          ボタンビュー
     */
	public void onHoldButtonTapped(View view) {
		CustomLog.d(TAG, "onHoldButtonTapped start");
        // ボタンの背景色を変更
        setButtonBackgroundColor(view);
        // プログレスダイアログ表示
        mProgressDialog = startProgress(this);
        // カテゴリ選択情報を保存
        ((SakatailApplication) getApplication()).setProductCategoryType(C.CAT_TYPE_PRODUCT_HOLD);
        ((SakatailApplication) getApplication()).setProductCategoryIndex(-1);
		// 商品一覧の取得
        execPostByHold();
	}

    /**
     * カクテル一覧の取得(持っている)
     */
    private void execPostByHold() {
        List<HavingProduct> havingProductList = mSQLitePersonalBelongings.getProductList();
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

    /**
     * ボタン背景色変更
     * @param view          選択中ボタンビュー
     */
    private void setButtonBackgroundColor(View view) {
        // 背景色を全て透明に戻す
        findViewById(R.id.AllButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.MakerButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.CategoryButton).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.HoldButton).setBackgroundColor(Color.TRANSPARENT);
        // 選択中ボタンの背景色を変更
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }
/*--------------------------------------------------------------------------------------------------
	非同期コールバック処理
--------------------------------------------------------------------------------------------------*/
    /**
     * 商品一覧取得処理の正常終了コールバック
     * @param productList   商品一覧
     */
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
        // プログレスダイアログを閉じる
        stopProgress(mProgressDialog);
    }

    /**
     * 商品一覧取得処理の異常終了コールバック
     * @param errorCode     エラーコード
     */
    @Override
    public void onError(int errorCode) {
        CustomLog.d(TAG, "onError start [errorCode:" + errorCode + "]");
        // プログレスダイアログを閉じる
        stopProgress(mProgressDialog);
        switch (errorCode) {
            case C.RSP_CD_HTTPCONNECTIONERROR:
                Toast.makeText(this, getString(R.string.ERR_VolleyMessage_text), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, getString(R.string.ERR_DownloadCategoryFailure), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
