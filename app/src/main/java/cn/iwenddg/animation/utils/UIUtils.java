package cn.iwenddg.animation.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.lang.reflect.Method;

import cn.iwenddg.animation.NavApplication;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author iwen大大怪
 * @create 2021/11/04 0:49
 */
public class UIUtils {
    public static final int DESIGN_WIDTH = 360;
    public static final int DESIGN_HEIGHT = 640;

    private static int width;
    private static int height;
    private static volatile long mLastClickTime;
    private static float mBestScaledBaseOnWidth;
    private static float mBestDensityBaseOnWidth;

    public static void init(Context context) {
        Resources res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    public static int getScreenWidth() {
        return width;
    }

    public static int getScreenHeight() {
        return height;
    }

    //    public static int dip2px(float dipValue) {
//        float density = getContext().getResources().getDisplayMetrics().density;
//        return (int) (dipValue * density + 0.5f);
//    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 在执行动画时使用这个dip转换px可以兼容宽为1080px但屏幕密度不为3的机型
     */
    public static int dip2pxByCompatDensity(Context context,float dipValue) {
        float density = getCompatDensity(context);
        return (int) (dipValue * density + 0.5f);
    }

    public static int px2dip(float pxValue) {
        Context context = NavApplication.getContext();
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    public static int sp2px(float spValue) {
        Context context = NavApplication.getContext();
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.heightPixels;
    }

    public static int getRawScreenHeight(Context context) {
        int dpi = 0;
        if (context == null) {
            return dpi;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, displayMetrics);
                dpi = displayMetrics.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dpi;
    }

    public static float getTextWidth(String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        return paint.measureText(text);
    }

    public static float getTextHeight(String text) {
        Paint pFont = new Paint();
        Rect rect = new Rect();
        pFont.getTextBounds(text, 0, 1, rect);
        return rect.height();
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return totalHeight;
    }

    /**
     * 上下文的获取
     */
    public static Context getContext() {
        return NavApplication.getContext();
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String getString(int resId, Object... obj) {
        return getResources().getString(resId, obj);
    }

    /**
     * 获取Drawable
     */
    public static Drawable getDrawable(Context context,int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    public static float getCompatDensity(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (getScreenWidth() == 1080) {
            density = 3;
        } else if (getScreenWidth() == 1176) {
            //mate 30 pro分辨率
            density = 4;
        } else if (getScreenWidth() == 1440) {
            density = 4;
        }

        return density;
    }

    public static float getDensity() {
        return getContext().getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity() {
        float density = getContext().getResources().getDisplayMetrics().scaledDensity;
//        if (getScreenWidth() == 1080) {
//            density = 3;
//        }
        return density;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取虚拟按键的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        if (context != null) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
            if (resourceId != 0) {
                boolean hasNav = res.getBoolean(resourceId);
                // check override flag
                String sNavBarOverride = getNavBarOverride();
                if ("1".equals(sNavBarOverride)) {
                    hasNav = false;
                } else if ("0".equals(sNavBarOverride)) {
                    hasNav = true;
                }
                return hasNav;
            } else { // fallback
                return !ViewConfiguration.get(context).hasPermanentMenuKey();
            }
        }
        return false;
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    public static boolean isMultiClick() {
        boolean isMultiClick = false;
        if (SystemClock.elapsedRealtime() - mLastClickTime < 700) {
            isMultiClick = true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return isMultiClick;
    }

    public static Drawable createDrawableFromAssets(Context context, String url) {
        AssetManager assets = context.getAssets();
        try {
            InputStream is = assets.open(url.replace("asset:/", ""));
            return BitmapDrawable.createFromStream(is, null);
        } catch (Throwable e) {
            //
        }
        return null;
    }

    public static Drawable createDrawableFromFile(String url) {
        return BitmapDrawable.createFromPath(url.replace("file://", ""));
    }

    public static Drawable createDrawable(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("file:/")) {
                return createDrawableFromFile(url);
            } else if (url.startsWith("asset:/")) {
                return createDrawableFromAssets(context, url);
            }
        }
        return null;
    }

    public static Disposable setImageWithDrawable(ImageView imageView, int resDrawable) {
        Context context = imageView.getContext();
        // Be sure the imageView is attached into BaseActivity
        AppCompatActivity base = (AppCompatActivity) context;

        return Observable.fromCallable(() ->
                getResources().getDrawable(resDrawable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(LifecycleUtils.bindLifecycle(base))
                .subscribe(drawable -> {
                    if (imageView != null) {
                        if (drawable != null) {
                            imageView.setImageDrawable(drawable);
                        }
                    }
                }, throwable -> {

                });
    }

//    public static Disposable setTextViewDrawable(TextView textView, int resDrawable) {
//        Context context = textView.getContext();
//        // Be sure the textView is attached into BaseActivity
//        Activity base = (Activity) context;
//
//        return Observable.fromCallable(() ->
//                getResources().getDrawable(resDrawable))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(LifecycleUtils.bindLifecycle(base))
//                .subscribe(drawable -> {
//                    if (textView != null) {
//                        if (drawable != null) {
//                            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//                        }
//                    }
//                }, throwable -> {
//
//                });
//    }
//
//    public static Disposable setProgressDrawable(ProgressBar progressBar, int resDrawable) {
//        Context context = progressBar.getContext();
//        // Be sure the progressBar is attached into BaseActivity
//        BaseActivity base = (BaseActivity) context;
//
//        return Observable.fromCallable(() ->
//                getResources().getDrawable(resDrawable))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(LifecycleUtils.bindLifecycle(base))
//                .subscribe(drawable -> {
//                    if (progressBar != null) {
//                        if (drawable != null) {
//                            progressBar.setIndeterminateDrawable(drawable);
//                        }
//                    }
//                }, throwable -> {
//
//                });
//    }
//
//    public static Disposable setImageBackground(View imageView, int resDrawable) {
//        Context context = imageView.getContext();
//        // Be sure the imageView is attached into BaseActivity
//        BaseActivity base = (BaseActivity) context;
//
//        return Observable.fromCallable(() ->
//                getResources().getDrawable(resDrawable))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(LifecycleUtils.bindLifecycle(base))
//                .subscribe(drawable -> {
//                    if (imageView != null) {
//                        if (drawable != null) {
//                            imageView.setBackground(drawable);
//                        }
//                    }
//                }, throwable -> {
//
//                });
//    }

    public static float getScaledSizeBaseOnDesignWidth(float designSizeInDp) {
        if (mBestScaledBaseOnWidth == 0) {
            Resources resources = NavApplication.getContext().getResources();
            if (resources != null) {
                float density = resources.getDisplayMetrics().density;
                mBestScaledBaseOnWidth = resources.getDisplayMetrics().widthPixels * 1.0f / (DESIGN_WIDTH * density);
            }
        }
        return designSizeInDp * (mBestScaledBaseOnWidth == 0 ? 1 : mBestScaledBaseOnWidth);
    }

    public static float getBestDensityBaseOnDesignWidth(float designSizeInDp) {
        if (mBestDensityBaseOnWidth == 0) {
            Resources resources = NavApplication.getContext().getResources();
            if (resources != null) {
                mBestDensityBaseOnWidth = resources.getDisplayMetrics().widthPixels * 1.0f / DESIGN_WIDTH;
            }
        }
        return designSizeInDp * (mBestDensityBaseOnWidth == 0 ? 1 : mBestDensityBaseOnWidth);
    }

    public static float getPixelSizeBaseOnDesignWidth(float designSize) {
        Resources resources = NavApplication.getContext().getResources();
        if (resources != null) {
            float density = resources.getDisplayMetrics().density;
            return (designSize * density) * resources.getDisplayMetrics().widthPixels * 1.0f / (DESIGN_WIDTH * density);
        }
        return designSize;
    }

    public static <T> T onConvertView(View view, int viewID) {
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(viewID);
    }

    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    public static Drawable getShape(String color, int radius) {
        int fillColor = Color.parseColor(color);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(radius);
        return gd;
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * dp转换成px
     *
     * @param context Context
     * @param dp      dp
     * @return px值
     */
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

}