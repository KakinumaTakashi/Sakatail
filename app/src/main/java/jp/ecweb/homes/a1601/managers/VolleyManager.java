package jp.ecweb.homes.a1601.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import jp.ecweb.homes.a1601.utils.CustomLog;


/**
 * Volley管理クラス
 */
public class VolleyManager {

	private static final String TAG = VolleyManager.class.getSimpleName();

	// メンバ変数
	private static VolleyManager ourInstance;			// インスタンス
	private RequestQueue mRequestQueue;					// リクエストキュー
	private ImageLoader mImageLoader;					// イメージローダー

	// 固定値
	private static final Object TAG_REQUEST_QUEUE =	new Object();       // キャンセル用タグ

/*--------------------------------------------------------------------------------------------------
	基本処理
--------------------------------------------------------------------------------------------------*/
    /**
     * インスタンスの取得・生成
     * @param context       コンテキスト
     * @return              インスタンス
     */
	public static synchronized VolleyManager getInstance(Context context) {
		// インスタンスが生成されていない場合は作成する
		if (ourInstance == null) {
			ourInstance = new VolleyManager(context.getApplicationContext());
			CustomLog.d(TAG, "Create volley instance");
		}
		return ourInstance;
	}

    /**
     * コンストラクタ
     * @param context       コンテキスト
     */
	private VolleyManager(Context context) {
		// リクエストキューをメンバ変数に格納
        mRequestQueue = Volley.newRequestQueue(context);
		// イメージローダーのインスタンスをメンバ変数に格納
		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap> cache = new LruCache<>(20);

					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}

    /**
     * リクエストキューへのリクエスト追加(非同期)
     * @param request       リクエストデータ
     * @param <T>           リクエストデータの型
     */
	public <T> void addToRequestQueue(Request<T> request) {
	    if (mRequestQueue != null) {
            request.setTag(TAG_REQUEST_QUEUE);
            mRequestQueue.add(request);
        } else {
	        CustomLog.e(TAG, "Request queue is null");
        }
	}

    /**
     * イメージローダーの取得
     * @return              イメージローダー
     */
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

    /**
     * リクエストの停止
     */
    @SuppressWarnings("UnusedDeclaration")
	void cancelAll() {
		if (mRequestQueue != null) {
            CustomLog.d(TAG, "Request cancel");
			mRequestQueue.cancelAll(TAG_REQUEST_QUEUE);
		}
	}

    /**
     * リクエストの中断
     */
    @SuppressWarnings("UnusedDeclaration")
	void stop() {
		if (mRequestQueue != null) {
            CustomLog.d(TAG, "Request stop");
			mRequestQueue.stop();
		}
	}

    /**
     * リクエストの開始
     */
    @SuppressWarnings("UnusedDeclaration")
	void start() {
		if (mRequestQueue != null) {
		    CustomLog.d(TAG, "Request start");
			mRequestQueue.start();
		}
	}
}
