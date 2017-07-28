package jp.ecweb.homes.a1601.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.SakatailApplication;
import jp.ecweb.homes.a1601.activities.CocktailListActivity;
import jp.ecweb.homes.a1601.models.Category;
import jp.ecweb.homes.a1601.models.CocktailCategory;
import jp.ecweb.homes.a1601.network.HttpCocktailCategoryListener;
import jp.ecweb.homes.a1601.network.HttpRequestCocktailCategory;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * カクテル一覧 フィルタ設定ダイアログ
 */
public class CocktailListFilterFragment extends DialogFragment {

    private static final String TAG = CocktailListFilterFragment.class.getSimpleName();

    private FilterListener mListener;
    private int mType;
    private int mIndex;
    private List<Category> mJapaneseCategoryList;					// 頭文字カテゴリリスト
    private List<Category> mBaseCategoryList;						// ベースカテゴリリスト


    public interface FilterListener {
        void onCloseButtonTapped(DialogFragment dialog, int type, String key);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (FilterListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 絞り込み用カテゴリ一覧の取得
        HttpRequestCocktailCategory categoryList = new HttpRequestCocktailCategory(this.getActivity());
        categoryList.get(new HttpCocktailCategoryListener() {
            @Override
            public void onSuccess(CocktailCategory cocktailCategory) {
                // 頭文字カテゴリ情報を設定
                mJapaneseCategoryList = new ArrayList<>();
                for (int i = 0; i < cocktailCategory.getCategory1List().size(); i++) {
                    Category category = new Category();
                    category.setValue(cocktailCategory.getCategory1List().get(i));
                    category.setCount(Integer.valueOf(cocktailCategory.getCategory1NumList().get(i)));
                    mJapaneseCategoryList.add(category);
                }
                // ベースカテゴリ情報を設定
                mBaseCategoryList = new ArrayList<>();
                for (int i = 0; i < cocktailCategory.getCategory2List().size(); i++) {
                    Category category = new Category();
                    category.setValue(cocktailCategory.getCategory2List().get(i));
                    category.setCount(Integer.valueOf(cocktailCategory.getCategory2NumList().get(i)));
                    mBaseCategoryList.add(category);
                }
                // 初期リストの取得
//                execInitialList();
            }
            @Override
            public void onError(int errorCode) {
                Toast.makeText(CocktailListFilterFragment.this.getActivity(),
                        getString(R.string.ERR_DownloadCategoryFailure), Toast.LENGTH_SHORT).show();
                // 初期リストの取得
//                execInitialList();
            }
        });

        return super.onCreateDialog(savedInstanceState);
    }

    public void setType(int type) {
        mType = type;
        CustomLog.d(TAG, "Set type [type:" + type + "]");
    }
    public void setIndex(int index) {
        mIndex = index;
        CustomLog.d(TAG, "Set index [index:" + index + "]");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cocktail_category, container, false);

        RadioButton radio_All = view.findViewById(R.id.radio_All);
        RadioButton radio_JapaneseSyllabary = view.findViewById(R.id.radio_JapaneseSyllabary);
        RadioButton radio_Base = view.findViewById(R.id.radio_Base);
        RadioButton radio_Favorite = view.findViewById(R.id.radio_Favorite);
        switch (mType) {
            case C.CAT_TYPE_COCKTAIL_ALL:
                radio_All.setChecked(true);
                break;
            case C.CAT_TYPE_COCKTAIL_JAPANESE:
                radio_JapaneseSyllabary.setChecked(true);
                break;
            case C.CAT_TYPE_COCKTAIL_BASE:
                radio_Base.setChecked(true);
                break;
            case C.CAT_TYPE_COCKTAIL_FAVORITE:
                radio_Favorite.setChecked(true);
                break;
        }

        // 全て
        view.findViewById(R.id.button_Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseButtonTapped(CocktailListFilterFragment.this, mType, "");
            }
        });
//        // 五十音
//        view.findViewById(R.id.JapaneseSyllabaryButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.onJapaneseSyllabaryButtonTapped(CocktailListFilterFragment.this);
//            }
//        });
//        // ベース
//        view.findViewById(R.id.BaseButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.onBaseButtonTapped(CocktailListFilterFragment.this);
//            }
//        });
//        // お気に入り
//        view.findViewById(R.id.favoriteButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.onFavoriteButtonTapped(CocktailListFilterFragment.this);
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            lp.width = (int) (metrics.widthPixels * 0.8);
            dialog.getWindow().setAttributes(lp);
        }
    }
}
