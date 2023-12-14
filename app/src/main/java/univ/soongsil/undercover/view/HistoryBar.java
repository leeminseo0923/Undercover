package univ.soongsil.undercover.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import univ.soongsil.undercover.R;

public class HistoryBar extends ProgressBar {
    private List<Integer> mProgresses;
    private List<String> mHistories;
    private List<Drawable> mMarkers;
    private Drawable mMarker;
    private int mBarWidth;
    private int mCurrentIndex;
    private int mMarkerRadius;
    private Paint mPrevTextPaint;
    private Paint mCurrentTextPaint;
    private Paint mFutureTextPaint;
    private int mTextOffset;
    private int mLongestTextWidth;
    private int mHistorySize;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public HistoryBar(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #HistoryBar(Context, AttributeSet, int)
     */
    public HistoryBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #HistoryBar(Context, AttributeSet)
     */
    public HistoryBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute or style resource. This constructor of View allows
     * subclasses to use their own base style when they are inflating.
     * <p>
     * When determining the final value of a particular attribute, there are
     * four inputs that come into play:
     * <ol>
     * <li>Any attribute values in the given AttributeSet.
     * <li>The style resource specified in the AttributeSet (named "style").
     * <li>The default style specified by <var>defStyleAttr</var>.
     * <li>The default style specified by <var>defStyleRes</var>.
     * <li>The base values in this theme.
     * </ol>
     * <p>
     * Each of these inputs is considered in-order, with the first listed taking
     * precedence over the following ones. In other words, if in the
     * AttributeSet you have supplied <code>&lt;Button * textColor="#ff000000"&gt;</code>
     * , then the button's text will <em>always</em> be black, regardless of
     * what is specified in any of the styles.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param defStyleRes  A resource identifier of a style resource that
     *                     supplies default values for the view, used only if
     *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
     *                     to not look for defaults.
     * @see #HistoryBar(Context, AttributeSet, int)
     */
    public HistoryBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        init(metrics);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HistoryBar,
                0, 0);

