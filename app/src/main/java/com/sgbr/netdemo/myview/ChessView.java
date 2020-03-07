package com.sgbr.netdemo.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.sgbr.netdemo.R;
import com.sgbr.netdemo.rule.MyChessRules;

import java.util.ArrayList;

public class ChessView extends View {
    public static final String TAG = "ChessView";

    // 存储恢复
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    // 尺寸相关成员变量
    public static final int MAX_LINE = 8;
    private int mViewSize;
    private float mGridSize;

    // 棋子相关
    private static final float RATIO_PIECE = 0.75f;
    private static int pieceSize;
    private boolean mIsWhite = true;// 当前是白棋下
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    // 画图
    private Paint mPaint = new Paint();

    /**
     * new 对象调用
     *
     * @param context 上下文对象
     */
    public ChessView(Context context) {
        this(context, null);
    }

    /**
     * 从xml中调用
     *
     * @param context 上下文对象
     * @param attrs   xml资源对象
     */
    public ChessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 从xml中调用，并且指定style
     *
     * @param context      上下文对象
     * @param attrs        xml资源对象
     * @param defStyleAttr style样式对象
     */
    public ChessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void reStart() {
        mIsWhite = true;
        mWhiteArray.clear();
        mBlackArray.clear();
        invalidate();
    }

    private void init() {
        // 1、初始化画笔
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        // 2、初始化棋子
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_black);
    }


    /**
     * 测量尺寸的方法：
     * 为了适配各种机型，先要对手机进行适配测量
     *
     * @param widthMeasureSpec  默认
     * @param heightMeasureSpec 默认
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int viewSize = Math.min(widthSize, heightSize);

        // 一般是针对scrollview组合的情景
        if (withMode == MeasureSpec.UNSPECIFIED) {
            viewSize = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            viewSize = widthSize;
        }

        setMeasuredDimension(viewSize, viewSize);
    }

    /**
     * 适配测量完成之后，需要对各尺寸参数如：进行初始化，以便在onDraw(canvas)中准确画出来
     * (1)棋盘网格尺寸
     * (2)棋子尺寸
     *
     * @param w    控件宽度
     * @param h    控件高度
     * @param oldw 旧有的控件宽度
     * @param oldh 旧有的控件高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 1、初始化棋盘网格尺寸
        mViewSize = w;
        mGridSize = mViewSize * 1.0f / MAX_LINE;

        // 2、初始化棋子尺寸
        pieceSize = (int) (mGridSize * RATIO_PIECE);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceSize, pieceSize, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceSize, pieceSize, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);

        drawPiece(canvas);
    }

    private void drawPiece(Canvas canvas) {
        // 画白棋子
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, whitePoint.x * mGridSize - pieceSize / 2, whitePoint.y * mGridSize - pieceSize / 2, null);
        }

        // 画黑棋子
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece, blackPoint.x * mGridSize - pieceSize / 2, blackPoint.y * mGridSize - pieceSize / 2, null);
        }

    }

    private void drawBoard(Canvas canvas) {
        canvas.drawRect(1 * mGridSize, 1 * mGridSize, 7 * mGridSize, 7 * mGridSize, mPaint);
        canvas.drawRect(2 * mGridSize, 2 * mGridSize, 6 * mGridSize, 6 * mGridSize, mPaint);
        canvas.drawRect(3 * mGridSize, 3 * mGridSize, 5 * mGridSize, 5 * mGridSize, mPaint);

        canvas.drawLine(1 * mGridSize, 4 * mGridSize, 3 * mGridSize, 4 * mGridSize, mPaint);
        canvas.drawLine(5 * mGridSize, 4 * mGridSize, 7 * mGridSize, 4 * mGridSize, mPaint);
        canvas.drawLine(4 * mGridSize, 1 * mGridSize, 4 * mGridSize, 3 * mGridSize, mPaint);
        canvas.drawLine(4 * mGridSize, 5 * mGridSize, 4 * mGridSize, 7 * mGridSize, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = Math.round(event.getX() / mGridSize);
            int y = Math.round(event.getY() / mGridSize);

            if (!MyChessRules.getValidPoint(x, y)) {
                Log.d(TAG, "非法点击");
                return false;
            }
            Log.d(TAG, x + ", " + y);

            Point p = new Point(x, y);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                Log.d(TAG, "该处存在棋子");
                return false;
            }

            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // 1、存默认
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());

        // 2、存棋子位置
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);

            // 默认
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
