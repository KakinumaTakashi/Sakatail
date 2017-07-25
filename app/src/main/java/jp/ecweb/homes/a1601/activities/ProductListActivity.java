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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.network.HttpCategoryListener;
import jp.ecweb.homes.a1601.network.HttpProductCategory;
import jp.ecweb.homes.a1601.network.HttpProductListListener;
import jp.ecweb.homes.a1601.network.HttpRequestProductList;
import jp.ecweb.homes.a1601.network.ProductListCallbacks;
import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.network.ProductListListener;
import jp.ecweb.homes.a1601.managers.VolleyManager;
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
					    Intent intent = new Intent(ProductListActivity.this, A0203_ProductActivity.class);
					    intent.putExtra(C.EXTRA_KEY_PRODUCTITEMCODE, product.getItemCode());
					    intent.putExtra(C.EXTRA_KEY_PRODUCTID, product.getId());
					    intent.putExtra(C.EXTRA_KEY_MATERIALID, product.getMaterialID());
						startActivity(intent);
				    }
			    }
	    );
		// 絞り込み用カテゴリ一覧の取得
		HttpProductCategory categoryList = new HttpProductCategory(this);
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

//	    // 絞り込み用カテゴリ一覧の取得
//	    // WEB API Url
//	    String url = getString(R.string.server_URL) + "getProductCategory.php";
//	    CustomLog.d(TAG, "WEB API URL=" + url);
//
//	    // Volleyリクエストの作成
//	    ProductCategoryListener productCategoryListener = new ProductCategoryListener(this);
//
//	    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//			    Request.Method.GET,
//			    url,
//			    null,
//			    productCategoryListener,
//			    productCategoryListener
//	    );
//
//	    // リクエストの送信
//	    VolleyManager.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

	@Override
	protected void onStart() {
		CustomLog.d(TAG, "onStart start");

		super.onStart();

		// 商品一覧の取得
		HttpRequestProductList productList = new HttpRequestProductList(this);
		productList.setCategory(mCategory);
		productList.post(this);

//		// 商品リストの取得
//		// WEB API Url
//		String url = getString(R.string.server_URL) + "getProductList.php";
//
//		// POSTデータ作成
//		JSONObject postData = new JSONObject();
//		try {
//			postData.put("Maker", mCategory.getCategory1());
//			postData.put("MaterialId", mCategory.getCategory2());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		// Volleyリクエストの作成
//		ProductListListener productListListener = new ProductListListener(this);
//
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//				Request.Method.POST,
//				url,
//				postData,
//				productListListener,
//				productListListener
//		);
//
//		// リクエストの送信
//		VolleyManager.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
        HttpRequestProductList productList = new HttpRequestProductList(this);
        productList.setCategory(mCategory);
        productList.post(this);
//		// 商品リストの取得
//		// WEB API Url
//		String url = getString(R.string.server_URL) + "getProductList.php";
//
//		// POSTデータ作成
//		JSONObject postData = new JSONObject();
//		try {
//			postData.put("Maker", mCategory.getCategory1());
//			postData.put("MaterialId", mCategory.getCategory2());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		// Volleyリクエストの作成
//		ProductListListener productListListener = new ProductListListener(this);
//
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//				Request.Method.POST,
//				url,
//				postData,
//				productListListener,
//				productListListener
//		);
//
//		// リクエストの送信
//		VolleyManager.getInstance(this).addToRequestQueue(jsonObjectRequest);
//
//		// リストを先頭に戻す
//		ListView listView = (ListView) findViewById(R.id.listView);
//		listView.setSelection(0);
	}

	// 製造・販売
	public void onMakerButtonTapped(View view) {
		CustomLog.d(TAG, "onMakerButtonTapped start");

		CharSequence[] makerListItems = new CharSequence[mCategory.getCategory1List().size()];

		for (int i = 0; i < mCategory.getCategory1List().size(); i++) {
//			makerListItems[i] = mCategory.getCategory1List().get(i) + "  （" +
//					mCategory.getCategory1NumList().get(i) + " 件）";
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
                HttpRequestProductList productList = new HttpRequestProductList(ProductListActivity.this);
                productList.setCategory(mCategory);
                productList.post(ProductListActivity.this);
//				// 商品リストの取得
//				// WEB API Url
//				String url = getString(R.string.server_URL) + "getProductList.php";
//
//				// POSTデータ作成
//				JSONObject postData = new JSONObject();
//				try {
//					postData.put("Maker", mCategory.getCategory1());
//					postData.put("MaterialId", mCategory.getCategory2());
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				CustomLog.d(TAG, "PostRequest=" + postData.toString());
//
//				// Volleyリクエストの作成
//				ProductListListener productListListener =
//						new ProductListListener(ProductListActivity.this);
//
//				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//						Request.Method.POST,
//						url,
//						postData,
//						productListListener,
//						productListListener
//				);
//
//				// リクエストの送信
//				VolleyManager.getInstance(ProductListActivity.this).
//						addToRequestQueue(jsonObjectRequest);
//
//				// リストを先頭に戻す
//				ListView listView = (ListView) findViewById(R.id.listView);
//				listView.setSelection(0);
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
//			categoryListItems[i] = mCategory.getCategory2List().get(i) + "  （" +
//					mCategory.getCategory2NumList().get(i) + " 件）";
            categoryListItems[i] = makeCategoryString(
                    mCategory.getCategory1List().get(i),
                    mCategory.getCategory1NumList().get(i)
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
                HttpRequestProductList productList = new HttpRequestProductList(ProductListActivity.this);
                productList.setCategory(mCategory);
                productList.post(ProductListActivity.this);
//				// 商品リストの取得
//				// WEB API Url
//				String url = getString(R.string.server_URL) + "getProductList.php";
//
//				// POSTデータ作成
//				JSONObject postData = new JSONObject();
//				try {
//					postData.put("Maker", mCategory.getCategory1());
//					postData.put("MaterialId", mCategory.getCategory2());
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				CustomLog.d(TAG, "PostRequest=" + postData.toString());
//
//				// Volleyリクエストの作成
//				ProductListListener productListListener =
//						new ProductListListener(ProductListActivity.this);
//
//				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//						Request.Method.POST,
//						url,
//						postData,
//						productListListener,
//						productListListener
//				);
//
//				// リクエストの送信
//				VolleyManager.getInstance(ProductListActivity.this).
//						addToRequestQueue(jsonObjectRequest);
//
//				// リストを先頭に戻す
//				ListView listView = (ListView) findViewById(R.id.listView);
//				listView.setSelection(0);
			}
		});

		builder.show();
	}

	// 持っている
	public void onHoldButtonTapped(View view) {
		CustomLog.d(TAG, "onHoldButtonTapped start");

		// 所持商品IDを取得
		List<HavingProduct> havingProductList = mSQLitePersonalBelongings.getProductList();

		// 商品リストの取得
		// WEB API Url
		String url = getString(R.string.server_URL) + "getProductHaveList.php";

		// POST用のデータ作成
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < havingProductList.size(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(havingProductList.get(i).getProductID());
		}

		JSONObject postData = new JSONObject();
		try {
			postData.put("id", stringBuilder);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Volleyリクエストの作成
		ProductListListener productListListener =
				new ProductListListener(ProductListActivity.this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				postData,
				productListListener,
				productListListener
		);

		// リクエストの送信
		VolleyManager.getInstance(ProductListActivity.this).
				addToRequestQueue(jsonObjectRequest);

		// リストを先頭に戻す
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setSelection(0);
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
//	// カテゴリ一覧取得処理のコールバック処理
//	@Override
//	public void CategoryCallback(Category category) {
//		CustomLog.d(TAG, "CategoryCallback start");
//
//		// カテゴリ情報を設定
//		mCategory = category;
//	}
//
//	// 商品一覧取得処理のコールバック処理
//	@Override
//	public void ListResponseCallback(List<Product> productList) {
//		CustomLog.d(TAG, "ListResponseCallback start");
//
//		// ListView表示用商品一覧を更新
//		mProductList.clear();
//		mProductList.addAll(productList);
//
//		// ListViewに更新を通知
//		mListViewAdapter.notifyDataSetChanged();
//	}
//
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

