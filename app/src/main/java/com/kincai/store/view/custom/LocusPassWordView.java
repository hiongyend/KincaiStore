package com.kincai.store.view.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kincai.store.model.ILockCompleteListener;
import com.kincai.store.utils.DensityUtils;
import com.kincai.store.utils.EncryptionUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义九宫格解锁
 */
public class LocusPassWordView extends View {
	private float width = 0;
	private float height = 0;

	//
	private boolean isCache = false;
	//
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	// 3*3的布局 9个点
	private Point[][] mPoints = new Point[3][3];
	//
	private float dotRadius = 0;
	//存放选中点的集合
	private List<Point> sPoints = new ArrayList<Point>();
	private boolean checking = false;
	private long CLEAR_TIME = 1000;
	private int pwdMaxLen = 9;// 密码做大线数 (两点连线数量)
	private int pwdMinLen = 1;// 密码做小线数
	private boolean isTouch = true;

	private Paint arrowPaint;
	private Paint linePaint;
	private Paint selectedPaint;
	private Paint errorPaint;
	private Paint normalPaint;
	// 颜色
	private int errorColor = 0xffea0945;
	private int selectedColor = 0xff0596f6;
	private int outterSelectedColor = 0xff8cbad8;
	private int outterErrorColor = 0xff901032;
	private int dotColor = 0xffd9d9d9;
	private int outterDotColor = 0xff929292;

	public LocusPassWordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LocusPassWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LocusPassWordView(Context context) {
		super(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!isCache) {
			initCache();
		}
		drawToCanvas(canvas);
	}

