package jp.ecweb.homes.a1601.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.Locale;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.managers.VolleyManager;
import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.network.HttpRequestCocktail;
import jp.ecweb.homes.a1601.network.HttpCocktailListener;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;


public class CocktailActivity extends AppCompatActivity {

	private static final String TAG = CocktailActivity.class.getSimpleName();

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CustomLog.d(TAG, "onCreate start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cocktail);

		// 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
		// インテントからカクテルIDを取得
		Intent intent = getIntent();
		String selectedCocktailID = intent.getStringExtra(C.EXTRA_KEY_COCKTAILID);
		// サーバーからカクテル情報を取得
		HttpRequestCocktail cocktail = new HttpRequestCocktail(this);
		cocktail.get(selectedCocktailID, new HttpCocktailListener() {
            @Override
            public void onSuccess(Cocktail cocktail) {
                initActivity(cocktail);
            }
            @Override
            public void onError(int errorCode) {
                Toast.makeText(CocktailActivity.this, getString(R.string.ERR_VolleyMessage_text), Toast.LENGTH_SHORT).show();
            }
        });
	}
/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
		getMenuInflater().inflate(R.menu.menu_cocktail, menu);
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
	Activity初期化処理
--------------------------------------------------------------------------------------------------*/
	private void initActivity(Cocktail cocktail) {
		CustomLog.d(TAG, "initActivity start");

		// カクテル名
		TextView cocktailNameView = (TextView) findViewById(R.id.A0302_CocktailNameView);
		cocktailNameView.setText(cocktail.getName());
		// カクテル写真
		String photoUrl = cocktail.getPhotoUrl();
		NetworkImageView cocktailImageView =
				(NetworkImageView) findViewById(R.id.A0302_CocktailImageView);
		ImageLoader imageLoader =
				VolleyManager.getInstance(getApplicationContext()).getImageLoader();
		cocktailImageView.setDefaultImageResId(R.drawable.nothumbnail);
		cocktailImageView.setErrorImageResId(R.drawable.noimage);
		CustomLog.d(TAG, "PhotoUrl:" + cocktail.getPhotoUrl());
		if (photoUrl.equals("")) {
			cocktailImageView.setImageUrl(null, imageLoader);
		} else {
			cocktailImageView.setImageUrl(photoUrl, imageLoader);
		}
		// 製法
		TextView methodsTextView = (TextView) findViewById(R.id.A0302_MethodsTextView);
		methodsTextView.setText(cocktail.getMethod());
		// グラス
		TextView grassTextView = (TextView) findViewById(R.id.A0302_GrassTextView);
		grassTextView.setText(cocktail.getGrass());
		// アルコール度数
		TextView alcoholDegreeTextView = (TextView) findViewById(R.id.A0302_AlcoholDegreeTextView);
		alcoholDegreeTextView.setText(String.format(Locale.US, "%.1f ％", cocktail.getAlcoholDegree()));
		// 著作権表記
		TextView copyrightText = (TextView) findViewById(R.id.copyrightText);
		if (!cocktail.getCopylight().equals("")) {
			String copylight = getResources().getText(R.string.Photo_text) + ":" + cocktail.getCopylight();
			copyrightText.setText(copylight);
		}
		// 作り方
		TextView howToTextView = (TextView) findViewById(R.id.A0302_HowToTextView);
		howToTextView.setText(cocktail.getHowTo().replaceAll("\\n", "\n"));
		// お気に入り
		FavoriteButton favoriteButton = (FavoriteButton) findViewById(R.id.favoriteButton);
        if (cocktail.getId() != null) {
            // チェックを設定
            favoriteButton.setChecked(cocktail.getId());
            // お気に入りボタンのタグにカクテルIDを設定
            favoriteButton.setTag(R.string.TAG_CocktailID_Key, cocktail.getId());
            // お気に入りボタンタップ時のリスナーを登録
            favoriteButton.setOnClickListener(new FavoriteButtonListener());
        }
		// ListViewのアダプターを登録
		RecipeListAdapter adapter = new RecipeListAdapter(this,
				R.layout.activity_recipe_list_item,
				cocktail.getRecipes());
		ListView recipeView = (ListView) findViewById(R.id.RecipeListView);
		recipeView.setAdapter(adapter);
	}
}
