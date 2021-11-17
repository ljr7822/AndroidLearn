package cn.iwenddg.animation.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwenddg.animation.R;

/**
 * 自定义单个tab图标
 *
 * @author iwen大大怪
 * @create 2021/11/03 23:51
 */
public class QLNavigationTabView extends FrameLayout {

    // 图标
    @BindView(R.id.iv_tab_icon)
    ImageView mIvTabIcon;
    // 动画
    @BindView(R.id.iv_tab_icon_animation)
    LottieAnimationView mIvTabIconAnimation;
    // 图标底部文字
    @BindView(R.id.tv_tab_text)
    TextView mTvTabText;
    // 小红点
    @BindView(R.id.iv_red_dot)
    ImageView mIvRedDot;
    //
    @BindView(R.id.tv_hint_text)
    TextView mTvHint;
    // 小红点文字
    @BindView(R.id.tv_red_number)
    TextView mTvRedNumber;

    // 第几个图标
    private int mPosition;
    // 正常的颜色
    private int normalColor;
    // 选中的颜色
    private int focusColor;
    // 正常的图标资源
    Resources resources;
    private Drawable mNormalIcon;
    // 选中的图标资源
    private Drawable mFocusIcon;
    // 是否选中：默认为否
    private boolean mIsSelected = false;

    public QLNavigationTabView(@NonNull Context context) {
        this(context,null);
    }

    public QLNavigationTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.nav_tab_view_layout, this,true);
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化tabView
     *
     * @param normal 正常的资源
     * @param focus  选中的资源
     */
    public void initTabView(Drawable normal, Drawable focus) {
        mNormalIcon = normal;
        mFocusIcon = focus;
        mIvTabIcon.setImageDrawable(mNormalIcon);
    }

    /**
     * 是否选中
     */
    public boolean selected() {
        return mIsSelected;
    }

    /**
     * 设置每个tab底部文字
     *
     * @param title 内容
     */
    public void setTabTitle(String title) {
        mTvTabText.setText(title);
    }

    /**
     * 设置动画
     *
     * @param lottieFileName 文件名
     */
    public void setAnimation(String lottieFileName) {
        // 设置动画应重复的次数。如果重复计数为 0，则动画永远不会重复。
        mIvTabIconAnimation.setRepeatCount(0);
        mIvTabIconAnimation.setAnimation(lottieFileName);
        mIvTabIconAnimation.setVisibility(View.VISIBLE);
    }

    /**
     * 从 Json 设置动画
     *
     * @param lottieJsonString json文件名
     */
    public void setAnimationFromJson(String lottieJsonString) {
        mIvTabIconAnimation.setRepeatCount(0);
        mIvTabIconAnimation.setAnimationFromJson(lottieJsonString);
        mIvTabIconAnimation.setVisibility(View.VISIBLE);
    }

    /**
     * 根据选中状态，设置隐藏及动画
     *
     * @param select 选中与否的布尔值
     */
    public void isSelect(boolean select) {
        mTvTabText.setTextColor(select ? focusColor : normalColor);
        if (select) {
            mIvTabIcon.setVisibility(View.GONE);
            mIvTabIconAnimation.setVisibility(View.VISIBLE);
            mIvTabIconAnimation.playAnimation();
        }
        if (!select) {
            mIvTabIcon.setVisibility(View.VISIBLE);
            mIvTabIconAnimation.setVisibility(View.GONE);
        }

        mIsSelected = select;
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float size) {
        mTvTabText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 获取点击的pos
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 设置点击的pos
     */
    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 设置文字颜色
     *
     * @param normalColor 正常的颜色
     * @param focusColor  选中的颜色
     */
    public void setTitleColor(int normalColor, int focusColor) {
        this.normalColor = normalColor;
        this.focusColor = focusColor;
    }

    /**
     * 红点提示的显示与隐藏
     */
    public void redDotVisibility(boolean visible) {
        this.mIvRedDot.setVisibility(visible ? VISIBLE : GONE);
        if (visible) {
            mTvHint.setVisibility(GONE);
        }
    }

    /**
     * 提示文本可见性
     */
    public void hintTextVisibility(boolean visible, String text) {
        boolean show = visible && !TextUtils.isEmpty(text);
        mTvHint.setVisibility(show ? VISIBLE : GONE);
        mTvHint.setText(text);
        if (show) {
            mIvRedDot.setVisibility(GONE);
        }
    }

    /**
     * 更新红点里面的数字
     */
    public void updateRedNumber(int number) {
        if (number > 0) {
            mTvRedNumber.setText(number > 99 ? "99+" : number + "");
            mTvRedNumber.setVisibility(VISIBLE);
        } else {
            mTvRedNumber.setVisibility(GONE);
        }
    }

    public static class Builder {
        private QLNavigationTabView qlNavigationTabView;
        private Drawable focusRes;
        private Drawable normalRes;
        private String title;
        private boolean isDefault = false;
        private int position;
        private int normalColor;
        private int focusColor;
        private float textSize = 0;
        private String lottieFileName;
        private String lottieJsonString;

        /**
         * 设置tab背景资源
         */
        Builder setTabBgRes(Drawable focusRes, Drawable normalRes) {
            this.focusRes = focusRes;
            this.normalRes = normalRes;
            return this;
        }

        /**
         * 是否默认选中
         */
        Builder isSelect(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        /**
         * 设置tab文字
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置title 颜色
         */
        Builder setTitleColor(int normalColor, int focusColor) {
            this.normalColor = normalColor;
            this.focusColor = focusColor;
            return this;
        }

        /**
         * 设置tab位置
         */
        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        /**
         * 设置title大小
         */
        public Builder setTextSize(float size) {
            this.textSize = size;
            return this;
        }

        public Builder setAnimation(String lottieFileName) {
            this.lottieFileName = lottieFileName;
            return this;
        }

        public Builder setAnimationFromJson(String jsonString) {
            this.lottieJsonString = jsonString;
            return this;
        }

        /**
         * 使用建造者模式构造QlTabView
         */
        public QLNavigationTabView build(Context context) {
            qlNavigationTabView = new QLNavigationTabView(context);
            qlNavigationTabView.initTabView(normalRes, focusRes);
            qlNavigationTabView.setTabTitle(title);
            qlNavigationTabView.setPosition(position);
            if (!TextUtils.isEmpty(lottieFileName)) {
                qlNavigationTabView.setAnimation(lottieFileName);
            } else if (!TextUtils.isEmpty(lottieJsonString)) {
                qlNavigationTabView.setAnimationFromJson(lottieJsonString);
            }
            qlNavigationTabView.setTitleColor(normalColor, focusColor);
            if (textSize != 0) {
                qlNavigationTabView.setTextSize(textSize);
            }
            qlNavigationTabView.isSelect(isDefault);
            return qlNavigationTabView;
        }
    }
}
