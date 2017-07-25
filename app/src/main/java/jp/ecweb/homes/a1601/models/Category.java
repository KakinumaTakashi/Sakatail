package jp.ecweb.homes.a1601.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.C;
import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * カテゴリ情報クラス
 */
public class Category {
    private static final String TAG = Category.class.getSimpleName();

	private String category1;                   // 頭文字
	private String category2;                   // ベース種類

	private List<String> category1List;         // 頭文字一覧
	private List<String> category1ValueList;    // 頭文字別内部値
	private List<String> category1NumList;      // 頭文字別件数
	private List<String> category2List;         // ベース種類一覧
	private List<String> category2NumList;      // ベース種類別件数
	private List<String> category2ValueList;    // ベース種類別内部値

    /**
     * コンストラクタ
     */
	public Category() {
		resetCategoryAll();
		setCategory1List(new ArrayList<String>());
		setCategory1ValueList(new ArrayList<String>());
        setCategory1NumList(new ArrayList<String>());
        setCategory2List(new ArrayList<String>());
		setCategory2NumList(new ArrayList<String>());
		setCategory2ValueList(new ArrayList<String>());
	}

    /**
     * コンストラクタ
     * @param jsonObject        レスポンスデータ部
     */
	public Category(JSONObject jsonObject) {
	    this();
	    fromJSON(jsonObject);
    }

    /**
     * カテゴリ情報全リセット
     */
	public void resetCategoryAll() {
		resetCategory1();
		resetCategory2();
	}

    /**
     * カテゴリ情報１リセット
     */
	public void resetCategory1() {
		this.category1 = "All";
	}

    /**
     * カテゴリ情報２リセット
     */
	public void resetCategory2() {
		this.category2 = "All";
	}

/*--------------------------------------------------------------------------------------------------
    Getter / Setter
--------------------------------------------------------------------------------------------------*/
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

	public List<String> getCategory1List() {
		return category1List;
	}

	private void setCategory1List(List<String> category1List) {
		this.category1List = category1List;
	}

	public List<String> getCategory2List() {
		return category2List;
	}

	private void setCategory2List(List<String> category2List) {
		this.category2List = category2List;
	}

	public List<String> getCategory1NumList() {
		return category1NumList;
	}

	private void setCategory1NumList(List<String> category1NumList) {
		this.category1NumList = category1NumList;
	}

	public List<String> getCategory2NumList() {
		return category2NumList;
	}

	private void setCategory2NumList(List<String> category2NumList) {
		this.category2NumList = category2NumList;
	}

	public List<String> getCategory1ValueList() {
		return category1ValueList;
	}

	private void setCategory1ValueList(List<String> category1ValueList) {
		this.category1ValueList = category1ValueList;
	}

	public List<String> getCategory2ValueList() {
		return category2ValueList;
	}

	private void setCategory2ValueList(List<String> category2ValueList) {
		this.category2ValueList = category2ValueList;
	}

	/**
	 * JSONからオブジェクト生成
	 * @param json        JSONオブジェクト
	 */
	private void fromJSON(JSONObject json) {
        try {
            // Category1Items
            JSONArray category1Items = json.getJSONArray(C.RSP_KEY_CATEGORY1ITEMS);
            for (int i = 0; i < category1Items.length(); i++) {
                JSONObject jsonObject = category1Items.getJSONObject(i);
                getCategory1List().add(jsonObject.getString(C.RSP_KEY_CATEGORY1));
                getCategory1NumList().add(jsonObject.getString(C.RSP_KEY_CATEGORY1NUM));
            }
            // Category2Items
            JSONArray category2Items = json.getJSONArray(C.RSP_KEY_CATEGORY2ITEMS);
            for (int i = 0; i < category2Items.length(); i++) {
                JSONObject jsonObject = category2Items.getJSONObject(i);
                getCategory2List().add(jsonObject.getString(C.RSP_KEY_CATEGORY2));
                getCategory2NumList().add(jsonObject.getString(C.RSP_KEY_CATEGORY2_NUM));
            }
        } catch (JSONException e) {
            CustomLog.e(TAG, "Failed to create object from JSON.", e);
        }
	}
}
