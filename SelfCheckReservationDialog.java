package client_system1;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SelfCheckReservationDialog extends Dialog implements ActionListener, WindowListener, ItemListener{
	
	boolean canceled;										//教室予約状況確認のキャンセルステータス（キャンセル：true）
	ReservationControl rc;									//ReservationControlインスタンス保存用
	
	//パネル
	Panel panelNorth;
	Panel panelCenter;
	Panel panelSouth;
	
	//入力用コンポーネント
	ChoiceFacility_self 			choiceFacility_s;		//教室選択用ボックス
	ChoiceNumberOfReservation		n_reservation;			//表示件数選択ボックス
	//年月日のテキストフィールド
	TextField				stfYear, etfYear, stfMonth, etfMonth, stfDay, etfDay;

	//ボタン
	Button			buttonOK;								//OKボタン
	Button			buttonCancel;							//キャンセルボタン
	
	//コンストラクタ
	public SelfCheckReservationDialog(Frame owner, ReservationControl rc) {
		//基底クラスのコンストラクタを呼び出す
		super(owner, "自己予約状況　　条件指定", true);
		
		this.rc = rc;										//ReservationControlのインスタンスを保存
		
		//初期値キャンセルを設定
		canceled = true;
		
		//テキストフィールドの生成（年月日）
		stfYear		= new TextField("", 4);
		stfMonth	= new TextField("", 2);
		stfDay		= new TextField("", 2);
		etfYear		= new TextField("", 4);
		etfMonth	= new TextField("", 2);
		etfDay		= new TextField("", 2);
		
		//ボタンの生成
		buttonOK	= new Button("　　OK　　");
		buttonCancel= new Button("キャンセル");
		
		//パネルの生成
		panelNorth	= new Panel();
		panelCenter = new Panel();
		panelSouth	= new Panel();
		
		// ChoiceFacility_selfのインスタンス化と代入
		choiceFacility_s = new ChoiceFacility_self();
		n_reservation = new ChoiceNumberOfReservation();
		
		//上部パネルに教室選択ボックスを配置
		panelNorth.add(new Label("教室"));
		panelNorth.add(choiceFacility_s);
		
		//中央パネル上部に年月日入力欄を配置
		Panel panelCenter1 = new Panel();
		panelCenter1.add(new Label("期間"));
		panelCenter1.add(stfYear);
		panelCenter1.add(new Label("年"));
		panelCenter1.add(stfMonth);
		panelCenter1.add(new Label("月"));
		panelCenter1.add(stfDay);
		panelCenter1.add(new Label("日"));
		panelCenter1.add(new Label("～"));
		panelCenter1.add(etfYear);
		panelCenter1.add(new Label("年"));
		panelCenter1.add(etfMonth);
		panelCenter1.add(new Label("月"));
		panelCenter1.add(etfDay);
		panelCenter1.add(new Label("日"));
		
		//中央パネル下部に表示件数ボックスを配置
		Panel panelCenter2 = new Panel();
		panelCenter2.add(new Label("表示件数"));
		panelCenter2.add(n_reservation);
		
		//2つ目と3つ目のパネルを含むパネル（panelCenter）
		panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(2, 1));
		panelCenter.add(panelCenter1);
		panelCenter.add(panelCenter2);
		
		//下部パネルに2つのボタンを追加
		panelSouth.add(buttonCancel);
		panelSouth.add(new Label("　"));
		panelSouth.add(buttonOK);
		
		//SelfCheckReservationDialogをBorderLayoutに設定し, 3つのパネルを追加
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter,BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);
		
		//Window Listenerを追加
		addWindowListener(this);
		//ボタンにアクションリスナを追加
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		//教室選択ボックス, 時・分選択ボックスそれぞれに項目Listenerを追加
		choiceFacility_s.addItemListener(this);
		n_reservation.addItemListener(this);
		
		//大きさの設定, Windowのサイズ変更不可の設定
		this.setBounds(100, 100, 600, 200);
		setResizable(false);
	}
	

	
	@Override
	public void windowOpened(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);									//ダイアログを非表示にする
		dispose();											//Windowのグラフィックリソース解放
		
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowDeiconified(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {
		//TODO　自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonCancel) {					//キャンセルボタン押下時
			setVisible(false);								//WIndowを非表示にする
			dispose();										//Windowのグラフィックリソースを解放
		}else if(e.getSource() == buttonOK) {				//OKボタン押下後
			canceled = false;								//新規予約キャンセルでない状態に設定
			setVisible(false);								//Windowを非表示にする
			dispose();										//Windowのグラフィックリソースを解放
		}		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}	
}