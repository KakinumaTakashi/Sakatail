package jp.ecweb.homes.a1601;

import android.text.SpannableStringBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.model.Recipe;

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

    String getPhotoUrl() {
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

	String getMethod() {
		return Method;
	}

	public void setMethod(String method) {
		Method = method;
	}

	String getGrass() {
		return Grass;
	}

	public void setGrass(String grass) {
		Grass = grass;
	}

	float getAlcoholDegree() {
		return AlcoholDegree;
	}

	public void setAlcoholDegree(float alcoholDegree) {
		AlcoholDegree = alcoholDegree;
	}

	String getHowTo() {
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

	String getCopylight() {
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
            setPhotoUrl(jsonCocktailObject.getString("PHOTOURL"));
            setThumbnailUrl(jsonCocktailObject.getString("THUMBNAILURL"));
            setCopylight(jsonCocktailObject.getString("COPYLIGHT"));
            setHowTo(jsonCocktailObject.getString("HOWTO"));
            setMethod(jsonCocktailObject.getString("METHOD"));
            setGrass(jsonCocktailObject.getString("GRASS"));
            setAlcoholDegree((float) jsonCocktailObject.getDouble("ALCOHOLDEGREE"));
            // レシピ情報部
            List<Recipe> recipeList = new ArrayList<>();
            SpannableStringBuilder recipeString = new SpannableStringBuilder();
            JSONArray jsonRecipeArray = jsonCocktailObject.getJSONArray("RECIPES");
            if (jsonRecipeArray.length() > 0) {
                for (int j = 0; j < jsonRecipeArray.length(); j++) {
                    Recipe recipe = new Recipe();
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
                    recipeString.append(jsonRecipeObject.getString("NAME"));
                }
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
