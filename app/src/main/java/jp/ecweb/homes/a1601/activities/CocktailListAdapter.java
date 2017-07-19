package jp.ecweb.homes.a1601.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.storage.FavoriteDAO;
import jp.ecweb.homes.a1601.storage.HavingProductDAO;
import jp.ecweb.homes.a1601.managers.VolleyManager;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.models.Favorite;
import jp.ecweb.homes.a1601.models.Recipe;

/**
 * カクテル一覧用アダプタ
 */
public class CocktailListAdapter extends ArrayAdapter<Cocktail> {

	// メンバ変数
	private LayoutInflater mInflater;               // セルレイアウト
	private int mResourceId;                        // セルに表示するリソースID

	private FavoriteDAO mFavoriteDAO;               // SQLite操作用
    private HavingProductDAO mHavingProductDAO;
    private TextAppearanceSpan mTextAppearanceSpan;
	private List<Cocktail> mCocktailList;            // カクテル一覧

    /**
     * セルのビュー保存用ビューホルダー
     */
	private class ViewHolder {
		TextView cocktailNameView;
		NetworkImageView thumbnailImageView;
		TextView recipeView;
		ToggleButton favoriteButton;
	}

	/**
	 * コンストラクタ
	 * @param context       コンテキスト
	 * @param resource      リソースID
	 * @param cocktailList  カクテル情報リスト
	 */
	CocktailListAdapter(Context context, int resource, List<Cocktail> cocktailList) {
		super(context, resource, cocktailList);

		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResourceId = resource;
		mCocktailList = cocktailList;
		mFavoriteDAO = new FavoriteDAO(context);
        mHavingProductDAO = new HavingProductDAO(context);
        mTextAppearanceSpan = new TextAppearanceSpan(context.getApplicationContext(), R.style.ListViewHaveItem);
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
	public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
		ViewHolder holder;

		// 再利用セルオブジェクトがnullの場合はセルオブジェクト作成
		if (convertView == null) {
			// セルにリソースを展開
			convertView = mInflater.inflate(mResourceId, null);
			// ビューホルダーに各ビューを保存
			holder = new ViewHolder();
			holder.cocktailNameView = convertView.findViewById(R.id.cocktailNameView);
			holder.thumbnailImageView = convertView.findViewById(R.id.cocktailImageView);
			holder.recipeView = convertView.findViewById(R.id.recipeView);
			holder.favoriteButton = convertView.findViewById(R.id.favoriteButton);
			// タグにビューホルダーのインスタンスを保存
			convertView.setTag(holder);
		} else {
			// タグからビューホルダーのインスタンスを取得
			holder = (ViewHolder) convertView.getTag();
		}

		/* カクテル情報 */
		Cocktail item = mCocktailList.get(position);
		// カクテル名
		holder.cocktailNameView.setText(item.getName());
		// サムネイル
		String thumbnailUrl = item.getThumbnailUrl();
		ImageLoader imageLoader = VolleyManager.getInstance(parent.getContext()).getImageLoader();
		holder.thumbnailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		holder.thumbnailImageView.setErrorImageResId(R.drawable.nothumbnail);
		if (thumbnailUrl.equals("")) {
			holder.thumbnailImageView.setImageUrl(null, imageLoader);
		} else {
			holder.thumbnailImageView.setImageUrl(thumbnailUrl, imageLoader);
		}
		/* レシピ */
        SpannableStringBuilder recipeString = new SpannableStringBuilder();
        List<Recipe> recipes = item.getRecipes();
        for (Recipe recipe : recipes) {
            if (recipeString.length() > 0) {
                recipeString.append("／");
            }
            int startPos = recipeString.length();
            recipeString.append(recipe.getMatelialName());
            if (mHavingProductDAO.ExistMaterialID(recipe.getMatelialID())) {
                // 所持商品と一致した場合は文字色を設定
                recipeString.setSpan(
                        mTextAppearanceSpan,
                        startPos,
                        recipeString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
        holder.recipeView.setText(recipeString);
        /* お気に入り */
		// お気に入りテーブルを検索しトグルのチェックを設定
		String cocktailId = item.getId();
		if (mFavoriteDAO.ExistCocktailId(cocktailId)) {
			holder.favoriteButton.setChecked(true);
		} else {
			holder.favoriteButton.setChecked(false);
		}
		// お気に入りボタンにカクテルIDをタグ付け
		holder.favoriteButton.setTag(R.string.TAG_CocktailID_Key, cocktailId);
		// お気に入りボタンタップ時のリスナーを登録
		holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToggleButton btn = (ToggleButton) view;
				if (btn.isChecked()) {
					// ボタンがONになった場合はお気に入りテーブルにカクテルIDを追加
					Favorite favorite = new Favorite();
					favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));
					mFavoriteDAO.insertFavorite(favorite);
				} else {
					// ボタンがOFFになった場合はお気に入りテーブルからカクテルIDを削除
					Favorite favorite = new Favorite();
					favorite.setCocktailId((String) btn.getTag(R.string.TAG_CocktailID_Key));
					mFavoriteDAO.deleteFavorite(favorite);
				}
			}
		});
		return convertView;
	}
}