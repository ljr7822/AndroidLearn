package cn.iwenddg.animation.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;

/**
 * @author iwen大大怪
 * @create 2021/11/05 15:33
 */
public class TranStatusUtil {
    private static final String TAG = "TranStatusUtil";
    public static void setStatusBarDraws(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    public static void setStatusBarColor(Context context, @ColorRes int... color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (color.length > 0) {
                ((Activity) context).getWindow().setStatusBarColor(context.getResources().getColor(color[0]));
            }
        } else {
            try{
                SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
                // enable status bar tint
                tintManager.setStatusBarTintEnabled(true);
                // enable navigation bar tint
                tintManager.setNavigationBarTintEnabled(false);
                if (color.length > 0) {
                    tintManager.setTintColor(context.getResources().getColor(color[0]));
                }
            }catch (Throwable t){
                Log.e(TAG,"setStatusBarColor t == "+t.toString());
            }
        }
    }

    public static void setStatusBarIntColor(Context context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            ((Activity) context).getWindow().setStatusBarColor(color);

        } else {
            SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(color);
        }
    }

    public static void darkMode(Window window, boolean dark) {
        LightStatusBarCompat.setLightStatusBar(window, dark);
    }
}
