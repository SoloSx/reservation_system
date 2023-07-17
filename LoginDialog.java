package client_system1;

import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends Dialog implements ActionListener, WindowListener {
	boolean		canceled;								//キャンセル：true　OK:false

	TextField	tfUserID;								//ユーザID入力用テキストフィールド
	TextField	tfPassword;								//パスワード入力用テキストフィールド
	
	Button		buttonOK;								//OKボタンインスタンス
	Button		buttonCancel;							//キャンセルボタンインスタンス

	Panel		panelNorth;								//上部パネル
	Panel		panelCenter;							//中央パネル
	Panel		panelSouth;								//下部パネル

	//LoginDialogクラスコンストラクタ
	public	LoginDialog(Frame arg0) {
		super(arg0, "Login", true);						//引数は,　Dialog所有者, タイトル,　モーダル指定
		canceled = true;								//キャンセル状態としておく
		
		//テキストフィールド生成
		tfUserID = new TextField("", 10);				//ユーザID入力用テキストフィールドのインスタンス生成
		tfPassword = new TextField("", 10);				//パスワード入力用テキストフィールドのインスタンス生成
		tfPassword.setEchoChar('*');					//パスワードは*表示とする
		
		//ボタン生成
		buttonOK = new Button("OK");					//OKボタンのインスタンス生成
		buttonCancel = new Button("キャンセル");			//キャンセルボタンのインスタンス生成
		
		//パネルの生成
		panelNorth = new Panel();						//上部パネルのインスタンス生成
		panelCenter = new Panel();						//中央パネルのインスタンス生成
		panelSouth = new Panel();						//下部パネルのインスタンス生成
		
		//上部パネルにユーザIDテキストフィールドを配置
		panelNorth.add(new Label("ユーザーID"));			//ユーザID入力横のラベル
		panelNorth.add(tfUserID);						//ユーザID入力用テキストフィールド
		
		//中央パネルにパスワードテキストフィールドを配置
		panelCenter.add(new Label("パスワード"));			//パスワード入力横のラベル
		panelCenter.add(tfPassword);					//パスワード入力用テキストフィールド
		
		//下部パネルに2つのボタンを配置
		panelSouth.add(buttonCancel);					//キャンセルボタン
		panelSouth.add(buttonOK);						//OKボタン
		
		//LoginDialogをBorderLayoutに設定し, 3つのパネルを追加
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);
		
		//WindowListenerを追加
		addWindowListener(this);
		
		//ボタンにActionListenerを追加
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);								//Windowを不可視化
		canceled = true;								//ログインキャンセルとする
		dispose();										//LoginDialogで使っていたリソース解放
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
		//TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowDeiconified(WindowEvent e) {
		//TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		//TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {
		//TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonCancel) {				//押下されたのがキャンセルボタンのとき
			canceled = true;							//ログインキャンセルとする
		}else if(e.getSource() == buttonOK) {			//押下されたのがOKボタンのとき
			canceled = false;							//認証処理へ
		}
		setVisible(false);								//LoginDialogを不可視化
		dispose();										//LoginDialogで使っていたリソースを解放
	}
}