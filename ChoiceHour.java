package client_system1;

import java.awt.*;

public class ChoiceHour extends Choice{
	//コンストラクタ
	ChoiceHour(){
		resetRange(9, 21);								//時刻の範囲を9～21とする
	}
	
	//指定できる時刻の範囲を設定するメソッド
	public void resetRange(int start,int end) {
		removeAll();									//選択ボックスの内容をクリア
		while(start <= end) {							//選択可能範囲の時刻を設定
			String h = String.valueOf(start);
			if(h.length() == 1) {						//一桁の場合は前に0を付与する
				h = "0" + h;
			}
			add(h);										//選択ボックスに文字列を追加
			start++;
		}
	}
	
	//設定されている一番早い時刻を返すメソッド
	public String getFirst() {							//登録されている時間の中で一番早い時間を返す
		return getItem(0);								//先頭から0番目のデータを返す
	}
	
	//設定されている一番遅い時刻を返すメソッド
	public String getLast() {
		return getItem(getItemCount() -1);
	}
}
