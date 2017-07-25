package jp.ecweb.homes.a1601.models;

import android.text.SpannableStringBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * カクテル情報クラス
 */
public class Cocktail {
    private static final String TAG = Cocktail.class.getSimpleName();

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
        fromJSON(jsonObject);
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
		this.Method = method;
	}

	public String getGrass() {
		return Grass;
	}

	public void setGrass(String grass) {
		this.Grass = grass;
	}

	public float getAlcoholDegree() {
		return AlcoholDegree;
	}

	public void setAlcoholDegree(float alcoholDegree) {
		this.AlcoholDegree = alcoholDegree;
	}

	public String getHowTo() {
		return HowTo;
	}

	public void setHowTo(String howTo) {
		this.HowTo = howTo;
	}

	public SpannableStringBuilder getRecipeStringBuffer() {
		return RecipeStringBuffer;
	}

	public void setRecipeStringBuffer(SpannableStringBuilder recipeStringBuffer) {
		this.RecipeStringBuffer = recipeStringBuffer;
	}

	public String getCopylight() {
		return Copylight;
	}

	public void setCopylight(String copylight) {
		this.Copylight = copylight;
	}

    /**
     * JSONからオブジェクト生成
     * @param json          JSONオブジェクト
     */
	private void fromJSON(JSONObject json) {
        try {
            // カクテル情報部
            setId(json.getString("ID"));
            setName(json.getString("NAME"));
            if (json.has("PHOTOURL")) setPhotoUrl(json.getString("PHOTOURL"));
            if (json.has("THUMBNAILURL")) setThumbnailUrl(json.getString("THUMBNAILURL"));
            if (json.has("COPYLIGHT")) setCopylight(json.getString("COPYLIGHT"));
            if (json.has("HOWTO")) setHowTo(json.getString("HOWTO"));
            if (json.has("METHOD")) setMethod(json.getString("METHOD"));
            if (json.has("PHOTOURL")) setGrass(json.getString("GRASS"));
            if (json.has("PHOTOURL")) setAlcoholDegree((float) json.getDouble("ALCOHOLDEGREE"));
            // レシピ情報部
            List<Recipe> recipeList = new ArrayList<>();
            SpannableStringBuilder recipeString = new SpannableStringBuilder();
            JSONArray jsonRecipeArray = json.getJSONArray("RECIPES");
            if (jsonRecipeArray.length() > 0) {
                for (int j = 0; j < jsonRecipeArray.length(); j++) {
                    Recipe recipe = new Recipe();
                    JSONObject jsonRecipeObject = jsonRecipeArray.getJSONObject(j);
                    if (jsonRecipeObject.has("ID")) recipe.setId(jsonRecipeObject.getString("ID"));
                    if (jsonRecipeObject.has("COCKTAILID")) recipe.setCocktailID(jsonRecipeObject.getString("COCKTAILID"));
                    if (jsonRecipeObject.has("MATERIALID")) recipe.setMatelialID(jsonRecipeObject.getString("MATERIALID"));
                    if (jsonRecipeObject.has("CATEGORY1")) recipe.setCategory1(jsonRecipeObject.getString("CATEGORY1"));
                    if (jsonRecipeObject.has("CATEGORY2")) recipe.setCategory2(jsonRecipeObject.getString("CATEGORY2"));
                    if (jsonRecipeObject.has("CATEGORY3")) recipe.setCategory3(jsonRecipeObject.getString("CATEGORY3"));
                    if (jsonRecipeObject.has("NAME")) recipe.setMatelialName(jsonRecipeObject.getString("NAME"));
                    if (jsonRecipeObject.has("QUANTITY")) recipe.setQuantity(jsonRecipeObject.getInt("QUANTITY"));
                    if (jsonRecipeObject.has("UNIT")) recipe.setUnit(jsonRecipeObject.getString("UNIT"));
                    recipeList.add(recipe);
                    // ListView表示用レシピ連結
                    if (j > 0) {
                        recipeString.append("／");
                    }
                    if (jsonRecipeObject.has("NAME")) recipeString.append(jsonRecipeObject.getString("NAME"));
                }
            } else {
                recipeString.append("レシピ準備中");
            }
            setRecipes(recipeList);
            setRecipeStringBuffer(recipeString);
        } catch (JSONException e) {
            CustomLog.w(TAG, "Failed to create object from JSON.", e);
        }
    }
}
