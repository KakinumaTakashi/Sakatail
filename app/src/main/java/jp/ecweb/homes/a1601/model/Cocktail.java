package jp.ecweb.homes.a1601.model;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.R;
import jp.ecweb.homes.a1601.dao.HavingProductDAO;

/**
 * カクテル情報クラス
 */
public class Cocktail {

	// メンバ変数
    private String id;					// カクテルID
    private String name;				// カクテル名
    private String photoUrl;			// 写真
    private String thumbnailUrl;		// サムネイルURL
	private String Method;              // 製法
	private String Grass;               // グラス
	private float AlcoholDegree;        // アルコール度数
//    private String detail;		    	// 詳細
//    private String recipeId;			// レシピID
    private List<Recipe> Recipes;
	private SpannableStringBuilder RecipeStringBuffer;
	private String HowTo;               // 作り方
	private String Copylight;           // 著作権表記

    /**
     * コンストラクタ
     */
    public Cocktail() {}

    /**
     * コンストラクタ
     * @param jsonObject        レスポンスデータ部
     */
    public Cocktail(JSONObject jsonObject) {
        parseJSON(jsonObject);
    }
/*--------------------------------------------------------------------------------------------------
	Getter / Setter
--------------------------------------------------------------------------------------------------*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
/*
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
*/
/*
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
*/
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public List<Recipe> getRecipes() {
		return Recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.Recipes = recipes;
	}

	public String getMethod() {
		return Method;
	}

	public void setMethod(String method) {
		Method = method;
	}

	public String getGrass() {
		return Grass;
	}

	public void setGrass(String grass) {
		Grass = grass;
	}

	public float getAlcoholDegree() {
		return AlcoholDegree;
	}

	public void setAlcoholDegree(float alcoholDegree) {
		AlcoholDegree = alcoholDegree;
	}

	public String getHowTo() {
		return HowTo;
	}

	public void setHowTo(String howTo) {
		HowTo = howTo;
	}

	public SpannableStringBuilder getRecipeStringBuffer() {
		return RecipeStringBuffer;
	}

	public void setRecipeStringBuffer(SpannableStringBuilder recipeStringBuffer) {
		RecipeStringBuffer = recipeStringBuffer;
	}

	public String getCopylight() {
		return Copylight;
	}

	public void setCopylight(String copylight) {
		Copylight = copylight;
	}

    /**
     * レスポンスデータ部パース処理
     * @param jsonCocktailObject    レスポンスデータ部
     */
	private void parseJSON(JSONObject jsonCocktailObject) {
        try {
            // カクテル情報部
            setId(jsonCocktailObject.getString("ID"));
            setName(jsonCocktailObject.getString("NAME"));
            setThumbnailUrl(jsonCocktailObject.getString("THUMBNAILURL"));
            // レシピ情報部
            List<Recipe> recipeList = new ArrayList<>();
            SpannableStringBuilder recipeString = new SpannableStringBuilder();
            JSONArray jsonRecipeArray = jsonCocktailObject.getJSONArray("RECIPES");
            if (jsonRecipeArray.length() > 0) {
                // TODO 所持マークは保留
//                HavingProductDAO havingProductDAO = new HavingProductDAO(mContext);
                for (int j = 0; j < jsonRecipeArray.length(); j++) {
                    Recipe recipe = new Recipe();
//                    TextAppearanceSpan textAppearanceSpan =	new TextAppearanceSpan(
//                            mContext.getApplicationContext(), R.style.ListViewHaveItem);
                    JSONObject jsonRecipeObject = jsonRecipeArray.getJSONObject(j);
                    recipe.setId(jsonRecipeObject.getString("ID"));
                    recipe.setCocktailID(jsonRecipeObject.getString("COCKTAILID"));
                    recipe.setMatelialID(jsonRecipeObject.getString("MATERIALID"));
                    recipe.setCategory1(jsonRecipeObject.getString("CATEGORY1"));
                    recipe.setCategory2(jsonRecipeObject.getString("CATEGORY2"));
                    recipe.setCategory3(jsonRecipeObject.getString("CATEGORY3"));
                    recipe.setMatelialName(jsonRecipeObject.getString("NAME"));
                    recipe.setQuantity(jsonRecipeObject.getInt("QUANTITY"));
                    recipe.setUnit(jsonRecipeObject.getString("UNIT"));
                    recipeList.add(recipe);
                    // ListView表示用レシピ連結
                    if (j > 0) {
                        recipeString.append("／");
                    }
//                    if (havingProductDAO.ExistMaterialID(jsonRecipeObject.getString("MATERIALID"))) {
//                        // 所持商品と一致した場合は文字色を設定
//                        int startPos = recipeString.length();
//                        recipeString.append(jsonRecipeObject.getString("NAME"));
//                        recipeString.setSpan(
//                                textAppearanceSpan,
//                                startPos,
//                                recipeString.length(),
//                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                        );
//                    } else {
                        recipeString.append(jsonRecipeObject.getString("NAME"));
//                    }
                }
//                havingProductDAO.close();
            } else {
                recipeString.append("レシピ準備中");
            }
            setRecipes(recipeList);
            setRecipeStringBuffer(recipeString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
