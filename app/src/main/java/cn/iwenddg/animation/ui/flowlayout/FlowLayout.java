package cn.iwenddg.animation.ui.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.text.TextUtilsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.iwenddg.animation.R;

/**
 * @author iwen大大怪
 * @create 2021/11/12 23:04
 */
public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";
    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;

    /**
     * 默认显示3行 断词条显示3行，长词条显示2行
     */
    private int limitLineCount;

    /**
     * 是否有行限制
     */
    private boolean isLimit;

    /**
     * 是否溢出2行
     */
    private boolean isOverFlow;

    private int mGravity;

    /**
     * 保存所有的view
     */
    protected List<List<View>> mAllViews = new ArrayList<List<View>>();

    /**
     * 保存行高
     */
    protected List<Integer> mLineHeight = new ArrayList<Integer>();

    /**
     * 保存行框
     */
    protected List<Integer> mLineWidth = new ArrayList<Integer>();

    /**
     * 保存每行的view
     */
    private List<View> lineViews = new ArrayList<>();


    public boolean isOverFlow() {
        return isOverFlow;
    }

    private void setOverFlow(boolean overFlow) {
        isOverFlow = overFlow;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean limit) {
        if (!limit) {
            setOverFlow(false);
        }
        isLimit = limit;
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 设置自定义参数
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        // 对齐方式
        mGravity = ta.getInt(R.styleable.TagFlowLayout_tag_gravity, LEFT);
        // 显示行数
        limitLineCount = ta.getInt(R.styleable.TagFlowLayout_limit_line_count, 3);
        // 是否隐藏
        isLimit = ta.getBoolean(R.styleable.TagFlowLayout_is_limit, false);
        int layoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
        if (layoutDirection == LayoutDirection.RTL) {
            if (mGravity == LEFT) {
                mGravity = RIGHT;
            } else {
                mGravity = LEFT;
            }
        }
        ta.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        // 在每一次换行之后记录，是否超过了行数
        int lineCount = 0;// 记录当前的行数

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                if (i == cCount - 1) {
                    if (isLimit) {
                        if (lineCount == limitLineCount) {
                            setOverFlow(true);
                            break;
                        } else {
                            setOverFlow(false);
                        }
                    }

                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                    lineCount++;
                }
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                if (isLimit) {
                    if (lineCount == limitLineCount) {
                        setOverFlow(true);
                        break;
                    } else {
                        setOverFlow(false);
                    }
                }
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
                lineCount++;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                if (isLimit) {
                    if (lineCount == limitLineCount) {
                        setOverFlow(true);
                        break;
                    } else {
                        setOverFlow(false);
                    }
                }
                width = Math.max(lineWidth, width);
                height += lineHeight;
                lineCount++;
            }
        }
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        lineViews.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        // 如果超过规定的行数则不进行绘制
        int lineCount = 0;// 记录当前的行数

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) continue;
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                if (isLimit) {
                    if (lineCount == limitLineCount) {
                        break;
                    }
                }
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                mLineWidth.add(lineWidth);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList<View>();
                lineCount++;
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);
        mAllViews.add(lineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            // set gravity
            int currentLineWidth = this.mLineWidth.get(i);
            switch (this.mGravity) {
                case LEFT:
                    left = getPaddingLeft();
                    break;
                case CENTER:
                    left = (width - currentLineWidth) / 2 + getPaddingLeft();
                    break;
                case RIGHT:
                    // 适配了rtl，需要补偿一个padding值
                    left = width - (currentLineWidth + getPaddingLeft()) - getPaddingRight();
                    // 适配了rtl，需要把lineViews里面的数组倒序排
                    Collections.reverse(lineViews);
                    break;
            }

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            top += lineHeight;
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}

