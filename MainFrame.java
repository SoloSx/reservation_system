package client_system1;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends Frame implements ActionListener, WindowListener {
	ReservationControl reservationControl;		//クラスのインスタンス生成
	//パネルインスタンス生成
	Panel	panelNorth;							//上部パネル
	Panel	panelNorthSub1;						//上部パネルの上
	Panel	panelNorthSub2;						//上部パネルの下
	Panel	panelCenter;						//中央パネル
	Panel	panelSouth;							//下部パネル
	
	//ボタンインスタンス生成
	Button buttonLog;							//ログイン・ログアウトボタン
	Button buttonExplanation;					//教室概要ボタン
	Button buttonReservation;					//新規予約画面
	Button buttonReservationStatus;				//教室予約状況確認画面
	Button buttonSelfCheckReservation;			//自己予約状況確認，確認ボタン
	Button buttonSelfCheckReservation2;			//自己予約状況確認条件指定画面
	Button buttonCancelReservation;				//自己予約キャンセル画面
	
	//コンボボックスのインスタンス生成
	ChoiceFacility choiceFacility;				//教室選択用コンボボックス
	
	//テキストフィールドのインスタンス生成
	TextField tfLoginID;						//ログインIDを表示するテキストフィールド
	
	//テキストエリアのインスタンス生成
	TextArea textMessage;						//結果表示用メッセージ欄
	
////mainFrameコンストラクタ
	public MainFrame(ReservationControl rc) {
		reservationControl = rc;
		//ボタン生成
		buttonLog = new Button(" ログイン ");
		buttonExplanation = new Button("教室概要");
		buttonReservationStatus = new Button("予約状況");	
		buttonReservation = new Button("新規予約");
		buttonSelfCheckReservation = new Button("確認");
		buttonSelfCheckReservation2 = new Button("条件指定");
		buttonCancelReservation = new Button("キャンセル");
		
		//教室選択用コンボボックスの生成
		List<String>facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility(facilityId);
		
		//ログインID用表示ボックスの生成
		tfLoginID = new TextField("未ログイン",12);
		tfLoginID.setEditable(false);
		
		//上中下のパネルを使うため、レイアウトマネジャにBorderLavoutを設定
		setLayout(new BorderLayout());
		
		//上部パネルの上パネルに予約システムというラベルと[ログイン]ボタンを追加
		panelNorthSub1 = new Panel(); 
		panelNorthSub1.add(new Label("教室予約システム  "));
		panelNorthSub1.add(buttonLog);
		panelNorthSub1.add(new Label("		ログインID:"));
		panelNorthSub1.add(tfLoginID);
		
		//上部パネルの下パネルに教室選択および教室概要ボタンを追加
		panelNorthSub2 = new Panel();
		panelNorthSub2.add(new Label("教室　 "));
		panelNorthSub2.add(choiceFacility);
		panelNorthSub2.add(new Label(" "));
		panelNorthSub2.add(buttonExplanation);
		//上部パネルの下パネルに教室予約助教確認用の予約状況ボタンを追加
		panelNorthSub2.add(new Label("　　　     "));
		panelNorthSub2.add(new Label(" "));
		panelNorthSub2.add(buttonReservationStatus);

		//上部パネルに上下2つのパネルを追加
		panelNorth = new Panel(new BorderLayout());
		panelNorth.add(panelNorthSub1, BorderLayout.NORTH);
		panelNorth.add(panelNorthSub2, BorderLayout.CENTER);
		
		//MainFrameに上部パネルを追加
		add(panelNorth, BorderLayout.NORTH);
		//中央パネルにテキストメッセージ欄を設定
		panelCenter = new Panel();
		textMessage = new TextArea(20,80);
		textMessage.setEditable(false);
		panelCenter.add(textMessage);
		//MainFrameに中央パネルを追加
		add(panelCenter, BorderLayout.CENTER);
		
		//下部パネルに新規予約ボタン追加
		panelSouth = new Panel();
		panelSouth.add(buttonReservation);
		//下部パネルに自己予約確認ボタン追加
		panelSouth.add(new Label("　　　　　　　　　　　　　　自己予約　"));
		panelSouth.add(buttonSelfCheckReservation);
		panelSouth.add(buttonSelfCheckReservation2);
		//下部パネルに自己予約キャンセルボタン追加
		panelSouth.add(new Label(" "));
		panelSouth.add(buttonCancelReservation);
		
		//メイン画面（MainFrame）に下部パネルを追加
		add(panelSouth, BorderLayout.SOUTH);
		
		//ボタンのアクションリスナの追加
		buttonLog.addActionListener(this);
		buttonExplanation.addActionListener(this);
		buttonReservationStatus.addActionListener(this);
		buttonReservation.addActionListener(this);
		buttonSelfCheckReservation.addActionListener(this);
		buttonSelfCheckReservation2.addActionListener(this);
		buttonCancelReservation.addActionListener(this);
		addWindowListener(this);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String result = new String();
		if (e.getSource() == buttonLog) {								//ログインボタンが押されたとき
			result = reservationControl.loginLogout(this);				//loginLogoutメソッドを呼び出す
		}else if(e.getSource() == buttonExplanation) {					//教室概要ボタンが押されたとき
			result = reservationControl.getFacilityExplanation(choiceFacility.getSelectedItem());//getFacilityExplanationを呼び出す
		}else if(e.getSource() == buttonReservation) {					//新規予約ボタンが押されたとき
			result = reservationControl.makeReservation(this);			//makeReservationを呼び出す
		}else if(e.getSource() == buttonReservationStatus) {			//予約状況ボタンが押されたとき
			result = reservationControl.makeReservationStatus(this);	//makeReservationStatusを呼び出す
		}else if(e.getSource() == buttonSelfCheckReservation) {			//自己予約の確認ボタンが押されたとき
			result = reservationControl.getSelfCheckReservation();		//getFacilityExplanationを呼び出す
		}else if(e.getSource() == buttonSelfCheckReservation2) {		//自己予約の条件指定ボタンが押されたとき
			result = reservationControl.makeSelfCheckReservation(this);	//makeReservationStatusを呼び出す
		}else if(e.getSource() == buttonCancelReservation) {			//自己予約キャンセルボタンが押されたとき
			result = reservationControl.makeCancelReservation(this);		//makeCancelReservationを呼び出す
		}
		textMessage.setText(result);								//テキストエリアにControllerからの戻り値を表示
	}
}