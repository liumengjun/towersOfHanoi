package com.lew.scott.hanoi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class FoolSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext = null;
	// private Bitmap mBkgImage = null;
	// �õ���Ļ��С
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private int xUnit;
	private int[] xTowerPos = null;
	private int yTowerTop;
	private int yTowerBottom;
	// �䵱���Ӻ���
	private PlateStack[] towers = null;
	private int numOfPlates = 3;// ���Ӹ���

	public FoolSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mPath = new Path();

		getHolder().addCallback(this);

		// Resources res = mContext.getResources();
		// mBkgImage = BitmapFactory.decodeResource(res, R.drawable.background);
	}

	public void setTowers(PlateStack[] towers) {
		this.towers = towers;
	}

	public void setNumOfPlates(int numOfPlates) {
		this.numOfPlates = numOfPlates;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mScreenWidth = holder.getSurfaceFrame().width();
		mScreenHeight = holder.getSurfaceFrame().height();
		Log.i("lew.scott.out", "mScreenHeight: " + mScreenHeight);
		Log.i("lew.scott.out", "mScreenWidth: " + mScreenWidth);
		// ���ĺ�������
		xUnit = (mScreenWidth - 10) / 6;
		xTowerPos = new int[]{5 + xUnit, 5 + 3 * xUnit, 5 + 5 * xUnit};
		Log.i("lew.scott.out", "xUnit: " + xUnit);
		// ���ĸ߶�
		yTowerTop = mScreenHeight / 9;
		yTowerBottom = mScreenHeight - yTowerTop - yTowerTop;
		Log.i("lew.scott.out", "yTowerTop: " + yTowerTop);
		Log.i("lew.scott.out", "yTowerBottom: " + yTowerBottom);

		// ����ǰ����
		Canvas canvas = holder.lockCanvas(null);
		// ��ʼ����
		drawTowers(canvas);
		drawPlates(canvas);
		// ����
		holder.unlockCanvasAndPost(canvas);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// TODO Auto-generated method stub
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	// ���»��ơ��������ӡ�
	protected void repaint() {
		SurfaceHolder holder = null;
		try {
			holder = getHolder();
		} catch (Exception e) {
		}
		if (holder != null) {
			// ����ǰ����
			Canvas canvas = holder.lockCanvas(null);
			if (canvas != null) {
				// ���
				canvas.drawColor(Color.BLACK, Mode.CLEAR);
				// ��ʼ����
				drawTowers(canvas);
				drawPlates(canvas);
				// ����
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	// ����y���꣬�����Ƿ�Խ��
	protected boolean isOutBoundByY(float ypos) {
		return (ypos < yTowerTop || ypos > yTowerBottom);
	}

	// ����x���꣬�������ĸ�tower
	protected int calcTowerNumByX(float xpos) {
		int now = -1;
		if (xpos > 5 && xpos < 5 + 2 * xUnit) {
			now = 0;
		} else if (xpos > 5 + 2 * xUnit && xpos < 5 + 4 * xUnit) {
			now = 1;
		} else if (xpos > 5 + 4 * xUnit && xpos < 5 + 6 * xUnit) {
			now = 2;
		}
		return now;
	}

	// �ƶ�����
	protected void moveDisk(int now, int next) {
		if (this.towers[now].ptr == 0) {
			// Toast.makeText(this.mContext, "���ܴӿ������Ƴ����ӣ�",
			// Toast.LENGTH_SHORT).show();
			return;
		}
		if ((this.towers[next].ptr == 0) || (this.towers[now].top() < this.towers[next].top())) {
			int x = this.towers[now].pop();
			this.towers[next].push(x);
			this.repaint();
		} else {
			Toast.makeText(this.mContext, "������Ѵ����ӷ���С������!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (this.towers[1].ptr == this.numOfPlates) {
			Toast.makeText(this.mContext, "��ϲ�㣬�������������һ�ְɣ�", Toast.LENGTH_SHORT).show();
			try{
				Thread.sleep(1000);
			}catch (Exception e) {}
			((TowersOfHanoiActivity)(this.mContext)).setNumOfPlates(this.numOfPlates+1);
			((TowersOfHanoiActivity)(this.mContext)).initiateGame();
		}
	}

	// �����������ӣ�������
	private void drawTowers(Canvas canvas) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.rgb(204, 170, 129));
		paint.setTextSize(32.0f);
		paint.setStrokeWidth(8.0f);
		// ����������
		for (int i = 0; i < 3; i++) {
			canvas.drawLine(xTowerPos[i], yTowerTop, xTowerPos[i], yTowerBottom, paint);
		}
		// ���Ƶ���
		paint.setStrokeWidth(12.0f);
		paint.setColor(Color.rgb(139, 95, 60));
		canvas.drawLine(0, yTowerBottom, mScreenWidth, yTowerBottom, paint);
		// д����
		paint.setTextSize(24.0f);
		canvas.drawText("Tower1", xTowerPos[0] - 40, yTowerBottom + 30, paint);
		canvas.drawText("Tower2", xTowerPos[1] - 40, yTowerBottom + 30, paint);
		canvas.drawText("Tower3", xTowerPos[2] - 40, yTowerBottom + 30, paint);
	}

	// ��������
	private void drawPlates(Canvas canvas) {
		if (towers == null || towers[0] == null || towers[1] == null || towers[2] == null || numOfPlates < 1
				|| numOfPlates > 14) {
			return;
		}
		// ��������
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(32.0f);
		paint.setStrokeWidth(2);
		// ���ӿ��
		int factor = 8;
		if (this.numOfPlates > 5) {
			factor = 10;
		} else if (this.numOfPlates > 7) {
			factor = 13;
		} else if (this.numOfPlates > 9) {
			factor = 16;
		}
		int w = xUnit / factor;
		int h = 22;
		// ������
		for (int i = 0; i < TowersOfHanoiActivity.NUM_TOWERS; i++) {
			for (int j = 0; j < this.towers[i].ptr; j++) {
				int d = this.towers[i].data[j];
				d++;
				int k = j + 1;
				// (left,top) (right,bottom)
				int left = xTowerPos[i] - d * w;
				int top = yTowerBottom - k * h - 6;
				int right = xTowerPos[i] + d * w;
				int bottom = yTowerBottom - k * h + h - 6;
				// ����һ��������
				paint.setColor(Color.CYAN);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(left, top + 1, right, bottom - 1, paint);
				// ���ƾ��α߿�
				paint.setColor(Color.MAGENTA);
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawRect(left - 1, top, right + 1, bottom, paint);
			}
		}
	}

	/**
	 * �����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		// Log.i("lew.scott.move", "(" + x + "," + y + ") onTouchEvent");

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				Log.i("lew.scott.move", "(" + x + "," + y + ") start");
				touch_start(x, y);
				break;
			case MotionEvent.ACTION_MOVE :
				// Log.i("lew.scott.move", "(" + x + "," + y + ") move");
				touch_move(x, y);
				break;
			case MotionEvent.ACTION_UP :
				Log.i("lew.scott.move", "(" + x + "," + y + ") up");
				touch_up(x, y);
				this.repaint();
				break;
		}
		return true;
	}

	private float mX, mY;
	private float preX, preY;
	private Path mPath;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		this.repaint();
		preX = mX = x;
		preY = mY = y;
		mPath.reset();
		mPath.moveTo(x, y);
		this.repaint();
	}
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			drawTouchPath();
		}
	}
	private void touch_up(float x, float y) {
		mPath.lineTo(mX, mY);
		drawTouchPath();
		mPath.reset();

		float rearX = x;
		float rearY = y;
		int preTower = calcTowerNumByX(preX);
		int rearTower = calcTowerNumByX(rearX);
		if (isOutBoundByY(preY) || isOutBoundByY(rearY)) {
			return;
		}
		if (preTower != rearTower && preTower > -1 && preTower < 3 && rearTower > -1 && rearTower < 3) {
			moveDisk(preTower, rearTower);
		}
	}

	private void drawTouchPath() {
		SurfaceHolder holder = null;
		try {
			holder = getHolder();
		} catch (Exception e) {
		}
		if (holder != null) {
			// ����ǰ����
			Canvas canvas = holder.lockCanvas(null);
			if (canvas != null) {
				// ��������
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setDither(true);
				paint.setTextSize(32.0f);
				paint.setColor(Color.GREEN);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeJoin(Paint.Join.ROUND);
				paint.setStrokeCap(Paint.Cap.ROUND);
				paint.setStrokeWidth(5);
				canvas.drawPath(mPath, paint);
				// ����
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}