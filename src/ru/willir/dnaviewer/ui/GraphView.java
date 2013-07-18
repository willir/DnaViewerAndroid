package ru.willir.dnaviewer.ui;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import ru.willir.dnaviewer.utils.DLog;
import ru.willir.dnaviewer.utils.DnaAbiData;
import ru.willir.dnaviewer.utils.SettingsUtils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;

public class GraphView extends View implements OnLongClickListener {

    private static Sizes mSizes = null;

    private class Sizes {

        private static final int GRAPH_HEADER_HEIGHT = 40;
        private static final int GRPAH_PADDING_LEFT = 8;
        private static final int GRPAH_PADDING_RIGHT = 5;
        private static final int GRPAH_PADDING_BOTTOM = 8;

        private static final float TOUCHS_SCREEN_PERCENT = (float) 0.50;

        private static final int GRAPH_TEXT_Y_POS = 20;
        private static final int GRAPH_TEXT_SIZE = 12;

        public int mGraphWidth;

        public int mHeaderHeight = GRAPH_HEADER_HEIGHT;
        public int mPaddingLeft = GRPAH_PADDING_LEFT;
        public int mPaddingRight = GRPAH_PADDING_RIGHT;
        public int mPaddingBottom = GRPAH_PADDING_BOTTOM;
        public int mHorizontalPadding = mPaddingLeft + mPaddingRight;

        public int mTouchsHeight;

        public int mTextYPos = GRAPH_TEXT_Y_POS;
        public int mTextSize = GRAPH_TEXT_SIZE;

        public final int mCursorLineColor = Color.LTGRAY;
        public final int mCursorLineWidth = 1;

        public final int mLineStrokeWidth = 1;
        public final int mTextStrokeWidth = 1;

        public float mXMult;

        public Sizes(float xScale, float yScale) {
            if(!hasData())
                return;

            int height = getHeight();

            mXMult = xScale;

            mHeaderHeight = GRAPH_HEADER_HEIGHT;
            mPaddingLeft = GRPAH_PADDING_LEFT;
            mPaddingRight = GRPAH_PADDING_RIGHT;
            mPaddingBottom = GRPAH_PADDING_BOTTOM;
            mHorizontalPadding = mPaddingLeft + mPaddingRight;

            mTouchsHeight = (int) (height * TOUCHS_SCREEN_PERCENT);

            mTextYPos = GRAPH_TEXT_Y_POS;
            mTextSize = GRAPH_TEXT_SIZE;

            mGraphWidth = (int) Math.ceil(mDnaData.lastNonTrashPoint * mXMult + mHorizontalPadding);
        }
    }

    private void reinitSizes() {
        mSizes = new Sizes(mSettingsUtils.getXScale(), 1);
    }

    private Sizes getSizes() {
        if(mSizes == null)
            reinitSizes();

        return mSizes;
    }

    private static final Map<Character, Integer> BASES_MAP_COLOR;
    static {
        Map<Character, Integer> basesMapColor = new TreeMap<Character, Integer>();
        basesMapColor.put('A', Color.GREEN);
        basesMapColor.put('T', Color.RED);
        basesMapColor.put('G', Color.BLACK);
        basesMapColor.put('C', Color.BLUE);
        BASES_MAP_COLOR = Collections.unmodifiableMap(basesMapColor);
    }

    private Paint mPaintVerticalLine = new Paint();
    private Paint mPaintLine = new Paint();
    private Paint mPaintText = new Paint();
    private float lastClickXOrd = 0;
    private float verticalLineXOrd = 0;

    private SettingsUtils mSettingsUtils = null;
    private DnaAbiData mDnaData = null;

    public GraphView(Context context) {
        super(context);
        init(context);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GraphView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(context);
    }

    private void init(Context ctx) {
        mSettingsUtils = SettingsUtils.getInstance(ctx);
        reinitSizes();
        settingPaints();
    }

    private void settingPaints() {
        Sizes sizes = getSizes();

        mPaintLine.setStrokeWidth(sizes.mLineStrokeWidth);

        Typeface tf = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mPaintText.setStrokeWidth(sizes.mTextStrokeWidth);
        mPaintText.setTypeface(tf);
        mPaintText.setTextSize(sizes.mTextSize);

        mPaintVerticalLine.setColor(sizes.mCursorLineColor);
        mPaintVerticalLine.setStrokeWidth(sizes.mCursorLineWidth);

        this.setOnLongClickListener(this);
    }

    private boolean hasData() {
        return mDnaData != null;
    }

    private void drawSequenceGraph(Canvas canvas, int ibase, Paint paint) {
        Sizes sizes = getSizes();

        final int yMax = getHeight() - sizes.mHeaderHeight - sizes.mPaddingBottom;

        float xPointPrev = 0;
        float yPointPrev = 0;

        for (int x = 0; x < mDnaData.lastNonTrashPoint; ++x) {
            float y = (float) mDnaData.trace[ibase][x];
            int yNormalized = (int) (y / mDnaData.tmax * yMax);
            yNormalized = yMax - yNormalized + sizes.mHeaderHeight;

            float xPoint = sizes.mPaddingLeft + x * sizes.mXMult;
            float yPoint = yNormalized;

            if (x != 0)
                canvas.drawLine(xPointPrev, yPointPrev, xPoint, yPoint, paint);

            xPointPrev = xPoint;
            yPointPrev = yPoint;
        }
    }

    private void drawSequenceText(Canvas canvas) {
        Sizes sizes = getSizes();

        float charHalfWidth = mPaintText.measureText("C") / 2;
        for (int iloop = 0; iloop < mDnaData.basePositions.length; iloop++) {
            char chr = mDnaData.nseq.charAt(iloop);
            float xPos = mDnaData.basePositions[iloop] * sizes.mXMult;
            xPos += sizes.mPaddingLeft - charHalfWidth;

            mPaintText.setColor(BASES_MAP_COLOR.get(chr));
            canvas.drawText(Character.toString(chr), xPos, sizes.mTextYPos, mPaintText);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!hasData())
            return;

        for (int ibase = 0; ibase < 4; ibase++) {
            mPaintLine.setColor(BASES_MAP_COLOR.get(mDnaData.basesOrder.charAt(ibase)));
            drawSequenceGraph(canvas, ibase, mPaintLine);
        }
        drawSequenceText(canvas);
        if (verticalLineXOrd > 0) {
            canvas.drawLine(verticalLineXOrd, 0, verticalLineXOrd, getHeight(), mPaintVerticalLine);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!hasData()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        Sizes sizes = getSizes();

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        int desiredWidth = sizes.mGraphWidth;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        setMeasuredDimension(width, height);
        reinitSizes();
    }

    public void setDnaData(DnaAbiData dnaAbiData) {
        Log.d(DLog.TAG, "setDnaData");
        mDnaData = dnaAbiData;
        requestLayout();
    }

    public void onSettingsChanged() {
        reinitSizes();
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Sizes sizes = getSizes();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            lastClickXOrd = event.getX();
            if (event.getY() < sizes.mTouchsHeight)
                postDrawVerticalLine();
            break;
        default:
            break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onLongClick(View v) {
        postDrawVerticalLine();
        return false;
    }

    private void postDrawVerticalLine() {
        verticalLineXOrd = lastClickXOrd;
        invalidate();
    }
}
