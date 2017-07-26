package jp.ecweb.homes.a1601.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ecweb.homes.a1601.utils.CustomLog;

/**
 * 楽天商品検索API レスポンス
 */
public class RakutenResponse {
    private static final String TAG = RakutenResponse.class.getSimpleName();

	// 全体情報
	private int count;      // 検索数
	private int page;       // ページ番号
	private int first;      // ページ内商品始追番
	private int last;       // ページ内商品終追番
	private int hits;       // ヒット件数番
	private int carrier;    // キャリア情報
	private int pageCount;  // 総ページ数

	// 商品情報
	private List<RakutenItem> rakutenItems;

    /**
     * コンストラクタ
     */
    public RakutenResponse() {}

    /**
     * コンストラクタ
     * @param jsonObject        レスポンスデータ部
     */
    public RakutenResponse(JSONObject jsonObject) {
        fromJSON(jsonObject);
    }
/*--------------------------------------------------------------------------------------------------
	Getter / Setter
--------------------------------------------------------------------------------------------------*/
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getCarrier() {
		return carrier;
	}

	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<RakutenItem> getRakutenItems() {
		return rakutenItems;
	}

	public void setRakutenItems(List<RakutenItem> rakutenItems) {
		this.rakutenItems = rakutenItems;
	}

    private void fromJSON(JSONObject response) {
        try {
            // ヘッダ部処理
            setCount(response.getInt("count"));
            setPage(response.getInt("page"));
            setFirst(response.getInt("first"));
            setLast(response.getInt("last"));
            setHits(response.getInt("hits"));
            setCarrier(response.getInt("carrier"));
            setPageCount(response.getInt("pageCount"));
            // データ部処理
            JSONArray data = response.getJSONArray("Items");
            // JSONをListに展開
            List<RakutenItem> rakutenItems = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                RakutenItem rakutenItem = new RakutenItem();
                rakutenItem.setItemName(jsonObject.getString("itemName"));
                rakutenItem.setCatchcopy(jsonObject.getString("catchcopy"));
                rakutenItem.setItemCode(jsonObject.getString("itemCode"));
                rakutenItem.setItemPrice(jsonObject.getInt("itemPrice"));
                rakutenItem.setItemCaption(jsonObject.getString("itemCaption"));
                rakutenItem.setItemUrl(jsonObject.getString("itemUrl"));
                rakutenItem.setAffiliateUrl(jsonObject.getString("affiliateUrl"));
                rakutenItem.setShopAffiliateUrl(jsonObject.getString("shopAffiliateUrl"));
                rakutenItem.setImageFlag(jsonObject.getInt("imageFlag"));
                List<String> smallImageUrlList = new ArrayList<>();
                for (int j = 0; j < jsonObject.getJSONArray("smallImageUrls").length(); j++) {
                    smallImageUrlList.add(jsonObject.getJSONArray("smallImageUrls").getString(j));
                }
                rakutenItem.setSmallImageUrls(smallImageUrlList);
                List<String> mediumImageUrlList = new ArrayList<>();
                for (int j = 0; j < jsonObject.getJSONArray("mediumImageUrls").length(); j++) {
                    mediumImageUrlList.add(jsonObject.getJSONArray("mediumImageUrls").getString(j));
                }
                rakutenItem.setMediumImageUrls(mediumImageUrlList);
                rakutenItem.setAvailability(jsonObject.getInt("availability"));
                rakutenItem.setTaxFlag(jsonObject.getInt("taxFlag"));
                rakutenItem.setPostageFlag(jsonObject.getInt("postageFlag"));
                rakutenItem.setCreditCardFlag(jsonObject.getInt("creditCardFlag"));
                rakutenItem.setShopOfTheYearFlag(jsonObject.getInt("shopOfTheYearFlag"));
                rakutenItem.setShipOverseasFlag(jsonObject.getInt("shipOverseasFlag"));
                rakutenItem.setShipOverseasArea(jsonObject.getString("shipOverseasArea"));
                rakutenItem.setAsurakuFlag(jsonObject.getInt("asurakuFlag"));
                rakutenItem.setAsurakuClosingTime(jsonObject.getString("asurakuClosingTime"));
                rakutenItem.setAsurakuArea(jsonObject.getString("asurakuArea"));
                rakutenItem.setAffiliateRate(jsonObject.getInt("affiliateRate"));
                rakutenItem.setStartTime(jsonObject.getString("startTime"));
                rakutenItem.setEndTime(jsonObject.getString("endTime"));
                rakutenItem.setReviewCount(jsonObject.getInt("reviewCount"));
                rakutenItem.setReviewAverage(jsonObject.getDouble("reviewAverage"));
                rakutenItem.setPointRate(jsonObject.getInt("pointRate"));
                rakutenItem.setPointRateStartTime(jsonObject.getString("pointRateStartTime"));
                rakutenItem.setPointRateEndTime(jsonObject.getString("pointRateEndTime"));
                rakutenItem.setGiftFlag(jsonObject.getInt("giftFlag"));
                rakutenItem.setShopName(jsonObject.getString("shopName"));
                rakutenItem.setShopCode(jsonObject.getString("shopCode"));
                rakutenItem.setShopUrl(jsonObject.getString("shopUrl"));
                rakutenItems.add(rakutenItem);
            }
            setRakutenItems(rakutenItems);
        } catch (JSONException e) {
            CustomLog.w(TAG, "Failed to create object from JSON.", e);
        }
    }
}
