package jp.ecweb.homes.a1601.models;

import org.json.JSONException;
import org.json.JSONObject;

import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 商品クラス
 */
public class Product {
    private static final String TAG = Product.class.getSimpleName();

    private String id;                  // 商品ID
    private String materialID;        // 素材ID
    private String category1;			// 大分類
    private String category2;			// 中分類
    private String category3;			// 小分類
    private String materialName;		// 素材名
    private String name;              // 商品名
    private String maker;             // メーカー名
    private String brand;             // ブランド名
    private float alcoholDegree;    // アルコール度数(%)
    private String thumbnailURL;     // サムネイルURL
    private String itemCode;            // 楽天商品コード

    /**
     * コンストラクタ
     */
    public Product() {}

    /**
     * コンストラクタ
     * @param jsonObject        レスポンスデータ部
     */
    public Product(JSONObject jsonObject) {
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

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getAlcoholDegree() {
        return alcoholDegree;
    }

    public void setAlcoholDegree(float alcoholDegree) {
        this.alcoholDegree = alcoholDegree;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

    /**
     * JSONからオブジェクト生成
     * @param jsonObject        JSONオブジェクト
     */
    private void fromJSON(JSONObject jsonObject) {
        try {
            setId(jsonObject.getString("ID"));
            setMaterialID(jsonObject.getString("MATERIALID"));
            setCategory1(jsonObject.getString("CATEGORY1"));
            setCategory2(jsonObject.getString("CATEGORY2"));
            setCategory3(jsonObject.getString("CATEGORY3"));
            setMaterialName(jsonObject.getString("MATERIALNAME"));
            setName(jsonObject.getString("NAME"));
            setMaker(jsonObject.getString("MAKER"));
            setBrand(jsonObject.getString("BRAND"));
            setAlcoholDegree((float) jsonObject.getDouble("ALCOHOLDEGREE"));
            setThumbnailURL(jsonObject.getString("THUMBNAILURL"));
            setItemCode(jsonObject.getString("ITEMCODE"));
        } catch (JSONException e) {
            e.printStackTrace();
            CustomLog.e(TAG, "Failed to create object from JSON.", e);
        }
    }
}