        try {
            mMarker = a.getDrawable(R.styleable.HistoryBar_markerDrawable);

            int initialBarWidthDP = 16;
            int initialMarkerRadiusDP = 12;

            mBarWidth = a.getDimensionPixelOffset(R.styleable.HistoryBar_barThickness, dpToPx(initialBarWidthDP, metrics));
            mMarkerRadius = a.getDimensionPixelOffset(R.styleable.HistoryBar_markerRadius, dpToPx(initialMarkerRadiusDP, metrics));

            mPrevTextPaint.setColor(a.getColor(R.styleable.HistoryBar_previousTextColor, Color.RED));
            mCurrentTextPaint.setColor(a.getColor(R.styleable.HistoryBar_currentTextColor, Color.RED));
            mFutureTextPaint.setColor(a.getColor(R.styleable.HistoryBar_futureTextColor, Color.RED));

            mTextOffset = a.getDimensionPixelOffset(R.styleable.HistoryBar_textOffset, dpToPx(9, metrics));

            setFont(a.getResourceId(R.styleable.HistoryBar_textFont, -1));

        } finally {
            a.recycle();
        }
        setInitialMarker();

    }

    private void setInitialMarker() {
        mMarkers = new ArrayList<>();
        for (int i = 0; i < mHistorySize; i++) {
            if (mMarker != null && mMarker.getConstantState() != null) {
                Drawable marker = mMarker.getConstantState().newDrawable();
                mMarkers.add(marker);
            }
        }
    }

    public String getNextHistory() {
        if (!isLastHistory())
            return mHistories.get(mCurrentIndex + 1);
        else
            return "";
    }

    public String getCurrentHistory() {
        return mHistories.get(mCurrentIndex);
    }

    public boolean isLastHistory() {
        return mHistorySize - 1 == mCurrentIndex;
    }

    private void setFont(int id) {
        if (id == -1) return;
        Typeface typeface = ResourcesCompat.getFont(getContext(), id);
        mPrevTextPaint.setTypeface(typeface);
        mCurrentTextPaint.setTypeface(typeface);
        mFutureTextPaint.setTypeface(typeface);
    }

    private void init(DisplayMetrics metrics) {
        mPrevTextPaint = new Paint();
        mCurrentTextPaint = new Paint();
        mFutureTextPaint = new Paint();

        mPrevTextPaint.setTextSize(spToPx(metrics));
        mCurrentTextPaint.setTextSize(spToPx(metrics));
        mFutureTextPaint.setTextSize(spToPx(metrics));

        List<Integer> progress = new ArrayList<>(List.of(0, 50, 90, 100));
        List<String> history = new ArrayList<>(List.of("한글 1", "Sample 2", "Sample 3", "Sample 4"));
        setHistories(history, progress);
        mCurrentIndex = 1;
        setProgress(mProgresses.get(mCurrentIndex + 1));

        setIndeterminate(false);
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setCurrentIndex(int id) {
        mCurrentIndex = id;
    }

    private int spToPx(DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) 14, metrics);
    }

    private int dpToPx(int dp, DisplayMetrics metrics) {
        return (int) (metrics.density * dp + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        updateTrackPosVertical(h);
        if (mMarkers != null) {
            for (int i = 0; i < mMarkers.size(); i++) {
                setMarkerPosV(h, mMarkers.get(i), getScale(i));
            }
        }
    }

    public void setHistories(List<String> labels, List<Integer> progresses) {
        mHistories = labels;
        mProgresses = progresses;
        mLongestTextWidth = (int) mCurrentTextPaint.measureText(
                mHistories.stream().max(Comparator.comparingInt(String::length)).orElse("")
        );
        mCurrentIndex = 0;
        mHistorySize = mHistories.size();
        if (mCurrentIndex + 1 < mProgresses.size())
            setProgress(mProgresses.get(mCurrentIndex + 1), true);
        else
            setProgress(mProgresses.get(mCurrentIndex), true);

        if (mMarker != null) {
            setInitialMarker();
        }
    }

    public void addHistory(String label, Integer progress) {
        if (mHistories == null) mHistories = new ArrayList<>();
        if (mProgresses == null) mProgresses = new ArrayList<>();
        mHistories.add(label);
        mProgresses.add(progress);
    }

    public void addHistory(String label, Integer progress, Integer index) {
        mHistories.add(index, label);
        mProgresses.add(index, progress);
    }

    public void clearHistories() {
        mHistories = new ArrayList<>();
        mProgresses = new ArrayList<>();
    }

    public void next() {
        if (mCurrentIndex + 1 < mHistorySize) {
            mCurrentIndex++;
        }
        if (mCurrentIndex + 1 < mHistorySize) {
            setProgress(mProgresses.get(mCurrentIndex + 1), true);
        }
        invalidate();
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawHistoryBar(canvas);
        for (int i = 0; i < mCurrentIndex + 2; i++) {
            if (i >= mMarkers.size()) break;
            Drawable marker = mMarkers.get(i);
            drawMarker(canvas, marker);
            Paint.FontMetrics metrics;
            Paint paint;

            if (i < mCurrentIndex)
                paint = mPrevTextPaint;
            else if (i == mCurrentIndex)
                paint = mCurrentTextPaint;
            else
                paint = mFutureTextPaint;

            metrics = paint.getFontMetrics();
            Rect bounds = marker.getBounds();
            canvas.drawText(mHistories.get(i), bounds.right + mTextOffset, bounds.bottom - metrics.descent * 2, paint);
        }
    }

    private void drawMarker(@NonNull Canvas canvas, Drawable marker) {
        if (marker != null) {
            int saveCount = canvas.save();

            canvas.translate(getPaddingLeft(), getPaddingTop());

            marker.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    private void drawHistoryBar(@NonNull Canvas canvas) {
        final Drawable d = getProgressDrawable();
        if (d != null) {
            int saveCount = canvas.save();

            if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                canvas.translate(getWidth() - getPaddingRight(), getPaddingTop());
            } else {
                canvas.translate(getPaddingLeft(), getPaddingTop());
            }

            d.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    private void updateTrackPosVertical(int height) {
        final int barWidth = mBarWidth;
        final Drawable bar = getProgressDrawable();

        final int markerWidth = mMarkerRadius * 2;

        int barOffset;
        barOffset = (markerWidth - barWidth) / 2;


        if (bar != null) {
            final int barHeight = height - getPaddingTop() - getPaddingBottom() - barOffset * 2;
            bar.setBounds(barOffset, barOffset, barOffset + barWidth, barOffset + barHeight);
            Log.d("LMS", String.valueOf(barOffset));
        }
    }

    private void setMarkerPosV(int h, Drawable marker, float scale) {
        int available = h - getPaddingBottom() - getPaddingTop();
        final int markerSize = mMarkerRadius * 2;
        available -= markerSize;

        final int markerPos = (int) (scale * available + 0.5f);

        final int left, right;
        left = 0;
        right = left + markerSize;

        final int bottom = markerPos + markerSize;

        final Drawable background = getBackground();
        if (background != null) {
            final int offsetX = getPaddingLeft();
            final int offsetY = getPaddingTop();
            background.setHotspotBounds(left + offsetX, markerPos + offsetY,
                    right + offsetX, bottom + offsetY);
        }

        marker.setBounds(left, markerPos, right, bottom);
    }

    private float getScale(int id) {
        int min = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            min = getMin();
        }
        int max = getMax();
        int range = max - min;
        return range > 0 ? (mProgresses.get(id) - min) / (float) range : 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;

        Drawable historyBar = getProgressDrawable();

        if (historyBar != null) {
            dw = Math.max(dw, mBarWidth);
            dh = Math.max(dh, historyBar.getIntrinsicHeight());
        }

        dw = Math.max(dw, mMarkerRadius * 2);
        dh = Math.max(dh, mMarkerRadius * 2);
        dh += (mMarkerRadius * 2 - mBarWidth);

        dw += mLongestTextWidth + mTextOffset;


        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();


        setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
                resolveSizeAndState(dh, heightMeasureSpec, 0));
    }
}