	/**
	 * 在画布上绘制
	 * 
	 * @param canvas
	 */
	private void drawToCanvas(Canvas canvas) {
		boolean inErrorState = false;
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (p.state == Point.STATE_CHECK) {
					selectedPaint.setColor(outterSelectedColor);
					canvas.drawCircle(p.x, p.y, dotRadius, selectedPaint);
					selectedPaint.setColor(selectedColor);
					canvas.drawCircle(p.x, p.y, dotRadius / 4, selectedPaint);
				} else if (p.state == Point.STATE_CHECK_ERROR) {
					inErrorState = true;
					errorPaint.setColor(outterErrorColor);
					canvas.drawCircle(p.x, p.y, dotRadius, errorPaint);
					errorPaint.setColor(errorColor);
					canvas.drawCircle(p.x, p.y, dotRadius / 4, errorPaint);
				} else {
					normalPaint.setColor(dotColor);
					canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
					normalPaint.setColor(outterDotColor);
					canvas.drawCircle(p.x, p.y, dotRadius / 4, normalPaint);
				}
			}
		}

		if (inErrorState) {
			arrowPaint.setColor(errorColor);
			linePaint.setColor(errorColor);
		} else {
			arrowPaint.setColor(selectedColor);
			linePaint.setColor(selectedColor);
		}

		if (sPoints.size() > 0) {
			int tmpAlpha = mPaint.getAlpha();
			Point tp = sPoints.get(0);
			for (int i = 1; i < sPoints.size(); i++) {
				Point p = sPoints.get(i);
				drawLine(tp, p, canvas, linePaint);
				drawArrow(canvas, arrowPaint, tp, p, dotRadius / 4, 38);
				tp = p;
			}
			if (this.movingNoPoint) {
				drawLine(tp, new Point(moveingX, moveingY, -1), canvas,
						linePaint);
			}
			mPaint.setAlpha(tmpAlpha);
		}

	}

	/**
	 * 绘制直线
	 * 
	 * @param start
	 * @param end
	 * @param canvas
	 * @param paint
	 */
	private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
		double d = distance(start.x, start.y, end.x, end.y);
		float rx = (float) ((end.x - start.x) * dotRadius / 4 / d);
		float ry = (float) ((end.y - start.y) * dotRadius / 4 / d);
		canvas.drawLine(start.x + rx, start.y + ry, end.x - rx, end.y - ry,
				paint);
	}

	/**
	 * 绘制小箭头
	 * 
	 * @param canvas
	 * @param paint
	 * @param start
	 * @param end
	 * @param arrowHeight
	 * @param angle
	 */
	private void drawArrow(Canvas canvas, Paint paint, Point start, Point end,
			float arrowHeight, int angle) {
		double d = distance(start.x, start.y, end.x, end.y);
		float sin_B = (float) ((end.x - start.x) / d);
		float cos_B = (float) ((end.y - start.y) / d);
		float tan_A = (float) Math.tan(Math.toRadians(angle));
		float h = (float) (d - arrowHeight - dotRadius * 1.1);
		float l = arrowHeight * tan_A;
		float a = l * sin_B;
		float b = l * cos_B;
		float x0 = h * sin_B;
		float y0 = h * cos_B;
		float x1 = start.x + (h + arrowHeight) * sin_B;
		float y1 = start.y + (h + arrowHeight) * cos_B;
		float x2 = start.x + x0 - b;
		float y2 = start.y + y0 + a;
		float x3 = start.x + x0 + b;
		float y3 = start.y + y0 - a;
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close();
		canvas.drawPath(path, paint);
	}

	/**
	 * 初始化
	 */
	private void initCache() {
		width = this.getWidth();
		height = this.getHeight();
		float x = 0;
		float y = 0;

		if (width > height) {
			x = (width - height) / 2;
			width = height;
		} else {
			y = (height - width) / 2;
			height = width;
		}

		int leftPadding = 15;
		float dotPadding = width / 3 - leftPadding;
		float middleX = width / 2;
		float middleY = height / 2;

		mPoints[0][0] = new Point(x + middleX - dotPadding, y + middleY
				- dotPadding, 1);
		mPoints[0][1] = new Point(x + middleX, y + middleY - dotPadding, 2);
		mPoints[0][2] = new Point(x + middleX + dotPadding, y + middleY
				- dotPadding, 3);
		mPoints[1][0] = new Point(x + middleX - dotPadding, y + middleY, 4);
		mPoints[1][1] = new Point(x + middleX, y + middleY, 5);
		mPoints[1][2] = new Point(x + middleX + dotPadding, y + middleY, 6);
		mPoints[2][0] = new Point(x + middleX - dotPadding, y + middleY
				+ dotPadding, 7);
		mPoints[2][1] = new Point(x + middleX, y + middleY + dotPadding, 8);
		mPoints[2][2] = new Point(x + middleX + dotPadding, y + middleY
				+ dotPadding, 9);

		Log.d("jerome", "canvas width:" + width);
		dotRadius = width / 10;
		isCache = true;

		initPaints();
	}

	/**
	 * 初始化画笔数据
	 */
	private void initPaints() {
		arrowPaint = new Paint();
		arrowPaint.setColor(selectedColor);
		arrowPaint.setStyle(Style.FILL);
		arrowPaint.setAntiAlias(true);

		linePaint = new Paint();
		linePaint.setColor(selectedColor);
		linePaint.setStyle(Style.STROKE);
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(dotRadius / 9);

		selectedPaint = new Paint();
		selectedPaint.setStyle(Style.STROKE);
		selectedPaint.setAntiAlias(true);
		selectedPaint.setStrokeWidth(dotRadius / 6);

		errorPaint = new Paint();
		errorPaint.setStyle(Style.STROKE);
		errorPaint.setAntiAlias(true);
		errorPaint.setStrokeWidth(dotRadius / 6);

		normalPaint = new Paint();
		normalPaint.setStyle(Style.STROKE);
		normalPaint.setAntiAlias(true);
		normalPaint.setStrokeWidth(dotRadius / 9);
	}

	/**
	 * 
	 * 得到数组索引
	 * 
	 * @param index
	 * @return
	 */
	public int[] getArrayIndex(int index) {
		int[] ai = new int[2];
		ai[0] = index / 3;
		ai[1] = index % 3;
		return ai;
	}

	/**
	 * 检查选择的点
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Point checkSelectPoint(float x, float y) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (checkInRound(p.x, p.y, dotRadius, (int) x, (int) y)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * 重置
	 */
	private void reset() {
		for (Point p : sPoints) {
			p.state = Point.STATE_NORMAL;
		}
		sPoints.clear();
		this.enableTouch();
	}

	/**
	 * 
	 * 交叉点判断
	 * 
	 * @param p
	 * @return
	 */
	private int crossPoint(Point p) {
		// reset
		if (sPoints.contains(p)) {
			if (sPoints.size() > 2) {
				//
				if (sPoints.get(sPoints.size() - 1).index != p.index) {
					return 2;
				}
			}
			return 1; //
		} else {
			return 0; //
		}
	}

	/**
	 * 
	 * 添加点到集合
	 * 
	 * @param point
	 */
	private void addPoint(Point point) {
		if (sPoints.size() > 0) {
			Point lastPoint = sPoints.get(sPoints.size() - 1);
			int dx = Math.abs(lastPoint.getColNum() - point.getColNum());
			int dy = Math.abs(lastPoint.getRowNum() - point.getRowNum());
			if ((dx > 1 || dy > 1) && (dx == 0 || dy == 0 || dx == dy)) {
				// if ((dx > 1 || dy > 1) && (dx != 2 * dy) && (dy != 2 * dx)) {
				int middleIndex = (point.index + lastPoint.index) / 2 - 1;
				Point middlePoint = mPoints[middleIndex / 3][middleIndex % 3];
				if (middlePoint.state != Point.STATE_CHECK) {
					middlePoint.state = Point.STATE_CHECK;
					sPoints.add(middlePoint);
				}
			}
		}
		this.sPoints.add(point);
	}

	/**
	 * 
	 * 获取点所对应的数字
	 * 
	 * @param points
	 * @return
	 */
	private String toPointString() {
		if (sPoints.size() >= pwdMinLen && sPoints.size() <= pwdMaxLen) {
			StringBuffer sf = new StringBuffer();
			for (Point p : sPoints) {
				sf.append(p.index);
			}
			return EncryptionUtil.md5Encryption(sf.toString());
		} else {
			return "";
		}
	}

	boolean movingNoPoint = false;
	float moveingX, moveingY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//
		if (!isTouch) {
			return false;
		}

		movingNoPoint = false;

		float ex = event.getX();
		float ey = event.getY();
		boolean isFinish = false;
		boolean redraw = false;
		Point p = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 按下
			//
			if (task != null) {
				task.cancel();
				task = null;
				Log.d("task", "touch cancel()");
			}
			//
			reset();
			p = checkSelectPoint(ex, ey);
			if (p != null) {
				checking = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:// 移动
			if (checking) {
				p = checkSelectPoint(ex, ey);
				if (p == null) {
					movingNoPoint = true;
					moveingX = ex;
					moveingY = ey;
				}
			}
			break;
		case MotionEvent.ACTION_UP:// 抬起 检查点
			p = checkSelectPoint(ex, ey);
			checking = false;
			isFinish = true;
			break;
		}
		if (!isFinish && checking && p != null) {

			int rk = crossPoint(p);
			if (rk == 2) {
				

				movingNoPoint = true;
				moveingX = ex;
				moveingY = ey;

				redraw = true;
			} else if (rk == 0) {
				p.state = Point.STATE_CHECK;
				addPoint(p);
				redraw = true;
			}
			

		}

		if (isFinish) {
			if (this.sPoints.size() == 1) {// 只有一个点的时候
				// 重置
				this.reset();
			} else if (sPoints.size() < pwdMinLen || sPoints.size() > pwdMaxLen) {

				if (mILockCompleteListener != null) {
					this.disableTouch();
					mILockCompleteListener.onLockComplete(toPointString());
				}
			} else if (mILockCompleteListener != null) {
				this.disableTouch();
				mILockCompleteListener.onLockComplete(toPointString());
			}
		}
		this.postInvalidate();
		return true;
	}

	/**
	 * 错误
	 */
	private void error() {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
	}

	public void enableTouch() {
		isTouch = true;
	}

	public void disableTouch() {
		isTouch = false;
	}

	private Timer timer = new Timer();
	private TimerTask task = null;

	/**
	 * 清除密码
	 */
	public void clearPassword() {
		clearPassword(CLEAR_TIME);
	}

	/**
	 * 清除密码
	 * 
	 * @param time
	 */
	public void clearPassword(final long time) {
		if (time > 1) {
			if (task != null) {
				task.cancel();
			}
			postInvalidate();
			task = new TimerTask() {
				public void run() {
					reset();
					postInvalidate();
				}
			};
			timer.schedule(task, time);
		} else {
			reset();
			postInvalidate();
		}

	}

	private ILockCompleteListener mILockCompleteListener;

	/**
	 * 设置手势完成监听
	 * 
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(ILockCompleteListener iLockCompleteListener) {
		this.mILockCompleteListener = iLockCompleteListener;
	}

	/**
	 * 密码错误调用
	 */
	public void passwordError() {
		error();
		clearPassword();
	}

	/**
	 * 点
	 */
	public static class Point {
		public static int STATE_NORMAL = 0;
		public static int STATE_CHECK = 1; //
		public static int STATE_CHECK_ERROR = 2; // 错误

		public float x;
		public float y;
		public int state = 0;
		public int index = 0;//

		public Point() {

		}

		public Point(float x, float y, int value) {
			this.x = x;
			this.y = y;
			index = value;
		}

		/**
		 * 列
		 * 
		 * @return
		 */
		public int getColNum() {
			return (index - 1) % 3;
		}

		/**
		 * 行
		 * 
		 * @return
		 */
		public int getRowNum() {
			return (index - 1) / 3;
		}

	}
	
	/**
	 * 间隔 计算
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
				+ Math.abs(y1 - y2) * Math.abs(y1 - y2));
	}

	/**
	 * 检测是否在圆内
	 * 
	 * @param sx
	 * @param sy
	 * @param r
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkInRound(float sx, float sy, float r, float x,
			float y) {
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
}
