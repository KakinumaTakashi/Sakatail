package jp.ecweb.homes.a1601.network;

import java.util.List;

import jp.ecweb.homes.a1601.models.Cocktail;
import jp.ecweb.homes.a1601.models.Category;

/**
 * Created by KakinumaTakashi on 2016/09/03.
 */
//TODO 削除予定
public interface CocktailListCallbacks {
	void CategoryCallback(Category category);
	void ListResponseCallback(List<Cocktail> cocktailList);
}
