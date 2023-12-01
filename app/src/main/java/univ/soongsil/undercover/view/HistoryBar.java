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
import android.util.TypedValue;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import univ.soongsil.undercover.R;

public class HistoryBar extends ProgressBar {
    private List<Integer> mHistories;
    private List<String> mTexts;
    private boolean mOrientation;
    private Drawable mCurrentMarker;
    private Drawable mPreviousMarker;
    private Drawable mFutureMarker;
    private int mMarkerOffset;
    private int mBarWidth;
    private int mCurrentIndex;
    private int mMarkerRadius;
    private Paint mPrevTextPaint;
    private Paint mCurrentTextPaint;
    private Paint mFutureTextPaint;
    private int mTextOffset;

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
            mOrientation = a.getBoolean(R.styleable.HistoryBar_orientation, false);
            Drawable marker = a.getDrawable(R.styleable.HistoryBar_markerDrawable);
            setMarkers(marker);

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

        mPrevTextPaint.setTextSize(spToPx(14, metrics));
        mCurrentTextPaint.setTextSize(spToPx(14, metrics));
        mFutureTextPaint.setTextSize(spToPx(14, metrics));

        mHistories = new ArrayList<>(List.of(25, 50, 75));
        mCurrentIndex = 1;
        mTexts = new ArrayList<>(List.of("한글 1", "Sample 2", "Sample 3"));
        setProgress(mHistories.get(mCurrentIndex+1));

