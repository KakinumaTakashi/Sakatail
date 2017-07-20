package jp.ecweb.homes.a1601.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.ecweb.homes.a1601.models.Recipe;
import jp.ecweb.homes.a1601.R;

/**
 * レシピ一覧用アダプタ
 */
public class RecipeListAdapter extends ArrayAdapter<Recipe> {

//    private static final String TAG = RecipeListAdapter.class.getSimpleName();

    // メンバ変数
	private LayoutInflater mInflater;
	private List<Recipe> mRecipeList;
	private int resourceId;

    /**
     * コンストラクタ
     * @param context       コンテキスト
     * @param resource      リソースID
     * @param recipeList    レシピ情報リスト
     */
	RecipeListAdapter(Context context, int resource, List<Recipe> recipeList) {
		super(context, resource, recipeList);

		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRecipeList = recipeList;
		resourceId = resource;
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
		View view;

        // 再利用セルオブジェクトがnullの場合はセルオブジェクト作成
		if (convertView == null) {
            // セルにリソースを展開
			view = mInflater.inflate(this.resourceId, null);
		} else {
			view = convertView;
		}

		/* レシピ情報 */
		Recipe recipe = mRecipeList.get(position);
		// 素材名
		TextView matelialNameView = view.findViewById(R.id.MatelialNameView);
		matelialNameView.setText(recipe.getMatelialName());
		// 分量(0の場合は空欄)
        String quantityText;
		TextView quantityView = view.findViewById(R.id.QuantityView);
		if (recipe.getQuantity() == 0) {
			quantityText = recipe.getUnit();
		} else {
			quantityText = String.valueOf(recipe.getQuantity()) + " " + recipe.getUnit();
		}
        quantityView.setText(quantityText);
		return view;
	}
}
