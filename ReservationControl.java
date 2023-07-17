package client_system;

import java.sql.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReservationControl {
	//MySQLに接続するためのデータ
	Connection 	sqlCon;
	Statement 	sqlStmt;
	String		sqlUserID = "puser";
	String		sqlPassword = "1234";
	
	//予約システムのユーザIDおよびLogin状態
	String		reservationUserID;											//ログイン中のユーザID
	private boolean flagLogin;												//ログイン状態（ログイン済み:true）
	
	//ReservationControlクラスのコンストラクタ
	ReservationControl(){
		flagLogin = false;													//ログイン状態を非ログインとする.
	}
	
	////MySQLに接続するためのメソッド
	private void connectDB() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");						//MySQLのドライバをロードする
			//MySQLに接続
			String url = "jdbc:mysql://localhost?useUnicode=true&characterEncoding=SJIS";
			sqlCon = DriverManager.getConnection(url,sqlUserID,sqlPassword);
			sqlStmt = sqlCon.createStatement();								//Statement Objectを生成
		}catch(Exception e) {												//例外発生時
			e.printStackTrace();											//StackTraceをコンソールに表示
		}
	}
	////MySQLから切断するためのメソッド
	private void closeDB() {
		try {
			sqlStmt.close();												//Statement ObjectをCloseする
			sqlCon.close();													//MySQLとの接続を切る
		}catch (Exception e) {												//例外発生時
			e.printStackTrace();											//StackTraceを表示
		}
	}
	//ログイン・ログアウトボタンの処理	
	public	String loginLogout(MainFrame frame) {
		String res = "";													//処理結果を戻り値で返すための変数
		if(flagLogin) {														//ログイン済みのとき
			flagLogin = false;												//未ログイン状態にする
			frame.buttonLog.setLabel("ログイン");								//ボタンラベルを「ログイン」にする
			frame.tfLoginID.setText("未ログイン");								//ログインID表示を「未ログイン」にする
		}else {
			//ログインダイアログ生成+表示
			LoginDialog ld = new LoginDialog(frame);						//LoginDialogインスタンスの生成
			ld.setBounds(100, 100, 350, 150);								//Window位置とサイズを設定
			ld.setResizable(false);											//Window位置とサイズを固定化
			ld.setVisible(true);											//Windowを可視化
			ld.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);		//このWindowを閉じるまで他のWindowの操作
	
			if(ld.canceled) {												//LoginDialogのキャンセルボタンが押下されていたら
				return "";													//MainFrameのテキストフィールドには何も表示しない
			}	
			
			
			reservationUserID = ld.tfUserID.getText();						//ログインダイアログに入力されたユーザIDを取得
			String password = ld.tfPassword.getText();						//ログインダイアログに入力されたパスワードを取得
	
			connectDB();													//MySQLに接続
			try {
				//ユーザ情報を取得するクエリを生成	
				String sql = "SELECT * FROM db_reservation.user WHERE user_id ='" + reservationUserID + "';";
				ResultSet rs = sqlStmt.executeQuery(sql);					//クエリを実行して, 結果セットを取得

				if(rs.next()) {												//ユーザ情報があったとき
					String password_form_db = rs.getString("Password");		//検索結果からパスワードを取得
					if(password_form_db.equals(password)) {					//パスワードが一致したとき
						flagLogin = true;									//ログイン済みにする
						frame.buttonLog.setLabel("ログアウト");					//ログインボタンのラベルを「ログアウト」に書き換え
						frame.tfLoginID.setText(reservationUserID);			//ログインID表示をログインしたユーザIDにする
						res = "";											//MainFrameのテキストエリアには何も表示しない
					}else {													//パスワードが正しくないとき
						res = " IDまたはパスワードが違います．";
					}
				}else {														//ユーザIDがデータベースに登録されていないとき
					res = " IDが違います．";
				}
			}catch(Exception e) {											//例外発生時
				e.printStackTrace();
			}
			closeDB();														//MySQLの接続を切断
		}
		return	res;														//MainFrameのテキストエリアに表示するデータを返す
	}
	
	//教室概要ボタンを押下時の処理を行うメソッド
	public String getFacilityExplanation(String facility_id) {
		String res = "";													//戻り値を入れる変数の宣言
		String exp = "";													//explanationを入れる変数の宣言
		String openTime = "";												//open_timeを入れる変数の宣言
		String closeTime = "";												//close_timeを入れる変数の宣言
		connectDB();														//MySQLに接続
		try {
			String sql = "SELECT * FROM db_reservation.facility WHERE facility_id = '" + facility_id + "';";
			ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと同じレコードを抽出
			if(rs.next()) {													//1件目のレコードを取得
				exp = rs.getString("explanation");							//explanationの取得
				openTime = rs.getString("open_time");						//open_timeの取得
				closeTime = rs.getString("close_time");						//close_timeの取得
				
				res = exp + "　利用可能時間：" + openTime.substring(0, 5) + "～" + closeTime.substring(0, 5);	//教室概要情報データの作成	
			}else {															//データがなかった時
				res = "教室番号が違います．";									//エラーメッセージを返す
			}
		}catch(Exception e) {												//例外発生時
			e.printStackTrace();											//StackTraceを表示
		}
		closeDB();															//MySQLとの接続を切る
		return res;
	}
	
	//すべてのfacility_idを取得するメソッド
	public List getFacilityId() {
		List<String> facilityId = new ArrayList<String>();
		connectDB();														//MySQLに接続
		try {
			String sql = "SELECT * FROM db_reservation.facility;";
			ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと同じレコードを抽出
			while(rs.next()) {												//レコードがなくなるまで繰り返す
				facilityId.add(rs.getString("facility_id"));				//読み出したデータのfacility_idをfacilityIdリストに追加
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();															//MySQLを切断
		return facilityId;
	}
	
	//新規予約ボタン押下時のメソッド
	public String makeReservation(MainFrame frame) {
		String res = "";													//予約結果のメッセージを入れる
		
		if(flagLogin) {														//ログイン済みの時
			//新規予約画面生成
			ReservationDialog rd = new ReservationDialog(frame,this);
			
			//新規予約画面を表示
			rd.setVisible(true);
			if(rd.canceled) {												//新規予約ダイアログでキャンセルボタンが押されたとき
				return res;													//処理終了.　（表示メッセージなし）
			}
			//新規予約画面から年月日を取得
			String ryear_str = rd.tfYear.getText();							//新規予約ダイアログで設定された「年」を取得
			String rmonth_str = rd.tfMonth.getText();						//新規予約ダイアログで設定された「月」を取得
			String rday_str = rd.tfDay.getText();							//新規予約ダイアログで設定された「日」を取得
			//月と日が一桁だったら,　前に0を付与
			if(rmonth_str.length() == 1) {
				rmonth_str = "0" + rmonth_str;
			}
			if(rday_str.length() == 1) {
				rday_str = "0" + rday_str;
			}
			String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;	//予約年月日をString型で合成
			
			//入力された日付が正しいかのチェック
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				df.setLenient(false);										//日付の厳密化チェックを有効化
				String convData = df.format(df.parse(rdate));				//rdateを一度date型に変換し, String型に再変換
				if ((! rdate.equals(convData))||(ryear_str.length()!=4)){	//String→date→Stringで同じ文字に戻らないか, 西暦が４桁でなければ書式エラー
					res = "日付の書式を修正して下さい　（年：西暦4桁, 月：1～12,　日：1～31（毎月末日まで））";
					return res;
				}
			}catch(ParseException p){										//rdateが日付として成立してない時の処理
				res = "日付の値を修正して下さい";
				return res;
			}
			
			//新規予約画面から教室名,　開始時刻, 終了時刻を取得
			String facility = rd.choiceFacility.getSelectedItem();			//選択された教室IDを取り出す
			String st = rd.startHour.getSelectedItem() + ":" + rd.startMinute.getSelectedItem() + ":00";	//選択された開始時刻を取り出す
			String et = rd.endHour.getSelectedItem() + ":" + rd.endMinute.getSelectedItem() + ":00";		//選択された終了時刻を取り出す
			
			if(st.compareTo(et)>= 0) {										//開始時刻と終了時刻が等しい（0）か終了時刻がが早い（-1）
				res = "開始時刻と終了時刻が同じか終了時刻の方が早くなっています";
			}else {															//終了時刻の方が遅い（正常）
				try {
					//予約した日時を取得
					Calendar justNow = Calendar.getInstance();				//現在時刻を取得
					SimpleDateFormat resDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String now = resDate.format(justNow.getTime());			//現在時刻をMySQLのdate型文字列に変換
					
					//過日予約チェックのための事前処理
					String reservationDate = rdate.replace("-", "");			//予約日の文字列を"yyyymmdd"に変更
					int rDate_int = Integer.parseInt(reservationDate);			//int型に変更
					SimpleDateFormat justNow_c = new SimpleDateFormat("yyyyMMdd");
			        String justNow_s = justNow_c.format(justNow.getTime());
			        int justNow_int = Integer.parseInt(justNow_s);				//int型に変更

					connectDB();													//MySQLに接続
					
					//二重予約のチェック
					String sql = "SELECT * FROM db_reservation.reservation WHERE facility_id = '" + facility + "' AND day = '" +  rdate + "';";
					ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと日付が同じレコードを抽出
					
					boolean hasReservation = false;  								// 予約が存在するかどうかを判定するフラグ
					boolean isOverlapping = false;  								// 予約が重複しているかどうかを判定するフラグ
					
					while(rs.next()) {												//レコードがなくなるまで繰り返す
						hasReservation = true;  									//予約が存在する場合、フラグを立てる
						
						String rstimeData = rs.getString("start_time");				//該当レコードのstart_timeを取得
						String rstartData = rstimeData.replace(":", "");			//rstimeDataの「：」を削除(既に予約されているデータの予約開始時間)，rstartData=HHｍｍｓｓ
						int rstartData_int = Integer.parseInt(rstartData);			//int型に変更
						String retimeData = rs.getString("end_time");				//該当レコードのend_timeを取得
						String rendData = retimeData.replace(":", "");				//retimeDataの「：」を削除(既に予約されているデータの予約開始時間)，rstartData=HHｍｍｓｓ
						int rendData_int = Integer.parseInt(rendData);				//int型に変更
						String nstartData = st.replace(":", "");					//stの「：」を削除(予約しようとしているデータの予約開始時間)，nstartData=HHｍｍｓｓ
						int nstartData_int = Integer.parseInt(nstartData);			//int型に変更
						String nendData = et.replace(":", "");						//etの「：」を削除(予約しようとしているデータの予約開始時間)，nendData=HHｍｍｓｓ
						int nendData_int = Integer.parseInt(nendData);				//int型に変更
							
						if (nendData_int <= rstartData_int || rendData_int <= nstartData_int) {
							isOverlapping = false;
						}else{
							isOverlapping = true;
							res = "選択された教室・日付のその予約時間は、他の方の予約で埋まっています。";
					        break;  // 予約が重複しているため、ループを終了
					    }
					}
					
					if (!hasReservation || !isOverlapping) {
					    // 予約が存在しないか、重複していない場合の処理
					    // 過日予約チェック
						if (rDate_int >= justNow_int) {
							
							// 予約情報をMySQLに書き込む
							String sql2 = "INSERT INTO db_reservation.reservation(facility_id, user_id, date, day, start_time, end_time) VALUES('"
									+ facility + "','" + reservationUserID + "','" + now + "','" + rdate + "','" + st + "','" + et +"');";
							sqlStmt.executeUpdate(sql2);
							res = "予約されました";
						} else {
							res = "過日予約はできません．";
						}
					} else {
					    res = "選択された教室・日付のその予約時間は、他の方の予約で埋まっています。";
					}

					rs.close();  											// ResultSetをクローズ
				}catch(Exception e){										//予約情報をDBに書き込む際にエラーが発生したとき
					e.printStackTrace();
				}
				closeDB();													//MySQLの接続を切る
			}	
		}else {																//未ログイン状態の場合
			res = "ログインして下さい";
		}
		return res;
	}
	
	//指定教室の利用可能開始・終了時間を取得する
	public int[]getAvailableTime(String facility){
		int[]abailableTime = {0, 0};										//開始時・終了時をこの配列に入れて呼び元に返す
		connectDB();
		try {
			String sql = "SELECT * FROM db_reservation.facility WHERE facility_id = " + facility + ";";
			ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと同じレコードを抽出
			while(rs.next()) {												//レコードがなくなるまで繰り返す
				String timeData = rs.getString("open_time");				//該当レコードのopen_timeを取得
				timeData = timeData.substring(0, 2);						//open_timeの「時」のみ取得
				abailableTime[0] = Integer.parseInt(timeData);				//open_timeの「時」を整数型に変換
				timeData = rs.getString("close_time");						//該当レコードのclose_timeを取得
				timeData = timeData.substring(0, 2);						//close_timeの「時」のみ取得
				abailableTime[1] = Integer.parseInt(timeData);				//close_timeの「時」を整数型に変換
			}
		}catch(Exception e) {												//該当レコードがない, 「時」を整数に変換できないなど
			e.printStackTrace();
		}
		return abailableTime;												//open_time, close_timeの「時」を返す. エラーの場合（0, 0）が返る.
	}
	
	//予約状況ボタン押下時のメソッド
	public String makeReservationStatus(MainFrame frame) {
		String res = "";													//予約状況のメッセージを入れる
			
		//教室予約状況画面生成
		ReservationStatusDialog rd = new ReservationStatusDialog(frame,this);
				
		//教室予約状況画面を表示
		rd.setVisible(true);
		if(rd.canceled) {													//予約状況ダイアログでキャンセルボタンが押されたとき
			return res;														//処理終了.　（表示メッセージなし）
		}
		//教室予約状況画面から年月日を取得
		String ryear_str = rd.tfYear.getText();								//予約状況ダイアログで設定された「年」を取得
		String rmonth_str = rd.tfMonth.getText();							//予約状況ダイアログで設定された「月」を取得
		String rday_str = rd.tfDay.getText();								//予約状況ダイアログで設定された「日」を取得
		//月と日が一桁だったら,　前に0を付与
		if(rmonth_str.length() == 1) {
			rmonth_str = "0" + rmonth_str;
		}
		if(rday_str.length() == 1) {
			rday_str = "0" + rday_str;
		}
		String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;		//年月日をString型で合成
				
		//入力された日付が正しいかのチェック
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(false);											//日付の厳密化チェックを有効化
			String convData = df.format(df.parse(rdate));					//rdateを一度date型に変換し, String型に再変換
			if ((! rdate.equals(convData))||(ryear_str.length()!=4)){		//String→date→Stringで同じ文字に戻らないか, 西暦が４桁でなければ書式エラー
				res = "日付の書式を修正して下さい　（年：西暦4桁, 月：1～12,　日：1～31（毎月末日まで））";
				return res;
			}
		}catch(ParseException p){											//rdateが日付として成立してない時の処理
			res = "日付の値を修正して下さい";
			return res;
		}
		//教室予約状況画面から教室名を取得
		String facility = rd.choiceFacility.getSelectedItem();				//選択された教室IDを取り出す
		//教室予約状況のOKボタンを押下時の処理
		try {
			String startTime = "";											//open_timeを入れる変数の宣言
			String endTime = "";											//close_timeを入れる変数の宣言
			
			connectDB();													//MySQLに接続
			String sql = "SELECT * FROM db_reservation.reservation WHERE facility_id = '" + facility + "' AND day = '" +  rdate + "';";
			ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと同じレコードを抽出
			if(rs.next()) {													//1件目のレコードを取得
				startTime = rs.getString("start_time");						//start_timeの取得
				endTime = rs.getString("end_time");							//end_timeの取得	
				res = ryear_str + "年" + rmonth_str + "月" + rday_str + "日　 　予約時間：" + startTime.substring(0, 5) + "～" + endTime.substring(0, 5) + "　 教室：" + facility;	//教室概要情報データの作成	
			}else {															//予約データがなかった時
				res = "指定された教室、日付には予約が入っておりません.　";				//メッセージを返す
			}
		}catch(Exception e) {												//例外発生時
			e.printStackTrace();											//StackTraceを表示
		}
		closeDB();															//MySQLとの接続を切る
		return res;
	}
	
	
	//自己予約確認ボタン押下時のメソッド（ミニマム仕様）
	public String getSelfCheckReservation() {
		String res = "";													//戻り値を入れる変数の宣言
		if(flagLogin) {
			List<String> reservationInfoList = new ArrayList<>();  			//予約データを格納するリストの宣言
	
			String facility = "";											//facility_idを入れる変数の宣言
			String reservationDay = "";										//dayを入れる変数の宣言
			String startTime = "";											//start_timeを入れる変数の宣言
			String endTime = "";											//end_timeを入れる変数の宣言
			String rdate_data = "";											//rdateを入れる変数の宣言
			
			//自己予約確認を行った日を取得
			Calendar justNow = Calendar.getInstance();						//現在の日時を取得
			SimpleDateFormat justNow_c = new SimpleDateFormat("yyyyMMdd");
	        String justNow_s = justNow_c.format(justNow.getTime());			//年月日のみの文字列に
	        int justNow_int = Integer.parseInt(justNow_s);					//int型に変更
			
			connectDB();													//MySQLに接続
			try {
				String sql = "SELECT * FROM db_reservation.reservation WHERE user_id = '" + reservationUserID + "';";
				ResultSet rs = sqlStmt.executeQuery(sql);					//選択された教室IDと同じレコードを抽出
				
				boolean hasReservation = false;  							//予約が存在するかどうかを判定するフラグ
				int count = 0; 												//予約情報のカウンタ
				while (rs.next()) {											//レコードがなくなるまで繰り返す
					hasReservation = true;  								//予約が存在する場合、フラグを立てる
					rdate_data = rs.getString("day");						//予約日を取得
					String rdate_s = rdate_data.replace("-", "");			//yyyy-mm-ddをyyyymmddに変更
					int rdate_int = Integer.parseInt(rdate_s);				//int型に変更
					if(rdate_int > justNow_int) {
						
						facility = rs.getString("facility_id");				//facility_idの取得、教室番号(str)
						startTime = rs.getString("start_time");				//start_timeの取得、予約の開始時間
						endTime = rs.getString("end_time");					//end_timeの取得、予約の終了時間
						
						//自己予約確認，表示データ作成
						String reservationInfo = "予約日：" + rdate_data.substring(0, 4) + "年" + rdate_data.substring(5, 7) + "月" + rdate_data.substring(8, 10) + "日"
													+ "　　予約時間：" + startTime.substring(0, 5) + "～" + endTime.substring(0, 5) + "　　教室：" + facility;
						count++; 											//カウンタをインクリメント
						reservationInfoList.add(reservationInfo);  			//予約情報をリストに追加
						res += reservationInfo + "\n";  					//予約情報を res にも追加

						if (count > 20) {
		                    break; 											// 20件の予約データを取得したらループを終了する
						}
					}
				}
				if (!hasReservation) {
				    // 予約が存在しない場合の処理
					res = "予約がありません．";									//メッセージを返す
				}
			}catch(Exception e) {											//例外発生時
				e.printStackTrace();										//StackTraceを表示
			}
			closeDB();														//MySQLとの接続を切る
		}else {																//未ログイン状態の場合
			res = "ログインして下さい";
		}
		return res;
	}
	
	//自己予約確認条件指定ボタン押下時のメソッド
	public String makeSelfCheckReservation(MainFrame frame) {
		String res = "";													//予約結果のメッセージを入れる
		
		if(flagLogin) {														//ログイン済みの時
			//画面生成
			SelfCheckReservationDialog rd = new SelfCheckReservationDialog(frame,this);
			
			//自己予約確認条件指定検索画面を表示
			rd.setVisible(true);
			if(rd.canceled) {												//新規予約確認条件指定検索ダイアログでキャンセルボタンが押されたとき
				return res;													//処理終了.　（表示メッセージなし）
			}
			//自己予約確認条件指定検索画面から年月日を取得
			String sryear_str = rd.stfYear.getText();						//入力された検索開始期間「年」を取得
			String srmonth_str = rd.stfMonth.getText();						//入力された検索開始期間「月」を取得
			String srday_str = rd.stfDay.getText();							//入力された検索開始期間「日」を取得
			String eryear_str = rd.etfYear.getText();						//入力された検索終了期間「年」を取得
			String ermonth_str = rd.etfMonth.getText();						//入力された検索終了期間「月」を取得
			String erday_str = rd.etfDay.getText();							//入力された検索終了期間「日」を取得
			    

			//月と日が一桁だったら,　前に0を付与
			if(srmonth_str.length() == 1) {
				srmonth_str = "0" + srmonth_str;
			}
			if(ermonth_str.length() == 1) {
				ermonth_str = "0" + ermonth_str;
			}
			if(srday_str.length() == 1) {
				srday_str = "0" + erday_str;
			}
			if(erday_str.length() == 1) {
				erday_str = "0" + erday_str;
			}
				
			//入力された年月日合成
			String srdate = "";
			if (!sryear_str.isEmpty() && !srmonth_str.isEmpty() && !srday_str.isEmpty()) {
			    srdate = sryear_str + "-" + srmonth_str + "-" + srday_str;		//検索開始年月日をString型で合成
			}
			String erdate = "";
			if (!eryear_str.isEmpty() && !ermonth_str.isEmpty() && !erday_str.isEmpty()) {
			    erdate = eryear_str + "-" + ermonth_str + "-" + erday_str;		//検索終了年月日をString型で合成
			}
				
			//年、月、日が全て空の場合は以下の処理をスキップする
			if (!(sryear_str.isEmpty() && srmonth_str.isEmpty() && srday_str.isEmpty() &&
			        eryear_str.isEmpty() && ermonth_str.isEmpty() && erday_str.isEmpty())) {
				//入力された日付が正しいかのチェック
				try {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					df.setLenient(false);										//日付の厳密化チェックを有効化
					String convData_s = df.format(df.parse(srdate));				//srdateを一度date型に変換し, String型に再変換
					String convData_e = df.format(df.parse(erdate));				//erdateを一度date型に変換し, String型に再変換
					
					//String→date→Stringで同じ文字に戻らないか, 西暦が４桁でなければ書式エラー
					if (((! srdate.equals(convData_s))||(sryear_str.length()!=4))||((! erdate.equals(convData_e))||(eryear_str.length()!=4))){
						res = "日付の書式を修正して下さい　（年：西暦4桁, 月：1～12,　日：1～31（毎月末日まで））";
						return res;
					}
					if ((sryear_str.isEmpty() && srmonth_str.isEmpty() && srday_str.isEmpty())||(eryear_str.isEmpty() && ermonth_str.isEmpty() && erday_str.isEmpty())) {
				        res = "";
				        return res;
				    }
				}catch(ParseException p){										//rdateが日付として成立してない時の処理
					res = "日付の値を修正して下さい";
					return res;
				}
			}
			
			//入力された年月日合成したstrをintに変換するまでの処理
			String srdate_s;
			String erdate_s;
			int srdate_int = 0;
			int erdate_int = 0;													//初期値
			if((!srdate.isEmpty()) && (!erdate.isEmpty())) {					//空じゃないとき
				srdate_s = srdate.replace("-", "");								//yyyymmddの文字列にする
				srdate_int = Integer.parseInt(srdate_s);						//int型に変更
				erdate_s = erdate.replace("-", "");								//yyyymmddの文字列にする
				erdate_int = Integer.parseInt(erdate_s);						//int型に変更
			
			}
			
			//自己予約確認条件指定検索画面から教室名と表示件数を取得
			String facility = rd.choiceFacility_s.getSelectedItem();			//選択された教室IDを取り出す
			String nreservation = rd.n_reservation.getSelectedItem();			//選択された表示件数を取り出す
			int nreservation_int = Integer.parseInt(nreservation);				//int型に変更
			
			List<String> reservationInfoList = new ArrayList<>();  				//予約データを格納するリストの宣言
			
			String startTime = "";												//start_timeを入れる変数の宣言
			String endTime = "";												//end_timeを入れる変数の宣言
			String rdate_data = "";												//dayを入れる変数の宣言
			
			//自己予約状況確認を行った日を取得
			Calendar justNow = Calendar.getInstance();							//現在日時を取得
			SimpleDateFormat justNow_c = new SimpleDateFormat("yyyyMMdd");
	        String justNow_s = justNow_c.format(justNow.getTime());				//年月日のみ文字列にする
	        int justNow_int = Integer.parseInt(justNow_s);						//int型に変更
	        
			
			connectDB();														//MySQLに接続
			String sql;
			try {
				if(facility == ""){
					sql = "SELECT * FROM db_reservation.reservation WHERE user_id = '" + reservationUserID + "';";
				}else{
					sql = "SELECT * FROM db_reservation.reservation WHERE user_id = '" + reservationUserID + "' AND facility_id = '" +  facility + "';";	
				}
				
				ResultSet rs = sqlStmt.executeQuery(sql);						//選択された教室IDと同じレコードを抽出
				
				boolean hasReservation = false;  								//予約が存在するかどうかを判定するフラグ
				int count = 0; 													//予約情報のカウンタ
				while (rs.next()) {												//レコードがなくなるまで繰り返す
					hasReservation = true;  									//予約が存在する場合、フラグを立てる
					rdate_data = rs.getString("day");
					String rdate_s = rdate_data.replace("-", "");				//yyyymmddのstr
					int rdate_int = Integer.parseInt(rdate_s);					//int型に変更
					
					if(rdate_int >= justNow_int && (!(srdate_int<justNow_int && erdate_int<justNow_int))) {
						int rdate_hint = 0;										//表示する予約日をintで入れる
						String rdate_h = "";									//ｓｔｒ型
						if((srdate_int == erdate_int) && (srdate_int == justNow_int)) {
							rdate_hint = srdate_int;
							rdate_h = String.valueOf(rdate_hint);
						}else if((srdate_int > justNow_int) && (erdate_int > justNow_int)){	
							if((srdate_int <= rdate_int) && (rdate_int <= erdate_int)){
								rdate_hint = rdate_int;
								rdate_h = String.valueOf(rdate_hint);
							}else if((srdate_int >= rdate_int) && (rdate_int >= erdate_int)){
								rdate_hint = rdate_int;
								rdate_h = String.valueOf(rdate_hint);
							}
						}else{
							//erdate_int >= justNow_int > srdate_intか，erdate_int > justNow_int >= srdate_int
							if(erdate_int >= justNow_int) {
								if((erdate_int >= rdate_int) && (rdate_int >= justNow_int)) {
									rdate_hint = rdate_int;
									rdate_h = String.valueOf(rdate_hint);
								}
							//srdate_int >= justNow_int > erdate_intか，srdate_int > justNow_int >= erdate_int
							}else if(srdate_int >= justNow_int){
								if((srdate_int >= rdate_int) && (rdate_int >= justNow_int)) {
									rdate_hint = rdate_int;
									rdate_h = String.valueOf(rdate_hint);
								}
							}	
						}

						startTime = rs.getString("start_time");					//start_timeの取得
						endTime = rs.getString("end_time");						//end_timeの取得
						
						//自己予約状況確認情報データの作成
						String reservationInfo = "予約日：" + rdate_h.substring(0, 4) + "年" + rdate_h.substring(4, 6) + "月" + rdate_h.substring(6, 8) + "日"
													+ "　　　　予約時間：" + startTime.substring(0, 5) + "～" + endTime.substring(0, 5) + "　　　　教室：" + facility;
						count++; 												//カウンタをインクリメント
						reservationInfoList.add(reservationInfo);				//予約データをリストに追加
						res += reservationInfo + "\n";  						//予約データを res にも追加
						
						if (count > nreservation_int) {							//表示件数を超えたら
		                    break; 												//件以上の予約情報を取得したらループを終了する
						}
					}
				}
				if (!hasReservation||(srdate_int<justNow_int && erdate_int<justNow_int)) {
				    // 予約が存在しない場合の処理
					res = "予約がありません．";										//メッセージを返す
				}
			}catch(Exception e) {												//例外発生時
				e.printStackTrace();											//StackTraceを表示
			}
			closeDB();															//MySQLとの接続を切る
		}else {																	//未ログイン状態の場合
			res = "ログインして下さい";
		}
		return res;
	}
}