        setIndeterminate(false);
    }

    private int spToPx(float sp, DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    private int dpToPx(int dp, DisplayMetrics metrics) {
        return (int) (metrics.density * dp + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateMarkerAndTrackPos(w, h, mFutureMarker, getScale(mCurrentIndex+1));
        updateMarkerAndTrackPos(w, h, mPreviousMarker, 0);
        updateMarkerAndTrackPos(w, h, mCurrentMarker, getScale(mCurrentIndex));
    }

    public void setHistories(List<String> labels, List<Integer> progresses) {
        mTexts = labels;
        mHistories = progresses;
        mCurrentIndex = 0;
    }

    public void addHistory(String label, Integer progress) {
        if (mTexts == null) mTexts = new ArrayList<>();
        if (mHistories == null) mHistories = new ArrayList<>();
        mTexts.add(label);
        mHistories.add(progress);
    }

    public void addHistory(String label, Integer progress, Integer index) {
        mTexts.add(index, label);
        mHistories.add(index, progress);
    }

    public void clearHistories() {
        mTexts = new ArrayList<>();
        mHistories = new ArrayList<>();
    }

    public void next() {
        mCurrentIndex++;
        setProgress(mHistories.get(mCurrentIndex+1));
    }

    private void setMarkers(Drawable marker) {
        if (marker != null) {
            setFutureMarker(marker.getConstantState().newDrawable());
            setCurrentMarker(marker.getConstantState().newDrawable());
            setPreviousMarker(marker.getConstantState().newDrawable());
        }
    }

    private void setFutureMarker(Drawable marker) {
        final boolean needUpdate;
        needUpdate = setMarker(mFutureMarker, marker);
        mFutureMarker = marker;
        invalidate();

        if (needUpdate) {
            updateMarkerAndTrackPos(getWidth(), getHeight(), mFutureMarker, Math.min(mCurrentIndex + 1, mHistories.size()));
            if (marker != null && marker.isStateful()) {
                int[] state = getDrawableState();
                marker.setState(state);
            }
        }
    }

    private void setCurrentMarker(Drawable marker) {
        final boolean needUpdate;
        needUpdate = setMarker(mCurrentMarker, marker);
        mCurrentMarker = marker;
        invalidate();

        if (needUpdate) {
            updateMarkerAndTrackPos(getWidth(), getHeight(), mCurrentMarker, mCurrentIndex);
            if (marker != null && marker.isStateful()) {
                int[] state = getDrawableState();
                marker.setState(state);
            }
        }
    }

    private void setPreviousMarker(Drawable marker) {
        final boolean needUpdate;
        needUpdate = setMarker(mPreviousMarker, marker);
        mPreviousMarker = marker;
        invalidate();

        if (needUpdate) {
            updateMarkerAndTrackPos(getWidth(), getHeight(), mPreviousMarker, Math.max(mCurrentIndex - 1, 0));
            if (marker != null && marker.isStateful()) {
                int[] state = getDrawableState();
                marker.setState(state);
            }
        }
    }

    private boolean setMarker(Drawable prevMarker, Drawable marker) {
        final boolean needUpdate;

        if (prevMarker != null && marker != prevMarker) {
            prevMarker.setCallback(null);
            needUpdate = true;
        } else {
            needUpdate = false;
        }

        if (marker != null) {
            marker.setCallback(this);
            if (canResolveLayoutDirection()) {
                marker.setLayoutDirection(getLayoutDirection());
            }

            mMarkerOffset = mMarkerRadius / 2;

            if (needUpdate &&
                    (marker.getIntrinsicWidth() != prevMarker.getIntrinsicWidth()
                            || marker.getIntrinsicHeight() != prevMarker.getIntrinsicHeight()))
                requestLayout();
        }

        return needUpdate;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawHistoryBar(canvas);
        drawMarker(canvas, mPreviousMarker);
        drawMarker(canvas, mCurrentMarker);
        drawMarker(canvas, mFutureMarker);
        if (mPreviousMarker != null && mCurrentMarker != null && mFutureMarker != null){
            Paint.FontMetrics metrics = mPrevTextPaint.getFontMetrics();
            if (mCurrentIndex > 0) {
                Rect bounds = mPreviousMarker.getBounds();
                canvas.drawText(mTexts.get(mCurrentIndex - 1), bounds.right + mTextOffset, bounds.bottom - metrics.descent * 2, mPrevTextPaint);
            }

            metrics = mCurrentTextPaint.getFontMetrics();
            Rect bounds = mCurrentMarker.getBounds();
            canvas.drawText(mTexts.get(mCurrentIndex), bounds.right + mTextOffset, bounds.bottom - metrics.descent * 2, mCurrentTextPaint);

            bounds = mFutureMarker.getBounds();
            metrics = mFutureTextPaint.getFontMetrics();
            if (mCurrentIndex + 1 < mTexts.size()) {
                canvas.drawText(mTexts.get(mCurrentIndex + 1), bounds.right + mTextOffset, bounds.bottom - metrics.descent * 2, mFutureTextPaint);
            }
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

    private void updateMarkerAndTrackPos(int width, int height, Drawable marker, float scale) {
        if (mOrientation) {
            updateMarkerAndTrackPosVertical(width, height, marker, scale);
        }
    }

    private void updateMarkerAndTrackPosVertical(int width, int height, Drawable marker, float scale) {
        final int barWidth = mBarWidth;
        final Drawable bar = getProgressDrawable();

        final int markerHeight = mMarkerRadius * 2;

        int barOffset;
        int markerOffset;
        markerOffset = getWidth() / 2 - mMarkerRadius;
        barOffset = getWidth() / 2 - barWidth / 2;

        int barTopOffset = (markerHeight - barWidth) / 2;

        if (bar != null) {
            final int barHeight = height - getPaddingTop() - getPaddingBottom() - barTopOffset * 2;
            bar.setBounds(barOffset, barTopOffset, barOffset + barWidth, barTopOffset + barHeight);
        }

        if (marker != null) {
            setMarkerPosV(height, marker, scale, markerOffset);
        }
    }

    private void setMarkerPosV(int h, Drawable marker, float scale, int offset) {
        int available = h - getPaddingBottom() - getPaddingTop();
        final int markerSize = mMarkerRadius * 2;
        available -= mMarkerOffset * 2;
        available -= markerSize;

        final int markerPos = (int) (scale * available + 0.5f);

        final int left, right;
        if (offset == Integer.MIN_VALUE) {
            final Rect oldBounds = marker.getBounds();
            left = oldBounds.left;
            right = oldBounds.right;
        } else {
            left = offset;
            right = offset + markerSize;
        }

        final int bottom = markerPos + markerSize;

        final Drawable background = getBackground();
        if (background != null) {
            final int offsetX = getPaddingLeft();
            final int offsetY = getPaddingTop() - mMarkerOffset;
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
        return range > 0 ? (mHistories.get(id) - min) / (float) range : 0;
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


        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();


        setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
                resolveSizeAndState(dh, heightMeasureSpec, 0));
    }
}
