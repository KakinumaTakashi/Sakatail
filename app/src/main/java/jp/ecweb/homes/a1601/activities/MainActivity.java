package jp.ecweb.homes.a1601.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.utils.CustomLog;
import jp.ecweb.homes.a1601.utils.ExternalServicesLoader;
import jp.ecweb.homes.a1601.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

/*--------------------------------------------------------------------------------------------------
	Activityイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CustomLog.d(TAG, "onCreate start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a0101__main);

		// 広告を表示
		ExternalServicesLoader.loadAdMob(findViewById(R.id.adView));
	    // 楽天ウェブサービスクレジット
        ExternalServicesLoader.loadRakutenCredit(findViewById(R.id.RakutenCreditView));
	    // バージョン表示
        String versionName = "version : " + Utils.getAppVersion(this);
        TextView versionNameText = (TextView) findViewById(R.id.versionNameText);
        versionNameText.setText(versionName);
    }
/*--------------------------------------------------------------------------------------------------
	メニューイベント処理
--------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// リソースの登録
        getMenuInflater().inflate(R.menu.menu_a0101__main, menu);

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
	ボタンタップ処理
--------------------------------------------------------------------------------------------------*/
    /**
     * 材料からカクテルを探す
     * @param view  Button View
     */
    public void onMaterialToCocktailButtonTapped(View view) {
        Intent intent = new Intent(this, A0201_ProductToCocktailActivity.class);
        startActivity(intent);
    }

    /**
     * カクテル一覧
     * @param view  Button View
     */
    public void onCocktailListButton(View view) {
        Intent intent = new Intent(this, CocktailListActivity.class);
		startActivity(intent);
    }
}
