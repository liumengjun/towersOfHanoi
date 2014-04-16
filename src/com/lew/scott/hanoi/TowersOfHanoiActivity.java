package com.lew.scott.hanoi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TowersOfHanoiActivity extends Activity implements OnClickListener {
	// 一些常量
	private static final int QUIT_DLG = 0;
	private static final int ABOUT_DLG = 1;
	private static final int HELP_DLG = 2;
	private static final int NEW_GAME_DLG = 3;
	private static final int END_GAME_DLG = 4;
	private static final int RESOLUTION_DLG = 5;

	public static final int NUM_TOWERS = 3;
	public static final int MAX_PLATES_NUM = 15;

	// 一些程序变量
	private PlateStack[] towers = new PlateStack[NUM_TOWERS];
	private int numOfPlates = 3;// 盘子个数

	// 一些界面元素
	private AlertDialog mQuitDlg = null;
	private Dialog mAboutDlg = null;
	private Dialog mHelpDlg = null;
	private Dialog mNewGameDlg = null;
	private Dialog mEndGameDlg = null;
	private Dialog mResolutionDlg = null;

	// 新游戏设定视图中的元素
	private EditText mPlateNumInput = null;
	private Button mBtnDiscard = null;
	private Button mBtnOk = null;

	// 绘制“汉诺塔”视图
	private FoolSurfaceView mySurfaceView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mySurfaceView = (FoolSurfaceView) this.findViewById(R.id.MAIN_VIEW);
		for (int i = 0; i < NUM_TOWERS; i++) {
			this.towers[i] = new PlateStack(MAX_PLATES_NUM);
		}
		for (int i = this.numOfPlates; i > 0; i--) {
			this.towers[0].push(i);
		}
		mySurfaceView.setNumOfPlates(this.numOfPlates);
		mySurfaceView.setTowers(towers);
		mySurfaceView.repaint();
	}

	/**
	 * 创建options菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 充实菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		// 注意：必须要调用超类的方法，否则无法实现意图回调
		return (super.onCreateOptionsMenu(menu));
	}

	// 初始化对话框
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case QUIT_DLG :
				return (initQuitDlg());
			case ABOUT_DLG :
				return (initAboutDlg());
			case HELP_DLG :
				return (initHelpDlg());
			case NEW_GAME_DLG :
				return (initNewGameDlg());
			case END_GAME_DLG :
				return (initENDGameDlg());
			case RESOLUTION_DLG :
				return (initResolutionDlg());
			default :
				return null;
		}
	}

	// 初始化退出对话框
	private Dialog initQuitDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 提示信息
		builder.setMessage("退出游戏?");
		builder.setCancelable(false);
		// 是按钮
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				TowersOfHanoiActivity.this.finish();
			}
		});
		// 否按钮
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});

		mQuitDlg = builder.create();
		return (mQuitDlg);
	}

	// 初始化关于对话框
	private Dialog initAboutDlg() {
		mAboutDlg = new Dialog(this);
		mAboutDlg.setContentView(R.layout.about_view);
		mAboutDlg.setTitle("汉诺塔（TowersOfHanoi)");

		return (mAboutDlg);
	}

	// 初始化帮助对话框
	private Dialog initHelpDlg() {
		mHelpDlg = new Dialog(this);
		mHelpDlg.setContentView(R.layout.help_view);
		mHelpDlg.setTitle("如何玩\"汉诺塔\":");

		return (mHelpDlg);
	}

	// 初始化新游戏设置盘子数对话框
	private Dialog initNewGameDlg() {
		mNewGameDlg = new Dialog(this);
		// LayoutInflater inflater = this.getLayoutInflater();
		LayoutInflater inflater = mNewGameDlg.getLayoutInflater();
		View newGameSettingView = inflater.inflate(R.layout.new_game_view, null);
		// 必须先填充，再获取其子组件
		mNewGameDlg.setContentView(newGameSettingView);
		mNewGameDlg.setTitle("盘子数(1 - " + (MAX_PLATES_NUM - 1) + "个)：");

		mPlateNumInput = (EditText) newGameSettingView.findViewById(R.id.plate_num_input);
		mBtnDiscard = (Button) newGameSettingView.findViewById(R.id.BTN_DISCARD);
		mBtnOk = (Button) newGameSettingView.findViewById(R.id.BTN_OK);

		mBtnDiscard.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);

		return (mNewGameDlg);
	}

	// 重玩本局
	private Dialog initENDGameDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 提示信息
		builder.setMessage("结束本局，重玩本局?");
		builder.setCancelable(false);
		// 是按钮
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				TowersOfHanoiActivity.this.initiateGame();
			}
		});
		// 否按钮
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});

		mEndGameDlg = builder.create();
		return (mEndGameDlg);
	}

	private Dialog initResolutionDlg() {
		mResolutionDlg = new Dialog(this);
		LayoutInflater inflater = mResolutionDlg.getLayoutInflater();
		View newGameSettingView = inflater.inflate(R.layout.solve_view, null);
		TextView resolutionTextview = (TextView) newGameSettingView.findViewById(R.id.resolution_textview);
		Solve solver = new Solve();
		String resolutionString = solver.solveOfHanoi(this.numOfPlates, 1, 2, 3);
		resolutionTextview.setText("一共需要" + solver.step + "步完成：\n" + resolutionString);
		mResolutionDlg.setContentView(newGameSettingView);
		mResolutionDlg.setTitle(this.numOfPlates + "个盘子\"汉诺塔\"解决方案:");

		return (mResolutionDlg);
	}

	/**
	 * options菜单相应函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.new_game : {
				doNewGame();
				break;
			}
			case R.id.resolution : {
				doResolution();
				break;
			}
			case R.id.end_game : {
				doEndGame();
				break;
			}
			case R.id.help : {
				doHelp();
				break;
			}
			case R.id.about : {
				doAbout();
				break;
			}
			case R.id.exit : {
				doQuit();
				break;
			}
		}

		return (super.onOptionsItemSelected(item));
	}

	private void doNewGame() {
		showDialog(NEW_GAME_DLG);
	}

	private void doResolution() {
		this.removeDialog(RESOLUTION_DLG);
		showDialog(RESOLUTION_DLG);
	}

	private void doEndGame() {
		showDialog(END_GAME_DLG);
	}

	private void doHelp() {
		showDialog(HELP_DLG);
	}

	private void doAbout() {
		showDialog(ABOUT_DLG);
	}

	private void doQuit() {
		showDialog(QUIT_DLG);
	}

	/**
	 * 按钮点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.BTN_DISCARD : {
				mNewGameDlg.dismiss();
				break;
			}
			case R.id.BTN_OK : {
				doNewGameSetting();
				mNewGameDlg.dismiss();
				break;
			}
		}
	}
	
	/**
	 * 设定盘子数
	 * @param numOfPlates
	 */
	public void setNumOfPlates(int numOfPlates){
		if (numOfPlates < 1 || numOfPlates >= MAX_PLATES_NUM) {
			return;
		}
		this.numOfPlates = numOfPlates;
	}

	// 设定盘子数处理
	private void doNewGameSetting() {
		String temp = mPlateNumInput.getText().toString();
		try {
			this.numOfPlates = Integer.parseInt(temp);
		} catch (Exception e) {
			Toast.makeText(this, "设定盘子数错误!", Toast.LENGTH_SHORT).show();
			mPlateNumInput.setText("" + this.numOfPlates);
			return;
		}
		if (this.numOfPlates < 1 || this.numOfPlates >= MAX_PLATES_NUM) {
			Toast.makeText(this, "设定盘子数越界!", Toast.LENGTH_SHORT).show();
			mPlateNumInput.setText("" + this.numOfPlates);
			return;
		}
		// 设定盘子，开始新游戏
		initiateGame();
	}

	// 初始化游戏，绘制汉诺塔视图
	protected void initiateGame() {
		// 初始化 塔和盘子
		for (int i = 0; i < NUM_TOWERS; i++) {
			this.towers[i] = new PlateStack(MAX_PLATES_NUM);
		}
		for (int i = this.numOfPlates; i > 0; i--) {
			this.towers[0].push(i);
		}
		// 绘制视图
		mySurfaceView.setNumOfPlates(this.numOfPlates);
		mySurfaceView.setTowers(towers);
		mySurfaceView.repaint();
	}

}