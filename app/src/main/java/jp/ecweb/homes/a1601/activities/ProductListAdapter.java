package jp.ecweb.homes.a1601.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jp.ecweb.homes.a1601.storage.SQLitePersonalBelongings;
import jp.ecweb.homes.a1601.models.HavingProduct;
import jp.ecweb.homes.a1601.models.Product;
import jp.ecweb.homes.a1601.managers.VolleyManager;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 材料からカクテルを探すビュー用アダプタ
 */
public class ProductListAdapter extends ArrayAdapter<Product> {

	private static final String TAG = ProductListAdapter.class.getSimpleName();

	// メンバ変数
	private LayoutInflater mInflater;               // セルレイアウト
	private int mResourceId;                        // セルに表示するリソースID

	private SQLitePersonalBelongings mSQLitePersonalBelongings;     // SQLite操作用

	private List<Product> mProductList;              // 製品一覧

	/**
	 * セルのビュー保持用
	 */
	private class ViewHolder {
		NetworkImageView thumbnailImageView;
		TextView categoryTextView;
		TextView productNameView;
		TextView makerTextView;
		ToggleButton productHavingButton;
	}

	/**
	 * コンストラクタ
	 * @param context       	コンテキスト
	 * @param resource      	リソースID
	 * @param productList  	商品情報リスト
	 */
	ProductListAdapter(Context context, int resource, List<Product> productList) {
		super(context, resource, productList);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResourceId = resource;
		mProductList = productList;
		mSQLitePersonalBelongings = new SQLitePersonalBelongings(context);
	}

	/**
	 * アダプタ終了処理
	 */
	void destroy() {
		if (mSQLitePersonalBelongings != null) {
			mSQLitePersonalBelongings.close();
			CustomLog.d(TAG, "SQLitePersonalBelongings closed.");
		}
	}

	/**
	 * セル描画
	 * @param position      リスト上の位置
	 * @param convertView   再利用セルオブジェクト(nullあり)
	 * @param parent        親ビュー
	 * @return              セルオブジェクト
	 */
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// セルにリソースを展開
			convertView = mInflater.inflate(mResourceId, null);
			// ビューホルダーに各ビューを保存
			holder = new ViewHolder();
			holder.thumbnailImageView = convertView.findViewById(R.id.ProductImageView);
			holder.categoryTextView = convertView.findViewById(R.id.CategoryTextView);
			holder.productNameView = convertView.findViewById(R.id.ProductNameView);
			holder.makerTextView = convertView.findViewById(R.id.MakerTextView);
			holder.productHavingButton = convertView.findViewById(R.id.PuductHavingButton);
			// タグにビューホルダーのインスタンスを保存
			convertView.setTag(holder);
		} else {
			// タグからビューホルダーのインスタンスを取得
			holder = (ViewHolder) convertView.getTag();
		}

		// 各ビューにカクテル情報を設定
		Product item = mProductList.get(position);
		// サムネイル
		ImageLoader imageLoader = VolleyManager.getInstance(parent.getContext()).getImageLoader();
		holder.thumbnailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		holder.thumbnailImageView.setErrorImageResId(R.drawable.nothumbnail);
		if (item.getThumbnailURL() != null) {
            if (item.getThumbnailURL().equals("")) {
                holder.thumbnailImageView.setImageUrl(null, imageLoader);
            } else {
                holder.thumbnailImageView.setImageUrl(item.getThumbnailURL(), imageLoader);
            }
        }
		// メーカー
		StringBuilder stringBuilder = new StringBuilder();
		if (item.getCategory1() != null) {
            stringBuilder.append(item.getCategory1());
        }
        if (item.getCategory2() != null) {
            if (!item.getCategory2().equals("")) {
                stringBuilder.append("/");
                stringBuilder.append(item.getCategory2());
            }
        }
        if (item.getCategory3() != null) {
            if (!item.getCategory3().equals("")) {
                stringBuilder.append("/");
                stringBuilder.append(item.getCategory3());
            }
        }
		holder.categoryTextView.setText(stringBuilder.toString());
		// 製品名
        if (item.getName() != null) {
            holder.productNameView.setText(item.getName());
        }
		// メーカー
        if (item.getMaker() != null) {
            if (item.getMaker().equals("")) {
                holder.makerTextView.setVisibility(View.GONE);
            } else {
                holder.makerTextView.setText(item.getMaker());
            }
        }
		// 持っているボタンの初期値を所持製品DBから取得
		// 製品IDをキーにDBを検索
		final HavingProduct product = new HavingProduct();
		product.setProductID(item.getId());
		product.setMaterialID(item.getMaterialID());
		// 製品IDがDBに登録されていたらボタンの初期値をON
		if (mSQLitePersonalBelongings.ExistProductID(product.getProductID())) {
			holder.productHavingButton.setChecked(true);
		} else {
			holder.productHavingButton.setChecked(false);
		}
		// 持っているボタンに製品ID・材料IDをタグ付け
		holder.productHavingButton.setTag(R.string.TAG_ProductID_Key, product.getProductID());
		holder.productHavingButton.setTag(R.string.TAG_MaterialID_Key, product.getMaterialID());
		// 持っているボタンタップ時のリスナーを登録
		holder.productHavingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;

				if (btn.isChecked()) {
					// ボタンがONになった場合 所持製品テーブルに製品ID・材料IDを登録
					mSQLitePersonalBelongings.insertProduct(product);
				} else {
					// ボタンがOFFになった場合 所持製品テーブルから製品ID・材料IDを削除
					mSQLitePersonalBelongings.deleteProduct(product);
				}
			}
		});
		return convertView;
	}
}
