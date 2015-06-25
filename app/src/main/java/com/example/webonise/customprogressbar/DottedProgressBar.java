package com.example.webonise.customprogressbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class DottedProgressBar extends View {
    private final float mDotSize;
    //    private final float mSpacing;
    private final int mJumpingSpeed;
    private final Context context;
    private int mEmptyDotsColor;
    private int mActiveDotColor;
    private Drawable mActiveDot;
    private Drawable mInactiveDot;
    private boolean isInProgress;
    private boolean isActiveDrawable = false;
    private boolean isInactiveDrawable = false;
    private int mActiveDotIndex;
    private int mNumberOfDots;
    private Paint mPaint;
    private int mPaddingLeft;
    private Handler mHandler;
    private int levels;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mNumberOfDots != 0)
                mActiveDotIndex = (mActiveDotIndex + 1) % mNumberOfDots;
            DottedProgressBar.this.invalidate();
            mHandler.postDelayed(mRunnable, mJumpingSpeed);
        }
    };

    public DottedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DottedProgressBar,
                0, 0);

        isInProgress = false;
        mHandler = new Handler();

        try {
//            mEmptyDotsColor = a.getColor(R.styleable.DottedProgressBar_emptyDotsColor, Color.WHITE);
//            mActiveDotColor = a.getColor(R.styleable.DottedProgressBar_activeDotColor, Color.BLUE);

            TypedValue value = new TypedValue();

            a.getValue(R.styleable.DottedProgressBar_activeDot, value);
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // It's a color
                isActiveDrawable = false;
                mActiveDotColor = getResources().getColor(value.resourceId);
            } else if (value.type == TypedValue.TYPE_STRING) {
                // It's a reference, hopefully to a drawable
                isActiveDrawable = true;
                mActiveDot = getResources().getDrawable(value.resourceId);
            }

            a.getValue(R.styleable.DottedProgressBar_inactiveDot, value);
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // It's a color
                isInactiveDrawable = false;
                mEmptyDotsColor = getResources().getColor(value.resourceId);
            } else if (value.type == TypedValue.TYPE_STRING) {
                // It's a reference, hopefully to a drawable
                isInactiveDrawable = true;
                mInactiveDot = getResources().getDrawable(value.resourceId);
            }

            mDotSize = a.getDimensionPixelSize(R.styleable.DottedProgressBar_dotSize, 5);
//            mSpacing = a.getDimensionPixelSize(R.styleable.DottedProgressBar_spacing, 10);

            mActiveDotIndex = a.getInteger(R.styleable.DottedProgressBar_activeDotIndex, 0);
            mNumberOfDots = a.getInteger(R.styleable.DottedProgressBar_noOfDots, 7);

            mJumpingSpeed = a.getInt(R.styleable.DottedProgressBar_jumpingSpeed, 500);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);

        } finally {
            a.recycle();
        }
    }

    public float getmDotSize() {
        return mDotSize;
    }

//    public float getmSpacing() {
//        return mSpacing;
//    }

    public int getmJumpingSpeed() {
        return mJumpingSpeed;
    }

    public int getmEmptyDotsColor() {
        return mEmptyDotsColor;
    }

    public void setmEmptyDotsColor(int mEmptyDotsColor) {
        this.mEmptyDotsColor = mEmptyDotsColor;
    }

    public int getmActiveDotColor() {
        return mActiveDotColor;
    }

    public void setmActiveDotColor(int mActiveDotColor) {
        this.mActiveDotColor = mActiveDotColor;
    }

    public Drawable getmActiveDot() {
        return mActiveDot;
    }

    public void setmActiveDot(Drawable mActiveDot) {
        this.mActiveDot = mActiveDot;
    }

    public Drawable getmInactiveDot() {
        return mInactiveDot;
    }

    public void setmInactiveDot(Drawable mInactiveDot) {
        this.mInactiveDot = mInactiveDot;
    }

    public int getmActiveDotIndex() {
        return mActiveDotIndex;
    }

    public void setmActiveDotIndex(int mActiveDotIndex) {
        this.mActiveDotIndex = mActiveDotIndex;
    }

    public int getmNumberOfDots() {
        return mNumberOfDots;
    }

    public void setmNumberOfDots(int mNumberOfDots) {
        this.mNumberOfDots = mNumberOfDots;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startPoint = (int) (mDotSize / 2) + getPaddingLeft();
        int endPoint = (int) (getWidth() - (mDotSize / 2) - getPaddingRight());

        int _spaceBetweenPoints = (endPoint - startPoint) / (mNumberOfDots - 1);

        int counter = startPoint;


        mPaint.setStrokeWidth(20);
        mPaint.setColor(mEmptyDotsColor);
        canvas.drawLine(startPoint, getHeight() / 2, endPoint, getHeight() / 2, mPaint);


        for (int i = 0; i < levels; i++, counter += _spaceBetweenPoints) {
/*//            int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + i * (mSpacing + mDotSize));
            if (isActiveDrawable) {
                mActiveDot.setBounds(x, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
                mActiveDot.draw(canvas);
            } else {*/
            mPaint.setColor(mActiveDotColor);
            canvas.drawCircle(counter, getPaddingTop() + mDotSize / 2, mDotSize / 2, mPaint);
            /*}*/
        }
        canvas.drawLine(startPoint, getHeight() / 2, counter - _spaceBetweenPoints, getHeight() / 2, mPaint);


        for (int i = levels; i < mNumberOfDots; i++, counter += _spaceBetweenPoints) {
/*//            int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + i * (mSpacing + mDotSize));
            if (isInactiveDrawable) {
                mInactiveDot.setBounds(x, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
                mInactiveDot.draw(canvas);
            } else {*/
            mPaint.setColor(mEmptyDotsColor);
            canvas.drawCircle(counter, getPaddingTop() + mDotSize / 2, mDotSize / 2, mPaint);
            /*}*/
        }

        /*if (isInProgress) {
            int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + mActiveDotIndex * (mSpacing + mDotSize));
            if (isActiveDrawable) {
                mActiveDot.setBounds(x, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
                mActiveDot.draw(canvas);
            } else {
                mPaint.setColor(mActiveDotColor);
                canvas.drawCircle(x + mDotSize / 2,
                        getPaddingTop() + mDotSize / 2, mDotSize / 2, mPaint);
            }
        }*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        /*int widthWithoutPadding = parentWidth - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = parentHeight - getPaddingTop() - getPaddingBottom();*/

        //setMeasuredDimension(parentWidth, calculatedHeight);

        int calculatedHeight = getPaddingTop() + getPaddingBottom() + (int) mDotSize;
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;


        setMeasuredDimension(width - getPaddingLeft() - getPaddingRight(), calculatedHeight);
//        mNumberOfDots = calculateDotsNumber(widthWithoutPadding);
    }

//    private int calculateDotsNumber(int width) {
//        int number = (int) (width / (mDotSize + mSpacing));
//        mPaddingLeft = (int) ((width % (mDotSize + mSpacing)) / 2);
//        //setPadding(getPaddingLeft() + (int) mPaddingLeft, getPaddingTop(), getPaddingRight() + (int) mPaddingLeft, getPaddingBottom());
//        return number;
//    }

    public void startProgress() {
        isInProgress = true;
        mActiveDotIndex = -1;
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopProgress() {
        isInProgress = false;
        mHandler.removeCallbacks(mRunnable);
        invalidate();
    }

    public void setLevel(int levels) {
        if (levels <= mNumberOfDots) {
            this.levels = levels;
        }
        invalidate();
    }

}