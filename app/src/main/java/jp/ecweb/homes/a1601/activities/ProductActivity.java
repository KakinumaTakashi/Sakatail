package jp.ecweb.homes.a1601.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.models.RakutenItem;
import jp.ecweb.homes.a1601.network.HttpRakutenListener;
import jp.ecweb.homes.a1601.network.HttpRequestRakuten;
import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.managers.VolleyManager;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.models.RakutenResponse;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;

public class ProductActivity extends AppCompatActivity {

	private static final String TAG = ProductActivity.class.getSimpleName();

	// メンバ変数
	private String mProductId;
	private String mMaterialId;

	private SQLitePersonalBelongings mSQLitePersonalBelongings;

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CustomLog.d(TAG, "onCreate start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		// 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
		// インテントから楽天商品コードを取得
		Intent intent = getIntent();
		String itemCode = intent.getStringExtra(C.EXTRA_KEY_PRODUCTITEMCODE);
		mProductId = intent.getStringExtra(C.EXTRA_KEY_PRODUCTID);
		mMaterialId = intent.getStringExtra(C.EXTRA_KEY_MATERIALID);
		// メンバ変数の初期化
		mSQLitePersonalBelongings = new SQLitePersonalBelongings(this);
        // 楽天商品検索APIから商品情報を取得
        HttpRequestRakuten rakuten = new HttpRequestRakuten(this);
        rakuten.get(itemCode, new HttpRakutenListener() {
            @Override
            public void onSuccess(RakutenResponse rakutenResponse) {
                initActivity(rakutenResponse);
            }
            @Override
            public void onError(int errorCode) {
                switch (errorCode) {
                    case C.RSP_CD_ITEMNOTFOUND:
                        Toast.makeText(ProductActivity.this, getString(R.string.ERR_ItemNotFound), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        Toast.makeText(ProductActivity.this, getString(R.string.ERR_VolleyMessage_text), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
	}

    @Override
    protected void onDestroy() {
        // DBをクローズ
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
		getMenuInflater().inflate(R.menu.menu_product, menu);
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
	// 購入
	public void onRakutenShopButtonTapped(View view) {
		CustomLog.d(TAG, "onRakutenShopButtonTapped start");
		// ブラウザでURLを開く
        String affiliateUrl = (String) findViewById(R.id.RakutenShopButton).getTag(R.string.TAG_AffiliateUrl_Key);
		Uri uri = Uri.parse(affiliateUrl);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
/*--------------------------------------------------------------------------------------------------
    Activity初期化処理
--------------------------------------------------------------------------------------------------*/
    private void initActivity(RakutenResponse rakutenResponse) {
        CustomLog.d(TAG, "initActivity start");
        RakutenItem item = rakutenResponse.getRakutenItems().get(0);
        // 商品名
        ((TextView) findViewById(R.id.itemNameView)).setText(item.getItemName());
        // 商品画像
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.ProductImageView);
        ImageLoader imageLoader = VolleyManager.getInstance(this).getImageLoader();
        imageView.setImageUrl(item.getMediumImageUrls().get(0), imageLoader);
        imageView.setErrorImageResId(R.drawable.nothumbnail);
        // 商品説明文
        ((TextView) findViewById(R.id.itemCaptionView)).setText(item.getItemCaption());
        // 店舗名
        ((TextView) findViewById(R.id.shopNameView)).setText(item.getShopName());
        // 商品コード
        ((TextView) findViewById(R.id.itemCodeView)).setText(item.getItemCode());
        // 持っているボタン
        ToggleButton productHavingButton = (ToggleButton) findViewById(R.id.PuductHavingButton);
        // 商品IDがテーブルに登録されていたらボタンの初期値をON
        if (mSQLitePersonalBelongings.ExistProductID(mProductId)) {
            productHavingButton.setChecked(true);
        } else {
            productHavingButton.setChecked(false);
        }
        // 持っているボタンに商品ID・材料IDをタグ付け
        productHavingButton.setTag(R.string.TAG_ProductID_Key, mProductId);
        productHavingButton.setTag(R.string.TAG_MaterialID_Key, mMaterialId);
        // 持っているボタンタップ時のリスナーを登録
        productHavingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton btn = (ToggleButton) view;
                HavingProduct product = new HavingProduct();
                product.setProductID((String) btn.getTag(R.string.TAG_ProductID_Key));
                product.setMaterialID((String) btn.getTag(R.string.TAG_MaterialID_Key));
                if (btn.isChecked()) {
                    // ボタンがONになった場合 所持商品テーブルに商品ID・材料IDを登録
                    mSQLitePersonalBelongings.insertProduct(product);
                } else {
                    // ボタンがOFFになった場合 所持商品テーブルから商品ID・材料IDを削除
                    mSQLitePersonalBelongings.deleteProduct(product);
                }
            }
        });

        findViewById(R.id.RakutenShopButton).setTag(R.string.TAG_AffiliateUrl_Key, item.getAffiliateUrl());
    }
}
