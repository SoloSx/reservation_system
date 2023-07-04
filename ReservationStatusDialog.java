package client_system;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationStatusDialog extends Dialog implements ActionListener, WindowListener, ItemListener{
	
	boolean canceled;										//教室予約状況確認のキャンセルステータス（キャンセル：true）
	ReservationControl rc;									//ReservationControlインスタンス保存用
	
	//パネル
	Panel panelNorth;
	Panel panelCenter;
	Panel panelSouth;
	
	//入力用コンポーネント
	ChoiceFacility 	choiceFacility;							//教室選択用ボックス
	TextField		tfYear, tfMonth, tfDay;					//年月日のテキストフィールド
	
	//ボタン
	Button			buttonOK;								//OKボタン
	Button			buttonCancel;							//キャンセルボタン
	
	//コンストラクタ
	public ReservationStatusDialog(Frame owner, ReservationControl rc) {
		//基底クラスのコンストラクタを呼び出す
		super(owner, "予約状況", true);
		
		this.rc = rc;										//ReservationControlのインスタンスを保存
		
		//初期値キャンセルを設定
		canceled = true;
		
		//教室選択ボックスの作成
		List<String>facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility(facilityId);
		//テキストフィールドの生成（年月日）
		tfYear		= new TextField("", 4);
		tfMonth		= new TextField("", 2);
		tfDay		= new TextField("", 2);
		
		//ボタンの生成
		buttonOK	= new Button("　　OK　　");
		buttonCancel= new Button("キャンセル");
		
		//パネルの生成
		panelNorth	= new Panel();
		panelCenter = new Panel();
		panelSouth	= new Panel();
		
		//上部パネルに教室選択ボックス, 年月日入力欄を配置
		panelNorth.add(new Label("教室"));
		panelNorth.add(choiceFacility);
		panelNorth.add(new Label("　　日付"));
		panelNorth.add(tfYear);
		panelNorth.add(new Label("年"));
		panelNorth.add(tfMonth);
		panelNorth.add(new Label("月"));
		panelNorth.add(tfDay);
		panelNorth.add(new Label("日"));
		
		//下部パネルに2つのボタンを追加
		panelSouth.add(buttonCancel);
		panelSouth.add(new Label("　"));
		panelSouth.add(buttonOK);
		
		//ReservationStatusDialogをBorderLayoutに設定し, 3つのパネルを追加
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
		choiceFacility.addItemListener(this);
		
		//大きさの設定, Windowのサイズ変更不可の設定
		this.setBounds(100, 100, 500, 150);
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