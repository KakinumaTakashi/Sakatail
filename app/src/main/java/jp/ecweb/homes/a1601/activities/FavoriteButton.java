package jp.ecweb.homes.a1601.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import jp.ecweb.homes.a1601.storage.SQLiteFavorite;

/**
 * お気に入りボタン (カスタムToggleButton)
 */
class FavoriteButton extends ToggleButton {

//    private static final String TAG = FavoriteButton.class.getSimpleName();

    private SQLiteFavorite mSQLiteFavorite;

    /**
     * コンストラクタ
     * @param context       The Context the Button is running in, through which it can access the current theme, resources, etc.
     */
    public FavoriteButton(Context context) {
        super(context);
        initialize(context);
    }

    /**
     * コンストラクタ
     * @param context       The Context the Button is running in, through which it can access the current theme, resources, etc.
     * @param attrs         The attributes of the XML Button tag being used to inflate the view.
     */
    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    /**
     * コンストラクタ
     * @param context       The Context the Button is running in, through which it can access the current theme, resources, etc.
     * @param attrs         The attributes of the XML Button tag being used to inflate the view.
     * @param defStyleAttr  The resource identifier of an attribute in the current theme whose value is the the resource id of a style.
     *                       The specified style’s attribute values serve as default values for the button. Set this parameter to 0 to avoid use of default values.
     */
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    /**
     * コンストラクタ
     * @param context       The Context the Button is running in, through which it can access the current theme, resources, etc.
     * @param attrs         The attributes of the XML Button tag being used to inflate the view.
     * @param defStyleAttr  The resource identifier of an attribute in the current theme whose value is the the resource id of a style.
     *                       The specified style’s attribute values serve as default values for the button. Set this parameter to 0 to avoid use of default values.
     * @param defStyleRes   The identifier of a style resource that supplies default values for the button, used only if defStyleAttr is 0 or cannot be found in the theme.
     *                       Set this parameter to 0 to avoid use of default values.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("UnusedDeclaration")
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    /**
     * 初期化処理
     * @param context       コンテキスト
     */
    private void initialize(Context context) {
        mSQLiteFavorite = new SQLiteFavorite(context);
    }

    /**
     * チェック状態設定
     * @param cocktailId    カクテルID
     */
    void setChecked(String cocktailId) {
        if (mSQLiteFavorite.ExistCocktailId(cocktailId)) {
            super.setChecked(true);
        } else {
            super.setChecked(false);
        }
    }
}
