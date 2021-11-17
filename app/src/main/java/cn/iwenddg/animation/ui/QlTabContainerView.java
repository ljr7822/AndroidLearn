package cn.iwenddg.animation.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

import cn.iwenddg.animation.R;
import cn.iwenddg.animation.bean.MainTabTheme;
import cn.iwenddg.animation.utils.LifecycleUtils;
import cn.iwenddg.animation.utils.QlAnimatorManager;
import cn.iwenddg.animation.utils.UIUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

/**
 * @author iwen大大怪
 * @create 2021/11/04 0:43
 */
public class QlTabContainerView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "QlTabContainerView";

    public final static String[] DEFAULT_TAB_NAME = {"社区", "消息", "联系人", "群组", "我"};

    private final static String[] DEFAULT_LOTTIE_FILES =
            {"tabs/community.json", "tabs/im.json", "tabs/addressbook.json", "tabs/service.json", "tabs/mine.json"};

    private View mDivider;
    private float mTextSize;
    private int mDefaultColor;
    private int mSelectColor;
    private int mCurrentPos = 0;
    private List<QLNavigationTabView> mQlNavigationTabViews = new ArrayList<>();
    // tab退出动画
    private ObjectAnimator mBottomTabExitAnimator;
    // tab进入动画
    private ObjectAnimator mBottomTabEnterAnimator;
    private OnClickListener mTabClickListener;
    private boolean isFirstEnter = true;

    private Drawable[] mDefaultDrawables;


    public QlTabContainerView(Context context) {
        this(context, null);
    }

    public QlTabContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

        //防止点击底部tab的事件穿透到卡片View
        setOnClickListener(this);
    }

    /**
     * 读取属性
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QlTabContainerView);
            mTextSize = ta.getDimension(R.styleable.QlTabContainerView_tab_textSize, UIUtils.dip2px( getContext(),10));
            mTextSize = mTextSize / UIUtils.getCompatDensity(getContext());
            mDefaultColor = ta.getColor(R.styleable.QlTabContainerView_tab_defaultColor, 0xffB4BBC7);
            mSelectColor = ta.getColor(R.styleable.QlTabContainerView_tab_selectColor, 0xff32ADEA);
            ta.recycle();
        }
    }

    /**
     * 添加tab
     */
    public QlTabContainerView addTab(boolean isSelect, Drawable tabNormal, Drawable tabFocus, String lottieFileName, String title) {
        QLNavigationTabView qlNavigationTabView = new QLNavigationTabView.Builder()
                .setTabBgRes(tabFocus, tabNormal)
                .setAnimation(lottieFileName)
                .setTitleColor(mDefaultColor, mSelectColor)
                .setTextSize(mTextSize)
                .setTitle(title)
                .isSelect(isSelect)
                .setPosition(getChildCount())
                .build(getContext());
        return addTab(qlNavigationTabView);
    }

    public QlTabContainerView addTab(QLNavigationTabView qlNavigationTabView) {
        addView(qlNavigationTabView);
        LayoutParams lp = (LayoutParams) qlNavigationTabView.getLayoutParams();
        lp.width = 0;
        lp.weight = 1;
        qlNavigationTabView.setLayoutParams(lp);
        mQlNavigationTabViews.add(qlNavigationTabView);
        addTabOnClickListener(qlNavigationTabView);
        return this;
    }

    /**
     * tab item点击事件,点击时直接跳到对应view
     *
     * @param view View
     */
    private void addTabOnClickListener(final QLNavigationTabView view) {
        OnClickListener listener = v -> {
            int pos = view.getPosition();
            if (mTabClickListener != null) {
                mTabClickListener.onClick(view);
            }
            changeToTab(pos);
        };
        view.setOnClickListener(listener);
    }

    /**
     * 设置tab点击监听器
     *
     * @param listener View.OnClickListener
     */
    public void setTabClickListener(View.OnClickListener listener) {
        mTabClickListener = listener;
    }

    public void changeToTab(int index) {
        if (mCurrentPos != index || isFirstEnter) {
            isFirstEnter = false;
            Log.i(TAG, "changeToTab pos  == " + index);
            if (!mQlNavigationTabViews.isEmpty()) {
                Log.i(TAG, "changeToTab onTabChange  == " + index);
                if (mOnTabChangeListener.onTabChange(index)) {
                    mQlNavigationTabViews.get(index).isSelect(true);
                    setSelectTab(index);
                    //XpDataAgentHelper.onTabChange(index);
                }
            } else {
                Log.i(TAG, "mHomeTabViews size  == " + mQlNavigationTabViews.size());
            }
        }
    }

    public boolean isTabViewsEmpty() {
        return mQlNavigationTabViews == null || mQlNavigationTabViews.isEmpty();
    }

    /**
     * 设置是否启用标签
     */
    public void setTabsEnable(boolean enable) {
        for (int i = 0; i < mQlNavigationTabViews.size(); i++) {
            QLNavigationTabView tab = mQlNavigationTabViews.get(i);
            if (tab != null) {
                tab.setEnabled(enable);
            }
        }
    }

    /**
     * 设置选中的标签
     */
    public void setSelectTab(int index) {
        mQlNavigationTabViews.get(index).isSelect(true);
        if (mCurrentPos != index) {
            mQlNavigationTabViews.get(mCurrentPos).isSelect(false);
        }
        mCurrentPos = index;
    }

    /**
     * 对应标签上是否显示红点
     *
     * @param pos     标签pos
     * @param visible 是否显示
     */
    public void redDotVisibility(int pos, boolean visible) {
        if (pos < 0 || pos >= mQlNavigationTabViews.size()) {
            return;
        }
        mQlNavigationTabViews.get(pos).redDotVisibility(visible);
    }

    public void hintText(int pos, boolean visibile, String text) {
        if (pos < 0 || pos >= mQlNavigationTabViews.size()) {
            return;
        }
        mQlNavigationTabViews.get(pos).hintTextVisibility(visibile, text);
    }

    public void updateRedNumber(int pos, int number) {
        if (pos < 0 || pos >= mQlNavigationTabViews.size()) {
            return;
        }
        mQlNavigationTabViews.get(pos).updateRedNumber(number);
    }

    private OnTabChangeListener mOnTabChangeListener;

    /**
     * tab切换监听
     */
    public void setTabChangeListener(OnTabChangeListener tabChangeListener) {
        mOnTabChangeListener = tabChangeListener;
    }

    /**
     * 设置分割线
     *
     * @param divider View
     */
    public void setDividerView(View divider) {
        mDivider = divider;
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 使用默认tab
     *
     * @param context
     * @param listener
     */
    public void createDefaultMainTab(Context context,  OnTabReadyListener listener) {
        Log.i(TAG, "createDefaultMainTab");
        loadDefaultRes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(LifecycleUtils.bindContext(context))
                .subscribe(drawables -> {
                            addTab(true, drawables[0], drawables[1], DEFAULT_LOTTIE_FILES[0], DEFAULT_TAB_NAME[0])
                            .addTab(false, drawables[2], drawables[3], DEFAULT_LOTTIE_FILES[1], DEFAULT_TAB_NAME[1])
                            .addTab(false, drawables[4], drawables[5], DEFAULT_LOTTIE_FILES[2], DEFAULT_TAB_NAME[2])
                            .addTab(false, drawables[6], drawables[7], DEFAULT_LOTTIE_FILES[3], DEFAULT_TAB_NAME[3])
                            .addTab(false, drawables[8], drawables[9], DEFAULT_LOTTIE_FILES[4], DEFAULT_TAB_NAME[4]);
                    if (listener != null) {
                        listener.onReady(null);
                    }
                }, Functions.emptyConsumer());

    }

    public interface OnTabChangeListener {
        /**
         * @param showPos 要显示tab
         */
        boolean onTabChange(int showPos);
    }

    public interface OnTabReadyListener {
        void onReady(MainTabTheme loadedTheme);
    }

    /**
     * 加载资源到Drawable数组中
     *
     * @return Observable
     */
    private Observable<Drawable[]> loadDefaultRes() {
        Log.i(TAG,"loadDefaultRes");
        if (mDefaultDrawables != null) {
            return Observable.just(mDefaultDrawables);
        }
        return Observable.fromCallable(() -> {
            mDefaultDrawables = new Drawable[10];
            mDefaultDrawables[0] = getResources().getDrawable(R.drawable.ico_community_normal);
            mDefaultDrawables[1] = getResources().getDrawable(R.drawable.ico_community_select);
            mDefaultDrawables[2] = getResources().getDrawable(R.drawable.ico_im_normal);
            mDefaultDrawables[3] = getResources().getDrawable(R.drawable.ico_im_select);
            mDefaultDrawables[4] = getResources().getDrawable(R.drawable.ico_home_normal);
            mDefaultDrawables[5] = getResources().getDrawable(R.drawable.ico_home_select);
            mDefaultDrawables[6] = getResources().getDrawable(R.drawable.ico_service_normal);
            mDefaultDrawables[7] = getResources().getDrawable(R.drawable.ico_service_select);
            mDefaultDrawables[8] = getResources().getDrawable(R.drawable.ico_mine_normal);
            mDefaultDrawables[9] = getResources().getDrawable(R.drawable.ico_mine_select);
            return mDefaultDrawables;
        });
    }

    // 是否正在动画
    private boolean isAniming;
    // 是否正在显示
    private boolean isDisplaying = true;

    /**
     * 退出底部tab动画
     *
     * @param duration 动画的长度
     */
    public void exitBottomTab(int duration) {
        if (!isAniming && isDisplaying) {
            measure(0, 0);
            if (mBottomTabExitAnimator == null) {
                mBottomTabExitAnimator = QlAnimatorManager.getTranslateAnimator(
                        this, 0, getMeasuredHeight(), duration);
            }

            mBottomTabExitAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAniming = true;
                    mDivider.setVisibility(View.GONE);
                    QlTabContainerView.this.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAniming = false;
                    isDisplaying = false;
                    QlTabContainerView.this.setVisibility(View.GONE);
                }
            });
            mBottomTabExitAnimator.start();
        }
    }

    /**
     * 进入底部tab
     *
     * @param duration 动画的长度
     */
    public void enterBottomTab(int duration) {
        if (!isAniming && !isDisplaying) {
            // 调用 view.measure()，再通过 getMeasuredWidth 和 getMeasuredHeight 获取宽高。
            measure(0, 0);
            mBottomTabEnterAnimator = QlAnimatorManager.getTranslateAnimator(
                    this, getMeasuredHeight(), 0, duration);
            // 添加动画监听器
            mBottomTabEnterAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAniming = true;
                    QlTabContainerView.this.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAniming = false;
                    isDisplaying = true;
                    mDivider.setVisibility(View.VISIBLE);
                }
            });
            mBottomTabEnterAnimator.start();
        }
    }
}
