package cn.iwenddg.animation.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwenddg.animation.R;
import cn.iwenddg.animation.fragment.CommunityFragment;
import cn.iwenddg.animation.fragment.HomeFragment;
import cn.iwenddg.animation.fragment.IMListFragment;
import cn.iwenddg.animation.fragment.MineFragment;
import cn.iwenddg.animation.fragment.ReactServiceFragment;
import cn.iwenddg.animation.fragment.SupportFragment;
import cn.iwenddg.animation.ui.QLNavigationTabView;
import cn.iwenddg.animation.ui.QlTabContainerView;
import cn.iwenddg.animation.utils.TranStatusUtil;
import cn.iwenddg.animation.utils.UIUtils;


public class MainActivity extends SupportActivity {

    private static final String TAG = "MainActivity";

    public static final int DISCOVER_TAB = 0;
    public static final int IM_TAB = 1;
    public static final int CAR_CONTROL_TAB = 2;
    public static final int SERVICE_TAB = 3;
    public static final int MINE_TAB = 4;
    // 底部导航的数量
    private static final int TAB_COUNT = 5;

    // 内容填充区
    @BindView(R.id.container_view)
    FrameLayout mContainerView;
    // 底部导航区
    @BindView(R.id.tab_container_view)
    QlTabContainerView mQlTabContainerView;
    // 动画
    @BindView(R.id.tab_extras)
    LinearLayout mTabExtraView;

    // 保存fragment的数组
    private SupportFragment[] mFragments = new SupportFragment[TAB_COUNT];


    public int mLastPos = 0;

    public int mNextPos = 0;

    private List<Class<? extends SupportFragment>> mFragmentList =
            Arrays.asList(
                    CommunityFragment.class,
                    IMListFragment.class,
                    HomeFragment.class,
                    ReactServiceFragment.class,
                    MineFragment.class);

    private String[] mExtraAnimations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWindow();
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate: ");
        initView();
    }

    private void initWindow() {
        getWindow().setBackgroundDrawable(UIUtils.getDrawable(this, R.drawable.btn_drawable_00000000));
    }


    private void initView() {
        View divider = findViewById(R.id.divider);
        mQlTabContainerView.setDividerView(divider);
        mQlTabContainerView.setTabClickListener(view -> {
            if (view instanceof QLNavigationTabView) {
                int pos = ((QLNavigationTabView) view).getPosition();
                // 双击刷新Tab
                if (pos == DISCOVER_TAB && pos == mLastPos) {
                    // TODO 双击刷新
                    Toast.makeText(this, "社区触发双击刷新啦...", Toast.LENGTH_SHORT).show();
                } else if (pos == IM_TAB && pos == mLastPos) {
                    // TODO 双击刷新
                    Toast.makeText(this, "消息触发双击刷新啦...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQlTabContainerView.setTabChangeListener(showPos -> {
            Log.i(TAG, "currentTab = " + mLastPos + ", changeToTab = " + showPos);
            mNextPos = showPos;
            // 显示隐藏Fragment
            showHideFragment(showPos);
            // showExtraAnimation
            showExtraAnimation(showPos);
            return true;
        });


        QlTabContainerView.OnTabReadyListener listener = loadedTheme -> {
            adaptChangeToTab();
            if (loadedTheme != null) {
                mExtraAnimations = new String[]{
                        loadedTheme.tab1.extraAnimJson,
                        loadedTheme.tab2.extraAnimJson,
                        loadedTheme.tab3.extraAnimJson,
                        loadedTheme.tab4.extraAnimJson,
                        loadedTheme.tab5.extraAnimJson,
                };

                for (int pos = 0, l = mExtraAnimations.length; pos < l; pos++) {
                    if (!TextUtils.isEmpty(mExtraAnimations[pos])) {
                        LottieAnimationView lottieView = (LottieAnimationView) mTabExtraView.getChildAt(pos);
                        lottieView.setAnimationFromJson(mExtraAnimations[pos]);
                        lottieView.addAnimatorUpdateListener(animation -> {
                            if (animation.getAnimatedFraction() == 1f) {
                                lottieView.setVisibility(View.INVISIBLE);
                            }
                        });
                        lottieView.setRepeatCount(0);
                    }
                }
            }
        };

        // 使用默认的tab
        mQlTabContainerView.createDefaultMainTab(MainActivity.this, listener);

        mContainerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mContainerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }


    private void adaptChangeToTab() {
        changeToTab(mLastPos);
    }

    public void changeToTab(int pos) {
        if (mQlTabContainerView != null) {
            mQlTabContainerView.changeToTab(pos);
        }
    }

    /**
     * 根据下标显示和隐藏fragment
     *
     * @param showPos 下标
     */
    private synchronized void showHideFragment(int showPos) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Class<? extends SupportFragment> clazz = mFragmentList.get(showPos);
        Log.i(TAG, "showPos = " + clazz.getSimpleName());
        try {
            SupportFragment fragment = mFragments[showPos];
            if (fragment == null) {
                fragment = findFragment(clazz);
            }
            if (fragment == null) {
                mFragments[showPos] = fragment = clazz.newInstance();
                fragmentTransaction.add(R.id.container_view, mFragments[showPos], String.valueOf(showPos));
                fragmentTransaction.commitAllowingStateLoss();
                Log.i(TAG, "new fragment " + fragment);
            } else {
                mFragments[showPos] = fragment;
            }
            showFragment(showPos);
            setFragmentStatusBar(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "MainActivity e == " + e.toString());
        }
        Log.i(TAG, "showPos = " + showPos);
    }

    /**
     * 设置Fragment的状态栏
     *
     * @param fragment Fragment
     */
    private void setFragmentStatusBar(Fragment fragment) {
        Window window = getWindow();
        if (window != null) {
            if (fragment instanceof SupportFragment) {
                SupportFragment supportFragment = (SupportFragment) fragment;
                TranStatusUtil.darkMode(window, supportFragment.isDarkStatusText());
            } else {
                TranStatusUtil.darkMode(window, true);
            }

            if (Build.VERSION.SDK_INT <= 21) {
                TranStatusUtil.setStatusBarColor(this, R.color.bg_black_alpha_20);
            } else {
                TranStatusUtil.setStatusBarColor(this, R.color.transparent);
            }
        }

    }

    /**
     * 根据showPos显示对应的fragment
     *
     * @param showPos int
     */
    private void showFragment(int showPos) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.length; i++) {
            Fragment fragment = mFragments[i];
            if (fragment != null && i != showPos && !fragment.isHidden()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragmentTransaction.show(mFragments[showPos]);
        mLastPos = showPos;
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 显示额外的动画
     *
     * @param pos 下标
     */
    private void showExtraAnimation(int pos) {
        if (mExtraAnimations != null && mExtraAnimations.length > pos) {
            if (!TextUtils.isEmpty(mExtraAnimations[pos])) {
                LottieAnimationView lottieView = (LottieAnimationView) mTabExtraView.getChildAt(pos);
                lottieView.setVisibility(View.VISIBLE);
                lottieView.playAnimation();
            }
        }
    }

    public QlTabContainerView getTabContainerView() {
        return mQlTabContainerView;
    }
}