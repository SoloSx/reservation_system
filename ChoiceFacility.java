package client_system1;

import	java.awt.*;
import	java.util.List;

public class ChoiceFacility extends Choice{
	////ChoiceFacilityコンストラクタ
	ChoiceFacility(List<String>facility){	//引数でfacility_idのリストを受け取る
		for(String id:facility) {			//facility_idをidに1つずつ追加する
			add(id);						//choiceにfacility_idを1つ追加する
		}
	}
}