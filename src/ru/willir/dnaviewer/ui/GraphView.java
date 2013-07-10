package ru.willir.dnaviewer.ui;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import ru.willir.dnaviewer.utils.DLog;
import ru.willir.dnaviewer.utils.DnaAbiData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.view.View.OnLongClickListener;

public class GraphView extends View implements OnLongClickListener {

    private static final int GRAPH_HEADER_HEIGHT = 40;
    private static final int GRPAH_PADDING_LEFT = 8;
    private static final int GRPAH_PADDING_RIGHT = 5;
    private static final int GRPAH_PADDING_BOTTOM = 8;
    private static final int GRAPH_HORIZONTAL_PADDING = GRPAH_PADDING_LEFT + GRPAH_PADDING_RIGHT;

    private static final int IMMEDIATELY_DRAW_VERTICAL_LINE_HEADER_HEIGHT = 50;

    private static final int GRAPH_TEXT_Y_POS = 20;
    private static final int GRAPH_TEXT_SIZE  = 12;

    private static final int VERICAL_MOUSE_LINE_COLOR = Color.LTGRAY;
    private static final int VERICAL_MOUSE_LINE_WIDTH = 1;

    private static final int LINE_STROKE_WIDTH = 1;
    private static final int TEXT_STROKE_WIDTH = 1;

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
    private DnaAbiData mDnaData = null;
    private float lastClickXOrd   = 0;
    private float verticalLineXOrd   = 0;

    public GraphView(Context context) {
        super(context);
        settingPaints();
        Log.d(DLog.TAG, "GraphView::GraphView(context)");
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        settingPaints();
        Log.d(DLog.TAG, "GraphView::GraphView(context, attributeSet)");
    }

    public GraphView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        settingPaints();
        Log.d(DLog.TAG, "GraphView::GraphView(context, attributeSet, defStyle)");
    }

    private void settingPaints() {
        mPaintLine.setStrokeWidth(LINE_STROKE_WIDTH);

        Typeface tf = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mPaintText.setStrokeWidth(TEXT_STROKE_WIDTH);
        mPaintText.setTypeface(tf);
        mPaintText.setTextSize(GRAPH_TEXT_SIZE);

        mPaintVerticalLine.setColor(VERICAL_MOUSE_LINE_COLOR);
        mPaintVerticalLine.setStrokeWidth(VERICAL_MOUSE_LINE_WIDTH);

        this.setOnLongClickListener(this);
    }

    private boolean hasData() {
        return mDnaData != null;
    }

    private void drawSequenceGraph(Canvas canvas, int ibase, Paint paint) {
        final int yMax = getHeight() - GRAPH_HEADER_HEIGHT - GRPAH_PADDING_BOTTOM;

        float xPointPrev = 0;
        float yPointPrev = 0;

        for (int x = 0; x < mDnaData.lastNonTrashPoint; ++x) {
            float y = (float) mDnaData.trace[ibase][x];
            int yNormalized = (int) (y / mDnaData.tmax * yMax);
            yNormalized = yMax - yNormalized + GRAPH_HEADER_HEIGHT;

            float xPoint = GRPAH_PADDING_LEFT + x;
            float yPoint = yNormalized;

            if (x != 0)
                canvas.drawLine(xPointPrev, yPointPrev, xPoint, yPoint, paint);

            xPointPrev = xPoint;
            yPointPrev = yPoint;
        }
    }

    private void drawSequenceText(Canvas canvas) {
        float charHalfWidth = mPaintText.measureText("C") / 2;
        for(int iloop = 0; iloop < mDnaData.basePositions.length; iloop++) {
            char chr = mDnaData.nseq.charAt(iloop);
            float xPos = GRPAH_PADDING_LEFT + mDnaData.basePositions[iloop] - charHalfWidth;

            mPaintText.setColor(BASES_MAP_COLOR.get(chr));
            canvas.drawText(Character.toString(chr), xPos, GRAPH_TEXT_Y_POS, mPaintText);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!hasData())
            return;

        Log.d(DLog.TAG, "onDraw");

        for(int ibase = 0; ibase < 4; ibase++) {
            mPaintLine.setColor(BASES_MAP_COLOR.get(mDnaData.basesOrder.charAt(ibase)));
            drawSequenceGraph(canvas, ibase, mPaintLine);
        }
        drawSequenceText(canvas);
        if(verticalLineXOrd > 0) {
            canvas.drawLine(verticalLineXOrd, 0, verticalLineXOrd, getHeight(), mPaintVerticalLine);
        }
    }

    public void setDnaData(DnaAbiData dnaAbiData) {
        Log.d(DLog.TAG, "setDnaData");
        setLayoutParams(new FrameLayout.LayoutParams(dnaAbiData.lastNonTrashPoint + GRPAH_PADDING_LEFT, getHeight()));
        setMinimumWidth(dnaAbiData.lastNonTrashPoint + GRAPH_HORIZONTAL_PADDING);
        mDnaData = dnaAbiData;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            lastClickXOrd = event.getX();
            if(event.getY() < IMMEDIATELY_DRAW_VERTICAL_LINE_HEADER_HEIGHT)
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
